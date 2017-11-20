package com.zd.school.jw.train.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.util.DateTimeSerializer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

/**
 * 班级培训考勤信息
 */
public class VoTrainClassCheck implements Serializable {

    @FieldInfo(name = "班级学员ID")
    private String classTraineeId;

    public String getClassTraineeId() {
        return classTraineeId;
    }

    public void setClassTraineeId(String classTraineeId) {
        this.classTraineeId = classTraineeId;
    }

    @FieldInfo(name = "班级ID")
    private String classId;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    @FieldInfo(name = "学员ID")
    private String traineeId;

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    @FieldInfo(name = "姓名")
    private String xm;

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXm() {
        return xm;
    }

    @FieldInfo(name = "性别")
    private String xbm;

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getXbm() {
        return xbm;
    }
    @FieldInfo(name = "移动电话")
    private String mobilePhone;

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }
    @FieldInfo(name = "所在单位")
    private String workUnit;

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    @FieldInfo(name = "班级课程安排Id")
    private String classScheduleId;

    public String getClassScheduleId() {
        return classScheduleId;
    }

    public void setClassScheduleId(String classScheduleId) {
        this.classScheduleId = classScheduleId;
    }
    @FieldInfo(name = "签到刷卡时间")
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date incardTime;
    public void setIncardTime(Date incardTime) {
        this.incardTime = incardTime;
    }
    public Date getIncardTime() {
        return incardTime;
    }

    @FieldInfo(name = "签退刷卡时间")
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date outcardTime;
    public void setOutcardTime(Date outcardTime) {
        this.outcardTime = outcardTime;
    }
    public Date getOutcardTime() {
        return outcardTime;
    }

    @FieldInfo(name = "考勤结果")
    private String attendResult;

    public String getAttendResult() {
        return attendResult;
    }

    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }
    
    @FieldInfo(name = "上课时间")
    private Integer attendMinute;

	public Integer getAttendMinute() {
		return attendMinute;
	}

	public void setAttendMinute(Integer attendMinute) {
		this.attendMinute = attendMinute;
	}

	@FieldInfo(name = "是否请假")	   
    private String isLeave;
    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }
    public String getIsLeave() {
        return isLeave;
    }
    
    @FieldInfo(name = "备注信息")
    private String remark;
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }
    
}
