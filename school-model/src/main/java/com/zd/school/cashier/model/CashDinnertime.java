package com.zd.school.cashier.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ClassName: CashDinnertime 
 * Function:
 * Reason:
 * Description: 快餐时间段定义(CASH_T_DINNERTIME)实体类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_DINNERTIME")
@AttributeOverride(name = "uuid", column = @Column(name = "DINNERTIME_ID", length = 36, nullable = false))
public class CashDinnertime extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "快餐类型",explain = "1-早餐 2-午餐 3-晚餐 4-夜宵")
    @Column(name = "MEAL_TYPE", length = 5, nullable = false)
    private Short mealType;
    public void setMealType(Short mealType) {
        this.mealType = mealType;
    }
    public Short getMealType() {
        return mealType;
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
    @Column(name = "END_TIME", length = 23, nullable = false)
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