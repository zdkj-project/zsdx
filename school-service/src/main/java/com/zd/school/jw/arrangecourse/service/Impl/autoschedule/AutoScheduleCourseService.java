package com.zd.school.jw.arrangecourse.service.Impl.autoschedule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.util.JsonBuilder;
import com.zd.school.jw.arrangecourse.model.AcsBaseinfo;
import com.zd.school.jw.arrangecourse.model.AcsCoursefeature;
import com.zd.school.jw.arrangecourse.model.AcsTimecourselimit;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher;
import com.zd.school.jw.arrangecourse.service.AcsBaseinfoService;
import com.zd.school.jw.arrangecourse.service.AcsCoursefeatureService;
import com.zd.school.jw.arrangecourse.service.AcsPublicclassService;
import com.zd.school.jw.arrangecourse.service.AcsTeacherfeatureService;
import com.zd.school.jw.arrangecourse.service.AcsTimecourselimitService;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService;
import com.zd.school.jw.arrangecourse.service.JwCourseteacherService;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTBasecourseService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Service
@Transactional
public class AutoScheduleCourseService {

	@Resource
	JwTBasecourseService basecourseService; 
	@Resource
	JwCourseteacherService courseteacherService;
	@Resource
	AcsBaseinfoService baseinfoService;

