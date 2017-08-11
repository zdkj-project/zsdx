package com.zd.school.jw.train.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.util.DateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class CourseCheck implements Serializable{
	//班级id
	private String classId;
	//课程id,对应日程id
	private String courseId;
	//物理卡号
	private String wlkh;
	//刷卡时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date time; 
	//终端号
	private String termCode;
	//签到签退标记0-签到1-签退
	private String lg;
	
	private boolean loaded;
	
	private boolean managed;

	private String attendResult;

    public String getAttendResult() {
        return attendResult;
    }

    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }

    /**
     * 考勤人员Id
     */
	private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getWlkh() {
		return wlkh;
	}
	public void setWlkh(String wlkh) {
		this.wlkh = wlkh;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getLg() {
		return lg;
	}
	public void setLg(String lg) {
		this.lg = lg;
	}
	
}
