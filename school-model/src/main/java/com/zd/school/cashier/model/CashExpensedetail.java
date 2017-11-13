package com.zd.school.cashier.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: CashExpensedetail 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 流水明细(CASH_T_EXPENSEDETAIL)实体类.
 * date: 2017-09-25
 * 
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_EXPENSEDETAIL")
@AttributeOverride(name = "uuid", column = @Column(name = "BILLITEM_ID", length = 36, nullable = false))
public class CashExpensedetail extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "流水ID")
    @Column(name = "EXPENSESERIAL_ID", length = 36, nullable = true)
    private String expenseserialId;
    public void setExpenseserialId(String expenseserialId) {
        this.expenseserialId = expenseserialId;
    }
    public String getExpenseserialId() {
        return expenseserialId;
    }
        
    @FieldInfo(name = "明细项")
    @Column(name = "DETAIL_NAME", length = 36, nullable = false)
    private String detailName;
    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }
    public String getDetailName() {
        return detailName;
    }
        
    @FieldInfo(name = "数量")
    @Column(name = "DETAIL_COUNT", length = 5, nullable = false)
    private Short detailCount;
    public void setDetailCount(Short detailCount) {
        this.detailCount = detailCount;
    }
    public Short getDetailCount() {
        return detailCount;
    }
        
    @FieldInfo(name = "单价")
    @Column(name = "DETAIL_PRICE", length = 19, nullable = false)
    private BigDecimal detailPrice;

    public BigDecimal getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(BigDecimal detailPrice) {
        this.detailPrice = detailPrice;
    }
/** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}