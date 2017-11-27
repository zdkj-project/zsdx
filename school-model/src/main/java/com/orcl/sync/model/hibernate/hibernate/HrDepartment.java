package com.orcl.sync.model.hibernate.hibernate;
// Generated 2017-4-27 9:47:22 by Hibernate Tools 4.0.0

import javax.persistence.*;
import java.util.Date;

/**
 * HrDepartment generated by hbm2java
 */
//@Entity
//@Table(name = "HR_DEPARTMENT", schema = "ZSDX_SYNC")
public class HrDepartment implements java.io.Serializable {

	private String id;
	private String names;
	private String parentid;
	private String postalcode;
	private String phone;
	private String fax;
	private String email;
	private String code;
	private String workplaceId;
	private Integer orderby;
	private String createBy;
	private String createName;
	private Date createDate;
	private String updateBy;
	private String updateName;
	private Date updateDate;
	private String adminregionId;
	private Integer isEnable;

	public HrDepartment() {
	}

	public HrDepartment(String id) {
		this.id = id;
	}

	public HrDepartment(String id, String names, String parentid, String postalcode, String phone, String fax,
			String email, String code, String workplaceId, Integer orderby, String createBy, String createName,
			Date createDate, String updateBy, String updateName, Date updateDate, String adminregionId,
			Integer isEnable) {
		this.id = id;
		this.names = names;
		this.parentid = parentid;
		this.postalcode = postalcode;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
		this.code = code;
		this.workplaceId = workplaceId;
		this.orderby = orderby;
		this.createBy = createBy;
		this.createName = createName;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateName = updateName;
		this.updateDate = updateDate;
		this.adminregionId = adminregionId;
		this.isEnable = isEnable;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAMES", length = 90)
	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	@Column(name = "PARENTID", length = 36)
	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	@Column(name = "POSTALCODE", length = 40)
	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@Column(name = "PHONE", length = 40)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "FAX", length = 40)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "EMAIL", length = 40)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CODE", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "WORKPLACE_ID", length = 36)
	public String getWorkplaceId() {
		return this.workplaceId;
	}

	public void setWorkplaceId(String workplaceId) {
		this.workplaceId = workplaceId;
	}

	@Column(name = "ORDERBY", precision = 22, scale = 0)
	public Integer getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
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

	@Column(name = "ADMINREGION_ID", length = 36)
	public String getAdminregionId() {
		return this.adminregionId;
	}

	public void setAdminregionId(String adminregionId) {
		this.adminregionId = adminregionId;
	}

	@Column(name = "IS_ENABLE", precision = 6, scale = 0)
	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

}
