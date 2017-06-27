package com.zd.school.oa.flow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StringVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.BaseApplayEntity;
import com.zd.core.util.DateUtil;
import com.zd.core.util.JsonBuilder;
import com.zd.school.jw.eduresources.model.JwGradeteacher;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwGradeteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.service.ApplayService;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Controller
@RequestMapping("/Process")
public class ProcessController extends FrameWorkController<LeaveApplay> implements Constant {

	@Resource
	private ProcessEngine processEngine;
	@Resource
	private TaskService taskService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private IdentityService identityService;
	@Resource
	private HistoryService historyService;
	@Resource
	private ApplayService applayService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private TeaTeacherbaseService teacherService;
	@Resource
	private PushInfoService pushInfoService;
	@Resource
	private BaseJobService baseJobService;
	@Resource
	private BaseOrgService baseOrgService;
	@Resource
	private JwTGradeService gradeService;
	@Resource
	private JwTGradeclassService gClassService;
	@Resource
	private JwGradeteacherService gTeacherService;
	
    private ServletContext servletContext = null;
    public void setServletContext( ServletContext servletContext )
    {
      this.servletContext = servletContext;
    } 

	// 启动流程
	@RequestMapping("/startProcess")
	public void startProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		// 获得页面所有表单
		Map<String, String[]> paramMap = request.getParameterMap();
		// Map<String, Object> map = convertMap(paramMap);
		String flowId = request.getParameter("flowId");
		String deptId = request.getParameter("deptleader");
		String deptName = baseOrgService.get(deptId).getNodeText();
		ProcessInstance pi = runtimeService.startProcessInstanceById(flowId);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		task.setOwner(currentUser.getUuid());                                                                                                                                                                                                                                                                                                                                                                                                                                               
		taskService.saveTask(task);
		// 把表单字段和当前任务绑定
		setVariable(task, paramMap);
		// taskService.setVariables(task.getId(), paramMap);
		taskService.setVariable(task.getId(), "deptName", deptName);
		// 完成提交申请任务
		taskService.complete(task.getId());

		Task nextTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		nextTask.setOwner(currentUser.getUuid());
		nextTask.setAssignee("f111ebab-933b-4e48-b328-c731ae792ca0");
		taskService.saveTask(nextTask);
		writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'流程启动成功'"));
	    //WebContext contex=WebContextFactory.get();  
	    ServerContext ctx = ServerContextFactory.get(servletContext);
	    
	    System.out.println(request.getContextPath());
	    
	    Collection<ScriptSession> sessions=ctx.getScriptSessionsByPage(request.getContextPath()+"/login/desktop");  
	  
	    Util util=new Util(sessions);  
	  
	    ScriptBuffer sb=new ScriptBuffer();  
	  
	    sb.appendScript("show(");  
	  
	    sb.appendData("推送");   
	  
	    sb.appendScript(")");  
	  
