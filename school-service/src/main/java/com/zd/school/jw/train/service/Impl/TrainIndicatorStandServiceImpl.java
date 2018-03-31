package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainIndicatorStandDao;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.service.TrainIndicatorStandService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luoyibo on 2017-06-18.
 */
@Service
@Transactional
public class TrainIndicatorStandServiceImpl extends BaseServiceImpl<TrainIndicatorStand> implements TrainIndicatorStandService {
    @Resource
    public void setTrainIndicatorStandDao(TrainIndicatorStandDao dao) {
        this.dao = dao;
    }

    private static Logger logger = Logger.getLogger(TrainEvalIndicatorServiceImpl.class);

    @Override
    public QueryResult<TrainIndicatorStand> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainIndicatorStand> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }
    /**
     * 根据主键逻辑删除数据
     *
     * @param ids
     *            要删除数据的主键
     * @param currentUser
     *            当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    @Override
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
        Boolean delResult = false;
        try {
            Object[] conditionValue = ids.split(",");
            String[] propertyName = { "isDelete", "updateUser", "updateTime" };
            Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
            this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
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
     * @param entity
     *            传入的要更新的实体对象
     * @param currentUser
     *            当前操作用户
     * @return
     */
    @Override
    public TrainIndicatorStand doUpdateEntity(TrainIndicatorStand entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainIndicatorStand saveEntity = this.get(entity.getUuid());
        try {
            BeanUtils.copyPropertiesExceptNull(saveEntity, entity);
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.doMerge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param entity
     *            传入的要更新的实体对象
     * @param currentUser
     *            当前操作用户
     * @return
     */
    @Override
    public TrainIndicatorStand doAddEntity(TrainIndicatorStand entity, SysUser currentUser) {
        TrainIndicatorStand saveEntity = new TrainIndicatorStand();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyPropertiesExceptNull(saveEntity, entity,excludedProp);
            saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.doMerge(saveEntity);// 执行修改方法

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
    public List<TrainIndicatorStand> getCourseEvalStand() {
        String hql = " from TrainIndicatorStand where indicatorObject=1 or indicatorObject=3 order by indicatorId";
        List<TrainIndicatorStand> list = this.getQuery(hql);
        return  list;
    }

    @Override
    public List<TrainIndicatorStand> getClassEvalStand() {
        String hql = " from TrainIndicatorStand where indicatorObject=2 or indicatorObject=3 order by indicatorId ";
        List<TrainIndicatorStand> list = this.getQuery(hql);
        return  list;
    }
}
