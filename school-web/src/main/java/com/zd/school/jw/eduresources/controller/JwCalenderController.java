
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
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.eduresources.model.JwCalender ;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.dao.JwCalenderDao ;
import com.zd.school.jw.eduresources.service.JwCalenderService ;

/**
 * 
 * ClassName: JwCalenderController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体Controller.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwCalender")
public class JwCalenderController extends FrameWorkController<JwCalender> implements Constant {

    @Resource
    JwCalenderService thisService; // service层接口


    @RequestMapping("/getCanderTree")
    public void getDicTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	StringBuffer pageJson = null;
    	pageJson = new StringBuffer("[{\"text\":\"ROOT\",\"expanded\":true,\"children\"" + ":");
    	 
    	pageJson.append("[");
    	
		String hql="from JwTCander j where 1=1 and j.isDelete=0";
    	List<JwCalender> list = thisService.getQuery(hql);
		for(JwCalender jwTCander : list){
			pageJson.append("{");
			pageJson.append("\"text\":\""+jwTCander.getCanderName()+"\",");
			pageJson.append("\"uuid\":\""+jwTCander.getUuid()+"\",");
			pageJson.append("\"leaf\":true");
			pageJson.append("}");  		
			pageJson.append(",");
		}
		if (list.size() > 0) {
			pageJson.deleteCharAt(pageJson.length() - 1);
		}
    	pageJson.append("]");
    	pageJson.append("}");
		pageJson.append("]");

    	System.out.println(pageJson);
    	
    	writeJSON(response,  pageJson.toString());
    }
    
    /**
      * list查询
      * @Title: list
      * @Description: TODO
      * @param @param entity 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute JwCalender entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1 and isDelete=0 ");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1 and isDelete=0 ");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        if(orderSql.equals(""))
        	orderSql=" order by activityState desc,activityTime desc";
        	
        int start = super.start(request); // 起始记录数
        int limit = entity.getLimit();// 每页记录数
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<JwCalender> lists = thisService.getQuery(hql.toString(), start, limit);// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * 
      * doAdd
      * @Title: doAdd
      * @Description: TODO
      * @param @param JwTCander 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwCalender entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
		//此处为放在入库前的一些检查的代码，如唯一校验等
		
		//获取当前操作用户
		String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        
        JwCalender saveEntity = new JwCalender();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);
        
		// 生成默认的orderindex
		//如果界面有了排序号的输入，则不需要取默认的了
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		entity.setOrderIndex(orderIndex);//排序
		
		//增加时要设置创建人
        entity.setCreateUser(userCh); //创建人
        		
        entity.setActivityState(0);
        
		//持久化到数据库
		entity = thisService.doMerge(entity);
		
		//返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
      * doDelete
      * @Title: doDelete
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        /*
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
        */
        if(delIds==null){
        	writeJSON(response, jsonBuilder.returnFailureJson("'未选择需删除的信息'"));
        	return;
        }
        try{
	        String doIds = "'" + delIds.replace(",", "','") + "'";
	        String hql="DELETE FROM JwCalenderdetail j  WHERE j.canderId IN (" + doIds + ")";
	        thisService.doExecuteHql(hql);
	        
	        hql="DELETE FROM JwCalender j  WHERE j.uuid IN (" + doIds + ")";
	        int flag = thisService.doExecuteHql(hql);
	        
	        if (flag>0) {
	            writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
	        } else {
	            writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
	        }
        }catch(Exception e){
        	writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,请刷新重试！'"));
        }
    }

    /**
      * doRestore还原删除的记录
      * @Title: doRestore
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录
     * @Title: doUpdate
     * @Description: TODO
     * @param @param JwTCander
     * @param @param request
     * @param @param response
     * @param @throws IOException    设定参数
     * @return void    返回类型
     * @throws
    */
    @RequestMapping("/doupdate")
    public void doUpdates(JwCalender entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		
		//获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
			
			
        //先拿到已持久化的实体
		//entity.getSchoolId()要自己修改成对应的获取主键的方法
        JwCalender perEntity = thisService.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity,entity);
       
        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    /**
     * doUpdate编辑记录
     * @Title: doUpdate
     * @Description: TODO
     * @param @param JwTCander
     * @param @param request
     * @param @param response
     * @param @throws IOException    设定参数
     * @return void    返回类型
     * @throws
    */
    @RequestMapping("/doUpdateState")
    public void doUpdateState(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		String calenderId=request.getParameter("uuid");
		
		int statu=thisService.updateStatu(calenderId);
		if(statu==1){
			writeJSON(response, jsonBuilder.returnSuccessJson("\'启用成功！\'"));
		}else{
			writeJSON(response, jsonBuilder.returnFailureJson("\'请求失败！\'"));
		}
		
    }
}
