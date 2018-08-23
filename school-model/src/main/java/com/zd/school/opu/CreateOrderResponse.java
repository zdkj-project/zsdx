package com.zd.school.opu;

import com.zd.core.util.JsonBuilder;

public class CreateOrderResponse {

	// 0表示成功，1表示失败
	private int rspCode;
	// 调用信息说明
	private String rspMsg;
	// 团名
	private String tm;
	// 房数
	private int fs;
	// 人数
	private int rs;
	// 入住日期
	private String rzrq;
	// 离店日期
	private String ldrq;
	// 联系人
	private String lxr;
	// 联系电话
	private String lxdh;
	// 接待备注
	private String jdbz;
	// 客人备注
	private String krbz;
	// 账务备注
	private String zwbz;
	// 预订号，订单创建成功后返回的预订号，如果订单创建失败返回的预订号为0
	private int r_ydh;
	// 创建订单结果文字说明
	private String r_msg;

	public static CreateOrderResponse fromJson(String json) {
		CreateOrderResponse result = (CreateOrderResponse) JsonBuilder.getInstance().fromJson(json, CreateOrderResponse.class);
		return result;
	}

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

	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}

	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	public int getRs() {
		return rs;
	}

	public void setRs(int rs) {
		this.rs = rs;
	}

	public String getRzrq() {
		return rzrq;
	}

	public void setRzrq(String rzrq) {
		this.rzrq = rzrq;
	}

	public String getLdrq() {
		return ldrq;
	}

	public void setLdrq(String ldrq) {
		this.ldrq = ldrq;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getJdbz() {
		return jdbz;
	}

	public void setJdbz(String jdbz) {
		this.jdbz = jdbz;
	}

	public String getKrbz() {
		return krbz;
	}

	public void setKrbz(String krbz) {
		this.krbz = krbz;
	}

	public String getZwbz() {
		return zwbz;
	}

	public void setZwbz(String zwbz) {
		this.zwbz = zwbz;
	}

	public int getR_ydh() {
		return r_ydh;
	}

	public void setR_ydh(int r_ydh) {
		this.r_ydh = r_ydh;
	}

	public String getR_msg() {
		return r_msg;
	}

	public void setR_msg(String r_msg) {
		this.r_msg = r_msg;
	}

}
