package com.zd.school.salary.salary.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

@Entity
@Table(name = "XC_T_SALARYBOOKSALARY")
@AttributeOverride(name = "uuid", column = @Column(name = "SALARYBOOKSALARY_ID", length = 36, nullable = false))
public class XcSalarybooksalary extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "工资台账ID")
	@Column(name = "SALARYBOOK_ID", length = 36, nullable = true)
	private String salarybookId;

	public void setSalarybookId(String salarybookId) {
		this.salarybookId = salarybookId;
	}

	public String getSalarybookId() {
		return salarybookId;
	}

	@FieldInfo(name = "工资套账ID")
	@Column(name = "SALARYPLAT_ID", length = 36, nullable = true)
	private String salaryplatId;

	public void setSalaryplatId(String salaryplatId) {
		this.salaryplatId = salaryplatId;
	}

	public String getSalaryplatId() {
		return salaryplatId;
	}

	@FieldInfo(name = "工资项ID")
	@Column(name = "SALARYITEM_ID", length = 36, nullable = true)
	private String salaryitemId;

	public void setSalaryitemId(String salaryitemId) {
		this.salaryitemId = salaryitemId;
	}

	public String getSalaryitemId() {
		return salaryitemId;
	}

	@FieldInfo(name = "工资项名称")
	@Column(name = "SALARYITEM_NAME", length = 36, nullable = true)
	private String salaryitemName;

	public void setSalaryitemName(String salaryitemName) {
		this.salaryitemName = salaryitemName;
	}

	public String getSalaryitemName() {
		return salaryitemName;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */

}
