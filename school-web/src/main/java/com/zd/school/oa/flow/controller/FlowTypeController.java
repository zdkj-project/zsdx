/**
 * Project Name:jw-web
 * File Name:FlowTypeController.java
 * Package Name:com.zd.school.oa.controller.flow
 * Date:2016年5月24日上午11:36:38
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.controller;

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

import com.zd.core.controller.core.BaseController;
import com.zd.core.util.JsonBuilder;
import com.zd.school.oa.flow.model.FlowType;
import com.zd.school.oa.flow.model.FlowTypeTree;
import com.zd.school.oa.flow.service.FlowTypeService;

/**
 * ClassName:FlowTypeController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月24日 上午11:36:38
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Controller
@RequestMapping("/flowtype")
public class FlowTypeController extends BaseController<FlowType> {

	@Resource
	private FlowTypeService thisService;
	
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute FlowType entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		List<FlowType> lists=thisService.getQueryAll();

		String strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping(value = { "/listtree" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void listTree(@ModelAttribute FlowType entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String whereSql = request.getParameter("whereSql");// 查询条件
		String parentSql = request.getParameter("parentSql");// 条件
		String querySql = request.getParameter("querySql");// 查询条件
		String orderSql = request.getParameter("orderSql");// 排序

		List<FlowTypeTree> lists = thisService.listTree(whereSql, orderSql, parentSql, querySql);

		String strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping("/doadd")
	public void doAdd(FlowType entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		String userCh = "超级管理员";
		FlowType saveEntity = new FlowType();
		FlowType parEntity = thisService.get(request.getParameter("parent"));
		saveEntity.setParentNode(entity.getParentNode());
		saveEntity.setOrderIndex(1);
		saveEntity.setLeaf(true);
		saveEntity.setNodeText(entity.getNodeText());
		saveEntity.setRemark(entity.getRemark());
		saveEntity.setCreateTime(new Date());
		saveEntity.setCreateUser(userCh);
		saveEntity.BuildNode(parEntity);

		entity = thisService.doMerge(saveEntity);

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	@RequestMapping("/dobroadd")
	public void dobroadd(FlowType entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		String userCh = "超级管理员";
		FlowType saveEntity = new FlowType();
		FlowType parEntity = thisService.get(request.getParameter("parent"));
		String parentbro = request.getParameter("parentbro");
		saveEntity.setParentNode(parentbro);
		saveEntity.setOrderIndex(1);
		if (parentbro.equals("0")) {
			saveEntity.setLeaf(false);
		} else {
			saveEntity.setLeaf(true);
		}
		saveEntity.setNodeText(entity.getNodeText());
		saveEntity.setRemark(entity.getRemark());
		saveEntity.setCreateTime(new Date());
		saveEntity.setCreateUser(userCh);
		// saveEntity.BuildBroNode(parentbro, parEntity);

		entity = thisService.doMerge(saveEntity);

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

}
