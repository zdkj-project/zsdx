
package com.zd.school.control.device.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.BaseControllerDateSoureTwo;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.control.device.model.MjUserright;
import com.zd.school.control.device.service.MjUserrightService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: MjUserrightController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 门禁权限表(MJ_UserRight)实体Controller. date:
 * 2016-09-08
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/MjUserright")
public class MjUserrightController extends BaseControllerDateSoureTwo {

	@Resource
	MjUserrightService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成树

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute MjUserright entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
		// 总记录数
		StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
		String whereSql = entity.getWhereSql();// 查询条件
		String querySql = entity.getQuerySql();// 查询条件
		hql.append(whereSql);
		hql.append(querySql);
		countHql.append(whereSql);
		countHql.append(querySql);
		List<MjUserright> lists = thisService.doQuery(hql.toString(), super.start(request), entity.getLimit());// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 生成树
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/treelist")
	public void getGradeTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_AREAROOMINFOTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param MjUserright
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(MjUserright entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
			String stuId[] = entity.getStuId().split(",");	//人员id
			String   termId[] = entity.getTermId().split(",");	//设备id
			for (int i = 0; i < termId.length; i++) {
				entity = new MjUserright();
				entity.setTermId(termId[i]);
				entity.setStuId(stuId[i]);
				thisService.merge(entity);
			}
		writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
//		if (StringUtils.isEmpty(delIds)) {
//			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
//			return;
//		} else {
//			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
//			if (flag) {
//				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
//			} else {
//				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
//			}
//		}
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
	 * MjUserright @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(MjUserright entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser(request);
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		MjUserright perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		entity = thisService.merge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}

}
