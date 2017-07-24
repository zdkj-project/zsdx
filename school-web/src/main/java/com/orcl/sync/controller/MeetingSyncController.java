package com.orcl.sync.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.CustomerContextHolder;
import com.zd.core.util.ModelUtil;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
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
import java.util.List;

@Controller
@RequestMapping("/meetingsync")
public class MeetingSyncController extends FrameWorkController<DocSendcheck> implements Constant{
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
    
	@RequestMapping(value = "meeting")
	public void meeting(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		//启用orcl数据库
		CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
		
		//3.打开session  
	    Session session = sssssss.openSession();  
	    //4.开启事务  
	    session.beginTransaction();
	
	    Query query = session.createSQLQuery("select MEETING_ID,cast(MEETING_TITLE as VARCHAR2(255)),cast(CONTENT as VARCHAR2(2048))," +
                "cast(MEETING_CATEGORY as VARCHAR2(255)),BEGIN_TIME,END_TIME,cast(ROOM_NAME as VARCHAR2(255)) from zsdx_sync.meeting_msg");
	    //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
	    List<Object[]> list = query.list();
	    
	    //6.提交事务  
	    session.getTransaction().commit();  
	    //7.关闭session  
	    session.flush(); 
	    
	    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
	    OaMeeting m=null;
	    //获取房间实体，拿到roomid
	    BuildRoominfo build=null;
	    for(Object[] o:list){
    		m=meetingService.get(o[0].toString());
    		if (m==null) {
				m=new OaMeeting(o[0].toString());
			}
    		m.setMeetingTitle(o[1].toString());
    		m.setMeetingName(o[1].toString());
    		m.setMeetingContent(o[2].toString());
    		m.setMeetingCategory(o[3].toString());
    		
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟    
    		m.setBeginTime(sdf.parse(o[4].toString()));
    		m.setEndTime(sdf.parse(o[5].toString()));
    		//m.setRoomId(o[7].toString());
    		m.setRoomName(o[6].toString());
    		
    		build=buildService.getByProerties("roomName", o[6].toString());
    		if(ModelUtil.isNotNull(build)){
    			m.setRoomId(build.getUuid());
    		}
    		
    		m.setNeedChecking((short) 1);
    		meetingService.merge(m);
	    }
	    employee();

	    
	    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
	}
	
	public void employee() throws ParseException{
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
	    OaMeetingemp m=null;
	    for(Object[] o:list){
    		m=empService.get(o[0].toString()+o[1].toString());
    		if (m==null) {
				m=new OaMeetingemp(o[0].toString()+o[1].toString());
			}
    		m.setMeetingId(o[0].toString());
    		m.setEmployeeId(o[1].toString());
    		m.setAttendResult("1");
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟    
    		Query query1 = session.createSQLQuery("select BEGIN_TIME,END_TIME from zsdx_sync.meeting_msg where MEETING_ID='"+o[0].toString()+"'");
    		List<Object[]> list1 = query1.list();
    		if(list1.size()>0){
        		for(Object[] o1:list1){
            		m.setBeginTime(sdf.parse(o1[0].toString()));
            		m.setEndTime(sdf.parse(o1[1].toString()));
        		}
    		}else{
        		m.setBeginTime(sdf.parse("1970-01-01 00:00:00"));
        		m.setEndTime(sdf.parse("1970-01-01 00:00:00"));
    		}

    		m.setXm(o[2].toString());
    		empService.merge(m);


	    }

	    
	    //writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
	}
	
	
	

}
