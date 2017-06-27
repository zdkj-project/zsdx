package com.zd.school.app;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zd.school.build.allot.model.JwClassRoomAllot;
import com.zd.school.build.allot.service.JwClassRoomAllotService;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.model.app.JwTGradeclassForApp;

@Controller
@RequestMapping("/app/JwClassRoomAllot")
public class JwClassRoomAllotAppController {

	@Resource
	JwClassRoomAllotService thisService;
	
	@Resource
	BuildRoominfoService brService;
	
	@Resource
	JwTGradeclassService gradeService;
	/**
	 * 根据房间ID得到该房间的班级信息
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getClassByroomId" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody JwTGradeclassForApp getClassByroomId(
			@RequestParam("roomCode") String roomCode, HttpServletRequest request,
			HttpServletResponse response) {
		JwTGradeclassForApp gradeClassApp = new JwTGradeclassForApp();
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if(bRoomInfo == null ){
			gradeClassApp.setMessage(false);
			gradeClassApp.setMessageInfo("没有找到该教室！");
			return gradeClassApp;
		}
		JwClassRoomAllot classRoom = thisService.getByProerties("roomId", bRoomInfo.getUuid());
		if(classRoom == null ){
			gradeClassApp.setMessage(false);
			gradeClassApp.setMessageInfo("没有找到该教室！");
			return gradeClassApp;
		}
		JwTGradeclass gradeClass = gradeService.get(classRoom.getClaiId());
		if(gradeClass == null ){
			gradeClassApp.setMessage(false);
			gradeClassApp.setMessageInfo("没有找到该班级！");
			return gradeClassApp;
		}
		gradeClassApp.setMessage(true);
		gradeClassApp.setGradeClass(gradeClass);
		return gradeClassApp;
	}
	
}
