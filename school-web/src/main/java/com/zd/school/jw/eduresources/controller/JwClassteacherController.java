
package com.zd.school.jw.eduresources.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwClassteacherController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 班主任信息实体Controller. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwClassteacher")
public class JwClassteacherController extends FrameWorkController<JwClassteacher> implements Constant {

    @Resource
    JwClassteacherService thisService; // service层接口

    @Resource
    private CommTreeService treeSerice;

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute JwClassteacher entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        //        String whereSql = request.getParameter("whereSql");
        //        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
        //        // 总记录数
        //        StringBuffer countHql = new StringBuffer(
        //                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
        //        if (StringUtils.isNotEmpty(whereSql)) {
        //            hql.append(whereSql);
        //            countHql.append(whereSql);
        //        }
        //
        //        List<JwClassteacher> lists = thisService.doQuery(hql.toString(), super.start(request), super.limit(request));// 执行查询方法
        //        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        //        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据   
        SysUser currentUser = getCurrentSysUser();
        String claiId = request.getParameter("claiId");
        QueryResult<JwClassteacher> qr = thisService.getclassTeacher(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true, claiId, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param JwClassteacher
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwClassteacher entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String claiId = entity.getClaiId(); //年级　
        String tteacId = entity.getTteacId(); //教师
        Integer studyYeah = entity.getStudyYeah(); //学年
        String semester = entity.getSemester(); //学期　
        Integer category = entity.getCategory(); //身份

        //此处为放在入库前的一些检查的代码，如唯一校验等
        //同一老师不能重复设置
        String hql = " o.isDelete='0' and o.tteacId='" + tteacId + "'";
        if (thisService.IsFieldExist("studyYeah", studyYeah.toString(), "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'此教师已是班主任！'"));
            return;
        }
        //同一学年、学期、年级的正年级组长只能有一个
        if (category == 0) {
            hql = " o.isDelete='0' and claiId='" + claiId + "' and o.semester='" + semester + "' and category='"
                    + category + "'";
            if (thisService.IsFieldExist("studyYeah", studyYeah.toString(), "-1", hql)) {
                writeJSON(response, jsonBuilder.returnFailureJson("'正班主任只能有一个！'"));
                return;
            }
        }

        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        /*
         * if (currentUser != null) userCh = currentUser.getXm();
         * 
         * JwClassteacher perEntity = new JwClassteacher();
         * BeanUtils.copyPropertiesExceptNull(entity, perEntity); //
         * 生成默认的orderindex //如果界面有了排序号的输入，则不需要取默认的了 Integer orderIndex =
         * thisService.getDefaultOrderIndex(entity);
         * entity.setOrderIndex(orderIndex);//排序
         * 
         * //增加时要设置创建人 entity.setCreateUser(userCh); //创建人
         */
        //持久化到数据库
        entity = thisService.doAddClassTeacher(entity, currentUser);

        //返回实体到前端界面
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
        SysUser currentUser = getCurrentSysUser();
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doDelete(delIds, currentUser);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    @RequestMapping("/classtreelist")
    public void getClassTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        SysUser currentUser = getCurrentSysUser();

        //List<CommTree> lists = treeSerice.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        List<CommTree> lists = thisService.getGradeClassTree("JW_V_GRADECLASSTREE", whereSql, currentUser);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    @RequestMapping("/allclasstreelist")
    public void getAllClassTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        SysUser currentUser = getCurrentSysUser();

        List<CommTree> lists = treeSerice.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        //List<CommTree> lists = thisService.getGradeClassTree("JW_V_GRADECLASSTREE", whereSql, currentUser);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }
}
