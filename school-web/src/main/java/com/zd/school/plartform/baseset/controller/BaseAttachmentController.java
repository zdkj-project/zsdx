
package com.zd.school.plartform.baseset.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseTAttachmentController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 公共附件信息表实体Controller. date: 2016-07-06
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseAttachment")
public class BaseAttachmentController extends FrameWorkController<BaseAttachment> implements Constant {

    @Resource
    BaseAttachmentService thisService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute BaseAttachment entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        int start = super.start(request); // 起始记录数
        int limit = entity.getLimit();// 每页记录数
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<BaseAttachment> lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param BaseTAttachment
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(BaseAttachment entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 此处为放在入库前的一些检查的代码，如唯一校验等

        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 生成默认的orderindex
        // 如果界面有了排序号的输入，则不需要取默认的了
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);// 排序

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人

        // 持久化到数据库
        entity = thisService.merge(entity);

        // 返回实体到前端界面
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
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * doRestore还原删除的记录 @Title: doRestore @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * BaseTAttachment @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(BaseAttachment entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
        BaseAttachment perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setCreateUser(userCh); // 设置修改人的中文名
        entity = thisService.merge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    @RequestMapping("/getFileList")	//Filename	sendId
    public void getFileList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	String entityName=request.getParameter("entityName");	//补充此参数
    	
    	String setRecordId=request.getParameter("recordId");
    	String attachIsMain = request.getParameter("attachIsMain");
    	if(setRecordId==null){
    		writeJSON(response,"[]");
    		return;
    	}
   
    	String hql="from BaseAttachment b where b.recordId='"+setRecordId+"' ";
    	if (StringUtils.isNotEmpty(attachIsMain)){
    		hql += " and b.attachIsMain='" + attachIsMain + "' ";
    	}
    	if(StringUtils.isNotEmpty(entityName)){
    		hql += " and b.entityName='" + entityName + "' ";
    	}
    	
    	hql += " order by b.createTime asc";
    	List<BaseAttachment> list = thisService.doQuery(hql);
    	
    	List<HashMap<String, Object>> lists=new ArrayList<>();
    	HashMap<String, Object> maps=null;
    	for(BaseAttachment bt : list){
    		maps = new LinkedHashMap<>();
    		maps.put("id", "SWFUpload_" + bt.getUuid());
    		maps.put("name", bt.getAttachName());
    		maps.put("size", bt.getAttachSize());
    		maps.put("type", bt.getAttachType());
    		maps.put("status", 0);
    		maps.put("percent", 100);
    		maps.put("fileId", bt.getUuid());
    		maps.put("fileUrl", bt.getAttachUrl());
    		lists.add(maps);
    	}
    	writeJSON(response,jsonBuilder.toJson(lists));
    }
}
