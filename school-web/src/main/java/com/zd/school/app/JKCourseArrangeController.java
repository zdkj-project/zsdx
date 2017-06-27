package com.zd.school.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.DateUtil;
import com.zd.core.util.SortListUtil;
import com.zd.school.build.allot.model.JwClassRoomAllot;
import com.zd.school.build.allot.service.JwClassRoomAllotService;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService;
import com.zd.school.jw.eduresources.model.JwCalenderdetail;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwCalenderService;
import com.zd.school.jw.eduresources.service.JwCalenderdetailService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.model.app.JKCourse;
import com.zd.school.jw.model.app.JKCourseToDayArray;
import com.zd.school.jw.model.app.JwTcourseArrangeForApp;
import com.zd.school.jw.push.service.PushInfoService;

@Controller
@RequestMapping("/app/JKCourseArrange")
public class JKCourseArrangeController extends FrameWorkController<JwCourseArrange> implements Constant {

	@Resource
	JwCourseArrangeService thisService; // service层接口。。。

	@Resource
	JwTGradeclassService classService;

	@Resource
	PushInfoService pushService;

	@Resource
	JwCalenderService canderService;

	@Resource
	JwCalenderdetailService canderDetailService;

	@Resource
	BuildRoominfoService brService;

	@Resource
	JwClassRoomAllotService jraService;

	/**
	 * @return list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */

