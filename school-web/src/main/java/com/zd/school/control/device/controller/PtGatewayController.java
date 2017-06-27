package com.zd.school.control.device.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.core.util.TLVUtils;
import com.zd.school.control.device.model.PtGateway;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.service.PtGatewayService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 网关表
 * @author hucy
 *
 */
@Controller
@RequestMapping("/PtGateway")
public class PtGatewayController extends FrameWorkController<PtGateway> implements Constant  {

	@Resource
	PtGatewayService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成树
	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute PtGateway entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
		// 总记录数
		StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
		String whereSql = entity.getWhereSql();// 查询条件
		String querySql = entity.getQuerySql();// 查询条件
		hql.append(whereSql);
		hql.append(querySql);
		countHql.append(whereSql);
		countHql.append(querySql);
		List<PtGateway> lists = thisService.doQuery(hql.toString(), super.start(request), entity.getLimit());// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 生成树
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/treelist")
	public void getGradeTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_V_SYS_FRONTSERVER", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}
	
	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids=request.getParameter("ids");
		if (StringUtils.isEmpty(ids)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.logicDelOrRestore(ids, StatuVeriable.ISDELETE);
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
	 * 批量设置前置
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdateBatch")
	public void doupdateBatch(PtGateway entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String uuid[] =entity.getUuid().split(",");
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		PtGateway ptGateway=null;
		for (int i = 0; i < uuid.length; i++) {
			ptGateway=thisService.get(uuid[i]);
			ptGateway.setFrontserverId(entity.getFrontserverId());
			ptGateway.setUpdateUser(userCh);
			ptGateway.setUpdateTime(new Date());
			thisService.merge(ptGateway);
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));
	}
	
	/**
	 * 修改
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(PtGateway entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		PtGateway perEntity = thisService.get(entity.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);
		perEntity.setUpdateUser(userCh);

		entity = thisService.merge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}
	
	/**
	 * 批量设置高级参数
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/batchHighParam")
	public void batchHighParam(TLVModel tlvs,HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		byte[] result=TLVUtils.encode(tlvs.getTlvs());
		List<PtGateway> list=thisService.doQueryAll();
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		for(PtGateway gateway:list){
			gateway.setUpdateUser(userCh);
			gateway.setUpdateTime(new Date());
			gateway.setAdvParam(result);
			thisService.merge(gateway);
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("'高级参数批量设置成功。'"));

	}
	
	/**
	 * 基础参数批量设置
	 * @param tlvs
	 * @param termTypeID
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/batchBaseParam")
	public void batchBaseParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
			byte[] result=TLVUtils.encode(tlvs.getTlvs());
			List<PtGateway> list=thisService.doQueryAll();
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();
			for(PtGateway gateway:list){
				gateway.setUpdateUser(userCh);
				gateway.setUpdateTime(new Date());
				gateway.setBaseParam(result);
				thisService.merge(gateway);
			}
			writeJSON(response, jsonBuilder.returnSuccessJson("'基础参数批量设置成功。'"));
	}
	
	
	
	@RequestMapping("/baseParam")
	public void baseParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		perEntity.setUpdateUser(userCh);
		perEntity.setUpdateTime(new Date());
		byte[] result =null;
		result=TLVUtils.encode( tlvs.getTlvs());
		perEntity.setBaseParam(result);

		thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'基础参数设置成功。'"));

	}
	
	@RequestMapping("/baseParam_read")
	public void baseParam_read(TLVModel tlvs, HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		String strData ="";
		if(perEntity.getBaseParam()!=null){
			TLVUtils.decode(perEntity.getBaseParam(), tlvs.getTlvs());
			strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
		}
		writeJSON(response, strData);// 返回数据
	}
	/**
	 * 设置单个高级参数
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/highParam")
	public void highParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		byte[] result = null;
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		SysUser currentUser = getCurrentSysUser();
		String userCh = "超级管理员";
		if (currentUser != null)
			userCh = currentUser.getXm();
		perEntity.setUpdateUser(userCh);
		perEntity.setUpdateTime(new Date());
		result=TLVUtils.encode(tlvs.getTlvs());
		perEntity.setAdvParam(result);
		thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'高级参数设置成功。'"));

	}
	@RequestMapping("/highParam_read")
	public void highParam_read(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		String strData ="";
		if(perEntity.getAdvParam()!=null){
			TLVUtils.decode(perEntity.getAdvParam(), tlvs.getTlvs());
			strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
		}
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping("/batchGatewayParam")
	public void batchGatewayParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
			byte[] result=TLVUtils.encode(tlvs.getTlvs());
			List<PtGateway> list=thisService.doQueryAll();
			SysUser currentUser = getCurrentSysUser();
			String userCh = "超级管理员";
			if (currentUser != null)
				userCh = currentUser.getXm();
			for(PtGateway gateway:list){
				gateway.setUpdateUser(userCh);
				gateway.setUpdateTime(new Date());
				gateway.setNetParam(result);
				thisService.merge(gateway);// 执行修改方法
			}
			writeJSON(response, jsonBuilder.returnSuccessJson("'网关参数批量设置成功。'"));
	}
	
	@RequestMapping("/gatewayParam")
	public void gatewayParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		byte[] result = null;
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		result=TLVUtils.encode(tlvs.getTlvs());
		perEntity.setNetParam(result);
		SysUser currentUser = getCurrentSysUser();
		String userCh = "超级管理员";
		if (currentUser != null)
			userCh = currentUser.getXm();
		perEntity.setUpdateUser(userCh);
		perEntity.setUpdateTime(new Date());
		thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'网关参数设置成功。'"));

	}
	@RequestMapping("/gatewayParam_read")
	public void gatewayParam_read(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		PtGateway perEntity = thisService.get(tlvs.getUuid());
		String strData ="";
		if(perEntity.getAdvParam()!=null){
			TLVUtils.decode(perEntity.getNetParam(), tlvs.getTlvs());
			strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
		}
		writeJSON(response, strData);// 返回数据
	}
}
