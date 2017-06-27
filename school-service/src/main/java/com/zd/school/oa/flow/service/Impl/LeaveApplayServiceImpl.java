package com.zd.school.oa.flow.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.DateUtil;
import com.zd.core.util.ModelUtil;
import com.zd.school.oa.flow.dao.LeaveApplayDao;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.service.LeaveApplayService;
import com.zd.school.oa.flow.service.WorkFlowService;

@Service
@Transactional
public class LeaveApplayServiceImpl extends BaseServiceImpl<LeaveApplay> implements LeaveApplayService {

	@Resource
	public void setLeaveApplayDao(LeaveApplayDao leaveApplayDao) {
		this.dao = leaveApplayDao;
	}

	@Resource
	WorkFlowService wFlowService;

	@Resource
	private HistoryService historyService;

	@Resource
	private TaskService taskService;

	@Override
	@SuppressWarnings("rawtypes")
	public boolean startWorkflow(String userId, String key, String businesskey, Map map) {

		// TODO Auto-generated method stub
		boolean result = false;
		ProcessInstance pInstance = wFlowService.startProcess(userId, key, businesskey, map);
		if (ModelUtil.isNotNull(pInstance))
			result = true;

		return result;
	}

	public List<LeaveApplay> getLastApplay(String userid) {
		List<LeaveApplay> applay = new ArrayList<LeaveApplay>();
		String str = "";
		LeaveApplay temp = null;
		
		List<HistoricTaskInstance> tasks1 = historyService.createHistoricTaskInstanceQuery().processDefinitionKey("学生请假流程").taskOwner(userid).orderByTaskId().desc().list();
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
							if (historicVariableInstance.getVariableName().equals("flowKey")) {
								temp.setFlowName(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("leaveReason")) {
								temp.setLeaveReason(historicVariableInstance.getValue().toString());
							}
							if (historicVariableInstance.getVariableName().equals("startDate")) {
								temp.setStartDate(DateUtil.getTime(historicVariableInstance.getValue().toString()));
							}
							if (historicVariableInstance.getVariableName().equals("endDate")) {
								temp.setEndDate(DateUtil.getTime(historicVariableInstance.getValue().toString()));
							}
						}
						temp.setExtField01(task.getExecutionId());
						temp.setFlowStatu("审批通过");
						temp.setUuid(task.getProcessInstanceId());
			
						applay.add(temp);
					}
				}

			}
		}

		return applay;
	}

}
