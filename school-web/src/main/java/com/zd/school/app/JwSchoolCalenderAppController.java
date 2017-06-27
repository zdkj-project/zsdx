package com.zd.school.app;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.school.jw.eduresources.model.JwSchoolCalender;
import com.zd.school.jw.eduresources.service.JwSchoolCalenderService;

@Controller
@RequestMapping("/app/schoolCalender")
public class JwSchoolCalenderAppController {

	
	@Resource
	JwSchoolCalenderService thisService; // service层接口

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwSchoolCalender> list(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String xcYear = request.getParameter("xcYear");
		String xcMonth = request.getParameter("xcMonth");
		String str="'"+xcYear+"-"+xcMonth;
		String hql="from JwSchoolCalender where start>="+str+"'"+" and start<="+str+"-31' order by start";
		List<JwSchoolCalender> lists = thisService.doQuery(hql);// 执行查询方法
		return lists;
	}
	
	@RequestMapping(value = { "/getByDay" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwSchoolCalender> getByDay(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String xcYear = request.getParameter("xcYear");
		String xcMonth = request.getParameter("xcMonth");
		String day= request.getParameter("day");
		String str=xcYear+"-"+xcMonth+"-"+day;
		String hql="from JwSchoolCalender where start like '%"+str+"%' order by start";
		List<JwSchoolCalender> lists = thisService.doQuery(hql);// 执行查询方法
		return lists;
	}
	
	
}
