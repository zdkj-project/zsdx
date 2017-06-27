/**
 * Project Name:jw-model
 * File Name:StudentLeave.java
 * Package Name:com.zd.school.oa.model.applay
 * Date:2016年4月13日下午4:57:40
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseApplayEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * ClassName:StudentLeave Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年4月13日 下午4:57:40
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "OA_T_LEAVEAPPLAY")
public class LeaveApplay extends BaseApplayEntity implements Serializable {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "开始日期")
    @Column(name = "START_Date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @FieldInfo(name = "结束日期")
    @Column(name = "END_Date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @FieldInfo(name = "请假天数")
    @Column(name = "LEAVE_DAY")
    private Double leaveDay;

    public Double getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(Double leaveDay) {
        this.leaveDay = leaveDay;
    }

    @FieldInfo(name = "请假原因")
    @Column(name = "LEAVE_REASON", length = 255)
    private String leaveReason;

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

}
