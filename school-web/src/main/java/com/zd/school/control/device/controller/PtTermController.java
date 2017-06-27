package com.zd.school.control.device.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
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
import com.zd.core.util.TagLenVal;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.service.PtTermService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 设备表
 * 
 * @author hucy
 *
 */
@Controller
@RequestMapping("/PtTerm")
public class PtTermController extends FrameWorkController<PtTerm> implements Constant {

	@Resource
	PtTermService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成树

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute PtTerm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
		hql.append(entity.getWhereSql());
		// 总记录数
		StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
		countHql.append(entity.getWhereSql());
		if (entity.getQuerySql() != null) {
			hql.append(entity.getQuerySql());
			countHql.append(entity.getQuerySql());
		}
		List<PtTerm> lists = thisService.doQuery(hql.toString(), super.start(request), entity.getLimit());// 执行查询方法
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
		List<CommTree> lists = treeService.getCommTree("JW_AREAROOMINFOTREE", " and 1=1");
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param MjUserright
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(String roomId, String uuid, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String roomIds[] = roomId.split(",");
		String uuids[] = uuid.split(",");
		PtTerm entity = null;
		for (int i = 0; i < uuids.length; i++) {
			entity = thisService.get(uuids[i]);
			entity.setRoomId(roomIds[i]);
			thisService.merge(entity);
		}
		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson("'成功。'"));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(uuid)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.logicDelOrRestore(uuid, StatuVeriable.ISDELETE);
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
	 * MjUserright @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(PtTerm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		PtTerm perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);
		perEntity.setCreateUser(userCh);
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
	public void batchHighParam(TLVModel tlvs,@RequestParam("termTypeID") String termTypeID, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		byte[] result=TLVUtils.encode(tlvs.getTlvs());
		thisService.updateByProperties("termTypeID",termTypeID ,new String[]{"advParam","updateTime"},new Object[]{ result,new Date()} );
		writeJSON(response, jsonBuilder.returnSuccessJson("'批量设置成功。'"));

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
	public void batchBaseParam(TLVModel tlvs,@RequestParam("termTypeID") String termTypeID, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
			byte[] result=TLVUtils.encode(tlvs.getTlvs());
			if(termTypeID.equals("11")||termTypeID.equals("17") ){//（红外控制器）（开关）
				String notes=request.getParameter("notes");
				thisService.updateByProperties( "termTypeID",termTypeID,new String[]{"baseParam","notes","updateTime"},new Object[]{ result,notes,new Date()});
			}else{
				thisService.updateByProperties( "termTypeID",termTypeID,new String[]{"baseParam","updateTime"},new Object[]{ result,new Date()} );
			}
			writeJSON(response, jsonBuilder.returnSuccessJson("'批量设置成功。'"));
	}
	
	/**
	 * 设置基础参数
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/baseParam")
	public void baseParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		perEntity.setUpdateUser(userCh);
		perEntity.setUpdateTime(new Date());
		byte[] result =null;
		result=TLVUtils.encode( tlvs.getTlvs());
		perEntity.setBaseParam(result);
		if(perEntity.getTermTypeID().equals("11")||perEntity.getTermTypeID().equals("17") ){
			String notes=request.getParameter("notes");
			perEntity.setNotes(notes);
		}
		thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'设置成功。'"));

	}
	
	@RequestMapping("/baseParam_read")
	public void baseParam_read(TLVModel tlvs, HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		String strData ="";
		if(perEntity.getBaseParam()!=null){
			TLVUtils.decode(perEntity.getBaseParam(), tlvs.getTlvs());
			if(perEntity.getTermTypeID().equals("11")||perEntity.getTermTypeID().equals("17") ){
				tlvs.setNotes(perEntity.getNotes());
				strData=JsonBuilder.getInstance().toJson(tlvs);
			}else{
				strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
			}
		}
		writeJSON(response, strData);// 返回数据
	}
	/**
	 * 设置单个高级参数
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
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		SysUser currentUser = getCurrentSysUser();
		result=TLVUtils.encode(tlvs.getTlvs());
		perEntity.setAdvParam(result);
		perEntity.setUpdateUser(currentUser.getXm());
		perEntity.setUpdateTime(new Date());
		thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}
	@RequestMapping("/highParam_read")
	public void highParam_read(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		String strData ="";
		if(perEntity.getAdvParam()!=null){
			TLVUtils.decode(perEntity.getAdvParam(), tlvs.getTlvs());
			strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
		}
		writeJSON(response, strData);// 返回数据
	}

	
}
