package com.orcl.sync.model.hibernate.hibernate;
// Generated 2017-4-27 15:22:10 by Hibernate Tools 4.0.0

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;

/**
 * HrDeptPosition generated by hbm2java
 */
@Entity
@Table(name = "HR_DEPT_POSITION", schema = "ZSDX_SYNC")
public class HrDeptPosition implements java.io.Serializable {

	private String id;
	private String positionId;
	private String departmentId;
	private String prepositionId;
	private String predepartmentId;
	private Clob descriptions;
	private String createBy;
	private String createName;
	private Date createDate;
	private String updateBy;
	private String updateName;
	private Date updateDate;

	public HrDeptPosition() {
	}

	public HrDeptPosition(String id) {
		this.id = id;
	}

	public HrDeptPosition(String id, String positionId, String departmentId, String prepositionId,
			String predepartmentId, Clob descriptions, String createBy, String createName, Date createDate,
			String updateBy, String updateName, Date updateDate) {
		this.id = id;
		this.positionId = positionId;
		this.departmentId = departmentId;
		this.prepositionId = prepositionId;
		this.predepartmentId = predepartmentId;
		this.descriptions = descriptions;
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

	@Column(name = "POSITION_ID", length = 32)
	public String getPositionId() {
		return this.positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	@Column(name = "DEPARTMENT_ID", length = 32)
	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "PREPOSITION_ID", length = 32)
	public String getPrepositionId() {
		return this.prepositionId;
	}

	public void setPrepositionId(String prepositionId) {
		this.prepositionId = prepositionId;
	}

	@Column(name = "PREDEPARTMENT_ID", length = 32)
	public String getPredepartmentId() {
		return this.predepartmentId;
	}

	public void setPredepartmentId(String predepartmentId) {
		this.predepartmentId = predepartmentId;
	}

	@Column(name = "DESCRIPTIONS")
	public Clob getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(Clob descriptions) {
		this.descriptions = descriptions;
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
