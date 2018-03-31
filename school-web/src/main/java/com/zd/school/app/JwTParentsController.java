
package com.zd.school.app;


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
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.model.StuParents;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;
import com.zd.school.student.studentinfo.service.StuParentsService;

/**
 * 
 * ClassName: JwTParentsController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学生家长信息实体Controller. date: 2016-05-03
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/JwTParents")
public class JwTParentsController extends FrameWorkController<StuParents> implements Constant {

	@Resource
	StuParentsService thisService; // service层接口

	@Resource
	StuBaseinfoService jwtService;
	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute StuParents entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
		// 总记录数
		StringBuffer countHql = new StringBuffer(
				"select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
		String whereSql = entity.getWhereSql();// 查询条件
		String parentSql = entity.getParentSql();// 条件
		String querySql = entity.getQuerySql();// 查询条件
		String orderSql = entity.getOrderSql();// 排序
		int start = super.start(request); // 起始记录数
		int limit = entity.getLimit();// 每页记录数
		hql.append(whereSql);
		hql.append(parentSql);
		hql.append(querySql);
		hql.append(orderSql);
		countHql.append(whereSql);
		countHql.append(querySql);
		countHql.append(parentSql);
		List<StuParents> lists = thisService.getQuery(hql.toString(), start, limit);// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * doAdd @Title: doAdd @Description: TODO @param @param JwTParents
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(StuParents entity, HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		entity.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		entity.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		entity = thisService.doMerge(entity);

		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	/**
	 * doDelete @Title: doDelete @Description: TODO @param @param
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
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
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
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * JwTParents @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(StuParents entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		StuParents perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		//perEntity.setModifyUser(userCh); // 设置修改人的中文名
		entity = thisService.doMerge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
	}

	@RequestMapping(value={"/parentsMessage"}, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<StuParents> parentsMessage(StuParents entity, String studentId,
			HttpServletRequest request, HttpServletResponse response) {
		//String dataList = ""; // 返回给js的数据
		List<StuParents> parentsLists = new ArrayList<StuParents>();
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName());
		if (studentId != null && !("").equals(studentId)) {
			hql.append(" where STUDENT_ID='" + studentId + "'");

			parentsLists = thisService.getQuery(hql.toString());// 执行查询方法
		}

		return parentsLists;
	}

	@RequestMapping("/parentsAdd")
	public @ResponseBody List<StuParents> parentsUpdateOrAdd(String xm, String uuid, String sfzjh, String studentId,
			String xbm, String xsgxm, String sfzjlxm, String mobile, String telphone, String eMail)
			throws IllegalAccessException, InvocationTargetException {
		//String data = "";
		List<StuParents> list = new ArrayList<StuParents>();
		StuBaseinfo student =	jwtService.get(studentId);
		StuParents parents = new StuParents();
		parents.setXm(xm);
		parents.setUuid(uuid);
		parents.setSfzjh(sfzjh);
		parents.setSfzjlxm(sfzjlxm);
		parents.setStuId(studentId);
		parents.setXbm(xbm);
		parents.setXsgxm(xsgxm);
		parents.setMobile(mobile);
		parents.setTelphone(telphone);
		parents.setEMail(eMail);
		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
		userCh = currentUser.getXm();

		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(parents);
		parents.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		parents.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		parents = thisService.doMerge(parents);
					
		StringBuffer hql = new StringBuffer("from " + parents.getClass().getSimpleName());
		if (studentId != null && !("").equals(studentId)) {
			hql.append(" where STUDENT_ID='" + studentId + "'");

			list = thisService.getQuery(hql.toString());// 执行查询方法
		}
		return list;
	}
}
