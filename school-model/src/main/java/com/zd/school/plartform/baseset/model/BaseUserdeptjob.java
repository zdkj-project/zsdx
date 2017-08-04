package com.zd.school.plartform.baseset.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import org.hibernate.annotations.Formula;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * ClassName: BaseUserdeptjob Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户部门岗位(BASE_T_USERDEPTJOB)实体类. date: 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_USERDEPTJOB")
@AttributeOverride(name = "uuid", column = @Column(name = "USERDEPTJOB_ID", length = 36, nullable = false))
public class BaseUserdeptjob extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "用户ID")
	@Column(name = "USER_ID", length = 36, nullable = false)
	private String userId;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	@FieldInfo(name = "部门岗位ID")
	@Column(name = "DEPTJOB_ID", length = 36, nullable = true)
	private String deptjobId;

	public void setDeptjobId(String deptjobId) {
		this.deptjobId = deptjobId;
	}

	public String getDeptjobId() {
		return deptjobId;
	}

	@FieldInfo(name = "部门ID")
	@Column(name = "DEPT_ID", length = 36, nullable = true)
	private String deptId;

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return deptId;
	}

	@FieldInfo(name = "岗位ID")
	@Column(name = "JOB_ID", length = 36, nullable = true)
	private String jobId;

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	@FieldInfo(name = "是否主部门 0-不是 1-是")
	@Column(name = "MASTER_DEPT", length = 5, nullable = true)
	private Integer masterDept;

	public void setMasterDept(Integer masterDept) {
		this.masterDept = masterDept;
	}

	public Integer getMasterDept() {
		return masterDept;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@FieldInfo(name = "部门名称")
	@Formula("(SELECT a.NODE_TEXT FROM dbo.BASE_T_ORG a WHERE a.DEPT_ID=DEPT_ID )")
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@FieldInfo(name = "岗位名称")
	@Formula("(SELECT a.JOB_NAME FROM dbo.BASE_T_JOB a WHERE a.JOB_ID=JOB_ID )")
	private String jobName;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
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

	@FieldInfo(name = "部门全称")
	@Formula("(SELECT a.ALL_DEPTNAME FROM dbo.BASE_T_ORG a WHERE a.DEPT_ID=DEPT_ID )")
	private String allDeptName;

	public String getAllDeptName() {
		return allDeptName;
	}

	public void setAllDeptName(String allDeptName) {
		this.allDeptName = allDeptName;
	}
	
	@FieldInfo(name = "部门全称")
	@Formula("(SELECT a.TREE_IDS FROM dbo.BASE_T_ORG a WHERE a.DEPT_ID=DEPT_ID )")
	private String treeIds;

	public String getTreeIds() {
		return treeIds;
	}

	public void setTreeIds(String treeIds) {
		this.treeIds = treeIds;
	}
	
	public BaseUserdeptjob() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseUserdeptjob(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
}