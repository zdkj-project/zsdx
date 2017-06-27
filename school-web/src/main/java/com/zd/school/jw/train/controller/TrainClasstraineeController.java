
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.service.TrainClasstraineeService;
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
import java.util.List;

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
		QueryResult<TrainClasstrainee> qResult = thisService.list(start, limit, sort, filter, false);
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

			//g.isDelete=0 and  查询所有状态的
			String hql = "select  new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.breakfast,g.lunch,g.dinner,g.isDelete) from TrainClasstrainee g where  g.classId='"
					+ classId + "' order by g.breakfast desc,g.lunch desc,g.dinner desc";
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
			
			//g.isDelete=0 or g.isDelete=1 and 
			String hql = "select new TrainClasstrainee(g.uuid,g.xm,g.xbm,g.siesta,g.sleep,g.roomId,g.roomName,g.isDelete) from TrainClasstrainee g where g.classId='"
					+ classId + "'  order by g.siesta desc,g.sleep desc";

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

}
