
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ExportExcel;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.core.util.exportMeetingInfo;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TrainClassscheduleController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 班级课程日历(TRAIN_T_CLASSSCHEDULE)实体Controller. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainClassschedule")
public class TrainClassscheduleController extends FrameWorkController<TrainClassschedule> implements Constant {

	@Resource
	TrainClassscheduleService thisService; // service层接口

	@Resource
	BaseDicitemService dicitemService;

	@Resource
	TrainClassService classService; // service层接口

	@Resource
	TrainClasstraineeService classTraineeService; // service层接口

	/**
	 * @param entity
	 *            实体类
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             设定参数
	 * @Title: list
	 * @Description: 查询数据列表
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute TrainClassschedule entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<TrainClassschedule> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doadd")
	public void doAdd(TrainClassschedule entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {

			String teachType = request.getParameter("teachType");
			String courseDate = request.getParameter("courseDate");
			String courseBeginTime = request.getParameter("courseBeginTime");
			String courseEndTime = request.getParameter("courseEndTime");

			entity.setBeginTime(sdf.parse(courseDate + " " + courseBeginTime));
			entity.setEndTime(sdf.parse(courseDate + " " + courseEndTime));

			// 判断房间是否被其他课程使用
			// if(StringUtils.isNotEmpty(entity.getRoomId())){
			// String roomIdArray[]=entity.getRoomId().split(",");
			// String id="";
			// String roomId=null;
			// for(int j=0;j<roomIdArray.length;j++){
			// roomId=roomIdArray[j];
			// List lists = thisService.doQuerySql("EXECUTE TRAIN_P_ISUSESITE
			// '"+id+"','"+roomId+"'");
			// if("0".equals(lists.get(0).toString())){
			// writeJSON(response,
			// jsonBuilder.returnSuccessJson("\"该教室在此时间段内，已被其他课程使用！\""));
			// return;
			// }
			// }
			// }

			entity = thisService.doAddEntity(entity, teachType, currentUser);// 执行增加方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 * @Title: doDelete
	 * @Description: 逻辑删除指定的数据
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
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(TrainClassschedule entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {

			String courseId = entity.getCourseId();

			String hql1 = " o.isDelete!=1 and o.classId='" + entity.getClassId() + "'";
			if (thisService.IsFieldExist("courseId", courseId, entity.getUuid(), hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"班级课程不能重复！\""));
				return;
			}

			String teachType = request.getParameter("teachType");
			String courseDate = request.getParameter("courseDate");
			String courseBeginTime = request.getParameter("courseBeginTime");
			String courseEndTime = request.getParameter("courseEndTime");

			entity.setBeginTime(sdf.parse(courseDate + " " + courseBeginTime));
			entity.setEndTime(sdf.parse(courseDate + " " + courseEndTime));

			entity = thisService.doUpdateEntity(entity, teachType, currentUser);// 执行修改方法
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

			InputStream in = null;
			List<List<Object>> listObject = null;
			List<ImportNotInfo> listReturn;

			if (!file.isEmpty()) {
				in = file.getInputStream();
				listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
				in.close();

				listReturn = thisService.doImportData(listObject, classId, currentUser);

				if (listReturn.size() == 0)
					writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
				else {
					String strData = jsonBuilder.buildList(listReturn, "");
					request.getSession().setAttribute("TrainClassScheduleImportError", strData);
					writeJSON(response, jsonBuilder.returnSuccessJson("-1")); // 返回前端-1，表示存在错误数据
				}

			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
			}

		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
		}
	}

	@RequestMapping(value = { "/downNotImportInfo" })
	public void downNotImportInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object obj = request.getSession().getAttribute("TrainClassScheduleImportError");
		if (obj != null) {

			request.getSession().removeAttribute("TrainClassScheduleImportError");// 移除此session

			String downData = (String) obj;

			List<ImportNotInfo> list = (List<ImportNotInfo>) jsonBuilder.fromJsonArray(downData, ImportNotInfo.class);
			ExportExcel excel = new ExportExcel();

			String[] Title = { "序号", "课程名", "异常级别", "异常原因" };
			Integer[] coulumnWidth = { 8, 20, 20, 100 };
			Integer[] coulumnDirection = { 1, 1, 1, 1 };

			List<String> excludeList = new ArrayList<>();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
			String fileNAME = "（" + sdf2.format(new Date()) + "导出）导入班级课程的异常信息名单";

			excel.exportExcel(response, fileNAME, Title, list, excludeList, coulumnWidth, coulumnDirection);
		}
	}

	/**
	 * 更新课程的场地信息
	 *
	 * @throws InterruptedException
	 */
	@RequestMapping("/updateRoomInfo")
	public void updateRoomInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException, InterruptedException {

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		String roomIds = request.getParameter("roomIds");
		String roomNames = request.getParameter("roomNames");
		String ids = request.getParameter("ids");

		int result = thisService.doUpdateRoomInfo(roomIds, roomNames, ids, currentUser);

		if (result == 1) {
			writeJSON(response, jsonBuilder.returnSuccessJson("\"设置场地成功！\""));
		} else if (result == 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"该教室在此时间段内，已被其他课程使用！\""));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
	}

	/**
	 * 取消课程的场地信息
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
			writeJSON(response, jsonBuilder.returnSuccessJson("\"取消场地成功！\""));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 * @Title: doDelete
	 * @Description: 更新是否评价
	 */
	@RequestMapping("/doUpdateEval")
	public void updateEval(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			String id = request.getParameter("id");
			String val = request.getParameter("val");

			SysUser currentUser = getCurrentSysUser();

			TrainClassschedule trainClassschedule = thisService.get(id);
			trainClassschedule.setIsEval(Integer.parseInt(val));
			trainClassschedule.setUpdateTime(new Date());
			trainClassschedule.setUpdateUser(currentUser.getXm());

			thisService.merge(trainClassschedule);

			writeJSON(response, jsonBuilder.returnSuccessJson("\"设置成功！\""));

		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请重试或联系管理员！\""));
		}
	}

	@RequestMapping("/listCourseEval")
	public void listCourseEval(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = "";
		String propName = request.getParameter("propName");
		String propValue = request.getParameter("propValue");
		String joinMethod = request.getParameter("joinMethod");
		Integer start = super.start(request);
		Integer limit = super.limit(request);

		String sql = "SELECT classId,classCategory,className,courseDate,courseTime,convert(varchar(10),verySatisfaction) as verySatisfaction"
				+ ",convert(varchar(10),satisfaction) as satisfaction,ranking,teacherId,teacherName,courseId,courseName,classScheduleId,evalState "
				+ ",teachTypeName FROM TRAIN_V_CLASSCOURSEEVAL ";
		if (StringUtils.isNotEmpty(propName)) {
			String[] name = propName.split(",");
			String[] value = propValue.split(",");
			StringBuilder sb = new StringBuilder(" where ");
			if ("or".equals(joinMethod)) {
				for (int i = 0; i < name.length; i++) {
					sb.append(name[i]);
					sb.append(" like '%");
					sb.append(value[i]);
					sb.append("%' or ");
				}
				sb = sb.delete(sb.length() - 3, sb.length());
			} else {
				for (int i = 0; i < name.length; i++) {
					sb.append(name[i]);
					sb.append(" = '");
					sb.append(value[i]);
					sb.append("' and ");
				}
				sb = sb.delete(sb.length() - 4, sb.length());
			}

			whereSql = sb.toString();
		}
		if (whereSql.length() > 0) {
			sql += whereSql;
		}
		QueryResult<TrainClassCourseEval> list = thisService.doQueryResultSqlObject(sql, start, limit,
				TrainClassCourseEval.class);
		strData = jsonBuilder.buildObjListToJson(list.getTotalCount(), list.getResultList(), true);

		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping("/listClassEvalCourse")
	public void listClassEvalCourse(String classId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strData = "";
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String orderSql = super.orderSql(request);

		String sql = "SELECT classId,classCategory,className,courseDate,courseTime,convert(varchar(10),verySatisfaction) as verySatisfaction"
				+ ",convert(varchar(10),satisfaction) as satisfaction,ranking,teacherId,teacherName,courseId,courseName,classScheduleId,evalState "
				+ ",teachTypeName FROM TRAIN_V_CLASSCOURSEEVAL where classId=''{0}'' ";
		sql = MessageFormat.format(sql, classId);
		if (StringUtils.isNotEmpty(orderSql)) {
			sql += orderSql;
		}
		QueryResult<TrainClassCourseEval> list = thisService.doQueryResultSqlObject(sql, start, limit,
				TrainClassCourseEval.class);
		strData = jsonBuilder.buildObjListToJson(list.getTotalCount(), list.getResultList(), true);

		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 导出班级部分课程的考勤信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCourseAttendExcel")
	public void exportCourseCheckExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportCourseAttendIsEnd", "0");
		request.getSession().removeAttribute("exportCourseAttendIsState");

		String classId = request.getParameter("classId"); // 一门或多门班级课程
		String ids = request.getParameter("ids"); // 一门或多门班级课程

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		Map<String, String> mapClassCategory = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('ZXXBJLX')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
		}

		Integer[] columnWidth = new Integer[] { 15, 15, 15, 15, 15, 15, 15, 15, 15 };

		// 1.班级信息
		TrainClass trainClass = classService.get(classId);

		// 2.班级学员信息
		List<TrainClasstrainee> trainClasstraineeList = null;
		String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		String sql = "";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId in ('" + classId + "')";
		}
		hql += " order by xm asc";
		trainClasstraineeList = classTraineeService.doQuery(hql);

		// 处理学员基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		int traineeNum = 0; // 学员人数
		for (TrainClasstrainee classTrainee : trainClasstraineeList) {
			traineeMap = new LinkedHashMap<>();
			traineeMap.put("xm", classTrainee.getXm());
			traineeNum++;
			// 只统计不为1的数据
			traineeList.add(traineeMap);
		}

		// 3.班级课程信息
		List<Map<String, Object>> trainClassscheduleList = null;
		sql = "SELECT CLASS_SCHEDULE_ID,COURSE_NAME,BEGIN_TIME,END_TIME FROM TRAIN_T_CLASSSCHEDULE where ISDELETE=0";
		sql += " and CLASS_SCHEDULE_ID in ('" + ids.replace(",", "','") + "')";
		sql += " order by CLASS_SCHEDULE_ID asc";
		trainClassscheduleList = thisService.getForValuesToSql(sql);

		// 4.班级课程考勤信息信息(最新加入 是否请假、备注)
		List<Map<String, Object>> voTrainClassCheckList = null;
		List<String> classScheduleIdList = new ArrayList<>();
		sql = "SELECT xm,classTraineeId,incardTime,outcardTime,attendResult,attendMinute,isleave,remark FROM TRAIN_V_CHECKRESULT WHERE classScheduleId in ('"
				+ ids.replace(",", "','") + "') order by classScheduleId asc";
		voTrainClassCheckList = thisService.getForValuesToSql(sql);
		int voTrainClassCheckListCount = voTrainClassCheckList.size();

		Map<String, List<String>> traineeResult = new LinkedHashMap<>();
		List<String> lists = null;
		Map<String, Object> tcc = null;
		for (int i = 0; i < voTrainClassCheckListCount; i++) {
			tcc = voTrainClassCheckList.get(i);
			lists = traineeResult.get(tcc.get("classTraineeId"));
			if (lists == null) {
				lists = new ArrayList<>();
				lists.add(String.valueOf(i + 1));
				lists.add(String.valueOf(tcc.get("xm")));
			}
			String incardTime = String.valueOf(tcc.get("incardTime"));
			if (incardTime.equals("null") == false) {
				incardTime = incardTime.substring(11, incardTime.lastIndexOf("."));
			}
			String outcardTime = String.valueOf(tcc.get("outcardTime"));
			if (outcardTime.equals("null") == false) {
				outcardTime = outcardTime.substring(11, outcardTime.lastIndexOf("."));
			}
			lists.add(incardTime);
			lists.add(outcardTime);
			lists.add(String.valueOf(tcc.get("attendMinute")));
			lists.add(String.valueOf(tcc.get("attendResult")));
			if("1".equals(tcc.get("isleave")))
				lists.add("是");
			else
				lists.add("否");
			lists.add(String.valueOf(tcc.get("remark")));

			traineeResult.put(String.valueOf(tcc.get("classTraineeId")), lists);
		}
		// System.out.println(traineeResult);

		// ----------------------组装导出数据----------------------------------
		List<Map<String, Object>> allList = new ArrayList<>();

		// --------1.处理班级基本数据，并组装表格数据（特殊：一行分作两行显示）
		List<Map<String, Object>> classList1 = new ArrayList<>(); // 虽然内容只有一行，但是由于接口的设定，所以依旧使用list存入数据
		Map<String, Object> classMap1 = new LinkedHashMap<>();
		classMap1.put("className", trainClass.getClassName());
		classMap1.put("classCategory", mapClassCategory.get(trainClass.getClassCategory()));
		classMap1.put("traineeNum", String.valueOf(traineeNum));
		classMap1.put("beginDate",
				fmtDate.format(trainClass.getBeginDate()) + " " + fmtTime.format(trainClass.getBeginDate()));
		classMap1.put("endDate",
				fmtDate.format(trainClass.getEndDate()) + " " + fmtTime.format(trainClass.getEndDate()));
		classMap1.put("needChecking", trainClass.getNeedChecking() == 1 ? "需要" : "不需要");
		classMap1.put("checkCourse", trainClassscheduleList);
		classMap1.put("voTrainClassCheck", traineeResult);
		classList1.add(classMap1);
		// 第一行数据
		Map<String, Object> classAllMap1 = new LinkedHashMap<>();
		classAllMap1.put("data", classList1);
		classAllMap1.put("title", "班级课程考勤信息表");
		classAllMap1.put("columnWidth", 15);
		classAllMap1.put("headColumnCount", 8);
		allList.add(classAllMap1);

		// 在导出方法中进行解析
		boolean result = exportMeetingInfo.exportCouseCheckResultInfoExcel(response, trainClass.getClassName() + "班级信息",
				trainClass.getClassName() + "班课程考勤表", allList);
		if (result == true) {
			request.getSession().setAttribute("exportCourseAttendIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportCourseAttendIsEnd", "0");
			request.getSession().setAttribute("exportCourseAttendIsState", "0");
		}

	}

	/**
	 * 判断是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkCourseAttendExcelEnd")
	public void checkCourseCheckExcelEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportCourseAttendIsEnd");
		Object state = request.getSession().getAttribute("exportCourseAttendIsState");
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
