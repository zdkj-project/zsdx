package com.zd.school.jw.eduresources.controller;

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

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.model.XcSalaryitem;

@Controller
@RequestMapping("/gradeclass")
public class JwTGradeClassController extends FrameWorkController<JwTGradeclass> implements Constant {

    @Resource
    private JwTGradeclassService thisService;

    @Resource
    private JwTGradeService gradeService;
    
    @Resource
    private CommTreeService treeSerice;

    //获取数据
    @RequestMapping(value = "/list", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getList(@ModelAttribute JwTGradeclass entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
        QueryResult<JwTGradeclass> qr = thisService.getGradeClassList(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    //获取数据（用于教室分配）
    @RequestMapping(value = "/classRoomAllot", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void classRoomAllot(@ModelAttribute JwTGradeclass entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<JwTGradeclass> lists = thisService.doQuery(hql.toString(), super.start(request), entity.getLimit());// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    //修改班级数据
    @RequestMapping("/doupdate")
    public void doUpdate(JwTGradeclass entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        SysUser currentUser = getCurrentSysUser();
	
        JwTGradeclass perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyProperties(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(currentUser.getXm());

        entity = thisService.merge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }

    @RequestMapping("/doadd")
    public void doAdd(JwTGradeclass entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        //String courseName = entity.getCourseName();
        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        Integer orderIndex = entity.getOrderIndex() + 1;
        String sName = "";
        String gradeId = entity.getGraiId();
        JwTGrade grade = gradeService.get(gradeId);
        switch (grade.getGradeCode()) {
        case "31":
            sName = "初一";
            break;
        case "32":
            sName = "初二";
            break;
        case "33":
            sName = "初三";
            break;
        case "41":
            sName = "高一";
            break;
        case "42":
            sName = "高二";
            break;
        case "43":
            sName = "高三";
            break;
        default:
            break;
        }
        for (int i = 1; i < orderIndex; i++) {
            String className = sName + "（" + i + "）班";
            String[] propName = new String[] { "className", "isDelete" };
            Object[] propValue = new Object[] { "className", "0" };
            JwTGradeclass isClass = thisService.getByProerties("className", className);
            if (!ModelUtil.isNotNull(isClass)) {
                JwTGradeclass gradeclass = new JwTGradeclass();
                BeanUtils.copyPropertiesExceptNull(entity, gradeclass);
                entity.setOrderIndex(i);
                entity.setClassName(sName + "（" + i + "）班");
                entity.setCreateUser(userCh);
                thisService.merge(entity);
            }
        }

        writeJSON(response, jsonBuilder.returnSuccessJson("'创建班级成功'"));
    }
    
    @RequestMapping("/getClassTree")
    public void getClassTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        SysUser currentUser = getCurrentSysUser();

        List<CommTree> lists = treeSerice.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        //List<CommTree> lists = thisService.getGradeClassTree("JW_V_GRADECLASSTREE", whereSql, currentUser);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    //获取数据
    @RequestMapping(value = "/classmottolist", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getClassmottoList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String claiId = request.getParameter("claiId");
        String claiLevel = request.getParameter("claiLevel");
        SysUser currentUser = getCurrentSysUser();
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<JwTGradeclass> qResult = thisService.getGradeClassList(start, limit, sort, filter,true,currentUser,claiId,claiLevel);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
}