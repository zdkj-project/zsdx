package com.zd.school.oa.flow.controller;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;

public class WFUtil {

	// 获取当前位置
	public static ActivityImpl getProcessMap(String procDefId, String executionId, ProcessEngine processEngine)
			throws Exception {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId).singleResult();
		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
		String processDefinitionId = pdImpl.getId();
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		ActivityImpl actImpl = null;

		RuntimeService runtimeService = processEngine.getRuntimeService();
		ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId)
				.singleResult();
		HistoricActivityInstance hisActivity = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
				.executionId(executionId).orderByHistoricActivityInstanceStartTime().desc().list().get(0);
		String activitiId = execution != null ? execution.getActivityId() : hisActivity.getActivityId();// 当前实例的执行到哪个节点
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (id.equals(activitiId)) {// 获得执行到那个节点
				actImpl = activityImpl;
				break;
			}
		}
		return actImpl;
	}

	// 获得流程图
	public static InputStream findProcessPic(String procDefId, ProcessEngine processEngine) throws Exception {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		String diagramResourceName = procDef.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
		return imageStream;
	}

}
