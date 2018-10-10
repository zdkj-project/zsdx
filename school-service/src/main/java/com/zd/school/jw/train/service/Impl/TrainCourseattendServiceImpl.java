package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainCourseattendDao;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainCourseattend;
import com.zd.school.jw.train.service.TrainCourseattendService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TrainCourseattendServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程考勤刷卡结果(TRAIN_T_COURSEATTEND)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainCourseattendServiceImpl extends BaseServiceImpl<TrainCourseattend> implements TrainCourseattendService {

    @Resource
    public void setTrainCourseattendDao(TrainCourseattendDao dao) {
        this.dao = dao;
    }

    private static Logger logger = Logger.getLogger(TrainCourseattendServiceImpl.class);

    @Override
    public QueryResult<TrainCourseattend> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainCourseattend> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }

    @Resource
    private TrainCourseattendService trainCourseattendService;

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
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCourseattend doUpdateEntity(TrainCourseattend entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainCourseattend saveEntity = this.get(entity.getUuid());
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
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCourseattend doAddEntity(TrainCourseattend entity, SysUser currentUser) {
        TrainCourseattend saveEntity = new TrainCourseattend();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyProperties(saveEntity, entity, excludedProp);
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
    public void installTrainCourseattend(List<Map<String, Object>> list, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception {
        Map<String, Object> map;
        TrainCourseattend trainCourseattend;
        SimpleDateFormat se = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            //正常签到
            trainCourseattend = new TrainCourseattend();
            trainCourseattend.setClassId(classId);
            trainCourseattend.setTraineeId(userId);
            trainCourseattend.setIncardTime(date);
            trainCourseattend.setBeginTime(se.parse(map.get("BEGIN_TIME").toString()));
            trainCourseattend.setEndTime(se.parse(map.get("END_TIME").toString()));
            trainCourseattend.setClassScheduleId(map.get("CLASS_SCHEDULE_ID").toString());
            trainCourseattend.setCreateUser("后台程序创建");

            //不需要签退
            if (0 == needCheckout) {
                if (date.before(se.parse(map.get("END_TIME").toString()))) {
                    trainCourseattend.setOutcardTime(se.parse(map.get("END_TIME").toString()));
                } else {
                    trainCourseattend.setOutcardTime(se.parse(list.get(list.size() - 1).get("END_TIME").toString()));
                }
                trainCourseattend.setAttendResult(attendResult);

                trainCourseattend.setAttendResult(attendResult);
            } else {
                //将现阶段考勤结果存入备用字段
                trainCourseattend.setExtField04(attendResult);
            }
            this.doMerge(trainCourseattend);
        }
    }

    @Override
    public void installTrainCourseattends(List<TrainClassschedule> list, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception {
        TrainCourseattend trainCourseattend;
        SimpleDateFormat se = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (TrainClassschedule l : list) {
            //正常签到
            trainCourseattend = new TrainCourseattend();
            trainCourseattend.setClassId(classId);
            trainCourseattend.setTraineeId(userId);
            trainCourseattend.setIncardTime(date);
            trainCourseattend.setBeginTime(se.parse(l.getBeginTime().toString()));
            trainCourseattend.setEndTime(se.parse(l.getEndTime().toString()));
            trainCourseattend.setClassScheduleId(l.getUuid());
            trainCourseattend.setCreateUser("后台程序创建");

            //不需要签退
            if (0 == needCheckout) {
                if (date.before(se.parse(l.getEndTime().toString()))) {
                    trainCourseattend.setOutcardTime(se.parse(l.getEndTime().toString()));
                } else {
                    trainCourseattend.setOutcardTime(se.parse(list.get(list.size() - 1).getEndTime().toString()));
                }
                trainCourseattend.setAttendResult(attendResult);
            } else {
                //将现阶段考勤结果存入备用字段
                trainCourseattend.setExtField04(attendResult);
            }
            this.doMerge(trainCourseattend);
        }
    }

    @Override
    public void installTrainCourseattend(Map<String, Object> map, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception {
        TrainCourseattend trainCourseattend;
        SimpleDateFormat se = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //正常签到
        trainCourseattend = new TrainCourseattend();
        trainCourseattend.setClassId(classId);
        trainCourseattend.setTraineeId(userId);
        trainCourseattend.setIncardTime(date);
        trainCourseattend.setBeginTime(se.parse(map.get("BEGIN_TIME").toString()));
        trainCourseattend.setEndTime(se.parse(map.get("END_TIME").toString()));
        trainCourseattend.setClassScheduleId(map.get("CLASS_SCHEDULE_ID").toString());
        trainCourseattend.setCreateUser("后台程序创建");
        //不需要签退
        if (0 == needCheckout) {
            if (date.before(se.parse(map.get("END_TIME").toString()))) {
                trainCourseattend.setOutcardTime(se.parse(map.get("END_TIME").toString()));
            } else {
                trainCourseattend.setOutcardTime(se.parse(map.get("END_TIME").toString()));
            }
            trainCourseattend.setAttendResult(attendResult);

            trainCourseattend.setAttendResult(attendResult);
        } else {
            //将现阶段考勤结果存入备用字段
            trainCourseattend.setExtField04(attendResult);
        }
        this.doMerge(trainCourseattend);
    }


    @Override
    public void updateTrainCourseattend(List<Map<String, Object>> list, String classId, String userId, Date date) {
        Map<String, Object> map;
        TrainCourseattend trainCourseattend;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, map.get("CLASS_SCHEDULE_ID"), userId, 0});
            trainCourseattend.setUpdateTime(date);
            trainCourseattend.setUpdateUser("后台程序创建");
            trainCourseattend.setOutcardTime(date);
            trainCourseattend.setAttendResult(trainCourseattend.getExtField04());
            this.doMerge(trainCourseattend);
        }

    }

    @Override
    public void updateTrainCourseattends(List<TrainClassschedule> list, String classId, String userId, Date date) {
        TrainCourseattend trainCourseattend;
        for (TrainClassschedule l : list) {
            trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, l.getUuid(), userId, 0});
            trainCourseattend.setUpdateTime(date);
            trainCourseattend.setUpdateUser("后台程序创建");
            trainCourseattend.setOutcardTime(date);
            trainCourseattend.setAttendResult(trainCourseattend.getExtField04());
            this.doMerge(trainCourseattend);
        }
    }

    @Override
    public void updateTrainCourseattend(Map<String, Object> map, String classId, String userId, Date date) {
        TrainCourseattend trainCourseattend;
        trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, map.get("CLASS_SCHEDULE_ID"), userId, 0});
        trainCourseattend.setUpdateTime(date);
        trainCourseattend.setUpdateUser("后台程序创建");
        trainCourseattend.setOutcardTime(date);
        trainCourseattend.setAttendResult(trainCourseattend.getExtField04());
        this.doMerge(trainCourseattend);
    }
}