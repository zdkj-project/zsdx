package com.zd.school.app;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.eduresources.model.JwCalender;
import com.zd.school.jw.eduresources.model.JwCalenderdetail;
import com.zd.school.jw.eduresources.service.JwCalenderService;
import com.zd.school.jw.eduresources.service.JwCalenderdetailService;
import com.zd.school.oa.flow.model.WfPorcessDefinition;

@Controller
@RequestMapping("/app/Calendar")
public class CalendarAppController extends FrameWorkController<JwCalender> implements Constant {
	
	@Resource
	private JwCalenderService calendarService;
	
	@Resource
	private JwCalenderdetailService calendarDetailService;
	//获取作息时间目录
	@RequestMapping(value = { "/getCalendar" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwCalender> getCalendar(HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {

		String hql = " from JwCalender where  activityState='1' and isDelete=0";
		List<JwCalender> list = calendarService.getQuery(hql);
//		String [] propName = {"activityState","isDelete"};
//		Object[] propValue = new Object[]{"1",Integer.getInteger("0")};
//		List<JwCalender> list = calendarService.queryByProerties(propName, propValue);
		return list;
	}
	// 获得指定作息时间目录的明细
	@RequestMapping(value = { "/getCalendarDetail" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwCalenderdetail> getCalendarDetail(@RequestParam("canderId") String canderId,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {
		
		String hql = " from JwCalenderdetail where canderId='" + canderId + "' and isDelete=0 order by isafgernoon asc,beginTime asc ";
		List<JwCalenderdetail> list = calendarDetailService.getQuery(hql);
		return list;
	}	
}
