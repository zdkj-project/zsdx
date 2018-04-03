package com.zd.school.oa.flow.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.flow.model.LeaveApplay;
import com.zd.school.oa.flow.service.ApplayService;
import com.zd.school.oa.flow.service.LeaveApplayService;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/leaveapplay")
public class LeaveApplayController extends FrameWorkController<LeaveApplay> implements Constant {

    @Resource
    private LeaveApplayService thisService;

    @Resource
    private ApplayService applayService;
    
	@Resource
	private TaskService taskService;
	
	@Resource
	private HistoryService historyService;
    

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<LeaveApplay> list(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	SysUser user=getCurrentSysUser();
    	String userid=user.getUuid();
    	List<Task> tasks=taskService.createTaskQuery().taskOwner(userid).list();
    	List<LeaveApplay> applay=new ArrayList<LeaveApplay>();
    	LeaveApplay temp=null;
    	for (Task task : tasks) {
    		String obj=taskService.getVariable(task.getId(),"applayName")+"";
    		String obj1=taskService.getVariable(task.getId(),"flowKey")+"";
    		String obj2=taskService.getVariable(task.getId(),"leaveReason")+"";
    		temp=new LeaveApplay();
    		temp.setFlowStatu(task.getName()+"正在审批");
    		temp.setApplayName(obj);
    		temp.setFlowName(obj1);
    		temp.setLeaveReason(obj2);
    		temp.setUuid(task.getProcessInstanceId());
    		temp.setExtField01(task.getExecutionId());
    		temp.setTaskId(task.getId());
    		temp.setTaskDefinitionKey(task.getProcessDefinitionId());
    		applay.add(temp);
		}
    	String str="";
    	List<HistoricTaskInstance> tasks1=historyService.createHistoricTaskInstanceQuery().taskOwner(userid).list();
    	for (HistoricTaskInstance task : tasks1) {
    		if (!str.equals(task.getExecutionId())) {
    			str=task.getExecutionId();
    			temp=new LeaveApplay();
        		if(taskService.createTaskQuery().executionId(task.getExecutionId()).list()!=null&&taskService.createTaskQuery().executionId(task.getExecutionId()).list().size()>0){
        				
        		}else{
        			String Description=historyService.createHistoricTaskInstanceQuery().executionId(task.getExecutionId()).orderByTaskId().desc().list().get(0).getDescription();
        			if(Description!=null&&Description.equals("1")){
        				List<HistoricVariableInstance> tasks2=historyService.createHistoricVariableInstanceQuery().executionId(task.getExecutionId()).list();
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
    					}
        				temp.setExtField01(task.getExecutionId());
        				temp.setFlowStatu("审批通过");
        				temp.setUuid(task.getProcessInstanceId());
        				temp.setTaskDefinitionKey(task.getProcessDefinitionId());
        				applay.add(temp);
        			}else{
        				List<HistoricVariableInstance> tasks2=historyService.createHistoricVariableInstanceQuery().executionId(task.getExecutionId()).list();
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
    	
    	
    	/*String sql="select a.NAME_,a.EXECUTION_ID_ from ACT_RU_TASK a where a.OWNER_='"+userid+"'";
    	
    	List<Object[]> list=thisService.ObjectQuerySql(sql);
    	List<LeaveApplay> applay=new ArrayList<LeaveApplay>();
    	LeaveApplay temp=null;
    	for (Object[] objects : list) {
    		temp=new LeaveApplay();
    		String sqltext="select TEXT_ from dbo.ACT_RU_VARIABLE where EXECUTION_ID_='"+objects[1]+"' and NAME_='applayName' union select TEXT_ from dbo.ACT_RU_VARIABLE where EXECUTION_ID_='"+objects[1]+"' and NAME_='flowKey'";
    		temp.setFlowStatu(objects[0]+"正在审批");
    		Object[] obj=thisService.ObjectQuerySql(sqltext).get(0);
    		Object[] obj1=thisService.ObjectQuerySql(sqltext).get(1);
    		temp.setApplayName(obj[0]+"");
    		temp.setFlowName(obj1[0]+"");
    		applay.add(temp);
    	}
		return applay;*/


    }

    /**
     * 
     * doAdd @Title: doAdd @Description: 增加请假申请 @param @param LeaveApplay
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(LeaveApplay entity, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 此处为放在入库前的一些检查的代码，如唯一校验等

        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 生成默认的orderindex
        // 如果界面有了排序号的输入，则不需要取默认的了
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);// 排序

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人

        // 持久化到数据库
        entity = thisService.doMerge(entity);

        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * doUpdate @Title: 编辑申请信息 @Description: 修改请假申请 @param @param
     * LeaveApplay @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(LeaveApplay entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        LeaveApplay perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        entity = thisService.doMerge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }

    /**
     * 
     * doAddStart @Title: 增加申请并同时启用流程 @Description: TODO @param @param
     * LeaveApplay 实体类 @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doaddstart")
    public void doAddStart(LeaveApplay entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 此处为放在入库前的一些检查的代码，如唯一校验等

        // 获取当前操作用户
        String userCh = "超级管理员";
        String userId = "";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null) {
            userCh = currentUser.getXm();
            userId = currentUser.getUuid();
        }

        // 生成默认的orderindex
        // 如果界面有了排序号的输入，则不需要取默认的了
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);// 排序

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人

        // 持久化到数据库
        // entity = thisService.merge(entity);

        // 启动流程
        String key = request.getParameter("flowKey");
        String businesskey = entity.getUuid();
        String classLeader = applayService.getClassLeader(entity.getApplayUserId());
        Map<String, String> map = new HashMap<String, String>();
        map.put("classLeader", classLeader);
        boolean flag = thisService.startWorkflow(userId, key, businesskey, map);

        // 返回实体到前端界面
        if (flag) {
            entity = thisService.doMerge(entity);
            writeJSON(response, jsonBuilder.returnSuccessJson("'提交并启动流程成功'"));
        } else
            writeJSON(response, jsonBuilder.returnFailureJson("'启动流程失败'"));
    }

    /**
     * doUpdateStart @Title: 编辑申请信息并同时启动流程 @Description: TODO @param @param
     * LeaveApplay @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdatestart")
    public void doUpdateStart(LeaveApplay entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        LeaveApplay perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        entity = thisService.doMerge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }

    /**
     * doDelete @Title: doDelete @Description: 逻辑删除指定ID的记录 @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }
}
