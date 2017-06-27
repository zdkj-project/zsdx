/**
 * Project Name:zd-core
 * File Name:BaseApplayEntity.java
 * Package Name:com.zd.core.model
 * Date:2016年4月13日下午3:44:19
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.util.DateTimeSerializer;

/**
 * ClassName:BaseApplayEntity Function: TODO ADD FUNCTION. Reason: 所有申请需要继承的基类
 * Date: 2016年4月13日 下午3:44:19
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "OA_T_APPLAYBASE")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride(name = "uuid", column = @Column(name = "APPLAY_ID", length = 36, nullable = false))
public class BaseApplayEntity extends BaseEntity implements Serializable {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "申请名称")
    @Column(name = "APPLAY_NAME", length = 64, nullable = false)
    private String applayName;

    public String getApplayName() {
        return applayName;
    }

    public void setApplayName(String applayName) {
        this.applayName = applayName;
    }

    @FieldInfo(name = "申请日期")
    @Column(name = "APPLAY_Date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date applayDate;

    public Date getApplayDate() {
        return applayDate;
    }

    public void setApplayDate(Date applayDate) {
        this.applayDate = applayDate;
    }

    @FieldInfo(name = "申请人ID")
    @Column(name = "APPLAY_USERID", length = 36, nullable = false)
    private String applayUserId;

    public String getApplayUserId() {
        return applayUserId;
    }

    public void setApplayUserId(String applayUserId) {
        this.applayUserId = applayUserId;
    }

    @FieldInfo(name = "申请人姓名")
    @Column(name = "APPLAY_USERNAME", length = 64, nullable = false)
    private String applayUserName;

    public String getApplayUserName() {
        return applayUserName;
    }

    public void setApplayUserName(String applayUserName) {
        this.applayUserName = applayUserName;
    }

    @FieldInfo(name = "流程ID")
    @Column(name = "FLOW_ID", length = 64, nullable = false)
    private String flowId;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @FieldInfo(name = "流程名称")
    @Column(name = "FLOW_NAME", length = 64, nullable = false)
    private String flowName;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    @FieldInfo(name = "状态")
    @Column(name = "FLOW_STATU", length = 64)
    private String flowStatu;

    public String getFlowStatu() {
        return flowStatu;
    }

    public void setFlowStatu(String flowStatu) {
        this.flowStatu = flowStatu;
    }

    @Transient
    @FieldInfo(name = "任务节点Id")
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Transient
    @FieldInfo(name = "任务节点名称")
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Transient
    @FieldInfo(name = "任务节点Key")
    private String taskDefinitionKey;

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Transient
    @FieldInfo(name = "任务节点创建时间")
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date taskCreateTime;

    public Date getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    @Transient
    @FieldInfo(name = "任务操作状态")
    private String taskAssignee;

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    @Transient
    @FieldInfo(name = "运行中流程实例ID")
    private String piId;

    public String getPiId() {
        return piId;
    }

    public void setPiId(String piId) {
        this.piId = piId;
    }

    @Transient
    @FieldInfo(name = "运行中流程实例状态")
    private String piSuspended;

    public String getPiSuspended() {
        return piSuspended;
    }

    public void setPiSuspended(String piSuspended) {
        this.piSuspended = piSuspended;
    }

    @Transient
    @FieldInfo(name = "上一步审批意见")
    private String nextadvice;

    public String getNextadvice() {
        return nextadvice;
    }

    public void setNextadvice(String nextadvice) {
        this.nextadvice = nextadvice;
    }

}
