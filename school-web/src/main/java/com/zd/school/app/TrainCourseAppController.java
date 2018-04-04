package com.zd.school.app;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.model.app.CourseCheckApp;
import com.zd.school.jw.model.app.ResponseApp;
import com.zd.school.jw.model.app.TrainCourseApp;
import com.zd.school.jw.train.model.CourseCheck;
import com.zd.school.jw.train.model.TrainCheckrule;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainCourseattend;
import com.zd.school.jw.train.model.vo.VoTrainClasstrainee;
import com.zd.school.jw.train.service.TrainCheckruleService;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.jw.train.service.TrainCourseattendService;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.plartform.comm.model.CommAttachment;

@Controller
@RequestMapping("/app/traincourse/")
public class TrainCourseAppController {

	@Resource
	private TrainClassscheduleService courseService;

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
					course = courseService.getQuery("from TrainClassschedule where (isDelete=0 or isDelete=2) and roomId like '%"
							+ roomTerm.getRoomId() + "%' and beginTime Between '" + s + " 06:00:00" + "' And '"
							+ sdf.format(date2) + "' order by beginTime asc",0,3);
				} else if (date.getTime() < date3.getTime()) {
					course = courseService.getQuery("from TrainClassschedule where (isDelete=0 or isDelete=2)  and roomId like '%"
							+ roomTerm.getRoomId() + "%' and beginTime Between '" + s + " 12:00:00" + "' And '"
							+ sdf.format(date3) + "' order by beginTime asc",0,3);
				} else {
					course = courseService.getQuery("from TrainClassschedule where (isDelete=0 or isDelete=2)  and roomId like '%"
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
						sql = MessageFormat.format("SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
								+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
								+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
								+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
								+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave "
								+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
								+ "	LEFT JOIN dbo.TRAIN_T_COURSEATTEND b "
								+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' "
								+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}''", c.getUuid() , classId);
						
//						sql = MessageFormat.format(" SELECT CLASS_ID AS classId,xm,CLASS_TRAINEE_ID AS traineeId,"
//								+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
//								+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
//								+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
//								+ "IS_LEAVE AS isLeave "
//								+ " FROM dbo.TRAIN_T_CLASSTRAINEE WHERE ISDELETE=0 and  CLASS_ID=''{0}''", classId);
						voList=classService.getQuerySqlObject(sql, VoTrainClasstrainee.class);
						voMapList.put(classId,voList);
						
						//统计请假人数
	 	                int leaveNum=0;
						for(int i=0;i<voList.size();i++){									 	           
	 	                	if("1".equals(voList.get(i).getIsLeave())){
	 	                		leaveNum++;
	 	                	}		 	               
						}
						c.setExtField05(String.valueOf(leaveNum));					
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
						voAttaList = classService.getQuerySqlObject(sql, CommAttachment.class);
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
			tca.setMessage("数据异常调用失败" +  Arrays.toString( e.getStackTrace()));
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
		Calendar calendar1 = Calendar.getInstance();
	    Calendar calendar2 = Calendar.getInstance();
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
						
//						if (t.getLg().equals("0")) {
//							attend.setIncardTime(t.getTime());
//						} else if (t.getLg().equals("1")) {
//							attend.setOutcardTime(t.getTime());
//						}
//						attend.setAttendResult(t.getAttendResult());
											
						if (t.getLg().equals("0")) {
	                    	if(attend.getIncardTime()==null){
	                    		attend.setIncardTime(t.getTime());	                    		
	                    		attend.setAttendResult(t.getAttendResult());
	                    	}else if(t.getTime()!=null){
	                    		//解决多个平板上传数据的问题
	                    		calendar1.setTime(attend.getIncardTime());
	                        	calendar2.setTime(t.getTime());
	                        	if(calendar1.compareTo(calendar2)==1){	//若第二次上传的时间【小于】第一次的时间，那就替换
	                        		attend.setIncardTime(t.getTime());
	                        		attend.setAttendResult(t.getAttendResult());
	                        	}
	                    	}
	                    } else if(t.getLg().equals("1")) {
	                    	if(attend.getOutcardTime()==null){
	                    		attend.setOutcardTime(t.getTime());
	                    		attend.setAttendResult(t.getAttendResult());
	                    	}else if(t.getTime()!=null){
	                    		//解决多个平板上传数据的问题
	                    		calendar1.setTime(attend.getOutcardTime());
	                        	calendar2.setTime(t.getTime());
	                        	if(calendar1.compareTo(calendar2)==-1){	//若第二次上传的时间【大于】第一次的时间，那就替换
	                        		attend.setOutcardTime(t.getTime());
	                        		attend.setAttendResult(t.getAttendResult());
	                        	}
	                    	}
	                    }else{
	                    	//当为缺勤的时候，并且判断之前没有考勤数据，就设置为缺勤
	                    	if(attend.getIncardTime()==null && attend.getOutcardTime()==null){
	                    		attend.setAttendResult(t.getAttendResult());
	                    	}
	                    }
						
						
						attend.setUpdateTime(currentDate);
						attendService.doMerge(attend);
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
			logger.error("提交课程考勤失败-->message:"+ Arrays.toString( e.getStackTrace()));			
			return cca;
		}

	}
	
	
	/**
	 * 移动端的班级数据接口 通过时间点，获取当前时间段的【未分配教室+设备】的班级课程考勤数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getMobileClassList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ResponseApp<List<Map<String, String>>> getMobileClassList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
		ResponseApp<List<Map<String,String>>> info = new ResponseApp<>(); // 返回此数据
		try {
			// 判断时间查询上午下午
			Date date = new Date();
			
			String s = dateFormat.format(date);
			Date date2 = sdf.parse(s + " 12:30:00");
			Date date3 = sdf.parse(s + " 18:30:00");
			Date date4 = sdf.parse(s + " 23:59:59");
			
			// OaMeetingcheckrule checkrule = null;
			List<Object[]> classInfo=null;
			
			if (date.getTime() < date2.getTime()) {
				classInfo = courseService.getForValues("select distinct o.classId,o.className from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 06:00:00" + "' And '"
						+ sdf.format(date2) + "' ");
				
			} else if (date.getTime() < date3.getTime()) {
				classInfo = courseService.getForValues("select distinct o.classId,o.className from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 12:30:00" + "' And '"
						+ sdf.format(date3) + "' ");
						
			} else {
				classInfo = courseService.getForValues("select distinct o.classId,o.className from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 18:30:00" + "' And '"
						+ sdf.format(date4) + "' ");				
			}
				
			if(classInfo.size()==0){
				info.setSuccess(false);
				info.setMessage("没有需要移动考勤的班级！");			
				return info;	//没有班级
			}
			
			//转为hashmap
			List<Map<String,String>> listMap=new ArrayList<>();
			TrainClass trainClass=null;
			for(int i=0;i<classInfo.size();i++){
				
				//只显示需要考勤的班级
				trainClass = classService.get(String.valueOf(classInfo.get(i)[0]));	
				if(trainClass!=null && trainClass.getNeedChecking()==1){
					Map<String,String> map=new HashMap<>();
					map.put("classId", String.valueOf(classInfo.get(i)[0]));
					map.put("className", String.valueOf(classInfo.get(i)[1]));
					listMap.add(map);
				}			
				
			}
			
			info.setSuccess(true);
			info.setMessage("请求成功！");
			info.setObj(listMap);
			return info;
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}
	
	/**
	 * 获取班级的班级课程详情信息（某一个时段）。
	 * classId：班级id
	 * @param request
	 * @param response
	 * @return 
	 * 参数说明：
	 * 	是否分班：classGroup->不分班、A班、B班 （0、1、2）
	 * 	是否选修：isOptional->0-必修，1-选修
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getClassCourseList" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ResponseApp<List<TrainClassschedule>>  getClassCourseList(@RequestParam("classId") String classId)
			throws IOException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
		ResponseApp<List<TrainClassschedule>> info = new ResponseApp<>(); // 返回此数据
		try {
		
			// 判断时间查询上午下午
			Date date = new Date();
			
			String s = dateFormat.format(date);
			Date date2 = sdf.parse(s + " 12:30:00");
			Date date3 = sdf.parse(s + " 18:30:00");
			Date date4 = sdf.parse(s + " 23:59:59");
			
			TrainClass classInfo = classService.get(classId);	
			if(classInfo==null){
				info.setSuccess(false);
				info.setMessage("不存在此班级！");			
				return info;	//没有班级
			}
			TrainCheckrule checkRule = null;			
			List<TrainClassschedule> courseInfo=null;
			List<VoTrainClasstrainee> trainees=null;
			
			if (date.getTime() < date2.getTime()) {
				courseInfo = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 06:00:00" + "' And '"
						+ sdf.format(date2) + "' "
						+ " and o.classId='"+classId+"'  order by o.beginTime asc");
				
			} else if (date.getTime() < date3.getTime()) {
				courseInfo = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 12:30:00" + "' And '"
						+ sdf.format(date3) + "' "
						+ " and o.classId='"+classId+"'   order by o.beginTime asc");
						
			} else {
				courseInfo = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
						+ " and (o.roomId is null or o.roomId not in("
						+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
						+ " and o.beginTime Between '" + s + " 18:30:00" + "' And '"
						+ sdf.format(date4) + "' "
						+ " and o.classId='"+classId+"'   order by o.beginTime asc");
			}
				
			if(courseInfo.size()==0){
				info.setSuccess(false);
				info.setMessage("没有需要移动考勤的班级课程！");			
				return info;	//没有班级课程
			}
			
			String sql="";
			for (TrainClassschedule c : courseInfo) {
				// 同一教室，同一时间段只可能有一个培训班(NO)					
				//new：加入学员和课程的分班情况筛选
				//如果课程是不分班，则所有学员都可以参加
				
				if("0".equals(c.getClassGroup())){
					sql = MessageFormat.format("SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
							+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
							+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
							+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
							+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
							+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
							+ "	LEFT JOIN dbo.TRAIN_T_COURSEATTEND b "
							+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' "
							+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}''", c.getUuid() , classId);
						
				}else{//否则只能是对应分班的学员才能参加
					sql = MessageFormat.format("SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
							+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
							+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
							+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
							+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
							+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
							+ "	LEFT JOIN dbo.TRAIN_T_COURSEATTEND b "
							+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' "
							+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}'' and X.CLASSGROUP=''{2}''", c.getUuid() , classId,c.getClassGroup());
						
				}
				
				trainees=classService.getQuerySqlObject(sql, VoTrainClasstrainee.class);
					
				// 统计总人数
				int countNum = trainees.size();
				c.setExtField03(String.valueOf(countNum));

				// 统计已签到人数
				long inResultNum = trainees.stream().filter((e) ->e.getIncardTime()!=null).count();
				c.setExtField04(String.valueOf(inResultNum));

				// 统计请假人数
				long leaveNum = trainees.stream().filter((e) -> "1".equals(e.getIsLeave())).count();
				c.setExtField05(String.valueOf(leaveNum));
																	
				c.setList(trainees);	
				
				checkRule=ruleService.get(classInfo.getCheckruleId());
				c.setCheckRule(checkRule);															
			}
		
			info.setSuccess(true);
			info.setMessage("请求成功！");
			info.setObj(courseInfo);
			return info;
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}

	/**
	 * 移动端获取课程的签到、未签到、请假的人员信息
	 * @param classId	班级id 
	 * @param courseUuid	班级课程id 
	 * @param type	0-全部 1-已签到 2-未签到 3-请假
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getCourseUserDeatil" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody ResponseApp<List<VoTrainClasstrainee>> getMeetingUserDeatil(
			@RequestParam("classId") String classId,
			@RequestParam("courseUuid") String courseId,
			@RequestParam("type") String type) throws IOException, ParseException {

		ResponseApp<List<VoTrainClasstrainee>> info = new ResponseApp<>(); // 返回此数据

		try {
			List<VoTrainClasstrainee> traineeList = null;	
			
			TrainClassschedule courseInfo = courseService.get(courseId);	
			if(courseInfo==null){
				info.setSuccess(false);
				info.setMessage("不存在此班级课程！");			
				return info;	//没有班级课程
			}
			
			String sql="";
			if ("0".equals(type)) {
				sql ="SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
						+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
						+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
						+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
						+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
						+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
						+ "	LEFT JOIN dbo.TRAIN_T_COURSEATTEND b "
						+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' "
						+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}''";
				if("0".equals(courseInfo.getClassGroup())){
					sql = MessageFormat.format(sql,courseId , classId);
				}else{
					sql+="  and X.CLASSGROUP=''{2}''";
					sql = MessageFormat.format(sql,courseId , classId,courseInfo.getClassGroup());
				}
				
			} else if ("1".equals(type)) {
				sql ="SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
						+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
						+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
						+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
						+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
						+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
						+ "	JOIN dbo.TRAIN_T_COURSEATTEND b "
						+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' "
						+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}'' and b.INCARD_TIME is not null";
				if("0".equals(courseInfo.getClassGroup())){
					sql = MessageFormat.format(sql,courseId , classId);
				}else{
					sql+="  and X.CLASSGROUP=''{2}''";
					sql = MessageFormat.format(sql,courseId , classId,courseInfo.getClassGroup());
				}
								
			} else if ("2".equals(type)) {				
				sql ="SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
						+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
						+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
						+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
						+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
						+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
						+ "	LEFT JOIN dbo.TRAIN_T_COURSEATTEND b "
						+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}'' AND  b.INCARD_TIME is null "
						+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}''";
				if("0".equals(courseInfo.getClassGroup())){
					sql = MessageFormat.format(sql,courseId , classId);
				}else{
					sql+="  and X.CLASSGROUP=''{2}''";
					sql = MessageFormat.format(sql,courseId , classId,courseInfo.getClassGroup());
				}				

			} else if ("3".equals(type)) {
				sql ="SELECT x.CLASS_ID AS classId,xm,x.CLASS_TRAINEE_ID AS traineeId,"
						+ "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId,"
						+ "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo, "
						+ "isnull((SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=x.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),0) AS useState,"
						+ "CONVERT(VARCHAR(36),isNULL(b.IS_LEAVE,''0'')) AS isLeave,INCARD_TIME as incardTime "
						+ "FROM dbo.TRAIN_T_CLASSTRAINEE X"
						+ "	JOIN dbo.TRAIN_T_COURSEATTEND b "
						+ " on x.CLASS_TRAINEE_ID=b.TRAINEE_ID  AND X.CLASS_ID = b.CLASS_ID  AND b.CLASS_SCHEDULE_ID=''{0}''"
						+ " WHERE (x.ISDELETE=0 or x.ISDELETE=2) and  X.CLASS_ID=''{1}'' and b.IS_LEAVE='1'";
				if("0".equals(courseInfo.getClassGroup())){
					sql = MessageFormat.format(sql,courseId , classId);
				}else{
					sql+="  and X.CLASSGROUP=''{2}''";
					sql = MessageFormat.format(sql,courseId , classId,courseInfo.getClassGroup());
				}		
			} else {
				info.setSuccess(false);
				info.setMessage("调用失败，参数不正确！");
				return info;
			}

			
			traineeList=classService.getQuerySqlObject(sql, VoTrainClasstrainee.class);
			

			info.setSuccess(true);
			info.setMessage("请求成功！");
			info.setObj(traineeList);

			return info;
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			return info;
		}
	}
	
	
	/**
	 * 处理课程考勤签到 
	 * classId：班级ID
	 * courseUuid：班级课程id 
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
	public @ResponseBody ResponseApp<Object> doMobileAttend(
			@RequestParam("classId") String classId,
			@RequestParam("courseUuid") String courseId,
			@RequestParam("factnumb") String factnumb)
			throws IOException, ParseException {

		ResponseApp<Object> info = new ResponseApp<>(); // 返回此数据

		try {
			Date currentDate = new Date();
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();

			logger.info("提交移动端课程考勤签到--参数：班级课程ID->" + courseId + ";人员物理卡号->" + factnumb);

			// 获取班级
			TrainClass classInfo = classService.get(classId);	
			if(classInfo==null){
				info.setSuccess(false);
				info.setMessage("不存在此班级！");			
				return info;	//没有班级
			}
			
			//获取考勤规则; 
			TrainCheckrule checkRule=ruleService.get(classInfo.getCheckruleId());
			
			// 1.通过物理卡号去获取具体的考勤人员，判断此人是否存在这个班级课程的成员中
			TrainClasstrainee tc=classtraineeService.getByProerties(new String[] { "classId", "factoryfixId","useState" }, 
					new Object[] { classId, Long.valueOf(factnumb),1 });
			if (tc == null) {
				info.setSuccess(false);
				info.setMessage("此人员不在此班级中！");
				return info;
			}
			
			TrainClassschedule courseInfo = courseService.get(courseId);	
			if(courseInfo==null){
				info.setSuccess(false);
				info.setMessage("不存在此班级课程！");			
				return info;	//没有班级课程
			}
			
			//分班原则：若班级课程是分班的，则学员必须是对应分班的才能上课
			//		     若班级课程是不分班的，则所有学员可以来上课
			//如果班级课程的分班不是（不分班），并且与学员分班不符合，则不能考勤
			if(!"0".equals(courseInfo.getClassGroup())){
				if(!tc.getClassGroup().equals(courseInfo.getClassGroup())){
					info.setSuccess(false);
					info.setMessage(tc.getXm()+"，您不在此课程上课！");			
					return info;	//没有班级课程
				}			
			}
			
			//获取考勤信息
			String[] param = { "classId", "classScheduleId", "traineeId" };			
			Object[] values = { classId, courseId, tc.getUuid() };		
			TrainCourseattend attend = attendService.getByProerties(param, values);	

			
			// 2.判断是否已签到
			if (attend!=null&&attend.getIncardTime()!=null) {
				info.setSuccess(false);
				info.setMessage(tc.getXm() + "，您已签到，不必重复刷卡！");
				return info;
			}
			
				
			// 3.判断签到结果
			if (checkRule == null) {	//A.若没有设定规则，则直接以课程的开始、结束时间作为约束	
				calendar1.setTime(currentDate);
				calendar2.setTime(courseInfo.getBeginTime());
				calendar2.add(Calendar.MINUTE, -30); // 提前30分钟签到
				if (calendar1.compareTo(calendar2) == -1) {
					info.setSuccess(false);
					info.setMessage("未到签到时间，请等待！");
					return info;
				}

				calendar2.setTime(courseInfo.getEndTime());
				if (calendar1.compareTo(calendar2) == 1) {
					info.setSuccess(false);
					info.setMessage("此课程已结束，不可签到！");
					return info;
				}
				
				if(attend==null)
					attend=new TrainCourseattend();
				attend.setClassId(classId);
				attend.setClassScheduleId(courseId);
				attend.setTraineeId(tc.getUuid());
				attend.setBeginTime(courseInfo.getBeginTime());
				attend.setEndTime(courseInfo.getEndTime());
				
				attend.setIncardTime(currentDate);	// 设置签到时间
				attend.setAttendResult("1");	 // 设为已签到状态
				
				attend.setUpdateTime(currentDate);
				attendService.doMerge(attend); // 保存入库

			} else {
				//判断考勤规则，是否为节次刷卡
				if(checkRule.getCheckMode()==1){	//B.若为按节次考勤，则一节节课程进行签到
					calendar1.setTime(currentDate);
					calendar2.setTime(courseInfo.getEndTime());
					if (calendar1.compareTo(calendar2) == 1) {
						info.setSuccess(false);
						info.setMessage("此课程已结束，不可签到！");
						return info;
					}
					
					calendar1.add(Calendar.MINUTE, checkRule.getInBefore()); // 提前签到
					calendar2.setTime(courseInfo.getBeginTime());
					if (calendar1.compareTo(calendar2) == -1) {
						info.setSuccess(false);
						info.setMessage("未到签到时间，请等待！");
						return info;
					}
					
					if(attend==null)
						attend=new TrainCourseattend();
					
					// 判断结果：1-正常 2-迟到 3-早退 4-缺勤 5-迟到早退
					calendar1.setTime(currentDate);
					calendar2.add(Calendar.MINUTE, checkRule.getAbsenteeism()); // 缺勤时间=签到时间+缺勤分钟
					if (calendar1.compareTo(calendar2) == 1) {
						attend.setAttendResult("4"); // 缺勤

					} else {
						calendar2.setTime(courseInfo.getBeginTime());
						calendar2.add(Calendar.MINUTE, checkRule.getBeLate()); // 迟到时间=签到时间+迟到分钟
						if (calendar1.compareTo(calendar2) == 1)
							attend.setAttendResult("2"); // 缺勤
						else
							attend.setAttendResult("1"); // 正常
					}

					attend.setClassId(classId);
					attend.setClassScheduleId(courseId);
					attend.setTraineeId(tc.getUuid());
					attend.setBeginTime(courseInfo.getBeginTime());
					attend.setEndTime(courseInfo.getEndTime());
					
					attend.setIncardTime(currentDate);	// 设置签到时间
					
					attend.setUpdateTime(currentDate);
					attendService.doMerge(attend); // 保存入库		
					
				}else{	//C.若为按半天考勤，则一节节课程进行签到
					//获取这个半天的所有符合的课程
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String s = dateFormat.format(currentDate);
					Date date2 = sdf.parse(s + " 12:30:00");
					Date date3 = sdf.parse(s + " 18:30:00");
					Date date4 = sdf.parse(s + " 23:59:59");
					
					List<TrainClassschedule> courseList=null;
					
					if (currentDate.getTime() < date2.getTime()) {
						
						courseList = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
								+ " and (o.roomId is null or o.roomId not in("
								+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
								+ " and o.beginTime Between '" + s + " 06:00:00" + "' And '"
								+ sdf.format(date2) + "' "
								+ " and o.classId='"+classId+"'   order by o.beginTime asc");
						
					} else if (currentDate.getTime() < date3.getTime()) {
						courseList = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
								+ " and (o.roomId is null or o.roomId not in("
								+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
								+ " and o.beginTime Between '" + s + " 12:30:00" + "' And '"
								+ sdf.format(date3) + "' "
								+ " and o.classId='"+classId+"'   order by o.beginTime asc");
								
					} else {
						courseList = courseService.getForValues("from TrainClassschedule o where (o.isDelete=0 or o.isDelete=2) "
								+ " and (o.roomId is null or o.roomId not in("
								+ " 	select roomId from OaInfoterm where roomId is not null" + "))"
								+ " and o.beginTime Between '" + s + " 18:30:00" + "' And '"
								+ sdf.format(date4) + "' "
								+ " and o.classId='"+classId+"'   order by o.beginTime asc");
					}
					
					//筛选符合这个学员的课程数据（分班）
					Iterator<TrainClassschedule> coursesIterator=courseList.stream().filter((e)->{
						return ("0").equals(e.getClassGroup())||e.getClassGroup().equals(tc.getClassGroup());				
					}).iterator();
					int i=0;
					String attendResult="0";
					while(coursesIterator.hasNext()){
						courseInfo=coursesIterator.next();
						
						if(i==0){	//只在初次判断
							calendar1.setTime(currentDate);
							calendar2.setTime(courseInfo.getEndTime());
							if (calendar1.compareTo(calendar2) == 1) {
								info.setSuccess(false);
								info.setMessage("此会议已结束，不可签到！");
								return info;
							}
							
							calendar1.add(Calendar.MINUTE, checkRule.getInBefore()); // 提前签到
							calendar2.setTime(courseInfo.getBeginTime());
							if (calendar1.compareTo(calendar2) == -1) {
								info.setSuccess(false);
								info.setMessage("未到签到时间，请等待！");
								return info;
							}
							
							// 判断结果：1-正常 2-迟到 3-早退 4-缺勤 5-迟到早退
							calendar1.setTime(currentDate);
							calendar2.add(Calendar.MINUTE, checkRule.getAbsenteeism()); // 缺勤时间=签到时间+缺勤分钟
							if (calendar1.compareTo(calendar2) == 1) {
								attendResult="4"; // 缺勤

							} else {
								calendar2.setTime(courseInfo.getBeginTime());
								calendar2.add(Calendar.MINUTE, checkRule.getBeLate()); // 迟到时间=签到时间+迟到分钟
								if (calendar1.compareTo(calendar2) == 1)
									attendResult="2"; // 迟到
								else
									attendResult="1"; // 正常
							}
						}
						i++;
						
						//获取此课程的考勤结果
						Object[] attendValues = { classId, courseInfo.getUuid(), tc.getUuid() };		
						attend = attendService.getByProerties(param, attendValues);	
						
						if(attend==null)
							attend=new TrainCourseattend();
						
						attend.setClassId(classId);
						attend.setClassScheduleId(courseInfo.getUuid());
						attend.setTraineeId(tc.getUuid());
						attend.setBeginTime(courseInfo.getBeginTime());
						attend.setEndTime(courseInfo.getEndTime());
						attend.setAttendResult(attendResult);
						attend.setIncardTime(currentDate);	// 设置签到时间
						
						attend.setUpdateTime(currentDate);
						attendService.doMerge(attend); // 保存入库	
					}																		
				}
			}

			info.setSuccess(true);
			info.setMessage(tc.getXm() + "，签到成功！");

			// 获取已签到成功人数		
			String hql = "select count(*) from TrainCourseattend where classId=? and classScheduleId=? and isDelete=0 and incardTime is not null";
			Long num = attendService.getForValue(hql, classId, courseId);	
			info.setObj(num);
			
			logger.info("提交课程签到信息成功!");
			
			return info;

		} catch (Exception e){
			info.setSuccess(false);
			info.setMessage("请求失败-"+ e.getMessage());
			logger.error("提交课程签到信息失败-->message:" + Arrays.toString(e.getStackTrace()));
			return info;				
		}
	}
}