package com.zd.school.oa.meeting.model;

import java.util.Date;

public class MeetingCheck {
	//会议id
	private String meetingId;
	//物理卡号
	private String wlkh;
	//签到签退时间
	private Date time;
	
	private boolean loaded;
	
	private boolean managed;

	//签到状态(1-正常 2-迟到4-缺勤 )
    private String inResult;
    //签退状态(1-正常  3-早退)
    private String outResult;
	//考勤结果1-正常 2-迟到 3-早退 4-缺勤 5-迟到早退
	private String attendResult;
	//设备号
	private String termCode;
	//签到签退标识0-签到，1-签退,-1缺勤
	private String lg;
	
	/**
     * 考勤人员Id
     */
	private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
	public String getWlkh() {
		return wlkh;
	}
	public void setWlkh(String wlkh) {
		this.wlkh = wlkh;
	}


	public String getAttendResult() {
		return attendResult;
	}
	public void setAttendResult(String attendResult) {
		this.attendResult = attendResult;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getLg() {
		return lg;
	}
	public void setLg(String lg) {
		this.lg = lg;
	}
	public String getInResult() {
		return inResult;
	}
	public void setInResult(String inResult) {
		this.inResult = inResult;
	}
	public String getOutResult() {
		return outResult;
	}
	public void setOutResult(String outResult) {
		this.outResult = outResult;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}


	
}
