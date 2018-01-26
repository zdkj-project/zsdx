package com.zd.school.jw.train.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

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
    
    @FieldInfo(name = "实际早餐餐标")
    @Column(name = "BREAKFAST_STAND_REAL", precision = 8, scale = 2, nullable = true)
    private BigDecimal breakfastStandReal;
    public void setBreakfastStandReal(BigDecimal breakfastStandReal) {
        this.breakfastStandReal = breakfastStandReal;
    }
    public BigDecimal getBreakfastStandReal() {
        return breakfastStandReal;
    }
    
    @FieldInfo(name = "早餐餐标")
    @Column(name = "BREAKFAST_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal breakfastStand;
    public void setBreakfastStand(BigDecimal breakfastStand) {
        this.breakfastStand = breakfastStand;
    }
    public BigDecimal getBreakfastStand() {
        return breakfastStand;
    }
        
    @FieldInfo(name = "实际午餐餐标")
    @Column(name = "LUNCH_STAND_REAL", precision = 8, scale = 2, nullable = true)
    private BigDecimal lunchStandReal;
    public void setLunchStandReal(BigDecimal lunchStandReal) {
        this.lunchStandReal = lunchStandReal;
    }
    public BigDecimal getLunchStandReal() {
        return lunchStandReal;
    }
    
    @FieldInfo(name = "午餐餐标")
    @Column(name = "LUNCH_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal lunchStand;
    public void setLunchStand(BigDecimal lunchStand) {
        this.lunchStand = lunchStand;
    }
    public BigDecimal getLunchStand() {
        return lunchStand;
    }
        
    @FieldInfo(name = "实际晚餐餐标")
    @Column(name = "DINNER_STAND_REAL", precision = 8, scale = 2, nullable = true)
    private BigDecimal dinnerStandReal;
    public void setDinnerStandReal(BigDecimal dinnerStandReal) {
        this.dinnerStandReal = dinnerStandReal;
    }
    public BigDecimal getDinnerStandReal() {
        return dinnerStandReal;
    }
    
    @FieldInfo(name = "晚餐餐标")
    @Column(name = "DINNER_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal dinnerStand;
    public void setDinnerStand(BigDecimal dinnerStand) {
        this.dinnerStand = dinnerStand;
    }
    public BigDecimal getDinnerStand() {
        return dinnerStand;
    }
    
    @FieldInfo(name = "加菜金额")
    @Column(name = "ADDDINNER_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal addDinnerStand;
    public void setAddDinnerStand(BigDecimal addDinnerStand) {
        this.addDinnerStand = addDinnerStand;
    }
    public BigDecimal getAddDinnerStand() {
        return addDinnerStand;
    }
        
    @FieldInfo(name = "纸巾金额")
    @Column(name = "TISSUE_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal tissueStand;
    public void setTissueStand(BigDecimal tissueStand) {
        this.tissueStand = tissueStand;
    }
    public BigDecimal getTissueStand() {
        return tissueStand;
    }
    
    @FieldInfo(name = "其他金额")
    @Column(name = "OTHER_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal otherStand;
    public void setOtherStand(BigDecimal otherStand) {
        this.otherStand = otherStand;
    }
    public BigDecimal getOtherStand() {
        return otherStand;
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
    
    @FieldInfo(name = "计划金额")
    @Formula("(SELECT (a.BREKFAST_STAND*a.BREAKFAST_COUNT+a.LUNCH_STAND*a.LUNCH_COUNT+a.DINNER_STAND*a.DINNER_COUNT)  FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private BigDecimal countMoneyPlan;

    public void setCountMoneyPlan(BigDecimal countMoneyPlan) {
        this.countMoneyPlan = countMoneyPlan;
    }

    public BigDecimal getCountMoneyPlan() {
        return countMoneyPlan;
    }
       
    //SELECT (a.BREKFAST_STAND*b.BREAKFAST_REAL+a.LUNCH_STAND*b.LUNCH_REAL+a.DINNER_STAND*b.DINNER_REAL) FROM dbo.TRAIN_T_CLASS a join TRAIN_T_CLASSREALDINNER b on a.CLASS_ID=b.CLASS_ID where b.CLASSEAT_ID=CLASSEAT_ID
    @FieldInfo(name = "实际金额")
    @Formula("(SELECT (a.BREKFAST_STAND*BREAKFAST_REAL+a.LUNCH_STAND*LUNCH_REAL+a.DINNER_STAND*DINNER_REAL)  FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private BigDecimal countMoneyReal;

    public void setCountMoneyReal(BigDecimal countMoneyReal) {
        this.countMoneyReal = countMoneyReal;
    }

    public BigDecimal getCountMoneyReal() {
        return countMoneyReal;
    }
    
    @FieldInfo(name = "用餐类型")
    @Formula("(SELECT a.DINNER_TYPE FROM dbo.TRAIN_T_CLASS a WHERE a.CLASS_ID=CLASS_ID)")
    private Integer dinnerType;
	public Integer getDinnerType() {
		return dinnerType;
	}
	public void setDinnerType(Integer dinnerType) {
		this.dinnerType = dinnerType;
	}

    
    
    
}