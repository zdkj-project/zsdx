package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainClassevalresultDao;
import com.zd.school.jw.train.model.TrainClassevalresult;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.service.*;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: TrainClassevalresultServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description:  班级评价结果(TRAIN_T_CLASSEVALRESULT)实体Service接口实现类.
 * date: 2017-06-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainClassevalresultServiceImpl extends BaseServiceImpl<TrainClassevalresult> implements TrainClassevalresultService {
    @Resource
    public void setTrainClassevalresultDao(TrainClassevalresultDao dao) {
        this.dao = dao;
    }

    private static Logger logger = Logger.getLogger(TrainClassevalresultServiceImpl.class);

    @Resource
    private TrainIndicatorStandService standService;
    @Resource
    private TrainClassService classService;
    @Resource
    private TrainClassscheduleService scheduleService;
    @Resource
    private TrainCourseevalresultService courseevalresultService;

    public QueryResult<TrainClassevalresult> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainClassevalresult> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
    public TrainClassevalresult doUpdateEntity(TrainClassevalresult entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainClassevalresult saveEntity = this.get(entity.getUuid());
        try {
            BeanUtils.copyProperties(saveEntity, entity);
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

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
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainClassevalresult doAddEntity(TrainClassevalresult entity, SysUser currentUser) {
        TrainClassevalresult saveEntity = new TrainClassevalresult();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyProperties(saveEntity, entity, excludedProp);
            saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public Boolean doSumClassEval(String ids) {
        String sql = MessageFormat.format("EXECUTE TRAIN_P_SUMCLASSEVAL ''{0}''",ids);
        List<?> alist = this.doQuerySql(sql);
        return true;
    }

    @Override
    public Boolean doStartClassEval(String ids) throws InvocationTargetException, IllegalAccessException {
        List<TrainIndicatorStand> indicatorStands = standService.getClassEvalStand();
        TrainClassevalresult classEvalStand = null;
        for (TrainIndicatorStand inStand : indicatorStands) {
            classEvalStand = new TrainClassevalresult();
            classEvalStand.setClassId(ids);
            classEvalStand.setIndicatorId(inStand.getIndicatorId());
            classEvalStand.setIndicatorName(inStand.getIndicatorName());
            classEvalStand.setStandId(inStand.getUuid());
            classEvalStand.setIndicatorStand(inStand.getIndicatorStand());
            classEvalStand.setAdvise("");
            classEvalStand.setBasSatisfactioncount(0);
            classEvalStand.setNoSatisfactioncount(0);
            classEvalStand.setVerySatisfactioncount(0);
            classEvalStand.setSatisfactioncount(0);
            classEvalStand.setVerySatisfaction(BigDecimal.valueOf(0));
            classEvalStand.setSatisfaction(BigDecimal.valueOf(0));

            this.merge(classEvalStand);
        }
        classService.updateByProperties("uuid",ids,"isEval",1);

        //同步启动班级下的需要评价课程
        String[] propName ={"classId","isEval"};
        Object[] propValue = {ids,1};
        List<TrainClassschedule> classCourse = scheduleService.queryByProerties(propName, propValue);
        for (TrainClassschedule trainClassschedule : classCourse) {
            courseevalresultService.doStartCourseEval(trainClassschedule.getUuid());
        }
        return true;
    }

    @Override
    public Boolean doEndStartClassEval(String ids,SysUser currentUser) {
        classService.updateByProperties("uuid",ids,"isEval",2);
        return true;
    }
}