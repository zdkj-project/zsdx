
package com.zd.school.jw.schoolcourse.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.schoolcourse.model.JwPublishcourse;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTSchoolcourseService;
import com.zd.school.jw.schoolcourse.dao.JwPublishcourseDao;
import com.zd.school.jw.schoolcourse.service.JwPublishcourseService;
import com.zd.school.oa.flow.model.FlowType;

/**
 * 
 * ClassName: JwPublishcourseController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 校本课程发布课程信息(JW_T_PUBLISHCOURSE)实体Controller.
 * date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwPublishcourse")
public class JwPublishcourseController extends FrameWorkController<JwPublishcourse> implements Constant {

	@Resource
	JwPublishcourseService thisService; // service层接口

	@Resource
	JwTSchoolcourseService courseService;

	@Resource
	JwTGradeService gradeService;

	@Resource
	JwPublishcourseService pulishService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute JwPublishcourse entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<JwPublishcourse> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql,
				currentUser);
//		List<JwPublishcourse> list = new ArrayList<JwPublishcourse>();
//		for (JwPublishcourse jpc : qResult.getResultList()) {
//			String coursehql = "from JwTSchoolcourse where uuid='" + jpc.getCourseId() + "'";
//			List<JwTSchoolcourse> courselist = courseService.doQuery(coursehql);
//			for (JwTSchoolcourse jsc : courselist) {
//				jpc.setCourseName(jsc.getCourseName());
//			}
//
//			String gradehql = "from JwPublishcourse as p inner join fetch p.pcourseGrade where p.uuid='" + jpc.getUuid()
//					+ "'";
//			List<JwPublishcourse> gradelist = pulishService.doQuery(gradehql);
//			for (JwPublishcourse jpctwo : gradelist) {
//				String gradenamehql = "from JwTGrade where uuid='"
//						+ new ArrayList<>(jpctwo.getPcourseGrade()).get(0).getUuid() + "'";
//				List<JwTGrade> gradenamelist = gradeService.doQuery(gradenamehql);
//				for (JwTGrade jg : gradenamelist) {
//					jpc.setGradeName(jg.getGradeName());
//				}
//			}
//			list.add(jpc);
//
//		}
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param JwPublishcourse
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(JwPublishcourse entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		Boolean addResult = thisService.doAdd(entity, currentUser);
		if (addResult)
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
		else
			writeJSON(response, jsonBuilder.returnFailureJson("'增加失败'"));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	/**
	 * doRestore还原删除的记录 @Title: doRestore @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dorestore")
	public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
			return;
		} else {
			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * JwPublishcourse @param @param request @param @param
	 * response @param @throws IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(JwPublishcourse entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		JwPublishcourse perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		Boolean addResult = thisService.doUpdate(perEntity, currentUser);
		if (addResult)
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
		else
			writeJSON(response, jsonBuilder.returnFailureJson("'编辑失败'"));
	}

	@RequestMapping("/coursename")
	public @ResponseBody List<JwTSchoolcourse> coursename(JwPublishcourse entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		String hql = "from JwTSchoolcourse where state=3 and isDelete=0";
		List<JwTSchoolcourse> list = courseService.doQuery(hql);

		return list;

	}

	@RequestMapping("/gradeName")
	public @ResponseBody List<JwTGrade> gradeName(JwPublishcourse entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		String hql = "from JwTGrade";
		List<JwTGrade> list = gradeService.doQuery(hql);

		return list;

	}
}
