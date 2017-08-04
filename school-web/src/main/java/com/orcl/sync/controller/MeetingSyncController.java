package com.orcl.sync.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.CustomerContextHolder;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/meetingsync")
public class MeetingSyncController extends FrameWorkController<DocSendcheck> implements Constant {
    @Resource
    //orcl的session工厂
    private SessionFactory sssssss;
    @Resource
    private BaseOrgService orgService;
    @Resource
    private OaMeetingService meetingService;
    @Resource
    private OaMeetingempService empService;
    @Resource
    private BuildRoominfoService buildService;
    @Resource
    private BaseDicitemService dicitemService;

    @RequestMapping(value = "meeting")
    public void meeting(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        request.getSession().setAttribute("syncMeetingInfoIsEnd", "0");
        request.getSession().removeAttribute("syncMeetingInfoState");
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createSQLQuery("select MEETING_ID,cast(MEETING_TITLE as VARCHAR2(255)),cast(CONTENT as VARCHAR2(2048))," +
                "cast(MEETING_CATEGORY as VARCHAR2(255)),BEGIN_TIME,END_TIME,cast(ROOM_NAME as VARCHAR2(255)) from zsdx_sync.meeting_msg ORDER BY  BEGIN_TIME ASC");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<Object[]> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        session.flush();

        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        OaMeeting m = null;
        //获取房间实体，拿到roomid
        //会议类型数据字典
        String mapKey = null;
        String[] propValue = {"MEETINGCATEGORY"};
        Map<String, String> mapDicItem = new HashMap<>();
        List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
        for (BaseDicitem baseDicitem : listDicItem) {
            mapKey = baseDicitem.getItemName() + baseDicitem.getDicCode();
            mapDicItem.put(mapKey, baseDicitem.getItemCode());
        }
        BuildRoominfo build = null;
        Map<String, String> mapRoomInfo = new HashMap<>();
        List<BuildRoominfo> roominfoList = buildService.doQueryAll();
        for (BuildRoominfo buildRoominfo : roominfoList) {
            mapRoomInfo.put(buildRoominfo.getRoomName(), buildRoominfo.getUuid());
        }
        for (Object[] o : list) {
            m = meetingService.get(o[0].toString());
            if (m == null) {
                m = new OaMeeting(o[0].toString());
            }
            m.setMeetingTitle(o[1].toString());
            m.setMeetingName(o[1].toString());
            m.setMeetingContent(o[2].toString());
            //会议类型数据字典转换
            if (o[3] != null)
                m.setMeetingCategory(mapDicItem.get(o[3].toString() + "MEETINGCATEGORY"));
            //m.setMeetingCategory(o[3].toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
            m.setBeginTime(sdf.parse(o[4].toString()));
            m.setEndTime(sdf.parse(o[5].toString()));
            //m.setRoomId(o[7].toString());
            m.setRoomName(o[6].toString());
            if (mapRoomInfo.get(o[6]) != null)
                m.setRoomId(mapRoomInfo.get(o[6]));
/*            build = buildService.getByProerties("roomName", o[6].toString());
            if (ModelUtil.isNotNull(build)) {
                m.setRoomId(build.getUuid());
            }*/

            m.setNeedChecking((short) 1);
            meetingService.merge(m);
        }
        employee();

        request.getSession().setAttribute("syncMeetingInfoIsEnd", "1");


        //writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
    }

    public void employee() throws ParseException {
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createSQLQuery("select MEETING_ID,EMPLOYEE_ID,cast(XM as VARCHAR2(255)) from zsdx_sync.meeting_user");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<Object[]> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        session.flush();

        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        OaMeetingemp m = null;
        for (Object[] o : list) {
            m = empService.get(o[0].toString() + o[1].toString());
            if (m == null) {
                m = new OaMeetingemp(o[0].toString() + o[1].toString());
            }
            m.setMeetingId(o[0].toString());
            m.setEmployeeId(o[1].toString());
            m.setAttendResult("1");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
            Query query1 = session.createSQLQuery("select BEGIN_TIME,END_TIME from zsdx_sync.meeting_msg where MEETING_ID='" + o[0].toString() + "'");
            List<Object[]> list1 = query1.list();
            if (list1.size() > 0) {
                for (Object[] o1 : list1) {
                    m.setBeginTime(sdf.parse(o1[0].toString()));
                    m.setEndTime(sdf.parse(o1[1].toString()));
                }
            } else {
                m.setBeginTime(sdf.parse("1970-01-01 00:00:00"));
                m.setEndTime(sdf.parse("1970-01-01 00:00:00"));
            }

            m.setXm(o[2].toString());
            empService.merge(m);


        }

        String sql = "UPDATE dbo.OA_T_MEETING SET METTING_EMPID=(SELECT dbo.OA_F_GETMEETINGEMPID(MEETING_ID)),\n" +
                "\tMEETING_EMPNMAE=(SELECT dbo.OA_F_GETMEETINGEMPNAME(MEETING_ID))";
        empService.executeSql(sql);
        //writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
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
