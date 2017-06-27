
package com.zd.school.jw.eduresources.controller;

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
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.eduresources.model.JwCalender;
import com.zd.school.jw.eduresources.model.JwCalenderdetail ;
import com.zd.school.jw.eduresources.dao.JwCalenderdetailDao ;
import com.zd.school.jw.eduresources.service.JwCalenderdetailService ;

/**
 * 
 * ClassName: JwCalenderdetailController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历节次信息表(JW_T_CALENDERDETAIL)实体Controller.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwCalenderdetail")
public class JwCalenderdetailController extends FrameWorkController<JwCalenderdetail> implements Constant {

    @Resource
    JwCalenderdetailService thisService; // service层接口

    /**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute JwCalenderdetail entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1 and isDelete=0 ");
		// 总记录数
		StringBuffer countHql = new StringBuffer(
				"select count(*) from " + entity.getClass().getSimpleName() + " where  1=1 and isDelete=0 ");
		String whereSql = entity.getWhereSql();// 查询条件
		if(whereSql.equals("")){
			writeJSON(response, "[]");// 返回数据
			return;
		}
		
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
		List<JwCalenderdetail> lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * doAdd @Title: doAdd @Description: TODO @param @param JwTCanderdetail
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("/doadd")
	public void doAdd(JwCalenderdetail entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		JwCalenderdetail saveEntity = new JwCalenderdetail();
	    BeanUtils.copyPropertiesExceptNull(entity, saveEntity);
		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		entity.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		entity.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		entity = thisService.merge(entity);

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
		if (delIds == null) {
			writeJSON(response, jsonBuilder.returnFailureJson("'未选择需删除的信息'"));
			return;
		}
		/*
		 * if (StringUtils.isEmpty(delIds)) { writeJSON(response,
		 * jsonBuilder.returnSuccessJson("'没有传入删除主键'")); return; } else {
		 * boolean flag = thisService.logicDelOrRestore(delIds,
		 * StatuVeriable.ISDELETE); if (flag) { writeJSON(response,
		 * jsonBuilder.returnSuccessJson("'删除成功'")); } else {
		 * writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'")); }
		 * 
		 * }
		 */
		try {
			String doIds = "'" + delIds.replace(",", "','") + "'";

			String hql = "DELETE FROM JwCalenderdetail j  WHERE j.uuid IN (" + doIds + ")";

			int flag = thisService.executeHql(hql);

			if (flag > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,请刷新重试！'"));
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
	 * JwTCanderdetail @param @param request @param @param
	 * response @param @throws IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(JwCalenderdetail entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		JwCalenderdetail perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。s
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		entity = thisService.merge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}
}
