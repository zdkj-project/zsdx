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

import com.zd.core.controller.core.BaseController;
import com.zd.core.util.DateUtil;
import com.zd.school.build.allot.model.JwClassRoomAllot;
import com.zd.school.build.allot.service.JwClassRoomAllotService;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.jw.ecc.model.EccClasselegant;
import com.zd.school.jw.ecc.model.EccClassredflag;
import com.zd.school.jw.ecc.model.EccClassstar;
import com.zd.school.jw.ecc.service.EccClasselegantService;
import com.zd.school.jw.ecc.service.EccClassredflagService;
import com.zd.school.jw.ecc.service.EccClassstarService;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.model.app.ClassInfoForApp;
import com.zd.school.jw.model.app.EccClasselegantApp;
import com.zd.school.jw.model.app.PictureApp;
import com.zd.school.jw.model.app.PictureForApp;
import com.zd.school.jw.model.app.PictureReturnApp;
import com.zd.school.jw.model.app.VideoApp;
import com.zd.school.jw.model.app.VideoForApp;
import com.zd.school.jw.model.app.VideoReturnApp;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Controller
@RequestMapping("/app/JwTGradeclass")
public class JwTGradeclassController extends BaseController<JwTGradeclass> {

	@Resource
	JwTGradeclassService thisService;

	@Resource
	TeaTeacherbaseService teacherService;

	@Resource
	JwClassteacherService classTeacherService;

	@Resource
	BuildRoominfoService brService;

	@Resource
	JwClassRoomAllotService jraService;

	@Resource
	EccClassstarService starService;

	@Resource
	EccClassredflagService flagService;

	@Resource
	EccClasselegantService elegantService; // service层接口

	@Resource
	BaseAttachmentService baseTAttachmentService;// service层接口

