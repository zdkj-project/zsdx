package com.zd.school.jw.eduresources.model;

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
 * ClassName: JwCalenderdetail 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历节次信息表(JW_T_CALENDERDETAIL)实体类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_CALENDERDETAIL")
@AttributeOverride(name = "uuid", column = @Column(name = "JC_ID", length = 36, nullable = false))
public class JwCalenderdetail extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "校历ID")
    @Column(name = "CANDER_ID", length = 36, nullable = true)
    private String canderId;
    public void setCanderId(String canderId) {
        this.canderId = canderId;
    }
    public String getCanderId() {
        return canderId;
    }
        
    @FieldInfo(name = "节次名称")
    @Column(name = "JC_NAME", length = 36, nullable = false)
    private String jcName;
    public void setJcName(String jcName) {
        this.jcName = jcName;
    }
    public String getJcName() {
        return jcName;
    }
        
    @FieldInfo(name = "上/下午标识,0-上午 1-下午")
    @Column(name = "ISAFGERNOON", length = 10, nullable = false)
    private Integer isafgernoon;
    public void setIsafgernoon(Integer isafgernoon) {
        this.isafgernoon = isafgernoon;
    }
    public Integer getIsafgernoon() {
        return isafgernoon;
    }
        
    @FieldInfo(name = "jcCode")
    @Column(name = "JC_CODE", length = 10, nullable = true)
    private String jcCode;
    public void setJcCode(String jcCode) {
        this.jcCode = jcCode;
    }
    public String getJcCode() {
        return jcCode;
    }
        
    @FieldInfo(name = "开始时间")
    @Column(name = "BEGIN_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date beginTime;
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getBeginTime() {
        return beginTime;
    }
        
    @FieldInfo(name = "结束时间")
    @Column(name = "END_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date endTime;
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return endTime;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}