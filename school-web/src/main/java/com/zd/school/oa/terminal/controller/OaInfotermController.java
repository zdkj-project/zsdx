
package com.zd.school.oa.terminal.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.model.OaRoomTerm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaInfotermController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 信息发布终端(OA_T_INFOTERM)实体Controller. date: 2017-01-14
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/OaInfoterm")
public class OaInfotermController extends FrameWorkController<OaInfoterm> implements Constant {

	@Resource
	OaInfotermService thisService; // service层接口

	@Resource
	private BuildRoominfoService roomService;

	/**
	 * @Title: list
	 * @Description: 查询数据列表
	 * @param entity
	 *            实体类
	 * @param request
	 * @param response
	 * @throws IOException
	 *             设定参数
	 * @return void 返回类型
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute OaInfoterm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<OaInfoterm> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: doadd
	 * @Description: 增加新实体信息至数据库
	 * @param OaInfoterm
	 *            实体类
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 */
	@RequestMapping("/doadd")
	public void doAdd(OaInfoterm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		Integer beforeNumber = Integer.parseInt(request.getParameter("beforeNumber"));
		Integer termCount = Integer.parseInt(request.getParameter("termCount"));
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doAddEntity(entity, currentUser, beforeNumber, termCount);// 执行增加方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		}
	}

	/**
	 * 
	 * @Title: doDelete
	 * @Description: 逻辑删除指定的数据
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入取消主键'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			try {
				boolean flag = thisService.doLogicDeleteByIds(delIds, currentUser);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'取消成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'取消失败,详情见错误日志'"));
				}
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("'取消失败,详情见错误日志'"));
			}
		}
	}

	/**
	 * @Title: doUpdate
	 * @Description: 编辑指定记录
	 * @param OaInfoterm
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(OaInfoterm entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/doSetTerminal")
	public void doSetTerminal(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String setTerminals = request.getParameter("terminals");
		String roomId = request.getParameter("roomId");
		String roomName = request.getParameter("roomName");
		SysUser currentUser = getCurrentSysUser();

		try {
			List<OaInfoterm> entityTerminals = (List<OaInfoterm>) jsonBuilder.fromJsonArray(setTerminals,
					OaInfoterm.class);
			Boolean result = thisService.doSetTerminal(entityTerminals, roomId, roomName, currentUser);
			if (result)
				writeJSON(response, jsonBuilder.returnSuccessJson("\"设置成功\""));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("\"设置失败,详情见错误日志\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'设置失败,详情见错误日志'"));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/getRoomTermInfo")
	public void getRoomInfo(String roomId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sql = "select * from OA_V_ROOMTERM where roomId='" + roomId + "'";
		OaRoomTerm roominfo = thisService.doQuerySqlObject(sql, OaRoomTerm.class).get(0);
		String strData = jsonBuilder.toJson(roominfo);
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));
	}

}