	    util.addScript(sb); 
	}

	// 显示待审批的列表
	@RequestMapping("/showTask")
	public void showTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		// 根据当前用户获得组
		List<Group> groups = identityService.createGroupQuery().groupMember(currentUser.getUuid()).list();
		// 任务列表
		List<Task> tasks = new ArrayList<Task>();
		// 根据组名查询得到任务列表 并添加到tasks里
		for (Group group : groups) {
			List<Task> list = taskService.createTaskQuery().taskAssignee(group.getName()).list();
			tasks.addAll(list);
		}

		// 查询出代理人和自己id匹配的
		List<Task> currentUserTask = taskService.createTaskQuery().taskAssigneeLike("%" + currentUser.getUuid() + "%")
				.list();
		tasks.addAll(currentUserTask);

		List<BaseApplayEntity> results = new ArrayList<BaseApplayEntity>();
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).active().singleResult();
			BaseApplayEntity baseApplay = new BaseApplayEntity();
			List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(processInstanceId).list();
			for (HistoricVariableInstance his : list) {
				if (his.getVariableName().equals("applayName")) {
					baseApplay.setApplayName(his.getValue().toString());
				}
				if (his.getVariableName().equals("applayUserName")) {
					baseApplay.setApplayUserName(his.getValue().toString());
				}
				if (his.getVariableName().equals("applayDate")) {
					baseApplay.setApplayDate(DateUtil.getTime(his.getValue().toString()));
				}
			}

			baseApplay.setExtField01(task.getExecutionId());
			baseApplay.setUuid(task.getProcessInstanceId());
			baseApplay.setTaskId(task.getId());
			baseApplay.setTaskName(task.getName());
			baseApplay.setTaskDefinitionKey(task.getProcessDefinitionId());
			baseApplay.setTaskAssignee(task.getAssignee());
			baseApplay.setTaskCreateTime(task.getCreateTime());
			baseApplay.setPiId(processInstance.getId());
			if (processInstance.isSuspended())
				baseApplay.setPiSuspended("挂起");
			else
				baseApplay.setPiSuspended("正常");

			results.add(baseApplay);
		}

		String strData = jsonBuilder.buildObjListToJson(new Long(results.size()), results, true);// 处理数据
		writeJSON(response, strData);// 返回数据

	}

	// 审批通过
	@RequestMapping("/complateTask")
	public void complateTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String taskId = request.getParameter("taskId");
		// 获得审批意见
		String opinion = request.getParameter("opinion") + "  同意";

		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批意见",
				opinion);
		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批人",
				currentUser.getXm());
		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批时间",
				DateUtil.formatDateTime(new Date()));

		Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();
		currentTask.setDescription("1");
		taskService.saveTask(currentTask);
		taskService.complete(taskId);

		Task nextTask = taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId())
				.singleResult();
		if (nextTask != null) {
			String deptId = runtimeService.getVariable(nextTask.getExecutionId(), "deptleader") + "";
			nextTask.setOwner(currentTask.getOwner());
			SysUser applayUser = sysUserService.get(currentTask.getOwner());
			SysUser approveUser = null;
			if (nextTask.getAssignee().equals("班主任")) {
				String classTeacherId = applayService.getClassLeaderList(currentTask.getOwner());
				if (classTeacherId.equals("-1")) {
					writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'没有找到班主任,审批失败'"));
					return;
				}
				// 设置本班班主任为代理人
				nextTask.setAssignee(classTeacherId);
				approveUser = sysUserService.get(classTeacherId);
			} else if (nextTask.getAssignee().equals("年级组长")) {
				BaseOrg org = baseOrgService.get(deptId);
				String hql = "from JwTGrade where gradeName='" + org.getNodeText() + "' and isDelete=0";
				List<JwTGrade> grades = gradeService.doQuery(hql);
				JwTGrade grade = grades != null && grades.size() > 0 ? grades.get(0) : null;
				hql = "from JwTGradeclass where className='" + org.getNodeText() + "' and isDelete=0";
				List<JwTGradeclass> gradeclasses = gClassService.doQuery(hql);
				JwTGradeclass gradeClass = gradeclasses != null && gradeclasses.size() > 0 ? gradeclasses.get(0) : null;

				// String[] proerties = new String[] { "gradeName", "isDelete"
				// };
				// Object[] values = new Object[] { org.getNodeText(), 0 };
				// JwTGrade grade = gradeService.getByProerties(proerties,
				// values);
				// proerties[0] = "className";
				// JwTGradeclass gradeClass =
				// gClassService.getByProerties(proerties, values);
				String gradeId = grade != null ? grade.getUuid() : gradeClass.getGraiId();
				// proerties = new String[] { "graiId", "category" };
				// values = new Object[] { gradeId, "0" };
				hql = "from JwGradeteacher where graiId='" + gradeId + "' and category=0 and isDelete=0";
				JwGradeteacher t = gTeacherService.doQuery(hql).get(0);
				// JwGradeteacher t = gTeacherService.getByProerties(proerties,
				// values);
				// 设置年级组长为代理人
				nextTask.setAssignee(t.getTteacId());
				approveUser = sysUserService.get(t.getTteacId());
			} else if (nextTask.getAssignee().equals("部门负责人")) {
				BaseOrg currentOrg = baseOrgService.get(deptId);
				BaseJob job = baseJobService.get(currentOrg.getMainLeader());
				approveUser = getUserByJob(job);
				nextTask.setAssignee(approveUser.getUuid());
			} else if (nextTask.getAssignee().startsWith("抄送:")) {
				// 是否抄送
				boolean isCC = nextTask.getAssignee().startsWith("抄送:");
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/"
						+ request.getRequestURI() + "?" + request.getQueryString();
				url = "";
				url += "/static/core/coreApp/flow/dynamicForm/hisApprove.jsp";
				url += "?processDefinitionId=" + nextTask.getProcessDefinitionId();
				url += "&executionId=" + nextTask.getExecutionId();
				while (nextTask != null && isCC) {
					nextTask.setOwner(applayUser.getUuid());
					pushInfoCC(applayUser, nextTask, url);
					nextTask.setDescription("1");
					taskService.saveTask(nextTask);
					taskService.complete(nextTask.getId());
					nextTask = taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId())
							.singleResult();
					if (nextTask == null) {
						break;
					}
					isCC = nextTask.getAssignee().startsWith("抄送:");
				}
				writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'办理成功'"));
				return;
			}
			// else if (nextTask.getAssignee().split("~").length == 2) {
			// String deptId =
			// runtimeService.getVariable(nextTask.getExecutionId(),
			// "deptleader") + "";
			// BaseOrg currentOrg = baseOrgService.get(deptId);
			// String[] arr = nextTask.getAssignee().split("~");
			// String deptName = arr[0];
			// String roleName = arr[1];
			// List<SysUser> list = sysUserService.getUserByRoleName(roleName);
			// for (SysUser sysUser : list) {
			// Set<BaseOrg> orgs = getUserDepts(sysUser);
			// for (BaseOrg baseOrg : orgs) {
			// if (baseOrg.getNodeText().equals(deptName)) { // 初中校团委,高中校团委
			// String treeIds = baseOrg.getTreeIds();
			// int treeIdsLen = treeIds.length();
			// String subDeptId = currentOrg.getTreeIds().substring(0,
			// treeIdsLen);
			// if (treeIds.equals(subDeptId)) {
			// nextTask.setAssignee(sysUser.getUuid());
			// approveUser = sysUser;
			// }
			// }
			// }
			// }
			// }
			else {
				approveUser = getApproveUser(deptId, nextTask); // 获得审批人
				nextTask.setAssignee(approveUser.getUuid()); // 并绑定到代理人那
			}
			pushInfo(approveUser, nextTask, approveUser.getExtField01());
			taskService.saveTask(nextTask);
		} else {
			String userId = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(currentTask.getProcessInstanceId()).variableName("applayUserId").singleResult()
					.getValue().toString();
			StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
			url.append("static/core/coreApp/flow/phoneapplay/hisApprove.jsp?");
			url.append("defineId=" + currentTask.getProcessDefinitionId());
			url.append("&executionId=" + currentTask.getExecutionId());
			SysUser user = sysUserService.get(userId);
			PushInfo pushInfo = new PushInfo();
			pushInfo.setEmplName(user.getXm());
			pushInfo.setEmplNo(user.getUserNumb());
			pushInfo.setRegTime(new Date());
			pushInfo.setEventType("消息提醒");
			pushInfo.setPushStatus(0);
			pushInfo.setPushWay(1);
			pushInfo.setRegStatus("您好,您提交的申请通过了!");
			pushInfo.setPushUrl(url.toString());
			pushInfoService.persist(pushInfo);
		}
		writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'办理成功'"));
	}

	// 审批驳回
	@RequestMapping("/stopTask")
	public @ResponseBody String stopTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String taskId = request.getParameter("taskId");
		// 获得审批意见
		String opinion = request.getParameter("opinion") + "  驳回";

		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批意见",
				opinion);
		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批人",
				currentUser.getXm());
		taskService.setVariable(taskId, "taskId:" + taskId + ",approveUserId:" + currentUser.getUuid() + ",审批时间",
				DateUtil.formatDateTime(new Date()));

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		task.setDescription("2");

		taskService.saveTask(task);
		runtimeService.deleteProcessInstance(task.getProcessInstanceId(), opinion);

		String userId = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).variableName("applayUserId").singleResult().getValue()
				.toString();
		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
		url.append("static/core/coreApp/flow/phoneapplay/hisApprove.jsp?");
		url.append("defineId=" + task.getProcessDefinitionId());
		url.append("&executionId=" + task.getExecutionId());
		SysUser user = sysUserService.get(userId);
		PushInfo pushInfo = new PushInfo();
		pushInfo.setEmplName(user.getXm());
		pushInfo.setEmplNo(user.getUserNumb());
		pushInfo.setRegTime(new Date());
		pushInfo.setEventType("消息提醒");
		pushInfo.setPushStatus(0);
		pushInfo.setPushWay(1);
		pushInfo.setRegStatus("您好,您提交的申请被驳回了!");
		pushInfo.setPushUrl(url.toString());
		pushInfoService.persist(pushInfo);

		return "true";
	}

	// 显示审批表单
	@RequestMapping("/approve")
	public void approve(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String taskId = request.getParameter("taskId");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).active().singleResult();
		LeaveApplay baseApplay = new LeaveApplay();
		baseApplay.setNextadvice("无");
		List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();
		for (HistoricVariableInstance his : list) {
			if (his.getVariableName().equals("applayName")) {
				baseApplay.setApplayName(his.getValue().toString());
			}
			if (his.getVariableName().equals("applayUserName")) {
				baseApplay.setApplayUserName(his.getValue().toString());
			}
			if (his.getVariableName().equals("applayDate")) {
				baseApplay.setApplayDate(DateUtil.getTime(his.getValue().toString()));
			}
			if (his.getVariableName().equals("startDate")) {
				baseApplay.setStartDate(DateUtil.getTime(his.getValue().toString()));
			}
			if (his.getVariableName().equals("endDate")) {
				baseApplay.setEndDate(DateUtil.getTime(his.getValue().toString()));
			}
			if (his.getVariableName().equals("leaveDay")) {
				baseApplay.setLeaveDay(Double.valueOf(his.getValue().toString()));
			}
			if (his.getVariableName().equals("leaveReason")) {
				baseApplay.setLeaveReason(his.getValue().toString());
			}
			if (his.getVariableName().indexOf("审批意见") != -1) {
				baseApplay.setNextadvice(his.getValue().toString());
			}
		}
		baseApplay.setTaskId(task.getId());
		baseApplay.setTaskName(task.getName());
		baseApplay.setTaskDefinitionKey(task.getTaskDefinitionKey());
		baseApplay.setTaskAssignee(task.getAssignee());
		baseApplay.setTaskCreateTime(task.getCreateTime());
		baseApplay.setPiId(processInstance.getId());
		if (processInstance.isSuspended())
			baseApplay.setPiSuspended("挂起");
		else
			baseApplay.setPiSuspended("正常");

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(baseApplay)));
	}

	//显示我的审批记录
	@RequestMapping("/showMyApprove")
	public @ResponseBody List<LeaveApplay> showMyApprove(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SysUser currentUser = getCurrentSysUser();
		List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery()
				.variableNameLike("%" + currentUser.getUuid() + "%").list();
		Set<String> procIds = new HashSet<String>();
		for (HistoricVariableInstance his : hviList) {
			procIds.add(his.getProcessInstanceId());
		}
		List<LeaveApplay> applays = new ArrayList<LeaveApplay>();
		LeaveApplay baseApplay = new LeaveApplay();
		for (String processInstanceId : procIds) {
			hviList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
			for (HistoricVariableInstance his : hviList) {
				if (baseApplay == null) {
					baseApplay = new LeaveApplay();
				}
				if (his.getVariableName().indexOf("审批意见") != -1
						&& his.getVariableName().indexOf(currentUser.getUuid()) != -1) {
					baseApplay.setNextadvice(his.getValue().toString());
					baseApplay.setApplayName(
							historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
									.variableName("applayName").singleResult().getValue().toString());
					baseApplay.setApplayUserName(
							historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
									.variableName("applayUserName").singleResult().getValue().toString());
				}
				if (baseApplay.getNextadvice() != null) {
					HistoricProcessInstance hisProcess =historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
					List<HistoricTaskInstance> hisTaskS= historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
					baseApplay.setTaskDefinitionKey(hisProcess.getProcessDefinitionId());
					baseApplay.setUuid(hisTaskS.get(0).getExecutionId());
					applays.add(baseApplay);
					baseApplay = null;
				}
			}
		}
		
		List<Task> tlist=taskService.createTaskQuery().processVariableValueEquals(currentUser.getUuid()).list();
		procIds.clear();
		for (Task task : tlist) {
			procIds.add(task.getProcessInstanceId());
		}
		for (String processInstanceId : procIds) {
			baseApplay = new LeaveApplay();
			ProcessInstance pInstance=runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			Map<String, Object> vars=pInstance.getProcessVariables();
			for (String key : vars.keySet()) {
				if (key.indexOf("审批意见")!=-1&& key.indexOf(currentUser.getUuid()) != -1) {
					baseApplay.setNextadvice(vars.get(key).toString());
					baseApplay.setApplayName(vars.get("applayName").toString());
					baseApplay.setApplayUserName(vars.get("applayUserName").toString());
					baseApplay.setTaskDefinitionKey(pInstance.getProcessDefinitionId());
					Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
					baseApplay.setUuid(task.getExecutionId());
					applays.add(baseApplay);
				}
			}
		}
		
		return applays;

	}

	// 显示历史审批信息
	@RequestMapping("/showApprove")
	public void showApprove(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String processInstanceId = request.getParameter("taskId");
		List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();
		HistoricProcessInstance hip = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		List<LeaveApplay> applays = new ArrayList<LeaveApplay>();
		LeaveApplay baseApplay = new LeaveApplay();
		baseApplay.setStartDate(hip.getStartTime());
		baseApplay.setEndDate(hip.getEndTime());
		HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).variableName("applayUserName").singleResult();
		baseApplay.setApplayUserName(var.getValue().toString());
		var = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
				.variableName("leaveReason").singleResult();
		baseApplay.setLeaveReason(var == null ? "" : var.getValue().toString());
		var = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
				.variableName("applayName").singleResult();
		baseApplay.setFlowName(var.getValue().toString());
		applays.add(baseApplay);
		baseApplay = null;
		for (HistoricVariableInstance his : list) {
			if (baseApplay == null) {
				baseApplay = new LeaveApplay();
			}
			if (his.getVariableName().indexOf("审批意见") != -1) {
				baseApplay.setUuid(his.getValue().toString());
				baseApplay.setApplayDate(his.getCreateTime());
			}
			if (his.getVariableName().indexOf("审批人") != -1) {
				baseApplay.setApplayName(his.getValue().toString());
			}
			if (baseApplay.getUuid() != null && baseApplay.getApplayName() != null) {
				applays.add(baseApplay);
				baseApplay = null;
			}
		}

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(applays)));
	}

	// 把表单字段和当前任务绑定
	private void setVariable(Task task, Map<String, String[]> paramMap) {
		for (String key : paramMap.keySet()) {
			String[] value = paramMap.get(key);
			// taskService.setVariable(task.getId(), key, value[0]);

			if (value.length == 1) {
				taskService.setVariable(task.getId(), key, value[0]);
			} else {
				String str = "";
				for (String string : value) {
					str += string + "&|";
				}
				str = str.substring(0, str.length() - 2);
				taskService.setVariable(task.getId(), key, str);
			}

		}
	}

	/**
	 * 获得审批人
	 * 
	 * @param applayUser
	 *            申请用户
	 * @param task
	 *            当前正在执行的任务
	 * @return 审批人
	 */
	private SysUser getApproveUser(String applayUserDeptId, Task task) {
		BaseOrg currentUserOrg = baseOrgService.get(applayUserDeptId); // 获得申请人所在部门
		String nextAssignee = task.getAssignee();// 获得下一个审批角色
		if (nextAssignee.startsWith("抄送:")) {
			nextAssignee = nextAssignee.substring(3, nextAssignee.length());
		}
		String[] arr = nextAssignee.split("~");
		if (arr.length == 2) { // 如果是教务处~主任
			String deptName = arr[0];
			String roleName = arr[1];
			List<SysUser> list = sysUserService.getUserByRoleName(roleName); // 先找角色
			if (list != null && list.size() == 1) { // 如果对应角色 只有一个默认就是他
				list.get(0).setExtField01(roleName);
				return list.get(0);
			}
			String userIdS = "";
			for (SysUser sysUser : list) {
				userIdS += "'" + sysUser.getUuid() + "',";
			}
			userIdS = userIdS.substring(0, userIdS.length() - 1);
			String hql = "from SysUser as u inner join fetch u.userDepts as d where u.uuid in(" + userIdS
					+ ") and d.nodeText='" + deptName + "' and d.isDelete=0 and u.isDelete=0";
			List<SysUser> list2 = sysUserService.doQuery(hql); // 在按部门名称
																// 和角色名称相同查找
			if (list2 != null && list2.size() == 1) {
				list2.get(0).setExtField01(roleName);
				return list2.get(0);
			}
			for (SysUser sysUser : list2) {
				Set<BaseOrg> orgs = getUserDepts(sysUser);
				for (BaseOrg baseOrg : orgs) {
					if (baseOrg.getNodeText().equals(deptName)) { // 找出部门名称相同
																	// 校团委 初中,高中
						String[] tempArr = baseOrg.getTreeIds().split(",");
						String tempStr = tempArr[0] + "," + tempArr[1]; // 截取前两位获得初高中部门
						int tempStrLen = tempStr.length();
						String subAplyUrOrg = currentUserOrg.getTreeIds().substring(0, tempStrLen); // 截取前两位获得申请人初高中部门
						if (subAplyUrOrg.equals(tempStr)) {
							sysUser.setExtField01(roleName);
							return sysUser;
						}
					}
				}
			}
		} else {
			List<SysUser> list = sysUserService.getUserByRoleName(nextAssignee); // 根据角色查询拥有这角色的人
			if (list != null && list.size() == 1) {
				list.get(0).setExtField01(nextAssignee);
				return list.get(0);
			}
			for (SysUser sysUser : list) {
				// BaseOrg tempOrg = baseOrgService.get(sysUser.getDeptId()); //
				// 遍历取得部门
				Set<BaseOrg> orgs = getUserDepts(sysUser);
				for (BaseOrg tempOrg : orgs) {
					// int tempOrgLen = tempOrg.getTreeIds().length();
					// 根据临时变量用户的部门下的TreeIds长度截取申请人的部门TreeIds
					// String applayUserOrg =
					// currentUserOrg.getTreeIds().substring(0, tempOrgLen);
					// if (tempOrg.getTreeIds().equals(applayUserOrg)) {
					// return sysUser;
					// }
					String[] tempArr = tempOrg.getTreeIds().split(",");
					String tempStr = tempArr[0] + "," + tempArr[1]; // 截取前两位获得部门
					int tempStrLen = tempStr.length();
					String subAplyUrOrg = currentUserOrg.getTreeIds().substring(0, tempStrLen); // 截取前两位获得申请人部门
					if (subAplyUrOrg.equals(tempStr)) { // 如果前两位部门相同
						Set<SysRole> roles = getUserRoles(sysUser);
						for (SysRole sysRole : roles) {
							if (sysRole.getRoleName().equals(nextAssignee)) { // 如果角色相同
								/**
								 * 如果部门前两位treeid和申请人部门前两位相同 (初中||高中)
								 * 而且这个角色名称和代理角色相同
								 */
								sysUser.setExtField01(nextAssignee);
								return sysUser;
							}
						}
					}
				}

			}
		}

		return null;
	}

	// 获得当前流程到哪个位置
	@RequestMapping("/showImage")
	public String showImage(Model model, @RequestParam("excuteId") String excuteId,
			@RequestParam("procIstid") String procIstid) {
		ActivityImpl wfLeaveImag = null;
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procIstid).singleResult();
		HistoricProcessInstance hisPi = historyService.createHistoricProcessInstanceQuery().processInstanceId(procIstid)
				.singleResult();
		String procDefId = pi != null ? pi.getProcessDefinitionId() : hisPi.getProcessDefinitionId();
		try {
			wfLeaveImag = WFUtil.getProcessMap(procDefId, excuteId, processEngine);
			model.addAttribute("wfLeaveImag", wfLeaveImag);
			model.addAttribute("procDefId", procDefId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "static/core/coreApp/flow/myapply/view/showImg";
	}

	// 显示流程图
	@RequestMapping("/findPic")
	public void findPic(@RequestParam("procDefId") String procDefId, HttpServletResponse response) {
		try {
			InputStream pic = WFUtil.findProcessPic(procDefId, processEngine);
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = pic.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/currentUserDepts")
	public @ResponseBody Set<BaseOrg> currentUserDepts(HttpServletRequest request, HttpServletResponse response) {
		SysUser user = getCurrentSysUser();
		String hql = "from SysUser as u inner join fetch u.userDepts as d where u.uuid='" + user.getUuid()
				+ "' and d.isDelete=0";
		List<SysUser> list = sysUserService.doQuery(hql);
/*		for (SysUser sysUser : list) {
			Set<BaseOrg> orgs = sysUser.getUserDepts();
			return orgs;
		}*/
		return null;
	}

	private Set<SysRole> getUserRoles(SysUser user) {
		String hql = "from SysUser as u inner join fetch u.sysRoles as r where u.uuid='" + user.getUuid()
				+ "' and r.isDelete=0";
		SysUser u = sysUserService.doQuery(hql).get(0);
		return u.getSysRoles();
	}

	private Set<BaseOrg> getUserDepts(SysUser user) {
/*		String hql = "from SysUser as u inner join fetch u.userDepts as d where u.uuid='" + user.getUuid()
				+ "'and d.isDelete=0";
		SysUser u = sysUserService.doQuery(hql).get(0);
		return u.getUserDepts();*/
		return  null;
	}

	private SysUser getUserByJob(BaseJob job) {
		String hql = "from SysUser as u inner join fetch u.userJobs as j where j.uuid='" + job.getUuid()
				+ "'and j.isDelete=0 and u.isDelete=0";
		return sysUserService.doQuery(hql).get(0);
	}

	// 推送消息
	private void pushInfo(SysUser approveUser, Task task, String roleName) {
		// SysUser approveUser = getApproveUser(applayUser, task);
		// String url = StringVeriable.WEB_URL + "";
		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
		url.append("static/core/coreApp/flow/phoneapplay/approve.jsp?");
		url.append("defineId=" + task.getProcessDefinitionId());
		url.append("&taskId=" + task.getId());
		url.append("&executionId=" + task.getExecutionId());
		url.append("&userId=" + approveUser.getUuid());

		TeaTeacherbase teacher = new TeaTeacherbase();
		teacher = teacherService.get(approveUser.getUuid());
		PushInfo pushInfo = new PushInfo();
		pushInfo.setEmplName(approveUser.getXm());
		pushInfo.setEmplNo(teacher.getGh());
		pushInfo.setRegTime(new Date());
		pushInfo.setEventType("审批提醒");
		pushInfo.setPushStatus(0);
		pushInfo.setPushWay(1);
		pushInfo.setPushUrl(url.toString());
		pushInfo.setRegStatus("您好," + approveUser.getXm() + "老师,有申请需要您尽快审批!");
		pushInfoService.persist(pushInfo);

		// String hql = "from SysUser where jobName='" + task.getAssignee() + "'
		// or uuid like '%" + task.getAssignee()
		// + "%' and isDelete=0";
		// List<SysUser> userList = sysUserService.doQuery(hql);
		// for (SysUser sysUser : userList) {
		// TeaTeacherbase teacher = new TeaTeacherbase();
		// teacher = teacherService.get(sysUser.getUuid());
		// PushInfo pushInfo = new PushInfo();
		// pushInfo.setEmplName(sysUser.getXm());
		// pushInfo.setEmplNo(teacher.getGh());
		// pushInfo.setRegTime(new Date());
		// pushInfo.setEventType("审批提醒");
		// pushInfo.setPushStatus(0);
		// pushInfo.setPushWay(1);
		// pushInfo.setRegStatus("您好," + sysUser.getXm() + "老师,有申请需要您尽快审批!");
		// pushInfoService.persist(pushInfo);
		// }
	}

	// 抄送消息
	private void pushInfoCC(SysUser applayUser, Task task, String url) {

		// SysUser approveUser = getApproveUser(deptId, task);
		// String processInstanceId = task.getProcessInstanceId();
		// HistoricVariableInstance var =
		// historyService.createHistoricVariableInstanceQuery()
		// .processInstanceId(processInstanceId).variableName("applayUserName").singleResult();
		// String applayUserName = var.getValue().toString();
		// var =
		// historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
		// .variableName("applayName").singleResult();
		// String applayName = var.getValue().toString();
		// TeaTeacherbase teacher = teacherService.get(approveUser.getUuid());
		// PushInfo pushInfo = new PushInfo();
		// pushInfo.setEmplName(approveUser.getXm());
		// pushInfo.setEmplNo(teacher.getGh());
		// pushInfo.setRegTime(new Date());
		// pushInfo.setEventType("消息提醒");
		// pushInfo.setPushStatus(0);
		// pushInfo.setPushWay(1);
		// pushInfo.setRegStatus("您好," + approveUser.getXm() + "老师," +
		// applayUserName + "提交的" + applayName + "审批通过了,"
		// + task.getDescription());
		// pushInfoService.persist(pushInfo);

		// String assignee = task.getAssignee();
		// String jobName = assignee.substring(3, assignee.length());
		//
		// String hql = "from SysUser where jobName='" + jobName + "' and
		// isDelete=0";
		// String processInstanceId = task.getProcessInstanceId();
		// HistoricVariableInstance var =
		// historyService.createHistoricVariableInstanceQuery()
		// .processInstanceId(processInstanceId).variableName("applayUserName").singleResult();
		// String applayUserName = var.getValue().toString();
		// var =
		// historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
		// .variableName("applayName").singleResult();
		// String applayName = var.getValue().toString();
		// List<SysUser> userList = sysUserService.doQuery(hql);
		// for (SysUser sysUser : userList) {
		// TeaTeacherbase teacher = teacherService.get(sysUser.getUuid());
		// PushInfo pushInfo = new PushInfo();
		// pushInfo.setEmplName(sysUser.getXm());
		// pushInfo.setEmplNo(teacher.getGh());
		// pushInfo.setRegTime(new Date());
		// pushInfo.setEventType("消息提醒");
		// pushInfo.setPushStatus(0);
		// pushInfo.setPushWay(1);
		// pushInfo.setRegStatus("您好," + sysUser.getXm() + "老师," +
		// applayUserName + "提交的" + applayName + "审批通过了,"
		// + task.getDescription());
		// pushInfoService.persist(pushInfo);

		List<SysUser> pushList = new ArrayList<SysUser>();
		String nextAssignee = task.getAssignee();
		nextAssignee = nextAssignee.substring(3, nextAssignee.length());
		String[] arr = nextAssignee.split("~");
		String roleName;
		if (arr.length == 2) { // 如果是教务处~主任
			String deptName = arr[0];
			roleName = arr[1];
			List<SysUser> list = sysUserService.getUserByRoleName(roleName); // 先找角色
			if (list != null && list.size() == 1) { // 如果对应角色 只有一个默认就是他
				pushList.add(list.get(0));
				pushInfo(pushList, task, roleName, url);
				return;
			}

			Set<BaseOrg> userSet = getUserDepts(applayUser);

			for (SysUser sysUser : list) {
				try {
					Set<BaseOrg> tempSet = getUserDepts(sysUser);
					for (BaseOrg baseOrg : tempSet) {
						if (baseOrg.getNodeText().equals(deptName)) {
							for (BaseOrg tempOrg : userSet) {
								String[] userTreeIds = tempOrg.getTreeIds().split(",");
								String[] tempTreeIds = baseOrg.getTreeIds().split(",");
								String userTwo = userTreeIds[0] + userTreeIds[1];
								String tempTwo = tempTreeIds[0] + tempTreeIds[1];
								if (userTwo.equals(tempTwo)) {
									pushList.add(sysUser);
								}
							}
						}
					}
				} catch (Exception e) {

				}
			}

		} else {
			List<SysUser> list = sysUserService.getUserByRoleName(nextAssignee); // 根据角色查询拥有这角色的人
			pushList.addAll(list);
			roleName = nextAssignee;
		}
		pushInfo(pushList, task, roleName, url);

	}

	public void pushInfo(List<SysUser> pushList, Task task, String roleName, String url) {
		for (SysUser sysUser : pushList) {
			String processInstanceId = task.getProcessInstanceId();
			HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(processInstanceId).variableName("applayUserName").singleResult();
			String applayUserName = var.getValue().toString();
			var = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
					.variableName("applayName").singleResult();
			String applayName = var.getValue().toString();
			TeaTeacherbase teacher = teacherService.get(sysUser.getUuid());
			PushInfo pushInfo = new PushInfo();
			pushInfo.setEmplName(sysUser.getXm());
			pushInfo.setEmplNo(teacher.getGh());
			pushInfo.setRegTime(new Date());
			pushInfo.setEventType("消息提醒");
			pushInfo.setPushStatus(0);
			pushInfo.setPushWay(1);
			pushInfo.setExtField01(url);
			pushInfo.setRegStatus("您好," + sysUser.getXm() + roleName + "," + applayUserName + "提交的" + applayName
					+ "审批通过了!" + task.getDescription());
			pushInfoService.persist(pushInfo);
		}
	}

}