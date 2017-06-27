package com.zd.school.plartform.system.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseTRole Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_ROLE")
@AttributeOverride(name = "uuid", column = @Column(name = "ROLE_ID", length = 36, nullable = false))
public class SysRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "角色编码")
    @Column(name = "ROLE_CODE", length = 12, nullable = false)
    private String roleCode;

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    @FieldInfo(name = "角色名称")
    @Column(name = "ROLE_NAME", length = 32, nullable = false)
    private String roleName;

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @FieldInfo(name = "是否系统角色")
    @Column(name = "ISSYSTEM", length = 10, nullable = false)
    private Integer issystem;

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Integer getIssystem() {
        return issystem;
    }

    @FieldInfo(name = "备注")
    @Column(name = "REMARK", length = 128, nullable = true)
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    @FieldInfo(name = "有权限的菜单")
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "SYS_T_ROLEPERM", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = {
            @JoinColumn(name = "PER_ID") })
    private Set<SysPermission> sysPermissions = new HashSet<SysPermission>();

    public Set<SysPermission> getSysPermissions() {
        return sysPermissions;
    }

    public void setSysPermissions(Set<SysPermission> sysPermissions) {
        this.sysPermissions = sysPermissions;
    }

    @FieldInfo(name = "是否隐藏,0-不隐藏 1-隐藏")
    @Column(name = "ISHIDDEN", length = 10, nullable = true)
    private String isHidden;

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }
    
    @FieldInfo(name = "备注")
    @Column(name = "SCHOOL_ID", length = 128, nullable = true)
    private String schoolId;
    
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public SysRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SysRole(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
    
    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}