package com.zd.school.opu;

public class CreateOrderPerson {

	// 姓名
	private String name;
	// 身份证号码
	private String usercardNo;
	// 性别 男 女
	private String sex;
	// 入住日期 2018-03-15
	private String checkinDate;
	// 离店日期 2018-04-08
	private String checkoutDate;
	// 是否住宿 0住宿 1不住宿
	private int isCheckin;

	private String studentno;

	// 房类
	private String roomtype;



	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}

	public String getStudentno() {
		return studentno;
	}

	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}

	public CreateOrderPerson() {

	}

	public CreateOrderPerson(String name, String usercardNo, String sex, String checkinDate, String checkoutDate,
							 int isCheckin,String studentno) {
		super();
		this.name = name;
		this.usercardNo = usercardNo;
		this.sex = sex;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.isCheckin = isCheckin;
		this.studentno = studentno;

	}
	public CreateOrderPerson(String name, String usercardNo, String sex, String checkinDate, String checkoutDate,
							 int isCheckin,String studentno,String roomtype) {
		super();
		this.name = name;
		this.usercardNo = usercardNo;
		this.sex = sex;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.isCheckin = isCheckin;
		this.studentno = studentno;
		this.roomtype=roomtype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsercardNo() {
		return usercardNo;
	}

	public void setUsercardNo(String usercardNo) {
		this.usercardNo = usercardNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public int getIsCheckin() {
		return isCheckin;
	}

	public void setIsCheckin(int isCheckin) {
		this.isCheckin = isCheckin;
	}

}
