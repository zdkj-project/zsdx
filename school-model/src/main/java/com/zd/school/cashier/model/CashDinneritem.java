package com.zd.school.cashier.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * ClassName: CashDinneritem 
 * Function:
 * Reason:
 * Description: 快餐项定义(CASH_T_DINNERITEM)实体类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_DINNERITEM")
@AttributeOverride(name = "uuid", column = @Column(name = "MEAL_ID", length = 36, nullable = false))
public class CashDinneritem extends BaseEntity implements Serializable{
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
        
    @FieldInfo(name = "餐名")
    @Column(name = "MEAL_NAME", length = 32, nullable = false)
    private String mealName;
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
    public String getMealName() {
        return mealName;
    }
        
    @FieldInfo(name = "餐标")
    @Column(name = "MEAL_PRICE", length = 19, nullable = false)
    private BigDecimal mealPrice;
    public void setMealPrice(BigDecimal mealPrice) {
        this.mealPrice = mealPrice;
    }
    public BigDecimal getMealPrice() {
        return mealPrice;
    }
        
    @FieldInfo(name = "说明")
    @Column(name = "MEAL_EXPLAIN", length = 128, nullable = true)
    private String mealExplain;
    public void setMealExplain(String mealExplain) {
        this.mealExplain = mealExplain;
    }
    public String getMealExplain() {
        return mealExplain;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}