package com.zd.school.app;

import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.model.app.CourseCheckApp;
import com.zd.school.jw.model.app.TrainCourseApp;
import com.zd.school.jw.train.model.CourseCheck;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainCourseattend;
import com.zd.school.jw.train.model.TrainCreditsrule;
import com.zd.school.jw.train.model.vo.VoTrainClasstrainee;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.jw.train.service.TrainCourseattendService;
import com.zd.school.jw.train.service.TrainCreditsruleService;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



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
	private TrainCreditsruleService ruleService;
	@Resource
	private TrainCourseattendService attendService;

	
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody TrainCourseApp courselist(String termCode,String time, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		//传过来的时间为yyyy-MM-dd HH：mm：ss
		TrainCourseApp tca=new TrainCourseApp();
		try {
			//判断时间查询上午下午
			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date=sdf.parse(time);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String s=dateFormat.format(date);
			Date date2=sdf.parse(s+" 12:00:00");
			Date date3=sdf.parse(s+" 23:59:59");			
			//设备号查询房间
			OaInfoterm roomTerm = termService.getByProerties("termCode", termCode);
			List<VoTrainClasstrainee> volist=null;
			VoTrainClasstrainee vo=null;
			List<TrainClassschedule> course = null;
			List<Object[]> obj=null;
			if (ModelUtil.isNotNull(roomTerm)) {
				if(date.getTime()<=date2.getTime()){
					course=courseService.doQuery("from TrainClassschedule where scheduleAddress='"+roomTerm.getRoomName()+"' and beginTime Between '"+s+" 06:00:00' And '"+sdf.format(date2)+"' order by beginTime asc");
				}else{
					course=courseService.doQuery("from TrainClassschedule where scheduleAddress='"+roomTerm.getRoomName()+"' and beginTime Between '"+s+" 12:00:00' And '"+sdf.format(date3)+"' order by beginTime asc");
				}
				//course=courseService.doQuery("from TrainClassschedule where scheduleAddress='"+roomTerm.getRoomName()+"' and Convert(varchar,beginTime,120) like'"+time+"%' order by beginTime asc");
				if(course.size()>0){
					for(TrainClassschedule c:course){
						volist=new ArrayList<VoTrainClasstrainee>();
						List<TrainClasstrainee> ctraineelist=classtraineeService.queryByProerties("classId", c.getClassId());
						for(TrainClasstrainee ct:ctraineelist){
							vo=new VoTrainClasstrainee();
							obj=classtraineeService.ObjectQuerySql("select cardno,FACTORYFIXID from dbo.PT_CARD where USER_ID='"+ct.getTraineeId()+"'");
							for(Object[] o:obj){
								vo.setCardNo(o[0].toString());
								vo.setFactoryfixId(o[1].toString());
							}
							vo.setXm(ct.getXm());
							vo.setTraineeId(ct.getTraineeId());
							vo.setClassId(ct.getClassId());
							volist.add(vo);
						}
						c.setList(volist);		
					}
				}else{
					tca.setCode(false);
					tca.setMessage("数据异常调用失败,没有对应时间的课程");
					return tca;
				}
			}
			tca.setCode(true);
			tca.setMessage("调用成功");
			tca.setCourse(course);
			return tca;

		
		} catch (Exception e) {
			tca.setCode(false);
			tca.setMessage("数据异常调用失败"+e.getMessage());
			return tca;
		}
		
	}
	
	@RequestMapping(value = { "/rule" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody TrainCreditsrule rule(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
			TrainCreditsrule rule=ruleService.getByProerties("startUsing", (short)1);

			return rule;
		
	}
	
	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody CourseCheckApp update(String coursecheck, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
			List<CourseCheck> check=null;
			if(null!=coursecheck){
				check = (List<CourseCheck>) JsonBuilder.getInstance().fromJsonArray(coursecheck,
						CourseCheck.class);
			}
			CourseCheckApp cca=new CourseCheckApp();
			
			TrainCourseattend attend=null;
			TrainClassschedule course=null;
			try {
				for(CourseCheck t:check){
					String uid="";
					List<Object[]> obj=termService.ObjectQuerySql("select USER_ID,CARDNO from dbo.PT_CARD where FACTORYFIXID='"+t.getWlkh()+"'");
					uid=obj.get(0)[0].toString();
					
					course=courseService.get(t.getCourseId());
					
		    		String[] param={"classId","classScheduleId","traineeId"};
	                Object[] values={t.getClassId(),t.getCourseId(),uid};
					attend=attendService.getByProerties(param,values);
					if(attend!=null){
						attend.setBeginTime(course.getBeginTime());
						attend.setEndTime(course.getEndTime());
						if(t.getLg().equals("0")){
							attend.setIncardTime(t.getTime());
						}else{
							attend.setOutcardTime(t.getTime());
						}
						attendService.merge(attend);
					}
					else{
						cca.setCode(false);
						cca.setMessage("查询不到对应的学生考勤");
						return cca;
					}
							
				}
				cca.setCode(true);
				cca.setMessage("存储数据成功");
				return cca;
				
			} catch (Exception e) {
				cca.setCode(false);
				cca.setMessage("存储异常"+e.getMessage());
				return cca;
			}

		
	}
	
}