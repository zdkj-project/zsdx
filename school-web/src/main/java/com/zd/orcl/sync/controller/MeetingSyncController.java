package com.zd.orcl.sync.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.DBContextHolder;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingcheckruleService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.plartform.baseset.service.BaseDicitemService;

@Controller
@RequestMapping("/meetingsync")
public class MeetingSyncController extends FrameWorkController<DocSendcheck> implements Constant {
	// orcl的session工厂
	// @Resource
	// private SessionFactory sssssss;

	private static Logger logger = Logger.getLogger(MeetingSyncController.class);

	@Resource
	private OaMeetingService meetingService;

	@Resource
	private OaMeetingempService empService;

	@Resource
	private BuildRoominfoService buildService;

	@Resource
	private BaseDicitemService dicitemService;

	@Resource
	private OaMeetingcheckruleService oaMeetingcheckruleService;
	/*
	 * 废弃
	 * 
	 * @RequestMapping(value = "meeting") public void meeting(HttpServletRequest
	 * request, HttpServletResponse response) throws IOException, ParseException
	 * { request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
	 * request.getSession().removeAttribute("syncMeetingInfoState");
	 * 
	 * try{ //启用orcl数据库
	 * CustomerContextHolder.setCustomerType(CustomerContextHolder.
	 * SESSION_FACTORY_ORACLE);
	 * 
	 * //3.打开session Session session = sssssss.openSession(); //4.开启事务
	 * session.beginTransaction();
	 * 
	 * Query query = session.createSQLQuery(
	 * "select MEETING_ID,cast(MEETING_TITLE as VARCHAR2(255)),cast(CONTENT as VARCHAR2(2048)),"
	 * +
	 * "cast(MEETING_CATEGORY as VARCHAR2(255)),BEGIN_TIME,END_TIME,cast(ROOM_NAME as VARCHAR2(255)),cast(CREATE_BY as VARCHAR2(255)) from zsdx_sync.meeting_msg ORDER BY  BEGIN_TIME ASC"
	 * ); //session.createSQLQuery(
	 * "update dept set dname='SALES1' where deptno=30").executeUpdate();
	 * List<Object[]> list = query.list();
	 * 
	 * //6.提交事务 session.getTransaction().commit(); //7.关闭session
	 * session.flush();
	 * 
	 * CustomerContextHolder.setCustomerType(CustomerContextHolder.
	 * SESSION_FACTORY_MYSQL); OaMeeting m = null; //获取房间实体，拿到roomid //会议类型数据字典
	 * String mapKey = null; String[] propValue = {"MEETINGCATEGORY"};
	 * Map<String, String> mapDicItem = new HashMap<>(); List<BaseDicitem>
	 * listDicItem = dicitemService.queryByProerties("dicCode", propValue); for
	 * (BaseDicitem baseDicitem : listDicItem) { mapKey =
	 * baseDicitem.getItemName() + baseDicitem.getDicCode();
	 * mapDicItem.put(mapKey, baseDicitem.getItemCode()); } BuildRoominfo build
	 * = null; Map<String, String> mapRoomInfo = new HashMap<>();
	 * List<BuildRoominfo> roominfoList = buildService.doQueryAll(); for
	 * (BuildRoominfo buildRoominfo : roominfoList) {
	 * mapRoomInfo.put(buildRoominfo.getRoomName(), buildRoominfo.getUuid()); }
	 * //默认的会议考勤规则 String[] rulePropertyName = {"isDelete","startUsing"};
	 * Object[] rulpropValue = {0,(short)1}; List<OaMeetingcheckrule> ruleList =
	 * oaMeetingcheckruleService.queryByProerties(rulePropertyName,
	 * rulpropValue); OaMeetingcheckrule checkRule = ruleList.get(0); for
	 * (Object[] o : list) { m = meetingService.get(o[0].toString()); if (m ==
	 * null) { m = new OaMeeting(o[0].toString());
	 * //考勤规则，现在放在第一次同步时来设定，因为后面可能会修改规则，所以之后不再同步 m.setNeedChecking((short) 1);
	 * m.setCheckruleId(checkRule.getUuid());
	 * m.setCheckruleName(checkRule.getRuleName()); }
	 * m.setMeetingTitle(o[1].toString()); m.setMeetingName(o[1].toString());
	 * m.setMeetingContent(o[2].toString()); //会议类型数据字典转换 if (o[3] != null){
	 * String categoryItem=mapDicItem.get(o[3].toString() + "MEETINGCATEGORY");
	 * if(categoryItem==null) categoryItem="7"; //若没有找到类型，则指定为其他
	 * m.setMeetingCategory(categoryItem); }else{
	 * m.setMeetingCategory(mapDicItem.get("其他MEETINGCATEGORY")); //若没有类型，则指定为其他
	 * } SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
	 * );//小写的mm表示的是分钟 m.setBeginTime(sdf.parse(o[4].toString()));
	 * m.setEndTime(sdf.parse(o[5].toString())); //m.setRoomId(o[7].toString());
	 * m.setRoomName(o[6].toString()); if (mapRoomInfo.get(o[6]) != null)
	 * m.setRoomId(mapRoomInfo.get(o[6]));
	 * 
	 * //创建人 m.setCreateUser(o[7].toString());
	 * 
	 * meetingService.merge(m); } employee(); }catch(Exception e){
	 * request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
	 * request.getSession().setAttribute("syncMeetingInfoState", "0"); }
	 * 
	 * request.getSession().setAttribute("syncMeetingInfoIsEnd", "1");
	 * 
	 * 
	 * //writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'")); }
	 */
	/*
	 * 废弃 public void employee() throws ParseException { //启用orcl数据库
	 * CustomerContextHolder.setCustomerType(CustomerContextHolder.
	 * SESSION_FACTORY_ORACLE);
	 * 
	 * //3.打开session Session session = sssssss.openSession(); //4.开启事务
	 * session.beginTransaction();
	 * 
	 * Query query = session.createSQLQuery(
	 * "select MEETING_ID,EMPLOYEE_ID,cast(XM as VARCHAR2(255)) from zsdx_sync.meeting_user"
	 * ); //session.createSQLQuery(
	 * "update dept set dname='SALES1' where deptno=30").executeUpdate();
	 * List<Object[]> list = query.list();
	 * 
	 * //6.提交事务 session.getTransaction().commit(); //7.关闭session
	 * session.flush();
	 * 
	 * CustomerContextHolder.setCustomerType(CustomerContextHolder.
	 * SESSION_FACTORY_MYSQL); OaMeetingemp m = null; for (Object[] o : list) {
	 * m = empService.get(o[0].toString() + o[1].toString()); if (m == null) { m
	 * = new OaMeetingemp(o[0].toString() + o[1].toString());
	 * m.setAttendResult("0"); //默认为0，未考勤 } m.setMeetingId(o[0].toString());
	 * m.setEmployeeId(o[1].toString());
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
	 * );//小写的mm表示的是分钟 Query query1 = session.createSQLQuery(
	 * "select BEGIN_TIME,END_TIME from zsdx_sync.meeting_msg where MEETING_ID='"
	 * + o[0].toString() + "'"); List<Object[]> list1 = query1.list(); if
	 * (list1.size() > 0) { for (Object[] o1 : list1) {
	 * m.setBeginTime(sdf.parse(o1[0].toString()));
	 * m.setEndTime(sdf.parse(o1[1].toString())); } } else {
	 * m.setBeginTime(sdf.parse("1970-01-01 00:00:00")); m.setEndTime(sdf.parse(
	 * "1970-01-01 00:00:00")); }
	 * 
	 * m.setXm(o[2].toString()); empService.merge(m);
	 * 
	 * 
	 * }
	 * 
	 * String sql =
	 * "UPDATE dbo.OA_T_MEETING SET METTING_EMPID=(SELECT dbo.OA_F_GETMEETINGEMPID(MEETING_ID)),\n"
	 * + "\tMEETING_EMPNMAE=(SELECT dbo.OA_F_GETMEETINGEMPNAME(MEETING_ID))";
	 * empService.executeSql(sql); //writeJSON(response,
	 * jsonBuilder.returnSuccessJson("'同步成功'")); }
	 */

