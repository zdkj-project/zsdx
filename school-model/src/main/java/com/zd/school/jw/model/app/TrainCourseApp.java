package com.zd.school.jw.model.app;

import java.util.List;

import com.zd.school.jw.train.model.TrainClassschedule;

public class TrainCourseApp {
	//判断是否调用成功，true为调用成功,false为失败。默认为false
	private boolean code=false;

	
	
	public boolean getCode() {
		return code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}


	//返回信息
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	//封装数据list实体
	private List<TrainClassschedule> course;



	public List<TrainClassschedule> getCourse() {
		return course;
	}

	public void setCourse(List<TrainClassschedule> course) {
		this.course = course;
	}
	
	
}
