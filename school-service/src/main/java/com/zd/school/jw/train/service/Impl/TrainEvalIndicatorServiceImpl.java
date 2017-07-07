package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainEvalIndicatorDao;
import com.zd.school.jw.train.model.TrainEvalIndicator;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.service.TrainEvalIndicatorService;
import com.zd.school.jw.train.service.TrainIndicatorStandService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by luoyibo on 2017-06-18.
 */
@Service
@Transactional
public class TrainEvalIndicatorServiceImpl extends BaseServiceImpl<TrainEvalIndicator> implements TrainEvalIndicatorService {
    @Resource
    public void setTrainEvalIndicatorDao(TrainEvalIndicatorDao dao) {
        this.dao = dao;
    }

    @Resource
    private TrainIndicatorStandService standService;
    private static Logger logger = Logger.getLogger(TrainEvalIndicatorServiceImpl.class);

    @Override
    public QueryResult<TrainEvalIndicator> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainEvalIndicator> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }

    /**
     * 根据主键逻辑删除数据
     *
     * @param ids         要删除数据的主键
     * @param currentUser 当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    @Override
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
        Boolean delResult = false;
        try {
            Object[] conditionValue = ids.split(",");
            String[] propertyName = {"isDelete", "updateUser", "updateTime"};
            Object[] propertyValue = {1, currentUser.getXm(), new Date()};
            this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
            standService.deleteByProperties("indicatorId", conditionValue);
            delResult = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            delResult = false;
        }
        return delResult;
    }

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainEvalIndicator doUpdateEntity(TrainEvalIndicator entity, List<TrainIndicatorStand> stands, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainEvalIndicator saveEntity = this.get(entity.getUuid());
        try {
            saveEntity.setIndicatorName(entity.getIndicatorName());
            saveEntity.setIndicatorObject(entity.getIndicatorObject());
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            //处理指标对应的评估标准
            String indicatorId = entity.getUuid();
            Map<String, TrainIndicatorStand> indicatorStandMap = new HashMap<>();
            List<TrainIndicatorStand> hasList = standService.queryByProerties("indicatorId", indicatorId);
            for (TrainIndicatorStand trainIndicatorStand : hasList) {
                indicatorStandMap.put(trainIndicatorStand.getUuid(), trainIndicatorStand);
            }
            TrainIndicatorStand saveStand = null;
            //提交的标准如果在数据库中有则修改，没有增加
            for (TrainIndicatorStand stand : stands) {
                if (indicatorStandMap.get(stand.getUuid()) != null) {
                    //数据库中已有此标准，更新
                    saveStand = indicatorStandMap.get(stand.getUuid());
                    saveStand.setIndicatorStand(stand.getIndicatorStand());
                    saveStand.setUpdateTime(new Date());
                    saveStand.setUpdateUser(currentUser.getXm());
                    standService.merge(saveStand);

                    //从已有列表中删除
                    hasList.remove(saveStand);
                } else {
                    doAddIndicatorStand(currentUser, indicatorId, stand);
                }
            }
            //要从数据库中清除的数据
            if (hasList.size() > 0) {
                for (TrainIndicatorStand trainIndicatorStand : hasList) {
                    standService.delete(trainIndicatorStand);
                }
            }
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private void doAddIndicatorStand(SysUser currentUser, String indicatorId, TrainIndicatorStand stand) {
        TrainIndicatorStand saveStand;
        saveStand = new TrainIndicatorStand();
        saveStand.setIndicatorId(indicatorId);
        saveStand.setIndicatorStand(stand.getIndicatorStand());
        saveStand.setCreateUser(currentUser.getXm());

        standService.merge(saveStand);
    }

    /**
     * 增加新的评估指标
     *
     * @param entity      评估指标实体
     * @param stands      指标评估标准实体列表
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainEvalIndicator doAddEntity(TrainEvalIndicator entity, List<TrainIndicatorStand> stands, SysUser currentUser) {
        TrainEvalIndicator saveEntity = new TrainEvalIndicator();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludedProp);
            saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            //添加指标对应的评估标准
            String indicatorId = entity.getUuid();
            TrainIndicatorStand saveStand = null;
            for (TrainIndicatorStand stand : stands) {
                doAddIndicatorStand(currentUser, indicatorId, stand);
            }
            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean doDelete(String ids, SysUser currentUser) {
        try {
            String hql = "delete from TrainIndicatorStand where uuid in (''{0}'')";
            hql = MessageFormat.format(hql,ids);
            this.executeHql(hql);

            String sql = "delete from TRAIN_T_EVALINDICATOR where (SELECT COUNT(*) FROM dbo.TRAIN_T_INDICATORSTAND WHERE INDICATOR_ID=TRAIN_T_EVALINDICATOR.INDICATOR_ID AND ISDELETE=0)=0";
            this.executeSql(sql);

            return  true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return  false;
        }

    }
}
