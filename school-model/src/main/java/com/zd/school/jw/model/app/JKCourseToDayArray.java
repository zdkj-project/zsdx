package com.zd.school.jw.model.app;

import java.util.List;

public class JKCourseToDayArray {
	
	private String dayFoWeek ;//星期数1~7
	private boolean message;//掉用结果 ture&false
	private String messageInfo;//返回状态信息
	private List<JKCourse> jcList;//课程结果集
	public String getDayFoWeek() {
		return dayFoWeek;
	}
	public void setDayFoWeek(String dayFoWeek) {
		this.dayFoWeek = dayFoWeek;
	}
	public boolean getMessage() {
		return message;
	}
	public void setMessage(boolean message) {
		this.message = message;
	}
	public String getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	public List<JKCourse> getJcList() {
		return jcList;
	}
	public void setJcList(List<JKCourse> jcList) {
		this.jcList = jcList;
	}
	
}
