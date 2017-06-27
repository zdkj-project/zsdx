package com.zd.school.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService;
import com.zd.school.jw.arrangecourse.service.JwCourseteacherService;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Controller
@RequestMapping("/app/Inquirecourse")
public class InquirecourseAppController extends FrameWorkController<JwCourseArrange> implements Constant {
 	@Resource
    JwCourseArrangeService thisService; 
 	
    @Resource
     TeaTeacherbaseService teacherService;
    
    @Resource
    SysUserService userService;
    
    @Resource
    JwCourseteacherService courseteacherService;
    
    @Resource
    JwTGradeclassService classService;
    
    //根据老师查询课程
    @RequestMapping(value = { "/gettname" },  method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public @ResponseBody String gettname(String tid,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	SysUser currentUser = userService.get(tid);
    	String tname=currentUser.getXm();

        return tname;
    }
	

    
    //根据老师查询课程
    @RequestMapping(value = { "/teachercourse" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwCourseArrange> teachercourse(String tname,String place,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="select TEACHER_NAME0"+place+",COURSE_NAME0"+place+",TEACH_TIME,CLASS_NAME from  dbo.JW_T_COURSE_ARRANGE where TEACHER_NAME0"+place+"='"+tname+"' and EXT_FIELD05=1";
        List<JwCourseArrange> list = thisService.doQuerySql(hql);// 执行查询方法
        return list;
    }
    
    //根据老师ID查询班级
    @RequestMapping(value = { "/getclass" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwTGradeclass> getclass(String tid,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	String hql="from JwCourseteacher where tteacId='"+tid+"'";
    	List<JwCourseteacher> list =courseteacherService.doQuery(hql);
    	List<JwTGradeclass> classlist = new ArrayList<JwTGradeclass>();
    	for (JwCourseteacher courseTeacher : list) {
			String classhql="from JwTGradeclass where uuid='"+courseTeacher.getClaiId()+"'";
			List<JwTGradeclass> listtwo=classService.doQuery(classhql);
			classlist.add(listtwo.get(0));
		}
    	
    	
    	
    	
		return classlist;

    }
    
    
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwCourseArrange> list(@ModelAttribute JwCourseArrange entity,String cname,String isdelete, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where className='"+cname+"' and extField05=1 order by className,teachTime asc");
        List<JwCourseArrange> lists = thisService.doQuery(hql.toString());// 执行查询方法
        List<JwCourseArrange> newlists=new ArrayList<JwCourseArrange>();
        for(JwCourseArrange jca:lists){
        	jca.setWeekOne(jca.getCourseName01()+"("+jca.getTeacherName01()+")");
        	jca.setWeekTwo(jca.getCourseName02()+"("+jca.getTeacherName02()+")");
        	jca.setWeekThree(jca.getCourseName03()+"("+jca.getTeacherName03()+")");
        	jca.setWeekFour(jca.getCourseName04()+"("+jca.getTeacherName04()+")");
        	jca.setWeekFive(jca.getCourseName05()+"("+jca.getTeacherName05()+")");
        	jca.setWeekSix(jca.getCourseName06()+"("+jca.getTeacherName06()+")");
        	jca.setWeekSeven(jca.getCourseName07()+"("+jca.getTeacherName07()+")");
        	newlists.add(jca);
        }
        /*Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), newlists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
*/        return newlists;
    }
    
    

}
