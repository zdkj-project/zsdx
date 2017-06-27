package com.zd.school.control.device.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: ContPerimisson 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_PERIMISSON实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CONT_T_PERIMISSON")
@AttributeOverride(name = "uuid", column = @Column(name = "STURIGHT_ID", length = 36, nullable = false))
public class ContPerimisson extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "isdownload")
    @Column(name = "ISDOWNLOAD", length = 1, nullable = true)
    private Boolean isdownload;
    public void setIsdownload(Boolean isdownload) {
        this.isdownload = isdownload;
    }
    public Boolean getIsdownload() {
        return isdownload;
    }
        
    @FieldInfo(name = "cardId")
    @Column(name = "CARD_ID", length = 36, nullable = true)
    private String cardId;
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getCardId() {
        return cardId;
    }
        
    @FieldInfo(name = "cardserNo")
    @Column(name = "CARDSER_NO", length = 36, nullable = true)
    private String cardserNo;
    public void setCardserNo(String cardserNo) {
        this.cardserNo = cardserNo;
    }
    public String getCardserNo() {
        return cardserNo;
    }
        
    @FieldInfo(name = "cardstatusId")
    @Column(name = "CARDSTATUS_ID", length = 10, nullable = false)
    private Integer cardstatusId;
    public void setCardstatusId(Integer cardstatusId) {
        this.cardstatusId = cardstatusId;
    }
    public Integer getCardstatusId() {
        return cardstatusId;
    }
        
    @FieldInfo(name = "controlsegId")
    @Column(name = "CONTROLSEG_ID", length = 36, nullable = false)
    private String controlsegId;
    public void setControlsegId(String controlsegId) {
        this.controlsegId = controlsegId;
    }
    public String getControlsegId() {
        return controlsegId;
    }
        
    @FieldInfo(name = "studentId")
    @Column(name = "STUDENT_ID", length = 36, nullable = true)
    private String studentId;
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }
        
    @FieldInfo(name = "deviceId")
    @Column(name = "DEVICE_ID", length = 36, nullable = true)
    private String deviceId;
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceId() {
        return deviceId;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}