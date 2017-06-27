
package com.zd.school.oa.doc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.zd.core.util.StringUtils;
import com.zd.school.oa.doc.model.DocReceive;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.service.DocReceiveService;
import com.zd.school.oa.doc.service.DocRecexaminesService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: DocRecexaminesController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公文收文批阅人实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/DocRecexamines")
public class DocRecexaminesController extends FrameWorkController<DocRecexamines> implements Constant {

    @Resource
    DocRecexaminesService thisService; // service层接口

    @Resource
    DocReceiveService docReceiveService;

    /**
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *             list查询 @Title: list @Description: TODO @param @param entity
     *             实体类 @param @param request @param @param
     *             response @param @throws IOException 设定参数 @return void
     *             返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute DocRecexamines entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<DocRecexamines> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /***
     * 
     * chuanyuelist:传阅公文列表
     *
     * @author luoyibo
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping(value = { "/chuanyuelist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void chuanyuelist(@ModelAttribute DocRecexamines entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<DocRecexamines> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 设置公文已批阅 doUpdates.
     *
     * @author luoyibo
     * @param @param
     *            entity
     * @param @param
     *            request
     * @param @param
     *            response
     * @param @throws
     *            IOException
     * @param @throws
     *            IllegalAccessException
     * @param @throws
     *            InvocationTargetException
     * @return void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/doupdate")
    public void doUpdates(DocRecexamines entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
        String distribType = request.getParameter("distribType");
        String distribId = request.getParameter("distribId");
        String progress=request.getParameter("progress");
        entity.setProgress(Integer.parseInt(progress));
        
        // 获取当前的操作用户
        SysUser currentUser = getCurrentSysUser();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
//        DocRecexamines perEntity = thisService.get(entity.getUuid());
//
//        // 将entity中不为空的字段动态加入到perEntity中去。
//        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
//
//        perEntity.setUpdateTime(new Date()); // 设置修改时间
//        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
//        perEntity.setState("1");
        entity = thisService.setIsPiYue(entity, distribType, distribId, currentUser);// 执行修改方法
//
        
        DocReceive dr=new DocReceive();
        dr.setProgress(Integer.parseInt(progress));
        String conditionName="uuid";
        String conditionValue=entity.getDocrecId();
        String propertyName="progress";
        int propertyValue=Integer.parseInt(progress);
        docReceiveService.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);
//        String docrecId = entity.getDocrecId();
//        String hql = "select count(*) from DocRecexamines where docrecId='" + docrecId
//                + "' and recexamType=0 and state=0";
//        Integer count = thisService.getCount(hql);
//        if (count == 0) {
//            String hql2 = "update DocReceive set docrecState=3 where uuid='" + docrecId + "'";
//            thisService.executeHql(hql2);
//        } else {
//            String hql2 = "update DocReceive set docrecState=2 where uuid='" + docrecId + "'";
//            thisService.executeHql(hql2);
//        }

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * 
     * doDelete:设置公文批量批阅.
     * 
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @SuppressWarnings("restriction")
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入批阅公文主键'"));
            return;
        } else {
            String doIds = "'" + delIds.replace(",", "','") + "'";
            String hql = " UPDATE " + DocRecexamines.class.getSimpleName() + " SET state='1' WHERE uuid IN (" + doIds
                    + ")";
            Integer iExecute = thisService.executeHql(hql);

            String[] list = delIds.split(",");
            for (int i = 0; i < list.length; i++) {
                DocRecexamines docRecexamines = thisService.get(list[i]);
                String docrecId = docRecexamines.getDocrecId();
                String hql1 = "select count(*) from DocRecexamines where docrecId='" + docrecId
                        + "' and recexamType=0 and state=0";
                Integer count = thisService.getCount(hql1);
                if (count == 0) {
                    String hql2 = "update DocReceive set docrecState=3 where uuid='" + docrecId + "'";
                    thisService.executeHql(hql2);
                } else {
                    String hql2 = "update DocReceive set docrecState=2 where uuid='" + docrecId + "'";
                    thisService.executeHql(hql2);
                }
            }

            if (iExecute > 0) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'批量批阅成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'批量批阅失败'"));
            }
        }
    }

    /**
     * 
     * isRead:设置公文已阅读.
     *
     * @author luoyibo
     * @param @param
     *            entity
     * @param @param
     *            request
     * @param @param
     *            response
     * @param @throws
     *            IOException
     * @param @throws
     *            IllegalAccessException
     * @param @throws
     *            InvocationTargetException
     * @return void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/isread")
    public void isRead(DocRecexamines entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        entity.setProgress(1);
        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        DocRecexamines perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        perEntity.setState("1");
        entity = thisService.merge(perEntity);// 执行修改方法
        
        String conditionName="uuid";
        String conditionValue=entity.getDocrecId();
        String propertyName="progress";
        int propertyValue=1;
        docReceiveService.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);

        String docrecId = entity.getDocrecId();
        String hql = "select count(*) from DocRecexamines where docrecId='" + docrecId
                + "' and recexamType=1 and state=0 ";
        Integer count = thisService.getCount(hql);
        if (count == 0) {
            String hql2 = "update DocReceive set docrecState=5 where uuid='" + docrecId + "'";
            thisService.executeHql(hql2);
        }

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }
    
    //处理完后批阅更新进度情况的方法
    @RequestMapping("/ischeckupdate")
    public void ischeckupdate(DocRecexamines entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码
        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        DocRecexamines perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
        

        String conditionName="uuid";
        String conditionValue=entity.getDocrecId();
        String propertyName="progress";
        int propertyValue=entity.getProgress();
        docReceiveService.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);

        String conditionName1="uuid";
        String conditionValue1=entity.getDocrecId();
        String propertyName1="progress";
        int propertyValue1=entity.getProgress();
        thisService.updateByProperties(conditionName1, conditionValue1, propertyName1, propertyValue1);
        



        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }

}
