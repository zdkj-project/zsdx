package com.zd.school.oa.notice.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaNotice Function: TODO ADD FUNCTION. Reason: TODO ADD REASON(可选).
 * Description: 公告信息表(OA_T_NOTICE)实体类. date: 2016-12-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "OA_T_NOTICE")
@AttributeOverride(name = "uuid", column = @Column(name = "NOTICE_ID", length = 36, nullable = false))
public class OaNotice extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "公告标题")
	@Column(name = "NOTICE_TITLE", length = 128, nullable = false)
	private String noticeTitle;

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	@FieldInfo(name = "公告类型(数据字典NOTICETYPE)")
	@Column(name = "NOTICE_TYPE", length = 4, nullable = false)
	private String noticeType;

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeType() {
		return noticeType;
	}

	@FieldInfo(name = "紧急程度(数据字典EMERGENCY)")
	@Column(name = "EMERGENCY", length = 4, nullable = false)
	private String emergency;

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getEmergency() {
		return emergency;
	}

	@FieldInfo(name = "公告正文")
	@Column(name = "NOTICE_CONTENT",/* length = 2048,*/ nullable = false,columnDefinition="varchar(MAX)")
	private String noticeContent;

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	@FieldInfo(name = "是否微信通知(0-不通知，1-通知)")
	@Column(name = "SEND_WX", length = 1, nullable = false)
	private String sendWx;

	public void setSendWx(String sendWx) {
		this.sendWx = sendWx;
	}

	public String getSendWx() {
		return sendWx;
	}

	@FieldInfo(name = "是否需要审核(0-不需要，1-需要,2-审核通过,3-审核不通过)")
	@Column(name = "IS_CHECK", length = 1, nullable = false)
	private String isCheck;

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getIsCheck() {
		return isCheck;
	}

	@FieldInfo(name = "生效日期")
	@Column(name = "BEGIN_DATE", length = 23, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date beginDate;

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	@FieldInfo(name = "中止日期")
	@Column(name = "END_DATE", length = 23, nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date endDate;

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@JsonIgnore
	@FieldInfo(name = "公告通知人员")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OA_T_NOTICEUSER", joinColumns = { @JoinColumn(name = "NOTICE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "USER_ID") })
	@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<SysUser> noticeUsers = new HashSet<SysUser>();

	public Set<SysUser> getNoticeUsers() {
		return noticeUsers;
	}

	public void setNoticeUsers(Set<SysUser> noticeUsers) {
		this.noticeUsers = noticeUsers;
	}

	@JsonIgnore
	@FieldInfo(name = "公告通知角色")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OA_T_NOTICEROLE", joinColumns = { @JoinColumn(name = "NOTICE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<SysRole> noticeRoles = new HashSet<SysRole>();

	public Set<SysRole> getNoticeRoles() {
		return noticeRoles;
	}

	public void setNoticeRoles(Set<SysRole> noticeRoles) {
		this.noticeRoles = noticeRoles;
	}

	@JsonIgnore
	@FieldInfo(name = "公告通知部门")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OA_T_NOTICEDEPT", joinColumns = { @JoinColumn(name = "NOTICE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "DEPT_ID") })
	@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<BaseOrg> noticeDepts = new HashSet<BaseOrg>();

	public Set<BaseOrg> getNoticeDepts() {
		return noticeDepts;
	}

	public void setNoticeDepts(Set<BaseOrg> noticeUDepts) {
		this.noticeDepts = noticeUDepts;
	}

	@JsonIgnore
	@FieldInfo(name = "公告通知审核人")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "oaNotice")
	private Set<OaNoticeauditor> noticeAutitors = new HashSet<OaNoticeauditor>();

	public Set<OaNoticeauditor> getNoticeAutitors() {
		return noticeAutitors;
	}

	public void setNoticeAutitors(Set<OaNoticeauditor> noticeAutitors) {
		this.noticeAutitors = noticeAutitors;
	}

	@FieldInfo(name = "发布部门id")
	@Column(name = "PUBDEPT_ID", length = 36, nullable = true)
	private String pubdeptId;

	public String getPubdeptId() {
		return pubdeptId;
	}

	public void setPubdeptId(String pubdeptId) {
		this.pubdeptId = pubdeptId;
	}

	@FieldInfo(name = "发布部门名称")
	@Column(name = "PUBDEPT_NAME", length = 128, nullable = true)
	private String pubdeptName;

	public String getPubdeptName() {
		return pubdeptName;
	}

	public void setPubdeptName(String pubdeptName) {
		this.pubdeptName = pubdeptName;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}