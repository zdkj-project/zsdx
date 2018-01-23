package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.dao.TrainClassevalresultDao;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassevalresult;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
import com.zd.school.jw.train.model.vo.TrainClassEval;
import com.zd.school.jw.train.service.*;
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
        //汇总班级评价
        String sql = MessageFormat.format("EXECUTE TRAIN_P_SUMCLASSEVAL ''{0}''",ids);
        List<?> alist = this.doQuerySql(sql);

        //同步汇总班级下的需要评价课程
        String[] propName ={"classId","isEval"};
        Object[] propValue = {ids,1};
        List<TrainClassschedule> classCourse = scheduleService.queryByProerties(propName, propValue);
        for (TrainClassschedule trainClassschedule : classCourse) {
            courseevalresultService.doSumCourseEval(trainClassschedule.getUuid());
        }
        //重新设置排名
        courseevalresultService.resetCourseEvalRanking(ids);
        return true;
    }

    @Override
    public Boolean doStartClassEval(String ids) throws InvocationTargetException, IllegalAccessException {
    	TrainClass trainClass = classService.get(ids);
    	
    	if(trainClass.getIsEval()==0){
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
    	}
        
        classService.updateByProperties("uuid",ids,"isEval",1);

        //同步启动班级下的需要评价课程(开启未评价的)
        String[] propName ={"classId","isEval","evalState"};
        Object[] propValue = {ids,1,0};
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

    @Override
    public Map<String, Object> getExportRankingData(String classId) {
        String orderSql = " order by ranking asc ";
        List<Map<String, Object>> courseRank = scheduleService.getClassCourseRanking(orderSql, classId);
        Map<String, Object> mapCourseRanking = new LinkedHashMap<>();
        mapCourseRanking.put("data", courseRank);
        mapCourseRanking.put("title", "班级基本信息表");
        mapCourseRanking.put("head", new String[] { "课程名称", "课程类型", "上课教师", "很满意度", "满意度", "很满意排名" });
        mapCourseRanking.put("columnWidth",  new Integer[] { 40, 15, 20, 15, 15, 15 }); // 30代表30个字节，15个字符
        mapCourseRanking.put("columnAlignment", new Integer[] { 1, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右

        return  mapCourseRanking;
    }

    public Map<String, Object> getClassEvalResult(String ids, TrainClassEval trainClass) {
        Map<String, List<Map<String, Object>>> classStands = courseevalresultService.getClassEvalResult(ids);
        //前台显示数据需要，将指标数据转成list
        List<List<Map<String,Object>>> list = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : classStands.entrySet()) {
            list.add(entry.getValue());
        }
        Map<String, Object> mapOneClass = new HashMap<>();
        mapOneClass.put("className",trainClass.getClassName());
        mapOneClass.put("holdUnit", trainClass.getHoldUnit());
        mapOneClass.put("undertaker", trainClass.getUndertaker());
        mapOneClass.put("traineeCount",trainClass.getTrainees().toString());
        mapOneClass.put("evalCount","0");
        mapOneClass.put("verySatisfaction", trainClass.getVerySatisfaction());
        mapOneClass.put("satisfaction",trainClass.getSatisfaction());
        mapOneClass.put("advise",trainClass.getAdvise());
        StringBuilder sb = new StringBuilder(trainClass.getBeginDate());
        sb.append(" - ");
        sb.append(trainClass.getEndDate());
        sb.append(" 共 ");
        sb.append(trainClass.getTrainDays());
        sb.append(" 天 ");
        mapOneClass.put("trainTime",sb.toString());
        mapOneClass.put("head1",new String[]{"评估内容","评估等级"});
        mapOneClass.put("head", new String[] { "评估指标", "评估标准", "很满意", "满意", "基本满意", "不满意","很满意度","满意度" });
        mapOneClass.put("columnWidth",  new Integer[] { 20, 60, 15, 15, 15, 15 ,15,15}); // 30代表30个字节，15个字符
//        mapOneClass.put("standList", classStands);
        mapOneClass.put("standList", list);
        return mapOneClass;
    }

    public List<Map<String, Object>> getClassCourseEvalResult(String ids, String orderSql) {
        List<Map<String,Object>> mapCourseEval=new ArrayList<>();
        QueryResult<TrainClassCourseEval> queryResult = scheduleService.getClassCourseEval(0, 200, orderSql, ids);
        List<TrainClassCourseEval> list = queryResult.getResultList();
        Map<String, Map<String,List<Map<String, Object>>>> courseStands = courseevalresultService.getClassCourseEvalResult(ids);

        Map<String,Object> mapOneCourse = null;
        int iLength= list.size();
        for (int i = 0; i < iLength ; i++) {
            mapOneCourse = new HashMap<>();
            mapOneCourse.put("className",list.get(i).getClassName());
            mapOneCourse.put("courseName",list.get(i).getCourseName());
            mapOneCourse.put("teachTypeName",list.get(i).getTeachTypeName());
            mapOneCourse.put("teacherName",list.get(i).getTeacherName());
            mapOneCourse.put("verySatisfaction", list.get(i).getVerySatisfaction());
            mapOneCourse.put("satisfaction",list.get(i).getSatisfaction());
            mapOneCourse.put("classScheduleId", list.get(i).getClassScheduleId());
            mapOneCourse.put("advise",list.get(i).getAdvise());
            mapOneCourse.put("standList", courseStands.get(list.get(i).getClassScheduleId()));
            //评估指标	评估标准	很满意	满意	基本满意	不满意	很满意度	满意度

            mapOneCourse.put("head", new String[] { "评估指标", "评估标准", "很满意", "满意", "基本满意", "不满意","很满意度","满意度" });
            mapOneCourse.put("columnWidth",  new Integer[] { 20, 60, 15, 15, 15, 15 ,15,15}); // 30代表30个字节，15个字符

            mapCourseEval.add(mapOneCourse);
        }
        return mapCourseEval;
    }
}