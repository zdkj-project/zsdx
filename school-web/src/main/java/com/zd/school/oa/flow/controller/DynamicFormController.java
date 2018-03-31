package com.zd.school.oa.flow.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.flow.model.DynamicForm;
import com.zd.school.oa.flow.model.TaskBoundForm;
import com.zd.school.oa.flow.service.DynamicFormService;
import com.zd.school.oa.flow.service.TaskBoundFormService;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/DynamicForm")
public class DynamicFormController extends FrameWorkController<DynamicForm> implements Constant {

	@Resource
	private DynamicFormService thisService;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private TaskBoundFormService taskBoundFormService;
	
	@Resource
	private HistoryService historyService;

	@RequestMapping("/addForm")
	public void addForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// String type_value = request.getParameter("type_value");
		String parse_form = request.getParameter("parse_form"); // 表单内容
		String formType = request.getParameter("formType"); // 表单标题
		String formId = request.getParameter("formId"); // ID
		String extField01 = request.getParameter("extField01"); // 所属类型
		String extField02 = request.getParameter("extField02"); // 备注信息
		int index1 = parse_form.indexOf("\"template\":");
		int index2 = parse_form.indexOf("\"parse\":");
		String string = parse_form.substring(index1 + 12, index2 - 6);
		string = string.replace("\\", "");

		string = string.replace("{", "");
		string = string.replace("}", "");
		string = string.replace("|", "");
		string = string.replace("-", "");
		string = string.replace("?", "");

		// string = string.replace("{|-", "");
		// string = string.replace("-|}", "");
		DynamicForm formModel = new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			formModel = thisService.get(formId);
		}
		formModel.setFormType(formType);
		formModel.setForm(string);
		formModel.setExtField01(extField01);
		formModel.setExtField02(extField02);
		thisService.doMerge(formModel);
	}

	@RequestMapping("/getFormList")
	public void getFormList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit = Integer.parseInt(request.getParameter("limit"));
		String whereSql = request.getParameter("whereSql");
		String hql = "from DynamicForm where 1=1 ";
		if (StringUtils.isNotEmpty(whereSql)) {
			hql += whereSql;
		}
		List<DynamicForm> list = thisService.getQuery(hql, start, limit);
		int count = thisService.getQuery(hql).size();

		String strData = jsonBuilder.buildObjListToJson(new Long(count), list, true);
		writeJSON(response, strData);
	}

	@RequestMapping("/getById")
	public @ResponseBody List<DynamicForm> getById(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<DynamicForm> list = thisService.queryByProerties("uuid", request.getParameter("formId"));
		return list;
	}

	@RequestMapping("/getByTaskId")
	public @ResponseBody List<DynamicForm> getByTaskId(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String taskId = request.getParameter("taskId");
		TaskBoundForm boundForm = taskBoundFormService.getByProerties("taskId", taskId);
		List<DynamicForm> list = thisService.queryByProerties("uuid", boundForm.getFormId());
		return list;
	}

	@RequestMapping("/deleteForm")
	public void deleteForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean flag = thisService.doDeleteByPK(request.getParameter("formId"));
		if (flag)
			writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'删除成功'"));
		else
			writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'删除失败'"));
	}

	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.doDeleteByPK(delIds);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	@RequestMapping("/getVariables")
	public @ResponseBody Map<String, Object> getVariables(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String executionId = request.getParameter("executionId");
		Map<String, Object> map = runtimeService.getVariables(executionId);
		return map;
	}
	
	@RequestMapping("/getHisVariables")
	public @ResponseBody Map<String, Object> getHisVariables(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<HistoricVariableInstance> list =historyService.createHistoricVariableInstanceQuery().executionId(request.getParameter("executionId")).list();
		Map<String, Object> map = new HashMap<String, Object>();
		for (HistoricVariableInstance hisVar : list) {
			map.put(hisVar.getVariableName(), hisVar.getValue());
		}
		return map;
	}
	
	

}
