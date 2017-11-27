package com.orcl.sync.model.hibernate.hibernate;
// Generated 2017-4-27 9:47:22 by Hibernate Tools 4.0.0

import javax.persistence.*;
import java.util.Date;

/**
 * HrUser generated by hbm2java
 */
//@Entity
//@Table(name = "HR_USER", schema = "ZSDX_SYNC")
public class HrUser implements java.io.Serializable {

	private String id;
	private String userName;
	private String userType;
	private String accounts;
	private String passwords;
	private Integer authType;
	private String mobilePhone;
	private String officePhone;
	private String workplaceId;
	private String email;
	private String jobNumber;
	private String fileNumber;
	private Date enterTime;
	private Date leaveTime;
	private String identitycard;
	private String userPic;
	private String remark;
	private Integer orderBy;
	private Integer isEnable;
	private Integer isOnthejob;
	private String createBy;
	private String createName;
	private Date createDate;
	private String updateBy;
	private String updateName;
	private Date updateDate;
	private String rand;
	private String skinfolder;
	private String skinname;
	private String userSex;
	private Date birthdate;
	private String mobilePhone2;
	private String houseNumber;
	private String politicaltype;
	private String partybranch;
	private String staffroom;
	private String tradeunion;
	private String userType2;
	private String salarypwd;
	private String oldAccount;
	private String school;
	private Date graduationDate;
	private String major;
	private String education;
	private String degree;
	private String professionalTitle;
	private Integer safeLevel;

	public HrUser() {
	}

	public HrUser(String id) {
		this.id = id;
	}

