package com.zd.school.oa.doc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: DocSenddoc 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文发文单实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "DOC_T_SENDDOC")
@AttributeOverride(name = "uuid", column = @Column(name = "SEND_ID", length = 36, nullable = false))
public class DocSenddoc extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "类型ID")
    @Column(name = "DOCTYPE_ID", length = 36, nullable = true)
    private String doctypeId;
    public void setDoctypeId(String doctypeId) {
        this.doctypeId = doctypeId;
    }
    public String getDoctypeId() {
        return doctypeId;
    }
    
    @FieldInfo(name = "类型名称")
    @Column(name = "DOCTYPE_NAME", length = 36,nullable = true)
    private String doctypeName;

    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }

    public String getDoctypeName() {
        return doctypeName;
    }
        
    @FieldInfo(name = "发文标题")
    @Column(name = "SEND_TITLE", length = 128, nullable = false)
    private String sendTitle;
    public void setSendTitle(String sendTitle) {
        this.sendTitle = sendTitle;
    }
    public String getSendTitle() {
        return sendTitle;
    }
        
    @FieldInfo(name = "发文字号")
    @Column(name = "SENDSHOPNAME", length = 36, nullable = true)
    private String sendshopname;
    public void setSendshopname(String sendshopname) {
        this.sendshopname = sendshopname;
    }
    public String getSendshopname() {
        return sendshopname;
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
        
    @FieldInfo(name = "主题词")
    @Column(name = "SUBJECT", length = 128, nullable = false)
    private String subject;
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }
        
    @FieldInfo(name = "启动流程ID")
    @Column(name = "PROCESSINSID", length = 36, nullable = true)
    private String processinsid;
    public void setProcessinsid(String processinsid) {
        this.processinsid = processinsid;
    }
    public String getProcessinsid() {
        return processinsid;
    }
        
    @FieldInfo(name = "发文状态;0-拟稿中 1-核稿中 2-盖章中 3-已发文")
    @Column(name = "DOCSEND_STATE", length = 10, nullable = true)
    private String docsendState;
    public void setDocsendState(String docsendState) {
        this.docsendState = docsendState;
    }
    public String getDocsendState() {
        return docsendState;
    }
        
    @FieldInfo(name = "发文日期")
    @Column(name = "SEND_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date sendDate;
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getSendDate() {
        return sendDate;
    }
        
    @FieldInfo(name = "核稿人ID")
    @Column(name = "DISTRIBID", length = 2048, nullable = true)
    private String distribId;

    public String getDistribId() {
        return distribId;
    }

    public void setDistribId(String distribId) {
        this.distribId = distribId;
    }
    
    @FieldInfo(name = "文种")
    @Column(name = "WENZHONG", length = 128, nullable = false)
    private String wenzhong;

    public String getWenzhong() {
        return wenzhong;
    }

    public void setWenzhong(String wenzhong) {
        this.wenzhong = wenzhong;
    }
    
    @FieldInfo(name = "核稿人姓名")
    @Column(name = "SENDCHECK_NAME", length = 1024, nullable = true)
    private String sendCheckName;

    public String getSendCheckName() {
        return sendCheckName;
    }

    public void setSendCheckName(String sendCheckName) {
        this.sendCheckName = sendCheckName;
    }
    
    @FieldInfo(name="拟稿人ID")
    @Column(name = "DRAFTER_ID", length = 36, nullable = true)
    private String drafterId;
    
    public String getDrafterId() {
		return drafterId;
	}
	public void setDrafterId(String drafterId) {
		this.drafterId = drafterId;
	}

	/** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
    
    @FieldInfo(name = "所有核稿人意见")
    @Formula("(SELECT dbo.DOC_F_GETDOCSENDCHECKS(SEND_ID))")
    public String allOpininon;

    public String getAllOpininon() {
        return allOpininon;
    }

    public void setAllOpininon(String allOpininon) {
        this.allOpininon = allOpininon;
    }
    
    @FieldInfo(name = "对应的收文登记情况")
    @Formula("(SELECT dbo.DOC_F_GETDOCSENDREG(SEND_ID))")
    public String sendReg;
	public String getSendReg() {
		return sendReg;
	}
	public void setSendReg(String sendReg) {
		this.sendReg = sendReg;
	} 
    
}