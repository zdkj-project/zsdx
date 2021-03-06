
package com.zd.school.jw.train.controller;

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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.Base64Util;
import com.zd.core.util.DBContextHolder;
import com.zd.core.util.ExportExcel;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.core.util.exportMeetingInfo;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.vo.VoTrainClassCheck;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: TrainClasstraineeController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 班级学员信息(TRAIN_T_CLASSTRAINEE)实体Controller. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainClasstrainee")
public class TrainClasstraineeController extends FrameWorkController<TrainClasstrainee> implements Constant {

	private static Logger logger = Logger.getLogger(TrainClasstraineeController.class);

	@Resource
	TrainClasstraineeService thisService; // service层接口

	@Resource
	TrainClassService trainClassService; // service层接口

	@Resource
	SysUserService sysUserService; // service层接口

	@Resource
	BaseDicitemService dicitemService;// 字典项service层接口

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
	public void list(@ModelAttribute TrainClasstrainee entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<TrainClasstrainee> qResult = thisService.list(start, limit, sort, filter, true);
		for (int i = 0; i < qResult.getResultList().size(); i++) {
			if (Base64Util.isBase64(qResult.getResultList().get(i).getMobilePhone())) {
				qResult.getResultList().get(i)
						.setMobilePhone(Base64Util.decodeData(qResult.getResultList().get(i).getMobilePhone()));
			}
			if (Base64Util.isBase64(qResult.getResultList().get(i).getSfzjh())) {
				qResult.getResultList().get(i)
						.setSfzjh(Base64Util.decodeData(qResult.getResultList().get(i).getSfzjh()));
			}
		}

		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
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
	public void doAdd(TrainClasstrainee entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			String traineeId = entity.getTraineeId();

			String hql1 = " o.isDelete='0' and o.classId='" + entity.getClassId() + "'";
			if (thisService.IsFieldExist("traineeId", traineeId, "-1", hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"班级学员不能重复！\""));
				return;
			}

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
		String classId = request.getParameter("classId");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			try {
				boolean flag = thisService.doLogicDeleteByIds(classId, delIds, currentUser);
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
	 *
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(TrainClasstrainee entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			String traineeId = entity.getTraineeId();

			String hql1 = " o.isDelete='0' and o.classId='" + entity.getClassId() + "'";
			if (thisService.IsFieldExist("traineeId", traineeId, entity.getUuid(), hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"班级学员不能重复！\""));
				return;
			}

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

			SysUser currentUser = getCurrentSysUser();

			String classId = request.getParameter("classId");
			String needSync = request.getParameter("needSync");

			InputStream in = null;
			List<List<Object>> listObject = null;
			List<ImportNotInfo> listReturn;

			if (!file.isEmpty()) {
				in = file.getInputStream();
				listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
				in.close();

				listReturn = thisService.doImportTrainee(listObject, classId, needSync, currentUser);

				if (listReturn.size() == 0)
					writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
				else {
					String strData = jsonBuilder.buildList(listReturn, "");
					request.getSession().setAttribute("TrainClassTraineeImportError", strData);
					writeJSON(response, jsonBuilder.returnSuccessJson("-1")); // 返回前端-1，表示存在错误数据
				}

			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请下载模板或联系管理员！\""));
		}
	}

	@RequestMapping(value = { "/downNotImportInfo" })
	public void downNotImportInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object obj = request.getSession().getAttribute("TrainClassTraineeImportError");
		if (obj != null) {

			request.getSession().removeAttribute("TrainClassTraineeImportError");// 移除此session

			String downData = (String) obj;

			List<ImportNotInfo> list = (List<ImportNotInfo>) jsonBuilder.fromJsonArray(downData, ImportNotInfo.class);
			ExportExcel excel = new ExportExcel();

			String[] Title = { "序号", "学生姓名", "异常级别", "异常原因" };
			Integer[] coulumnWidth = { 8, 20, 20, 100 };
			Integer[] coulumnDirection = { 1, 1, 1, 1 };

			List<String> excludeList = new ArrayList<>();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
			String fileNAME = "（" + sdf2.format(new Date()) + "导出）导入班级学员的异常信息名单";

			excel.exportExcel(response, fileNAME, Title, list, excludeList, coulumnWidth, coulumnDirection);
		}
	}

	/**
	 * 描述：同步班级学员信息到学员库
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/syncClassTrainee", method = { RequestMethod.GET, RequestMethod.POST })
	public void syncClassTrainee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {

			SysUser currentUser = getCurrentSysUser();

			String classId = request.getParameter("classId");

			thisService.doSyncClassTrainee(classId, currentUser);

			writeJSON(response, jsonBuilder.returnSuccessJson("\"同步到学员库成功！\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请联系管理员！\""));
		}
	}

	/**
	 * 查询班级的学员用餐情况
	 */
	@RequestMapping(value = { "/getClassFoodTrainees" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getClassFoodTrainees(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据

		try {
			Integer start = super.start(request);
			Integer limit = super.limit(request);
			String classId = request.getParameter("classId");
			String isDelete = request.getParameter("isDelete");

			// g.isDelete=0 and 查询所有状态的
			// new 不查询状态为1的
			String hql = "select  new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.breakfast,g.lunch,g.dinner,g.isDelete) from TrainClasstrainee g "
					+ " where g.classId='" + classId + "'";
			if (isDelete != null) {
				hql += " and g.isDelete!=1 ";
			}
			hql += " order by g.breakfast desc,g.lunch desc,g.dinner desc";

			QueryResult<TrainClasstrainee> result = thisService.getQueryResult(hql, start, limit);

			strData = jsonBuilder.buildObjListToJson(result.getTotalCount(), result.getResultList(), true);
			writeJSON(response, strData);// 返回数据
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员！\""));
		}
	}

	/**
	 * 查询班级的学员住宿情况
	 */
	@RequestMapping(value = { "/getClassRoomTrainees" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getClassRoomTrainees(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据

		try {
			Integer start = super.start(request);
			Integer limit = super.limit(request);
			String classId = request.getParameter("classId");
			String isDelete = request.getParameter("isDelete");

			// g.isDelete=0 or g.isDelete=1 and
			// new: 不查询状态为1的
			String hql = "select new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.siesta,g.sleep,g.roomId,g.roomName,g.workUnit,g.isDelete) from TrainClasstrainee g "
					+ " where g.classId='" + classId + "'";
			if (isDelete != null) {
				hql += " and g.isDelete!=1 ";
			}
			hql += "  order by g.siesta desc,g.sleep desc,g.xbm asc,g.workUnit asc";

			QueryResult<TrainClasstrainee> result = thisService.getQueryResult(hql, start, limit);

			strData = jsonBuilder.buildObjListToJson(result.getTotalCount(), result.getResultList(), true);
			writeJSON(response, strData);// 返回数据
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员！\""));
		}
	}

	/**
	 * 更新学员的房间信息
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping("/updateRoomInfo")
	public void updateRoomInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException, InterruptedException {

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		String roomId = request.getParameter("roomId");
		String roomName = request.getParameter("roomName");
		String ids = request.getParameter("ids");
		String xbm = request.getParameter("xbm");
		String classId = request.getParameter("classId");
		int result = thisService.doUpdateRoomInfo(classId, roomId, roomName, ids, xbm, currentUser);

		if (result == 1) {
			writeJSON(response, jsonBuilder.returnSuccessJson("\"设置宿舍成功！\""));
		} else if (result == 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"此宿舍可分配的人数超出限制！\""));
		} else if (result == -2) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"当前选择的学员与此宿舍学员的性别不一致！\""));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
	}

	/**
	 * 取消学员的房间信息
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping("/cancelRoomInfo")
	public void cancelRoomInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException, InterruptedException {

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		String ids = request.getParameter("ids");

		int result = thisService.doCancelRoomInfo(ids, currentUser);

		if (result == 1) {
			writeJSON(response, jsonBuilder.returnSuccessJson("\"取消宿舍成功！\""));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
	}

	/*
	 * 一键同步UP的方式
	 */
	@RequestMapping("/doSyncClassCardInfoFromUp")
	public void doSyncClassCardInfoFromUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuffer returnJson = null;
		try {
			String classId = request.getParameter("classId");
			TrainClass trainClass = trainClassService.get(classId);
			// 设置部门ID：Train20170505（Train+班级编号）【班级编号不可变化】
			String departmentId = "Train" + trainClass.getClassNumb();

			// 1.切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			// 2.查询UP中的发卡信息(查询班级学员的卡片信息)
			// String sql = "select convert(varchar,a.CardID) as
			// upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as
			// userId,"
			// + " convert(int,a.CardStatusIDXF) as useState,"
			// + " b.EmployeeStrID as sid,b.EmployeeStatusID as employeeStatusID
			// "
			// + " from Tc_Employee b join TC_Card a on b.CardID=a.CardID" + "
			// where b.DepartmentID='"
			// + departmentId + "'" + " order by a.CardID asc,a.ModifyDate asc";

			// String sql = "select convert(varchar,a.CardID) as
			// upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as
			// userId,"
			// + " convert(int,a.CardStatusIDXF) as useState,"
			// + " b.SID as sid,b.EmployeeStatusID as employeeStatusID "
			// + " from Tc_Employee b join TC_Card a on b.CardID=a.CardID"
			// + " where b.DepartmentID='"+departmentId+"'"
			// + " order by a.CardID asc,a.ModifyDate asc";

			// 修改了查询的方式，以发卡表中的最新的一条数据为准
			String sql = "select a.UserId as userId,a.EmployeeStrID as sid,a.EmployeeStatusID as employeeStatusID,"
					+ " convert(varchar,b.CardID) as upCardId,convert(varchar,b.FactoryFixID) as factNumb,"
					+ " convert(int,b.CardStatusIDXF) as useState from Tc_Employee a join TC_Card b" + " on b.CardID=("
					+ "		select top 1 CardID from TC_Card where EmployeeID=a.EmployeeID order by ModifyDate desc"
					+ " ) where a.DepartmentID='" + departmentId + "' and a.UserId is not null ";

			List<CardUserInfoToUP> upCardUserInfos = thisService.getQuerySqlObject(sql, CardUserInfoToUP.class);

			// 3.恢复数据源
			DBContextHolder.clearDBType();

			int row = 0;
			if (upCardUserInfos.size() > 0) {
				row = sysUserService.syncClassCardInfoFromUp(upCardUserInfos, classId);
			} else {
				row = sysUserService.syncClassCardInfoFromUp(null, classId);
			}

			if (row == 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"此班级学员已是最新发卡数据！\"}");
			} else if (row > 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"更新发卡数据成功！\"}");
			} else {
				returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"更新发卡数据失败，请联系管理员！\"}");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"更新发卡数据失败，请联系管理员！\"}");
		}

		writeAppJSON(response, returnJson.toString());
	}

	/**
	 * 导出班级学员导入UP模版排信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportExcel")
	public void exportSiteExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassTraineeIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassTraineeIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 30, 15, 20, 40, 20 };

		// 1.班级信息
		String classId = request.getParameter("classId"); // 程序中限定每次只能导出一个班级

		// 2.班级学员信息
		List<TrainClasstrainee> trainClasstraineeList = null;
		String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(classId)) {
			hql += " and classId ='" + classId + "'";
		}
		hql += " order by createTime desc";
		trainClasstraineeList = thisService.getQuery(hql);

		// 处理班级基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		for (TrainClasstrainee classTrainee : trainClasstraineeList) {
			traineeMap = new LinkedHashMap<>();

			traineeMap.put("className", classTrainee.getClassName());
			traineeMap.put("traineeName", classTrainee.getXm());
			traineeMap.put("sfzjh", classTrainee.getSfzjh());
			traineeMap.put("uuid", classTrainee.getUuid());
			traineeMap.put("cardNum", "");
			traineeList.add(traineeMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", traineeList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "班级名称", "人员姓名", "身份证件号", "人员标识", "卡片号" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "班级学员发卡模版表格", null, allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassTraineeIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassTraineeIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassTraineeIsState", "0");
		}
	}

	/**
	 * 判断导出时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportEnd")
	public void checkExportRoomEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassTraineeIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassTraineeIsState");
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

	@RequestMapping("/getCreditsDetail")
	public void doGetCreditsDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String classId = request.getParameter("classId");
		String classTraineeId = request.getParameter("ids");
		List<Map<String, Object>> list = thisService.getClassTraineeCreditsList(classTraineeId);
		strData = jsonBuilder.buildObjListToJson((long) list.size(), list, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 导出班级学员导入UP模版排信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getCreditsexportExcel")
	public void getCreditsexportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportgetCreditsIsEnd", "0");
		request.getSession().removeAttribute("exportgetCreditsIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 30, 30, 20, 20, 20, 20 };

		// 1.班级信息
		String classId = request.getParameter("classId"); // 程序中限定每次只能导出一个班级
		String classTraineeId = request.getParameter("ids");

		List<Map<String, Object>> classTraineeinfo = new ArrayList<>();
		String sql = "SELECT * FROM TRAIN_T_CLASSTRAINEE WHERE CLASS_TRAINEE_ID='" + classTraineeId + "'";
		classTraineeinfo = thisService.getForValuesToSql(sql);
		String name = String.valueOf(classTraineeinfo.get(0).get("XM"));
		
		String traineePhone=String.valueOf(classTraineeinfo.get(0).get("MOBILE_PHONE"));
		if (Base64Util.isBase64(traineePhone)) {
			traineePhone=Base64Util.decodeData(traineePhone);
		}	
		classTraineeinfo.get(0).put("MOBILE_PHONE", traineePhone);

		List<Map<String, Object>> list = thisService.getClassTraineeCreditsList(classTraineeId);
		// 处理班级基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		int k = 1;
		for (Map<String, Object> list1 : list) {
			traineeMap = new LinkedHashMap<>();
			traineeMap.put("xh", String.valueOf(k++));
			traineeMap.put("className", String.valueOf(list1.get("className")));
			traineeMap.put("courseName", String.valueOf(list1.get("courseName")));
			traineeMap.put("date", String.valueOf(list1.get("courseDate")));
			traineeMap.put("time", String.valueOf(list1.get("courseTime")));
			traineeMap.put("courseCredits", String.valueOf(list1.get("courseCredits")));
			traineeMap.put("realCredits", String.valueOf(list1.get("realCredits")));
			traineeList.add(traineeMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", traineeList);
		courseAllMap.put("headinfo", classTraineeinfo);
		courseAllMap.put("head", new String[] { "序号", "班级名称", "课程名称", "上课日期", "上课时间", "实际学分", "实际学分" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = exportMeetingInfo.exportCourseCreditExcel(response, name + "学分详细信息", name + "学分详细信息", allList);
		if (result == true) {
			request.getSession().setAttribute("exportgetCreditsIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportgetCreditsIsEnd", "0");
			request.getSession().setAttribute("exportgetCreditsIsState", "0");
		}
	}

	/**
	 * 判断导出时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getCreditscheckExportEnd")
	public void getCreditsexportcheckExportRoomEnd(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Object isEnd = request.getSession().getAttribute("exportgetCreditsIsEnd");
		Object state = request.getSession().getAttribute("exportgetCreditsIsState");
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

	@RequestMapping(value = { "/getCheckList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getCheckList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		String xm = request.getParameter("xm");
		String classId = request.getParameter("classId");
		String classScheduleId = request.getParameter("classScheduleId");
		/*
		 * if(StringUtils.isEmpty(classId) &&
		 * StringUtils.isEmpty(classScheduleId)){ //writeJSON(response,
		 * jsonBuilder.returnFailureJson("\"没有传入查询考勤的参数：班级或课程\"")); strData =
		 * jsonBuilder.buildObjListToJson((long)0, new ArrayList<>(), true);
		 * writeJSON(response, strData);// 返回数据 return; }
		 */
		QueryResult<VoTrainClassCheck> qResult = thisService.getCheckList(start, limit, classId, classScheduleId, xm);
		
		for (int i = 0; i < qResult.getResultList().size(); i++) {
			if (Base64Util.isBase64(qResult.getResultList().get(i).getMobilePhone())) {
				qResult.getResultList().get(i)
						.setMobilePhone(Base64Util.decodeData(qResult.getResultList().get(i).getMobilePhone()));
			}		
		}
		
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 导出指定班级的学员学分
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/exportCredit")
	public void doexportCredit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("exportCreditsIsEnd", "0");
		request.getSession().removeAttribute("exportCreditsIsState");

		// 获取班级ID
		String classId = request.getParameter("ids");
		String className = request.getParameter("className");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 10, 30, 30, 20, 20, 20, 20 };
		Integer[] headColumnWidth = new Integer[] { 10, 15, 15, 20, 15, 25, 25, 25, 30 };

		// 数据字典项
		String mapKey = null;
		String[] propValue = { "XBM", "HEADSHIPLEVEL" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}

		// 获取班级所有学员信息
		List<Map<String, Object>> classTraineetList = new ArrayList<>();
		String sql = "select CLASS_TRAINEE_ID,XM,XBM,REAL＿CREDIT,MOBILE_PHONE,WORK_UNIT,POSITION,HEADSHIP_LEVEL,TRAINEE_NUMBER from TRAIN_T_CLASSTRAINEE where (ISDELETE=0 or ISDELETE=2) AND CLASS_ID='"
				+ classId + "' order by xm asc";
		classTraineetList = thisService.getForValuesToSql(sql);

		// 处理班级所有学员数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		List<String> classTraineeIdList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		int j = 1;
		
		String traineePhone="";
		for (Map<String, Object> list : classTraineetList) {
			traineeMap = new LinkedHashMap<>();
			classTraineeIdList.add(String.valueOf(list.get("CLASS_TRAINEE_ID")));

			String headShipLevel = mapDicItem.get(String.valueOf(list.get("HEADSHIP_LEVEL")) + "HEADSHIPLEVEL");
			if (headShipLevel == null)
				headShipLevel = "";

			traineeMap.put("xh", String.valueOf(j++));
			traineeMap.put("xm", String.valueOf(list.get("XM")));
			traineeMap.put("xb", mapDicItem.get(String.valueOf(list.get("XBM")) + "XBM"));
			traineeMap.put("xuehao", String.valueOf(list.get("TRAINEE_NUMBER")));
			traineeMap.put("xf", (String.valueOf(list.get("REAL＿CREDIT"))).equals("null") ? ""
					: String.valueOf(list.get("REAL＿CREDIT")));
			
			traineePhone=String.valueOf(list.get("MOBILE_PHONE"));
			if (Base64Util.isBase64(traineePhone)) {
				traineePhone=Base64Util.decodeData(traineePhone);
			}
			traineeMap.put("phone", traineePhone);
			traineeMap.put("position", String.valueOf(list.get("POSITION")));
			traineeMap.put("headShipLevel", headShipLevel);

			traineeMap.put("workUnit", String.valueOf(list.get("WORK_UNIT")));
			traineeMap.put("classTraineeId", String.valueOf(list.get("CLASS_TRAINEE_ID")));
			traineeList.add(traineeMap);
		}

		// 获取该班级下所有学员的学分成绩
		List<Map<String, Object>> classTrainCreditList = new ArrayList<>();
		sql = "SELECT classTraineeId,className,courseName,courseDate,courseTime,courseCredits,changeCredits,realCredits FROM dbo.TRAIN_V_CLASSTRAINEECREDITS WHERE classId='"
				+ classId + "' order by xm asc";
		classTrainCreditList = thisService.getForValuesToSql(sql);

		// 处理学分数据，按照classTaineeId进行分组
		Map<String, List<Map<String, String>>> traineeCreditMaps = new LinkedHashMap<>();
		Map<String, String> traineeCreditMap = null;
		List<Map<String, String>> traineeCreditList = null;
		for (int i = 0; i < classTraineeIdList.size(); i++) {
			traineeCreditList = new ArrayList<>();
			String classTraineeId = classTraineeIdList.get(i);
			int k = 1;
			for (Map<String, Object> list : classTrainCreditList) {
				traineeCreditMap = new LinkedHashMap<>();
				if (classTraineeId.equals(String.valueOf(list.get("classTraineeId")))) {
					traineeCreditMap.put("xh", String.valueOf(k++));
					traineeCreditMap.put("className", String.valueOf(list.get("className")));
					traineeCreditMap.put("courseName", String.valueOf(list.get("courseName")));
					traineeCreditMap.put("date", String.valueOf(list.get("courseDate")));
					traineeCreditMap.put("time", String.valueOf(list.get("courseTime")));
					traineeCreditMap.put("courseCredits", String.valueOf(list.get("courseCredits")));
					traineeCreditMap.put("realCredits", String.valueOf(list.get("realCredits")));
					traineeCreditList.add(traineeCreditMap);
				}
			}
			traineeCreditMaps.put(classTraineeId, traineeCreditList);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", traineeCreditMaps);
		courseAllMap.put("headinfo", traineeList);
		courseAllMap.put("head", new String[] { "序号", "班级名称", "课程名称", "上课日期", "上课时间", "课程学分", "实际学分" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("headColumnWidth", headColumnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		boolean result = exportMeetingInfo.exportTraineeCreditExcel(response, className + "学分详细信息", className + "学员列表",
				allList);
		if (result == true) {
			request.getSession().setAttribute("exportCreditsIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportCreditsIsEnd", "0");
			request.getSession().setAttribute("exportCreditsIsState", "0");
		}
	}

	/**
	 * 检查学分导出是否完成
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/checkExportCreditEnd")
	public void docheckExportCreditEnd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object isEnd = request.getSession().getAttribute("exportCreditsIsEnd");
		Object state = request.getSession().getAttribute("exportCreditsIsState");
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
	 * 导出班级学员卡的信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCardExcel")
	public void exportCardExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassTraineeCardIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassTraineeCardIIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 6, 10, 6, 15, 10, 10, 10, 15, 10 };

		// 1.班级信息
		String classId = request.getParameter("classId"); // 程序中限定每次只能导出一个班级
		TrainClass trainClass = trainClassService.get(classId);
		String dinnerType = "";
		if (trainClass.getDinnerType() == 1) {
			dinnerType = "围餐";
		} else if (trainClass.getDinnerType() == 2) {
			dinnerType = "自助餐";
		} else {
			dinnerType = "快餐";
		}

		// 数据字典项
		String mapKey = null;
		String[] propValue = { "XBM", "CARDSTATE" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}

		// 2.班级学员信息
		List<TrainClasstrainee> trainClasstraineeList = null;
		String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(classId)) {
			hql += " and classId ='" + classId + "'";
		}
		hql += " order by createTime desc";
		trainClasstraineeList = thisService.getQuery(hql);

		// 处理班级基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		String ClassName = "";
		int i = 1;
		String traineePhone="";
		for (TrainClasstrainee classTrainee : trainClasstraineeList) {
			traineeMap = new LinkedHashMap<>();
			ClassName = classTrainee.getClassName();
			traineeMap.put("xh", i + "");
			traineeMap.put("name", classTrainee.getXm());
			traineeMap.put("xb", mapDicItem.get(classTrainee.getXbm() + "XBM"));
			
			traineePhone=String.valueOf( classTrainee.getMobilePhone());
			if (Base64Util.isBase64(traineePhone)) {
				traineePhone=Base64Util.decodeData(traineePhone);
			}		
			traineeMap.put("phone", traineePhone);
			
			traineeMap.put("stustatus",
					(classTrainee.getIsDelete() == 0) ? "正常" : ((classTrainee.getIsDelete() == 1) ? "取消" : "新增"));
			traineeMap.put("cardPrintNo", classTrainee.getCardPrintId());

			traineeMap.put("dinnerType", dinnerType);
			traineeMap.put("roomName", classTrainee.getRoomName());
			traineeMap.put("isGet", "");

			i++;
			traineeList.add(traineeMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", traineeList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号", "姓名", "性别", "电话", "学员状态", "印刷卡号", "就餐类型", "房间名称", "是否领用" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, ClassName + "-学员卡详请", ClassName + "-学员卡信息", allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsState", "0");
		}
	}

	/**
	 * 判断导出时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportCardEnd")
	public void checkExportCardEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassTraineeCardIIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassTraineeCardIIsState");
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
	 * 绑定卡
	 */
	@RequestMapping("/cardBind")
	public void cardBind(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取班级ID
		String classId = request.getParameter("classId");

		// 获取学员ID
		String ids = request.getParameter("ids");
		if (ids.equals("") == true) {
			// 获取班级学员
			List<TrainClasstrainee> trainClasstraineeList = null;
			String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
			if (StringUtils.isNotEmpty(classId)) {
				hql += " and classId ='" + classId + "'";
			}
			hql += " order by createTime desc";
			trainClasstraineeList = thisService.getQuery(hql);
			int trainClasstraineeListCount = trainClasstraineeList.size();

			for (int i = 0; i < trainClasstraineeListCount; i++) {
				ids += trainClasstraineeList.get(i).getUuid() + ",";
			}

			ids = ids.substring(0, ids.length() - 1);
		}

		// 判断原先是否已经有学员已经绑定
		String sql = "select Count(CARD_ID) from CARD_T_USEINFO where USER_ID in ('" + ids.replace(",", "','") + "')";
		int count = thisService.getForValueToSql(sql);
		if (count != 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"所选学员已经有部分绑定！\""));
			return;
		}

		String[] idsArray = ids.split(",");
		int idsCount = idsArray.length;
		// 获取未绑定卡的数量
		sql = "select Count(CARD_ID) from CARD_T_USEINFO where CARD_TYPE_ID=3 AND USER_ID IS NULL AND USE_STATE !=2 and USE_STATE !=3 ";
		count = thisService.getForValueToSql(sql);
		// 判断空闲卡数量
		if (count < idsCount) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"绑定失败，没有足够的空闲卡!<br/><br/>剩余可用卡 " + count + " 张！\""));
			return;
		}
		List<Map<String, Object>> cardInfoToUp = thisService.doCardBind(ids);

		if (cardInfoToUp == null) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"绑定失败，服务器错误！\""));
			return;
		} else if (cardInfoToUp.size() == 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"绑定失败，没有足够的空闲卡！\""));
			return;
		}

		SysUser sysuser = getCurrentSysUser();
		try {
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			thisService.doSyncBindToUP(cardInfoToUp, sysuser.getXm());

		} catch (Exception e) {
			// 恢复web数据
			DBContextHolder.clearDBType();
			thisService.doRestoreCardBind(cardInfoToUp);

			writeJSON(response, jsonBuilder.returnFailureJson("\"绑定失败，服务器错误！\""));
			return;
		} finally {
			DBContextHolder.clearDBType();
		}

		writeJSON(response, jsonBuilder.returnSuccessJson("\"绑定成功！\""));

	}

	/**
	 * 解除绑定卡（将 状态为1 和 2 的卡片数据与用户解绑）
	 */
	@RequestMapping("/cardUnBind")
	public void cardUnBind(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取班级ID
		String classId = request.getParameter("classId");
		// 获取学员ID
		String ids = request.getParameter("ids");

		SysUser sysuser = getCurrentSysUser();

		// String[] idsArray = ids.split(",");
		// int idsCount = idsArray.length;

		if (ids.equals("") == true) {
			// 获取班级学员
			List<TrainClasstrainee> trainClasstraineeList = null;
			String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
			if (StringUtils.isNotEmpty(classId)) {
				hql += " and classId ='" + classId + "'";
			}
			hql += " order by createTime desc";
			trainClasstraineeList = thisService.getQuery(hql);
			int trainClasstraineeListCount = trainClasstraineeList.size();

			for (int i = 0; i < trainClasstraineeListCount; i++) {
				ids += trainClasstraineeList.get(i).getUuid() + ",";
			}
			ids = ids.substring(0, ids.length() - 1);
		}

		// 使用两个异常处理器来防止数据错乱。
		String sql = "";
		try {
			// 正常发卡的解绑，把状态置为4，挂失类的卡片信息，不用更改状态
			sql = "update CARD_T_USEINFO SET USE_STATE=4  WHERE USE_STATE=1 and USER_ID in ('" + ids.replace(",", "','")
					+ "');";

			thisService.doExecuteSql(sql);
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"解除绑定失败！\""));
			return;
		}

		try {
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			thisService.doSyncUnBindToUP(ids, sysuser.getXm());

		} catch (Exception e) {
			// 恢复web数据
			DBContextHolder.clearDBType();
			sql = "update CARD_T_USEINFO SET USE_STATE=1 WHERE USE_STATE=4 and USER_ID in ('" + ids.replace(",", "','")
					+ "')";
			thisService.doExecuteSql(sql);

			writeJSON(response, jsonBuilder.returnFailureJson("\"解除绑定失败！\""));
			return;
		} finally {
			DBContextHolder.clearDBType();
		}

		// 当没有报错的时候，再把用户id清空(分别把正常解绑 和 挂失解绑 的数据处理)
		sql = "update CARD_T_USEINFO SET USER_ID=NULL  WHERE USE_STATE=4 and USER_ID in ('" + ids.replace(",", "','")
				+ "');";
		sql += "update CARD_T_USEINFO SET USER_ID=NULL  WHERE USE_STATE=2 and USER_ID in ('" + ids.replace(",", "','")
				+ "')";
		thisService.doExecuteSql(sql);

		writeJSON(response, jsonBuilder.returnSuccessJson("\"解除绑定成功！\""));
	}

	/**
	 * 挂失卡
	 */
	@RequestMapping("/cardLose")
	public void cardLose(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取班级ID
		String classId = request.getParameter("classId");

		// 获取学员ID
		String ids = request.getParameter("ids");
		String[] idsArray = ids.split(",");
		int idsCount = idsArray.length;

		List<Map<String, Object>> cardUnBindList = new ArrayList<>();
		String sql = "select * from CARD_T_USEINFO where USER_ID='" + ids + "'";
		cardUnBindList = thisService.getForValuesToSql(sql);
		int cardUnBindListCount = cardUnBindList.size();
		if (cardUnBindListCount == 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"此学员未绑定卡！\""));
			return;
		} else {
			// 保存原始数据
			String oldUseState = String.valueOf(cardUnBindList.get(0).get("USE_STATE"));
			String cardId = String.valueOf(cardUnBindList.get(0).get("UP_CARD_ID"));

			// 使用两个异常处理器来防止数据错乱。
			try {
				// sql = "update CARD_T_USEINFO SET USE_STATE='2' ,USER_ID=NULL
				// WHERE USER_ID='" + ids + "'";
				sql = "update CARD_T_USEINFO SET USE_STATE='2' WHERE USER_ID='" + ids + "'";
				thisService.doExecuteSql(sql);
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"挂失失败！\""));
				return;
			}

			try {
				DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
				sql = "UPDATE tc_card SET CardStatusIDXF=2,CardStatusIDJS=2 WHERE CARDID =" + cardId;
				thisService.doExecuteSql(sql);

			} catch (Exception e) {
				// 恢复web数据
				DBContextHolder.clearDBType();
				sql = "update CARD_T_USEINFO SET USE_STATE='" + oldUseState + "' WHERE USER_ID='" + ids + "'";
				thisService.doExecuteSql(sql);

				writeJSON(response, jsonBuilder.returnFailureJson("\"挂失失败！\""));
				return;
			} finally {
				DBContextHolder.clearDBType();
			}

		}
		writeJSON(response, jsonBuilder.returnSuccessJson("\"挂失成功！\""));
	}

}
