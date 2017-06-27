
package com.zd.school.jw.schoolcourse.controller;

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
import com.zd.school.jw.schoolcourse.model.JwCoursepublish;
import com.zd.school.jw.schoolcourse.service.JwCoursepublishService;
import com.zd.school.jw.schoolcourse.service.JwPublishcourseService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwCoursepublishController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 校本课程发布信息(JW_T_COURSEPUBLISH)实体Controller.
 * date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwCoursepublish")
public class JwCoursepublishController extends FrameWorkController<JwCoursepublish> implements Constant {

    @Resource
    private JwCoursepublishService thisService; // service层接口

    @Resource
    private JwPublishcourseService pcourseService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute JwCoursepublish entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
        String whereSql = super.whereSql(request);
        String orderSql = super.orderSql(request);
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        QueryResult<JwCoursepublish> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql,
                currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param JwCoursepublish
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwCoursepublish entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 此处为放在入库前的一些检查的代码，如唯一校验等

        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        JwCoursepublish perEntity = new JwCoursepublish();
        BeanUtils.copyPropertiesExceptNull(entity, perEntity);
        // 生成默认的orderindex
        // 如果界面有了排序号的输入，则不需要取默认的了
        // Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        // entity.setOrderIndex(orderIndex);//排序

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人

        // 持久化到数据库
        entity = thisService.merge(entity);

        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
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
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * JwCoursepublish @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(JwCoursepublish entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
        JwCoursepublish perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        entity = thisService.merge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

    @RequestMapping("/deleteCourse")
    public void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = pcourseService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

}
