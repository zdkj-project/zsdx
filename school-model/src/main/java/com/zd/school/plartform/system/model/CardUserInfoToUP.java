package com.zd.school.plartform.system.model;

import java.io.Serializable;

public class CardUserInfoToUP implements Serializable {

	private static final long serialVersionUID = 1L;
	private String uuid;
	private String upCardId;
	private String userId;
	private String factNumb;
	private Integer useState;	//1为正常使用，其他貌似是无用状态（1正常、2挂失、3无卡、4退卡）
	private String sid;		//用来记录卡片的文字卡号
	private Integer employeeStatusID;	//up人员库的人员状态，值为24代表可用（由于人员数据以WEB平台为准，所以不从UP中同步后影响WEB的人员信息）
	private Integer cardTypeId;	//1职工、2合同工、3学员
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUpCardId() {
		return upCardId;
	}
	public void setUpCardId(String upCardId) {
		this.upCardId = upCardId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFactNumb() {
		return factNumb;
	}
	public void setFactNumb(String factNumb) {
		this.factNumb = factNumb;
	}
	public Integer getUseState() {
		return useState;
	}
	public void setUseState(Integer useState) {
		this.useState = useState;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Integer getEmployeeStatusID() {
		return employeeStatusID;
	}
	public void setEmployeeStatusID(Integer employeeStatusID) {
		this.employeeStatusID = employeeStatusID;
	}
	
	public Integer getCardTypeId() {
		return cardTypeId;
	}
	public void setCardTypeId(Integer cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	public CardUserInfoToUP() {
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardUserInfoToUP other = (CardUserInfoToUP) obj;
		if (factNumb == null) {
			if (other.factNumb != null)
				return false;
		} else if (!factNumb.equals(other.factNumb))
			return false;
		if (upCardId == null) {
			if (other.upCardId != null)
				return false;
		} else if (!upCardId.equals(other.upCardId))
			return false;
		if (useState == null) {
			if (other.useState != null)
				return false;
		} else if (!useState.equals(other.useState))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
//		if (sid == null) {
//			if (other.sid != null)
//				return false;
//		} else if (!sid.equals(other.sid))
//			return false;
		return true;
	}
	
	
	
}
