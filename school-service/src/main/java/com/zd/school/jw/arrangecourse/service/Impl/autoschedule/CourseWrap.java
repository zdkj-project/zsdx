package com.zd.school.jw.arrangecourse.service.Impl.autoschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.school.jw.arrangecourse.model.AcsBaseinfo;
import com.zd.school.jw.arrangecourse.model.AcsCoursefeature;
import com.zd.school.jw.arrangecourse.model.AcsTimecourselimit;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.plartform.system.model.SysUser;

public class CourseWrap {
 AcsCoursefeature feature;
 /**上本课程的老师 和其他课程共享同一变量**/
 TeacherWrap teacherwp;
 ClassWrap calsswp;
 AcsBaseinfo baseinfo;
 JwTBasecourse course;
 Schdulefeature schdulefeature;
 String pubClassid;//公用教室ID
 public int possibility=-1;
 private static final Logger logger = LoggerFactory.getLogger(CourseWrap.class);
 
 
 
 /**计算课的排课可能值**/
 public void calculatePossby(int jie){
	 possibility=-1;
	 int num=jie%100;//第几节课
	 int week=jie/100;//星期几
	 if(calculateLimit(jie)){
		 return;
	 }
	 
	 if(schdulefeature.getPaiMaxNum_day(week)==0){ possibility= 0; return;}//所有课已经排完
	 if(avgScheduled(jie))return;
	 mainCourse(jie);
 }
 /**大概的平均 分散课时**/
 private  boolean avgScheduled(int jie){
	 int num=jie%100;//第几节课
	 int week=jie/100;//星期几
	double avg= schdulefeature.getAvgPaiNum(baseinfo.getWeekday());
	double davg= AcsUtils.div(String.valueOf(schdulefeature.paiednum), String.valueOf(week), 2);
	if(davg>avg){
			possibility=0;return true;
	}
	return Boolean.FALSE;
 }
 
 /**是否主课逻辑**/
 private void mainCourse(int jie){
	 int num=jie%100;//第几节课
	 int week=jie/100;//星期几
	 boolean isam=num<=baseinfo.getNumam();
	// boolean lkzyksw=baseinfo.getLkzyksw();//班级连课在一块上完
	 if(feature.getMaincourse()){//主课
		possibility+=AcsUtils.mainCourseScore;
		if(isam){
			//主课在上午
	 		possibility+=AcsUtils.am;
		}else{
				
		}
	 }else{
		 if(num==1 || num==2){ possibility =0;return;}//第一节和第二节非主课不排
		 if(isam){
			// possibility+=20;//非主课在上午
		 }else{
			 possibility+=AcsUtils.am;
		 }
	 }
	 possibility+=schdulefeature.getPaiMaxNum_day(week)*AcsUtils.mainCourseScore;
 }
 


 public boolean calculateLimit(int jie){
	 teacherLimit(jie);
	 classLimit(jie);
	 courseLimit(jie);
	 if(possibility!=-1)
		 return true;
	 return false;
 }
 
 private void teacherLimit(int jie){
	 AcsTimecourselimit limit=teacherwp.schdulefeature.schudle.get(jie);
	 if(limit!=null){//本节课老师有限制
		 if(limit.getIsschedu()){//自动排课时增加的
			 if(limit.getCourseid().equals(course.getUuid())&&
					limit.getClassid().equals(calsswp.gclass.getUuid())){//老师在其他班上课
				 logger.error(jie+calsswp.gclass.getClassName()+"指定排本课"+course.getCourseName()+teacherwp.teacher.getUserName());
			 }
			 possibility=0; return;
		 }else{//教师在本节不排课
			 logger.debug(jie+"不排"+course.getCourseName()+teacherwp.teacher.getUserName());
			 possibility=0; return;
		 }
		 
	 }
 }
 
 
 private void classLimit(int jie){
	 AcsTimecourselimit limit=calsswp.schdulefeature.schudle.get(jie);
	 if(limit!=null){//本节课班级有限制
		 if(limit.getIsschedu()){//有自习课
			 if(limit.getCourseid().equals(course.getUuid())){//
				 logger.debug(jie+calsswp.gclass.getClassName()+"指定排本课"+course.getCourseName()+teacherwp.teacher.getXm());
				 possibility=AcsUtils.fixedCourseScore; return;
			 }else{
				 logger.debug(jie+"不排"+course.getCourseName()+"排其他课程");
				 possibility=0;
				 
			 }
		 }else{//在本节不排课
			 logger.debug(jie+"不排课");
			 possibility=0; return;
		 }
	 }
 }
 
 private void courseLimit(int jie){
	 AcsTimecourselimit limit=schdulefeature.schudle.get(jie);
	 if(limit!=null){//本节课班级有限制,本节课不排
		 logger.debug(jie+"不排课"+course.getAliasName());
		 possibility=0; return;
	 }
 }
 
 public int joinCourseNum(){
	int wd=baseinfo.getWeekday();
	if(schdulefeature.allnum-wd>0){
		return schdulefeature.allnum-wd;
	}
	return 0;
 }
 
 public void clearDayData(){
	int wd=baseinfo.getWeekday();
	
 }
}