	public HrUser(String id, String userName, String userType, String accounts, String passwords, Integer authType,
			String mobilePhone, String officePhone, String workplaceId, String email, String jobNumber,
			String fileNumber, Date enterTime, Date leaveTime, String identitycard, String userPic, String remark,
			Integer orderBy, Integer isEnable, Integer isOnthejob, String createBy, String createName, Date createDate,
			String updateBy, String updateName, Date updateDate, String rand, String skinfolder, String skinname,
			String userSex, Date birthdate, String mobilePhone2, String houseNumber, String politicaltype,
			String partybranch, String staffroom, String tradeunion, String userType2, String salarypwd,
			String oldAccount, String school, Date graduationDate, String major, String education, String degree,
			String professionalTitle, Integer safeLevel) {
		this.id = id;
		this.userName = userName;
		this.userType = userType;
		this.accounts = accounts;
		this.passwords = passwords;
		this.authType = authType;
		this.mobilePhone = mobilePhone;
		this.officePhone = officePhone;
		this.workplaceId = workplaceId;
		this.email = email;
		this.jobNumber = jobNumber;
		this.fileNumber = fileNumber;
		this.enterTime = enterTime;
		this.leaveTime = leaveTime;
		this.identitycard = identitycard;
		this.userPic = userPic;
		this.remark = remark;
		this.orderBy = orderBy;
		this.isEnable = isEnable;
		this.isOnthejob = isOnthejob;
		this.createBy = createBy;
		this.createName = createName;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateName = updateName;
		this.updateDate = updateDate;
		this.rand = rand;
		this.skinfolder = skinfolder;
		this.skinname = skinname;
		this.userSex = userSex;
		this.birthdate = birthdate;
		this.mobilePhone2 = mobilePhone2;
		this.houseNumber = houseNumber;
		this.politicaltype = politicaltype;
		this.partybranch = partybranch;
		this.staffroom = staffroom;
		this.tradeunion = tradeunion;
		this.userType2 = userType2;
		this.salarypwd = salarypwd;
		this.oldAccount = oldAccount;
		this.school = school;
		this.graduationDate = graduationDate;
		this.major = major;
		this.education = education;
		this.degree = degree;
		this.professionalTitle = professionalTitle;
		this.safeLevel = safeLevel;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USER_NAME", length = 36)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_TYPE", length = 20)
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "ACCOUNTS", length = 32)
	public String getAccounts() {
		return this.accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	@Column(name = "PASSWORDS", length = 300)
	public String getPasswords() {
		return this.passwords;
	}

	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}

	@Column(name = "AUTH_TYPE", precision = 6, scale = 0)
	public Integer getAuthType() {
		return this.authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	@Column(name = "MOBILE_PHONE", length = 20)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "OFFICE_PHONE", length = 50)
	public String getOfficePhone() {
		return this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "WORKPLACE_ID", length = 36)
	public String getWorkplaceId() {
		return this.workplaceId;
	}

	public void setWorkplaceId(String workplaceId) {
		this.workplaceId = workplaceId;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "JOB_NUMBER", length = 50)
	public String getJobNumber() {
		return this.jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	@Column(name = "FILE_NUMBER", length = 50)
	public String getFileNumber() {
		return this.fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTER_TIME", length = 7)
	public Date getEnterTime() {
		return this.enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LEAVE_TIME", length = 7)
	public Date getLeaveTime() {
		return this.leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	@Column(name = "IDENTITYCARD", length = 50)
	public String getIdentitycard() {
		return this.identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	@Column(name = "USER_PIC")
	public String getUserPic() {
		return this.userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "ORDER_BY", precision = 6, scale = 0)
	public Integer getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "IS_ENABLE", precision = 6, scale = 0)
	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "IS_ONTHEJOB", precision = 6, scale = 0)
	public Integer getIsOnthejob() {
		return this.isOnthejob;
	}

	public void setIsOnthejob(Integer isOnthejob) {
		this.isOnthejob = isOnthejob;
	}

	@Column(name = "CREATE_BY", length = 36)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_NAME", length = 32)
	public String getCreateName() {
		return this.createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_BY", length = 36)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "UPDATE_NAME", length = 32)
	public String getUpdateName() {
		return this.updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_DATE", length = 7)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "RAND", length = 50)
	public String getRand() {
		return this.rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	@Column(name = "SKINFOLDER", length = 100)
	public String getSkinfolder() {
		return this.skinfolder;
	}

	public void setSkinfolder(String skinfolder) {
		this.skinfolder = skinfolder;
	}

	@Column(name = "SKINNAME", length = 100)
	public String getSkinname() {
		return this.skinname;
	}

	public void setSkinname(String skinname) {
		this.skinname = skinname;
	}

	@Column(name = "USER_SEX", length = 10)
	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDATE", length = 7)
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name = "MOBILE_PHONE2", length = 20)
	public String getMobilePhone2() {
		return this.mobilePhone2;
	}

	public void setMobilePhone2(String mobilePhone2) {
		this.mobilePhone2 = mobilePhone2;
	}

	@Column(name = "HOUSE_NUMBER", length = 10)
	public String getHouseNumber() {
		return this.houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Column(name = "POLITICALTYPE", length = 20)
	public String getPoliticaltype() {
		return this.politicaltype;
	}

	public void setPoliticaltype(String politicaltype) {
		this.politicaltype = politicaltype;
	}

	@Column(name = "PARTYBRANCH", length = 100)
	public String getPartybranch() {
		return this.partybranch;
	}

	public void setPartybranch(String partybranch) {
		this.partybranch = partybranch;
	}

	@Column(name = "STAFFROOM", length = 100)
	public String getStaffroom() {
		return this.staffroom;
	}

	public void setStaffroom(String staffroom) {
		this.staffroom = staffroom;
	}

	@Column(name = "TRADEUNION", length = 100)
	public String getTradeunion() {
		return this.tradeunion;
	}

	public void setTradeunion(String tradeunion) {
		this.tradeunion = tradeunion;
	}

	@Column(name = "USER_TYPE2", length = 20)
	public String getUserType2() {
		return this.userType2;
	}

	public void setUserType2(String userType2) {
		this.userType2 = userType2;
	}

	@Column(name = "SALARYPWD", length = 50)
	public String getSalarypwd() {
		return this.salarypwd;
	}

	public void setSalarypwd(String salarypwd) {
		this.salarypwd = salarypwd;
	}

	@Column(name = "OLD_ACCOUNT", length = 50)
	public String getOldAccount() {
		return this.oldAccount;
	}

	public void setOldAccount(String oldAccount) {
		this.oldAccount = oldAccount;
	}

	@Column(name = "SCHOOL", length = 300)
	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "GRADUATION_DATE", length = 7)
	public Date getGraduationDate() {
		return this.graduationDate;
	}

	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}

	@Column(name = "MAJOR", length = 300)
	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "EDUCATION", length = 300)
	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Column(name = "DEGREE", length = 300)
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "PROFESSIONAL_TITLE", length = 300)
	public String getProfessionalTitle() {
		return this.professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}

	@Column(name = "SAFE_LEVEL", precision = 6, scale = 0)
	public Integer getSafeLevel() {
		return this.safeLevel;
	}

	public void setSafeLevel(Integer safeLevel) {
		this.safeLevel = safeLevel;
	}

}
