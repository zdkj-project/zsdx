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
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/gradeinfo")
public class JwTGradeController extends FrameWorkController<JwTGrade> implements Constant {

    @Resource
    private JwTGradeService thisService;

    //获取数据
    @RequestMapping(value = "/getGradeList", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getGradeList(@ModelAttribute JwTGrade entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
        //        QueryResult<JwTGrade> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
        //                super.sort(request), super.filter(request), true);

        QueryResult<JwTGrade> qr = thisService.getGradeList(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    //修改年级信息
    @RequestMapping("/doupdate")
    public void doUpdate(JwTGrade entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String gradeCode = entity.getSectionCode() + entity.getNj();
        entity.setGradeCode(gradeCode);
        SysUser currentUser = getCurrentSysUser();

        JwTGrade perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(currentUser.getXm());

        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }
}
