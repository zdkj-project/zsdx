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
 * ClassName: CashDishes 
 * Function:
 * Reason:
 * Description: 菜品管理(CASH_T_DISHES)实体类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_DISHES")
@AttributeOverride(name = "uuid", column = @Column(name = "DISHES_ID", length = 36, nullable = false))
public class CashDishes extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "菜品类别",explain = "1- 荦菜类 2-素菜类 3-汤类 4-主食类 5-酒水类")
    @Column(name = "DISHES_TYPE", length = 5, nullable = false)
    private Short dishesType;
    public void setDishesType(Short dishesType) {
        this.dishesType = dishesType;
    }
    public Short getDishesType() {
        return dishesType;
    }
        
    @FieldInfo(name = "菜品名称")
    @Column(name = "DISHES_NAME", length = 36, nullable = false)
    private String dishesName;
    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }
    public String getDishesName() {
        return dishesName;
    }
        
    @FieldInfo(name = "菜品编号")
    @Column(name = "DISHES_CODE", length = 5, nullable = false)
    private Short dishesCode;
    public void setDishesCode(Short dishesCode) {
        this.dishesCode = dishesCode;
    }
    public Short getDishesCode() {
        return dishesCode;
    }
        
    @FieldInfo(name = "菜品单价")
    @Column(name = "DISHES_PRICE", length = 19, nullable = false)
    private BigDecimal dishesPrice;
    public void setDishesPrice(BigDecimal dishesPrice) {
        this.dishesPrice = dishesPrice;
    }
    public BigDecimal getDishesPrice() {
        return dishesPrice;
    }
        
    @FieldInfo(name = "菜品说明")
    @Column(name = "DISHES_EXPLAIN", length = 128, nullable = true)
    private String dishesExplain;
    public void setDishesExplain(String dishesExplain) {
        this.dishesExplain = dishesExplain;
    }
    public String getDishesExplain() {
        return dishesExplain;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}