	/**
	 * ZZK修改
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "syncMeeting")
	public void syncMeeting(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
		request.getSession().removeAttribute("syncMeetingInfoState");

		try {
			logger.info("准备同步OA会议！");
			//切换oracle数据库
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_OA);
			
			// 从这个表直接获取人员数据，并进行拆分入库
			// 目前MEETING_USER_IDS 的字符长度为4000，可能会存在隐患，今后OA那边可能会处理，改为clob类型
			List<Object[]> meetingList = empService.ObjectQuerySql(
					"select MEETING_ID,cast(MEETING_TITLE as VARCHAR2(255)),cast(CONTENT as VARCHAR2(2048)),"
							+ " cast(MEETING_CATEGORY as VARCHAR2(255)),BEGIN_TIME,END_TIME,"
							+ " cast(ROOM_NAME as VARCHAR2(255)),cast(CREATE_BY as VARCHAR2(255))"
							+ " from zsdx_sync.meeting_msg where BEGIN_TIME >=(sysdate-7) ORDER BY  BEGIN_TIME ASC");
	

			// 从这个表取人员数据。
			List<Object[]> empList = empService.ObjectQuerySql(
					"select a.MEETING_ID,a.EMPLOYEE_ID,cast(a.XM as VARCHAR2(255)),b.BEGIN_TIME,b.END_TIME "
							+ " from zsdx_sync.meeting_user a join zsdx_sync.meeting_msg b "
							+ " on a.MEETING_ID =b.MEETING_ID" + " where b.BEGIN_TIME >=(sysdate-7) ");
			
			
			logger.info("查询到OA会议数据！");
			
			DBContextHolder.clearDBType();
			
			logger.info("开始同步OA会议数据！");
			
			Integer state = meetingService.syncMetting(meetingList,empList);
			
			logger.info("同步OA会议数据完毕！");
			if (state == 1)
				request.getSession().setAttribute("syncMeetingInfoIsEnd", "1");
			else {
				request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
				request.getSession().setAttribute("syncMeetingInfoState", "0");
			}
		} catch (Exception e) {

            logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
			request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
			request.getSession().setAttribute("syncMeetingInfoState", "0");
		
		} finally {
			DBContextHolder.clearDBType();
		}

		// writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
	}

	@RequestMapping("/checkSyncEnd")
	public void checkSyncEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("syncMeetingInfoIsEnd");
		Object state = request.getSession().getAttribute("syncMeetingInfoState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"会议同步完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"会议同步未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"会议同步未完成！\""));
		}
	}

}
