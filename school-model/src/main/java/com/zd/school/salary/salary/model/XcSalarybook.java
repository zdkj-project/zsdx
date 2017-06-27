package com.zd.school.salary.salary.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: XcSalarybook Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 工资台账表(XC_T_SALARYBOOK)实体类. date: 2016-12-05
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "XC_T_SALARYBOOK")
@AttributeOverride(name = "uuid", column = @Column(name = "SALARYBOOK_ID", length = 36, nullable = false))
public class XcSalarybook extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "工资套账ID")
	@Column(name = "SALARYPLAT_ID", length = 36, nullable = true)
	private String salaryplatId;

	public void setSalaryplatId(String salaryplatId) {
		this.salaryplatId = salaryplatId;
	}

	public String getSalaryplatId() {
		return salaryplatId;
	}
	
	@Formula("(SELECT a.SALARYPLAT_NAME FROM XC_T_SALARYPLAT a WHERE a.SALARYPLAT_ID=SALARYPLAT_ID)")	
	@FieldInfo(name = "工资套账名称")
	private String salaryplatName;
	public String getSalaryplatName() {
		return salaryplatName;
	}

	public void setSalaryplatName(String salaryplatName) {
		this.salaryplatName = salaryplatName;
	}

	@FieldInfo(name = "绩效年份")
	@Column(name = "XC_YEAR", length = 10, nullable = true)
	private Integer xcYear;

	public void setXcYear(Integer xcYear) {
		this.xcYear = xcYear;
	}

	public Integer getXcYear() {
		return xcYear;
	}

	@FieldInfo(name = "绩效月份")
	@Column(name = "XC_MONTH", length = 10, nullable = true)
	private Integer xcMonth;

	public void setXcMonth(Integer xcMonth) {
		this.xcMonth = xcMonth;
	}

	public Integer getXcMonth() {
		return xcMonth;
	}

	@FieldInfo(name = "工资台账名称")
	@Column(name = "SALARY_NAME", length = 36, nullable = true)
	private String salaryName;

	public String getSalaryName() {
		return salaryName;
	}

	public void setSalaryName(String salaryName) {
		this.salaryName = salaryName;
	}

	@FieldInfo(name = "绩效工资台帐ID")
	@Column(name = "JXBOOK_ID", length = 36, nullable = true)
	private String jxbookId;

	public String getJxbookId() {
		return jxbookId;
	}

	public void setJxbookId(String jxbookId) {
		this.jxbookId = jxbookId;
	}
	
	
	@FieldInfo(name = "是否发布,0=未发布,1=已发布")
	@Column(name = "ISPUSHED", length = 10, nullable = true)
	private Integer isPushed=0;

	public Integer getIsPushed() {
		return isPushed;
	}

	public void setIsPushed(Integer isPushed) {
		this.isPushed = isPushed;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}