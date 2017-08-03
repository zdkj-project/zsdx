
package com.zd.school.oa.meeting.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.oa.meeting.dao.OaMeetingDao;
import com.zd.school.oa.meeting.service.OaMeetingService;

/**
 * 
 * ClassName: OaMeetingController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 会议信息(OA_T_MEETING)实体Controller. date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/OaMeeting")
public class OaMeetingController extends FrameWorkController<OaMeeting> implements Constant {

	@Resource
	OaMeetingService thisService; // service层接口

	@Resource
	private BaseDicitemService dicitemService;
	@Resource
	private BuildRoominfoService roominfoService;

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
	public void list(@ModelAttribute OaMeeting entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<OaMeeting> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: doadd
	 * @Description: 增加新实体信息至数据库
	 * @param OaMeeting
	 *            实体类
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 */
	@RequestMapping("/doadd")
	public void doAdd(OaMeeting entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
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
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			try {
				boolean flag = thisService.doLogicDeleteByIds(delIds, currentUser);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
				}
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
			}
		}
	}

	/**
	 * @Title: doUpdate
	 * @Description: 编辑指定记录
	 * @param OaMeeting
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(OaMeeting entity, HttpServletRequest request, HttpServletResponse response)
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

	/**
	 * 描述：通过传统方式form表单提交方式导入excel文件
	 *
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/importData", method = { RequestMethod.GET, RequestMethod.POST })
	public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			InputStream in = null;
			List<List<Object>> listObject = null;
			if (!file.isEmpty()) {
				in = file.getInputStream();
				listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
				in.close();

				/*
				 * //要转换的数据字典 String mapKey = null; String[] propValue =
				 * {"TECHNICAL","XBM","XLM","INOUT"}; Map<String, String>
				 * mapDicItem = new HashMap<>(); List<BaseDicitem> listDicItem =
				 * dicitemService.queryByProerties("dicCode", propValue); for
				 * (BaseDicitem baseDicitem : listDicItem) { mapKey =
				 * baseDicitem.getItemName() + baseDicitem.getDicCode();
				 * mapDicItem.put(mapKey, baseDicitem.getItemCode()); }
				 */
				BaseDicitem dicitem = null;
				BuildRoominfo roominfo = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int i = 0; i < listObject.size(); i++) {
					List<Object> lo = listObject.get(i);

					OaMeeting m = new OaMeeting();
					m.setMeetingTitle(lo.get(0).toString());
					m.setMeetingName(lo.get(1).toString());
					dicitem = dicitemService.getByProerties("itemName", lo.get(2).toString());
					if (dicitem != null) {
						m.setMeetingCategory(dicitem.getItemCode());
					} else {
						m.setMeetingCategory("1");
					}
					m.setBeginTime(sdf.parse(lo.get(3).toString()));
					m.setEndTime(sdf.parse(lo.get(4).toString()));
					if (lo.get(5).toString().equals("需要")) {
						m.setNeedChecking((short) 1);
					} else {
						m.setNeedChecking((short) 0);
					}
					m.setMeetingContent(lo.get(6).toString());
					roominfo = roominfoService.getByProerties("roomName", lo.get(7).toString());
					if (roominfo != null) {
						m.setRoomId(roominfo.getUuid());
					}
					m.setRoomName(lo.get(7).toString());
					thisService.merge(m);

				}
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
			}

			writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
		}
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTeacherIsEnd", "0");
		String ids = request.getParameter("ids");
		List<OaMeeting> list = null;
		String hql = " from OaMeeting where isDelete=0 ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and uuid in ('" + ids.replace(",", "','") + "')";
		}
		list = thisService.doQuery(hql);
		List<OaMeeting> exportList = new ArrayList<>();

		BaseDicitem dicitem = null;
		for (OaMeeting o : list) {
			String[] name = { "dicCode", "itemCode" };
			String[] value = { "MEETINGCATEGORY", o.getMeetingCategory() };
			dicitem = dicitemService.getByProerties(name, value);
			if (dicitem != null) {
				o.setMeetingCategoryName(dicitem.getItemName());
			} else {
				o.setMeetingCategoryName("会议类型字典编码错误");
			}
		}
		exportList.addAll(list);
		// Thread.sleep(10000);
		FastExcel.exportExcel(response, "会议信息", exportList);
		request.getSession().setAttribute("exportTeacherIsEnd", "1");

	}

	@RequestMapping("/checkExportEnd")
	public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strData = "";
		Object isEnd = request.getSession().getAttribute("exportTeacherIsEnd");

		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
				request.getSession().setAttribute("exportTeacherIsEnd", "0");
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
			request.getSession().setAttribute("exportTeacherIsEnd", "0");
		}
	}

	/**
	 * 导出信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportMeetingExcel")
	public void exportRoomExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassRoomIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassRoomIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		// SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		// SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		
		
		Map<String, String> mapMeetingCategory = new HashMap<>(); 
		String hql1 = " from BaseDicitem where dicCode in ('MEETINGCATEGORY')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			mapMeetingCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName()); 
		}
		 

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 30, 30, 15, 15, 10, 15, 15,20,30,10,40 };

		
		// --------.处理信息，并组装表格数据--------------------
		String ids = request.getParameter("ids"); 
		List<OaMeeting> list = null;
		String hql = " from OaMeeting where isDelete=0 ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and uuid in ('" + ids.replace(",", "','") + "')";
		}
		list = thisService.doQuery(hql);		
		
		// 处理导出的基本数据
		List<Map<String, String>> mapList = new ArrayList<>();
		Map<String, String> map = null;
		for (OaMeeting oaMeeting : list) {
			map = new LinkedHashMap<>();
			map.put("meetingTitle",oaMeeting.getMeetingTitle());
			map.put("meetingName",oaMeeting.getMeetingName());
			map.put("meetingCategory",mapMeetingCategory.get(oaMeeting.getMeetingCategory()));
			map.put("emcee",oaMeeting.getEmcee());
			map.put("needChecking",oaMeeting.getNeedChecking()==1?"需要":"不需要");
			map.put("beginTime",fmtDate.format(oaMeeting.getBeginTime()));
			map.put("endTime",fmtDate.format(oaMeeting.getEndTime()));
			//map.put("meetingContent",oaMeeting.getMeetingContent());
			map.put("roomName",oaMeeting.getRoomName());
			map.put("mettingEmpname",oaMeeting.getMettingEmpname());
			Short state= oaMeeting.getMeetingState();
			map.put("meetingState",(state==null || state==0)?"未开始":state==1?"进行中":"已完成");
			map.put("attendResult",oaMeeting.getAttendResult());
			
			mapList.add(map);
		}
		Map<String, Object> allMap = new LinkedHashMap<>();
		allMap.put("data", mapList);
		allMap.put("title", "会议考勤信息表");
		allMap.put("head", new String[] { "会议主题", "会议名称", "会议类型", "主持人", "是否考勤", "开始时间", "结束时间",  "会议地点", "参会人员", "会议状态","考勤结果" }); // 规定名字相同的，设定为合并
		allMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		allMap.put("columnAlignment", new Integer[] { 1, 1, 0, 0, 0, 0, 0, 1,1,0,1 }); // 0代表居中，1代表居左，2代表居右
		allMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(allMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "会议考勤信息表", "会议考勤信息", allList);
		if (result == true) {
			request.getSession().setAttribute("exportMeetingIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportMeetingIsEnd", "0");
			request.getSession().setAttribute("exportMeetingIsState", "0");
		}

	}

	/**
	 * 判断导出信息时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportMeetingEnd")
	public void checkExportRoomEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportMeetingIsEnd");
		Object state = request.getSession().getAttribute("exportMeetingIsState");
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

}
