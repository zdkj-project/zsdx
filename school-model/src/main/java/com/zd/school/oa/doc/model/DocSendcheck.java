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
 * ClassName: DocSendcheck 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文发文核稿人实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "DOC_T_SENDCHECK")
@AttributeOverride(name = "uuid", column = @Column(name = "SENDCHECK_ID", length = 36, nullable = false))
public class DocSendcheck extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "主键")
    @Column(name = "USER_ID", length = 36, nullable = true)
    private String userId;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
        
    @FieldInfo(name = "发文ID")
    @Column(name = "SEND_ID", length = 36, nullable = true)
    private String sendId;
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
    public String getSendId() {
        return sendId;
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
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "发文标题")
    @Formula("( select a.SEND_TITLE from  DOC_T_SENDDOC a where a.send_id=send_id)")
    private String sendTitle;

    public void setSendTitle(String sendTitle) {
        this.sendTitle = sendTitle;
    }

    public String getSendTitle() {
        return sendTitle;
    }

    @FieldInfo(name = "发文字号")
    @Transient
    private String sendshopname;

    public void setSendshopname(String sendshopname) {
        this.sendshopname = sendshopname;
    }

    public String getSendshopname() {
        return sendshopname;
    }

    @FieldInfo(name = "紧急程度")
    @Transient
    private String emergency;

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getEmergency() {
        return emergency;
    }

    @FieldInfo(name = "密级")
    @Transient
    private String classification;

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    @FieldInfo(name = "关键词")
    @Transient
    private String subject;

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @FieldInfo(name = "启动流程ID")
    @Transient
    private String processinsid;

    public void setProcessinsid(String processinsid) {
        this.processinsid = processinsid;
    }

    public String getProcessinsid() {
        return processinsid;
    }

    @FieldInfo(name = "发文状态;0-拟稿中 1-核稿中 2-盖章中 3-已发文")
    @Transient
    private String docsendState;

    public void setDocsendState(String docsendState) {
        this.docsendState = docsendState;
    }

    public String getDocsendState() {
        return docsendState;
    }

    @FieldInfo(name = "文种")
    @Transient
    private String wenzhong;

    public String getWenzhong() {
        return wenzhong;
    }

    public void setWenzhong(String wenzhong) {
        this.wenzhong = wenzhong;
    }

    @FieldInfo(name = "发文日期")
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date sendDate;

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    @Transient
    @FieldInfo(name = "类型名称")
    private String doctypeName;

    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }

    public String getDoctypeName() {
        return doctypeName;
    }

    @FieldInfo(name = "核稿人姓名")
    @Formula("( select a.XM from  SYS_T_USER a where a.USER_ID=USER_ID)")
    private String xm;
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
    @FieldInfo(name = "所有批阅人意见")
    //@Transient
    @Formula("(SELECT dbo.DOC_F_GETDOCSENDCHECKS(SEND_ID))")
    public String allOpininon;

    public String getAllOpininon() {
        return allOpininon;
    }

    public void setAllOpininon(String allOpininon) {
        this.allOpininon = allOpininon;
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