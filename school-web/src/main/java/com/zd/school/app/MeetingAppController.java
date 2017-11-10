package com.zd.school.app;

import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.model.app.MeetCheckApp;
import com.zd.school.jw.model.app.MeetingApp;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    MeetingApp meetinglist(String termCode, String time, HttpServletRequest request,
                           HttpServletResponse response) throws IOException, ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        MeetingApp info = new MeetingApp();	//返回此数据
        
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
	        		//meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId='" + roomTerm.getRoomId() + "' and Convert(varchar,beginTime,120) like'" + time + "%' order by beginTime asc");
	        		//注：我们正式使用时需要将可能作为会议室的房间名称发给OA，所以要将房间查询方式换为id
	        		meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 06:00:00" + "' And '"
							+ sdf.format(date2) + "' order by beginTime asc");
				} else if (date.getTime() < date3.getTime()) {
					meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 12:00:00" + "' And '"
							+ sdf.format(date3) + "' order by beginTime asc");
				} else {
					meeting = meetingService.doQuery("from OaMeeting where isDelete=0 and roomId like '%"
							+ roomTerm.getRoomId() + "%'  and beginTime Between '" + s + " 18:00:00" +  "' And '"
							+ sdf.format(date4) + "' order by beginTime asc");
				}
	        	
        		 for (OaMeeting o : meeting) {
 	                o.setBegin(timeFormat.format(o.getBeginTime()));
 	                o.setEnd(timeFormat.format(o.getEndTime()));
 	
 	                List<OaMeetingemp> emplist = empService.queryByProerties("meetingId", o.getUuid());
 	                o.setMeetingemp(emplist);
 	
 	                checkrule = ruleService.get(o.getCheckruleId());
 	                o.setOaMeetingcheckrule(checkrule);
 	
 	                String sql = MessageFormat.format("SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE  ATTACH_TYPE=''jpg'' AND ENTITY_NAME=''OaMeeting'' AND RECORD_ID=''{0}''", o.getUuid());
 	                List<CommAttachment> attachmentList = meetingService.doQuerySqlObject(sql, CommAttachment.class);
 	                if(attachmentList.size()==0){
 	                	CommAttachment tempAtt=new CommAttachment();
 	                	tempAtt.setAttachName("image");
 	                	tempAtt.setAttachUrl("/static/core/resources/images/defaultMettingImg.png");
 	                	attachmentList.add(tempAtt);
 	                }
 	                o.setAttachment(attachmentList);
 	            }
	        	
	        }
	         else {
	            info.setCode(false);
	            info.setMessage("没有找到该终端设备！");
	            return info;
	        }
	
	        info.setCode(true);
	        info.setMessage("请求成功！");
	        info.setMeeting(meeting);
	        return info;
        }catch (Exception e) {
        	info.setCode(false);
        	info.setMessage("数据异常调用失败" + e.getMessage());
			return info;
		}

    }

    @RequestMapping(value = {"/rule"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    OaMeetingcheckrule rule(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ParseException {
        OaMeetingcheckrule rule = ruleService.getByProerties("startUsing", (short) 1);

        return rule;
    }


    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    MeetCheckApp update(String meetcheck, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ParseException {
        List<MeetingCheck> check = null;
        Date currentDate=new Date();
        if (null != meetcheck) {
            check = (List<MeetingCheck>) JsonBuilder.getInstance().fromJsonArray(meetcheck,
                    MeetingCheck.class);
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        MeetCheckApp mca = new MeetCheckApp();
        try {
            for (MeetingCheck mc : check) {
                OaMeeting meeting = meetingService.get(mc.getMeetingId());
                //考勤人员id
                //String uid = empService.getForValueToSql("select USER_ID from dbo.CARD_T_USEINFO where FACT_NUMB='" + mc.getWlkh() + "'");
            
                //String[] param = {"meetingId", "employeeId"};
                //Object[] values = {mc.getMeetingId(),mc.getUserId()};
                OaMeetingemp emp = empService.getByProerties("uuid", mc.getUserId());
                if (emp != null) {
                    emp.setMeetingId(meeting.getUuid());
                    if (mc.getLg().equals("0")) {
                    	if(emp.getIncardTime()==null){
                    		emp.setIncardTime(mc.getTime());
                            emp.setInResult(mc.getInResult());
                            emp.setAttendResult(mc.getAttendResult());
                    	}else if(mc.getTime()!=null){
                    		//解决多个平板上传数据的问题
                    		calendar1.setTime(emp.getIncardTime());
                        	calendar2.setTime(mc.getTime());
                        	if(calendar1.compareTo(calendar2)==1){	//若第二次上传的时间【小于】第一次的时间，那就替换
                        		emp.setIncardTime(mc.getTime());
                                emp.setInResult(mc.getInResult());
                                emp.setAttendResult(mc.getAttendResult());
                        	}
                    	}
                    } else if(mc.getLg().equals("1")) {
                    	if(emp.getOutcardTime()==null){
                    		emp.setOutcardTime(mc.getTime());
                            emp.setOutResult(mc.getOutResult());
                            emp.setAttendResult(mc.getAttendResult());
                    	}else if(mc.getTime()!=null){
                    		//解决多个平板上传数据的问题
                    		calendar1.setTime(emp.getOutcardTime());
                        	calendar2.setTime(mc.getTime());
                        	if(calendar1.compareTo(calendar2)==-1){	//若第二次上传的时间【大于】第一次的时间，那就替换
                        		emp.setOutcardTime(mc.getTime());
                                emp.setOutResult(mc.getOutResult());
                                emp.setAttendResult(mc.getAttendResult());
                        	}
                    	}
                    }else{
                    	//当为缺勤的时候，并且判断之前没有考勤数据，就设置为缺勤
                    	if(emp.getIncardTime()==null && emp.getOutcardTime()==null){
                    		 emp.setAttendResult(mc.getAttendResult());
                    	}
                    }
                    emp.setUpdateTime(currentDate);
                    empService.merge(emp);
                }
                /*else {
                    mca.setCode(false);
                    mca.setMessage("会议或者人员ID错误，找不到对应参会人员信息。");
                    return mca;
                }*/
            }
            mca.setCode(true);
            mca.setMessage("存储数据成功");
            return mca;
        } catch (Exception e) {
            mca.setCode(false);
            mca.setMessage("参数错误" + e.getMessage());
            return mca;
        }
    }

}
