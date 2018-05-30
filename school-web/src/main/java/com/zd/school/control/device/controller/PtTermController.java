package com.zd.school.control.device.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.annotation.Auth;
import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.core.util.TLVUtils;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.model.TLVModel;
import com.zd.school.control.device.service.PtTermService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
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
	
	@Resource
	BaseDicitemService dicitemService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	//查询方法不用设置@Auth("PtRoombagStatus_look")
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute PtTerm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		String filter = request.getParameter("filter");
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
        if (StringUtils.isNotEmpty(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时
				if (StringUtils.isNotEmpty(filter)) {
					filter = filter.substring(0, filter.length() - 1);
					filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + roomId
							+ "\",\"field\":\"roomId\"}" + "]";
				} else {
					filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + roomId
							+ "\",\"field\":\"roomId\"}]";
				}
			} else {					// 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId);
					
				if(!roomList.isEmpty()){
					String roomIds=roomList.stream().collect(Collectors.joining(","));		
					if (StringUtils.isNotEmpty(filter)) {
						filter = filter.substring(0, filter.length() - 1);
						filter += ",{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + roomIds
								+ "\",\"field\":\"roomId\"}" + "]";
					} else {
						filter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + roomIds
								+ "\",\"field\":\"roomId\"}]";
					}
					
				}else{	// 若区域之下没有房间，则直接返回空数据
					
					strData = jsonBuilder.buildObjListToJson(0L,new ArrayList<>(), true);// 处理数据
					writeJSON(response, strData);// 返回数据
					return;
				}				
			}
		}
		
		QueryResult<PtTerm> qResult = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 获取未分配的设备
	 */
	@RequestMapping(value = { "/getNoAllotList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getNoAllotList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据

		String hql = "from PtTerm g where g.isDelete=0 and (g.roomId = '' or g.roomId is null) ";

		// QueryResult<PtTerm> qResult = thisService.queryResult(hql,
		// super.start(request), super.limit(request));
		QueryResult<PtTerm> qResult = thisService.queryCountToHql(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), hql, null, null);

		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 设置设备的房间绑定关系
	 * 
	 * @param roomId
	 * @param uuid
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Auth("DEVICEALLOT_add")
	@RequestMapping("/doSetPtTerm")
	public void doSetPtTerm(String roomId, String uuid, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		// String uuids[] = uuid.split(",");
		// String roomIds[] = roomId.split(",");
		// PtTerm entity = null;
		SysUser currentUser = getCurrentSysUser();
		thisService.doSetPtTerm(roomId, uuid, currentUser);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson("\"分配成功!\""));

	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param MjUserright
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doAdd")
	public void doAdd(String roomId, String uuid, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String uuids[] = uuid.split(",");
		String roomIds[] = roomId.split(",");
		PtTerm entity = null;
		for (int i = 0; i < uuids.length; i++) {
			entity = thisService.get(uuids[i]);
			entity.setRoomId(roomIds[i]);
			thisService.doMerge(entity);
			// thisService.updateByProperties("uuid", uuids[i], "roomId",
			// roomId);
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("\"分配成功!\""));
	}

	/**
	 * 修改设备数据
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Auth("DEVICEALLOT_update")
	@RequestMapping("/doupdate")
	public void doupdates(PtTerm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		SysUser currentUser = getCurrentSysUser();
		entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
		if (ModelUtil.isNotNull(entity))
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
		else
			writeJSON(response, jsonBuilder.returnFailureJson("\"数据修改失败,详情见错误日志\""));

	}

	/**
	 * 移除房间设备，将房间ID置为空
	 * @param uuid
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Auth("DEVICEALLOT_delete")
	@RequestMapping("/dodelete")
	public void dodelete(String uuid, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String uuids[] = uuid.split(",");
		for (int i = 0; i < uuids.length; i++) {
			thisService.doUpdateByProperties("uuid", uuids[i], "roomId", "");
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("'成功。'"));
	}

	// 获取高级参数
	@RequestMapping("/highParam_read")
	public void highParam_read(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		String strData = "";
		if (perEntity.getAdvParam() != null) {
			TLVUtils.decode(perEntity.getAdvParam(), tlvs.getTlvs());
			strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
		}
		writeJSON(response, strData);// 返回数据
	}

	@Auth("BASESMARTDEVICE_setHigh")
	@RequestMapping("/doSetHighParam")
	public void doSetHighParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		SysUser currentUser = getCurrentSysUser();
		String termTypeID = request.getParameter("termTypeID");

		// 1.判断，是否批量设置(0-不批量，4-本楼层，3-本楼栋，2-本校区，1-本学校，5-选择批量)
		String termRadio = request.getParameter("termRadio");
		if ("1".equals(termRadio)) {
			thisService.doBatchUpdateHighParam(tlvs, termTypeID, "1", currentUser.getXm());
		} else if ("2".equals(termRadio)) {
			thisService.doBatchUpdateHighParam(tlvs, termTypeID, "2", currentUser.getXm());
		} else if ("3".equals(termRadio)) {
			thisService.doBatchUpdateHighParam(tlvs, termTypeID, "3", currentUser.getXm());
		} else if ("4".equals(termRadio)) {
			thisService.doBatchUpdateHighParam(tlvs, termTypeID, "4", currentUser.getXm());
		} /*
			 * else if("5".equals(termRadio)){ String
			 * termIds=request.getParameter("termIds");
			 * thisService.doUpdatHighParamToIds(tlvs, termIds ,
			 * currentUser.getXm()); }
			 */else { // 默认为0，只设置当前自己
			thisService.doUpdateHighParam(tlvs, currentUser.getXm());
		}

		writeJSON(response, jsonBuilder.returnSuccessJson("\"设备参数设置成功！\""));

		// byte[] result = null;
		// PtTerm perEntity = thisService.get(tlvs.getUuid());
		// SysUser currentUser = getCurrentSysUser();
		// result=TLVUtils.encode(tlvs.getTlvs());
		// perEntity.setAdvParam(result);
		// perEntity.setUpdateUser(currentUser.getXm());
		// perEntity.setUpdateTime(new Date());
		// thisService.merge(perEntity);// 执行修改方法
		// writeJSON(response,
		// jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
	}

	// 获取基础参数
	@RequestMapping("/baseParam_read")
	public void baseParam_read(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		PtTerm perEntity = thisService.get(tlvs.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		String strData = "";
		if (perEntity.getBaseParam() != null) {
			TLVUtils.decode(perEntity.getBaseParam(), tlvs.getTlvs());
			if ("11".equals(perEntity.getTermTypeID()) || "17".equals(perEntity.getTermTypeID())) {
				tlvs.setNotes(perEntity.getNotes());
				strData = JsonBuilder.getInstance().toJson(tlvs);
			} else {
				strData = JsonBuilder.getInstance().buildList(tlvs.getTlvs(), "");// 处理数据
			}
		}
		writeJSON(response, strData);// 返回数据

	}

	@Auth("BASESMARTDEVICE_setBase")
	@RequestMapping("/doSetBaseParam")
	public void doSetBaseParam(TLVModel tlvs, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		SysUser currentUser = getCurrentSysUser();
		String termTypeID = request.getParameter("termTypeID");
		String notes = request.getParameter("notes");

		// 1.判断，是否批量设置(0-不批量，4-本楼层，3-本楼栋，2-本校区，1-本学校，5-选择批量)
		String termRadio = request.getParameter("termRadio");
		if ("1".equals(termRadio)) {
			thisService.doBatchUpdateBaseParam(tlvs, termTypeID, notes, "1", currentUser.getXm());
		} else if ("2".equals(termRadio)) {
			thisService.doBatchUpdateBaseParam(tlvs, termTypeID, notes, "2", currentUser.getXm());
		} else if ("3".equals(termRadio)) {
			thisService.doBatchUpdateBaseParam(tlvs, termTypeID, notes, "3", currentUser.getXm());
		} else if ("4".equals(termRadio)) {
			thisService.doBatchUpdateBaseParam(tlvs, termTypeID, notes, "4", currentUser.getXm());
		} /*
			 * else if("5".equals(termRadio)){ String
			 * termIds=request.getParameter("termIds");
			 * thisService.doUpdatHighParamToIds(tlvs, termIds ,
			 * currentUser.getXm()); }
			 */else { // 默认为0，只设置当前自己
			thisService.doUpdateBaseParam(tlvs, notes, currentUser.getXm());
		}

		writeJSON(response, jsonBuilder.returnSuccessJson("\"设备参数设置成功！\""));

	}

	@RequestMapping(value = { "/termlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void listterm(@ModelAttribute PtTerm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<PtTerm> qr = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@Auth("BASESMARTDEVICE_export")
	@RequestMapping("/doExportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("exportPtTermIsEnd", "0");
		request.getSession().removeAttribute("exportPtTermIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 15, 15, 15, 15 };

		// 数据字典项
		String mapKey = null;
		String[] propValue = { "PTTERMTYPE" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}

		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		String termName = request.getParameter("termName");

		String hql = " from PtTerm a where a.isDelete=0 ";
		// 组装房间id参数
		if (StringUtils.isNotEmpty(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时
				hql += " and a.roomId='" + roomId + "'";

			} else { // 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId);
                if (!roomList.isEmpty()) {
					String roomIds = roomList.stream().collect(Collectors.joining("','", "'", "'"));
					hql += " and a.roomId in (" + roomIds + ") ";
				} else {
					hql += " and 1=2 "; // 区域之下没有房间，则显示空数据
				}
			}
		}
		List<PtTerm> ptTermList = null;
		if (StringUtils.isNotEmpty(termName)) {
			hql += " and termName like'%" + termName + "%'";
		}
		hql += " order by a.createTime desc";
		ptTermList = thisService.queryByHql(hql);

		// 处理班级基本数据
		List<Map<String, String>> ptTermExportList = new ArrayList<>();
		Map<String, String> ptTermExporMap = null;
		String ClassName = "";
		int i = 1;
		for (PtTerm ptTermdetail : ptTermList) {
			ptTermExporMap = new LinkedHashMap<>();
			ptTermExporMap.put("xh", i + "");
			ptTermExporMap.put("termName", ptTermdetail.getTermName());
			ptTermExporMap.put("gatewayName", ptTermdetail.getGatewayName());
			ptTermExporMap.put("roomName", ptTermdetail.getRoomName());
			ptTermExporMap.put("termTypeID", mapDicItem.get(ptTermdetail.getTermTypeID() + "PTTERMTYPE"));
			i++;
			ptTermExportList.add(ptTermExporMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", ptTermExportList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号", "设备名称", "网关名称", "房间名称", "设备类型" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "房间下所有设备", "设备列表", allList);
		if (result == true) {
			request.getSession().setAttribute("exportPtTermIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportPtTermIsEnd", "0");
			request.getSession().setAttribute("exportPtTermIsState", "0");
		}
	}
	@RequestMapping("/checkExportEnd")
	public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportPtTermIsEnd");
		Object state = request.getSession().getAttribute("exportPtTermIsState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
		}
	}

	@Auth("SBXX_export")
	@RequestMapping("/doSbxxExportExcel")
	public void doSbxxExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("exportSbxxIsEnd", "0");
		request.getSession().removeAttribute("exportSbxxIsState");

		String termName = request.getParameter("termName");
		String roomName = request.getParameter("roomName");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 25, 30, 20, 20, 25 };
		// 数据字典项
		String mapKey = null;
		String[] propValue = { "PTTERMTYPE" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}
		List<PtTerm> ptTermList = null;
		String hql = " from PtTerm a where a.isDelete=0 ";

		if (StringUtils.isNotEmpty(termName)) {
			hql += " and a.termName like'%" + termName + "%'";
		}
		if (StringUtils.isNotEmpty(roomName)) {
			hql += " and a.roomName like'%" + roomName + "%'";
		}
		ptTermList = thisService.queryByHql(hql);

		List<Map<String, String>> ptTermExpList = new ArrayList<>();

		Map<String, String> ptTermMap = null;
		int i = 1;
		for (PtTerm ptTerm : ptTermList) {
			ptTermMap = new LinkedHashMap<>();
			ptTermMap.put("xh", i + "");
			ptTermMap.put("termName", ptTerm.getTermName());
			ptTermMap.put("termSN", ptTerm.getTermSN());
			ptTermMap.put("roomName", ptTerm.getRoomName());
			ptTermMap.put("gatewayName", ptTerm.getGatewayName());
			ptTermMap.put("termTypeID", mapDicItem.get(ptTerm.getTermTypeID() + "PTTERMTYPE"));
			i++;
			ptTermExpList.add(ptTermMap);
		}

		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", ptTermExpList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号", "设备名称", "序列号", "房间名称", "网关名称", "设备类型" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "设备序列号", "设备序列号", allList);
		if (result == true) {
			request.getSession().setAttribute("exportSbxxIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportSbxxIsEnd", "0");
			request.getSession().setAttribute("exportSbxxIsState", "0");
		}

	}
	@RequestMapping("/checkSbxxExportEnd")
	public void checkSbxxExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportSbxxIsEnd");
		Object state = request.getSession().getAttribute("exportSbxxIsState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
		}
	}



	@Auth("DEVICEALLOT_export")
	@RequestMapping("/doExportPtTermAllotExcel")
	public void doExportPtTermAllotExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("exportPtTermAllotIsEnd", "0");
		request.getSession().removeAttribute("exportPtTermAllotIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 15, 15, 15, 15, 15, 15 };

		// 数据字典项
		String mapKey = null;
		String[] propValue = { "PTTERMTYPE" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}
		List<PtTerm> ptTermList = null;
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		String termSN = request.getParameter("termSN");
		String termNo = request.getParameter("termNo");
		String termName = request.getParameter("termName");

		String hql = " from PtTerm a where a.isDelete=0 ";
		//组装房间id参数
		if (StringUtils.isNotEmpty(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时
				hql += " and a.roomId='"+roomId+"'";
				
			} else {					// 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId);
					
				if(!roomList.isEmpty()){
					String roomIds=roomList.stream().collect(Collectors.joining("','","'","'"));				
					hql += " and a.roomId in (" + roomIds + ") ";
				} else {
					hql += " and 1=2 ";						//区域之下没有房间，则显示空数据
				}					
			}
		}

		if (StringUtils.isNotEmpty(termSN)) {
			hql += " and a.termSN like'%" + termSN + "%'";
		}
		if (StringUtils.isNotEmpty(termNo)) {
			hql += " and a.termNo like'%" + termNo + "%'";
		}
		if (StringUtils.isNotEmpty(termName)) {
			hql += " and a.termName like'%" + termName + "%'";
		}
		hql += " order by a.createTime desc";
		ptTermList = thisService.queryByHql(hql);

		// 处理基本数据
		List<Map<String, String>> ptTermExportList = new ArrayList<>();
		Map<String, String> ptTermExporMap = null;
		int i = 1;
		for (PtTerm ptTermdetail : ptTermList) {
			ptTermExporMap = new LinkedHashMap<>();
			ptTermExporMap.put("xh", i + "");
			ptTermExporMap.put("termSN", ptTermdetail.getTermSN());
			ptTermExporMap.put("termNo", (String.valueOf(ptTermdetail.getTermNo()).equals("null") ? ""
					: String.valueOf(ptTermdetail.getTermNo())));
			ptTermExporMap.put("termName", ptTermdetail.getTermName());
			ptTermExporMap.put("gatewayName", ptTermdetail.getGatewayName());
			ptTermExporMap.put("termTypeID", mapDicItem.get(ptTermdetail.getTermTypeID() + "PTTERMTYPE"));
			String termStatus = "禁用";
			if (ptTermdetail.getTermStatus() != null) {
				if (ptTermdetail.getTermStatus() == 0) {
					termStatus = "禁用";
				} else {
					termStatus = "启用";
				}
			}
			ptTermExporMap.put("termStatus", termStatus);
			i++;
			ptTermExportList.add(ptTermExporMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", ptTermExportList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号", "序列号", "机号", "设备名称", "网关名称", "设备类型", "设备状态" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "房间下所有设备", "设备分配列表", allList);
		if (result == true) {
			request.getSession().setAttribute("exportPtTermAllotIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportPtTermAllotIsEnd", "0");
			request.getSession().setAttribute("exportPtTermAllotIsState", "0");
		}
	}

	@RequestMapping("/checkPtTermAllotExportEnd")
	public void checkPtTermAllotExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportPtTermAllotIsEnd");
		Object state = request.getSession().getAttribute("exportPtTermAllotIsState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
		}
	}
	/**
	 * 获取某个区域下的所有房间数据（只查询出已定义的房间）
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
			hql = "select a.uuid from BuildRoominfo a where a.isDelete=0 and a.roomType!='0' and a.areaId in (" + areaIds + ")";
			result = thisService.queryEntityByHql(hql);
		}

		return result;
	}
}
