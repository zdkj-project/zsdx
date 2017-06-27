package com.zd.school.app;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.control.device.model.Ptcard;
import com.zd.school.jw.model.app.MeetCheckApp;
import com.zd.school.jw.model.app.MeetingApp;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.oa.meeting.model.MeetingCheck;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingcheckrule;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingcheckruleService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;

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

	
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetingApp meetinglist(String termCode,String time, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		MeetingApp info=new MeetingApp();
		OaInfoterm roomTerm = termService.getByProerties("termCode", termCode);

		List<OaMeeting> meeting = null;
		List<Object[]> obj=null;
		if (ModelUtil.isNotNull(roomTerm)) {
			meeting = meetingService.doQuery("from OaMeeting where roomId='"+roomTerm.getRoomId()+"' and Convert(varchar,beginTime,120) like'"+time+"%' order by beginTime asc");
			for(OaMeeting o:meeting){
				o.setBegin(dateFormat.format(o.getBeginTime()));
				o.setEnd(dateFormat.format(o.getEndTime()));
				List<OaMeetingemp> emplist=empService.queryByProerties("meetingId", o.getUuid());
				for(OaMeetingemp e:emplist){
					obj=empService.ObjectQuerySql("select cardno,FACTORYFIXID from dbo.PT_CARD where USER_ID='"+e.getEmployeeId()+"'");
					if(obj!=null){
						for(Object[] ob:obj){
							e.setCardNo(ob[0].toString());
							e.setFactoryfixId(ob[1].toString());
						}
					}
				}
				o.setMeetingemp(emplist);
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
		
	}
	
	@RequestMapping(value = { "/rule" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody OaMeetingcheckrule rule(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
			OaMeetingcheckrule rule=ruleService.getByProerties("startUsing", (short)1);

			return rule;
		
	}
	
	
	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody MeetCheckApp update(String meetcheck, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
				List<MeetingCheck> check=null;
				if(null!=meetcheck){
					check = (List<MeetingCheck>) JsonBuilder.getInstance().fromJsonArray(meetcheck,
							MeetingCheck.class);
				}
				MeetCheckApp mca=new MeetCheckApp();
				try {
					for(MeetingCheck mc:check){
						OaMeeting meeting=meetingService.get(mc.getMeetingId());
						//考勤人员id
						String uid="";
						List<Object[]> obj=empService.ObjectQuerySql("select USER_ID,CARDNO from dbo.PT_CARD where FACTORYFIXID='"+mc.getWlkh()+"'");
						uid=obj.get(0)[0].toString();
			    		String[] param={"meetingId","employeeId"};
		                Object[] values={mc.getMeetingId(),uid};		
						OaMeetingemp emp=empService.getByProerties(param, values);
						if(emp!=null){
							emp.setMeetingId(meeting.getUuid());
							if(mc.getLg().equals("0")){
								emp.setIncardTime(mc.getTime());
								emp.setInResult(mc.getInResult());
							}else{
								emp.setOutcardTime(mc.getTime());
								emp.setOutResult(mc.getOutResult());
							}
							emp.setAttendResult(mc.getAttendResult());
							empService.merge(emp); 
						}else{
							mca.setCode(false);
							mca.setMessage("会议或者人员ID错误，找不到对应参会人员信息。");
							return mca;
						}
					}
					mca.setCode(true);
					mca.setMessage("存储数据成功");
					return mca;
				} catch (Exception e) {
					mca.setCode(false);
					mca.setMessage("参数错误"+e.getMessage());
					return mca;
				}
		
	}

}
