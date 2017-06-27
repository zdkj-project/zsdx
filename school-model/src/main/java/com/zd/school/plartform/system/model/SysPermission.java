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

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseTPerimisson Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 权限表实体类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_PERIMISSON")
@AttributeOverride(name = "uuid", column = @Column(name = "PER_ID", length = 36, nullable = false))
public class SysPermission extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "权限类型")
    @Column(name = "PER_TYPE", length = 8, nullable = false)
    private String perType;

    public void setPerType(String perType) {
        this.perType = perType;
    }

    public String getPerType() {
        return perType;
    }

    @FieldInfo(name = "权限码")
    @Column(name = "PER_CODE", length = 36, nullable = false)
    private String perCode;

    public void setPerCode(String perCode) {
        this.perCode = perCode;
    }

    public String getPerCode() {
        return perCode;
    }
    
    @FieldInfo(name = "权限名称")
    @Formula("(SELECT a.NODE_TEXT FROM SYS_T_MENU a WHERE a.MENU_ID=PER_CODE)")
    private String perText;
    
    public String getPerText() {
		return perText;
	}

	public void setPerText(String perText) {
		this.perText = perText;
	}

    @FieldInfo(name = "权限路径")
    @Column(name = "PER_PATH")
    private String perPath;

    public String getPerPath() {
        return perPath;
    }

    public void setPerPath(String perPath) {
        this.perPath = perPath;
    }

    @FieldInfo(name = "有权限的角色")
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "SYS_T_ROLEPERM", joinColumns = { @JoinColumn(name = "PER_ID") }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID") })
    private Set<SysRole> sysRoles = new HashSet<SysRole>();

    public Set<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(Set<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

	

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}