package com.zd.school.jw.arrangecourse.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.jw.arrangecourse.model.JwCourseStudy;
import com.zd.school.jw.arrangecourse.service.JwCourseStudyService;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTBasecourseService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: JwCourseStudyController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 自习课程表实体Controller. date: 2016-04-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwTCourseStudy")
public class JwCourseStudyController extends FrameWorkController<JwCourseStudy> implements Constant {

    @Resource
    JwCourseStudyService thisService; // service层接口

    @Resource
    JwTGradeclassService classService;

    @Resource
    TeaTeacherbaseService teacherService;

    @Resource
    JwTBasecourseService courseService;

    @Resource
    SessionFactory sessionFactory;

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwCourseStudy> list(String cname, String morning, String night, String schoolyear,
            String schoolterm, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //查询班级对应的id
        String classid = "";
        String classcode = "";
        String classhql = "from JwTGradeclass where className='" + cname + "'";
        List<JwTGradeclass> list = classService.getQuery(classhql);
        for (JwTGradeclass jclass : list) {
            classid = jclass.getUuid();
            classcode = jclass.getClassCode();
        }

        // hql语句
        String hql = "select count(*)  from  JwCourseStudy where className='" + cname + "'";
        Integer morningnumber = Integer.parseInt(morning);
        Integer nightnumber = Integer.parseInt(night);
        //查询自习节数
        Integer classcount = thisService.getCount(hql);
        JwCourseStudy entity = null;
        if (classcount == 0) {
            for (int i = 1; i <= morningnumber; i++) {
                entity = new JwCourseStudy();
                entity.setSchoolId("402881e4536e2d6401536e2d926a0000");
                entity.setSchoolName("深大附中");
                entity.setSchoolYear(schoolyear);
                entity.setSchoolTerm(schoolterm);
                entity.setCourseName01("无课程");
                entity.setCourseName02("无课程");
                entity.setCourseName03("无课程");
                entity.setCourseName04("无课程");
                entity.setCourseName05("无课程");
                entity.setCourseName06("无课程");
                entity.setCourseName07("无课程");
                entity.setTeacherName01("无老师");
                entity.setTeacherName02("无老师");
                entity.setTeacherName03("无老师");
                entity.setTeacherName04("无老师");
                entity.setTeacherName05("无老师");
                entity.setTeacherName06("无老师");
                entity.setTeacherName07("无老师");
                entity.setClaiId(classid);
                entity.setClassCode(classcode);
                entity.setClassName(cname);
                entity.setStudyCategory("早自习" + i);
                entity.setOrderIndex(i);
                thisService.doMerge(entity);
            }
            for (int i = 6; i <= (5 + nightnumber); i++) {
                entity = new JwCourseStudy();
                entity.setSchoolId("402881e4536e2d6401536e2d926a0000");
                entity.setSchoolName("深大附中");
                entity.setSchoolYear(schoolyear);
                entity.setSchoolTerm(schoolterm);
                entity.setCourseName01("无课程");
                entity.setCourseName02("无课程");
                entity.setCourseName03("无课程");
                entity.setCourseName04("无课程");
                entity.setCourseName05("无课程");
                entity.setCourseName06("无课程");
                entity.setCourseName07("无课程");
                entity.setTeacherName01("无老师");
                entity.setTeacherName02("无老师");
                entity.setTeacherName03("无老师");
                entity.setTeacherName04("无老师");
                entity.setTeacherName05("无老师");
                entity.setTeacherName06("无老师");
                entity.setTeacherName07("无老师");
                entity.setClaiId(classid);
                entity.setClassCode(classcode);
                entity.setClassName(cname);
                entity.setStudyCategory("晚自习" + (i - 5));
                entity.setOrderIndex(i);
                thisService.doMerge(entity);
            }
            String hql1 = "from JwCourseStudy where className='" + cname + "' order by className,orderIndex asc";
            List<JwCourseStudy> list1 = thisService.getQuery(hql1);
            return list1;
        } else {
            //查询早自习晚自习节数
            String hqlmorning = "select count(*) from JwCourseStudy where className='" + cname + "' and orderIndex<6";
            String hqlnight = "select count(*) from JwCourseStudy where className='" + cname
                    + "' and orderIndex<11 and orderIndex>5";
            Integer countmorning = thisService.getCount(hqlmorning);
            Integer countnight = thisService.getCount(hqlnight);
            if (countmorning <= morningnumber) {
                for (int i = (countmorning + 1); i <= morningnumber; i++) {
                    entity = new JwCourseStudy();
                    entity.setSchoolId("402881e4536e2d6401536e2d926a0000");
                    entity.setSchoolName("深大附中");
                    entity.setSchoolYear(schoolyear);
                    entity.setSchoolTerm(schoolterm);
                    entity.setCourseName01("无课程");
                    entity.setCourseName02("无课程");
                    entity.setCourseName03("无课程");
                    entity.setCourseName04("无课程");
                    entity.setCourseName05("无课程");
                    entity.setCourseName06("无课程");
                    entity.setCourseName07("无课程");
                    entity.setTeacherName01("无老师");
                    entity.setTeacherName02("无老师");
                    entity.setTeacherName03("无老师");
                    entity.setTeacherName04("无老师");
                    entity.setTeacherName05("无老师");
                    entity.setTeacherName06("无老师");
                    entity.setTeacherName07("无老师");
                    entity.setClaiId(classid);
                    entity.setClassCode(classcode);
                    entity.setClassName(cname);
                    entity.setStudyCategory("早自习" + i);
                    entity.setOrderIndex(i);
                    thisService.doMerge(entity);
                }
            }
            if (countnight <= nightnumber) {
                for (int i = (countnight + 6); i <= (5 + nightnumber); i++) {
                    entity = new JwCourseStudy();
                    entity.setSchoolId("402881e4536e2d6401536e2d926a0000");
                    entity.setSchoolName("深大附中");
                    entity.setSchoolYear(schoolyear);
                    entity.setSchoolTerm(schoolterm);
                    entity.setCourseName01("无课程");
                    entity.setCourseName02("无课程");
                    entity.setCourseName03("无课程");
                    entity.setCourseName04("无课程");
                    entity.setCourseName05("无课程");
                    entity.setCourseName06("无课程");
                    entity.setCourseName07("无课程");
                    entity.setTeacherName01("无老师");
                    entity.setTeacherName02("无老师");
                    entity.setTeacherName03("无老师");
                    entity.setTeacherName04("无老师");
                    entity.setTeacherName05("无老师");
                    entity.setTeacherName06("无老师");
                    entity.setTeacherName07("无老师");
                    entity.setClaiId(classid);
                    entity.setClassCode(classcode);
                    entity.setClassName(cname);
                    entity.setStudyCategory("晚自习" + (i - 5));
                    entity.setOrderIndex(i);
                    thisService.doMerge(entity);
                }
            }
            if (countmorning > morningnumber) {
                for (int i = countmorning; i > morningnumber; i--) {
                    String deletemorning = "delete JwCourseStudy where className='" + cname + "' and orderIndex='" + i
                            + "'";
                    thisService.doExecuteHql(deletemorning);
                }
            }
            if (countnight > nightnumber) {
                for (int i = (5 + countnight); i > (5 + nightnumber); i--) {
                    String deletenight = "delete JwCourseStudy where className='" + cname + "' and orderIndex='" + i
                            + "'";
                    thisService.doExecuteHql(deletenight);
                }
            }
            String hql1 = "from JwCourseStudy where className='" + cname + "' order by className,orderIndex asc";
            List<JwCourseStudy> list1 = thisService.getQuery(hql1);
            return list1;

        }

    }

