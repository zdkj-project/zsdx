package com.orcl.sync.model.hibernate.hibernate;
// Generated 2017-4-27 9:47:22 by Hibernate Tools 4.0.0

import javax.persistence.*;
import java.util.Date;

/**
 * HrPosition generated by hbm2java
 */
//@Entity
//@Table(name = "HR_POSITION", schema = "ZSDX_SYNC")
public class HrPosition implements java.io.Serializable {

	private String id;
	private Integer orderby;
	private String names;
	private String duty;
	private String createBy;
	private String createName;
	private Date createDate;
	private String updateBy;
	private String updateName;
	private Date updateDate;

	public HrPosition() {
	}

	public HrPosition(String id) {
		this.id = id;
	}

	public HrPosition(String id, Integer orderby, String names, String duty, String createBy, String createName,
			Date createDate, String updateBy, String updateName, Date updateDate) {
		this.id = id;
		this.orderby = orderby;
		this.names = names;
		this.duty = duty;
		this.createBy = createBy;
		this.createName = createName;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateName = updateName;
		this.updateDate = updateDate;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ORDERBY", precision = 22, scale = 0)
	public Integer getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	@Column(name = "NAMES", length = 60)
	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	@Column(name = "DUTY", length = 1500)
	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
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

}
