
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.*;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.vo.VoTrainClassCheck;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	@Resource
	TrainClasstraineeService thisService; // service层接口
	
	@Resource
	TrainClassService trainClassService; // service层接口

	@Resource
	SysUserService sysUserService; // service层接口
	
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
		String classId=request.getParameter("classId");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			try {
				boolean flag = thisService.doLogicDeleteByIds(classId,delIds, currentUser);
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
			if (!file.isEmpty()) {
				in = file.getInputStream();
				listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
				in.close();

				thisService.doImportTrainee(listObject, classId, needSync, currentUser);

			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
			}

			writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
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
			
			//g.isDelete=0 and  查询所有状态的
			//new 不查询状态为1的
			String hql = "select  new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.breakfast,g.lunch,g.dinner,g.isDelete) from TrainClasstrainee g "
					+ " where g.classId='" + classId + "'";
			if(isDelete!=null){
				hql+=" and g.isDelete!=1 ";
			}
			hql+=" order by g.breakfast desc,g.lunch desc,g.dinner desc";
			
			QueryResult<TrainClasstrainee> result = thisService.doQueryResult(hql, start, limit);

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
			
			//g.isDelete=0 or g.isDelete=1 and 
			//new: 不查询状态为1的
			String hql = "select new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.siesta,g.sleep,g.roomId,g.roomName,g.isDelete) from TrainClasstrainee g "
					+ " where g.classId='"+ classId + "'";
			if(isDelete!=null){
				hql+=" and g.isDelete!=1 ";
			}
			hql+="  order by g.siesta desc,g.sleep desc,g.xbm asc";
				
			QueryResult<TrainClasstrainee> result = thisService.doQueryResult(hql, start, limit);

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
		int result = thisService.doUpdateRoomInfo(roomId, roomName, ids, xbm, currentUser);

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
			//设置部门ID：Train20170505（Train+班级编号）【班级编号不可变化】
			String departmentId = "Train" + trainClass.getClassNumb();
			
			// 1.切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_Five);

			// 2.查询UP中的发卡信息(查询班级学员的卡片信息)
			String sql = "select convert(varchar,a.CardID) as upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as userId,"
					+ " convert(int,a.CardStatusIDXF) as useState,"
					+ " b.SID as sid,b.EmployeeStatusID as employeeStatusID "
					+ " from Tc_Employee b join TC_Card a on b.CardID=a.CardID"
					+ " where b.DepartmentID='"+departmentId+"'"
					+ "	order by a.CardID asc,a.ModifyDate asc";
			

			List<CardUserInfoToUP> upCardUserInfos = thisService.doQuerySqlObject(sql, CardUserInfoToUP.class);

			// 3.恢复数据源
			DBContextHolder.clearDBType();

			int row = 0;
			if (upCardUserInfos.size() > 0) {
				row = sysUserService.syncClassCardInfoFromUp(upCardUserInfos,classId);
			} else {
				row = sysUserService.syncClassCardInfoFromUp(null,classId);
			}

			if (row == 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"此班级学员已是最新发卡数据！\"}");
			} else if (row > 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"更新发卡数据成功！\"}");
			} else {
				returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"更新发卡数据失败，请联系管理员！\"}");
			}

		} catch (Exception e) {
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
		trainClasstraineeList = thisService.doQuery(hql);
		
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
    public void doGetCreditsDetail(HttpServletRequest request,HttpServletResponse response) throws  IOException{
        String strData = "";
	    String classId = request.getParameter("classId");
        String classTraineeId = request.getParameter("ids");
        List<Map<String, Object>> list = thisService.getClassTraineeCreditsList(classTraineeId);
        strData = jsonBuilder.buildObjListToJson((long) list.size(), list, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    @RequestMapping(value = { "/getCheckList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getCheckList( HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        String classId = request.getParameter("classId");
        String classScheduleId = request.getParameter("classScheduleId");
        if(StringUtils.isEmpty(classId) && StringUtils.isEmpty(classScheduleId)){
            writeJSON(response, jsonBuilder.returnFailureJson("'没有传入查询考勤的参数：班级或课程'"));
            return;
        }
        QueryResult<VoTrainClassCheck> qResult = thisService.getCheckList(start, limit, classId, classScheduleId);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
}
