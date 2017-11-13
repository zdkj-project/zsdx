package com.zd.school.app;

import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.model.app.CourseCheckApp;
import com.zd.school.jw.model.app.TrainCourseApp;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.model.vo.VoTrainClasstrainee;
import com.zd.school.jw.train.service.*;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.plartform.comm.model.CommAttachment;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/traincourse/")
public class TrainCourseAppController {

	@Resource
	private TrainClassscheduleService courseService;
	@Resource
	private TrainClasstraineeService traineeService;
	@Resource
	private TrainClasstraineeService classtraineeService;
	@Resource
	private OaInfotermService termService;
	@Resource
	private TrainCheckruleService ruleService;
	@Resource
	private TrainCourseattendService attendService;
	@Resource
	private TrainClassService classService;
	
	private static Logger logger = Logger.getLogger(TrainCourseAppController.class);

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody TrainCourseApp courselist(String termCode, String time, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		// 传过来的时间为yyyy-MM-dd HH：mm：ss
		TrainCourseApp tca = new TrainCourseApp();
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
			
			Map<String,List<VoTrainClasstrainee>> voMapList = new HashMap<>();
			List<VoTrainClasstrainee> voList=null;
			Map<String,TrainCheckrule> voMapRule = new HashMap<>();
			TrainCheckrule voRule=null;
			Map<String,List<CommAttachment>> voMapAttaList = new HashMap<>();
			List<CommAttachment> voAttaList=null;
			List<TrainClassschedule> course = null;
			String bothClassId=null;
			int isSameClass=0;	//如果班级一样， 就返回0，否则1
			if (ModelUtil.isNotNull(roomTerm)) {
				if (date.getTime() < date2.getTime()) {
					// course = courseService.doQuery("from TrainClassschedule
					// where scheduleAddress='" + roomTerm.getRoomName() + "'
					// and beginTime Between '" + s + " 06:00:00' And '" +
					// sdf.format(date2) + "' order by beginTime asc");
					course = courseService.doQuery("from TrainClassschedule where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%' and beginTime Between '" + s + " 06:00:00" + "' And '"
							+ sdf.format(date2) + "' order by beginTime asc",0,3);
				} else if (date.getTime() < date3.getTime()) {
					course = courseService.doQuery("from TrainClassschedule where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%' and beginTime Between '" + s + " 12:00:00" + "' And '"
							+ sdf.format(date3) + "' order by beginTime asc",0,3);
				} else {
					course = courseService.doQuery("from TrainClassschedule where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%' and beginTime Between '" + s + " 18:00:00" +  "' And '"
							+ sdf.format(date4) + "' order by beginTime asc",0,3);
				}

//				if (course.size() > 0) {
//					// 同一教室，同一时间段只可能有一个培训班(NO)
//					String classId = course.get(0).getClassId();
//					String classScheduleId = course.get(0).getUuid();
//					TrainClass classInfo = classService.get(classId);
//					String sql = MessageFormat.format(" SELECT CLASS_ID AS classId,xm,CLASS_TRAINEE_ID AS traineeId,"
//							+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
//							+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo "
//							+ " FROM dbo.TRAIN_T_CLASSTRAINEE WHERE ISDELETE=0 and  CLASS_ID=''{0}''", classId);
//					volist = classService.doQuerySqlObject(sql, VoTrainClasstrainee.class);
//					for (TrainClassschedule c : course) {
//						c.setList(volist);
//					}
//
//					TrainCheckrule checkrule = ruleService.get(classInfo.getCheckruleId());
//					tca.setCheckrule(checkrule);
//
//					sql = MessageFormat.format(
//							"SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''TrainClass'' AND RECORD_ID=''{0}''",
//							classId);
//					List<CommAttachment> attachmentList = classService.doQuerySqlObject(sql, CommAttachment.class);
//					tca.setAttachment(attachmentList);
//
//				} else {
//					tca.setCode(false);
//					tca.setMessage("数据异常调用失败,没有对应时间的课程");
//					return tca;
//				}

				String sql = "";
				for (TrainClassschedule c : course) {
					// 同一教室，同一时间段只可能有一个培训班(NO)
					String classId = c.getClassId();
					TrainClass classInfo = classService.get(classId);
					voList=voMapList.get(classId);
					if(voList==null){
						sql = MessageFormat.format(" SELECT CLASS_ID AS classId,xm,CLASS_TRAINEE_ID AS traineeId,"
								+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
								+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
								+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState "
								+ " FROM dbo.TRAIN_T_CLASSTRAINEE WHERE ISDELETE=0 and  CLASS_ID=''{0}''", classId);
						voList=classService.doQuerySqlObject(sql, VoTrainClasstrainee.class);
						voMapList.put(classId,voList);
					}
					c.setList(voList);
					
					voRule=voMapRule.get(classId);
					if(voRule==null){
						TrainCheckrule checkRule = new TrainCheckrule();
						voRule=ruleService.get(classInfo.getCheckruleId());
						if(voRule!=null){
							BeanUtils.copyProperties(checkRule, voRule);
							voMapRule.put(classId,checkRule);
							c.setCheckRule(checkRule);	
						}else{
							c.setCheckRule(null);
						}
						
					}else{
						bothClassId=classId;
						c.setCheckRule(voRule);
					}
					
					voAttaList=voMapAttaList.get(classId);
					if(voAttaList==null){
						sql = MessageFormat.format(
								"SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''TrainClass'' AND RECORD_ID=''{0}''",
								classId);
						voAttaList = classService.doQuerySqlObject(sql, CommAttachment.class);
						voMapAttaList.put(classId,voAttaList);
					}
					c.setAttachmentlist(voAttaList);
				}
				
				//如果map大于1，则代表有多个班级
				if(voMapList.size()>1){
					isSameClass=1;
				}

				//如果map大于2，则代表3个班都不同,将考勤模式强制设置为1（按节次考勤）
				if(voMapRule.size()>2){
					for (Map.Entry<String,TrainCheckrule> entry : voMapRule.entrySet()) {  
						entry.getValue().setCheckMode((short) 1);
					}  
				}else if(voMapRule.size()==2){	//如果等于2，则表明有两个班一样 或者 两个不同的班，则设置为考勤模式为1
					for (String key: voMapRule.keySet()) {  
						if(!key.equals(bothClassId))
							voMapRule.get(key).setCheckMode((short) 1);					
					}  
				}
			}else{
				tca.setCode(false);
				tca.setMessage("没有找到该终端设备！");
	            return tca;
			}
			tca.setCode(true);
			tca.setMessage("调用成功");
			tca.setCourse(course);
			tca.setIsSameClass(isSameClass);
			tca.setRoomName(roomTerm.getRoomName());
			return tca;

		} catch (Exception e) {
			tca.setCode(false);
			tca.setMessage("数据异常调用失败" + e.getMessage());
			return tca;
		}

	}

	@RequestMapping(value = { "/rule" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody TrainCheckrule rule(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		TrainCheckrule rule = ruleService.getByProerties("startUsing", (short) 1);

		return rule;

	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody CourseCheckApp update(String coursecheck, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		
		logger.info("提交课程考勤数据-->参数coursecheck："+coursecheck);
		
		Date currentDate = new Date();
		List<CourseCheck> check = null;
		if (null != coursecheck) {
			check = (List<CourseCheck>) JsonBuilder.getInstance().fromJsonArray(coursecheck, CourseCheck.class);
		}
		CourseCheckApp cca = new CourseCheckApp();

		TrainCourseattend attend = null;
		TrainClassschedule course = null;
		try {
			if (coursecheck.length() > 0) {
				for (CourseCheck t : check) {
					/*
					 * String uid = ""; List<Object[]> obj =
					 * termService.ObjectQuerySql(
					 * "select USER_ID,CARDNO from dbo.PT_CARD where FACTORYFIXID='"
					 * + t.getWlkh() + "'"); uid = obj.get(0)[0].toString();
					 */

					// zzk修改：若courseId的数据有多个，逗号分隔，则按课程存入
					String[] courseIds = t.getCourseId().split(",");
					String[] param = { "classId", "classScheduleId", "traineeId" };

					for (int i = 0; i < courseIds.length; i++) {
						course = courseService.get(courseIds[i]);
						//当课程为自选课程，且学生为缺勤的时候，不计入考勤数据(课程分班和学员分班的数据，在sql统计中处理)
						if(course.getIsOptional()==1){
							if("4".equals(t.getAttendResult()))
								continue;
						}
						
						Object[] values = { t.getClassId(), courseIds[i], t.getUserId() };
						// Object[] values = {t.getClassId(), t.getCourseId(),
						// uid};
						attend = attendService.getByProerties(param, values);	//造成每次查库，是否有必要
						if (attend == null)
							attend = new TrainCourseattend();
						// if (attend != null) {
						attend.setClassId(t.getClassId());
						attend.setClassScheduleId(courseIds[i]);
						attend.setTraineeId(t.getUserId());
						attend.setBeginTime(course.getBeginTime());
						attend.setEndTime(course.getEndTime());
						if (t.getLg().equals("0")) {
							attend.setIncardTime(t.getTime());
						} else if (t.getLg().equals("1")) {
							attend.setOutcardTime(t.getTime());
						}
						attend.setAttendResult(t.getAttendResult());
						attend.setUpdateTime(currentDate);
						attendService.merge(attend);
					}

					/*
					 * } else { cca.setCode(false);
					 * cca.setMessage("查询不到对应的考勤人员"); return cca; }
					 */

				}
				cca.setCode(true);
				cca.setMessage("存储数据成功");
				logger.info("提交课程考勤成功!");
				return cca;
			} else {
				cca.setCode(false);
				cca.setMessage("没有上传考勤数据");
				logger.info("没有上传考勤数据!");
				return cca;
			}

		} catch (Exception e) {
			cca.setCode(false);
			cca.setMessage("存储异常" + e.getMessage());
			logger.error("提交课程考勤失败-->message:"+e.getStackTrace());			
			return cca;
		}

	}

}