package com.zd.school.oa.notice.model;

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
 * ClassName: OaNoticeauditor 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告权限(OA_T_NOTICERIGHT)实体类.
 * date: 2016-12-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Entity
@Table(name = "OA_T_NOTICERIGHT")
@AttributeOverride(name = "uuid", column = @Column(name = "RIGHT_ID", length = 36, nullable = false))
public class OaNoticeright extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldInfo(name = "拥有权限的角色ID")
    @Column(name = "OWN_ROLEID", length = 36, nullable = false)
    private String ownRoleid;

	public String getOwnRoleid() {
		return ownRoleid;
	}

	public void setOwnRoleid(String ownRoleid) {
		this.ownRoleid = ownRoleid;
	}
	
	@FieldInfo(name = "拥有权限的角色名称")
    @Formula("(SELECT a.ROLE_NAME FROM SYS_T_ROLE a WHERE a.ROLE_ID=OWN_ROLEID)")
	private String ownRoleName;
	public String getOwnRoleName() {
		return ownRoleName;
	}

	public void setOwnRoleName(String ownRoleName) {
		this.ownRoleName = ownRoleName;
	}
	
	@FieldInfo(name = "审核的角色ID")
    @Column(name = "CHECK_ROLEID", length = 36, nullable = true)
    private String checkRoleid;

	public String getCheckRoleid() {
		return checkRoleid;
	}

	public void setCheckRoleid(String checkRoleid) {
		this.checkRoleid = checkRoleid;
	}
	
	@FieldInfo(name = "审核角色的名称")
    @Formula("(SELECT a.ROLE_NAME FROM SYS_T_ROLE a WHERE a.ROLE_ID=CHECK_ROLEID)")
	private String checkRoleName;
	public String getCheckRoleName() {
		return checkRoleName;
	}

	public void setCheckRoleName(String checkRoleName) {
		this.checkRoleName = checkRoleName;
	}
	

	
}
