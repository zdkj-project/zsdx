package com.zd.school.oa.doc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: DocReceive Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公文收文单实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "DOC_T_RECEIVE")
@AttributeOverride(name = "uuid", column = @Column(name = "DOCREC_ID", length = 36, nullable = false))
public class DocReceive extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "来文标题")
	@Column(name = "REC_TITLE", length = 128, nullable = false)
	private String recTitle;

	public void setRecTitle(String recTitle) {
		this.recTitle = recTitle;
	}

	public String getRecTitle() {
		return recTitle;
	}

	@FieldInfo(name = "来文日期")
	@Column(name = "REC_DATE", length = 23, nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date recDate;

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public Date getRecDate() {
		return recDate;
	}

	@FieldInfo(name = "来文单位")
	@Column(name = "REC_UNIT", length = 128, nullable = false)
	private String recUnit;

	public void setRecUnit(String recUnit) {
		this.recUnit = recUnit;
	}

	public String getRecUnit() {
		return recUnit;
	}

	@FieldInfo(name = "来文字号")
	@Column(name = "REC_SHOPNAME", length = 32, nullable = false)
	private String recShopname;

	public String getRecShopname() {
		return recShopname;
	}

	public void setRecShopname(String recShopname) {
		this.recShopname = recShopname;
	}

	@FieldInfo(name = "收文编号")
	@Column(name = "REC_NUMB", length = 32, nullable = true)
	private String recNumb;

	public void setRecNumb(String recNumb) {
		this.recNumb = recNumb;
	}

	public String getRecNumb() {
		return recNumb;
	}

	@FieldInfo(name = "紧急程度")
	@Column(name = "EMERGENCY", length = 36, nullable = false)
	private String emergency;

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getEmergency() {
		return emergency;
	}

	@FieldInfo(name = "密级")
	@Column(name = "CLASSIFICATION", length = 36, nullable = true)
	private String classification;

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return classification;
	}

	@FieldInfo(name = "收文状态;0-待登记  1-已登记 2-待批阅 3-已批阅 4-待传阅 5-已传阅")
	@Column(name = "DOCREC_STATE", length = 10, nullable = false)
	private Integer docrecState;

	public void setDocrecState(Integer docrecState) {
		this.docrecState = docrecState;
	}

	public Integer getDocrecState() {
		return docrecState;
	}

	@FieldInfo(name = "类型ID")
	@Column(name = "DOCTYPE_ID", length = 36, nullable = true)
	private String doctypeId;

	public String getDoctypeId() {
		return doctypeId;
	}

	public void setDoctypeId(String doctypeId) {
		this.doctypeId = doctypeId;
	}

	@FieldInfo(name = "分发对象类型,0-领导批阅 1-直接分发传阅 ")
	@Column(name = "DISTRIBUTION_TYPE", length = 2, nullable = true)
	private String distribType;

	public String getDistribType() {
		return distribType;
	}

	public void setDistribType(String distribType) {
		this.distribType = distribType;
	}

	@FieldInfo(name = "传阅人ID")
	@Column(name = "DISTRIBID", length = 2048)
	private String distribId;

	public String getDistribId() {
		return distribId;
	}

	public void setDistribId(String distribId) {
		this.distribId = distribId;
	}

	@FieldInfo(name = "传阅人姓名")
	@Column(name = "DISTRIBNAME", length = 2048)
	private String distribName;

	public String getDistribName() {
		return distribName;
	}

	public void setDistribName(String distribName) {
		this.distribName = distribName;
	}

	@FieldInfo(name = "类型名称")
	@Column(name = "DOCTYPE_NAME", length = 36)
	private String doctypeName;

	public void setDoctypeName(String doctypeName) {
		this.doctypeName = doctypeName;
	}

	public String getDoctypeName() {
		return doctypeName;
	}

	@FieldInfo(name = "关键词")
	@Column(name = "KEYWORD", length = 128, nullable = true)
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@FieldInfo(name = "文种")
	@Column(name = "WENZHONG", length = 128, nullable = true)
	private String wenzhong;

	public String getWenzhong() {
		return wenzhong;
	}

	public void setWenzhong(String wenzhong) {
		this.wenzhong = wenzhong;
	}

	@FieldInfo(name = "收文部门")
	@Column(name = "RECEIVE_DEPT", length = 36, nullable = true)
	private String receiveDept;

	public String getReceiveDept() {
		return receiveDept;
	}

	public void setReceiveDept(String receiveDept) {
		this.receiveDept = receiveDept;
	}

	@FieldInfo(name = "来文类型 0-外部来文 1-内部收文")
	@Column(name = "RECEIVE_SOURCE", length = 4, nullable = true)
	private String receiveSource;

	public String getReceiveSource() {
		return receiveSource;
	}

	public void setReceiveSource(String receiveSource) {
		this.receiveSource = receiveSource;
	}

	@FieldInfo(name = "发文ID,针对接收的是内部发文")
	@Column(name = "SEND_ID", length = 36, nullable = true)
	private String sendId;

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	@FieldInfo(name = "登记人ID")
	@Column(name = "REGUSER_ID", length = 36, nullable = true)
	private String regUserId;
	
	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@FieldInfo(name = "所有批阅人意见")
	@Formula("(SELECT dbo.DOC_F_GETDOCRECEXAMINES(DOCREC_ID))")
	public String allOpininon;

	public String getAllOpininon() {
		return allOpininon;
	}

	public void setAllOpininon(String allOpininon) {
		this.allOpininon = allOpininon;
	}

	@FieldInfo(name = "收文部门名称")
	@Formula("(SELECT a.NODE_TEXT FROM BASE_T_ORG a WHERE a.DEPT_ID=RECEIVE_DEPT )")
	private String receiveDeptName;

	public String getReceiveDeptName() {
		return receiveDeptName;
	}

	public void setReceiveDeptName(String receiveDeptName) {
		this.receiveDeptName = receiveDeptName;
	}

	@FieldInfo(name = "公文状态")
	@Formula("(SELECT (CASE a.DOCREC_STATE WHEN '0' THEN '待登记' WHEN '1' THEN '已登记' WHEN '2' THEN '待批阅' WHEN '3' THEN '已批阅' WHEN '4' THEN '待传阅' WHEN '5' THEN '已传阅' END )  FROM dbo.DOC_T_RECEIVE a WHERE a.DOCREC_ID=DOCREC_ID)")
	private String stateName;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

    @FieldInfo(name = "收文阅读情况")
    @Formula("(SELECT dbo.DOC_F_GETDOCRECREAD(DOCREC_ID))")
    public String receiveRead;

	public String getReceiveRead() {
		return receiveRead;
	}

	public void setReceiveRead(String receiveRead) {
		this.receiveRead = receiveRead;
	}
	
	@FieldInfo(name = "进度状态;0-未完成  1-已完成 ")
	@Column(name = "PROGRESS", length = 10, nullable = false)
	private Integer progress=0;

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}


}