	@Resource
	AcsCoursefeatureService coursefeatureService;
	@Resource
	AcsTeacherfeatureService teacherfeatureService;
	@Resource
	AcsPublicclassService publicclassService;
	@Resource
	AcsTimecourselimitService timecourselimitService;
	@Resource
	TeaTeacherbaseService teaTeacherbaseService;
	@Resource
	JwTGradeclassService jwTGradeclassService;
	JwCourseArrangeService courseArrangeService;
	private static final Logger logger = LoggerFactory.getLogger(AutoScheduleCourseService.class);
	public  ClassWrap[] startScheduleCourse(AcsBaseinfo acsbaseinfo) throws Exception{
		String queryClassHQL="select gc from JwTGrade grade, JwTGradeclass gc where gc.graiId=grade.uuid and grade.sectionCode='"+acsbaseinfo.getStage()+"'";
		String queryTeacherHQL="select t from TeaTeacherbase t ,SysUser su where su.uuid=t.uuid and "
				+ "exists(select gc from JwTGradeclass gc,JwTGrade g,JwCourseteacher ct where  ct.tteacId =t.uuid and ct.claiId=gc.uuid and  g.uuid=gc.graiId and g.sectionCode='"+acsbaseinfo.getStage()+"')  ";
		List<TeaTeacherbase> teachers= teaTeacherbaseService.doQuery(queryTeacherHQL);
		List<JwTGradeclass> gclases =jwTGradeclassService.doQuery(queryClassHQL);
		List<AcsCoursefeature> coursefeatures=coursefeatureService.doQueryAll();
		List<TeacherWrap> teacherwps=initTeacherWrap( teachers);
		ClassWrap[] classWraps=new ClassWrap[gclases.size()];
		for(int i=0;i<gclases.size();i++){
			ClassWrap cw=new ClassWrap();
			cw.gclass=gclases.get(i);
			cw.baseinfo=acsbaseinfo;
			initClassSchdulefeature(cw);
			classRelatCourse(cw,acsbaseinfo,coursefeatures,teacherwps);
			classWraps[i]=cw;
		}
		
		startScheduleCourse(classWraps,acsbaseinfo);
		//saveScheduleResult(acsbaseinfo);
		return classWraps;
		//startScheduleCourse_ClassProty(classWarps,acsbaseinfo);
	}
	private ClassWrap[] startScheduleCourse(ClassWrap[] classWraps,AcsBaseinfo acsbaseinfo) throws Exception{
		int week=acsbaseinfo.getWeekday();
		int lessonum=acsbaseinfo.getDaynum();
	//	org.apache.commons.lang3.ArrayUtils.reverse(classWraps);
		for(int weeknum=1; weeknum<=week;weeknum++){
			for(int lesnum=1; lesnum<=lessonum;lesnum++){
				for(ClassWrap classwrap:classWraps){
					classwrap.schduleCourse(weeknum*100+lesnum);
				}
			}
			
		}
		annalyReslt(classWraps,acsbaseinfo);
		printSchedule(classWraps,acsbaseinfo);
		return classWraps;
	}
	public List<JwCourseArrange> convertClsWpList2CrsArgeList(ClassWrap[] classWraps,AcsBaseinfo acsbaseinfo){
		int week=acsbaseinfo.getWeekday();
		int lessonum=acsbaseinfo.getDaynum();
		List<JwCourseArrange> coursearglist=new ArrayList<JwCourseArrange>();
		for(ClassWrap classwrap:classWraps){
			for(int lesnum=1; lesnum<=lessonum;lesnum++){
				JwCourseArrange courseArrange= new JwCourseArrange();
				courseArrange.setClaiId(classwrap.gclass.getUuid());
				courseArrange.setClassName(classwrap.gclass.getClassName());
				courseArrange.setSchoolTerm(acsbaseinfo.getSchoolterm());
				courseArrange.setSchoolYear(acsbaseinfo.getSchoolyear());
				courseArrange.setTeachTime(String.valueOf(lesnum));
					for(int weeknum=1; weeknum<=week;weeknum++){
						int jie=weeknum*100+lesnum;
						AcsTimecourselimit limit=classwrap.schdulefeature.schudle.get(jie);
						try {
							if(limit==null){
								BeanUtils.setProperty(courseArrange, "courseId0"+weeknum, null);
								BeanUtils.setProperty(courseArrange, "courseName0"+weeknum, null);
								BeanUtils.setProperty(courseArrange, "tteacId0"+weeknum, null);
								BeanUtils.setProperty(courseArrange, "teacherGh0"+weeknum, null);
								BeanUtils.setProperty(courseArrange, "teacherName0"+weeknum, null);
							}else{
								BeanUtils.setProperty(courseArrange, "courseId0"+weeknum, limit.getCourseid());
								BeanUtils.setProperty(courseArrange, "courseName0"+weeknum, limit.CourseName);
								BeanUtils.setProperty(courseArrange, "tteacId0"+weeknum, limit.getTeacherid());
								BeanUtils.setProperty(courseArrange, "teacherGh0"+weeknum, limit.teachergh);
								BeanUtils.setProperty(courseArrange, "teacherName0"+weeknum, limit.teacherName);
							}
							
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					coursearglist.add(courseArrange);
				}
		}
		return coursearglist;
	}
	
	private void saveScheduleResult(ClassWrap[] classWraps,AcsBaseinfo acsbaseinfo){
		List<JwCourseArrange> coursearglist=convertClsWpList2CrsArgeList(classWraps, acsbaseinfo);
		for(JwCourseArrange jca:coursearglist){
			courseArrangeService.merge(jca);
		}
	}
	
	/**班级优先排
	 * @throws Exception ***/
	private void startScheduleCourse_ClassProty(ClassWrap[] classWraps,AcsBaseinfo acsbaseinfo) throws Exception{
		int week=acsbaseinfo.getWeekday();
		int lessonum=acsbaseinfo.getDaynum();
		ArrayUtils.reverse(classWraps);
		for(ClassWrap classwrap:classWraps){
			for(int weeknum=1; weeknum<=week;weeknum++){
				for(int lesnum=1; lesnum<=lessonum;lesnum++){
						classwrap.schduleCourse(weeknum*100+lesnum);
				}
			}
		}
		annalyReslt(classWraps,acsbaseinfo);
		printSchedule(classWraps,acsbaseinfo);
	}
	
	public  List<JwCourseArrange> findTeacherCourseInWeek(ClassWrap[] classWraps,AcsBaseinfo acsbaseinfo,String teacherid) throws IllegalAccessException, InvocationTargetException{
		int week=acsbaseinfo.getWeekday();
		int lessonum=acsbaseinfo.getDaynum();
		List<JwCourseArrange> list =new ArrayList<JwCourseArrange> ();
		for(int lesnum=1; lesnum<=lessonum;lesnum++){
			JwCourseArrange courseArrange= new JwCourseArrange();
			for(int weeknum=1; weeknum<=week;weeknum++){
				int jie=weeknum*100+lesnum;
				for(ClassWrap classwrap:classWraps){
					AcsTimecourselimit limit=classwrap.schdulefeature.schudle.get(jie);
					if(limit.getTeacherid()!=null&&limit.getTeacherid().equals(teacherid)){
						BeanUtils.setProperty(courseArrange, "courseId0"+weeknum, limit.getCourseid());
						BeanUtils.setProperty(courseArrange, "courseName0"+weeknum, limit.CourseName);
						BeanUtils.setProperty(courseArrange, "tteacId0"+weeknum, limit.getTeacherid());
						BeanUtils.setProperty(courseArrange, "teacherGh0"+weeknum, limit.teachergh);
						BeanUtils.setProperty(courseArrange, "teacherName0"+weeknum, limit.teacherName);
					}
					
				}
			}
			list.add(courseArrange);
		}
		return list;
	}
	
	public List<Map<String, Object>> convert2dataView(List<JwCourseArrange> list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Map<String, Object>> list2= new ArrayList<Map<String, Object>>();
		String currt=list.get(0).getClaiId();
		Map<String, Object> map=new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			JwCourseArrange ca=list.get(i);
			if(currt.equals(ca.getClaiId())){
				String jie=ca.getTeachTime();
				Map<String, String> tp=BeanUtils.describe(ca);
				Object[] array= tp.keySet().toArray();
				for(Object key:array){
					String v=tp.get(key);
					tp.remove(key);
					tp.put(key+jie, v);
				}
				map.putAll(tp);
			}else{
				list2.add(map);
				map=new HashMap<String, Object>();
				currt=list.get(i).getClaiId();
				i--;
			}
		}
		return list2;
	}
	
	/**哪些课没有排完
	 * @param acsbaseinfo 
	 * @throws Exception ***/
	private void annalyReslt(ClassWrap[] classWraps, AcsBaseinfo acsbaseinfo) throws Exception{
		boolean repetSchedule=false;
		String exceptionMsg="";
		for(ClassWrap cw:classWraps){
			for(CourseWrap cwp:cw.courses){
				if(cwp.schdulefeature.nupainum !=0){
					exceptionMsg+=cw.gclass.getClassName()+cwp.course.getCourseName()+cwp.teacherwp.teacher.getXm()+cwp.schdulefeature.nupainum+"节未排课<br/>";
					repetSchedule=true;
				}
			}
		}
		System.out.println(exceptionMsg);
		if(repetSchedule){
			logger.error("---重新排课----");
			startScheduleCourse(acsbaseinfo);
			acsbaseinfo.repeatScheduleNum++;
			exceptionMsg.startsWith("第"+acsbaseinfo.repeatScheduleNum+"次排课：<br/>");
			if(acsbaseinfo.repeatScheduleNum==AcsUtils.repetScheduleNumThrowExcetption){
				throw new Exception(exceptionMsg);
			}
		}
	}
	private void printSchedule(ClassWrap[] cws,AcsBaseinfo acsbaseinfo){
		for(ClassWrap cw:cws){
			System.out.println(cw.gclass.getClassName()+"-"+cw.courses.length+"--");
			printCourse(cw);
			int week=acsbaseinfo.getWeekday();
			int lessonum=acsbaseinfo.getDaynum();
			for(int weeknum=1; weeknum<=week;weeknum++){
				for(int lesnum=1; lesnum<=lessonum;lesnum++){
					int temp=weeknum*100+lesnum;
					AcsTimecourselimit limit=cw.schdulefeature.schudle.get(temp);
					if(limit!=null)
						System.out.print("【"+limit.getLessonnum()+limit.CourseName+limit.teacherName+"】");
					else
						System.out.print("【"+temp+"****"+"****】");
				}
				System.out.print ("----");
			}
			System.out.println();
		}
		System.out.println("");
	}
	private void printCourse(ClassWrap cw){
		String print="";
		for(CourseWrap coursewp:cw.courses){
			print+=coursewp.course.getCourseName()+coursewp.schdulefeature.allnum+"|";
		}
		System.out.println(print);
	}
	private void sortLesson(ClassWrap[] cws){
		Comparator compatr=	new Comparator<Map.Entry<Integer,AcsTimecourselimit>>() {
            //升序排序
            public int compare(Entry<Integer,AcsTimecourselimit> o1,
                    Entry<Integer,AcsTimecourselimit> o2) {
                return o1.getValue().getLessonnum()-o2.getValue().getLessonnum();
            }
        };
		for(ClassWrap cw:cws){
			 List<Map.Entry<Integer,AcsTimecourselimit>> list = new ArrayList<Map.Entry<Integer,AcsTimecourselimit>>(cw.schdulefeature.schudle.entrySet());
		     Collections.sort(list,compatr);
		}
	}
	private void classRelatCourse(ClassWrap cw,AcsBaseinfo acsbaseinfo,List<AcsCoursefeature> coursefeatures,List<TeacherWrap> teacherwps){
		String classCoursehql="select bc,ct from JwTBasecourse bc,JwCourseteacher ct where bc.uuid =ct.courseId  and  ct.claiId=?";
		List<Object[]> bs=baseinfoService.getForValues(classCoursehql,cw.gclass.getUuid());
		CourseWrap[] coursWraps=new CourseWrap[bs.size()];
		for(int i=0;i<bs.size();i++){
			Object[] os=bs.get(i);
			JwTBasecourse bc=(JwTBasecourse)os[0];
			JwCourseteacher ct=(JwCourseteacher)os[1];
			CourseWrap temp=new CourseWrap();
			temp.calsswp=cw;
			temp.course=bc;
			temp.baseinfo=acsbaseinfo;
			initCourseSchdulefeature(temp,ct);
			temp.feature=findAcsCoursefeature(coursefeatures,bc.getUuid());
			courseRelatTeacherWrap(temp, ct, teacherwps);
			coursWraps[i]=temp;
			logger.debug(cw.gclass.getClassName()+bs.size()+"门课--"+bc.getCourseName());
		}
		cw.courses=coursWraps;
	}
	
	private void courseRelatTeacherWrap(CourseWrap cw,JwCourseteacher ct,List<TeacherWrap> teacherwps){
		TeacherWrap t=findTeacherwp(teacherwps, ct.getTteacId());
		cw.teacherwp=t;
	}
	private List<TeacherWrap>  initTeacherWrap(List<TeaTeacherbase> teachers){
		List<TeacherWrap> teacherwps=new ArrayList<TeacherWrap>();
		for(TeaTeacherbase teacher:teachers){
			TeacherWrap tw=new TeacherWrap();
			tw.feature=teacherfeatureService.getByProerties("teacherid", teacher.getUuid());
			tw.teacher=teacher;
			List<AcsTimecourselimit> limits=timecourselimitService.queryByProerties("teacherid", teacher.getUuid());
			tw.schdulefeature=new Schdulefeature();
			tw.schdulefeature.schudle=listToMap(limits);
			teacherwps.add(tw);
		}
		return teacherwps;
	}
	private void initCourseSchdulefeature(CourseWrap cw,JwCourseteacher ct){
		cw.schdulefeature=new Schdulefeature();
		if(ct.getAcszjs()==null){
			logger.error(cw.course.getAliasName()+cw.calsswp.gclass.getClassName()+"没有设置周课时");
			 Random genr = new Random();
			 int indx=genr.nextInt(3)+1;
			 indx=2;
			cw.schdulefeature.allnum=indx;
			cw.schdulefeature.nupainum=indx;
		}else{
			cw.schdulefeature.allnum=ct.getAcszjs();
			cw.schdulefeature.nupainum=ct.getAcszjs();
		}
	
		List<AcsTimecourselimit> list=timecourselimitService.queryByProerties("courseid", cw.course.getUuid());
		cw.schdulefeature.schudle=listToMap(list);
	}
	
	private void initClassSchdulefeature(ClassWrap cw){
		cw.schdulefeature=new Schdulefeature();
		List<AcsTimecourselimit> list=timecourselimitService.queryByProerties("classid", cw.gclass.getUuid());
		setClassLimitCourseName(list);
		cw.schdulefeature.schudle=listToMap(list);
	}
	/**取出自习 课本等课名字**/
	private void setClassLimitCourseName(List<AcsTimecourselimit> list){
		for(AcsTimecourselimit limit:list){
			if(limit.getTeacherid()==null&&limit.getCourseid()!=null){//设置了自习课
				JwTBasecourse course=basecourseService.getByProerties("uuid", limit.getCourseid());
				limit.CourseName=course.getCourseName();
			}
		}
	}
	
	private Map<Integer,AcsTimecourselimit>  listToMap(List<AcsTimecourselimit> list){
		Map<Integer,AcsTimecourselimit>  map=new HashMap<Integer,AcsTimecourselimit>();
		for(AcsTimecourselimit t:list){
			map.put(t.getLessonnum(), t);
		}
		return map;
	}
	
	private AcsCoursefeature findAcsCoursefeature(List<AcsCoursefeature> coursefeatures,String uuid){
		AcsCoursefeature temp=null;
		for(AcsCoursefeature cf:coursefeatures){
			if(cf.getCourseid().equals(uuid)){
				temp=cf;
				break;
			}
		}
		if(temp!=null){
			return temp;
		}else{
			return new AcsCoursefeature().AcsCoursefeature_Init();
		}
	}
	
	private TeacherWrap findTeacherwp(List<TeacherWrap> teachers,String uuid){
		TeacherWrap temp=null;
		for(TeacherWrap cf:teachers){
			if(cf.teacher.getUuid().equals(uuid)){
				temp=cf;
				break;
			}
		}
		if(temp!=null){
			return temp;
		}else{
			logger.error("没有找到老师通过老师UUID");
			return null;
		}
	}
	
	public static void main(String[] args) {
			System.out.format("%-6s", "dfdfddfdfddfdfddfdfddfdfddfdfddfdfddfdfd");
	}

	
}
