package com.zd.school.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.util.DateUtil;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTSchoolcourseService;
import com.zd.school.jw.schoolcourse.model.JwCoursepublish;
import com.zd.school.jw.schoolcourse.model.JwPublishcourse;
import com.zd.school.jw.schoolcourse.model.JwStuEnteredCourse;
import com.zd.school.jw.schoolcourse.service.JwCoursepublishService;
import com.zd.school.jw.schoolcourse.service.JwPublishcourseService;
import com.zd.school.jw.schoolcourse.service.JwStuEnteredCourseService;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.service.JwClassstudentService;

@Controller
@RequestMapping("/app/JwCoursepublish")
public class JwCoursepublishAppController {

	@Resource
	private JwCoursepublishService thisService; // service层接口
	@Resource
	private JwPublishcourseService publishService; // service层接口
	@Resource
	private JwTSchoolcourseService courseService;
	@Resource
	private JwTGradeService gradeService;
	@Resource
	private JwClassstudentService stuService;
	@Resource
	private JwStuEnteredCourseService stuCourseService;

	// 获取今天可报名的列表
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwCoursepublish> list(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String today = DateUtil.formatDateTime(new Date());
		String hql = "from JwCoursepublish where beginDate<='" + today + "' and endDate>='" + today
				+ "' and isDelete=0";
		List<JwCoursepublish> list = thisService.getQuery(hql);
		return list;
	}

	// 获取今天可报名的详细列表
	@RequestMapping(value = { "/courseList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwPublishcourse> courseList(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String today = DateUtil.formatDateTime(new Date());
		String userId = request.getParameter("userId");
		JwClassstudent stu = stuService.getByProerties("studentId", userId);
		String hql = "from JwPublishcourse as p inner join fetch p.pcourseGrade as g where g.uuid=(select graiId from JwTGradeclass where uuid='"
				+ stu.getClaiId() + "') and p.publishId in (select uuid from JwCoursepublish where beginDate<='" + today
				+ "' and endDate>='" + today + "' and isDelete=0) and p.isDelete=0 ";

		List<JwPublishcourse> qResult = publishService.getQuery(hql);
		List<JwPublishcourse> list = new ArrayList<JwPublishcourse>();
		/*
		 * for (JwPublishcourse jpc : qResult) { String coursehql =
		 * "from JwTSchoolcourse where uuid='" + jpc.getCourseId() + "'";
		 * List<JwTSchoolcourse> courselist = courseService.doQuery(coursehql);
		 * for (JwTSchoolcourse jsc : courselist) {
		 * jpc.setCourseName(jsc.getCourseName());
		 * jpc.setCourseContent(jsc.getCourseContent()); }
		 * 
		 * String gradehql =
		 * "from JwPublishcourse as p inner join fetch p.pcourseGrade where p.uuid='"
		 * + jpc.getUuid() + "'"; List<JwPublishcourse> gradelist =
		 * publishService.doQuery(gradehql); for (JwPublishcourse jpctwo :
		 * gradelist) { String gradenamehql = "from JwTGrade where uuid='" + new
		 * ArrayList<>(jpctwo.getPcourseGrade()).get(0).getUuid() + "'";
		 * List<JwTGrade> gradenamelist = gradeService.doQuery(gradenamehql);
		 * for (JwTGrade jg : gradenamelist) {
		 * jpc.setGradeName(jg.getGradeName()); } } list.add(jpc);
		 * 
		 * }
		 */
		return qResult;

	}

	// 学生报名
	@RequestMapping(value = { "/stuEnteredCourse" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String stuEnteredCourse(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userId = request.getParameter("userId");
		String pcourseId = request.getParameter("pcourseId");
		JwStuEnteredCourse enteredCourse = new JwStuEnteredCourse();
		enteredCourse.setStudentId(userId);
		enteredCourse.setPcourseId(pcourseId);
		stuCourseService.doMerge(enteredCourse);
		return "ok";
	}

	// 取消报名
	@RequestMapping(value = { "/delStuEnteredCourse" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String delStuEnteredCourse(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userId = request.getParameter("userId");
		String pcourseId = request.getParameter("pcourseId");
		String[] params = { "pcourseId", "studentId" };
		String[] values = { pcourseId, userId };
		stuCourseService.doDeleteByProperties(params, values);
		return "ok";
	}

	// 获得是否报名
	@RequestMapping(value = { "/getBaomingState" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String getBaomingState(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String pcourseId = request.getParameter("pcourseId");
		String[] params = { "pcourseId", "studentId" };
		String[] values = { pcourseId, userId };
		JwStuEnteredCourse enteredCourse = stuCourseService.getByProerties(params, values);
		if (enteredCourse == null) {
			return "no";
		} else {
			return "ok";
		}
	}

	// 获得报名数量
	@RequestMapping(value = { "/getBaomingCount" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String getBaomingCount(HttpServletRequest request, HttpServletResponse response) {
		String pcourseId = request.getParameter("pcourseId");
		return stuCourseService.getCount("select count(1) from JwStuEnteredCourse where pcourseId='" + pcourseId + "'")
				.toString();
	}

	// 获取任课列表
	@RequestMapping(value = { "/getCourseList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwPublishcourse> getCourseList(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String today = DateUtil.formatDateTime(new Date());
		String userId = request.getParameter("userId");
		String hql = "from JwPublishcourse where beginDate<='" + today + "' and endDate>='" + today
				+ "'and isDelete=0 and courseId in(select uuid from JwTSchoolcourse where teachID='" + userId + "')";
		List<JwPublishcourse> jwTSchoolcourses = publishService.getQuery(hql);
		return jwTSchoolcourses;
	}

	// 获取已报名的列表
	@RequestMapping(value = { "/getBaomingDetail" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwStuEnteredCourse> getBaomingDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String today = DateUtil.formatDateTime(new Date());
		String courseId = request.getParameter("courseId");
		String hql = "from JwStuEnteredCourse where pcourseId in(";
		hql += "select uuid from JwPublishcourse where beginDate<='" + today + "' and endDate>='" + today
				+ "'and isDelete=0 and courseId='" + courseId + "')";
		List<JwStuEnteredCourse> list = stuCourseService.getQuery(hql);
		return list;
	}

	@RequestMapping(value = { "/myEntered" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwStuEnteredCourse> myEntered(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userId = request.getParameter("userId");
		String hql="from JwStuEnteredCourse where studentId='"+userId+"' order by createTime";

		List<JwStuEnteredCourse> qResult = stuCourseService.getQuery(hql);
		return qResult;
	}

}
