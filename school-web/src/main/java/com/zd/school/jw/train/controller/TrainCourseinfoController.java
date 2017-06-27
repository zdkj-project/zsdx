
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainCourseEval;
import com.zd.school.jw.train.model.TrainCourseinfo;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.jw.train.service.TrainCoursecategoryService;
import com.zd.school.jw.train.service.TrainCourseinfoService;
import com.zd.school.jw.train.service.TrainTeacherService;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * ClassName: TrainCourseinfoController
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程信息(TRAIN_T_COURSEINFO)实体Controller.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainCourseinfo")
public class TrainCourseinfoController extends FrameWorkController<TrainCourseinfo> implements Constant {

    private static Logger logger = Logger.getLogger(TrainCourseinfoController.class);

    @Resource
    TrainCourseinfoService thisService; // service层接口

    @Resource
    private TrainCoursecategoryService coursecategoryService;

    @Resource
    private BaseDicitemService dicitemService;

    @Resource
    private TrainTeacherService teacherService;

    /**
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void list(@ModelAttribute TrainCourseinfo entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        QueryResult<TrainCourseinfo> qResult = thisService.list(start, limit, sort, filter, true);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/doadd")
    public void doAdd(TrainCourseinfo entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        //此处为放在入库前的一些检查的代码，如唯一校验等

        //获取当前操作用户
        SysUser currentUser = getCurrentSysUser();
        try {

/*			String hql1 = " o.isDelete='0' and o.categoryId='"+entity.getCategoryId()+"'";
            if (thisService.IsFieldExist("courseName", entity.getCourseName(), "-1", hql1)) {
	            writeJSON(response, jsonBuilder.returnFailureJson("\"课程名称不能重复！\""));
	            return;
	        }	        
	        if (thisService.IsFieldExist("courseCode", entity.getCourseCode(), "-1", hql1)) {
	            writeJSON(response, jsonBuilder.returnFailureJson("\"课程编码不能重复！\""));
	            return;
	        }*/

            entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
            if (ModelUtil.isNotNull(entity))
                writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
            else
                writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
        }
    }

    /**
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException 抛出异常
     * @Title: doDelete
     * @Description: 逻辑删除指定的数据
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            SysUser currentUser = getCurrentSysUser();
            try {
                boolean flag = thisService.doLogicDeleteByIds(delIds, currentUser);
                if (flag) {
                    writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
                } else {
                    writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
                }
            } catch (Exception e) {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
            }
        }
    }

    /**
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/doupdate")
    public void doUpdates(TrainCourseinfo entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        //入库前检查代码

        //获取当前的操作用户
        SysUser currentUser = getCurrentSysUser();
        try {

/*            String hql1 = " o.isDelete='0' and o.categoryId='" + entity.getCategoryId() + "'";
            if (thisService.IsFieldExist("courseName", entity.getCourseName(), entity.getUuid(), hql1)) {
                writeJSON(response, jsonBuilder.returnFailureJson("\"课程名称不能重复！\""));
                return;
            }
            if (thisService.IsFieldExist("courseCode", entity.getCourseCode(), entity.getUuid(), hql1)) {
                writeJSON(response, jsonBuilder.returnFailureJson("\"课程编码不能重复！\""));
                return;
            }*/


            entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
            if (ModelUtil.isNotNull(entity))
                writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
            else
                writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
        }
    }

    /**
     * 描述：通过传统方式form表单提交方式导入excel文件
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/importData", method = {RequestMethod.GET, RequestMethod.POST})
    public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

/*            String categoryId = null;
            String categoryCode = null;
            String teachType = null;
            String teacherId = null;
            SysUser currentUser = getCurrentSysUser();
            //所有的课程分类
            Map<String, TrainCoursecategory> mapCoursecategory = new HashMap<>();
            List<TrainCoursecategory> lisCourSecategory = coursecategoryService.doQueryAll();
            for (TrainCoursecategory trainCoursecategory : lisCourSecategory) {
                mapCoursecategory.put(trainCoursecategory.getNodeText(), trainCoursecategory);
            }
            //所有的教学形式字典项
            Map<String, String> mapTeachType = new HashMap<>();
            String hql = " from BaseDicitem where dicCode='TEACHTYPE'";
            List<BaseDicitem> listTeachType = dicitemService.doQuery(hql);
            for (BaseDicitem baseDicitem : listTeachType) {
                mapTeachType.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
            }

            //所有的教师
            Map<String, String> mapTeacher = new HashMap<>();
            hql = " from TrainTeacher where isDelete=0";
            List<TrainTeacher> listTeacher = teacherService.doQuery(hql);
            for (TrainTeacher trainTeacher : listTeacher) {
                mapTeacher.put(trainTeacher.getXm(), trainTeacher.getUuid());
            }*/
            InputStream in = null;
            List<List<Object>> listObject = null;
            SysUser currentUser = getCurrentSysUser();
            if (!file.isEmpty()) {
                in = file.getInputStream();
                listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
                in.close();
                thisService.doImportCourse(listObject,currentUser);
                /**
                 * 格式
                 * 第一行为列头【课程名称	所属类别	教学形式	课时	教学时长	课程学分	主讲老师	课程简介】
                 * 第二行开始为数据 【课程1	111	12	24	100	德玛西亚	课程才撒旦撒旦阿萨德 】
                 */
/*                for (int i = 0; i < listObject.size(); i++) {
                    List<Object> lo = listObject.get(i);
                    categoryId = mapCoursecategory.get(lo.get(1)).getUuid();
                    categoryCode = mapCoursecategory.get(lo.get(1)).getNodeCode();
                    teachType = mapTeachType.get(lo.get(2));
                    teacherId = mapTeacher.get(lo.get(6));

                    TrainCourseinfo course = new TrainCourseinfo();
                    course.setCategoryId(categoryId);
                    course.setCategoryCode(categoryCode);
                    course.setTeachType(teachType);
                    course.setCourseName(String.valueOf(lo.get(0)));
                    //course.setCourseCode(String.valueOf(lo.get(1)));

                    course.setPeriod(Integer.parseInt(lo.get(3).toString()));
                    course.setPeriodTime(Integer.parseInt(lo.get(4).toString()));
                    course.setCredits(Integer.parseInt(lo.get(5).toString()));

                    course.setMainTeacherName(String.valueOf(lo.get(6)));
                    course.setMainTeacherId(teacherId);
                    course.setCourseDesc(String.valueOf(lo.get(8)));
                    if (!StringUtils.isEmpty(teacherId))
                        thisService.doAddEntity(course, currentUser);

                }*/
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
            }

            writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = {"/courseteacher"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void courseteacherList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据

        String courseId = request.getParameter("courseId");
        if (StringUtils.isEmpty(courseId)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'没有传入查询参数'"));
            return;
        } else {
            List<TrainTeacher> listTeacher = thisService.getCourseTeacherList(courseId);

            strData = jsonBuilder.buildObjListToJson((long) listTeacher.size(), listTeacher, true);// 处理数据
            writeJSON(response, strData);// 返回数据
        }
    }

    /**
     * 导出课程列表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("exportCourseIsEnd", "0");
        String ids = request.getParameter("ids");
        String orderSql =request.getParameter("orderSql");
        try {
            List<TrainCourseinfo> list = thisService.listExport(ids, orderSql);
            FastExcel.exportExcel(response, "课程信息", list);
            request.getSession().setAttribute("exportCourseIsEnd", "1");
        } catch (IOException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute("exportCourseIsEnd", "1");
            writeJSON(response,jsonBuilder.returnFailureJson("\"文件导出失败，详情请见错误日志\""));
        }
    }

    @RequestMapping("/checkExportEnd")
    public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strData = "";
        Object isEnd = request.getSession().getAttribute("exportCourseIsEnd");

        if (isEnd != null) {
            if ("1".equals(isEnd.toString())) {
                writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
                request.getSession().setAttribute("exportCourseIsEnd", "0");
            }
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
            request.getSession().setAttribute("exportCourseIsEnd", "0");
        }
    }

    /**
     * 列出指定课程的评价情况
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = {"/getCourseEvalList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void getCourseEvalList( HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        String courseId = request.getParameter("courseId");

        List<TrainCourseEval> qResult = thisService.getCouseEvalList(start, limit, sort, filter, true,courseId);

        strData = jsonBuilder.buildObjListToJson(Long.valueOf(qResult.size()), qResult, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

}
