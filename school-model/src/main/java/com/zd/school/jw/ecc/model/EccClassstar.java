package com.zd.school.jw.ecc.model;

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
 * ClassName: EccClassstar 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级评星信息(ECC_T_CLASSSTAR)实体类.
 * date: 2016-12-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ECC_T_CLASSSTAR")
@AttributeOverride(name = "uuid", column = @Column(name = "STAR_ID", length = 36, nullable = false))
public class EccClassstar extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "班级ID")
    @Column(name = "CLAI_ID", length = 36, nullable = true)
    private String claiId;
    public void setClaiId(String claiId) {
        this.claiId = claiId;
    }
    public String getClaiId() {
        return claiId;
    }
        
    @FieldInfo(name = "班级名称")
    @Column(name = "CLASS_NAME", length = 36, nullable = true)
    private String className;
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }
        
    @FieldInfo(name = "星级")
    @Column(name = "STAR_LEVEL", length = 4, nullable = false)
    private String starLevel;
    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }
    public String getStarLevel() {
        return starLevel;
    }
        
    @FieldInfo(name = "评定日期")
    @Column(name = "DO_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date doDate;
    public void setDoDate(Date doDate) {
        this.doDate = doDate;
    }
    public Date getDoDate() {
        return doDate;
    }
        
    @FieldInfo(name = "开始日期")
    @Column(name = "BEGIN_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date beginDate;
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getBeginDate() {
        return beginDate;
    }
        
    @FieldInfo(name = "结束日期")
    @Column(name = "END_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date endDate;
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getEndDate() {
        return endDate;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}