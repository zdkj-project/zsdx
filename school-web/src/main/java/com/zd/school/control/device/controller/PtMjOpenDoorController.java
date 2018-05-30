package com.zd.school.control.device.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.school.control.device.model.PtMjOpenDoor;
import com.zd.school.control.device.service.PtMjOpenDoorService;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 门禁开门记录
 * 
 * @author hucy
 *
 */
@Controller
@RequestMapping("/PtMjOpenDoor")
public class PtMjOpenDoorController extends FrameWorkController<PtMjOpenDoor> implements Constant {

	@Resource
	PtMjOpenDoorService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成树

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute PtMjOpenDoor entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		String filter = request.getParameter("filter");

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
				List<String> roomList = getRoomIds(roomId, roomLeaf);
					
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

		QueryResult<PtMjOpenDoor> qr = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), filter, false); // 列出包含isDelte不为0的数据
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@Auth("PtMjOpenDoor_export")
	@RequestMapping("/doExportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("exportPtMjOpenDoorIsEnd", "0");
		request.getSession().removeAttribute("exporPtMjOpenDoorIsState");
		String roomId = request.getParameter("roomId");
		String roomLeaf = request.getParameter("roomLeaf");
		String openDateStart = request.getParameter("openDateStart");
		String openDateEnd = request.getParameter("openDateEnd");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 15, 15, 20, 20, 30, 15 ,15};
		List<PtMjOpenDoor> mjOpenDoorList = null;

		String hql = " from PtMjOpenDoor a where 1=1 ";		// a.isDelete=0 ";暂时不需要这个条件
		
		//组装房间id参数
		if (StringUtils.isNotEmpty(roomId)) {
			if ("1".equals(roomLeaf)) { // 当选择的区域为房间时
				hql += " and a.roomId='"+roomId+"'";
				
			} else {					// 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId, roomLeaf);
					
				if(!roomList.isEmpty()){
					String roomIds=roomList.stream().collect(Collectors.joining("','","'","'"));				
					hql += " and a.roomId in (" + roomIds + ") ";
				} else {
					hql += " and 1=2 ";						//区域之下没有房间，则显示空数据
				}					
			}
		}
		
		if (StringUtils.isNotEmpty(openDateStart)) {
			hql += " and a.openDate>='" + openDateStart + "'";
		}
		if (StringUtils.isNotEmpty(openDateEnd)) {
			hql += " and a.openDate<='" + openDateEnd + "'";
		}
		
		hql += " order by a.openDate desc";
		
		mjOpenDoorList = thisService.queryByHql(hql);

		List<Map<String, String>> mjOpenDoorExpList = new ArrayList<>();

		Map<String, String> mjOpenDoorMap = null;
		int i = 1;
		for (PtMjOpenDoor mjOpenDoor : mjOpenDoorList) {
			mjOpenDoorMap = new LinkedHashMap<>();
			mjOpenDoorMap.put("xh", i + "");
			mjOpenDoorMap.put("roomName", mjOpenDoor.getRoomName());
			mjOpenDoorMap.put("termName", mjOpenDoor.getTermName());
			mjOpenDoorMap.put("userName", mjOpenDoor.getUserName());
			mjOpenDoorMap.put("openDate", format.format(mjOpenDoor.getOpenDate()));
			mjOpenDoorMap.put("roomArea", mjOpenDoor.getRoomArea());
			mjOpenDoorMap.put("inoutType", mjOpenDoor.getInoutType());
			mjOpenDoorMap.put("openType", mjOpenDoor.getOpenType());
			i++;
			mjOpenDoorExpList.add(mjOpenDoorMap);
		}

		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", mjOpenDoorExpList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号", "房间名称", "设备名称", "开门人员姓名", "开门时间", "房间所在区域", "进出标识", "开门类型" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 ,0}); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "门禁开门记录", "门禁开门记录", allList);
		if (result == true) {
			request.getSession().setAttribute("exportPtMjOpenDoorIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportPtMjOpenDoorIsEnd", "0");
			request.getSession().setAttribute("exporPtMjOpenDoorIsState", "0");
		}

	}

	@RequestMapping("/checkExportEnd")
	public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportPtMjOpenDoorIsEnd");
		Object state = request.getSession().getAttribute("exporPtMjOpenDoorIsState");
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
	 * 获取某个区域下的所有房间数据
	 * 
	 * @param roomId
	 * @param roomLeaf
	 * @return
	 */
	private List<String> getRoomIds(String roomId, String roomLeaf) {
		List<String> result = new ArrayList<>();

		// 当选择的区域不为房间时
		String hql = "select a.uuid from BuildRoomarea a where a.isDelete=0  and a.areaType='04' and a.treeIds like '%"
				+ roomId + "%'";
		List<String> lists = thisService.queryEntityByHql(hql);
		if (lists.size() > 0) {
			String areaIds = lists.stream().collect(Collectors.joining("','", "'", "'"));
			hql = "select a.uuid from BuildRoominfo a where a.isDelete=0  and a.roomType!='0' and a.areaId in (" + areaIds + ")";
			result = thisService.queryEntityByHql(hql);
		}

		return result;
	}
}
