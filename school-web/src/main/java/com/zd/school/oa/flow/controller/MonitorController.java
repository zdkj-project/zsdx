package com.zd.school.oa.flow.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.JsonBuilder;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.oa.flow.model.FlowTypeTree;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

@Controller
@RequestMapping("/ProcessMonitor")
public class MonitorController extends FrameWorkController<LeaveApplay> implements Constant {

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
    private SysUserService sysUserService;

    @Resource
    private TeaTeacherbaseService teacherService;

    @Resource
    private BaseJobService jobService;

    @Resource
    private PushInfoService pushInfoService;

    // 已经启动的流程信息
    @RequestMapping(value = { "/startedInfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<LeaveApplay> startedInfo(
            @RequestParam("processDefinitionName") String processDefinitionName, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List<LeaveApplay> applay = new ArrayList<LeaveApplay>();
        SysUser applayUser = null;

        // 正在进行的任务
        List<Task> tasks = taskService.createTaskQuery().processDefinitionName(processDefinitionName)
                .orderByTaskCreateTime().asc().list();
        LeaveApplay temp = null;
        for (Task task : tasks) {
            temp = new LeaveApplay();
            applayUser = new SysUser();
            applayUser = sysUserService.get(task.getOwner());
            temp.setApplayUserName(applayUser.getXm());
            String obj = taskService.getVariable(task.getId(), "applayName") + "";
            temp.setFlowStatu(task.getName() + "正在审批");
            temp.setApplayName(obj);
            temp.setTaskId(task.getId());
            // 根据时间升序获得最早的提交时间
            HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery()
                    .executionId(task.getExecutionId()).orderByHistoricTaskInstanceStartTime().asc().list().get(0);
            temp.setApplayDate(hisTask.getStartTime());
            applay.add(temp);
        }

        // 进行完毕的任务
        String str = "";
        List<HistoricTaskInstance> tasks1 = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionName(processDefinitionName).list();
        for (HistoricTaskInstance task : tasks1) {
            if (!str.equals(task.getExecutionId())) {
                str = task.getExecutionId();
                temp = new LeaveApplay();

                // 过滤掉还在审批的0.0
                if (taskService.createTaskQuery().executionId(task.getExecutionId()).list() != null
                        && taskService.createTaskQuery().executionId(task.getExecutionId()).list().size() > 0) {

                } else {
                    applayUser = new SysUser();
                    applayUser = sysUserService.get(task.getOwner());
                    temp.setApplayUserName(applayUser.getXm());
                    // 同上上上个注释= =
                    HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery()
                            .executionId(task.getExecutionId()).orderByHistoricTaskInstanceStartTime().asc().list()
                            .get(0);
                    temp.setApplayDate(hisTask.getStartTime());

                    // 获得标题
                    List<HistoricVariableInstance> tasks2 = historyService.createHistoricVariableInstanceQuery()
                            .executionId(task.getExecutionId()).list();
                    for (HistoricVariableInstance historicVariableInstance : tasks2) {
                        if (historicVariableInstance.getVariableName().equals("applayName")) {
                            temp.setApplayName(historicVariableInstance.getValue().toString());
                            break;
                        }
                    }
                    temp.setTaskId(task.getId());

                    // 获得审批状态
                    String Description = historyService.createHistoricTaskInstanceQuery()
                            .executionId(task.getExecutionId()).orderByTaskId().desc().list().get(0).getDescription();
                    if (Description != null && Description.equals("1")) {
                        temp.setFlowStatu("审批通过");
                    } else {
                        temp.setFlowStatu("审批驳回");
                    }
                    applay.add(temp);
                }
            }
        }
        return applay;
    }

    // 树菜单
    @RequestMapping("/listtree")
    public void listtree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据

        // 查询出所有流程定义
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().latestVersion()
                .list();

        List<FlowTypeTree> list = new ArrayList<FlowTypeTree>();
        FlowTypeTree tree;
        for (ProcessDefinition processDefinition : definitionList) {
            tree = new FlowTypeTree();
            tree.setText(processDefinition.getName());
            tree.setId(processDefinition.getKey());
            list.add(tree);
        }
        strData = JsonBuilder.getInstance().buildList(list, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    // 推送消息
    @RequestMapping("/pushInfo")
    public void pushInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String taskId = request.getParameter("taskId");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String hql = "from SysUser where jobName='" + task.getAssignee() + "' or uuid like '%" + task.getAssignee()
                + "%'";
        List<SysUser> userList = sysUserService.doQuery(hql);
        for (SysUser sysUser : userList) {
            TeaTeacherbase teacher = new TeaTeacherbase();
            teacher = teacherService.get(sysUser.getUuid());
            PushInfo pushInfo = new PushInfo();
            pushInfo.setEmplName(sysUser.getXm());
            pushInfo.setEmplNo(teacher.getUserNumb());
            pushInfo.setRegTime(new Date());
            pushInfo.setEventType("审批提醒");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            pushInfo.setRegStatus("您好," + sysUser.getXm() + "老师,有申请需要您尽快审批!");
            pushInfoService.persist(pushInfo);
        }
        writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'消息提醒成功'"));
    }

    @RequestMapping("/approveChange")
    public void approveChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String taskId = request.getParameter("taskId");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String xm = request.getParameter("xm");
        String tteacId = request.getParameter("tteacId");
        task.setAssignee(tteacId);
        task.setName(xm);
        taskService.saveTask(task);
        writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'审批人修改成功!'"));
    }

}