    //查询所有班级
    @RequestMapping(value = { "/classlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwTGradeclass> classlist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
        String hql = "from JwTGradeclass where isdelete=0";
        List<JwTGradeclass> list = classService.getQuery(hql);// 执行查询方法
        return list;
    }

    //查询所有老师
    @RequestMapping(value = { "/teacherlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<TeaTeacherbase> teacherlist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
        String hql = "from TeaTeacherbase where isdelete=0";
        List<TeaTeacherbase> list = teacherService.getQuery(hql);// 执行查询方法
        return list;
    }

    //查询所有课程
    @RequestMapping(value = { "/courselist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwTBasecourse> courselist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
        String hql = "from JwTBasecourse where isdelete=0";
        List<JwTBasecourse> list = courseService.getQuery(hql);// 执行查询方法
        return list;
    }

    //按班级指定查询课表显示
    @RequestMapping(value = { "/listtwo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwCourseStudy> listtwo(String cname, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // hql语句
        String hql = "from JwCourseStudy where className='" + cname + "' order by className,orderIndex asc";
        List<JwCourseStudy> list = thisService.getQuery(hql);// 执行查询方法
        return list;
    }

    //根据班级查询教师
    @RequestMapping(value = { "/courseteacherlist" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<Object[]> courseteacherlist(String cid, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // hql语句

        String sql = "SELECT a.UUID,a.CLAI_ID,a.TTEAC_ID,b.xm,b.user_numb FROM dbo.JW_T_COURSETEACHER a LEFT JOIN dbo.Sys_t_user b ON a.TTEAC_ID=b.user_id WHERE a.CLAI_ID='"
                + cid + "' and a.isdelete=0 and b.isdelete=0";
        List<Object[]> list = courseService.ObjectQuerySql(sql);
        return list;
    }

    @RequestMapping(value = { "/copy" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody String copy(String cname, String cname2, String classid, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // hql语句
        String sql = "delete dbo.JW_T_COURSE_STUDY where class_name='" + cname2 + "'";
        thisService.doExecuteSql(sql);

        String copysql = "insert into  dbo.JW_T_COURSE_STUDY select NEWID(), SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, '"
                + classid + "', '', '" + cname2
                + "', STUDY_CATEGORY, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, EXT_FIELD05, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, ISDELETE, VERSION from dbo.JW_T_COURSE_STUDY where  CLASS_NAME='"
                + cname + "'";
        thisService.doExecuteSql(copysql);
        return "true";
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * JwCourseStudy @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/updatestudy")
    public void doUpdates(String updatestudy, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        //拆分前台传过来的数据
        String[] updatestr = updatestudy.split(",");
        String id = updatestr[0];
        String place = updatestr[1];
        String cid = updatestr[2];
        String cname = updatestr[3];
        String tid = updatestr[4];
        String tgh = updatestr[5];
        String tname = updatestr[6];

        String hql = "update JwCourseStudy set courseId" + place + "='" + cid + "',courseName" + place + "='" + cname
                + "',tteacId" + place + "='" + tid + "',teacherGh" + place + "='" + tgh + "',teacherName" + place + "='"
                + tname + "' where zxid='" + id + "'";
        thisService.doExecuteHql(hql);
    }
}
