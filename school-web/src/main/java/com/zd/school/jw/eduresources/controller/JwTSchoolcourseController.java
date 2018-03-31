
package com.zd.school.jw.eduresources.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.service.JwTSchoolcourseService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwTSchoolcourseController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 校本课程实体Controller. date: 2016-03-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/schoolcourse")
public class JwTSchoolcourseController extends FrameWorkController<JwTSchoolcourse> implements Constant {

    @Resource
    JwTSchoolcourseService thisService; // service层接口

    //获取列表数据
    @RequestMapping(value = "/list", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getList(@ModelAttribute JwTSchoolcourse entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<JwTSchoolcourse> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param JwTSchoolcourse
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/doadd")
    public void doAdd(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();

        if (thisService.IsFieldExist("courseName", courseName, "-1")) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        JwTSchoolcourse saveEntity = new JwTSchoolcourse();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人
        // 持久化到数据库
        entity = thisService.doMerge(entity);

        //返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * doDelete @Title: doDelete @Description: TODO @param @param
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
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * JwTSchoolcourse @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();
        String courseId = entity.getUuid();

        if (thisService.IsFieldExist("courseName", courseName, courseId)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        JwTSchoolcourse perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
}
