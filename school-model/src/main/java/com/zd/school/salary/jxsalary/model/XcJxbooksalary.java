package com.zd.school.salary.jxsalary.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

@Entity
@Table(name = "XC_T_JXBOOKSALARY")
@AttributeOverride(name = "uuid", column = @Column(name = "JXBOOKSALARY_ID", length = 36, nullable = false))
public class XcJxbooksalary extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "绩效台账ID")
    @Column(name = "JXBOOK_ID", length = 36, nullable = true)
    private String jxbookId;
    public void setJxbookId(String jxbookId) {
        this.jxbookId = jxbookId;
    }
    public String getJxbookId() {
        return jxbookId;
    }

    @FieldInfo(name = "绩效套账ID")
    @Column(name = "JXPLART_ID", length = 36, nullable = true)
    private String jxplartId;
    public void setJxplartId(String jxplartId) {
        this.jxplartId = jxplartId;
    }
    public String getJxplartId() {
        return jxplartId;
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
