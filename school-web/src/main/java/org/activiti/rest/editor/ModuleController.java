package org.activiti.rest.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zd.activiti.oa.model.model.ActivitiModel;
import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.oa.flow.model.DynamicForm;
import com.zd.school.oa.flow.model.FlowType;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.model.TaskBoundForm;
import com.zd.school.oa.flow.model.WfPorcessDefinition;
import com.zd.school.oa.flow.service.DynamicFormService;
import com.zd.school.oa.flow.service.FlowTypeService;
import com.zd.school.oa.flow.service.TaskBoundFormService;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.service.SysRoleService;

@Controller
@RequestMapping("/model")
public class ModuleController extends FrameWorkController<BaseEntity> implements Constant {

	private Logger logger = LoggerFactory.getLogger(ModuleController.class);

	@Autowired
	private RepositoryService repositoryService;

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private DynamicFormService dynamicFormService;

	@Resource
	private TaskBoundFormService taskBoundFormService;
	
	@Resource
	private BaseJobService jobService;

	@Resource
	private FlowTypeService thisService;
	
	@Resource
	private SysRoleService roleService; 

	@RequestMapping(value = "create")
	public void create(@RequestParam("name") String name, @RequestParam("key") String key,
			@RequestParam("description") String description, @RequestParam("category") String category,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setCategory(category);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

			System.out.println(request.getContextPath());
			String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/modeler.jsp?modelId=" + modelData.getId();
			// response.sendRedirect(request.getContextPath() +
			// "/modeler.jsp?modelId=" + modelData.getId());
			writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'" + path + "'"));
		} catch (Exception e) {
			logger.error("新建流程模型失败", e);
		}
	}

	/**
	 * 模型列表
	 * 
	 * @throws IOException
	 * 
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/modelList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void modelList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*
		 * String page=req.getParameter("page"); String
		 * rows=req.getParameter("rows"); int currentpage =
		 * Integer.parseInt((page == null || page.equals("0")) ? "1":
		 * page);//第几页 int pagesize = Integer.parseInt((rows == null ||
		 * rows.equals("0")) ? "10": rows);//每页多少行 Map<String, Object> map = new
		 * HashMap<String, Object>();
		 */
		String strData = "";
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit = Integer.parseInt(request.getParameter("limit"));
		String whereSql = request.getParameter("whereSql");
		long count = repositoryService.createModelQuery().list().size();
		List<ActivitiModel> modelList = new ArrayList<ActivitiModel>();
		try {
			List<Model> modelList1 = repositoryService.createModelQuery().listPage(start, limit);
			if (modelList1 != null && modelList1.size() > 0) {
				for (Model model : modelList1) {
					ActivitiModel activitiModel = new ActivitiModel();
					activitiModel.setId(model.getId());
					activitiModel.setCreateTime((model.getCreateTime()).toLocaleString());
					activitiModel.setDescription(model.getMetaInfo());
					activitiModel.setKey(model.getKey());
					activitiModel.setLastUpdateTime((model.getLastUpdateTime()).toLocaleString());
					activitiModel.setName(model.getName());
					activitiModel.setVersion(model.getVersion());
					activitiModel.setMetaInfo(model.getMetaInfo());
					modelList.add(activitiModel);
				}
			}
			/*
			 * int total= repositoryService.createModelQuery().list().size();
			 * map.put("total", total); map.put("rows", modelList1);
			 */

		} catch (Exception e) {
			e.getStackTrace();
		}
		strData = jsonBuilder.buildObjListToJson(new Long(count), modelList, true);
		writeJSON(response, strData);
	}

	/**
	 * @throws IOException 根据模型id部署流程定义
	 * 
	 * @author：tuzongxun @Title: deploye @param @param
	 * activitiModel @param @param redirectAttributes @param @return @return
	 * Object @date Mar 17, 2016 12:30:05 PM @throws
	 */
	@RequestMapping(value = "/deploye", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void deploye(
			@RequestParam("modelId") String modelId, /*
														 * @RequestParam("formId")
														 * String formId,
														 */
			HttpServletRequest req, HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			String processName = modelData.getName() + ".bpmn20.xml";

			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addString(processName, new String(bpmnBytes, "utf-8")).deploy();

			ProcessDefinition pdf = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
					.singleResult();
			repositoryService.setProcessDefinitionCategory(pdf.getId(), modelData.getCategory());
			repositoryService.addCandidateStarterGroup(pdf.getId(), "843bb53b-09f6-422e-9575-82d541478469");
			if (deployment != null && deployment.getId() != null) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'部署成功'"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeJSON(response, jsonBuilder.returnSuccessJson("'部署失败'"));

		}
		// writeJSON(response, jsonBuilder.returnSuccessJson("部署失败"));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void delete(@RequestParam("modelId") String modelId, HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		/*
		 * JSONObject result = new JSONObject(); for(String id : ids){
		 * repositoryService.deleteModel(id); } result.put("msg", "删除成功");
		 * result.put("type", "success"); return result.toString();
		 */
		repositoryService.deleteModel(modelId);
		writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
	}

	@RequestMapping(value = "/formlist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<DynamicForm> formlist(HttpServletRequest req) {
		String hql = "from DynamicForm";
		List<DynamicForm> list = dynamicFormService.doQuery(hql);// 执行查询方法
		return list;

	}

	@RequestMapping(value = "/tasklist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<WfPorcessDefinition> tasklist(HttpServletRequest req)
			throws IllegalAccessException, InvocationTargetException {
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
		List<WfPorcessDefinition> wflist = new ArrayList<WfPorcessDefinition>();
		for (ProcessDefinition pd : list) {

			WfPorcessDefinition wfpd = new WfPorcessDefinition();
			BeanUtils.copyProperties(wfpd, pd);
			wflist.add(wfpd);
		}

		return wflist;

	}

	@RequestMapping(value = "/saveform", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void saveform(@RequestParam("uuid") String formid, @RequestParam("id") String taskid, HttpServletRequest req,
			HttpServletResponse response) throws IOException {
		/*
		 * JSONObject result = new JSONObject(); for(String id : ids){
		 * repositoryService.deleteModel(id); } result.put("msg", "删除成功");
		 * result.put("type", "success"); return result.toString();
		 */
		TaskBoundForm t = new TaskBoundForm();
		String sql = "delete FLOW_T_TASKBOUNDFORM where taskid='" + taskid + "'";
		taskBoundFormService.executeSql(sql);
		t.setFormId(formid);
		t.setTaskId(taskid);
		taskBoundFormService.merge(t);
		writeJSON(response, jsonBuilder.returnSuccessJson("'绑定成功'"));

	}
	
	@RequestMapping(value = "/saveJob", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void saveJob(@RequestParam("uuid") String jobid, @RequestParam("id") String taskid, HttpServletRequest req,
			HttpServletResponse response) throws IOException {
		repositoryService.addCandidateStarterGroup(taskid,jobid);
		writeJSON(response, jsonBuilder.returnSuccessJson("'绑定成功'"));

	}

	@RequestMapping(value = "/flowlist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<FlowType> flowlist(HttpServletRequest request) {
		/*
		 * JSONObject result = new JSONObject(); for(String id : ids){
		 * repositoryService.deleteModel(id); } result.put("msg", "删除成功");
		 * result.put("type", "success"); return result.toString();
		 */
		String hql = "from FlowType";
		List<FlowType> list = thisService.doQuery(hql);
		return list;

	}
	
	@RequestMapping(value = "/joblist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<BaseJob> joblist(HttpServletRequest req) {
		String hql = "from BaseJob where isDelete=0";
		List<BaseJob> list = jobService.doQuery(hql);// 执行查询方法
		return list;

	}
	
	@RequestMapping(value = "/rolelist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<SysRole> rolelist(HttpServletRequest req) {
		String hql = "from SysRole where isDelete=0";
		List<SysRole> list = roleService.doQuery(hql);// 执行查询方法
		return list;

	}
}
