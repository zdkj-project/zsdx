package com.zd.school.jw.model.app;

public class VideoApp {
	private boolean message;//调用结果 ture&false
	private String messageInfo;//返回状态信息
	private VideoReturnApp data;
	public VideoReturnApp getData() {
		return data;
	}
	public void setData(VideoReturnApp data) {
		this.data = data;
	}
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
	
}
