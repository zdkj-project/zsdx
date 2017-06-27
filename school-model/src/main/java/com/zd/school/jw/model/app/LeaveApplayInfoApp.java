package com.zd.school.jw.model.app;

import com.zd.school.oa.flow.model.LeaveApplay;

public class LeaveApplayInfoApp {
	private boolean message;//调用结果 ture&false
	private String messageInfo;//返回状态信息
	private LeaveApplay leaveapplay;
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
	public LeaveApplay getLeaveapplay() {
		return leaveapplay;
	}
	public void setLeaveapplay(LeaveApplay leaveapplay) {
		this.leaveapplay = leaveapplay;
	}
	
	
}
