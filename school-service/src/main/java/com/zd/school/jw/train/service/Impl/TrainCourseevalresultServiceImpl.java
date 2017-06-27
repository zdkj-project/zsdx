package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainCourseevalresultDao;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainCourseevalresult;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainCourseevalresultService;
import com.zd.school.jw.train.service.TrainIndicatorStandService;
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
 *
 * ClassName: TrainCourseevalresultServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程评价结果(TRAIN_T_COURSEEVALRESULT)实体Service接口实现类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainCourseevalresultServiceImpl extends BaseServiceImpl<TrainCourseevalresult> implements TrainCourseevalresultService{

    @Resource
    public void setTrainCourseevalresultDao(TrainCourseevalresultDao dao) {
        this.dao = dao;
    }
    @Resource
    private TrainIndicatorStandService standService;
    @Resource
    private TrainClassscheduleService scheduleService;

    private static Logger logger = Logger.getLogger(TrainCourseevalresultServiceImpl.class);

    @Override
    public QueryResult<TrainCourseevalresult> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainCourseevalresult> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
     * @param entity
     *            传入的要更新的实体对象
     * @param currentUser
     *            当前操作用户
     * @return
     */
    @Override
    public TrainCourseevalresult doUpdateEntity(TrainCourseevalresult entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainCourseevalresult saveEntity = this.get(entity.getUuid());
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
     * @param entity
     *            传入的要更新的实体对象
     * @param currentUser
     *            当前操作用户
     * @return
     */
    @Override
    public TrainCourseevalresult doAddEntity(TrainCourseevalresult entity, SysUser currentUser) {
        TrainCourseevalresult saveEntity = new TrainCourseevalresult();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyProperties(saveEntity, entity,excludedProp);
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

    @Override
    public Boolean doStartCourseEval(String ids) {
        TrainClassschedule trainClassschedule = scheduleService.get(ids);
        List<TrainIndicatorStand> indicatorStands = standService.getCourseEvalStand();
        TrainCourseevalresult courseEvalStand = null;
        for (TrainIndicatorStand inStand : indicatorStands) {
            courseEvalStand = new TrainCourseevalresult();
            courseEvalStand.setClassId(trainClassschedule.getClassId());
            courseEvalStand.setClassScheduleId(trainClassschedule.getUuid());
            courseEvalStand.setCourseId(trainClassschedule.getCourseId());
            courseEvalStand.setCourseName(trainClassschedule.getCourseName());
            courseEvalStand.setIndicatorId(inStand.getIndicatorId());
            courseEvalStand.setIndicatorName(inStand.getIndicatorName());
            courseEvalStand.setStandId(inStand.getUuid());
            courseEvalStand.setIndicatorStand(inStand.getIndicatorStand());
            courseEvalStand.setAdvise("");
            courseEvalStand.setBasSatisfactioncount(0);
            courseEvalStand.setNoSatisfactioncount(0);
            courseEvalStand.setVerySatisfactioncount(0);
            courseEvalStand.setSatisfactioncount(0);
            courseEvalStand.setVerySatisfaction(BigDecimal.valueOf(0));
            courseEvalStand.setSatisfaction(BigDecimal.valueOf(0));

            this.merge(courseEvalStand);
        }
        scheduleService.updateByProperties("uuid",ids,"evalState",1);
        return true;
    }

    @Override
    public Boolean doSumCourseEval(String ids) {
        String sql = MessageFormat.format("EXECUTE TRAIN_P_SUMCLASSCOURSEEVAL ''{0}''", ids);
        List<?> alist = this.doQuerySql(sql);

        return true;
    }

    @Override
    public Boolean doEndCourseEval(String ids, SysUser currentUser) {
        scheduleService.updateByProperties("uuid",ids,"evalState",2);
        return  true;

    }
}