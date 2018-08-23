package com.zd.school.opu;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zd.core.util.JsonBuilder;

public class OpuResult<T> {

	// 0表示成功，1表示失败
	private int rspCode;
	// 调用信息说明
	private String rspMsg;

	// 数据列表
	private List<T> datas;

	public static OpuResult<?> fromJson(String json) {
		OpuResult<?> result = (OpuResult<?>) JsonBuilder.getInstance().fromJson(json, OpuResult.class);
		return result;
	}

	public static OpuResult<?> fromJson(String json, Class<?>... elementClasses) {
		OpuResult<?> result = (OpuResult<?>) JsonBuilder.getInstance().fromJson(json, OpuResult.class, elementClasses);
		return result;
	}

	public OpuResult() {

	}

	public OpuResult(int rspCode, String rspMsg) {
		this.rspCode = rspCode;
		this.rspMsg = rspMsg;
	}

	// 余额
	private BigDecimal balance;
	// 门锁记录8位16进制的序号
	private String lockNo;
	// 房间类型
	private String roomType;

	public int getRspCode() {
		return rspCode;
	}

	public void setRspCode(int rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public String getLockNo() {
		return lockNo;
	}

	public void setLockNo(String lockNo) {
		this.lockNo = lockNo;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

}
