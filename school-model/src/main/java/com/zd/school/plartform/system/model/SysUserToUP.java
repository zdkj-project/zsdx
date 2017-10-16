package com.zd.school.plartform.system.model;

import java.io.Serializable;

public class SysUserToUP implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String employeeId;
	private String employeeName;
	private String employeeStrId;
	private String sexId;
	private String identifier;
	private String sid;	//尚未明确SID的用处，此字段原本用于存放发卡后的卡片uuid，与CardInfo表对应，现在不使用了
	private String departmentId;
	private String jobName;	//用于获取用户的编制、或者是否为学员，然后便于初始化卡片类型
	
	private String employeePwd;
	private String cardState;
	
	private Integer isDelete=0;
	private String employeeTel;
	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeStrId() {
		return employeeStrId;
	}

	public void setEmployeeStrId(String employeeStrId) {
		this.employeeStrId = employeeStrId;
	}

	public String getSexId() {
		return sexId;
	}

	public void setSexId(String sexId) {
		this.sexId = sexId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSid() {
		if(sid==null)
			return "";
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getEmployeePwd() {
		return employeePwd;
	}

	public void setEmployeePwd(String employeePwd) {
		this.employeePwd = employeePwd;
	}
	

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public SysUserToUP() {
		super();
	}

	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

    public String getEmployeeTel() {
        return employeeTel;
    }

    public void setEmployeeTel(String employeeTel) {
        this.employeeTel = employeeTel;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		SysUserToUP other = (SysUserToUP) obj;

		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (employeeName == null) {
			if (other.employeeName != null)
				return false;
		} else if (!employeeName.equals(other.employeeName))
			return false;
		/*
		if (employeeStrId == null) {
			if (other.employeeStrId != null)
				return false;
		} else if (!employeeStrId.equals(other.employeeStrId))
			return false;
		*/
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		
		if (sexId == null) {
			if (other.sexId != null)
				return false;
		} else if (!sexId.equals(other.sexId))
			return false;

		if(employeeTel==null){
		    if(other.employeeTel!=null)
		        return  false;
        } else if (!employeeTel.equals(other.employeeTel)){
		    return false;
        }
		/*尚未明确SID的用处，故注释
		if (sid == null) {
			if (other.sid != null)
				return false;
		} else if (!sid.equals(other.sid))
			return false;
		*/
		return true;
	}

}
