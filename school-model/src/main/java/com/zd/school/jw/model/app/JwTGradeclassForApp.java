package com.zd.school.jw.model.app;

import com.zd.school.jw.eduresources.model.JwTGradeclass;


public class JwTGradeclassForApp {
	
	private boolean message;//掉用结果 ture&false
	private String messageInfo;//返回状态信息
	private JwTGradeclass gradeClass;//班级信息对象
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
	public JwTGradeclass getGradeClass() {
		return gradeClass;
	}
	public void setGradeClass(JwTGradeclass gradeClass) {
		this.gradeClass = gradeClass;
	}
	
	
}
