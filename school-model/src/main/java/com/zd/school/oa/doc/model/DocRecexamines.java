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
 * ClassName: DocRecexamines Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公文收文批阅人实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "DOC_T_RECEXAMINES")
@AttributeOverride(name = "uuid", column = @Column(name = "RECEXAM_ID", length = 36, nullable = false))
public class DocRecexamines extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "收文ID")
    @Column(name = "DOCREC_ID", length = 36, nullable = true)
    private String docrecId;

    public void setDocrecId(String docrecId) {
        this.docrecId = docrecId;
    }

    public String getDocrecId() {
        return docrecId;
    }

    @FieldInfo(name = "主键")
    @Column(name = "USER_ID", length = 36, nullable = true)
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @FieldInfo(name = "状态")
    @Column(name = "STATE", length = 4, nullable = true)
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @FieldInfo(name = "意见")
    @Column(name = "OPININON", length = 255, nullable = true)
    private String opininon;

    public void setOpininon(String opininon) {
        this.opininon = opininon;
    }

    public String getOpininon() {
        return opininon;
    }

    @FieldInfo(name = "批阅类型,0-领导批阅 1-分发传阅")
    @Column(name = "RECEXAM_TYPE", length = 4, nullable = true)
    private String recexamType;

    public String getRecexamType() {
        return recexamType;
    }

    public void setRecexamType(String recexamType) {
        this.recexamType = recexamType;
    }

    public DocRecexamines() {

        super();
        // TODO Auto-generated constructor stub

    }

    public DocRecexamines(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */

    @FieldInfo(name = "来文标题")
    @Formula("(SELECT a.REC_TITLE from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String recTitle;

    public void setRecTitle(String recTitle) {
        this.recTitle = recTitle;
    }

    public String getRecTitle() {
        return recTitle;
    }

    @FieldInfo(name = "来文单位")
    @Formula("(SELECT a.REC_UNIT from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String recUnit;

    public void setRecUnit(String recUnit) {
        this.recUnit = recUnit;
    }

    public String getRecUnit() {
        return recUnit;
    }

    @FieldInfo(name = "来文字号")
    @Formula("(SELECT a.REC_SHOPNAME from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String recShopname;

    public String getRecShopname() {
        return recShopname;
    }

    public void setRecShopname(String recShopname) {
        this.recShopname = recShopname;
    }

    @FieldInfo(name = "收文编号")
    @Formula("(SELECT a.REC_NUMB from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String recNumb;

    public void setRecNumb(String recNumb) {
        this.recNumb = recNumb;
    }

    public String getRecNumb() {
        return recNumb;
    }

    @FieldInfo(name = "紧急程度")
    @Formula("(SELECT a.EMERGENCY from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String emergency;

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getEmergency() {
        return emergency;
    }

    @FieldInfo(name = "密级")
    @Formula("(SELECT a.CLASSIFICATION from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String classification;

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    @FieldInfo(name = "收文状态;0-待登记  1-已登记 2-待批阅 3-已批阅 4-待传阅 5-已传阅")
    @Formula("(SELECT a.DOCREC_STATE from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private Integer docrecState;

    public void setDocrecState(Integer docrecState) {
        this.docrecState = docrecState;
    }

    public Integer getDocrecState() {
        return docrecState;
    }

    @FieldInfo(name = "关键词")
    @Formula("(SELECT a.KEYWORD from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @FieldInfo(name = "来文日期")
    @Formula("(SELECT a.REC_DATE from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date recDate;

    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    @FieldInfo(name = "文种")
    @Formula("(SELECT a.WENZHONG from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String wenzhong;

    public String getWenzhong() {
        return wenzhong;
    }

    public void setWenzhong(String wenzhong) {
        this.wenzhong = wenzhong;
    }

    @FieldInfo(name = "类型名称")
    @Formula("(SELECT a.DOCTYPE_NAME from DOC_T_RECEIVE a where a.DOCREC_ID=DOCREC_ID)")
    private String doctypeName;

    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }

    public String getDoctypeName() {
        return doctypeName;
    }

    @FieldInfo(name = "所有批阅人意见")
    //@Transient
    @Formula("(SELECT dbo.DOC_F_GETDOCRECEXAMINES(DOCREC_ID))")
    public String allOpininon;

    public String getAllOpininon() {
        return allOpininon;
    }

    public void setAllOpininon(String allOpininon) {
        this.allOpininon = allOpininon;
    }

    @FieldInfo(name = "批阅人姓名")
    @Formula("(SELECT a.XM from SYS_T_USER a where a.USER_ID=USER_ID)")
    private String xm;

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
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
	@FieldInfo(name = "公文状态")
	@Formula("(SELECT (CASE a.DOCREC_STATE WHEN '0' THEN '待登记' WHEN '1' THEN '已登记' WHEN '2' THEN '待批阅' WHEN '3' THEN '已批阅' WHEN '4' THEN '待传阅' WHEN '5' THEN '已传阅' END )  FROM dbo.DOC_T_RECEIVE a WHERE a.DOCREC_ID=DOCREC_ID)")
	private String stateName;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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