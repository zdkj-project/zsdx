package com.zd.school.opu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zd.core.util.JsonBuilder;

public class GetGuestInfoResponse {

	// 客人账号
	private String guestNo;
	// 客人状态，N表示预订状态， I表示在住状态
	private String state;
	// 房号
	private String roomcode;
	// 姓名
	private String name;
	// 预定号
	private String orderId;
	// 团号
	private String teamNo;
	// 团名
	private String teamName;
	// 入住日期
	private String checkinDate;
	// 离店日期
	private String checkoutDate;
	// 离店时间
	private String checkoutTime;
	// 房间类型
	private String roomtype;
	
	public static OpuResult<GetGuestInfoResponse> fromJson(String json) {
		OpuResult<GetGuestInfoResponse> result = (OpuResult<GetGuestInfoResponse>) JsonBuilder.getInstance()
				.fromJson(json, new TypeReference<OpuResult<GetGuestInfoResponse>>() {
				});
		return result;
	}

	public String getGuestNo() {
		return guestNo;
	}

	public void setGuestNo(String guestNo) {
		this.guestNo = guestNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRoomcode() {
		return roomcode;
	}

	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}

}
