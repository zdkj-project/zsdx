package com.zd.school.app.oa.flow;

import com.zd.core.constant.StringVeriable;
import com.zd.core.model.BaseApplayEntity;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DateUtil;
import com.zd.school.jw.eduresources.model.JwGradeteacher;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwGradeteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.model.app.LeaveApplayInfoApp;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.oa.flow.model.DynamicForm;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.model.TaskBoundForm;
import com.zd.school.oa.flow.model.WfPorcessDefinition;
import com.zd.school.oa.flow.service.ApplayService;
import com.zd.school.oa.flow.service.DynamicFormService;
import com.zd.school.oa.flow.service.TaskBoundFormService;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
@RequestMapping("/app/OaProcess")
public class OaProcessAppController {

	@Resource
	private ProcessEngine processEngine;
	@Resource
	private RepositoryService repositoryService;
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
	private SysUserService userService;
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

	@Resource
	private TaskBoundFormService taskBoundFormService;

	@Resource
	private DynamicFormService formService;

	// 获得流程定义列表
	@RequestMapping(value = { "/getDefineList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<WfPorcessDefinition> getFlowDefineList(@RequestParam("userId") String userId,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {
		ProcessDefinitionQuery pdQeury = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> definitions = pdQeury.latestVersion().startableByUser(userId).list();
		List<WfPorcessDefinition> wflist = new ArrayList<WfPorcessDefinition>();
		for (ProcessDefinition pd : definitions) {
			WfPorcessDefinition wfpd = new WfPorcessDefinition();
			BeanUtils.copyProperties(wfpd, pd);
			wflist.add(wfpd);
		}
		return wflist;
	}

	//获取表单
	@RequestMapping(value = { "/getForm" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody DynamicForm getForm(@RequestParam("defineId") String defineId, HttpServletRequest request,
			HttpServletResponse response) {
		TaskBoundForm boundForm = taskBoundFormService.getByProerties("taskId", defineId);
		DynamicForm form = formService.get(boundForm.getFormId());
		return form;
	}

	//获得用户部门
	@RequestMapping(value = { "/getUserDepts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<BaseOrg> currentUserDepts(@RequestParam("userId") String userId,
			HttpServletRequest request, HttpServletResponse response) {
		String hql = "from SysUser as u inner join fetch u.userDepts as d where u.uuid='" + userId
				+ "' and d.isDelete=0";

		List<SysUser> list = userService.doQuery(hql);
		SysUser u = list.get(0);
/*		List<BaseOrg> listTemp = new ArrayList<BaseOrg>();
		listTemp.addAll(u.getUserDepts());
		String [] deptIds = new String[listTemp.size()];
		for (int i = 0; i < listTemp.size(); i++) {
			deptIds[i] = listTemp.get(i).getUuid();
			
		}*/
		String [] deptIds = new String[0];
		Map<String, String> sortedCondition = new HashMap<String ,String >();
		sortedCondition.put("nodeLevel", "ASC");
		sortedCondition.put("deptType", "ASC");
		
		List<BaseOrg> listJob = baseOrgService.queryByProerties("uuid", deptIds,sortedCondition);		
		//Set<BaseOrg> sets = new HashSet<BaseOrg>();
		//sets.addAll(listJob);			
//		for (SysUser sysUser : list) {
//			Set<BaseOrg> orgs = sysUser.getUserDepts();
//			return orgs;
//		}
		return listJob;
	}

	//获得用户基本信息
	@RequestMapping(value = { "/getUserInfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody SysUser getUserInfo(@RequestParam("userId") String userId, HttpServletRequest request,
			HttpServletResponse response) {
		return userService.get(userId);
	}

	//判断当前任务是否结束
	@RequestMapping(value = { "/taskIsExist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String taskIsExist(@RequestParam("taskId") String taskId, HttpServletRequest request,
			HttpServletResponse response) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task != null) {
			return "yes";
		} else {
			return "no";
		}
	}
	
	@RequestMapping(value = { "/userIsTeacher" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody String userIsTeacher(@RequestParam("userId") String userId, HttpServletRequest request,
			HttpServletResponse response) {
		TeaTeacherbase teacherbase= teacherService.get(userId);
		if (teacherbase != null) {
			return "yes";
		} else {
			return "no";
		}
	}


	// 启动流程
	@RequestMapping(value = { "/startProcess" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody LeaveApplayInfoApp startProcess(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		LeaveApplayInfoApp infoApp = new LeaveApplayInfoApp();
		String userId = request.getParameter("applayUserId");
		SysUser currentUser = userService.get(userId);
		// 获得页面所有表单
		Map<String, String[]> paramMap = request.getParameterMap();
		// Map<String, Object> map = convertMap(paramMap);
		String flowId = request.getParameter("defineId");
		String deptId = request.getParameter("deptleader");
		BaseOrg org = baseOrgService.get(deptId);
		String deptName = org.getNodeText();
		ProcessInstance pi = runtimeService.startProcessInstanceById(flowId);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		task.setOwner(currentUser.getUuid());
		taskService.saveTask(task);
		// 把表单字段和当前任务绑定
		setVariable(task, paramMap);
		taskService.setVariable(task.getId(), "deptName", deptName);
		// 完成提交申请任务
		taskService.complete(task.getId());

		Task nextTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		nextTask.setOwner(currentUser.getUuid());
		String tteacId;
		String nextTaskAssignee = nextTask.getAssignee();
		SysUser approveUser = null;
		if (nextTaskAssignee.equals("班主任")) {
			String classTeacherId = applayService.getClassLeaderList(currentUser.getUuid());
			if (classTeacherId.equals("-1")) {
				infoApp.setMessageInfo("没有找到班主任,流程启动失败");
				return infoApp;
			}
			// 设置本班班主任为代理人
			nextTask.setAssignee(classTeacherId);
			approveUser = userService.get(classTeacherId);
		} else if (nextTaskAssignee.equals("年级组长")) {
			String hql = "from JwTGrade where gradeName='" + org.getNodeText() + "' and isDelete=0";
			List<JwTGrade> grades = gradeService.doQuery(hql);
			JwTGrade grade = grades != null && grades.size() > 0 ? grades.get(0) : null;
			hql = "from JwTGradeclass where className='" + org.getNodeText() + "' and isDelete=0";
			List<JwTGradeclass> gradeclasses = gClassService.doQuery(hql);
			JwTGradeclass gradeClass = gradeclasses != null && gradeclasses.size() > 0 ? gradeclasses.get(0) : null;
			String gradeId = grade != null ? grade.getUuid() : gradeClass.getGraiId();
			hql = "from JwGradeteacher where graiId='" + gradeId + "' and category=0 and isDelete=0";
			JwGradeteacher t = gTeacherService.doQuery(hql).get(0);
			// 设置年级组长为代理人
			nextTask.setAssignee(t.getTteacId());
			approveUser = userService.get(t.getTteacId());
		} else if ((tteacId = request.getParameter("tteacId")) != null) {
			// 如果是指导老师审批
			nextTask.setAssignee(tteacId);
			approveUser = userService.get(tteacId);
		} else if (nextTask.getAssignee().equals("部门负责人")) {
			BaseJob job = baseJobService.get(org.getMainLeader());
			approveUser = getUserByJob(job);
			nextTask.setAssignee(approveUser.getUuid());
		} else if (nextTaskAssignee.startsWith("抄送:")) {
			// 是否抄送
			boolean isCC = nextTaskAssignee.startsWith("抄送:");
			String url = "";
			url += "/static/core/coreApp/flow/dynamicForm/hisApprove.jsp";
			url += "?processDefinitionId=" + task.getProcessDefinitionId();
			url += "&executionId=" + task.getExecutionId();
			while (nextTask != null && isCC) {
				nextTask.setOwner(currentUser.getUuid());
				pushInfoCC(currentUser, nextTask, url);
				nextTask.setDescription("1");
				taskService.saveTask(nextTask);
				taskService.complete(nextTask.getId());
				nextTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
				if (nextTask == null) {
					break;
				}
				isCC = nextTask.getAssignee().startsWith("抄送:");
			}
			infoApp.setMessageInfo("流程启动成功");
			return infoApp;
		}

		else {
			approveUser = getApproveUser(deptId, nextTask); // 获得审批人
			nextTask.setAssignee(approveUser.getUuid()); // 并绑定到代理人那
		}
		pushInfo(approveUser, nextTask, approveUser.getExtField01());
		taskService.saveTask(nextTask);
		infoApp.setMessageInfo("流程启动成功");
		return infoApp;
	}

	// 显示待审批的列表
	@RequestMapping("/showTask")
	public @ResponseBody List<BaseApplayEntity> showTask(@RequestParam("approveUserId") String userId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = userService.get(userId);
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
		return results;
	}

	// 审批通过
	@RequestMapping("/complateTask")
	public @ResponseBody LeaveApplayInfoApp complateTask(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		LeaveApplayInfoApp infoApp = new LeaveApplayInfoApp();
		String approveUserId = request.getParameter("approveUserId");
		SysUser currentUser = userService.get(approveUserId);
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
			SysUser applayUser = userService.get(currentTask.getOwner());
			SysUser approveUser = null;
			if (nextTask.getAssignee().equals("班主任")) {
				String classTeacherId = applayService.getClassLeaderList(currentTask.getOwner());
				if (classTeacherId.equals("-1")) {
					infoApp.setMessageInfo("没有找到班主任,审批失败");
					return infoApp;
				}
				// 设置本班班主任为代理人
				nextTask.setAssignee(classTeacherId);
				approveUser = userService.get(classTeacherId);
			} else if (nextTask.getAssignee().equals("年级组长")) {
				BaseOrg org = baseOrgService.get(deptId);
				String hql = "from JwTGrade where gradeName='" + org.getNodeText() + "' and isDelete=0";
				List<JwTGrade> grades = gradeService.doQuery(hql);
				JwTGrade grade = grades != null && grades.size() > 0 ? grades.get(0) : null;
				hql = "from JwTGradeclass where className='" + org.getNodeText() + "' and isDelete=0";
				List<JwTGradeclass> gradeclasses = gClassService.doQuery(hql);
				JwTGradeclass gradeClass = gradeclasses != null && gradeclasses.size() > 0 ? gradeclasses.get(0) : null;
				String gradeId = grade != null ? grade.getUuid() : gradeClass.getGraiId();
				hql = "from JwGradeteacher where graiId='" + gradeId + "' and category=0 and isDelete=0";
				JwGradeteacher t = gTeacherService.doQuery(hql).get(0);
				nextTask.setAssignee(t.getTteacId());
				approveUser = userService.get(t.getTteacId());
			} else if (nextTask.getAssignee().equals("部门负责人")) {
				BaseOrg currentOrg = baseOrgService.get(deptId);
				BaseJob job = baseJobService.get(currentOrg.getMainLeader());
				approveUser = getUserByJob(job);
				nextTask.setAssignee(approveUser.getUuid());
			} else if (nextTask.getAssignee().startsWith("抄送:")) {
				// 是否抄送
				boolean isCC = nextTask.getAssignee().startsWith("抄送:");
				String url = "";
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
				infoApp.setMessageInfo("办理成功");
				return infoApp;
			} else {
				approveUser = getApproveUser(deptId, nextTask); // 获得审批人
				nextTask.setAssignee(approveUser.getUuid()); // 并绑定到代理人那
			}
			pushInfo(approveUser, nextTask, approveUser.getExtField01());
			taskService.saveTask(nextTask);
		}else{
			String userId = historyService.createHistoricVariableInstanceQuery().processInstanceId(currentTask.getProcessInstanceId())
					.variableName("applayUserId").singleResult().getValue().toString();
			StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
			url.append("static/core/coreApp/flow/phoneapplay/hisApprove.jsp?");
			url.append("defineId=" + currentTask.getProcessDefinitionId());
			url.append("&executionId=" + currentTask.getExecutionId());
			SysUser user = userService.get(userId);
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
		infoApp.setMessageInfo("办理成功");
		return infoApp;
	}

	// 审批驳回
	@RequestMapping("/stopTask")
	public @ResponseBody LeaveApplayInfoApp stopTask(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		LeaveApplayInfoApp infoApp = new LeaveApplayInfoApp();
		String approveUserId = request.getParameter("approveUserId");
		SysUser currentUser = userService.get(approveUserId);
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
		infoApp.setMessageInfo("驳回成功");
		
		String userId = historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId())
				.variableName("applayUserId").singleResult().getValue().toString();
		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
		url.append("static/core/coreApp/flow/phoneapplay/hisApprove.jsp?");
		url.append("defineId=" + task.getProcessDefinitionId());
		url.append("&executionId=" + task.getExecutionId());
		SysUser user = userService.get(userId);
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
		
		return infoApp;
	}

	
	//获得申请时填写的数据
	@RequestMapping("/getVariables")
	public @ResponseBody Map<String, Object> getVariables(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String executionId = request.getParameter("executionId");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.putAll(runtimeService.getVariables(executionId));
		} catch (Exception e) {

		}
		try {
			List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
					.executionId(executionId).list();
			for (HistoricVariableInstance hisVar : list) {
				map.put(hisVar.getVariableName(), hisVar.getValue());
			}
		} catch (Exception e) {

		}
		return map;
	}

	// 显示历史审批信息
	@RequestMapping("/showApprove")
	public @ResponseBody List<LeaveApplay> showApprove(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String processInstanceId = request.getParameter("executionId");
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
		// applays.add(baseApplay);
		baseApplay = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			HistoricVariableInstance his = list.get(i);
			if (baseApplay == null) {
				baseApplay = new LeaveApplay();
			}
			if (his.getVariableName().indexOf("审批意见") != -1) {
				baseApplay.setNextadvice(his.getValue().toString());
				baseApplay.setApplayDate(his.getCreateTime());
			}
			if (his.getVariableName().indexOf("审批人") != -1) {
				baseApplay.setApplayName(his.getValue().toString());
			}
			if (baseApplay.getNextadvice() != null && baseApplay.getApplayName() != null) {
				applays.add(baseApplay);
				baseApplay = null;
			}
		}
		return applays;
	}

	//我的申请记录
	@RequestMapping(value = { "/myApplayList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<LeaveApplay> myApplayList(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userid = request.getParameter("applayUserId");		
		List<Task> tasks = taskService.createTaskQuery().taskOwner(userid).list();
		List<LeaveApplay> applay = new ArrayList<LeaveApplay>();
		LeaveApplay temp = null;
		for (Task task : tasks) {
			String obj = taskService.getVariable(task.getId(), "applayName") + "";
			String obj1 = taskService.getVariable(task.getId(), "flowName") + "";
			String obj2 = taskService.getVariable(task.getId(), "leaveReason") + "";
			String obj3 = taskService.getVariable(task.getId(), "applayDate") + "";
			temp = new LeaveApplay();
			temp.setFlowStatu(task.getName() + "正在审批");
			temp.setApplayName(obj);
			temp.setFlowName(obj1);
			temp.setLeaveReason(obj2);
			temp.setApplayDate(DateUtil.getTime(obj3));
			temp.setUuid(task.getProcessInstanceId());
			temp.setExtField01(task.getExecutionId());
			temp.setTaskId(task.getId());
			temp.setTaskDefinitionKey(task.getProcessDefinitionId());
			applay.add(temp);
		}
		String str = "";
		List<HistoricTaskInstance> tasks1 = historyService.createHistoricTaskInstanceQuery().taskOwner(userid).list();
		for (HistoricTaskInstance task : tasks1) {
			if (!str.equals(task.getExecutionId())) {
				str = task.getExecutionId();
				temp = new LeaveApplay();
				if (taskService.createTaskQuery().executionId(task.getExecutionId()).list() != null
						&& taskService.createTaskQuery().executionId(task.getExecutionId()).list().size() > 0) {

				} else {
					String Description = historyService.createHistoricTaskInstanceQuery()
							.executionId(task.getExecutionId()).orderByTaskId().desc().list().get(0).getDescription();
					if (Description != null && Description.equals("1")) {
						List<HistoricVariableInstance> tasks2 = historyService.createHistoricVariableInstanceQuery()
								.executionId(task.getExecutionId()).list();
						for (HistoricVariableInstance historicVariableInstance : tasks2) {
							if (historicVariableInstance.getVariableName().equals("applayName")) {
								temp.setApplayName(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("flowName")) {
								temp.setFlowName(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("leaveReason")) {
								temp.setLeaveReason(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("applayDate")) {
								temp.setApplayDate(DateUtil.getTime(historicVariableInstance.getValue().toString()));
							}
						}
						temp.setExtField01(task.getExecutionId());
						temp.setFlowStatu("审批通过");
						temp.setUuid(task.getProcessInstanceId());
						temp.setTaskDefinitionKey(task.getProcessDefinitionId());
						applay.add(temp);
					} else {
						List<HistoricVariableInstance> tasks2 = historyService.createHistoricVariableInstanceQuery()
								.executionId(task.getExecutionId()).list();
						for (HistoricVariableInstance historicVariableInstance : tasks2) {
							if (historicVariableInstance.getVariableName().equals("applayName")) {
								temp.setApplayName(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("flowName")) {
								temp.setFlowName(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("leaveReason")) {
								temp.setLeaveReason(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("applayDate")) {
								temp.setApplayDate(DateUtil.getTime(historicVariableInstance.getValue().toString()));
							}
						}
						temp.setExtField01(task.getExecutionId());
						temp.setFlowStatu("审批驳回");
						temp.setUuid(task.getProcessInstanceId());
						temp.setTaskDefinitionKey(task.getProcessDefinitionId());
						applay.add(temp);
					}
				}

			}
		}
		return applay;
	}
	
	@RequestMapping("/showMyApprove")
	public @ResponseBody List<LeaveApplay> showMyApprove(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String approveUserId = request.getParameter("approveUserId");
		SysUser currentUser = userService.get(approveUserId);
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
			List<SysUser> list = userService.getUserByRoleName(roleName); // 先找角色
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
			List<SysUser> list2 = userService.doQuery(hql); // 在按部门名称
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
			List<SysUser> list = userService.getUserByRoleName(nextAssignee); // 根据角色查询拥有这角色的人
			if (list != null && list.size() == 1) {
				list.get(0).setExtField01(nextAssignee);
				return list.get(0);
			}
			for (SysUser sysUser : list) {
				Set<BaseOrg> orgs = getUserDepts(sysUser);
				for (BaseOrg tempOrg : orgs) {
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

	private SysUser getUserByJob(BaseJob job) {
		String hql = "from SysUser as u inner join fetch u.userJobs as j where j.uuid='" + job.getUuid()
				+ "'and j.isDelete=0 and u.isDelete=0";
		return userService.doQuery(hql).get(0);
	}

	private Set<BaseOrg> getUserDepts(SysUser user) {
/*		String hql = "from SysUser as u inner join fetch u.userDepts as d where u.uuid='" + user.getUuid()
				+ "'and d.isDelete=0";
		SysUser u = userService.doQuery(hql).get(0);
		List<BaseOrg> listTemp = new ArrayList<BaseOrg>();
		listTemp.addAll(u.getUserDepts());
		String [] deptIds = new String[listTemp.size()];
		for (int i = 0; i < listTemp.size(); i++) {
			deptIds[i] = listTemp.get(i).getUuid();
			
		}
		Map<String, String> sortedCondition = new HashMap<String ,String >();
		sortedCondition.put("deptType", "ASC");
		
		List<BaseOrg> listJob = baseOrgService.queryByProerties("uuid", deptIds,sortedCondition);		
		Set<BaseOrg> sets = new HashSet<BaseOrg>();
		sets.addAll(listJob);*/
		Set<BaseOrg> sets = new HashSet<BaseOrg>();
		return sets;
	}

	private Set<SysRole> getUserRoles(SysUser user) {
		String hql = "from SysUser as u inner join fetch u.sysRoles as r where u.uuid='" + user.getUuid()
				+ "' and r.isDelete=0";
		SysUser u = userService.doQuery(hql).get(0);
		return u.getSysRoles();
	}

	private void pushInfo(SysUser approveUser, Task task, String roleName) {
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
	}

	// 抄送消息
	private void pushInfoCC(SysUser applayUser, Task task, String url) {
		List<SysUser> pushList = new ArrayList<SysUser>();
		String nextAssignee = task.getAssignee();
		nextAssignee = nextAssignee.substring(3, nextAssignee.length());
		String[] arr = nextAssignee.split("~");
		String roleName;
		if (arr.length == 2) { // 如果是教务处~主任
			String deptName = arr[0];
			roleName = arr[1];
			List<SysUser> list = userService.getUserByRoleName(roleName); // 先找角色
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
			List<SysUser> list = userService.getUserByRoleName(nextAssignee); // 根据角色查询拥有这角色的人
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
