package com.zd.school.jw.model.app;

import java.util.List;

import com.zd.school.jw.ecc.model.EccClasselegant;

public class EccClasselegantApp {
	private boolean message;//调用结果 ture&false
	private String messageInfo;//返回状态信息
	private List<EccClasselegant> elegantList;
	public boolean isMessage() {
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
	public List<EccClasselegant> getElegantList() {
		return elegantList;
	}
	public void setElegantList(List<EccClasselegant> elegantList) {
		this.elegantList = elegantList;
	}
	
	
}
