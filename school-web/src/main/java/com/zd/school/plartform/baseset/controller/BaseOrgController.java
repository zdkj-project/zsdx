
package com.zd.school.plartform.baseset.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DBContextHolder;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.model.BaseOrgChkTree;
import com.zd.school.plartform.baseset.model.BaseOrgToUP;
import com.zd.school.plartform.baseset.model.BaseOrgTree;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;

/**
 * 
 * ClassName: BaseOrgController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: BASE_T_ORG实体Controller. date: 2016-07-26
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseOrg")
public class BaseOrgController extends FrameWorkController<BaseOrg> implements Constant {

	@Resource
	private BaseOrgService thisService; // service层接口

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
	@RequestMapping("/treelist")
	public void getOrgTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		String orderSql = request.getParameter("orderSql");
        String excludes = super.excludes(request);

		SysUser currentUser = getCurrentSysUser();
		List<BaseOrgTree> lists = thisService.getOrgTreeList(whereSql, orderSql, currentUser);

		strData = JsonBuilder.getInstance().buildList(lists, excludes);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping("/chktreelist")
	public void getOrgChkTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		String orderSql = request.getParameter("orderSql");
		String excludes = super.excludes(request);

		SysUser currentUser = getCurrentSysUser();
		List<BaseOrgChkTree> lists = thisService.getOrgChkTreeList(whereSql, orderSql, currentUser);

		strData = JsonBuilder.getInstance().buildList(lists, excludes);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

    /**
     *
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	@RequestMapping("/doadd")
	public void doAdd(BaseOrg entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String parentNode = entity.getParentNode();
		String parentName = entity.getParentName();
		String nodeText = entity.getNodeText();
		Integer orderIndex = entity.getOrderIndex();

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
		if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), "-1", hql1)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
			return;
		}
		if (thisService.IsFieldExist("nodeText", nodeText, "-1", hql1)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此部门！'"));
			return;
		}
		SysUser sysuser = getCurrentSysUser();
		entity = thisService.addOrg(entity, sysuser);
		// 返回的是实体前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	/**
	 * 响应删除的事件 把id传到controller里面，然后查询子节点所对应的根节点id，如果！=null，那么他就有子集。
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptId = request.getParameter("ids");
		if (StringUtils.isEmpty(deptId)) {
			writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'没有传入删除主键'"));
			return;
		}
		SysUser currentUser = getCurrentSysUser();
		boolean flag = thisService.delOrg(deptId, currentUser);
		if (flag) {
			writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'删除成功'"));
		} else {
			writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'删除失败'"));
		}
	}

	/**
	 * 
	 * doUpdates:修改部门信息.
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
	@RequestMapping("/doupdate")
	public void doUpdates(BaseOrg entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		String parentNode = entity.getParentNode();
		String parentName = entity.getParentName();
		String nodeText = entity.getNodeText();
		String uuid = entity.getUuid();
		Integer orderIndex = entity.getOrderIndex();

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
		if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), uuid, hql1)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
			return;
		}
		if (thisService.IsFieldExist("nodeText", nodeText, uuid, hql1)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此部门！'"));
			return;
		}

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		//
		// 先拿到已持久化的实体
		BaseOrg perEntity = thisService.get(uuid);
		Boolean isLeaf = perEntity.getLeaf();
		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		perEntity.setLeaf(isLeaf);

		entity = thisService.merge(perEntity);// 执行修改方法

		entity.setParentName(parentName);
		entity.setParentNode(parentNode);

		// 更新父节点的是否叶节点的标记
		BaseOrg parentOrg = thisService.get(parentNode);
		parentOrg.setUpdateTime(new Date()); // 设置修改时间
		parentOrg.setUpdateUser(userCh); // 设置修改人的中文名
		parentOrg.setLeaf(false);
		thisService.merge(parentOrg);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
	}
	
	/*
     * 单条数据调用同步UP的方式
     * */
    @RequestMapping("/doSyncDeptInfoToUp/{smallDeptId}")
	public void doSyncDeptInfoToUp(@PathVariable("smallDeptId") String smallDeptId,HttpServletRequest request, HttpServletResponse response) throws IOException {
    	StringBuffer returnJson = null;
    	try{
    		
    		//String smallDeptId="12";
    	
    		// 1.查询这个smallDeptId的最新的部门信息
			String sql = "select EXT_FIELD04 as departmentId,EXT_FIELD05 as parentDepartmentId,"
					+ "	NODE_TEXT as departmentName,convert(varchar,NODE_LEVEL) as layer,"
					+ " convert(varchar,ORDER_INDEX) as layerorder  "
					+ " from BASE_T_ORG"
					+ " where isdelete=0 and EXT_FIELD04='"+smallDeptId+"'"
					+ " order by DepartmentID asc";
			
			List<BaseOrgToUP> deptInfo = thisService.doQuerySqlObject(sql, BaseOrgToUP.class);
			
			//2.进入事物之前切换数据源		
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
			int row = 0;
			if(deptInfo.size()!=0){			
				row = thisService.syncDeptInfoToUP(deptInfo.get(0),smallDeptId);
			}else{			
				row = thisService.syncDeptInfoToUP(null, smallDeptId);			
			}
    		
			if(row==0){
				returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"未有部门数据需要同步！\"}");
			}else if(row>0){
				returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"同步部门数据成功！\"}");
			}else{
				returnJson = new StringBuffer("{ \"succes\" : false, \"msg\":\"同步部门数据到UP失败，请联系管理员！\"}");
			}
				
	    } catch (Exception e) {
			returnJson = new StringBuffer("{ \"succes\" : false, \"msg\":\"同步部门数据到UP失败，请联系管理员！\"}");
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}
	
		writeAppJSON(response, returnJson.toString());
    }
    
    /*
     * 所有部门数据调用同步UP的方式
     * */
    @RequestMapping("/doSyncAllDeptInfoToUp")
	public void doSyncAllDeptInfoToUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	StringBuffer returnJson = null;
    	try{
    		
    		//String smallDeptId="12";
    	
    		// 1.查询这个smallDeptId的最新的部门信息
			String sql = "select EXT_FIELD04 as departmentId,EXT_FIELD05 as parentDepartmentId,"
					+ "	NODE_TEXT as departmentName,convert(varchar,NODE_LEVEL) as layer,"
					+ " convert(varchar,ORDER_INDEX) as layerorder  "
					+ " from BASE_T_ORG"
					+ " where isdelete=0"
					+ " order by DepartmentID asc";
			
			List<BaseOrgToUP> deptInfo = thisService.doQuerySqlObject(sql, BaseOrgToUP.class);
			
			//2.进入事物之前切换数据源		
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
			int row = 0;
			if(deptInfo.size()!=0){			
				row = thisService.syncAllDeptInfoToUP(deptInfo);
			}
    		
			if(row==0){
				returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"未有部门数据需要同步！\"}");
			}else if(row>0){
				returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"同步部门数据成功！\"}");
			}else{
				returnJson = new StringBuffer("{ \"succes\" : false, \"msg\":\"同步部门数据到UP失败，请联系管理员！\"}");
			}
				
	    } catch (Exception e) {
			returnJson = new StringBuffer("{ \"succes\" : false, \"msg\":\"同步部门数据到UP失败，请联系管理员！\"}");
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}
	
		writeAppJSON(response, returnJson.toString());
    }
}
