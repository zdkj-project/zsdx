package com.zd.school.jw.train.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.util.DateTimeSerializer;

public class SchoolAttend {
	private String classId;
	private String courseId;
	private String traineeId;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
	private Date incardTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
	private Date outcardTime;
	
	public SchoolAttend(String classId, String courseId, String traineeId, Date incardTime, Date outcardTime) {
		super();
		this.classId = classId;
		this.courseId = courseId;
		this.traineeId = traineeId;
		this.incardTime = incardTime;
		this.outcardTime = outcardTime;
	}


	public SchoolAttend() {
		super();
		// TODO Auto-generated constructor stub
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


	public String getTraineeId() {
		return traineeId;
	}


	public void setTraineeId(String traineeId) {
		this.traineeId = traineeId;
	}


	public Date getIncardTime() {
		return incardTime;
	}


	public void setIncardTime(Date incardTime) {
		this.incardTime = incardTime;
	}


	public Date getOutcardTime() {
		return outcardTime;
	}


	public void setOutcardTime(Date outcardTime) {
		this.outcardTime = outcardTime;
	}


	@Override
	public String toString() {
		return "schoolAttend [classId=" + classId + ", courseId=" + courseId + ", traineeId=" + traineeId
				+ ", incardTime=" + incardTime + ", outcardTime=" + outcardTime + "]";
	}
}
