package com.zd.school.build.allot.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.JwOfficeAllot;
import com.zd.school.build.allot.service.JwOfficeAllotService;
import com.zd.school.build.define.model.BuildOfficeDefine;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildOfficeDefineService;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 房间分配（原办公室分配）
 *
 */
@Controller
@RequestMapping("/BaseOfficeAllot")
public class BaseOfficeAllotController extends FrameWorkController<JwOfficeAllot> implements Constant {
	@Resource
	JwOfficeAllotService thisService; // service层接口
	
	@Resource
	CommTreeService treeService; // 生成树
	
	@Resource
	SysUserService sysUserService; // service层接口
	
	@Resource
	BuildRoominfoService offdService;

	/**
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute JwOfficeAllot entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		String filter = request.getParameter("filter");
		String roomId = request.getParameter("roomId"); // 获取区域或房间id（areaId/roomId）
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
			} else { // 当选择的区域不为房间时
				List<String> roomList = getRoomIds(roomId);

				if (!roomList.isEmpty()) {
					String roomIds = roomList.stream().collect(Collectors.joining(","));
					if (StringUtils.isNotEmpty(filter)) {
						filter = filter.substring(0, filter.length() - 1);
						filter += ",{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + roomIds
								+ "\",\"field\":\"roomId\"}" + "]";
					} else {
						filter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + roomIds
								+ "\",\"field\":\"roomId\"}]";
					}

				} else { // 若区域之下没有房间，则直接返回空数据

					strData = jsonBuilder.buildObjListToJson(0L, new ArrayList<>(), true);// 处理数据
					writeJSON(response, strData);// 返回数据
					return;
				}
			}
		}

		QueryResult<JwOfficeAllot> qr = thisService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), filter, true);

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
		String excludes = request.getParameter("excludes");
		List<CommTree> lists = treeService.getCommTree("JW_V_OFFICEALLOTTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, excludes);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 分配办公室
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Auth("BASEROOMALLOT_add")
	@RequestMapping("/doAdd")
	public void doAdd(JwOfficeAllot entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		Boolean flag = false;
		BuildRoominfo off = null;
		Map<String, Object> hashMap = new HashMap<String, Object>();
		String[] name = { "uuid", "isDelete" };
		Object[] value = { entity.getRoomId(), 0 };
		off = offdService.getByProerties(name, value);
		if (off != null) {
			SysUser currentUser = getCurrentSysUser();
			flag = thisService.doAddRoom(entity, hashMap, currentUser);// 执行增加方法
			flag = (Boolean) hashMap.get("flag") == null ? true : (Boolean) hashMap.get("flag");
			if (flag) {
				// 返回实体到前端界面
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			} else {
				StringBuffer xm = (StringBuffer) hashMap.get("xm");
				StringBuffer roomName = (StringBuffer) hashMap.get("roomName");
				writeJSON(response,
						jsonBuilder.returnFailureJson("'操作未完全成功！<br/><br/>【" + xm.substring(0, xm.length() - 1)
								+ "】分别已存在【" + roomName.substring(0, roomName.length() - 1) + "】办公室'"));
				return;
			}

		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"您刚已经在定义中删除了此办公室，请刷新树在重试\""));
			return;
		}

	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@Auth("BASEROOMALLOT_delete")
	@RequestMapping("/doDelete")
	public void doDelete(String uuid, String roomId, String tteacId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (StringUtils.isEmpty(uuid)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("\"没有传入删除主键\""));
			return;
		} else {
			boolean flag = thisService.doDeleteOff(uuid, roomId, tteacId);
			if (flag) {
				thisService.doOffSetOff(roomId);
				writeJSON(response, jsonBuilder.returnSuccessJson("\"删除成功\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"删除失败\""));
			}
		}
	}
	/*
	 * @RequestMapping("/doSetOff") public void doSetOff(HttpServletRequest
	 * request, HttpServletResponse response) throws IOException { String
	 * roomIds = request.getParameter("roomId");
	 * thisService.doOffSetOff(roomIds); }
	 */

	/**
	 * 公用的教师查询（用于办公室分配使用）
	 * 
	 */
	@RequestMapping(value = { "/teacherAllot" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void teacherAllot(@ModelAttribute SysUser entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<SysUser> qr = sysUserService.queryPageResult(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	// 推送消息
	@Auth("BASEROOMALLOT_officeTs")
	@RequestMapping("/doPushMessage")
	public void doPushMessage(String roomId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Boolean flag = false;
		flag = thisService.doPushMessage(roomId);
		if (flag) {
			writeJSON(response, jsonBuilder.returnSuccessJson("\"推送信息成功。\""));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"推送信息失败。\""));
		}

	}

	/**
	 * 获取某个区域下的所有办公室数据
	 * 
	 * @param roomId
	 * @param roomLeaf
	 * @return
	 */
	private List<String> getRoomIds(String areaId) {
		List<String> result = new ArrayList<>();

		String hql = "select a.uuid from BuildRoomarea a where a.isDelete=0  and a.areaType='04' and a.treeIds like '%"
				+ areaId + "%'";
		List<String> lists = thisService.queryEntityByHql(hql);

		if (lists.size() > 0) {
			String areaIds = lists.stream().collect(Collectors.joining("','"));
			hql = "select a.uuid from BuildRoominfo a where a.isDelete=0 and a.roomType='2' and a.areaId in ('"
					+ areaIds + "')";
			result = thisService.queryEntityByHql(hql);

		}

		return result;
	}
}
