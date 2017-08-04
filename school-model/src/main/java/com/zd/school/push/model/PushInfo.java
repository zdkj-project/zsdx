package com.zd.school.push.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

@Entity
@Table(name = "PUSH_T_PUSHINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "PUSHINFO_ID", length = 36, nullable = false) )
public class PushInfo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// @Column(name = "PUSH_TYPE", length = 128, nullable = false)
	// private String pushType; //推送的类型

	@Column(name = "EVENT_TYPE", length = 128, nullable = false)
	private String eventType;

	@Column(name = "REG_STATUS", length = 1024, nullable = false)
	private String regStatus;

	@Column(name = "PUSH_WAY", nullable = false)
	private Integer pushWay; // 1:微信 2:APP 3:短信

	@Column(name = "REG_TIME", columnDefinition = "datetime", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date regTime;

	@Column(name = "EMPL_NO", length = 20, nullable = false)
	private String emplNo;

	@Column(name = "EMPL_NAME", length = 20, nullable = false)
	private String emplName;

	@Column(name = "PUSH_STATUS", nullable = false)
	private Integer pushStatus; // 0未发送 1发送成功 -1发送失败

	@Column(name = "PUSH_TIME", nullable = true)
	private Date pushTime;

	@Column(name = "PUSH_URL", length = 512, nullable = true)
	private String pushUrl;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}

	public Integer getPushWay() {
		return pushWay;
	}

	public void setPushWay(Integer pushWay) {
		this.pushWay = pushWay;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getEmplNo() {
		return emplNo;
	}

	public void setEmplNo(String emplNo) {
		this.emplNo = emplNo;
	}

	public String getEmplName() {
		return emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

}
