package com.zd.school.app;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.controller.core.BaseController;
import com.zd.school.jw.model.app.LeaveApplayInfoApp;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.service.LeaveApplayService;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;

@Controller
@RequestMapping("/app/leaveapplay/")
public class LeaveApplayAppController extends BaseController<LeaveApplay>{
	 @Resource
	 private LeaveApplayService thisService;
	 
	 @Resource
	 private StuBaseinfoService stuInfoService;
	 
	 /**
	  * 根据学生ID判断学生当前是否请假
	  * @param stuId
	  * @param request
	  * @param response
	  * @return
	  */
	@RequestMapping(value = { "/nowIsLeave" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody LeaveApplayInfoApp nowIsLeave(@RequestParam("stuCode") String stuCode,HttpServletRequest request,
			HttpServletResponse response) {
		LeaveApplayInfoApp infoApp = new LeaveApplayInfoApp();
		if(stuCode == null || stuCode.trim().equals("")){
			infoApp.setMessage(false);
			infoApp.setMessageInfo("学生学号不能为空！");
			return infoApp;
		}
		StuBaseinfo stuInfo = stuInfoService.getByProerties("xh", stuCode);
		if(stuInfo == null){
			infoApp.setMessage(false);
			infoApp.setMessageInfo("没有找到该学生学号："+stuCode +"！");
			return infoApp;
		}
		List<LeaveApplay> leaveList = thisService.getLastApplay(stuInfo.getUuid());
		Date now = new Date();
		boolean flag = false;
		for(LeaveApplay la : leaveList){
			if (la.getStartDate().getTime() <= now.getTime()
					&& la.getEndDate().getTime() >= now.getTime()) {
				infoApp.setLeaveapplay(la);
				flag = true;
				break;
			}
		}
		if(flag){
			infoApp.setMessage(true);
			infoApp.setMessageInfo("当前已请假！");
		}else{
			infoApp.setMessage(true);
			infoApp.setMessageInfo("当前未请假！");
		}
		return infoApp;
	}
}
