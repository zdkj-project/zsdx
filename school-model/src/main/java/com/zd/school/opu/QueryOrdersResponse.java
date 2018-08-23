package com.zd.school.opu;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zd.core.util.JsonBuilder;

public class QueryOrdersResponse {

	// 预订号
	private int orderId;
	// 同住序号
	private int tzxh;
	// 房间号
	private String roomcode;
	// 手机号
	private String phone;
	// 租金
	private BigDecimal rent;
	// 付款方式
	private String payType;
	// 入住日期
	private String checkinDate;
	// 离店日期
	private String checkoutDate;
	
	public static OpuResult<QueryOrdersResponse> fromJson(String json) {
		OpuResult<QueryOrdersResponse> result = (OpuResult<QueryOrdersResponse>) JsonBuilder.getInstance()
				.fromJson(json, new TypeReference<OpuResult<QueryOrdersResponse>>() {
				});
		return result;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTzxh() {
		return tzxh;
	}

	public void setTzxh(int tzxh) {
		this.tzxh = tzxh;
	}

	public String getRoomcode() {
		return roomcode;
	}

	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getRent() {
		return rent;
	}

	public void setRent(BigDecimal rent) {
		this.rent = rent;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

}