	/**
	 * 根据班级ID得到该班级信息与班主任信息
	 * 
	 * @param claiId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = { "/getClassInfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ClassInfoForApp getClassInfo(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ClassInfoForApp info = new ClassInfoForApp();
		String roomCode = claiId;
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if (bRoomInfo == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwClassRoomAllot classRoom = jraService.getByProerties("roomId", bRoomInfo.getUuid());
		if (classRoom == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwTGradeclass gradeClass = thisService.get(classRoom.getClaiId());// 班级对象
		if (gradeClass == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该班级！");
			return info;
		}
		if (gradeClass.getUuid() == null || gradeClass.getUuid().trim().equals("")) {
			info.setMessage(false);
			info.setMessageInfo("班级ID不能为空");
			return info;
		}
		List<JwTGradeclass> classList = thisService.queryByProerties("uuid", gradeClass.getUuid());
		if (classList == null || classList.size() <= 0) {
			info.setMessage(false);
			info.setMessageInfo("未找到该班级信息：班级ID=" + gradeClass.getUuid());
			return info;
		}
		JwTGradeclass classInfo = classList.get(0);
		info.setClassInfo(classInfo);
		JwClassteacher calssTeacher = classTeacherService.getByProerties("claiId", classInfo.getUuid());
		if (calssTeacher == null) {
			info.setMessage(false);
			info.setMessageInfo("班主任ID不能为空：班级ID=" + classInfo.getTeacherId());
			return info;
		}
		String teacherId = calssTeacher.getTteacId();
		List<TeaTeacherbase> teacherList = teacherService.queryByProerties("uuid", teacherId);
		if (teacherList == null || teacherList.size() <= 0) {
			info.setMessage(false);
			info.setMessageInfo("未找到该班级的班主任信息：班级ID=" + classInfo.getTeacherId() + "班主任ID=" + teacherId);
			return info;
		}
		Date date = new Date();
		String today = DateUtil.formatDate(date);
		String hql = "from EccClassstar where isDelete=0 and claiId='" + classInfo.getUuid() + "' and beginDate<='"
				+ today + "' and endDate>='" + today + "'";
		List<EccClassstar> classstarList = starService.doQuery(hql);
		if (classstarList != null && classstarList.size() > 0) {
			EccClassstar starInfo = classstarList.get(0);
			info.setClassstarInfo(starInfo);
		}
		hql = "from EccClassredflag where isDelete=0 and claiId='" + classInfo.getUuid() + "' and beginDate<='" + today
				+ "' and endDate>='" + today + "'";
		List<EccClassredflag> classflagList = flagService.doQuery(hql);
		if (classflagList != null && classflagList.size() > 0) {
			if (classflagList.size() > 1) {
				for (int i = 1; i < classflagList.size(); i++) {
					EccClassredflag before = classflagList.get(i - 1);
					EccClassredflag now = classflagList.get(i);
					if (before.getRedflagType().equals(now.getRedflagType())) {
						classflagList.remove(before);
					}
				}
			}
			info.setRedflagList(classflagList);
		}

		info.setMessage(true);
		info.setMessageInfo("请求成功！");
		info.setTeacherInfo(teacherList.get(0));
		return info;
	}

	@RequestMapping(value = { "/getClasselegantInfo" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody EccClasselegantApp getClasselegantInfo(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		EccClasselegantApp info = new EccClasselegantApp();
		String roomCode = claiId;
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if (bRoomInfo == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwClassRoomAllot classRoom = jraService.getByProerties("roomId", bRoomInfo.getUuid());
		if (classRoom == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwTGradeclass gradeClass = thisService.get(classRoom.getClaiId());// 班级对象
		if (gradeClass == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该班级！");
			return info;
		}
		if (gradeClass.getUuid() == null || gradeClass.getUuid().trim().equals("")) {
			info.setMessage(false);
			info.setMessageInfo("班级ID不能为空");
			return info;
		}
		List<JwTGradeclass> classList = thisService.queryByProerties("uuid", gradeClass.getUuid());
		if (classList == null || classList.size() <= 0) {
			info.setMessage(false);
			info.setMessageInfo("未找到该班级信息：班级ID=" + gradeClass.getUuid());
			return info;
		}
		JwTGradeclass classInfo = classList.get(0);
		List<EccClasselegant> eleganeList = elegantService.queryByProerties("claiId", classInfo.getUuid());
		for (EccClasselegant eccClasselegant : eleganeList) {
			List<BaseAttachment> attList = baseTAttachmentService.queryByProerties("recordId",
					eccClasselegant.getUuid());
			eccClasselegant.setFileList(attList);
		}
		info.setElegantList(eleganeList);
		info.setMessage(true);
		info.setMessageInfo("请求成功！");
		return info;
	}

	@RequestMapping(value = { "/downloadPic" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody PictureApp downloadPic(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PictureApp info=new PictureApp();
		String roomCode = claiId;
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if (bRoomInfo == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwClassRoomAllot classRoom = jraService.getByProerties("roomId", bRoomInfo.getUuid());
		if (classRoom == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwTGradeclass gradeClass = thisService.get(classRoom.getClaiId());// 班级对象
		if (gradeClass == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该班级！");
			return info;
		}
		if (gradeClass.getUuid() == null || gradeClass.getUuid().trim().equals("")) {
			info.setMessage(false);
			info.setMessageInfo("班级ID不能为空");
			return info;
		}
		List<JwTGradeclass> classList = thisService.queryByProerties("uuid", gradeClass.getUuid());
		if (classList == null || classList.size() <= 0) {
			info.setMessage(false);
			info.setMessageInfo("未找到该班级信息：班级ID=" + gradeClass.getUuid());
			return info;
		}
		JwTGradeclass classInfo = classList.get(0);
		String[] inType = { "JPG", "JPEG", "BMP", "PNG" };
		int maxSize = 300 * 1024 * 1024;
		List<BaseAttachment> attList = filterFile(classInfo.getUuid(), inType, maxSize);
		if (attList.size() > 100) {
			attList = attList.subList(0, 100);
		}
		List<PictureForApp> picList = new ArrayList<PictureForApp>();
		for (BaseAttachment baseAttachment : attList) {
			PictureForApp pic = new PictureForApp();
			String attUrl = baseAttachment.getAttachUrl();
			pic.setPictureName(attUrl.substring(attUrl.lastIndexOf('/') + 1));
			pic.setPictureURL(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
					  attUrl);
			picList.add(pic);
		}
		PictureReturnApp data=new PictureReturnApp();
		data.setTotalCount(picList.size());
		data.setPicList(picList);
		info.setData(data);
		info.setMessage(true);
		info.setMessageInfo("请求成功！");
		info.setData(data);
		return info;
	}

	@RequestMapping(value = { "/downloadVideo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody VideoApp downloadVideo(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		VideoApp info=new VideoApp();
		String roomCode = claiId;
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if (bRoomInfo == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwClassRoomAllot classRoom = jraService.getByProerties("roomId", bRoomInfo.getUuid());
		if (classRoom == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该教室！");
			return info;
		}
		JwTGradeclass gradeClass = thisService.get(classRoom.getClaiId());// 班级对象
		if (gradeClass == null) {
			info.setMessage(false);
			info.setMessageInfo("没有找到该班级！");
			return info;
		}
		if (gradeClass.getUuid() == null || gradeClass.getUuid().trim().equals("")) {
			info.setMessage(false);
			info.setMessageInfo("班级ID不能为空");
			return info;
		}
		List<JwTGradeclass> classList = thisService.queryByProerties("uuid", gradeClass.getUuid());
		if (classList == null || classList.size() <= 0) {
			info.setMessage(false);
			info.setMessageInfo("未找到该班级信息：班级ID=" + gradeClass.getUuid());
			return info;
		}
		JwTGradeclass classInfo = classList.get(0);
		String[] inType = { "avi", "mp4", "3gp" };
		int maxSize = 800 * 1024 * 1024;
		List<BaseAttachment> attList = filterFile(classInfo.getUuid(), inType, maxSize);
		if (attList.size() > 100) {
			attList = attList.subList(0, 100);
		}
		List<VideoForApp> videoList = new ArrayList<VideoForApp>();
		for (BaseAttachment baseAttachment : attList) {
			VideoForApp pic = new VideoForApp();
			String attUrl = baseAttachment.getAttachUrl();
			pic.setVideoName(attUrl.substring(attUrl.lastIndexOf('/') + 1));
			pic.setVideoURL(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ attUrl);
			videoList.add(pic);
		}
		VideoReturnApp data=new VideoReturnApp();
		data.setTotalCount(videoList.size());
		data.setVideoList(videoList);
		info.setData(data);
		info.setMessage(true);
		info.setMessageInfo("请求成功！");
		info.setData(data);
		return info;
	}

	private List<BaseAttachment> filterFile(String claiId, String[] inType, int maxSize) {
		List<BaseAttachment> returnList = new ArrayList<BaseAttachment>();
		StringBuffer types = new StringBuffer();
		for (String type : inType) {
			types.append("'." + type + "',");
		}
		types = types.deleteCharAt(types.length() - 1);
		String hql = "from EccClasselegant where claiId='" + claiId + "' order by createTime desc";
		List<EccClasselegant> eleganeList = elegantService.doQuery(hql);
		int size = 0;
		for (EccClasselegant eccClasselegant : eleganeList) {
			hql = "from BaseAttachment where recordId='" + eccClasselegant.getUuid() + "' and attachType in(" + types
					+ ") order by createTime desc";
			List<BaseAttachment> attList = baseTAttachmentService.doQuery(hql);
			for (BaseAttachment baseAttachment : attList) {
				if (size + baseAttachment.getAttachSize() <= maxSize) {
					returnList.add(baseAttachment);
					size += baseAttachment.getAttachSize();
				}
			}
		}
		return returnList;
	}

}
