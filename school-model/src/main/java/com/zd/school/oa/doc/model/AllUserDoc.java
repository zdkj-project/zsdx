package com.zd.school.oa.doc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.logging.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.util.DateTimeSerializer;

/**
 * 用户所有待处理公文集合的实体类
 * 
 * @author luoyibo
 *
 */
public class AllUserDoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "待处理记录ID")
	private String recordID;
	
	@FieldInfo(name = "收发文的ID")
	private String docId;
	
	@FieldInfo(name = "收发文的标题")
	private String docTitle;
	
	@FieldInfo(name = "处理意见")
	private String doResult;
	
	@FieldInfo(name = "处理状态")
	private String doState;
	
	@FieldInfo(name = "待处理公文的创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)	
	private Date createTime;
	
	@FieldInfo(name = "发文的编号/收文的编号")
	private String docNumb;
	
	@FieldInfo(name = "公文的紧急程度")
	private String docEmg;
	
	@FieldInfo(name = "要处理的类型")
	private String operType;

	@FieldInfo(name = "当前用户ID")
	private String userId;
	
	@FieldInfo(name="所有人员的意见")
	private String allOpininon;
	
	@FieldInfo(name="密级")
	private String classification;

	@FieldInfo(name="文种")
	private String wenzhong;
	
	@FieldInfo(name="关键词")
	private String keyword;

	@FieldInfo(name="创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)		
	private Date createDate;
	
	@FieldInfo(name="公文类型")
	private String doctypeName;
	
	@FieldInfo(name = "进度状态;0-未完成  1-已完成 ")
	private Integer progress;

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	
	public String getDoctypeName() {
		return doctypeName;
	}

	public void setDoctypeName(String doctypeName) {
		this.doctypeName = doctypeName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getWenzhong() {
		return wenzhong;
	}

	public void setWenzhong(String wenzhong) {
		this.wenzhong = wenzhong;
	}

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDoResult() {
		return doResult;
	}

	public void setDoResult(String doResult) {
		this.doResult = doResult;
	}

	public String getDoState() {
		return doState;
	}

	public void setDoState(String doState) {
		this.doState = doState;
	}


	public String getDocNumb() {
		return docNumb;
	}

	public void setDocNumb(String docNumb) {
		this.docNumb = docNumb;
	}

	public String getDocEmg() {
		return docEmg;
	}

	public void setDocEmg(String docEmg) {
		this.docEmg = docEmg;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAllOpininon() {
		return allOpininon;
	}

	public void setAllOpininon(String allOpininon) {
		this.allOpininon = allOpininon;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

}
