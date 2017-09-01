package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.DBContextHolder;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.core.util.exportMeetingInfo;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClassrealdinnerService;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * ClassName: TrainClassController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 培训开班信息(TRAIN_T_CLASS)实体Controller. date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainClass")
public class TrainClassController extends FrameWorkController<TrainClass> implements Constant {

	private static Logger logger = Logger.getLogger(TrainClassController.class);

	@Resource
	TrainClassService thisService; // service层接口
	
	@Resource
	TrainClasstraineeService classTraineeService; // service层接口

	@Resource
	TrainClassscheduleService classScheduleSerive;
	
	@Resource
	TrainClasstraineeService trainClasstraineeService;

	@Resource
	BaseDicitemService dicitemService;

	@Resource
	TrainClassrealdinnerService classrealdinnerService; // service层接口
	
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
	public void list(@ModelAttribute TrainClass entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<TrainClass> qResult = thisService.list(start, limit, sort, filter, true);
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
	@RequestMapping(value = { "/doadd" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void doAdd(TrainClass entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			String hql1 = " o.isDelete='0'";
			/*
			 * if (thisService.IsFieldExist("classNumb", entity.getClassNumb(),
			 * "-1", hql1)) { writeJSON(response,
			 * jsonBuilder.returnFailureJson("\"班级编号不能重复！\"")); return; }
			 */
			if (thisService.IsFieldExist("className", entity.getClassName(), "-1", hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"班级名称不能重复！\""));
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
	 *
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(TrainClass entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {

			String hql1 = " o.isDelete='0'";
			TrainClass saveEntity = thisService.get(entity.getUuid());
			if (saveEntity.getIsarrange() != null && saveEntity.getIsarrange() == 1) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"此班级信息已经被安排，不可再修改！\""));
				return;
			}

			/*
			 * if (thisService.IsFieldExist("classNumb", entity.getClassNumb(),
			 * entity.getUuid(), hql1)) { writeJSON(response,
			 * jsonBuilder.returnFailureJson("\"班级编号不能重复！\"")); return; }
			 */

			if (thisService.IsFieldExist("className", entity.getClassName(), entity.getUuid(), hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"班级名称不能重复！\""));
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
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getClassInfo")
	public void getClassInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String id = request.getParameter("classId");
		if (StringUtils.isNotEmpty(id)) {
			HashMap<String, String> classInfo = thisService.getClassInfo(id);
			strData = jsonBuilder.toJson(classInfo);// 处理数据
			writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"无数据\""));// 返回数据
		}
	}