	@RequestMapping(value = { "/jkcourse" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody JwTcourseArrangeForApp jkcourse(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JwTcourseArrangeForApp jk = new JwTcourseArrangeForApp();
		claiId = getClaid(claiId);
		if (claiId == null || claiId.trim().equals("")) {
			jk.setMessage("请传入参数！");
			return jk;
		}
		jk.setCode(1);
		// String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer(
				"from JwCourseArrange where claiId='" + claiId + "' and extField05=1 order by className,teachTime asc");
		List<JwCourseArrange> lists = thisService.doQuery(hql.toString());// 执行查询方法
		List<JwCourseArrange> newlists = new ArrayList<JwCourseArrange>();

		if (lists != null && lists.size() > 0) {
			for (JwCourseArrange jca : lists) {
				jca.setWeekOne(jca.getCourseName01() + "(" + jca.getTeacherName01() + ")");
				jca.setWeekTwo(jca.getCourseName02() + "(" + jca.getTeacherName02() + ")");
				jca.setWeekThree(jca.getCourseName03() + "(" + jca.getTeacherName03() + ")");
				jca.setWeekFour(jca.getCourseName04() + "(" + jca.getTeacherName04() + ")");
				jca.setWeekFive(jca.getCourseName05() + "(" + jca.getTeacherName05() + ")");
				jca.setWeekSix(jca.getCourseName06() + "(" + jca.getTeacherName06() + ")");
				jca.setWeekSeven(jca.getCourseName07() + "(" + jca.getTeacherName07() + ")");
				newlists.add(jca);
			}
			jk.setMessage("查询班级课表成功");
			jk.setJkArrange(newlists);
		} else {
			jk.setMessage("查询班级课表无信息");
			jk.setJkArrange(null);
		}
		return jk;
	}

	/**
	 * 根据班级得到当天的课程表
	 * 
	 * @param claiId
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author huangzc
	 */
	@RequestMapping(value = { "/todaycourse" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody JKCourseToDayArray todaycourse(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		claiId = getClaid(claiId);
		return getTodaycourse(claiId);
	}

	/**
	 * 根据班级得到当天当时的前两节课的课程表
	 * 
	 * @param claiId
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author huangzc
	 */
	@RequestMapping(value = { "/getCourseForNow" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody JKCourseToDayArray getCourseForNow(String claiId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		claiId = getClaid(request.getParameter("claiId"));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String nowTime[] = sdf.format(new Date()).trim().split(":");
		int hour = Integer.parseInt(nowTime[0]);
		int minute = Integer.parseInt(nowTime[1]);
		int courseNum = 2;
		JKCourseToDayArray jtd = getTodaycourse(claiId);
		if (jtd == null || jtd.getMessage() == false || jtd.getJcList() == null || jtd.getJcList().size() <= 0)
			return jtd;
		List<JKCourse> jkcList = jtd.getJcList();
		List<JKCourse> tempJKC = new ArrayList<JKCourse>();
		jtd.setJcList(null);
		SortListUtil<JKCourse> sortJkc = new SortListUtil<JKCourse>();
		sortJkc.Sort(jkcList, "beginTime", "");
		for (JKCourse jkc : jkcList) {
			String tempTime[] = jkc.getBeginTime().trim().split(":");
			int tempBeginHour = Integer.parseInt(tempTime[0]);
			int tempBeginMinute = Integer.parseInt(tempTime[1]);
			if (tempBeginHour < hour)
				continue;
			if (tempBeginHour == hour)
				if (tempBeginMinute <= minute)
					continue;
			tempJKC.add(jkc);
			courseNum--;
			if (courseNum == 0)
				break;
		}
		jtd.setJcList(tempJKC);
		return jtd;
	}

	/**
	 * 根据班级得到当天的课程表
	 * 
	 * @param claiId
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author huangzc
	 */
	public JKCourseToDayArray getTodaycourse(String claiId) throws IOException {
		JKCourseToDayArray jctd = new JKCourseToDayArray();
		if (claiId == null || claiId.trim().equals("")) {
			jctd.setMessage(false);
			jctd.setMessageInfo("请传入参数！");
			return jctd;
		}
		try {
			List<JKCourse> jcList = new ArrayList<JKCourse>();
			int dayNum = DateUtil.mathWeekDay(new Date());// 星期参数
			System.out.println("星期=" + dayNum);
			if (dayNum <= 0) {
				jctd.setMessage(false);
				jctd.setMessageInfo("日期参数有误");
				return jctd;
			}
			List<JwCalenderdetail> canderDetilList = canderDetailService.queryJwTCanderdetailByJwTCander(
					canderService.findJwTcanderByClaiId(classService.findJwTGradeByClaiId(claiId)));// 校历详细列表
			System.out.println("校历详细列表=" + canderDetilList.size());
			if (canderDetilList == null || canderDetilList.size() <= 0) {
				jctd.setMessage(false);
				jctd.setMessageInfo("校历详细列表为空");
				return jctd;
			}
			StringBuffer hql = new StringBuffer("from JwCourseArrange where claiId='");
			hql.append(claiId).append("' and extField05=1  order by className,teachTime asc");
			List<JwCourseArrange> jtaList = thisService.doQuery(hql.toString());// 执行查询方法得到班级课程表
			if (jtaList == null || jtaList.size() <= 0) {
				jctd.setMessage(false);
				jctd.setMessageInfo("查询班级课表无信息");
				return jctd;
			}
			// 数据处理
			List<JwCalenderdetail> canderDetilListed = new ArrayList<JwCalenderdetail>();
			for (JwCalenderdetail jtc : canderDetilList) {
				if (jtc.getJcCode() == null || jtc.getJcCode().trim().equals(""))
					continue;
				canderDetilListed.add(jtc);
			}
			SortListUtil<JwCalenderdetail> slu = new SortListUtil<JwCalenderdetail>();
			slu.Sort(canderDetilListed, "jcCode", null);
			SortListUtil<JwCourseArrange> jta = new SortListUtil<JwCourseArrange>();
			jta.Sort(jtaList, "teachTime", null);
			SimpleDateFormat simpl = new SimpleDateFormat("HH:mm");
			for (JwCalenderdetail tempJtc : canderDetilListed) {
				JKCourse jc = new JKCourse();
				boolean flag = false;
				for (JwCourseArrange tempJta : jtaList) {
					if (tempJtc.getJcCode().equals(tempJta.getTeachTime())) {
						jc.setJcName(tempJtc.getJcName());
						jc.setBeginTime(simpl.format(tempJtc.getBeginTime()));
						jc.setEndTime(simpl.format(tempJtc.getEndTime()));
						switch (dayNum) {
						case 1:
							jc.setTeachrName(tempJta.getTeacherName01());
							jc.setCourseName(tempJta.getCourseName01());
							flag = true;
							break;
						case 2:
							jc.setTeachrName(tempJta.getTeacherName02());
							jc.setCourseName(tempJta.getCourseName02());
							flag = true;
							break;
						case 3:
							jc.setTeachrName(tempJta.getTeacherName03());
							jc.setCourseName(tempJta.getCourseName03());
							flag = true;
							break;
						case 4:
							jc.setTeachrName(tempJta.getTeacherName04());
							jc.setCourseName(tempJta.getCourseName04());
							flag = true;
							break;
						case 5:
							jc.setTeachrName(tempJta.getTeacherName05());
							jc.setCourseName(tempJta.getCourseName05());
							flag = true;
							break;
						case 6:
							jc.setTeachrName(tempJta.getTeacherName06());
							jc.setCourseName(tempJta.getCourseName06());
							flag = true;
							break;
						case 7:
							jc.setTeachrName(tempJta.getTeacherName07());
							jc.setCourseName(tempJta.getCourseName07());
							flag = true;
							break;
						default:
							break;
						}
						break;
					}
				}
				if (flag)
					jcList.add(jc);
			}
			if (jcList != null && jcList.size() > 0) {
				jctd.setJcList(jcList);
				jctd.setDayFoWeek(String.valueOf(dayNum));
				jctd.setMessage(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(jctd);
		return jctd;
	}

	public String getClaid(String roomCode) {
		BuildRoominfo bRoomInfo = brService.getByProerties("roomCode", roomCode);
		if (bRoomInfo == null)
			return null;
		JwClassRoomAllot classRoom = jraService.getByProerties("roomId", bRoomInfo.getUuid());
		if (classRoom == null)
			return null;
		JwTGradeclass gradeClass = classService.get(classRoom.getClaiId());// 班级对象
		if (gradeClass == null)
			return null;
		return gradeClass.getUuid();
	}
}