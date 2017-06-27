package com.zd.school.jw.model.app;

public class CourseCheckApp {
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

}
