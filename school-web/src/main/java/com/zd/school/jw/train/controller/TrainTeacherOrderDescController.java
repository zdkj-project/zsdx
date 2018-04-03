
package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.train.model.TrainTeacherOrderDesc;
import com.zd.school.jw.train.service.TrainTeacherOrderDescService;
import com.zd.school.plartform.system.model.SysUser;


@Controller
@RequestMapping("/TrainTeacherOrderDesc")
public class TrainTeacherOrderDescController extends FrameWorkController<TrainTeacherOrderDesc> implements Constant {

	private static Logger logger = Logger.getLogger(TrainTeacherOrderDescController.class);

    @Resource
    TrainTeacherOrderDescService thisService; // service层接口

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param BizTJob
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/getOrderDesc")
    public void getOrderDesc(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

    	
        //当前节点
    	String hql="from TrainTeacherOrderDesc order by updateTime desc";
    	QueryResult<TrainTeacherOrderDesc>  result = thisService.getQueryResult(hql, 0, 1);
    	
        if(result.getTotalCount()>0)
            writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(result.getResultList().get(0))));
        else
            writeJSON(response, jsonBuilder.returnFailureJson("\"暂无数据\""));
    	
        
    }
    

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param BizTJob
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/doAdd")
    public void doAddDesc(TrainTeacherOrderDesc entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //当前节点
        TrainTeacherOrderDesc saveEntity = new TrainTeacherOrderDesc();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人
        // 持久化到数据库
        entity = thisService.doMerge(entity);

        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }
    
    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * BizTJob @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doUpdate")
    public void doUpdateDesc(TrainTeacherOrderDesc entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
        TrainTeacherOrderDesc perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        entity = thisService.doMerge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
}
