package com.zd.school.jw.train.model.vo;

public class VoTrainClasstrainee {
	//班级id
	private String classId;
	
    //人员姓名
	private String xm;
	//人员id
	private String traineeId;
	//物理卡号
	private String factoryfixId;
	//流水号
	private String cardNo;
	//卡片使用状态
	private Integer useState;
	
	//是否请假
	private  String isLeave;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getTraineeId() {
		return traineeId;
	}
	public void setTraineeId(String traineeId) {
		this.traineeId = traineeId;
	}
	public String getFactoryfixId() {
		return factoryfixId;
	}
	public void setFactoryfixId(String factoryfixId) {
		this.factoryfixId = factoryfixId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getUseState() {
		return useState;
	}
	public void setUseState(Integer useState) {
		this.useState = useState;
	}
	public String getIsLeave() {
		return isLeave;
	}
	public void setIsLeave(String isLeave) {
		this.isLeave = isLeave;
	}
	
	

}
