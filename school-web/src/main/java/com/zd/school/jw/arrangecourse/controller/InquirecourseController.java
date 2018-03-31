package com.zd.school.jw.arrangecourse.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Controller
@RequestMapping("/Inquirecourse")
public class InquirecourseController extends FrameWorkController<JwCourseArrange> implements Constant {
 	@Resource
    JwCourseArrangeService thisService; 
 	
    @Resource
    private TeaTeacherbaseService teacherService;
	
    //查询所有老师
    @RequestMapping(value = { "/teacherlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<TeaTeacherbase> teacherlist(String tname,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="from TeaTeacherbase where isdelete=0 and xm like '%"+tname+"%'";
        List<TeaTeacherbase> list = teacherService.getQuery(hql);// 执行查询方法
        return list;
    }
    
    //根据老师查询课程
    @RequestMapping(value = { "/teachercourse" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwCourseArrange> teachercourse(String tname,String place,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="select TEACHER_NAME0"+place+",COURSE_NAME0"+place+",TEACH_TIME,CLASS_NAME from  dbo.JW_T_COURSE_ARRANGE where TEACHER_NAME0"+place+"='"+tname+"' and EXT_FIELD05=1";
        List<JwCourseArrange> list = thisService.getQuerySql(hql);// 执行查询方法
        return list;
    }

}