	/**
	 * 描述：通过传统方式form表单提交方式导入excel文件 导入班级（现已作废，前端暂无使用）
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/importData", method = { RequestMethod.GET, RequestMethod.POST })
	public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			InputStream in = null;
			List<List<Object>> listObject = null;
			if (!file.isEmpty()) {
				in = file.getInputStream();
				listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
				in.close();

				/**
				 * 格式 第一行为列头【班级名称 班级类型 CLASSCATEGORY数据字典
				 * 班级编号，原则上根据开班的年份+班级类型编码+3位顺序号 是否考勤 0-不需要，1-需要
				 * 是否同步学员0:I不同步,1:同步 班级简介 开始日期 结束日期 班主任Name】 第二行开始为数据 【班级1 普通班
				 * 11231 0 0 打算阿大使 2017/4/1 2018/4/4 阿杜 】
				 */
				for (int i = 0; i < listObject.size(); i++) {
					List<Object> lo = listObject.get(i);

					TrainClass trainClass = new TrainClass();

					trainClass.setClassName(String.valueOf(lo.get(0)));
					trainClass.setClassCategory(String.valueOf(lo.get(1)));
					trainClass.setClassNumb(String.valueOf(lo.get(2)));

					trainClass.setNeedChecking(Short.parseShort(lo.get(3).toString()));
					trainClass.setNeedSynctrainee(Short.parseShort(lo.get(4).toString()));

					trainClass.setClassDesc(String.valueOf(lo.get(5)));
					trainClass.setBeginDate(sdf.parse(String.valueOf(lo.get(6))));
					trainClass.setEndDate(sdf.parse(String.valueOf(lo.get(7))));
					trainClass.setBzrName(String.valueOf(lo.get(8)));

					thisService.merge(trainClass);

				}
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
			}

			writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
		}
	}

	/**
	 * NEW：当班级已经设置为提交状态后，则再次修改，会将isuse设置为2（修改未提交状态）
	 * 
	 * @Title: doEditClassFood
	 * @Description: 编辑班级学员用餐申请信息
	 */
	@RequestMapping(value = { "/doEditClassFood" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void doEditClassFood(TrainClass entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		try {
			SysUser currentUser = getCurrentSysUser();

			String classFoodInfo = request.getParameter("classFoodInfo");

			int result = thisService.doEditClassFood(entity, classFoodInfo, currentUser);

			if (result == 1) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"就餐申请设置成功！\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"设置失败，请重试或联系管理员\""));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}

	}

	/**
	 * NEW：当班级已经设置为提交状态后，则再次修改，会将isuse设置为2（修改未提交状态）
	 * 
	 * @Title: doEditClassRoom
	 * @Description: 编辑班级学员住宿申请信息
	 */
	@RequestMapping(value = { "/doEditClassRoom" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void doEditClassRoom(@ModelAttribute TrainClass entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		try {
			SysUser currentUser = getCurrentSysUser();

			String classRoomInfo = request.getParameter("classRoomInfo");

			int result = thisService.doEditClassRoom(entity, classRoomInfo, currentUser);

			if (result == 1) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"住宿申请设置成功！\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"设置失败，请重试或联系管理员\""));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
	}

	/**
	 * 启用班级（启用之后，会将班级名称同步到UP中的部门库中，以及将班级学员数据也同步到UP人员库中） 当同步失败后，数据会回滚，以下代码重置，
	 * NEW：可重复提交，当提交之后，并且再修改了内容，则会再次提交（isuse：0置为1；2置为3）
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doClassUse")
	public void doClassUse(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		int state = 0;
		int result = -1;
		String classId = null;
		TrainClass trainClass = null;
		Integer isuse = null;
		try {
			// 获取当前的操作用户
			SysUser currentUser = getCurrentSysUser();
			classId = request.getParameter("classId");

			// 1.查询班级信息
			trainClass = thisService.get(classId);

			isuse = trainClass.getIsuse();
			if (trainClass.getIsuse() != null && (trainClass.getIsuse() == 1 || trainClass.getIsuse() == 3)) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"此班级已经提交，不可重复提交！\""));
				state = 1;
				result = 0;
				return;
			}

			// 2.将班级设置为启用状态；（若为0，则置为1，若为2则置为3）
			isuse = trainClass.getIsuse();
			if (isuse == null || isuse == 0)
				trainClass.setIsuse(1);
			else if (isuse == 2)
				trainClass.setIsuse(3);
			trainClass.setUpdateTime(new Date());
			trainClass.setUpdateUser(currentUser.getXm());
			thisService.update(trainClass);
			state = 1; // 执行到这里，表明设置班级启用状态成功

			// 3.设置部门ID：Train20170505（Train+班级编号）【班级编号不可变化】
			String departmentId = "Train" + trainClass.getClassNumb();

			// 4.查询班级的学员信息【查询班级学员的最新信息】，【New】identifier字段也存放为身份证件号字段
			String sql = "select CLASS_TRAINEE_ID as userId,XM as employeeName,SFZJH as employeeStrId,"
					+ "	'' as employeePwd,CASE XBM WHEN '2' THEN '0' ELSE '1' END AS sexId,"
					+ " isDelete as isDelete,SFZJH AS identifier,'1' AS cardState, " + " '' as sid,'" + departmentId
					+ "' as departmentId,'学员' as jobName " + " from TRAIN_T_CLASSTRAINEE " + " where CLASS_ID='" + classId + "'"
					+ " order by CLASS_TRAINEE_ID asc";

			List<SysUserToUP> userInfos = thisService.doQuerySqlObject(sql, SysUserToUP.class);

			// 5.切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_Five);

			// 6.执行同步代码
			result = thisService.syncTraineeClassInfoToAllUP(departmentId, trainClass, userInfos);

			if (result != -1) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"提交班级信息成功！\""));
			} else {
				// 若执行到这里，表明没有同步成功；所以，要把启用状态再设置为0；
				// thisService.updateByProperties("uuid", classId,"isuse",0);
				writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
			}

		} catch (Exception e) {
			// 若此值为1，表明设置启用成功，但是同步失败
			// if(state==1&&result==0){
			// thisService.updateByProperties("uuid", classId,"isuse",0);
			// }
			logger.error(e.getMessage());
			writeJSON(response, jsonBuilder.returnSuccessJson("\"请求失败，请联系管理员！\""));
		} finally {
			DBContextHolder.clearDBType();

			// 当设置启用状态 和 同步到UP都执行成功后，再提交此更改。
			if (state == 0 || result == -1) {
				trainClass.setIsuse(0);
				thisService.update(trainClass);
			}
		}
	}

	/**
	 * 安排班级信息（安排之后，会将班级的学员就餐信息同步到UP中） 当同步失败后，数据会回滚，以下代码重置，
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doClassArrange")
	public void doClassArrange(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		int state = 0;
		int result = -1;
		String classId = null;
		TrainClass trainClass = null;
		SysUser currentUser = null;
		try {
			// 获取当前的操作用户
			currentUser = getCurrentSysUser();
			classId = request.getParameter("classId");

			// 1.查询班级信息
			trainClass = thisService.get(classId);

			// if(trainClass.getIsarrange()!=null&&trainClass.getIsarrange()==1&&trainClass.getIsuse()==1){
			// writeJSON(response,
			// jsonBuilder.returnSuccessJson("\"此班级已经被安排，不可重复安排！\""));
			// state = 1;
			// result = 0;
			// return;
			// }
			// // 2.将班级设置为启用状态
			// trainClass.setIsarrange(1);
			// trainClass.setUpdateTime(new Date());
			// trainClass.setUpdateUser(currentUser.getXm());
			// thisService.update(trainClass);
			// state = 1; // 执行到这里，表明设置班级安排状态成功

			// 生成班级就餐登记数据,当类型不为快餐的时候
			if (trainClass.getDinnerType() != null && trainClass.getDinnerType() != 3)
				classrealdinnerService.createClassRecord(trainClass, currentUser);

			// 4.判断是否需要进行同步，仅当就餐类型为快餐的时候，才需要进行学员就餐信息的同步【处理重复安排导致的餐券问题，不重复生成此班级的餐券类型】
			if (trainClass.getDinnerType() == null || trainClass.getDinnerType() == 3) {
				// 查询班级学员就餐信息
				// 4.查询班级的学员信息 (new:只查询出正常状态的学员，不再删除餐券数据)
				String hql = "select new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.breakfast,g.lunch,g.dinner,g.isDelete) from TrainClasstrainee g "
						+ "where g.isDelete!=1 and g.classId='"
						+ classId + "' order by g.breakfast desc,g.lunch desc,g.dinner desc";
				List<TrainClasstrainee> traineeFoods = classTraineeService.doQuery(hql);

				if (traineeFoods.size() > 0) {
					// 5.切换数据源
					DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_Five);

					// 6.执行同步代码
					result = thisService.syncClassTraineeFoodsToUP(trainClass, traineeFoods);
				} else {
					result = 0;
				}
			} else {
				result = 0;
			}

			if (result != -1) {

				writeJSON(response, jsonBuilder.returnSuccessJson("\"安排班级成功！\""));
			} else {
				// 若执行到这里，表明没有同步成功；所以，要把启用状态再设置为0；
				// thisService.updateByProperties("uuid", classId,"isuse",0);
				writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
			}

		} catch (Exception e) {
			// 若此值为1，表明设置启用成功，但是同步失败
			// if(state==1&&result==0){
			// thisService.updateByProperties("uuid", classId,"isuse",0);
			// }
			logger.error(e.getMessage());
			writeJSON(response, jsonBuilder.returnSuccessJson("\"请求失败，请联系管理员！\""));
		} finally {
			DBContextHolder.clearDBType();

			// 当设置启用状态 和 同步到UP都执行成功后，再提交此更改。
			if (result != -1) {

				// 将此班级的学员和课程isdelete状态为2的，设置回0；
				String updateSql = "update TrainClasstrainee set isDelete=0 where isDelete=2 and classId='" + classId
						+ "'";
				classTraineeService.executeHql(updateSql);
				updateSql = "update TrainClassschedule set isDelete=0 where isDelete=2 and classId='" + classId + "'";
				classScheduleSerive.executeHql(updateSql);

				// 当isuse为3的时候，执行后，设置回1；
				if (trainClass.getIsuse() == 3)
					trainClass.setIsuse(1);

				// 将班级设置为启用状态
				trainClass.setIsarrange(1);
				trainClass.setUpdateTime(new Date());
				trainClass.setUpdateUser(currentUser.getXm());
				thisService.update(trainClass);
			}
		}
	}

	/**
	 * 获取班级的班主任列表数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/classBzr" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void classBzrList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据

		String classId = request.getParameter("classId");
		if (StringUtils.isEmpty(classId)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入查询参数'"));
			return;
		} else {
			List<TrainTeacher> listTeacher = thisService.getClassBzrList(classId);

			strData = jsonBuilder.buildObjListToJson((long) listTeacher.size(), listTeacher, true);// 处理数据
			writeJSON(response, strData);// 返回数据
		}
	}

	/**
	 * 获取班级的所有相关数据（班级信息、学员信息、课程信息）
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/getClassAllInfo" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getClassAllInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat fmtDateTime = new SimpleDateFormat("yyyy-M-d h:mm");
		String classId = request.getParameter("classId");

		if (StringUtils.isEmpty(classId)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入查询参数'"));
			return;
		} else {
			try {
				Map<String, String> mapHeadshipLevel = new HashMap<>();
				Map<String, String> mapXbm = new HashMap<>();
				Map<String, String> mapClassCategory = new HashMap<>();
				Map<String, String> mapMzmCategory = new HashMap<>();
				Map<String, String> mapTraineeCategory = new HashMap<>();
				String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM','ZXXBJLX','TRAINEECATEGORY','MZM')";
				List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
				for (BaseDicitem baseDicitem : listBaseDicItems1) {
					if (baseDicitem.getDicCode().equals("XBM"))
						mapXbm.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
					else if (baseDicitem.getDicCode().equals("HEADSHIPLEVEL"))
						mapHeadshipLevel.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
					else if (baseDicitem.getDicCode().equals("MZM"))
						mapMzmCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
					else if (baseDicitem.getDicCode().equals("TRAINEECATEGORY"))
						mapTraineeCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
					else
						mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
				}

				// 1.获取班级信息
				TrainClass trainClass = thisService.get(classId);

				// 2.班级学员信息
				List<TrainClasstrainee> trainClasstraineeList = null;
				// isDelete=0 and
				String hql = " from TrainClasstrainee where classId = '" + classId + "' order by xm asc";
				trainClasstraineeList = classTraineeService.doQuery(hql);
				// 处理学员基本数据
				List<Map<String, String>> traineeList = new ArrayList<>();
				Map<String, String> traineeMap = null;
				int traineeNum = 0; // 学员人数
				int foodBreakfastNum = 0; // 早餐人数
				int foodLunchNum = 0; // 午餐人数
				int foodDinnerNum = 0; // 晚餐人数
				int roomSiestaNum = 0; // 午休人数
				int roomSleepNum = 0; // 晚宿人数
				int traineeManNum=0;	//男学员人数
				int traineeWomenNum=0;	//女学员人数
				int isEvalNum=0;	//需要评价的课程数
				int roomSiestaManNum = 0; // 男生午休人数
				int roomSleepManNum = 0; // 男生晚宿人数
				int roomSiestaWomenNum = 0; // 女生午休人数
				int roomSleepWomenNum = 0; // 女生晚宿人数
				int roomNoSiestaNum = 0; // 不午休人数
				int roomNoSleepNum = 0; // 不晚宿人数
				
				if (trainClass.getDinnerType() != null && trainClass.getDinnerType() != 3){ 
					//foodBreakfastNum = foodLunchNum = foodDinnerNum = traineeNum;// 若就餐类型不为快餐，则就餐人数为学员总数
					//就餐人数从这个参数里面取
					foodBreakfastNum=trainClass.getBreakfastCount() == null ? 0 : trainClass.getBreakfastCount();
					foodLunchNum=trainClass.getLunchCount() == null ? 0 : trainClass.getLunchCount();
					foodDinnerNum=trainClass.getDinnerCount() == null ? 0 : trainClass.getDinnerCount();				
				}

				for (TrainClasstrainee classTrainee : trainClasstraineeList) {
					traineeMap = new LinkedHashMap<>();
					traineeMap.put("xm", classTrainee.getXm());
					traineeMap.put("xbm", mapXbm.get(classTrainee.getXbm()));
					traineeMap.put("mz",mapMzmCategory.get(classTrainee.getMzm()));
					traineeMap.put("position", classTrainee.getPosition());
					traineeMap.put("headshipLevel", mapHeadshipLevel.get(classTrainee.getHeadshipLevel()));
					traineeMap.put("phone", classTrainee.getMobilePhone());
					//traineeMap.put("sfzjh", classTrainee.getSfzjh());
					traineeMap.put("workUnit", classTrainee.getWorkUnit());
					traineeMap.put("traineeCategory", mapTraineeCategory.get(classTrainee.getTraineeCategory()));

					String isDelete = "";
					if (classTrainee.getIsDelete() == 0)
						isDelete = "正常";
					else if (classTrainee.getIsDelete() == 1)
						isDelete = "删除";
					else if (classTrainee.getIsDelete() == 2)
						isDelete = "新增";
					traineeMap.put("isDelete", isDelete);
					
					//只统计状态不为1的数据
					if(classTrainee.getIsDelete()!=1){
						// 若就餐类型为快餐，则统计
						if (trainClass.getDinnerType() != null && trainClass.getDinnerType() == 3) {
							if (classTrainee.getBreakfast() != null && classTrainee.getBreakfast() == 1)
								foodBreakfastNum++;
							if (classTrainee.getLunch() != null && classTrainee.getLunch() == 1)
								foodLunchNum++;
							if (classTrainee.getDinner() != null && classTrainee.getDinner() == 1)
								foodDinnerNum++;
						}

						if (classTrainee.getSiesta() != null && classTrainee.getSiesta() == 1){
							roomSiestaNum++;
							if("1".equals(classTrainee.getXbm()))
								roomSiestaManNum++;
							else
								roomSiestaWomenNum++;
						}else{
							roomNoSiestaNum++;
						}
							
						if (classTrainee.getSleep() != null && classTrainee.getSleep() == 1){
							roomSleepNum++;
							if("1".equals(classTrainee.getXbm()))
								roomSleepManNum++;
							else
								roomSleepWomenNum++;
						}else{
							roomNoSleepNum++;
						}
							
						
						if("1".equals(classTrainee.getXbm()))
							traineeManNum++;
						else
							traineeWomenNum++;
						
						traineeNum++;
					}								
					
					traineeList.add(traineeMap);
				}

				// 3.班级课程信息
				List<TrainClassschedule> trainClassscheduleList = null;
				// isDelete=0 and
				hql = " from TrainClassschedule where  classId = '" + classId + "' order by beginTime asc";
				trainClassscheduleList = classScheduleSerive.doQuery(hql);
				// 处理课程基本数据
				List<Map<String, String>> courseList = new ArrayList<>();
				Map<String, String> courseMap = null;
				for (TrainClassschedule classSchedule : trainClassscheduleList) {
					courseMap = new LinkedHashMap<>();
					courseMap.put("beginTime", fmtDateTime.format(classSchedule.getBeginTime()));
					courseMap.put("endTime", fmtDateTime.format(classSchedule.getEndTime()));
					courseMap.put("courseName", classSchedule.getCourseName());
					courseMap.put("teachType", classSchedule.getTeachTypeName());
					courseMap.put("teacher", classSchedule.getMainTeacherName());
					courseMap.put("address", classSchedule.getScheduleAddress());
					courseMap.put("isEval",
							classSchedule.getIsEval() != null ? classSchedule.getIsEval() == 1 ? "是" : "否" : "否");

					String isDelete = "";
					if (classSchedule.getIsDelete() == 0)
						isDelete = "正常";
					else if (classSchedule.getIsDelete() == 1)
						isDelete = "删除";
					else if (classSchedule.getIsDelete() == 2)
						isDelete = "新增";
					courseMap.put("isDelete", isDelete);
					
					//只统计状态不为1的数据
					if(classSchedule.getIsDelete()!=1){
						if(classSchedule.getIsEval()==1)
							isEvalNum++;
					}
									
					courseList.add(courseMap);
				}

				Map<String, String> classMap = new LinkedHashMap<>();
				classMap.put("classCategory", mapClassCategory.get(trainClass.getClassCategory()));
				classMap.put("beginDate", fmtDate.format(trainClass.getBeginDate()));
				classMap.put("endDate", fmtDate.format(trainClass.getEndDate()));
				classMap.put("className", trainClass.getClassName());
				classMap.put("bzr", trainClass.getBzrName());
				classMap.put("contactPerson", trainClass.getContactPerson());
				classMap.put("contactPhone", trainClass.getContactPhone());
				classMap.put("traineeNum", String.valueOf(traineeNum));
				classMap.put("foodBreakfastNum", String.valueOf(foodBreakfastNum));
				classMap.put("foodLunchNum", String.valueOf(foodLunchNum));
				classMap.put("foodDinnerNum", String.valueOf(foodDinnerNum));
				classMap.put("roomSiestaNum", String.valueOf(roomSiestaNum));
				classMap.put("roomSleepNum", String.valueOf(roomSleepNum));
				classMap.put("traineeManNum", String.valueOf(traineeManNum));
				classMap.put("traineeWomenNum", String.valueOf(traineeWomenNum));
				classMap.put("isEvalNum", String.valueOf(isEvalNum));
				classMap.put("roomSiestaManNum", String.valueOf(roomSiestaManNum));
				classMap.put("roomSleepManNum", String.valueOf(roomSleepManNum));
				classMap.put("roomSiestaWomenNum", String.valueOf(roomSiestaWomenNum));
				classMap.put("roomSleepWomenNum", String.valueOf(roomSleepWomenNum));
				classMap.put("roomNoSiestaNum", String.valueOf(roomNoSiestaNum));
				classMap.put("roomNoSleepNum", String.valueOf(roomNoSleepNum));
				
				Long time=trainClass.getEndDate().getTime() - trainClass.getBeginDate().getTime();
				classMap.put("classDay",String.valueOf(time/(1000*60*60*24)+1));
				
				String isUse = "";
				if (trainClass.getIsuse() == null || trainClass.getIsuse() == 0)
					isUse = "未提交";
				else if (trainClass.getIsuse() == 1)
					isUse = "已提交";
				else if (trainClass.getIsuse() == 2)
					isUse = "修改未提交";
				else if (trainClass.getIsuse() == 3)
					isUse = "修改已提交";
				classMap.put("isUse", isUse);

				String isArrange = "";
				if (trainClass.getIsarrange() == null || trainClass.getIsarrange() == 0)
					isArrange = "未安排";
				else if (trainClass.getIsarrange() == 1) {
					if (trainClass.getIsuse() == 1)
						isArrange = "安排完毕";
					else if (trainClass.getIsuse() == 2)
						isArrange = "等待提交";
					else if (trainClass.getIsuse() == 3) {
						isArrange = "可更新安排";
					}
				}
				classMap.put("isArrange", isArrange);

				classMap.put("needChecking",
						trainClass.getNeedChecking() != null ? trainClass.getNeedChecking() == 1 ? "需要" : "不需要" : "不需要");
				classMap.put("dinnerType",
						trainClass.getDinnerType() == null ? "3" : String.valueOf(trainClass.getDinnerType()));
				classMap.put("avgNumber",
						trainClass.getAvgNumber() == null ? "0" : String.valueOf(trainClass.getAvgNumber()));
				classMap.put("breakfastStand",
						trainClass.getBreakfastStand() == null ? "0" : String.valueOf(trainClass.getBreakfastStand()));
				classMap.put("breakfastCount",
						trainClass.getBreakfastCount() == null ? "0" : String.valueOf(trainClass.getBreakfastCount()));
				classMap.put("lunchStand",
						trainClass.getLunchStand() == null ? "0" : String.valueOf(trainClass.getLunchStand()));
				classMap.put("lunchCount",
						trainClass.getLunchCount() == null ? "0" : String.valueOf(trainClass.getLunchCount()));
				classMap.put("dinnerStand",
						trainClass.getDinnerStand() == null ? "0" : String.valueOf(trainClass.getDinnerStand()));
				classMap.put("dinnerCount",
						trainClass.getDinnerCount() == null ? "0" : String.valueOf(trainClass.getDinnerCount()));

				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("classInfo", classMap);
				resultMap.put("classTrainee", traineeList);
				resultMap.put("classCourse", courseList);

				strData = jsonBuilder.toJson(resultMap);
				writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请重试或联系管理员！\""));
			}
		}
	}

	/**
	 * 导出班级住宿安排信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportRoomExcel")
	public void exportRoomExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassRoomIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassRoomIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		// SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		// SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		Map<String, String> mapHeadshipLevel = new HashMap<>();
		Map<String, String> mapXbm = new HashMap<>();
		Map<String, String> mapClassCategory = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM','ZXXBJLX')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			if (baseDicitem.getDicCode().equals("XBM"))
				mapXbm.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else if (baseDicitem.getDicCode().equals("HEADSHIPLEVEL"))
				mapHeadshipLevel.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else
				mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
		}

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 15, 15, 15, 20, 15, 10, 10, 10, 10 };

		// 1.班级信息
		String ids = request.getParameter("ids"); // 程序中限定每次只能导出一个班级
		TrainClass trainClass = thisService.get(ids);

		// --------2.处理住宿信息，并组装表格数据--------------------
		List<TrainClasstrainee> roomClasstraineeList = null;
		String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId = '" + ids + "'";
		}
		hql += " order by siesta desc,sleep desc,xbm asc,roomName desc ";
		roomClasstraineeList = classTraineeService.doQuery(hql);
		// 处理学员基本数据
		List<Map<String, String>> roomList = new ArrayList<>();
		Map<String, String> roomMap = null;
		for (TrainClasstrainee classTrainee : roomClasstraineeList) {
			roomMap = new LinkedHashMap<>();
			Integer siesta = classTrainee.getSiesta();
			Integer sleep = classTrainee.getSleep();

			roomMap.put("xm", classTrainee.getXm());
			roomMap.put("xbm", mapXbm.get(classTrainee.getXbm()));
			roomMap.put("phone", classTrainee.getMobilePhone());
			roomMap.put("sfzjh", classTrainee.getSfzjh());
			roomMap.put("position", classTrainee.getPosition());
			roomMap.put("headshipLevel", mapHeadshipLevel.get(classTrainee.getHeadshipLevel()));
			roomMap.put("siesta", siesta == null ? "否" : siesta == 1 ? "是" : "否");
			roomMap.put("sleep", sleep == null ? "否" : sleep == 1 ? "是" : "否");
			roomMap.put("roomName", classTrainee.getRoomName());
			roomList.add(roomMap);
		}
		Map<String, Object> roomAllMap = new LinkedHashMap<>();
		roomAllMap.put("data", roomList);
		roomAllMap.put("title", "班级学员住宿安排信息表");
		roomAllMap.put("head", new String[] { "姓名", "性别", "电话号码", "身份证件号", "职务", "行政级别", "是否午休", "是否晚宿", "房间名称" }); // 规定名字相同的，设定为合并
		roomAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		roomAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		roomAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(roomAllMap);

		// 在导出方法中进行解析
		String beginDate = fmtDate.format(trainClass.getBeginDate());
		String endDate = fmtDate.format(trainClass.getEndDate());
		String sheetTitle = trainClass.getClassName() + "（" + beginDate + "-" + endDate + "）";
		boolean result = PoiExportExcel.exportExcel(response, "班级住宿安排信息", sheetTitle, allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassRoomIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassRoomIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassRoomIsState", "0");
		}
	}

	/**
	 * 判断导出学员住宿安排信息时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportRoomEnd")
	public void checkExportRoomEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassRoomIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassRoomIsState");
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
	 * 导出班级住宿安排信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportSiteExcel")
	public void exportSiteExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassSiteIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassSiteIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 15, 15, 15, 20, 15, 15, 15 };

		// 1.班级信息
		String ids = request.getParameter("ids"); // 程序中限定每次只能导出一个班级
		TrainClass trainClass = thisService.get(ids);

		// 2.班级课程信息
		List<TrainClassschedule> trainClassscheduleList = null;
		String hql = " from TrainClassschedule where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId ='" + ids + "'";
		}
		hql += " order by beginTime asc";
		trainClassscheduleList = classScheduleSerive.doQuery(hql);
		// 处理课程基本数据
		List<Map<String, String>> courseList = new ArrayList<>();
		Map<String, String> courseMap = null;
		for (TrainClassschedule classSchedule : trainClassscheduleList) {
			courseMap = new LinkedHashMap<>();

			Date beginTime = classSchedule.getBeginTime();
			Date endTime = classSchedule.getEndTime();
			courseMap.put("date", fmtDateWeek.format(beginTime).replace(" ", "\r\n"));
			courseMap.put("timeInterval", getTimeInterval(beginTime));
			courseMap.put("time", fmtTime.format(beginTime) + "-" + fmtTime.format(endTime));

			String teachType = classSchedule.getTeachTypeName();
			if (teachType == null)
				teachType = "";
			else
				teachType += "：";
			String cotent = teachType + classSchedule.getCourseName();
			courseMap.put("content", cotent);
			courseMap.put("teacher", classSchedule.getMainTeacherName());
			courseMap.put("address", classSchedule.getScheduleAddress());
			courseMap.put("beizhu", "");
			courseList.add(courseMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", courseList);
		courseAllMap.put("title", "班级课程场地安排表");
		courseAllMap.put("head", new String[] { "日期", "时间", "时间", "内容", "任课教师", "上课地点", "备注" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 1, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", new String[] { "date", "timeInterval" }); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		String beginDate = fmtDate.format(trainClass.getBeginDate());
		String endDate = fmtDate.format(trainClass.getEndDate());
		String sheetTitle = trainClass.getClassName() + "（" + beginDate + "-" + endDate + "）";
		boolean result = PoiExportExcel.exportExcel(response, "班级课程场地安排信息", sheetTitle, allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassSiteIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassSiteIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassSiteIsState", "0");
		}
	}

	/**
	 * 判断是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportSiteEnd")
	public void checkExportSiteEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassSiteIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassSiteIsState");
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
	 * 导出班级所有信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCourseCheckExcel")
	public void exportCourseCheckExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportCourseCheckIsEnd", "0");
		request.getSession().removeAttribute("exportCourseCheckIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		Map<String, String> mapClassCategory = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('ZXXBJLX')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
				mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
		}

		Integer[] columnWidth = new Integer[] { 15, 15, 15, 15, 15, 15, 15,15,15};
		
		// 1.班级信息
				String ids = request.getParameter("ids"); // 程序中限定每次只能导出一个班级
				TrainClass trainClass = thisService.get(ids);

				// 2.班级学员信息
				List<TrainClasstrainee> trainClasstraineeList = null;
				String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
				String sql ="";		
				if (StringUtils.isNotEmpty(ids)) {
					hql += " and classId in ('" + ids.replace(",", "','") + "')";
				}
				hql += " order by xm asc";
				trainClasstraineeList = classTraineeService.doQuery(hql);
				// 处理学员基本数据
				List<Map<String, String>> traineeList = new ArrayList<>();
				Map<String, String> traineeMap = null;
				int traineeNum =0; // 学员人数
				for (TrainClasstrainee classTrainee : trainClasstraineeList) {
					traineeMap = new LinkedHashMap<>();
					traineeMap.put("xm", classTrainee.getXm());
					traineeNum++;
					//只统计不为1的数据
					traineeList.add(traineeMap);
				}
				// 3.班级课程信息
				List<Map<String,Object>> trainClassscheduleList = null;
				sql = "SELECT CLASS_SCHEDULE_ID,COURSE_NAME,BEGIN_TIME,END_TIME FROM TRAIN_T_CLASSSCHEDULE where ISDELETE=0";
		        sql += " and CLASS_ID='" + ids +"'";
		        sql += " order by CLASS_SCHEDULE_ID asc";
				trainClassscheduleList = classScheduleSerive.getForValuesToSql(sql);

				// 4.班级学员信息
				List<Map<String,Object>> voTrainClassCheckList = null;
				List<String> classScheduleIdList = new ArrayList<>();
				sql="SELECT xm,classTraineeId,incardTime,outcardTime,attendResult FROM TRAIN_V_CHECKRESULT WHERE classId='" + ids +"'"+" order by classScheduleId asc";
				voTrainClassCheckList = trainClasstraineeService.getForValuesToSql(sql);
				int voTrainClassCheckListCount = voTrainClassCheckList.size();
				
				Map<String,List<String>> traineeResult=new LinkedHashMap<>();	
				List<String> lists=null;
				Map<String,Object> tcc=null;
				for(int i=0;i<voTrainClassCheckListCount;i++){
					tcc=voTrainClassCheckList.get(i);
					lists=traineeResult.get(tcc.get("classTraineeId"));
					if(lists==null){
						lists=new ArrayList<>();
						lists.add(String.valueOf(i+1));
						lists.add(String.valueOf(tcc.get("xm")));
					}
					String incardTime=String.valueOf(tcc.get("incardTime"));
					if(incardTime.equals("null")==false) {
						incardTime=incardTime.substring(11, incardTime.lastIndexOf("."));
					}
					String outcardTime=String.valueOf(tcc.get("outcardTime"));
					if(outcardTime.equals("null")==false) {
						outcardTime=outcardTime.substring(11, outcardTime.lastIndexOf("."));
					}
					lists.add(incardTime);
					lists.add(outcardTime);
					lists.add(String.valueOf(tcc.get("attendResult")));
					
					traineeResult.put(String.valueOf(tcc.get("classTraineeId")), lists);
				}
				System.out.println(traineeResult);
				
				// ----------------------组装导出数据----------------------------------
				List<Map<String, Object>> allList = new ArrayList<>();

				// --------1.处理班级基本数据，并组装表格数据（特殊：一行分作两行显示）
				List<Map<String, Object>> classList1 = new ArrayList<>(); // 虽然内容只有一行，但是由于接口的设定，所以依旧使用list存入数据
				Map<String, Object> classMap1 = new LinkedHashMap<>();
				classMap1.put("className", trainClass.getClassName());
				classMap1.put("classCategory", mapClassCategory.get(trainClass.getClassCategory()));
				classMap1.put("traineeNum", String.valueOf(traineeNum));
				classMap1.put("beginDate",fmtDate.format(trainClass.getBeginDate())+" "+ fmtTime.format(trainClass.getBeginDate()));
				classMap1.put("endDate", fmtDate.format(trainClass.getEndDate())+" "+fmtTime.format(trainClass.getEndDate()));
				classMap1.put("needChecking", trainClass.getNeedChecking()==1?"需要":"不需要");
				classMap1.put("checkCourse", trainClassscheduleList);
				classMap1.put("voTrainClassCheck", traineeResult);
				classList1.add(classMap1);
				// 第一行数据
				Map<String, Object> classAllMap1 = new LinkedHashMap<>();
				classAllMap1.put("data", classList1);
				classAllMap1.put("title", "班级基本信息表");
				classAllMap1.put("columnWidth", 15);
				classAllMap1.put("headColumnCount", 8);
				allList.add(classAllMap1);
				
		// 在导出方法中进行解析
		boolean result = exportMeetingInfo.exportCheckResultInfoExcel(response, trainClass.getClassName()+"班级信息", trainClass.getClassName()+"班考勤表", allList);
		if (result == true) {
			request.getSession().setAttribute("exportCourseCheckIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportCourseCheckIsEnd", "0");
			request.getSession().setAttribute("exportCourseCheckIsState", "0");
		}

	}

	/**
	 * 判断是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkCourseCheckExcelEnd")
	public void checkCourseCheckExcelEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportCourseCheckIsEnd");
		Object state = request.getSession().getAttribute("exportCourseCheckIsState");
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
	 * 导出班级所有信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");

		// 由于多个不同的表格放在一个sheet里，所以列宽只能是只有一份。
		Integer[] columnWidth = new Integer[] { 15, 15, 15, 20, 15, 15, 15 };

		Map<String, String> mapHeadshipLevel = new HashMap<>();
		Map<String, String> mapXbm = new HashMap<>();
		Map<String, String> mapClassCategory = new HashMap<>();
		Map<String, String> mapMzmCategory = new HashMap<>();
		Map<String, String> mapTraineeCategory = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM','ZXXBJLX','TRAINEECATEGORY','MZM')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			if (baseDicitem.getDicCode().equals("XBM"))
				mapXbm.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else if (baseDicitem.getDicCode().equals("HEADSHIPLEVEL"))
				mapHeadshipLevel.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else if (baseDicitem.getDicCode().equals("MZM"))
				mapMzmCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else if (baseDicitem.getDicCode().equals("TRAINEECATEGORY"))
				mapTraineeCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
			else
				mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
		}

		// ----------------------查询出各类数据，并作数据处理----------------------------------
		// 1.班级信息
		String ids = request.getParameter("ids"); // 程序中限定每次只能导出一个班级
		TrainClass trainClass = thisService.get(ids);

		// 2.班级学员信息
		List<TrainClasstrainee> trainClasstraineeList = null;
		String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId in ('" + ids.replace(",", "','") + "')";
		}
		hql += " order by xm asc";
		trainClasstraineeList = classTraineeService.doQuery(hql);
		// 处理学员基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		int traineeNum =0; // 学员人数
		int foodBreakfastNum = 0; // 早餐人数
		int foodLunchNum = 0; // 午餐人数
		int foodDinnerNum = 0; // 晚餐人数
		int roomSiestaNum = 0; // 午休人数
		int roomSleepNum = 0; // 晚宿人数
		if (trainClass.getDinnerType() != null && trainClass.getDinnerType() != 3) {
			// 若就餐类型不为快餐，则就餐人数为学员总数		
			// foodBreakfastNum = foodLunchNum = foodDinnerNum = traineeNum; //		
			foodBreakfastNum=trainClass.getBreakfastCount() == null ? 0 : trainClass.getBreakfastCount();
			foodLunchNum=trainClass.getLunchCount() == null ? 0 : trainClass.getLunchCount();
			foodDinnerNum=trainClass.getDinnerCount() == null ? 0 : trainClass.getDinnerCount();
		}

		for (TrainClasstrainee classTrainee : trainClasstraineeList) {
			traineeMap = new LinkedHashMap<>();

			traineeMap.put("xm", classTrainee.getXm());
			traineeMap.put("xbm", mapXbm.get(classTrainee.getXbm()));
			traineeMap.put("mz", mapMzmCategory.get(classTrainee.getMzm()));
			traineeMap.put("position", classTrainee.getPosition());
			traineeMap.put("headshipLevel", mapHeadshipLevel.get(classTrainee.getHeadshipLevel()));
			traineeMap.put("phone", classTrainee.getMobilePhone());
			traineeMap.put("sfzjh", classTrainee.getSfzjh());
			traineeMap.put("workUnit", classTrainee.getWorkUnit());
			traineeMap.put("traineeCategory", mapTraineeCategory.get(classTrainee.getTraineeCategory()));
			
			
			//只统计不为1的数据
			if(classTrainee.getIsDelete()!=1){
				// 若就餐类型为快餐，则统计
				if (trainClass.getDinnerType() != null && trainClass.getDinnerType() == 3) {
					if (classTrainee.getBreakfast() != null && classTrainee.getBreakfast() == 1)
						foodBreakfastNum++;
					if (classTrainee.getLunch() != null && classTrainee.getLunch() == 1)
						foodLunchNum++;
					if (classTrainee.getDinner() != null && classTrainee.getDinner() == 1)
						foodDinnerNum++;
				}
	
				if (classTrainee.getSiesta() != null && classTrainee.getSiesta() == 1)
					roomSiestaNum++;
				if (classTrainee.getSleep() != null && classTrainee.getSleep() == 1)
					roomSleepNum++;
				
				traineeNum++;
			}
			traineeList.add(traineeMap);
		}

		// 3.班级课程信息
		List<TrainClassschedule> trainClassscheduleList = null;
		hql = " from TrainClassschedule where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId in ('" + ids.replace(",", "','") + "')";
		}
		hql += " order by beginTime asc";
		trainClassscheduleList = classScheduleSerive.doQuery(hql);
		// 处理课程基本数据
		List<Map<String, String>> courseList = new ArrayList<>();
		Map<String, String> courseMap = null;
		for (TrainClassschedule classSchedule : trainClassscheduleList) {
			courseMap = new LinkedHashMap<>();

			Date beginTime = classSchedule.getBeginTime();
			Date endTime = classSchedule.getEndTime();
			courseMap.put("date", fmtDateWeek.format(beginTime).replace(" ", "\r\n"));
			courseMap.put("timeInterval", getTimeInterval(beginTime));
			courseMap.put("time", fmtTime.format(beginTime) + "-" + fmtTime.format(endTime));

			String teachType = classSchedule.getTeachTypeName();
			if (teachType == null)
				teachType = "";
			else
				teachType += "：";
			String cotent = teachType + classSchedule.getCourseName();
			courseMap.put("content", cotent);
			courseMap.put("teacher", classSchedule.getMainTeacherName());
			courseMap.put("address", classSchedule.getScheduleAddress());
			courseMap.put("beizhu", "");
			courseList.add(courseMap);
		}

		// ----------------------组装导出数据----------------------------------
		// 1：设定一个大的List，分别用来存放 班级信息、学员信息、课程信息、就餐信息、住宿信息
		// 2:循环将各个信息分别装入Map中
		// 3：若多个相邻的表头和数据一致，则合并行单元格
		// 4：若同列的数据一致，并且根据设定的列合并条件成立，则合并列单元格
		List<Map<String, Object>> allList = new ArrayList<>();

		// --------1.处理班级基本数据，并组装表格数据（特殊：一行分作两行显示）
		List<Map<String, String>> classList1 = new ArrayList<>(); // 虽然内容只有一行，但是由于接口的设定，所以依旧使用list存入数据
		List<Map<String, String>> classList2 = new ArrayList<>(); // 由于特殊要求，所以分为两列去显示表头和数据
		Map<String, String> classMap1 = new LinkedHashMap<>();
		Map<String, String> classMap2 = new LinkedHashMap<>();
		classMap1.put("classCategory", mapClassCategory.get(trainClass.getClassCategory()));
		classMap1.put("beginDate", fmtDate.format(trainClass.getBeginDate()));
		classMap1.put("endDate", fmtDate.format(trainClass.getEndDate()));
		classMap1.put("className", trainClass.getClassName());
		classMap1.put("bzr", trainClass.getBzrName());
		classMap1.put("contactPerson", trainClass.getContactPerson());
		classMap1.put("contactPhone", trainClass.getContactPhone());
		classMap2.put("traineeNum", String.valueOf(traineeNum));
		classMap2.put("foodBreakfastNum", String.valueOf(foodBreakfastNum));
		classMap2.put("foodLunchNum", String.valueOf(foodLunchNum));
		classMap2.put("foodDinnerNum", String.valueOf(foodDinnerNum));
		classMap2.put("roomSiestaNum", String.valueOf(roomSiestaNum));
		classMap2.put("roomSleepNum", String.valueOf(roomSleepNum));

		String isUse = "";
		if (trainClass.getIsuse() == null || trainClass.getIsuse() == 0)
			isUse = "未提交";
		else if (trainClass.getIsuse() == 1)
			isUse = "已提交";
		else if (trainClass.getIsuse() == 2)
			isUse = "修改未提交";
		else if (trainClass.getIsuse() == 3)
			isUse = "修改已提交";
		classMap2.put("isUse", isUse);

		classList1.add(classMap1);
		classList2.add(classMap2);
		// 第一行数据
		Map<String, Object> classAllMap1 = new LinkedHashMap<>();
		classAllMap1.put("data", classList1);
		classAllMap1.put("title", "班级基本信息表");
		classAllMap1.put("head", new String[] { "班级类型", "开始日期", "结束日期", "班级名称", "班主任", "联系人", "联系电话" }); // 规定
																											// 名字相同的，设定为合并
		classAllMap1.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		classAllMap1.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		classAllMap1.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(classAllMap1);
		// 第二行数据
		Map<String, Object> classAllMap2 = new LinkedHashMap<>();
		classAllMap2.put("data", classList2);
		classAllMap2.put("title", null); // 这一行不需要标题了
		classAllMap2.put("head", new String[] { "学员人数", "早餐人数", "午餐人数", "晚餐人数", "午休人数", "晚宿人数", "提交状态" }); // 规定
																											// 名字相同的，设定为合并
		classAllMap2.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		classAllMap2.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		classAllMap2.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(classAllMap2);

		// --------2.组装学员表格数据
		Map<String, Object> traineeAllMap = new LinkedHashMap<>();
		traineeAllMap.put("data", traineeList);
		traineeAllMap.put("title", "班级学员表");
		traineeAllMap.put("head", new String[] { "姓名", "性别","民族","职务", "行政级别", "移动电话", "身份证号码", "所在单位", "学员类型"  }); // 规定
																										// 名字相同的，设定为合并
		traineeAllMap.put("columnWidth",  new Integer[] { 15, 15, 15, 20, 15, 15, 15,15,15 }); // 30代表30个字节，15个字符
		traineeAllMap.put("columnAlignment", new Integer[] { 0, 0,0, 0, 0, 0, 0,1,0 }); // 0代表居中，1代表居左，2代表居右
		traineeAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(traineeAllMap);

		// --------3.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", courseList);
		courseAllMap.put("title", "班级课程表");
		courseAllMap.put("head", new String[] { "日期", "时间", "时间", "内容", "任课教师", "上课地点", "备注" }); // 规定
																									// 名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 1, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", new String[] { "date", "timeInterval" }); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// --------4.处理就餐信息，并组装表格数据（特殊：一行分作两行显示）--------------------
		// 组装班级就餐数据，由于组装的数据格式固定，所以这里也是用一个list存入data
		List<Map<String, String>> foodList1 = new ArrayList<>();
		List<Map<String, String>> foodList2 = new ArrayList<>();
		Map<String, String> foodMap1 = new LinkedHashMap<>();
		Map<String, String> foodMap2 = new LinkedHashMap<>();
		String[] foodHead1 = null;
		String[] foodHead2 = null;
		Integer[] foodColumnAlignment1 = null;
		Integer[] foodColumnAlignment2 = null;
		Integer dinnerType = trainClass.getDinnerType();
		if (dinnerType == null)
			dinnerType = 3;
		if (dinnerType == 1) {
			foodMap1.put("foodType", "围餐");
			foodMap1.put("breakfastStand", String.valueOf(trainClass.getBreakfastStand()));
			foodMap1.put("breakfastStand2", String.valueOf(trainClass.getBreakfastStand()));
			foodMap1.put("lunchStand", String.valueOf(trainClass.getLunchStand()));
			foodMap1.put("lunchStand2", String.valueOf(trainClass.getLunchStand()));
			foodMap1.put("dinnerStand", String.valueOf(trainClass.getDinnerStand()));
			foodMap1.put("dinnerStand2", String.valueOf(trainClass.getDinnerStand()));
			foodHead1 = new String[] { "就餐类型", "早餐餐标", "早餐餐标", "午餐餐标", "午餐餐标", "晚餐餐标", "晚餐餐标" };
			foodColumnAlignment1 = new Integer[] { 0, 0, 0, 0, 0, 0, 0 };

			foodMap2.put("avgNumber", String.valueOf(trainClass.getAvgNumber()));
			foodMap2.put("breakfastCount", String.valueOf(trainClass.getBreakfastCount()));
			foodMap2.put("breakfastCount2", String.valueOf(trainClass.getBreakfastCount()));
			foodMap2.put("lunchCount", String.valueOf(trainClass.getLunchCount()));
			foodMap2.put("lunchCount2", String.valueOf(trainClass.getLunchCount()));
			foodMap2.put("dinnerCount", String.valueOf(trainClass.getDinnerCount()));
			foodMap2.put("dinnerCount2", String.valueOf(trainClass.getDinnerCount()));
			foodHead2 = new String[] { "每围人数", "早餐围数", "早餐围数", "午餐围数", "午餐围数", "晚餐围数", "晚餐围数" };
			foodColumnAlignment2 = new Integer[] { 0, 0, 0, 0, 0, 0, 0 };

			foodList1.add(foodMap1);
			foodList2.add(foodMap2);
		} else if (dinnerType == 2) {
			foodMap1.put("foodType", "自助餐");
			foodMap1.put("breakfastStand", String.valueOf(trainClass.getBreakfastStand()));
			foodMap1.put("breakfastStand2", String.valueOf(trainClass.getBreakfastStand()));
			foodMap1.put("lunchStand", String.valueOf(trainClass.getLunchStand()));
			foodMap1.put("lunchStand2", String.valueOf(trainClass.getLunchStand()));
			foodMap1.put("dinnerStand", String.valueOf(trainClass.getDinnerStand()));
			foodMap1.put("dinnerStand2", String.valueOf(trainClass.getDinnerStand()));
			foodHead1 = new String[] { "就餐类型", "早餐餐标", "早餐餐标", "午餐餐标", "午餐餐标", "晚餐餐标", "晚餐餐标" };
			foodColumnAlignment1 = new Integer[] { 0, 0, 0, 0, 0, 0, 0 };

			foodMap2.put("avgNumber", "");
			foodMap2.put("breakfastCount", String.valueOf(trainClass.getBreakfastCount()));
			foodMap2.put("breakfastCount2", String.valueOf(trainClass.getBreakfastCount()));
			foodMap2.put("lunchCount", String.valueOf(trainClass.getLunchCount()));
			foodMap2.put("lunchCount2", String.valueOf(trainClass.getLunchCount()));
			foodMap2.put("dinnerCount", String.valueOf(trainClass.getDinnerCount()));
			foodMap2.put("dinnerCount2", String.valueOf(trainClass.getDinnerCount()));
			foodHead2 = new String[] { "每围人数", "早餐人数", "早餐人数", "午餐人数", "午餐人数", "晚餐人数", "晚餐人数" };
			foodColumnAlignment2 = new Integer[] { 0, 0, 0, 0, 0, 0, 0 };

			foodList1.add(foodMap1);
			foodList2.add(foodMap2);
		} else if (dinnerType == 3) {
			foodMap1.put("foodType", "快餐");
			foodMap1.put("foodType2", "快餐");
			foodMap1.put("breakfastStand", String.valueOf(trainClass.getBreakfastStand()));
			foodMap1.put("lunchStand", String.valueOf(trainClass.getLunchStand()));
			foodMap1.put("dinnerStand", String.valueOf(trainClass.getDinnerStand()));
			;
			foodHead1 = new String[] { "就餐类型", "就餐类型", "早餐餐标", "午餐餐标", "晚餐餐标" };
			foodColumnAlignment1 = new Integer[] { 0, 0, 0, 0, 0 };
			foodList1.add(foodMap1);

			// 查询班级学员就餐信息
			List<TrainClasstrainee> foodClasstraineeList = null;
			hql = " from TrainClasstrainee where isDelete=0 ";
			if (StringUtils.isNotEmpty(ids)) {
				hql += " and classId in ('" + ids.replace(",", "','") + "')";
			}
			hql += " order by breakfast desc,lunch desc,dinner desc";
			foodClasstraineeList = classTraineeService.doQuery(hql);
			// 处理学员基本数据
			for (TrainClasstrainee classTrainee : foodClasstraineeList) {
				foodMap2 = new LinkedHashMap<>();
				Integer breakFast = classTrainee.getBreakfast();
				Integer lunch = classTrainee.getLunch();
				Integer dinner = classTrainee.getDinner();

				foodMap2.put("xm", classTrainee.getXm());
				foodMap2.put("xbm", mapXbm.get(classTrainee.getXbm()));
				foodMap2.put("breakfast", breakFast == null ? "否" : breakFast == 1 ? "是" : "否");
				foodMap2.put("lunch", lunch == null ? "否" : lunch == 1 ? "是" : "否");
				foodMap2.put("dinner", dinner == null ? "否" : dinner == 1 ? "是" : "否");

				foodList2.add(foodMap2);
			}
			foodHead2 = new String[] { "姓名", "性别", "是否早餐", "是否午餐", "是否晚餐" };
			foodColumnAlignment2 = new Integer[] { 0, 0, 0, 0, 0 };
		}
		// 第一行数据
		Map<String, Object> foodAllMap1 = new LinkedHashMap<>();
		foodAllMap1.put("data", foodList1);
		foodAllMap1.put("title", "班级就餐信息表");
		foodAllMap1.put("head", foodHead1); // 规定 名字相同的，设定为合并
		foodAllMap1.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		foodAllMap1.put("columnAlignment", foodColumnAlignment1); // 0代表居中，1代表居左，2代表居右
		foodAllMap1.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(foodAllMap1);
		// 第二行数据
		if (foodList2.size() > 0) {
			Map<String, Object> foodAllMap2 = new LinkedHashMap<>();
			foodAllMap2.put("data", foodList2);
			foodAllMap2.put("title", null);
			foodAllMap2.put("head", foodHead2); // 规定 名字相同的，设定为合并
			foodAllMap2.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
			foodAllMap2.put("columnAlignment", foodColumnAlignment2); // 0代表居中，1代表居左，2代表居右
			foodAllMap2.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(foodAllMap2);
		}

		// --------5.处理住宿信息，并组装表格数据--------------------
		List<TrainClasstrainee> roomClasstraineeList = null;
		hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and classId in ('" + ids.replace(",", "','") + "')";
		}
		hql += " order by siesta desc,sleep desc,xbm asc";
		roomClasstraineeList = classTraineeService.doQuery(hql);
		// 处理学员基本数据
		List<Map<String, String>> roomList = new ArrayList<>();
		Map<String, String> roomMap = null;
		for (TrainClasstrainee classTrainee : roomClasstraineeList) {
			roomMap = new LinkedHashMap<>();
			Integer siesta = classTrainee.getSiesta();
			Integer sleep = classTrainee.getSleep();

			roomMap.put("xm", classTrainee.getXm());
			roomMap.put("xbm", mapXbm.get(classTrainee.getXbm()));
			roomMap.put("siesta", siesta == null ? "否" : siesta == 1 ? "是" : "否");
			roomMap.put("sleep", sleep == null ? "否" : sleep == 1 ? "是" : "否");

			roomList.add(roomMap);
		}
		Map<String, Object> roomAllMap = new LinkedHashMap<>();
		roomAllMap.put("data", roomList);
		roomAllMap.put("title", "班级学员宿舍信息表");
		roomAllMap.put("head", new String[] { "姓名", "性别", "是否午休", "是否晚宿" }); // 规定
																				// 名字相同的，设定为合并
		roomAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		roomAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		roomAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(roomAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, "班级信息", trainClass.getClassName(), allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassIsState", "0");
		}

	}

	/**
	 * 判断是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportEnd")
	public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassIsState");
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
	 * 通过传入的时间参数，获得时段值
	 * 
	 * @param date
	 * @return
	 */
	private String getTimeInterval(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour <= 12)
			return "上午";
		else if (hour < 19)
			return "下午";
		else
			return "晚上";
	}

	/**
	 * 汇总班级学员的学分
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/sumcredit")
	public void doSumCredit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String endFlag = currentUser.getUserName() + "sumCreditIsEnd";
		String stateFlag = currentUser.getUserName() + "sumCreditState";
		String ids = request.getParameter("ids");
		request.getSession().setAttribute(endFlag, "0");
		request.getSession().removeAttribute(stateFlag);

		if (StringUtils.isEmpty(ids)) {
			request.getSession().setAttribute(endFlag, "0");
			request.getSession().setAttribute(stateFlag, "0");
		} else {
			boolean result = thisService.doSumCredit(ids);
			if (result == true) {
				request.getSession().setAttribute(endFlag, "1");
			} else {
				request.getSession().setAttribute(endFlag, "0");
				request.getSession().setAttribute(stateFlag, "0");
			}
		}
	}

	/**
	 * 检查学分汇总是否完成
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/checkSumCreditEnd")
	public void docheckSumCreditEnd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String endFlag = currentUser.getUserName() + "sumCreditIsEnd";
		String stateFlag = currentUser.getUserName() + "sumCreditState";
		Object isEnd = request.getSession().getAttribute(endFlag);
		Object state = request.getSession().getAttribute(stateFlag);
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"学分汇总完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"学分汇总未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"学分汇总未完成！\""));
		}
	}

	
			
			/**
	 * 导出指定班级的学员学分
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
//	@RequestMapping("/exportCredit")
//	public void doexportCredit(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		request.getSession().setAttribute("exportCreditsIsEnd", "0");
//		request.getSession().removeAttribute("exportCreditsIsState");
//
//			List<Map<String, Object>> allList = new ArrayList<>();
//			Integer[] columnWidth = new Integer[] { 10,30, 30, 20, 20, 20, 20 };
//		
//			List<TrainClasstrainee> exportList = new ArrayList<>();
//			String ids = request.getParameter("ids");
//			// 职称数据字典
//			String mapKey = null;
//			String[] propValue = { "XBM", "HEADSHIPLEVEL" };
//			Map<String, String> mapDicItem = new HashMap<>();
//			List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
//			for (BaseDicitem baseDicitem : listDicItem) {
//				mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
//				mapDicItem.put(mapKey, baseDicitem.getItemName());
//			}
//
//			//获取学员数据,
//			List<TrainClasstrainee> trainClasstraineeList = null;
//			String hql = " from TrainClasstrainee where (isDelete=0 or isDelete=2) and classId='";
//			hql += ids;
//			hql += "' order by xm asc";
//			trainClasstraineeList = classTraineeService.doQuery(hql);
//			//将sheet第一页学员数据以Map<String (uuid), Object (trainTeacher)>存储
//			Map<String, Object> trainClasstraineeMap = new HashMap<>();
//			//将traineeId存储在traineeIdList中
//			List<String> traineeIdList = new ArrayList<>();
//			for (TrainClasstrainee trainTeacher : trainClasstraineeList) {
//				trainTeacher.setXbm(mapDicItem.get(trainTeacher.getXbm() + "XBM"));
//				trainTeacher.setHeadshipLevel(mapDicItem.get(trainTeacher.getHeadshipLevel() + "HEADSHIPLEVEL"));
//				traineeIdList.add(trainTeacher.getUuid());
//				trainClasstraineeMap.put(trainTeacher.getUuid(), trainTeacher);
//			}
//			
//			//将所有学员的表头数据以存储在classTraineeinfoList中
//			List<Map<String,Object>> classTraineeinfo=null;
//			List<Map<String,Object>> classTraineeinfoList=new ArrayList<>();
//			for(int i=0;i<traineeIdList.size();i++){
//				classTraineeinfo= new ArrayList<>();
//				String classTraineeId =traineeIdList.get(i);
//				String sql="SELECT * FROM TRAIN_T_CLASSTRAINEE WHERE CLASS_TRAINEE_ID='" +classTraineeId+"'";
//				classTraineeinfo = classTraineeService.getForValuesToSql(sql);
//				classTraineeinfoList.add(classTraineeinfo.get(0));
//			}
//			
//			List<Map<String,Object>> classTrainCreditList= null;
//			List<String> classScheduleIdList = new ArrayList<>();
//			String sql="SELECT * FROM dbo.TRAIN_V_CLASSTRAINEECREDITS WHERE classId='" + ids +"' order by xm asc";
//			classTrainCreditList = classTraineeService.getForValuesToSql(sql);
//			int voTrainClassCheckListCount = classTrainCreditList.size();
//			
//			
//			
//			
//			
//			List<List<Map<String, String>>> traineeCreditList = new ArrayList<>();
//			for(int i=0;i<traineeIdList.size();i++){
//				String classTraineeId =traineeIdList.get(i).trim();
//				List<Map<String, String>> traineeList = new ArrayList<>();
//				List<Map<String, Object>> list = classTraineeService.getClassTraineeCreditsList(classTraineeId);
//				Map<String, String> traineeMap = null;
//				int k=0;
//				for (Map<String, Object> list1 : list) {
//					traineeMap = new LinkedHashMap<>();
//					traineeMap.put("xh", String.valueOf(k++));
//					traineeMap.put("className", String.valueOf(list1.get("className")));
//					traineeMap.put("courseName", String.valueOf(list1.get("courseName")));
//					traineeMap.put("date",  String.valueOf(list1.get("courseDate")));
//					traineeMap.put("time",  String.valueOf(list1.get("courseTime")));
//					traineeMap.put("courseCredits",  String.valueOf(list1.get("courseCredits")));
//					traineeMap.put("realCredits",  String.valueOf(list1.get("realCredits")));
//					traineeList.add(traineeMap);
//				}
//				traineeCreditList.add(traineeList);
//			}
//			
//			
//			
//			
//			boolean result = exportMeetingInfo.exportCourseCreditExcel(response,"学分详细信息", "学分详细信息", allList);
//			if (result == true) {
//				request.getSession().setAttribute("exportCreditsIsEnd", "1");
//			} else {
//				request.getSession().setAttribute("exportCreditsIsEnd", "0");
//				request.getSession().setAttribute("exportCreditsIsState", "0");
//			}
//	}

//	/**
//	 * 检查学分导出是否完成
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	@RequestMapping("/checkExportCreditEnd")
//	public void docheckExportCreditEnd(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		Object isEnd = request.getSession().getAttribute("exportCreditsIsEnd");
//		Object state = request.getSession().getAttribute("exportCreditsIsState");
//		if (isEnd != null) {
//			if ("1".equals(isEnd.toString())) {
//				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
//			} else if (state != null && state.equals("0")) {
//				writeJSON(response, jsonBuilder.returnFailureJson("0"));
//			} else {
//				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
//			}
//		} else {
//			writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
//		}
//	}

	@RequestMapping("/addSendUser")
	public void doaddSendUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String ids = request.getParameter("uuid");
		String sendUserId = request.getParameter("sendUserId");
		String sendUserName = request.getParameter("sendUserName");
		String sendInfo = request.getParameter("sendInfo");
		if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(sendInfo)) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"没有传入发送消息的参数！\""));
			return;
		}

		try {
			Boolean br = thisService.doSendInfoUser(sendUserId, sendInfo, currentUser);
			if (br)
				writeJSON(response, jsonBuilder.returnSuccessJson("\"消息发送成功！\""));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("\"消息发送失败！\""));
		} catch (IOException e) {
			logger.error(e.getMessage());
			writeJSON(response, jsonBuilder.returnFailureJson("\"消息发送失败！\""));
		}
	}
	
}
