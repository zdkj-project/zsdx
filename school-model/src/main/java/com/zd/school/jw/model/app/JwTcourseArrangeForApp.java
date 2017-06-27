package com.zd.school.jw.model.app;

import java.util.List;

import com.zd.school.jw.arrangecourse.model.JwCourseArrange;


public class JwTcourseArrangeForApp {
	
	//判断是否调用成功，1为调用成功,0为失败。默认为0
	private Integer code=0;

	
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
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
	private List<JwCourseArrange> jkArrange;

	public List<JwCourseArrange> getJkArrange() {
		return jkArrange;
	}

	public void setJkArrange(List<JwCourseArrange> jkArrange) {
		this.jkArrange = jkArrange;
	}



}
