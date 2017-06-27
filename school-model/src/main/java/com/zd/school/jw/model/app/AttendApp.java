package com.zd.school.jw.model.app;

import java.util.List;

import com.zd.school.jw.train.model.TrainCourseattend;
import com.zd.school.jw.train.model.SchoolAttend;

public class AttendApp {
	//判断是否调用成功，true为调用成功,false为失败。默认为false
	private boolean success=false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	//返回信息
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private List<SchoolAttend> obj;

	public List<SchoolAttend> getObj() {
		return obj;
	}

	public void setObj(List<SchoolAttend> obj) {
		this.obj = obj;
	}


	
}
