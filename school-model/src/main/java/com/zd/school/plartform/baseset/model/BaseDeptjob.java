package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseDeptjob Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 部门岗位信息(BASE_T_DEPTJOB)实体类. date: 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_DEPTJOB")
@AttributeOverride(name = "uuid", column = @Column(name = "DEPTJOB_ID", length = 36, nullable = false))
public class BaseDeptjob extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "部门ID")
	@Column(name = "DEPT_ID", length = 36, nullable = false)
	private String deptId;

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return deptId;
	}

	@FieldInfo(name = "岗位ID")
	@Column(name = "JOB_ID", length = 36, nullable = false)
	private String jobId;

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	@FieldInfo(name = "岗位类型 2-普通岗位 -1副负责岗位  0-主负责岗位")
	@Column(name = "JOB_TYPE", length = 5, nullable = false)
	private Integer jobType;

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public Integer getJobType() {
		return jobType;
	}

	@FieldInfo(name = "上级部门ID")
	@Column(name = "PARENTDEPT_ID", length = 36, nullable = true)
	private String parentdeptId;

	public String getParentdeptId() {
		return parentdeptId;
	}

	public void setParentdeptId(String parentdeptId) {
		this.parentdeptId = parentdeptId;
	}

	@FieldInfo(name = "上级岗位ID")
	@Column(name = "PARENTJOB_ID", length = 36, nullable = true)
	private String parentjobId;

	public String getParentjobId() {
		return parentjobId;
	}

	public void setParentjobId(String parentjobId) {
		this.parentjobId = parentjobId;
	}

	@FieldInfo(name = "部门名称")
	@Column(name = "DEPT_NAME", length = 36, nullable = true)
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@FieldInfo(name = "岗位名称")
	@Column(name = "JOB_NAME", length = 36, nullable = true)
	private String jobName;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@FieldInfo(name = "上级部门名称")
	@Column(name = "PARENTDEPT_NAME", length = 36, nullable = true)
	private String parentdeptName;

	public String getParentdeptName() {
		return parentdeptName;
	}

	public void setParentdeptName(String parentdeptName) {
		this.parentdeptName = parentdeptName;
	}

	@FieldInfo(name = "上级岗位名称")
	@Column(name = "PARENTJOB_NAME", length = 36, nullable = true)
	private String parentjobName;

	public String getParentjobName() {
		return parentjobName;
	}

	public void setParentjobName(String parentjobName) {
		this.parentjobName = parentjobName;
	}

	@FieldInfo(name = "部门全称")
	@Column(name = "ALL_DEPTNAME", length = 500, nullable = true)
	private String allDeptName;

	public String getAllDeptName() {
		return allDeptName;
	}

	public void setAllDeptName(String allDeptName) {
		this.allDeptName = allDeptName;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@FieldInfo(name = "部门岗位名称")
	@Formula("(SELECT a.DEPT_NAME+a.JOB_NAME FROM BASE_T_DEPTJOB a WHERE a.DEPTJOB_ID=DEPTJOB_ID)")
	private String deptjobName;

	public String getDeptjobName() {
		return deptjobName;
	}

	public void setDeptjobName(String deptjobName) {
		this.deptjobName = deptjobName;
	}

	@FieldInfo(name = "部门岗位全称")
	@Formula("(SELECT a.ALL_DEPTNAME+a.JOB_NAME FROM BASE_T_DEPTJOB a WHERE a.DEPTJOB_ID=DEPTJOB_ID)")
	private String alldeptjobName;

	public String getAlldeptjobName() {
		return alldeptjobName;
	}

	public void setAlldeptjobName(String alldeptjobName) {
		this.alldeptjobName = alldeptjobName;
	}

	@FieldInfo(name = "岗位级别")
	@Formula("(SELECT a.ORDER_INDEX FROM dbo.BASE_T_JOB a WHERE a.JOB_ID=JOB_ID )")
	private Integer jobLevel;

	public Integer getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(Integer jobLevel) {
		this.jobLevel = jobLevel;
	}
	
	
	public BaseDeptjob() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseDeptjob(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
}