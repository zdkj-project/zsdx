package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainCourseevalresultDao;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainCourseevalresult;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
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
import java.util.*;

/**
 * ClassName: TrainCourseevalresultServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程评价结果(TRAIN_T_COURSEEVALRESULT)实体Service接口实现类.
 * date: 2017-06-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainCourseevalresultServiceImpl extends BaseServiceImpl<TrainCourseevalresult> implements TrainCourseevalresultService {

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
        QueryResult<TrainCourseevalresult> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
    public TrainCourseevalresult doUpdateEntity(TrainCourseevalresult entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainCourseevalresult saveEntity = this.get(entity.getUuid());
        try {
            BeanUtils.copyProperties(saveEntity, entity);
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
    public TrainCourseevalresult doAddEntity(TrainCourseevalresult entity, SysUser currentUser) {
        TrainCourseevalresult saveEntity = new TrainCourseevalresult();
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

            this.doMerge(courseEvalStand);
        }
        scheduleService.doUpdateByProperties("uuid", ids, "evalState", 1);
        return true;
    }

    @Override
    public Boolean doSumCourseEval(String ids) {
        String sql = MessageFormat.format("EXECUTE TRAIN_P_SUMCLASSCOURSEEVAL ''{0}''", ids);
        List<?> alist = this.getQuerySql(sql);

        return true;
    }

    @Override
    public Boolean doEndCourseEval(String ids, SysUser currentUser) {
        scheduleService.doUpdateByProperties("uuid", ids, "evalState", 2);
        return true;

    }

    public Map<String, Map<String, List<Map<String, Object>>>> getClassCourseEvalResult(String classId) {
        //"评估指标", "评估标准", "很满意", "满意", "基本满意", "不满意","很满意度","满意度"
        String sql = "SELECT INDICATOR_NAME,INDICATOR_STAND,VERY_SATISFACTIONCOUNT,SATISFACTIONCOUNT,BAS_SATISFACTIONCOUNT,NO_SATISFACTIONCOUNT,VERY_SATISFACTION,SATISFACTION," +
                "CLASS_SCHEDULE_ID,INDICATOR_ID FROM dbo.TRAIN_T_COURSEEVALRESULT WHERE CLASS_ID=''{0}'' order by  CLASS_SCHEDULE_ID,INDICATOR_ID";
        sql = MessageFormat.format(sql, classId);
        String classCourseId = "";
        String indicatorId = "";
        Map<String, List<Map<String, Object>>> mapCourseIndicatorStand = new HashMap<>(); //返回的数据

        //班级下所课程的所有评价指标标准
        List<Map<String, Object>> listClassCourseStand = this.getForValuesToSql(sql);
        int standCount = listClassCourseStand.size();
        if(standCount==0){
        	return new HashMap<>();
        }
        
        String perIndicatorId = listClassCourseStand.get(0).get("INDICATOR_ID").toString();
        String perClassCourseid = listClassCourseStand.get(0).get("CLASS_SCHEDULE_ID").toString();

        Map<String, Object> addStand = new HashMap<>();
        List<Map<String, Object>> addStandList = new ArrayList<>();//要归拢的标准

        Map<String, List<Map<String, Object>>> mapIndicatorStand = new HashMap<>();  //指标的map

        Map<String, Map<String, List<Map<String, Object>>>> mapCourseStand = new HashMap<>();  //课程包含标准
        for (int i = 0; i < standCount; i++) {
            addStand = listClassCourseStand.get(i);
            classCourseId = addStand.get("CLASS_SCHEDULE_ID").toString();
            indicatorId = addStand.get("INDICATOR_ID").toString();
            if (!perClassCourseid.equals(classCourseId) || i == standCount - 1) {
                if (!perIndicatorId.equals(indicatorId) || i == standCount - 1) {
                    //如果前一指标Id和当前指标id不同并且不是最后 一个
                    if (i == standCount - 1)
                        addStandList.add(addStand);
                    List<Map<String, Object>> addStandList1 = new ArrayList<>();//要归拢的标准
                    addStandList1.addAll(addStandList);
                    mapIndicatorStand.put(perIndicatorId, addStandList1);
                    addStandList.clear();
                }
                Map<String, List<Map<String, Object>>> mapIndicatorStand1 = new HashMap<>();  //指标的map
                mapIndicatorStand1.putAll(mapIndicatorStand);
                mapCourseStand.put(perClassCourseid, mapIndicatorStand1);
                mapIndicatorStand.clear();
            } else {
                if (!perIndicatorId.equals(indicatorId) || i == standCount - 1) {
                    //如果前一指标Id和当前指标id不同并且不是最后 一个
                    if (i == standCount - 1) {
                        addStandList.add(addStand);
                        perIndicatorId = indicatorId;
                    }
                    List<Map<String, Object>> addStandList1 = new ArrayList<>();//要归拢的标准
                    addStandList1.addAll(addStandList);
                    mapIndicatorStand.put(perIndicatorId, addStandList1);
                    addStandList.clear();
                }
            }
            addStandList.add(addStand);
            perClassCourseid = classCourseId;
            perIndicatorId = indicatorId;
        }
        return mapCourseStand;

    }

    public Map<String, List<Map<String, Object>>> getClassEvalResult(String ids) {
        String sql = " SELECT INDICATOR_NAME,INDICATOR_STAND,VERY_SATISFACTIONCOUNT,SATISFACTIONCOUNT,BAS_SATISFACTIONCOUNT,NO_SATISFACTIONCOUNT," +
                "VERY_SATISFACTION,SATISFACTION,INDICATOR_ID FROM dbo.TRAIN_T_CLASSEVALRESULT WHERE CLASS_ID=''{0}'' order by  INDICATOR_ID ";
        sql = MessageFormat.format(sql, ids);
        //班级的所有评价标准
        List<Map<String, Object>> listClassStand = this.getForValuesToSql(sql);
        int standCount = listClassStand.size();  //所有的标准个数
        if(standCount==0){
        	return new HashMap<>();
        }
        String perIndicatorId = listClassStand.get(0).get("INDICATOR_ID").toString(); //第一条标准对应的指标id
        String indicatorId = ""; //最校报一条标准的Id
        Map<String, Object> addStand = new HashMap<>();  //一条标准
        List<Map<String, Object>> addStandList = new ArrayList<>();//要归拢到指标的标准的集合
        Map<String, List<Map<String, Object>>> mapIndicatorStand = new HashMap<>();  //指标包含的标准的map
        //循环处理每条标准数据
        for (int i = 0; i < standCount; i++) {
            addStand = listClassStand.get(i);
            indicatorId = addStand.get("INDICATOR_ID").toString();
            if (!perIndicatorId.equals(indicatorId)){
                //前一指标Id和当前指标id不同，汇总前一指标的标准
                List<Map<String, Object>> addStandList1 = new ArrayList<>();//要归拢的标准列表
                addStandList1.addAll(addStandList);
                mapIndicatorStand.put(perIndicatorId, addStandList1);
                addStandList.clear();

                //如果当前标准是最后一条标准了
                if (i == standCount-1) {
                    addStandList.add(addStand);
                    mapIndicatorStand.put(indicatorId, addStandList);
                } else {
                    addStandList.add(addStand);
                    perIndicatorId = indicatorId;
                }
            } else {
                //如果当前标准是最后一条标准了
                if (i == standCount-1) {
                    addStandList.add(addStand);
                    mapIndicatorStand.put(indicatorId, addStandList);
                } else {
                    addStandList.add(addStand);
                    perIndicatorId = indicatorId;
                }
            }
        }
        return mapIndicatorStand;
    }

    public Boolean resetCourseEvalRanking(String classId) {
        String[] propName = {"classId", "isEval"};
        Object[] propValue = {classId, 1};
        Map<String, String> sortedCondition = new LinkedHashMap<>();
        sortedCondition.put("satisfaction", "desc");
        List<TrainClassschedule> classCourse = scheduleService.queryByProerties(propName, propValue, sortedCondition);
        StringBuilder sb = new StringBuilder();
        int courseCount = classCourse.size();
        for (int i = 0; i < courseCount; i++) {
            sb.append(MessageFormat.format("update TRAIN_T_CLASSSCHEDULE set ranking={0} where CLASS_SCHEDULE_ID=''{1}'';", i + 1, classCourse.get(i).getUuid()));
        }
        if (sb.length() > 0) {
            this.doExecuteSql(sb.toString());
            return true;
        } else
            return true;
    }

    public Map<String, List<Map<String, Object>>> getCourseEvalResult(String courseId) {
        //"评估指标", "评估标准", "很满意", "满意", "基本满意", "不满意","很满意度","满意度"
        String sql = "SELECT INDICATOR_NAME,INDICATOR_STAND,VERY_SATISFACTIONCOUNT,SATISFACTIONCOUNT,BAS_SATISFACTIONCOUNT,NO_SATISFACTIONCOUNT,VERY_SATISFACTION,SATISFACTION," +
                "CLASS_SCHEDULE_ID,INDICATOR_ID FROM dbo.TRAIN_T_COURSEEVALRESULT WHERE CLASS_SCHEDULE_ID=''{0}'' order by  INDICATOR_ID";
        sql = MessageFormat.format(sql, courseId);
        String indicatorId = "";

        //所课程的所有评价指标标准
        List<Map<String, Object>> listClassCourseStand = this.getForValuesToSql(sql);
        int standCount = listClassCourseStand.size();
        if(standCount==0)
        	return null;
        
        String perIndicatorId = listClassCourseStand.get(0).get("INDICATOR_ID").toString();

        Map<String, Object> addStand = new HashMap<>();  //标准对象
        List<Map<String, Object>> addStandList = new ArrayList<>();//要归拢的标准列表

        Map<String, List<Map<String, Object>>> mapIndicatorStand = new HashMap<>();  //指标的map

        for (int i = 0; i < standCount; i++) {
            addStand = listClassCourseStand.get(i);
            indicatorId = addStand.get("INDICATOR_ID").toString();
            if (!perIndicatorId.equals(indicatorId)){
                //前一指标Id和当前指标id不同，汇总前一指标的标准
                List<Map<String, Object>> addStandList1 = new ArrayList<>();//要归拢的标准列表
                addStandList1.addAll(addStandList);
                mapIndicatorStand.put(perIndicatorId, addStandList1);
                addStandList.clear();

                //如果当前标准是最后一条标准了
                if (i == standCount-1) {
                    addStandList.add(addStand);
                    mapIndicatorStand.put(indicatorId, addStandList);
                } else {
                    addStandList.add(addStand);
                    perIndicatorId = indicatorId;
                }
            } else {
                //如果当前标准是最后一条标准了
                if (i == standCount-1) {
                    addStandList.add(addStand);
                    mapIndicatorStand.put(indicatorId, addStandList);
                } else {
                    addStandList.add(addStand);
                    perIndicatorId = indicatorId;
                }
            }
        }

        return  mapIndicatorStand;
    }

    public Map<String, Object>  getCourseEvalResultDetail(String courseId){
        String sql = "SELECT classId as classId,classCategory as classCategory,className as className,courseDate as courseDate"
        		+ ",courseTime as courseTime,convert(varchar(10),verySatisfaction) as verySatisfaction"
                + ",convert(varchar(10),satisfaction) as satisfaction,ranking as ranking,teacherId as teacherId"
                + ",teacherName as teacherName,courseId as courseId,courseName as courseName,classScheduleId as classScheduleId"
                + ",teachTypeName as teachTypeName,advise as advise FROM TRAIN_V_CLASSCOURSEEVAL where classScheduleId=''{0}''";
        sql = MessageFormat.format(sql, courseId);
        QueryResult<TrainClassCourseEval> qr = this.getQueryResultSqlObject(sql, 0, 200,TrainClassCourseEval.class);
        TrainClassCourseEval classschedule = qr.getResultList().get(0);

        Map<String,Object> mapOneCourse = new HashMap<>();
        Map<String, List<Map<String, Object>>> courseStands = this.getCourseEvalResult(courseId);
        
        //前台显示数据需要，将指标数据转成list
        List<List<Map<String,Object>>> list = new ArrayList<>();
        if(courseStands!=null){	     
	        for (Map.Entry<String, List<Map<String, Object>>> entry : courseStands.entrySet()) {
	            list.add(entry.getValue());
	        }
        }
        mapOneCourse.put("className", classschedule.getClassName());
        mapOneCourse.put("courseName",classschedule.getCourseName());
        mapOneCourse.put("teachTypeName",classschedule.getTeachTypeName());
        mapOneCourse.put("teacherName",classschedule.getTeacherName());
        mapOneCourse.put("verySatisfaction", classschedule.getVerySatisfaction());
        mapOneCourse.put("satisfaction",classschedule.getSatisfaction());
        mapOneCourse.put("classScheduleId", classschedule.getClassScheduleId());
        mapOneCourse.put("advise",classschedule.getAdvise());
        mapOneCourse.put("standList", list);
        //mapOneCourse.put("standList", courseStands);

        return  mapOneCourse;
    }
}