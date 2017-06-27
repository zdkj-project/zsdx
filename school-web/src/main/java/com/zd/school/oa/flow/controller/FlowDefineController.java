/**
 * Project Name:jw-web
 * File Name:FlowController.java
 * Package Name:com.zd.school.jw.controller.flow
 * Date:2016年3月29日下午4:08:52
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.flow.model.WfPorcessDefinition;
import com.zd.school.oa.flow.service.WorkFlowService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * ClassName:FlowController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年3月29日 下午4:08:52
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Controller
@RequestMapping("/flowdefine")
public class FlowDefineController extends FrameWorkController<BaseEntity> implements Constant {

	@Resource
	private WorkFlowService thisService;

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private RepositoryService repositoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strData = "";
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit = Integer.parseInt(request.getParameter("limit"));
		String whereSql = request.getParameter("whereSql");

		SysUser currentUser = getCurrentSysUser();
		long count;
		List<ProcessDefinition> list;
		if (StringUtils.isEmpty(whereSql)) {
			count = thisService.getPdCount(currentUser.getUuid());
			list = (List<ProcessDefinition>) thisService.findPdByPage(start, limit, currentUser.getUuid());
		} else {
			list = (List<ProcessDefinition>) thisService.findPdByPage(start, limit, whereSql, currentUser.getUuid());
			count = thisService.getPdCount(whereSql, currentUser.getUuid());
		}

		List<WfPorcessDefinition> wflist = new ArrayList();
		for (ProcessDefinition pd : list) {

			WfPorcessDefinition wfpd = new WfPorcessDefinition();
			BeanUtils.copyProperties(wfpd, pd);
			wflist.add(wfpd);
		}

		strData = jsonBuilder.buildObjListToJson(new Long(count), wflist, true);
		writeJSON(response, strData);
	}

	// 删除流程定义信息
	@RequestMapping("/doRealDelete")
	public void doRealDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.delteProcessDefinitionById(delIds);
			if (flag) {
				writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'删除失败'"));
			}
		}
	}

	// 部署流程
	@RequestMapping("/deploy")
	public void deploy(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 先将本地的文件上传到服务器
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");

		// 默认的发布目录
		String deployDir = "e:/workspace/jw//jw-web/src/main/webapp/static//upload/flow";
		if (file != null) {
			boolean flag = thisService.deployment(file, deployDir);
			if (flag)
				writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'流程部署成功'"));
			else {
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'流程部署失败'"));
			}
		} else
			writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'流程部署失败'"));
	}

	/**
	 * 读取资源，通过部署ID
	 *
	 * @param processDefinitionId
	 *            流程定义
	 * @param resourceType
	 *            资源类型(xml|image)
	 * @throws Exception
	 */
	@RequestMapping(value = "/resource/read")
	public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("resourceType") String resourceType, HttpServletResponse response) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

}
