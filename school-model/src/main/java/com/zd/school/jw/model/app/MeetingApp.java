package com.zd.school.jw.model.app;

import com.zd.school.oa.meeting.model.OaMeeting;

import java.util.List;

public class MeetingApp {
    /**
     * 调用成功与否的标记
     */
	private boolean code=false;

	public boolean getCode() {
		return code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}


    /**
     * 调用返回的消息
     */
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    /**
     * 返回的会议列表信息
     */
	private List<OaMeeting> meeting;
	public List<OaMeeting> getMeeting() {
		return meeting;
	}

	public void setMeeting(List<OaMeeting> meeting) {
		this.meeting = meeting;
	}


	
	

}
