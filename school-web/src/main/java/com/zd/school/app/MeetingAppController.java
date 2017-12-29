package com.zd.school.app;

import com.orcl.sync.controller.UserSyncController;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.model.app.MeetCheckApp;
import com.zd.school.jw.model.app.MeetingApp;
import com.zd.school.jw.model.app.ResponseApp;
import com.zd.school.oa.meeting.model.MeetingCheck;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingcheckrule;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingcheckruleService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.plartform.comm.model.CommAttachment;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/meeting/")
public class MeetingAppController {
	@Resource
	private OaMeetingService meetingService;
	@Resource
	private OaMeetingempService empService;
	@Resource
	private OaInfotermService termService;
	@Resource
	private OaMeetingcheckruleService ruleService;

	private static Logger logger = Logger.getLogger(MeetingAppController.class);

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetingApp meetinglist(String termCode, String time, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		MeetingApp info = new MeetingApp(); // 返回此数据

		try {
			// 判断时间查询上午下午
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(time);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String s = dateFormat.format(date);
			Date date2 = sdf.parse(s + " 12:00:00");
			Date date3 = sdf.parse(s + " 18:00:00");
			Date date4 = sdf.parse(s + " 23:59:59");

			// 设备号查询房间
			OaInfoterm roomTerm = termService.getByProerties("termCode", termCode);

			List<OaMeeting> meeting = null;
			OaMeetingcheckrule checkrule = null;
			List<Object[]> obj = null;
			if (ModelUtil.isNotNull(roomTerm)) {
				if (date.getTime() < date2.getTime()) {
					// course = courseService.doQuery("from TrainClassschedule
					// where scheduleAddress='" + roomTerm.getRoomName() + "'
					// and beginTime Between '" + s + " 06:00:00' And '" +
					// sdf.format(date2) + "' order by beginTime asc");
					// meeting = meetingService.doQuery("from OaMeeting where
					// isDelete=0 and roomId='" + roomTerm.getRoomId() + "' and
					// Convert(varchar,beginTime,120) like'" + time + "%' order
					// by beginTime asc");
					// 注：我们正式使用时需要将可能作为会议室的房间名称发给OA，所以要将房间查询方式换为id
					meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 06:00:00" + "' And '"
							+ sdf.format(date2) + "' order by beginTime asc");
				} else if (date.getTime() < date3.getTime()) {
					meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 12:00:00" + "' And '"
							+ sdf.format(date3) + "' order by beginTime asc");
				} else {
					meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 18:00:00" + "' And '"
							+ sdf.format(date4) + "' order by beginTime asc");
				}

				for (OaMeeting o : meeting) {
					o.setBegin(timeFormat.format(o.getBeginTime()));
					o.setEnd(timeFormat.format(o.getEndTime()));

					List<OaMeetingemp> emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete" },
							new Object[] { o.getUuid(), 0 });
					o.setMeetingemp(emplist);

					// 统计请假人数
					int leaveNum = 0;
					for (OaMeetingemp emp : emplist) {
						if ("1".equals(emp.getIsLeave())) {
							leaveNum++;
						}
					}
					o.setExtField05(String.valueOf(leaveNum));

					checkrule = ruleService.get(o.getCheckruleId());
					o.setOaMeetingcheckrule(checkrule);

