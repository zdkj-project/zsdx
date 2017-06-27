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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: TrainClassorder 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级订餐/住宿信息(TRAIN_T_CLASSORDER)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_CLASSORDER")
@AttributeOverride(name = "uuid", column = @Column(name = "CLASS_ORDER_ID", length = 36, nullable = false))
public class TrainClassorder extends BaseEntity implements Serializable{
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
        
    @FieldInfo(name = "用餐类别,早餐、午餐、晚餐组合选择")
    @Column(name = "DINNER_GROUP", length = 5, nullable = false)
    private Short dinnerGroup;
    public void setDinnerGroup(Short dinnerGroup) {
        this.dinnerGroup = dinnerGroup;
    }
    public Short getDinnerGroup() {
        return dinnerGroup;
    }
        
    @FieldInfo(name = "餐类,DINNERTYPE数据字典")
    @Column(name = "DINNER_TYPE", length = 16, nullable = false)
    private String dinnerType;
    public void setDinnerType(String dinnerType) {
        this.dinnerType = dinnerType;
    }
    public String getDinnerType() {
        return dinnerType;
    }
        
    @FieldInfo(name = "餐标")
    @Column(name = "DINNER_STANDARD", length = 8, nullable = false)
    private BigDecimal dinnerStandard;
    public void setDinnerStandard(BigDecimal dinnerStandard) {
        this.dinnerStandard = dinnerStandard;
    }
    public BigDecimal getDinnerStandard() {
        return dinnerStandard;
    }
        
    @FieldInfo(name = "用餐人数")
    @Column(name = "DINNER_COUNT", length = 5, nullable = false)
    private Short dinnerCount;
    public void setDinnerCount(Short dinnerCount) {
        this.dinnerCount = dinnerCount;
    }
    public Short getDinnerCount() {
        return dinnerCount;
    }
        
    @FieldInfo(name = "住宿人数")
    @Column(name = "STAY_COUNT", length = 5, nullable = false)
    private Short stayCount;
    public void setStayCount(Short stayCount) {
        this.stayCount = stayCount;
    }
    public Short getStayCount() {
        return stayCount;
    }
        
    @FieldInfo(name = "其它说明")
    @Column(name = "ORDER_DESC", length = 128, nullable = true)
    private String orderDesc;
    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }
    public String getOrderDesc() {
        return orderDesc;
    }
        
    @FieldInfo(name = "就餐日期")
    @Column(name = "DINNER_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date dinnerDate;
    public void setDinnerDate(Date dinnerDate) {
        this.dinnerDate = dinnerDate;
    }
    public Date getDinnerDate() {
        return dinnerDate;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}