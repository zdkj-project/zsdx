package com.zd.school.control.device.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zd.core.annotation.Auth;
import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.control.device.model.MjUserright;
import com.zd.school.control.device.service.MjUserrightService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.ViewUserRoom;

/**
 * 门禁权限
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/BaseMjUserright")
public class BaseMjUserrightController extends FrameWorkController<MjUserright> implements Constant {

	@Resource
	MjUserrightService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成树

	/**
	 * 门禁权限列表
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute MjUserright entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据

		QueryResult<MjUserright> qr = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据

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
		List<CommTree> lists = treeService.getCommTree("JW_AREAROOMINFOTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 生成树 获取分配了学生宿舍的班级房间数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/treeStuDormList")
	public void getStuDormList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_V_STU_DORMALLOTTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param MjUserright
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doAdd")
	public void doAdd(MjUserright entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
		if (ModelUtil.isNotNull(entity))
			writeJSON(response, jsonBuilder.returnSuccessJson("\"数据增加成功\""));
		else
			writeJSON(response, jsonBuilder.returnFailureJson("\"数据增加失败,详情见错误日志\""));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@Auth("USERACCESS_delete")
	@RequestMapping("/dodelete")
	public void dodelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("ids");
		if (StringUtils.isEmpty(ids)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			boolean flag = thisService.doLogicDelOrRestore(ids, StatuVeriable.ISDELETE, currentUser.getXm());
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"删除成功\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"删除失败\""));
			}
		}
	}

	/**
	 * 给设备增加用户权限
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Auth("USERACCESS_add")
	@RequestMapping("/doAddMj")
	public void doAddMj(@RequestParam("userId") String userId, @RequestParam("termId") String termId,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		thisService.doAddMj(userId, termId, currentUser);// 执行增加方法

		writeJSON(response, jsonBuilder.returnSuccessJson("\"增加权限成功\""));
	}

	/**
	 * 按人员查看权限列表
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/mjrightlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void mjrightlist(@ModelAttribute MjUserright entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String strData = ""; // 返回给js的数据	
		String userId = request.getParameter("userId");
		if(StringUtils.isEmpty(userId)){
			strData = jsonBuilder.buildObjListToJson(0L, new ArrayList<>(), true);// 处理数据
			writeJSON(response, strData);// 返回数据
			return;
		}
		
		String roomName = request.getParameter("roomName");	
		String querySql ="";
		if(StringUtils.isNotEmpty(userId)){
			querySql +=" and o.USER_ID='"+userId+"'";
		}
		if(StringUtils.isNotEmpty(roomName)){
			querySql +=" and o.ROOM_NAME like '%"+roomName+"%'";
		}
		
		String sort=super.sort(request);	
		String orderSql = StringUtils.convertSortToSql(sort);
		if(StringUtils.isNotEmpty(orderSql)){
			orderSql=" order by "+orderSql;
		}
		
		
		String sql = " select  XM as XM,ROOM_NAME as ROOM_NAME,ROOM_TYPE as ROOM_TYPE  from PT_V_USERROOM as o where 1=1 ";
		sql = sql + querySql + orderSql;
		
		String countSql="select count(*) from PT_V_USERROOM as o where 1=1";
		countSql=countSql + querySql ;
					
		QueryResult<ViewUserRoom> qResult = thisService.queryPageResultBySql(sql, super.start(request), super.limit(request), 
				ViewUserRoom.class, countSql);

		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
		
		/*
		 * 以门禁记录表为主的方式
		String strData = ""; // 返回给js的数据
		String filter = request.getParameter("filter");
		String userId = request.getParameter("userId");
		
		if (StringUtils.isNotEmpty(filter)) {
			filter = filter.substring(0, filter.length() - 1);
			filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + userId
					+ "\",\"field\":\"stuId\"}" + "]";
		} else {
			filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + userId
					+ "\",\"field\":\"stuId\"}]";
		}
		
		QueryResult<MjUserright> qr = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), filter, true);
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据

		writeJSON(response, strData);// 返回数据
		*/
	}

	/**
	 * 获取房间有权限的用户设备列表（按房间查看权限列表）
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/roomUserRightList" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void roomUserRightList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*
		String strData = ""; // 返回给js的数据
		
		String filter = request.getParameter("filter");
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		
		List<String> termList=new ArrayList<>();
		if (StringUtils.isNotEmpty(roomId) && !AdminType.ADMIN_ORG_ID.equals(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时		
				termList=getPtTermIds(roomId);		
				
			} else {					// 当选择的区域不为房间时			
				List<String> roomList = getRoomIds(roomId);						
				if(!roomList.isEmpty()){				
					String areaIds = roomList.stream().collect(Collectors.joining("','"));
					termList=getPtTermIds(areaIds);
					
				}else{	// 若区域之下没有房间，则直接返回空数据				
					strData = jsonBuilder.buildObjListToJson(0L,new ArrayList<>(), true);// 处理数据
					writeJSON(response, strData);// 返回数据
					return;
				}				
			}
		}
		
		if(!termList.isEmpty()){
			String termIds=termList.stream().collect(Collectors.joining(","));	
			if (StringUtils.isNotEmpty(filter)) {
				filter = filter.substring(0, filter.length() - 1);
				filter += ",{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + termIds
						+ "\",\"field\":\"termId\"}" + "]";
			} else {
				filter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + termIds
						+ "\",\"field\":\"termId\"}]";
			}
		}else{
			strData = jsonBuilder.buildObjListToJson(0L,new ArrayList<>(), true);// 处理数据
			writeJSON(response, strData);// 返回数据
			return;
		}
		
		
		QueryResult<MjUserright> qResult = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
		*/
		
		
		String strData = ""; // 返回给js的数据
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		
		String xm = request.getParameter("xm");

		String querySql ="";
		if(StringUtils.isNotEmpty(xm)){
			querySql +=" and o.XM like '%"+xm+"%'";
		}
		
		String sort=super.sort(request);	
		String orderSql = StringUtils.convertSortToSql(sort);
		if(StringUtils.isNotEmpty(orderSql)){
			orderSql=" order by "+orderSql;
		}
		
		String sql = " select o.XM as XM,o.ROOM_CODE as ROOM_CODE,"
				+ " o.ROOM_NAME as ROOM_NAME,o.ROOM_TYPE as ROOM_TYPE"
				+ " from PT_V_USERROOM AS o where 1=1 ";
		String countSql = "select count(*) from PT_V_USERROOM as o where 1=1  ";
		
		
		if (StringUtils.isNotEmpty(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时
				querySql += " and ROOM_ID ='" + roomId + "'";
			} else { // 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId);

				if (!roomList.isEmpty()) {
					String roomIds = roomList.stream().collect(Collectors.joining("','", "'", "'"));
					querySql += " and ROOM_ID in (" + roomIds + ")";

				} else { // 若区域之下没有房间，则直接返回空数据

					strData = jsonBuilder.buildObjListToJson(0L, new ArrayList<>(), true);// 处理数据
					writeJSON(response, strData);// 返回数据
					return;
				}
			}
		}
		sql = sql + querySql  + orderSql;
		countSql = countSql + querySql;

		QueryResult<ViewUserRoom> qResult = thisService.queryPageResultBySql(sql, super.start(request),
				super.limit(request), ViewUserRoom.class, countSql);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
		
	}

	/**
	 * 获取某个区域下的所有房间数据
	 * 
	 * @param roomId
	 * @param roomLeaf
	 * @return
	 */
	private List<String> getRoomIds(String areaId) {
		List<String> result = new ArrayList<>();

		// 当选择的区域不为房间时
		String hql = "select a.uuid from BuildRoomarea a where a.isDelete=0  and a.areaType='04' and a.treeIds like '%"
				+ areaId + "%'";
		List<String> lists = thisService.queryEntityByHql(hql);
		if (lists.size() > 0) {
			String areaIds = lists.stream().collect(Collectors.joining("','", "'", "'"));
			hql = "select a.uuid from BuildRoominfo a where a.isDelete=0  and a.roomType!='0' and a.areaId in (" + areaIds + ")";
			result = thisService.queryEntityByHql(hql);
		}

		return result;
	}
	
	private List<String> getPtTermIds(String roomIds){
		List<String> result = new ArrayList<>();

		String sql = "select b.TERM_ID from BUILD_T_ROOMINFO a join PT_TERM b "
				+ "	on a.ROOM_ID=b.ROOM_ID"
				+ " where a.ISDELETE=0 and a.ROOM_TYPE!=0 and b.ISDELETE=0 "
				+ " and a.ROOM_ID in ('" + roomIds + "')";
		result = thisService.queryEntityBySql(sql,null);
		
		return result;
	}

}
