package com.zd.school.jw.train.model;

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
 * ClassName: TrainClassrealdinner 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级就餐登记(TRAIN_T_CLASSREALDINNER)实体类.
 * date: 2017-06-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_CLASSREALDINNER")
@AttributeOverride(name = "uuid", column = @Column(name = "CLASSEAT_ID", length = 36, nullable = false))
public class TrainClassrealdinner extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "班级ID")
    @Column(name = "CLASS_ID", length = 36, nullable = true)
    private String classId;
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getClassId() {
        return classId;
    }
        
    @FieldInfo(name = "就餐日期")
    @Column(name = "DINNER_DATE", length = 23, nullable = false)
    private Date dinnerDate;
    public void setDinnerDate(Date dinnerDate) {
        this.dinnerDate = dinnerDate;
    }
    public Date getDinnerDate() {
        return dinnerDate;
    }
        
    @FieldInfo(name = "早餐围/人数")
    @Column(name = "BREAKFAST_REAL", length = 10, nullable = false)
    private Integer breakfastReal;
    public void setBreakfastReal(Integer breakfastReal) {
        this.breakfastReal = breakfastReal;
    }
    public Integer getBreakfastReal() {
        return breakfastReal;
    }
        
    @FieldInfo(name = "午餐围/人数")
    @Column(name = "LUNCH_REAL", length = 10, nullable = false)
    private Integer lunchReal;
    public void setLunchReal(Integer lunchReal) {
        this.lunchReal = lunchReal;
    }
    public Integer getLunchReal() {
        return lunchReal;
    }
        
    @FieldInfo(name = "晚餐围/人数")
    @Column(name = "DINNER_REAL", length = 10, nullable = false)
    private Integer dinnerReal;
    public void setDinnerReal(Integer dinnerReal) {
        this.dinnerReal = dinnerReal;
    }
    public Integer getDinnerReal() {
        return dinnerReal;
    }
        
    @FieldInfo(name = "备注")
    @Column(name = "REMARK", length = 128, nullable = true)
    private String remark;
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }
        
    
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
     *@Transient
     *@FieldInfo(name = "")
     *private String field1;
     */
    @FieldInfo(name = "班级名称")
    @Formula("(SELECT a.CLASS_NAME FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private String className;
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }
    
    @FieldInfo(name = "早餐围/人数")
    @Formula("(SELECT a.BREAKFAST_COUNT FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private Integer breakfastCount;
    public void setBreakfastCount(Integer breakfastCount) {
        this.breakfastCount = breakfastCount;
    }
    public Integer getBreakfastCount() {
        return breakfastCount;
    }
    
    @FieldInfo(name = "午餐围/人数")
    @Formula("(SELECT a.LUNCH_COUNT FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private Integer lunchCount;
    public void setLunchCount(Integer lunchCount) {
        this.lunchCount = lunchCount;
    }
    public Integer getLunchCount() {
        return lunchCount;
    }
    
    @FieldInfo(name = "晚餐围/人数")
    @Formula("(SELECT a.DINNER_COUNT FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private Integer dinnerCount;
    public void setDinnerCount(Integer dinnerCount) {
        this.dinnerCount = dinnerCount;
    }
    public Integer getDinnerCount() {
        return dinnerCount;
    }
    
    @FieldInfo(name = "联系人")
    @Formula("(SELECT a.XM FROM dbo.SYS_T_USER a WHERE a.USER_ID=CREATE_USER)")
    private String contactPerson;

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    @FieldInfo(name = "联系电话")
    @Formula("(SELECT isnull(a.MOBILE,'未设置电话号码') FROM dbo.SYS_T_USER a WHERE a.USER_ID=CREATE_USER)")
    private String contactPhone;

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}