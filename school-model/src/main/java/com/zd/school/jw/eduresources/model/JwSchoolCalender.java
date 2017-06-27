package com.zd.school.jw.eduresources.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

@Entity
@Table(name = "JW_T_SCHOOLCALENDER")
@AttributeOverride(name = "uuid", column = @Column(name = "SCHOOLCANDER_ID", length = 36, nullable = false))
public class JwSchoolCalender extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "等同UUID")
	@Column(name = "ID", length = 36, nullable = true)
	private String id;

	@FieldInfo(name = "标题")
	@Column(name = "TITLE", length = 200, nullable = true)
	private String title;

	@FieldInfo(name = "开始时间")
	@Column(name = "START_DATE", length = 36, nullable = true)
	private String start;

	@FieldInfo(name = "结束时间")
	@Column(name = "END_DATE", length = 36, nullable = true)
	private String end;

	@FieldInfo(name = "位置")
	@Column(name = "LOC", length = 200, nullable = true)
	private String loc;

	@FieldInfo(name = "WEB链接")
	@Column(name = "URL", length = 1000, nullable = true)
	private String url;

	@FieldInfo(name = "便签")
	@Column(name = "NOTES", length = 1000, nullable = true)
	private String notes;

	@FieldInfo(name = "提醒器")
	@Column(name = "REM", length = 36, nullable = true)
	private String rem;

	@FieldInfo(name = "是否全天")
	@Column(name = "AD", length = 36, nullable = true)
	private String ad;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}




}
