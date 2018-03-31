
package com.zd.school.jw.arrangecourse.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.core.util.TLVUtils;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher;
import com.zd.school.jw.arrangecourse.service.JwCourseteacherService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: JwCourseteacherController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 教师任课信息(JW_T_COURSETEACHER)实体Controller.
 * date: 2016-08-26
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwCourseteacher")
public class JwCourseteacherController extends FrameWorkController<JwCourseteacher> implements Constant {

    @Resource
    JwCourseteacherService thisService; // service层接口
    @Resource 
    private TeaTeacherbaseService teacherService;
    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute JwCourseteacher entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
		String claiId = request.getParameter("claiId");
		Integer claiLevel=0;
		if(request.getParameter("claiLevel")!=null){
			claiLevel = Integer.parseInt(request.getParameter("claiLevel")); 
		}
        QueryResult<JwCourseteacher> qr = thisService.getClassCourseTeacherList(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true,claiId,claiLevel);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * 
     * getYearCourseTeacherList:指定学年、学期的所有班级的任课教师.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/getYearCourseTeacherList")
    public void getYearCourseTeacherList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer studyYeah = Integer.parseInt(request.getParameter("studyYeah"));
        String semester = request.getParameter("semester");

        String hql = " from JwCourseteacher where studyYeah=" + studyYeah + " and semester='" + semester
                + "' and isDelete='0' ";
        List<JwCourseteacher> lists = thisService.getQuery(hql);
        String strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * doAddCourseTeacher:设置任课教师
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/doAddCourseTeacher")
    public void doAddCourseTeacher(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String jsonData = request.getParameter("jsonData");
        String removeIds = request.getParameter("removeIds");
        int studyYeah = Integer.parseInt(request.getParameter("studyYeah"));
        String semester = request.getParameter("semester");
        SysUser sysuser = getCurrentSysUser();
        Boolean strData = thisService.doAddCourseTeacher(studyYeah, semester, jsonData, removeIds, sysuser);
        if (strData)
            writeJSON(response, jsonBuilder.returnSuccessJson("'设置任课教师成功'"));
        else
            writeJSON(response, jsonBuilder.returnFailureJson("'设置任课教师失败'"));
    }
    
    /**
     * 更新课程周节数
     */
    @RequestMapping("/updateZjs")
    public void updateZjs(	@RequestParam("zjs") int zjs,HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	String claiId = request.getParameter("claiId");
    	String courseId = request.getParameter("courseId");
    	String batch = request.getParameter("batch");
    	if(batch!=null){
    		thisService.updateZjsByClassId(claiId, courseId, zjs);
    	}else{
			thisService.doUpdateByProperties(new String[]{"claiId","courseId"}, new Object[]{claiId,courseId},new String[]{"acszjs"}, new Object[]{zjs});
    	}
    	writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));
    }
    
    
}
