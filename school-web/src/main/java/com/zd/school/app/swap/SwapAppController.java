package com.zd.school.app.swap;

import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.IpControl.IpControl.service.SysIpService;
import com.zd.school.jw.model.app.AttendApp;
import com.zd.school.jw.model.app.SwapApp;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.service.*;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app/swap")
public class SwapAppController {
	@Resource
	private TrainTraineeService trainService;
	@Resource
	private StuBaseinfoService stuService;
	@Resource
	private TrainClassService classService;
	@Resource
	private TrainClasstraineeService classtraineeService;
	@Resource
	private TrainClassscheduleService courseService;
	@Resource
	private TrainCourseinfoService courseinfoService;
	@Resource
	private TrainCourseattendService attendService;
	@Resource
	private SysIpService ipservice;
	
	@RequestMapping(value = { "/traineelist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody SwapApp traineelist(String trainee,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
			SwapApp sa=new SwapApp();
			//IP限制判断，测试过程可以注释掉
			String ipsql="select count(*) from SYS_T_IP where ip_url='"+getIpAddr(request)+"'";
			Integer count=ipservice.getCountSql(ipsql);
			if(count>0){
				if(trainee.equals("")){
					sa.setSuccess(false);
					sa.setMessage("参数错误");
					return sa;
				}
				//JSONObject jsonObj=null;
				List<TrainTrainee> tlist=null;
				try {
					//jsonObj=JSONObject.fromObject(trainee);
					tlist=(List<TrainTrainee>) JsonBuilder.getInstance().fromJsonArray(trainee,
							TrainTrainee.class);
				} catch (Exception e) {
					sa.setSuccess(false);
					sa.setMessage("参数错误");
					return sa;
				}
	
				TrainTrainee t=null;
				for(TrainTrainee tt:tlist){
					t=trainService.getByProerties("extField01", tt.getUuid());
					if (ModelUtil.isNotNull(t)) {
						t.setExtField01(tt.getUuid());
						t.setXm(tt.getXm());
						t.setXbm(tt.getXbm());
						t.setZp(tt.getZp());
						t.setWorkUnit(tt.getWorkUnit());
						t.setMobilePhone(tt.getMobilePhone());
						t.setIsDelete(tt.getIsDelete());
						trainService.merge(t);
					}else{
						t=new TrainTrainee();
						t.setExtField01(tt.getUuid());
						t.setXm(tt.getXm());
						t.setXbm(tt.getXbm());
						t.setZp(tt.getZp());
						t.setSfzjh(tt.getSfzjh());
						t.setWorkUnit(tt.getWorkUnit());
						t.setMobilePhone(tt.getMobilePhone());
						t.setIsDelete(tt.getIsDelete());
						trainService.merge(t);
						
					}
				}
				sa.setSuccess(true);
				sa.setMessage("同步成功");
				return sa;
			}else{
				sa.setSuccess(false);
				sa.setMessage("ip限制访问接口");
				return sa;
			}
		
	}
	@RequestMapping(value = { "/classinfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody SwapApp classinfo(String classes,String classtrain,String classcourse,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
			JSONObject jsonObj=JSONObject.fromObject(classes);
			TrainClass tclass=(TrainClass) JSONObject.toBean(jsonObj, TrainClass.class);
			
			List<TrainClasstrainee> traineelist=null;
			if(null!=classtrain){
				traineelist = (List<TrainClasstrainee>) JsonBuilder.getInstance().fromJsonArray(classtrain,
						TrainClasstrainee.class);
			}
			
			List<TrainClassschedule> courselist=null;
			if(null!=classcourse){
				courselist = (List<TrainClassschedule>) JsonBuilder.getInstance().fromJsonArray(classcourse,
						TrainClassschedule.class);
			}
			SwapApp sa=new SwapApp();
			
			//保存班级信息
			TrainClass tc=null;
			tc=classService.get(tclass.getUuid());
			if (ModelUtil.isNotNull(tc)) {
				tc.setClassName(tclass.getClassName());
				tc.setBeginDate(tclass.getBeginDate());
				tc.setEndDate(tclass.getEndDate());
				tc.setClassCategory("1");
				tc.setClassNumb(tclass.getUuid());
				tc.setNeedChecking((short) 1);
				tc.setNeedSynctrainee((short) 1);	
				classService.merge(tc);
			}else{
				tc=new TrainClass(tclass.getUuid());
				tc.setClassName(tclass.getClassName());
				tc.setBeginDate(tclass.getBeginDate());
				tc.setEndDate(tclass.getEndDate());
				tc.setClassCategory("1");
				tc.setClassNumb(tclass.getUuid());
				tc.setNeedChecking((short) 1);
				tc.setNeedSynctrainee((short) 1);
				classService.merge(tc);
			}
			
			//保存班级学员
			TrainClasstrainee ct=null;
			//查询学员姓名
			TrainTrainee t=null;
			for(TrainClasstrainee trainee:traineelist){
/*				ct=classtraineeService.get(trainee.getClassId()+trainee.getTraineeId());
				if(ct==null){
					ct=new TrainClasstrainee(trainee.getClassId()+trainee.getTraineeId());
				}
				ct.setClassId(trainee.getClassId());
				ct.setTraineeId(trainee.getTraineeId());*/
				//Integer count=classtraineeService.getCountSql("select count(*) from TRAIN_T_CLASSTRAINEE where CLASS_ID='"+trainee.getClassId()+"' and TRAINEE_ID='"+trainee.getTraineeId()+"'");
				t=trainService.getByProerties("extField01", trainee.getTraineeId());
				
				String[] param={"classId","traineeId"};
                Object[] values={trainee.getClassId(),t.getUuid()};
				ct=classtraineeService.getByProerties(param, values);
				if (!ModelUtil.isNotNull(ct)){
					ct=new TrainClasstrainee();				
				}
				ct.setClassId(trainee.getClassId());
				ct.setTraineeId(t.getUuid());
				ct.setMobilePhone("未设置");
				if(ModelUtil.isNotNull(t)){
					ct.setXm(t.getXm());
					ct.setXbm(t.getXbm());
				}
				try {
					classtraineeService.merge(ct);
				} catch (Exception e) {
					sa.setSuccess(false);
					sa.setMessage("异常，班级学员数据错误。"+e.getMessage());
					return sa;
				}
			}
			
			//先删除课程安排表，避免重复。
			if(courselist!=null){
				for(TrainClassschedule course:courselist){
					courseService.executeSql("delete TRAIN_T_CLASSSCHEDULE where CLASS_ID='"+course.getClassId()+"'");
				}
				//保存班级课程
				TrainClassschedule cc=null;
				TrainCourseinfo ci=null;
				for(TrainClassschedule course:courselist){
					cc=new TrainClassschedule();
					cc.setClassId(course.getClassId());
					//传过来的courseid放入扩展字段1
					//根据传过来的coursename查询课程库有没有对应的课程，有则取出来赋值给courseid
					ci=courseinfoService.getByProerties("courseName", course.getCourseName());
					if(ci!=null){
						cc.setCourseId(ci.getUuid());
					}
					cc.setExtField01(course.getCourseId());
					cc.setCourseName(course.getCourseName());
					cc.setBeginTime(course.getBeginTime());
					cc.setEndTime(course.getEndTime());
					cc.setScheduleAddress(course.getScheduleAddress());
					cc.setCourseMode(Integer.valueOf(1));
					try {
						courseService.merge(cc);
					} catch (Exception e) {
						sa.setSuccess(false);
						sa.setMessage("异常，课程安排数据错误"+e.getMessage());
						return sa;
					}
					
				}
			}else{
				sa.setSuccess(false);
				sa.setMessage("异常，课程安排数据为空。");
				return sa;
			}
			sa.setSuccess(true);
			sa.setMessage("同步成功");
			return sa;
		
	}
	
	@RequestMapping(value = { "/attendlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody AttendApp attendlist(String classId,String courseId,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
			AttendApp app=new AttendApp();
			try {
	    		String[] param={"classId","extField01"};
                Object[] values={classId,courseId};
				List<TrainClassschedule> chedulelist=courseService.queryByProerties(param, values);
				List<TrainCourseattend> attendlist=new ArrayList<TrainCourseattend>();
				for(TrainClassschedule cc:chedulelist){
					attendlist.addAll(attendService.doQuery("from TrainCourseattend where classId='"+cc.getClassId()+"' and classScheduleId='"+cc.getUuid()+"' order by beginTime asc"));
				}
				List<SchoolAttend> schoolAttends = new ArrayList<>();
				SchoolAttend satd = null;
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟    
				for(TrainCourseattend tct:attendlist){
					String cId = tct.getClassId();
					String csid = this.courseService.get(tct.getClassScheduleId()).getExtField01();
					String tiid = this.trainService.get(tct.getTraineeId()).getExtField01();
					satd = new SchoolAttend(cId, csid, tiid, tct.getIncardTime(), tct.getOutcardTime());
					schoolAttends.add(satd);
				}
				
				app.setSuccess(true);
				app.setMessage("数据读取成功");
				app.setObj(schoolAttends);
				return app;
				
				} catch (Exception e) {
					app.setSuccess(false);
					app.setMessage("数据参数异常:"+e.getMessage());
					return app;
				}

	}
	
	
	//获取ip地址
	   public  String getIpAddr(HttpServletRequest request) {
	        if (null == request) {
	            return null;
	        }
	        String proxs[] = { "X-Forwarded-For", "Proxy-Client-IP",
	                "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" ,"x-real-ip" };
	        String ip = null;
	        for (String prox : proxs) {
	            ip = request.getHeader(prox);
	            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
	                continue;
	            } else {
	                break;
	            }
	        }
	        if (StringUtils.isBlank(ip)) {
	        	System.out.println(request.getRemoteAddr());
	            return request.getRemoteAddr();
	        }
	        System.out.println(ip);
	        return ip;
	    }

}
