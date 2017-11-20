
package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainCourseattend;
import com.zd.school.jw.train.dao.TrainCourseattendDao;
import com.zd.school.jw.train.service.TrainClassscheduleService;
import com.zd.school.jw.train.service.TrainCourseattendService;
import com.zd.school.oa.meeting.model.OaMeetingemp;

/**
 * 
 * ClassName: TrainCourseattendController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 课程考勤刷卡结果(TRAIN_T_COURSEATTEND)实体Controller. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainCourseattend")
public class TrainCourseattendController extends FrameWorkController<TrainCourseattend> implements Constant {

	@Resource
	TrainCourseattendService thisService; // service层接口
	@Resource
	private TrainClassscheduleService courseService;

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
	public void list(@ModelAttribute TrainCourseattend entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<TrainCourseattend> qResult = thisService.list(start, limit, sort, filter, true);
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
	public void doAdd(TrainCourseattend entity, HttpServletRequest request, HttpServletResponse response)
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
	 *
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(TrainCourseattend entity, HttpServletRequest request, HttpServletResponse response)
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
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 * @Title: doDelete
	 * @Description: 是否请假
	 */
	@RequestMapping("/doUpdateLeave")
	public void doUpdateLeave(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			String classId = request.getParameter("classId");
			String classScheduleId = request.getParameter("classScheduleId");
			String classTraineeId = request.getParameter("classTraineeId");
			String val = request.getParameter("val");

			SysUser currentUser = getCurrentSysUser();

			TrainClassschedule course = courseService.get(classScheduleId);

			// 查询看看是否存在了数据
			String[] param = { "classId", "classScheduleId", "traineeId" };
			Object[] values = { classId, classScheduleId, classTraineeId };
			TrainCourseattend attend = thisService.getByProerties(param, values);
			if (attend == null) {
				attend = new TrainCourseattend();
				// if (attend != null) {
				attend.setClassId(classId);
				attend.setClassScheduleId(classScheduleId);
				attend.setTraineeId(classTraineeId);
				attend.setCreateUser(currentUser.getXm());
				attend.setBeginTime(course.getBeginTime());
				attend.setEndTime(course.getEndTime());
			}

			attend.setIsLeave(val);
			attend.setUpdateTime(new Date());
			attend.setUpdateUser(currentUser.getXm());

			thisService.merge(attend);

			writeJSON(response, jsonBuilder.returnSuccessJson("\"设置成功！\""));

		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请重试或联系管理员！\""));
		}
	}
	
	/**
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
	 * @Title: doDelete
	 * @Description: 设置备注
	 */
	@RequestMapping("/doRemark")
	public void doRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			String classId = request.getParameter("classId");
			String classScheduleId = request.getParameter("classScheduleId");
			String classTraineeId = request.getParameter("classTraineeId");
			String remark = request.getParameter("remark");

			SysUser currentUser = getCurrentSysUser();

			TrainClassschedule course = courseService.get(classScheduleId);

			// 查询看看是否存在了数据
			String[] param = { "classId", "classScheduleId", "traineeId" };
			Object[] values = { classId, classScheduleId, classTraineeId };
			TrainCourseattend attend = thisService.getByProerties(param, values);
			if (attend == null) {
				attend = new TrainCourseattend();
				// if (attend != null) {
				attend.setClassId(classId);
				attend.setClassScheduleId(classScheduleId);
				attend.setTraineeId(classTraineeId);
				attend.setCreateUser(currentUser.getXm());
				attend.setBeginTime(course.getBeginTime());
				attend.setEndTime(course.getEndTime());
			}

			attend.setRemark(remark);
			attend.setUpdateTime(new Date());
			attend.setUpdateUser(currentUser.getXm());

			thisService.merge(attend);

			writeJSON(response, jsonBuilder.returnSuccessJson("\"设置成功！\""));

		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请重试或联系管理员！\""));
		}
	}
}
