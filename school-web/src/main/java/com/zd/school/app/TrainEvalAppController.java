package com.zd.school.app;

import com.zd.core.model.extjs.ExtResult;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.model.app.ClassEvalApp;
import com.zd.school.jw.model.app.CourseEvalApp;
import com.zd.school.jw.train.model.TrainClassevaldetail;
import com.zd.school.jw.train.model.TrainCourseevaldetail;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClassevaldetailService;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainCourseevaldetailService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoyibo on 2017-06-19.
 */
@Controller
@RequestMapping("/app/traineval")
public class TrainEvalAppController {

    @Resource
    private TrainClassscheduleService scheduleService;
    @Resource
    private TrainClassService classService;
    @Resource
    private TrainClassevaldetailService classevaldetailService;
    @Resource
    private TrainCourseevaldetailService courseevaldetailService;
    @Resource
    private SysUserService userService;

    /**
     *
     * @param courseId
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/getCourseEvalStand" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody CourseEvalApp getCourseEvalStand(String courseId, HttpServletRequest request, HttpServletResponse response) throws IOException{

        CourseEvalApp entity = scheduleService.getCourseEvalStand(courseId);
        return entity;
    }

    /**
     * 提交班经评价
     * @param eval
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = { "/saveClassEval" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody
    ExtResult saveClassEval(String eval, HttpServletRequest request, HttpServletResponse response) throws IOException{
        ExtResult extResult= new ExtResult();
        try {
            SysUser currentUser = userService.get("f111ebab-933b-4e48-b328-c731ae792ca0");
            List<TrainClassevaldetail> evalList = (List<TrainClassevaldetail>) JsonBuilder.getInstance().fromJsonArray(eval, TrainClassevaldetail.class);
            classevaldetailService.doAddClassEval(evalList,currentUser);
            extResult.setSuccess(true);
            extResult.setObj("'提交成功'");

            return extResult;
        } catch (Exception e) {
            extResult.setSuccess(false);
            extResult.setObj("'提交失败'");

            return extResult;
        }
    }

    /**
     * 提交课程评价
     * @param eval
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = { "/saveCourseEval" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody
    ExtResult saveCourseEval(String eval, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExtResult extResult = new ExtResult();
        try {
            SysUser currentUser = userService.get("f111ebab-933b-4e48-b328-c731ae792ca0");
            List<TrainCourseevaldetail> evalList = (List<TrainCourseevaldetail>) JsonBuilder.getInstance().fromJsonArray(eval, TrainCourseevaldetail.class);
            courseevaldetailService.saveCourseEval(evalList, currentUser);
            extResult.setSuccess(true);
            extResult.setObj("'提交成功'");

            return extResult;
        } catch (Exception e) {
            extResult.setSuccess(false);
            extResult.setObj("'提交失败'");

            return extResult;
        }
    }

    /**
     * 获取班评价的指标及标准
     * @param classId
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/getClassEvalStand" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody
    ClassEvalApp getClassEvalStand(String classId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        ClassEvalApp entity = classService.getClassEvalStand(classId);
        return entity;
    }
    
	@RequestMapping(value = "/listClassEvalCourse", produces = "application/json; charset=utf-8", method = {
			RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
	public Map<String,Object> listClassEvalCourse(@RequestParam("classId") String classId, @RequestParam("page") Integer page,@RequestParam("limit")  Integer limit) {
		Map<String,Object> returnMap=new HashMap<>();
		
		Integer start=(page-1)*limit;
		String currentDate=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		String sql = "select courseDate as courseDate,courseTime as courseTime,teacherName as teacherName,"
				+ "courseName as courseName,classScheduleId as classScheduleId,evalState as evalState,"
				+ "teachTypeName as teachTypeName,(case when ''{0}''>endTime then 1 else 0 end) as isOver"
				+ "   FROM TRAIN_V_CLASSCOURSEEVAL where  classId=''{1}'' "
				+ "ORDER BY isOver desc,evalState asc,courseDate desc,courseTime desc";
		sql = MessageFormat.format(sql, currentDate,classId);
	
		
		QueryResult<TrainClassCourseEval> list = courseevaldetailService.getQueryResultSqlObject(sql, start, limit,
				TrainClassCourseEval.class);
		
		returnMap.put("code", 0);
		returnMap.put("msg", "");
		returnMap.put("count", list.getTotalCount());
		returnMap.put("data", list.getResultList());
		
		return returnMap;
	}
    
}
