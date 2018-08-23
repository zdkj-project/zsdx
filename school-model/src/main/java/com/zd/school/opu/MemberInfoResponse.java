package com.zd.school.opu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zd.core.util.JsonBuilder;

public class MemberInfoResponse {

	// 会员等级代码
	private String memberType;
	// 会员等级文字描述
	private String level;
	// 会员证件号
	private String usercardNo;
	// 姓名
	private String name;
	// 手机号
	private String phone;
	
	public static OpuResult<MemberInfoResponse> fromJson(String json) {
		OpuResult<MemberInfoResponse> result = (OpuResult<MemberInfoResponse>) JsonBuilder.getInstance()
				.fromJson(json, new TypeReference<OpuResult<MemberInfoResponse>>() {
				});
		return result;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUsercardNo() {
		return usercardNo;
	}

	public void setUsercardNo(String usercardNo) {
		this.usercardNo = usercardNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
