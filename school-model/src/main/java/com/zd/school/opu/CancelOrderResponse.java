package com.zd.school.opu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zd.core.util.JsonBuilder;

public class CancelOrderResponse {

	// 已取消预订的预订号
	private int orderId;
	// 已取消预订的同住序号
	private int tzxh;
	// 已取消预订的房间号
	private String roomcode;

	public static OpuResult<CancelOrderResponse> fromJson(String json) {
		OpuResult<CancelOrderResponse> result = (OpuResult<CancelOrderResponse>) JsonBuilder.getInstance()
				.fromJson(json, new TypeReference<OpuResult<CancelOrderResponse>>() {
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

}