					String sql = MessageFormat.format(
							"SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE  ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''OaMeeting'' AND RECORD_ID=''{0}''",
							o.getUuid());
					List<CommAttachment> attachmentList = meetingService.doQuerySqlObject(sql, CommAttachment.class);
					// if(attachmentList.size()==0){
					// CommAttachment tempAtt=new CommAttachment();
					// tempAtt.setAttachName("image");
					// tempAtt.setAttachUrl("/static/core/resources/images/defaultMettingImg.png");
					// attachmentList.add(tempAtt);
					// }
					o.setAttachment(attachmentList);
				}

			} else {
				info.setCode(false);
				info.setMessage("没有找到该终端设备！");
				return info;
			}

			info.setCode(true);
			info.setMessage("请求成功！");
			info.setMeeting(meeting);
			return info;
		} catch (Exception e) {
			info.setCode(false);
			info.setMessage("数据异常调用失败" + Arrays.toString(e.getStackTrace()));
			return info;
		}

	}

	@RequestMapping(value = { "/rule" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody OaMeetingcheckrule rule(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		OaMeetingcheckrule rule = ruleService.getByProerties("startUsing", (short) 1);

		return rule;
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetCheckApp update(String meetcheck, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {

		logger.info("提交会议考勤数据-->参数meetcheck：" + meetcheck);
		List<MeetingCheck> check = null;
		Date currentDate = new Date();
		if (null != meetcheck) {
			check = (List<MeetingCheck>) JsonBuilder.getInstance().fromJsonArray(meetcheck, MeetingCheck.class);
		}
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		MeetCheckApp mca = new MeetCheckApp();
		try {
			for (MeetingCheck mc : check) {
				OaMeeting meeting = meetingService.get(mc.getMeetingId());
				// 考勤人员id
				// String uid = empService.getForValueToSql("select USER_ID from
				// dbo.CARD_T_USEINFO where FACT_NUMB='" + mc.getWlkh() + "'");

				// String[] param = {"meetingId", "employeeId"};
				// Object[] values = {mc.getMeetingId(),mc.getUserId()};
				OaMeetingemp emp = empService.getByProerties("uuid", mc.getUserId());
				if (emp != null) {
					emp.setMeetingId(meeting.getUuid());
					if (mc.getLg().equals("0")) {
						if (emp.getIncardTime() == null) {
							emp.setIncardTime(mc.getTime());
							emp.setInResult(mc.getInResult());
							emp.setAttendResult(mc.getAttendResult());
						} else if (mc.getTime() != null) {
							// 解决多个平板上传数据的问题
							calendar1.setTime(emp.getIncardTime());
							calendar2.setTime(mc.getTime());
							if (calendar1.compareTo(calendar2) == 1) { // 若第二次上传的时间【小于】第一次的时间，那就替换
								emp.setIncardTime(mc.getTime());
								emp.setInResult(mc.getInResult());
								emp.setAttendResult(mc.getAttendResult());
							}
						}
					} else if (mc.getLg().equals("1")) {
						if (emp.getOutcardTime() == null) {
							emp.setOutcardTime(mc.getTime());
							emp.setOutResult(mc.getOutResult());
							emp.setAttendResult(mc.getAttendResult());
						} else if (mc.getTime() != null) {
							// 解决多个平板上传数据的问题
							calendar1.setTime(emp.getOutcardTime());
							calendar2.setTime(mc.getTime());
							if (calendar1.compareTo(calendar2) == -1) { // 若第二次上传的时间【大于】第一次的时间，那就替换
								emp.setOutcardTime(mc.getTime());
								emp.setOutResult(mc.getOutResult());
								emp.setAttendResult(mc.getAttendResult());
							}
						}
					} else {
						// 当为缺勤的时候，并且判断之前没有考勤数据，就设置为缺勤
						if (emp.getIncardTime() == null && emp.getOutcardTime() == null) {
							emp.setAttendResult(mc.getAttendResult());
						}
					}
					emp.setUpdateTime(currentDate);
					empService.merge(emp);
				}
				/*
				 * else { mca.setCode(false);
				 * mca.setMessage("会议或者人员ID错误，找不到对应参会人员信息。"); return mca; }
				 */
			}
			mca.setCode(true);
			mca.setMessage("存储数据成功");

			logger.info("提交会议成功!");
			return mca;
		} catch (Exception e) {
			mca.setCode(false);
			mca.setMessage("参数错误" + e.getMessage());
			logger.error("提交会议失败-->message:" + Arrays.toString(e.getStackTrace()));
			return mca;
		}
	}

	/**
	 * 移动端的会议数据接口 通过时间点，获取当前时间段的【未分配教室+设备】的会议数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getMobileList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetingApp getMobileList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		MeetingApp info = new MeetingApp(); // 返回此数据

		try {
			// 判断时间查询上午下午
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String s = dateFormat.format(date);
			Date date2 = sdf.parse(s + " 12:30:00");
			Date date3 = sdf.parse(s + " 18:30:00");
			Date date4 = sdf.parse(s + " 23:59:59");

			List<OaMeeting> meeting = null;
			// OaMeetingcheckrule checkrule = null;

			if (date.getTime() < date2.getTime()) {
				// 注：我们正式使用时需要将可能作为会议室的房间名称发给OA，所以要将房间查询方式换为id
				meeting = meetingService.doQuery("from OaMeeting o where o.isDelete=0 and o.beginTime Between '" + s
						+ " 06:00:00" + "' And '" + sdf.format(date2) + "' "
						+ " and (o.roomId is null or o.roomId not in("
						+ " select roomId from OaInfoterm where roomId is not null" + ")) order by o.beginTime asc");
			} else if (date.getTime() < date3.getTime()) {
				meeting = meetingService.doQuery("from OaMeeting o where o.isDelete=0 and o.beginTime Between '" + s
						+ " 12:30:00" + "' And '" + sdf.format(date3) + "' "
						+ " and (o.roomId is null or o.roomId not in("
						+ " select roomId from OaInfoterm where roomId is not null" + ")) order by o.beginTime asc");
			} else {
				meeting = meetingService.doQuery("from OaMeeting o where o.isDelete=0 and o.beginTime Between '" + s
						+ " 18:30:00" + "' And '" + sdf.format(date4) + "' "
						+ " and (o.roomId is null or o.roomId not in("
						+ " select roomId from OaInfoterm where roomId is not null" + ")) order by o.beginTime asc");
			}
			for (OaMeeting o : meeting) {
				o.setBegin(timeFormat.format(o.getBeginTime()));
				o.setEnd(timeFormat.format(o.getEndTime()));

				List<OaMeetingemp> emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete" },
						new Object[] { o.getUuid(), 0 });
						// o.setMeetingemp(emplist); 不返回人员

				// 统计总人数
				int countNum = emplist.size();
				o.setExtField03(String.valueOf(countNum));

				// 统计已签到人数
				long inResultNum = emplist.stream().filter((e) -> "1".equals(e.getInResult())).count();
				o.setExtField04(String.valueOf(inResultNum));

				// 统计请假人数
				long leaveNum = emplist.stream().filter((e) -> "1".equals(e.getIsLeave())).count();
				o.setExtField05(String.valueOf(leaveNum));

				// checkrule = ruleService.get(o.getCheckruleId());
				// o.setOaMeetingcheckrule(checkrule);不返回规则

				/*
				 * 不返回图片 String sql = MessageFormat.format(
				 * "SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE  ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''OaMeeting'' AND RECORD_ID=''{0}''"
				 * , o.getUuid()); List<CommAttachment> attachmentList =
				 * meetingService.doQuerySqlObject(sql, CommAttachment.class);
				 * // if(attachmentList.size()==0){ // CommAttachment
				 * tempAtt=new CommAttachment(); //
				 * tempAtt.setAttachName("image"); // tempAtt.setAttachUrl(
				 * "/static/core/resources/images/defaultMettingImg.png"); //
				 * attachmentList.add(tempAtt); // }
				 * o.setAttachment(attachmentList);
				 */
			}

			info.setCode(true);
			info.setMessage("请求成功！");
			info.setMeeting(meeting);
			return info;
		} catch (Exception e) {
			info.setCode(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}

	/**
	 * 移动端的会议数据接口 通过时间点，获取当前时间段的【未分配教室+设备】的会议数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getMeetingDeatil" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetingApp getMeetingDeatil(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		MeetingApp info = new MeetingApp(); // 返回此数据

		try {
			String meetingId = request.getParameter("id");

			OaMeetingcheckrule checkrule = null;
			OaMeeting oaMeeting = meetingService.get(meetingId);
			if (oaMeeting != null) {

				oaMeeting.setBegin(timeFormat.format(oaMeeting.getBeginTime()));
				oaMeeting.setEnd(timeFormat.format(oaMeeting.getEndTime()));

				List<OaMeetingemp> emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete" },
						new Object[] { oaMeeting.getUuid(), 0 });
				oaMeeting.setMeetingemp(emplist);

				// 统计总人数
				int countNum = emplist.size();
				oaMeeting.setExtField03(String.valueOf(countNum));

				// 统计已签到人数
				long inResultNum = emplist.stream().filter((e) -> "1".equals(e.getInResult())).count();
				oaMeeting.setExtField04(String.valueOf(inResultNum));

				// 统计请假人数
				long leaveNum = emplist.stream().filter((e) -> "1".equals(e.getIsLeave())).count();
				oaMeeting.setExtField05(String.valueOf(leaveNum));

				checkrule = ruleService.get(oaMeeting.getCheckruleId());
				oaMeeting.setOaMeetingcheckrule(checkrule);

				/*
				 * 不返回图片 String sql = MessageFormat.format(
				 * "SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE  ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''OaMeeting'' AND RECORD_ID=''{0}''"
				 * , o.getUuid()); List<CommAttachment> attachmentList =
				 * meetingService.doQuerySqlObject(sql, CommAttachment.class);
				 * // if(attachmentList.size()==0){ // CommAttachment
				 * tempAtt=new CommAttachment(); //
				 * tempAtt.setAttachName("image"); // tempAtt.setAttachUrl(
				 * "/static/core/resources/images/defaultMettingImg.png"); //
				 * attachmentList.add(tempAtt); // }
				 * o.setAttachment(attachmentList);
				 */
				info.setCode(true);
				info.setMessage("请求成功！");
				info.setMeeting(Arrays.asList(oaMeeting));

			} else {
				info.setCode(false);
				info.setMessage("请求失败，找不到会议数据！");
				info.setMeeting(null);
			}

			return info;
		} catch (Exception e) {
			info.setCode(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}

	/**
	 * 移动端获取会议的签到、未签到、请假的人员信息
	 *  id：会议id 
	 *  type：0-全部 1-已签到 2-未签到 3-请假
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getMeetingUserDeatil" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ResponseApp<List<OaMeetingemp>> getMeetingUserDeatil(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		ResponseApp<List<OaMeetingemp>> info = new ResponseApp<>(); // 返回此数据

		try {
			List<OaMeetingemp> emplist = null;

			String meetingId = request.getParameter("id");
			String type = request.getParameter("type");
			if (StringUtils.isEmpty(meetingId) || StringUtils.isEmpty(type)) {
				info.setSuccess(false);
				info.setMessage("调用失败，缺少请求参数！");
				return info;
			}

			Map<String, String> sortedCondition = new LinkedHashMap<>();
			sortedCondition.put("inResult", "desc");
			sortedCondition.put("incardTime", "desc");

			if ("0".equals(type)) {
				emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete" },
						new Object[] { meetingId, 0 }, sortedCondition);
			} else if ("1".equals(type)) {
				emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete", "inResult" },
						new Object[] { meetingId, 0, "1" }, sortedCondition);

			} else if ("2".equals(type)) {
				String hql = "from OaMeetingemp where meetingId=? and isDelete=0 and (inResult='0' or inResult is null)"
						+ " order by inResult desc,incardTime desc";
				emplist = empService.getForValues(hql, meetingId);

			} else if ("3".equals(type)) {
				emplist = empService.queryByProerties(new String[] { "meetingId", "isDelete", "isLeave" },
						new Object[] { meetingId, 0, "1" }, sortedCondition);

			} else {
				info.setSuccess(false);
				info.setMessage("调用失败，参数不正确！");
				return info;
			}

			info.setSuccess(true);
			info.setMessage("请求成功！");
			info.setObj(emplist);

			return info;
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}

	/**
	 * 处理会议考勤签到 
	 * id：会议id 
	 * factnumb：人员的物理卡号
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/doMobileSignin" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ResponseApp<Object> doMobileAttend(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {

		ResponseApp<Object> info = new ResponseApp<>(); // 返回此数据

		String meetingId = request.getParameter("id");
		String factnumb = request.getParameter("factnumb");
		if (StringUtils.isEmpty(meetingId) || StringUtils.isEmpty(factnumb)) {
			info.setSuccess(false);
			info.setMessage("调用失败，缺少请求参数！");
			return info;
		}

		try {
			Date currentDate = new Date();
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();

			logger.info("提交移动端会议考勤签到--参数：会议ID->" + meetingId + ";人员物理卡号->" + factnumb);

			// 获取会议
			OaMeeting oaMeeting = meetingService.get(meetingId);
			if (oaMeeting == null) {
				info.setSuccess(false);
				info.setMessage("找不到会议信息！");
				return info;
			}
			// 获取会议考勤规则; 若没有设定规则，则直接以会议的开始、结束时间作为约束
			OaMeetingcheckrule checkrule = ruleService.get(oaMeeting.getCheckruleId());

			// 1.通过物理卡号去获取具体的考勤人员，判断此人是否存在这个会议的成员中
			OaMeetingemp emp = empService.getByProerties(new String[] { "meetingId", "isDelete", "factoryfixId" },
					new Object[] { meetingId, 0, Long.valueOf(factnumb) });
			if (emp == null) {
				info.setSuccess(false);
				info.setMessage("此人员不在此会议中！");
				return info;
			}

			// 2.判断是否已签到
			if ("1".equals(emp.getInResult())) {
				info.setSuccess(false);
				info.setMessage(emp.getXm() + "，您已签到，不必重复刷卡！");
				return info;
			}

			// 3.判断签到结果
			if (checkrule == null) {
				calendar1.setTime(currentDate);
				calendar2.setTime(oaMeeting.getBeginTime());
				calendar2.add(Calendar.MINUTE, -30); // 提前30分钟签到
				if (calendar1.compareTo(calendar2) == -1) {
					info.setSuccess(false);
					info.setMessage("未到签到时间，请等待！");
					return info;
				}

				calendar2.setTime(oaMeeting.getEndTime());
				if (calendar1.compareTo(calendar2) == 1) {
					info.setSuccess(false);
					info.setMessage("此会议已结束，不可签到！");
					return info;
				}

				emp.setIncardTime(currentDate); // 设置签到时间
				emp.setInResult("1"); // 设为已签到状态
				emp.setAttendResult("1"); // 没有规则，只要签到成功，即算做正常考勤
				emp.setUpdateTime(currentDate);
				empService.merge(emp); // 保存入库

			} else {

				calendar1.setTime(currentDate);
				calendar2.setTime(oaMeeting.getEndTime());
				if (calendar1.compareTo(calendar2) == 1) {
					info.setSuccess(false);
					info.setMessage("此会议已结束，不可签到！");
					return info;
				}

				calendar1.add(Calendar.MINUTE, checkrule.getInBefore()); // 提前签到
				calendar2.setTime(oaMeeting.getBeginTime());
				if (calendar1.compareTo(calendar2) == -1) {
					info.setSuccess(false);
					info.setMessage("未到签到时间，请等待！");
					return info;
				}

				// 判断结果：1-正常 2-迟到 3-早退 4-缺勤 5-迟到早退
				calendar1.setTime(currentDate);
				calendar2.add(Calendar.MINUTE, checkrule.getAbsenteeism()); // 缺勤时间=签到时间+缺勤分钟
				if (calendar1.compareTo(calendar2) == 1) {
					emp.setAttendResult("4"); // 缺勤

				} else {
					calendar2.setTime(oaMeeting.getBeginTime());
					calendar2.add(Calendar.MINUTE, checkrule.getBeLate()); // 迟到时间=签到时间+迟到分钟
					if (calendar1.compareTo(calendar2) == 1)
						emp.setAttendResult("2"); // 迟到
					else
						emp.setAttendResult("1"); // 正常
				}

				emp.setIncardTime(currentDate); // 设置签到时间
				emp.setInResult("1"); // 设为已签到状态
				emp.setUpdateTime(currentDate);
				empService.merge(emp); // 保存入库

			}

			info.setSuccess(true);
			info.setMessage(emp.getXm() + "，签到成功！");

			// 获取已签到成功人数		
			String hql = "select count(*) from OaMeetingemp where meetingId=? and isDelete=0 and inResult='1'";
			Long num = empService.getForValue(hql, meetingId);	
			info.setObj(num);
			
			logger.info("提交会议签到信息成功!");
			
			return info;

		} catch (Exception e){
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			logger.error("提交会议签到信息失败-->message:" + Arrays.toString(e.getStackTrace()));
			return info;				
		}
	}
}
