package com.zd.school.opu;

public class CreateOrderPerson {

	// 身份证号码
	private String idCode;
	// 姓名
	private String name;
	// 性别 男 女
	private String phone;
	//主随客，0：主客    1：随客
	private int isOther;
	//是否住宿 0住宿 1不住宿
	private int isCheckin;
	// 学号
	private String studentno;

	public CreateOrderPerson(){}
	public CreateOrderPerson(String idCode, String name, String phone, int isOther, int isCheckin, String studentno) {
		this.idCode = idCode;
		this.name = name;
		this.phone = phone;
		this.isOther = isOther;
		this.isCheckin = isCheckin;
		this.studentno = studentno;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
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

	public int getIsOther() {
		return isOther;
	}

	public void setIsOther(int isOther) {
		this.isOther = isOther;
	}

	public int getIsCheckin() {
		return isCheckin;
	}

	public void setIsCheckin(int isCheckin) {
		this.isCheckin = isCheckin;
	}

	public String getStudentno() {
		return studentno;
	}

	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}
}
