package com.zd.school.build.define.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 综合前置管理
 * 
 * @author hucy
 *
 */
@Entity
@Table(name = "SYS_FRONTSERVER")
@AttributeOverride(name = "uuid", column = @Column(name = "FRONTSERVER_ID", length = 36, nullable = false) )
public class SysFrontServer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "服务器名称")
	@Column(name = "FRONTSERVER_NAME", length = 200, nullable = true)
	private String frontServerName;

	@FieldInfo(name = "服务IP")
	@Column(name = "FRONTSERVER_IP", length = 200, nullable = true)
	private String frontServerIp;

	@FieldInfo(name = "服务端口")
	@Column(name = "FRONTSERVER_PORT")
	private Integer frontServerPort;

	@FieldInfo(name = "请求任务URL")
	@Column(name = "FRONTSERVER_URL", length = 100, nullable = true)
	private String frontServerUrl;

	@FieldInfo(name = "是否启用 0启用1禁用")
	@Column(name = "FRONTSERVER_STATUS")
	private Integer frontServerStatus;

	@FieldInfo(name = "备注")
	@Column(name = "FRONTSERVER_NOTES", length = 500, nullable = true)
	
	private String notes;

	public String getFrontServerName() {
		return frontServerName;
	}

	public void setFrontServerName(String frontServerName) {
		this.frontServerName = frontServerName;
	}

	public String getFrontServerIp() {
		return frontServerIp;
	}

	public void setFrontServerIp(String frontServerIp) {
		this.frontServerIp = frontServerIp;
	}

	public Integer getFrontServerPort() {
		return frontServerPort;
	}

	public void setFrontServerPort(Integer frontServerPort) {
		this.frontServerPort = frontServerPort;
	}

	public String getFrontServerUrl() {
		return frontServerUrl;
	}

	public void setFrontServerUrl(String frontServerUrl) {
		this.frontServerUrl = frontServerUrl;
	}

	public Integer getFrontServerStatus() {
		return frontServerStatus;
	}

	public void setFrontServerStatus(Integer frontServerStatus) {
		this.frontServerStatus = frontServerStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}



	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}