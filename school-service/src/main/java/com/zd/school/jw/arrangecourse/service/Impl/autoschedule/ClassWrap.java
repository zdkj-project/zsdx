package com.zd.school.jw.arrangecourse.service.Impl.autoschedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.school.jw.arrangecourse.model.AcsBaseinfo;
import com.zd.school.jw.arrangecourse.model.AcsTimecourselimit;
import com.zd.school.jw.eduresources.model.JwTGradeclass;

public class ClassWrap {
	JwTGradeclass gclass;
	CourseWrap[] courses;
	AcsBaseinfo baseinfo;
	Schdulefeature schdulefeature;
	private static final Logger logger = LoggerFactory.getLogger(ClassWrap.class);
	public void schduleCourse(int jie){
		int num=jie%100;//第几节课
		int week=jie/100;//星期几
		for(CourseWrap c:courses){
			c.calculatePossby(jie);
		}
		printPossby(jie);
		takeCourseByPossby(jie);
		if(num==baseinfo.getDaynum()){//最后一节课排完
			resetDayData(week);
		}
	}
	
	private void printPossby(int jie){
		for(CourseWrap c:courses){
			logger.debug(gclass.getClassName()+"--"+jie+c.course.getCourseName()+c.possibility);
		}
	}
	private void takeCourseByPossby(int jie){
		int total=0;
		Map<Integer, CourseWrap> cs=new HashMap<Integer, CourseWrap>();
		for(int i=0; i<courses.length;i++){
			CourseWrap c=courses[i];
			if(c.possibility==AcsUtils.fixedCourseScore){//固定排课
				confrimCourse(c,jie,true);
				return;
			}
			if(c.possibility!=0){//排的课
				total+=c.possibility;
				cs.put(total, c);
			}
			if(i==(courses.length-1) && total==0){//不排课
				logger.debug(gclass.getClassName()+"不排课"+jie);
				confrimCourse(null,jie,false);
				return;
			}
		}
		 Random genr = new Random();
		 int indx=genr.nextInt(total)+1;
		 for(int key:cs.keySet()){
			 if(indx<=key){
				 confrimCourse(cs.get(key),jie,true);
				 return;
			 }
		 }
	}
	
	private void confrimCourse(CourseWrap cw, int jie,boolean isSchudle){
		AcsTimecourselimit exist= schdulefeature.schudle.get(jie);
		if(!isSchudle){//不排课排自习
			if(exist==null){
				AcsTimecourselimit acs=new AcsTimecourselimit();
				acs.CourseName="自习";
				acs.setClassid(gclass.getUuid());
				acs.setIsschedu(true);
				acs.setLessonnum(jie);
				schdulefeature.schudle.put(jie, acs);//班级
				return;
			}
		}
		if(exist!=null){
			if(exist.getLimittype()!=null){
			}else{
				logger.error("排课错误---"+gclass.getClassName()+jie+cw.course.getCourseName());
			}
			exist.teacherName=cw.teacherwp.teacher.getXm();
			exist.CourseName=cw.course.getCourseName();
			exist.teachergh=cw.teacherwp.teacher.getGh();
			exist.setTeacherid(cw.teacherwp.teacher.getUuid());
		}else{
			AcsTimecourselimit acs=new AcsTimecourselimit();
			acs.teacherName=cw.teacherwp.teacher.getXm();
			acs.CourseName=cw.course.getCourseName();
			acs.setClassid(gclass.getUuid());
			acs.setIsschedu(true);
			acs.setTeacherid(cw.teacherwp.teacher.getUuid());
			acs.setCourseid(cw.course.getUuid());
			acs.setLessonnum(jie);
			acs.teachergh=cw.teacherwp.teacher.getGh();
			schdulefeature.schudle.put(jie, acs);//班级
			cw.teacherwp.schdulefeature.schudle.put(jie, acs);
		}
		modifyDayData(cw,jie);
	}
	private void modifyDayData(CourseWrap cw, int jie){
		cw.schdulefeature.paidenum_day++;
		cw.schdulefeature.paiednum++;
		cw.schdulefeature.nupainum--;
	}
	private void resetDayData(int week){
		for(CourseWrap c:courses){
			c.schdulefeature.paidenum_day=0;
		}
	}
	
}
