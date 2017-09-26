package com.zd.school.cashier.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * ClassName: CashExpenseserial 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 消费流水(CASH_T_EXPENSESERIAL)实体类.
 * date: 2017-09-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_EXPENSESERIAL")
@AttributeOverride(name = "uuid", column = @Column(name = "EXPENSESERIAL_ID", length = 36, nullable = false))
public class CashExpenseserial extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "流水号")
    @Column(name = "CONSUME_SERIAL", length = 20, nullable = false)
    private String consumeSerial;
    public void setConsumeSerial(String consumeSerial) {
        this.consumeSerial = consumeSerial;
    }
    public String getConsumeSerial() {
        return consumeSerial;
    }
        
    @FieldInfo(name = "消费类型")
    @Column(name = "CONSUME_TYPE", length = 16, nullable = false)
    private String consumeType;
    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }
    public String getConsumeType() {
        return consumeType;
    }
        
    @FieldInfo(name = "消费人")
    @Column(name = "CONSUME_USER", length = 128, nullable = false)
    private String consumeUser;
    public void setConsumeUser(String consumeUser) {
        this.consumeUser = consumeUser;
    }
    public String getConsumeUser() {
        return consumeUser;
    }
        
    @FieldInfo(name = "接待部门")
    @Column(name = "RECEPTION_DEPT", length = 128, nullable = true)
    private String receptionDept;
    public void setReceptionDept(String receptionDept) {
        this.receptionDept = receptionDept;
    }
    public String getReceptionDept() {
        return receptionDept;
    }
        
    @FieldInfo(name = "来访单位")
    @Column(name = "VISITOR_UNIT", length = 128, nullable = true)
    private String visitorUnit;
    public void setVisitorUnit(String visitorUnit) {
        this.visitorUnit = visitorUnit;
    }
    public String getVisitorUnit() {
        return visitorUnit;
    }
        
    @FieldInfo(name = "消费总额")
    @Column(name = "CONSUME_TOTAL", length = 19, nullable = false)
    private BigDecimal consumeTotal;
    public void setConsumeTotal(BigDecimal consumeTotal) {
        this.consumeTotal = consumeTotal;
    }
    public BigDecimal getConsumeTotal() {
        return consumeTotal;
    }
        
    @FieldInfo(name = "实付金额")
    @Column(name = "REAL_PAY", length = 19, nullable = false)
    private BigDecimal realPay;
    public void setRealPay(BigDecimal realPay) {
        this.realPay = realPay;
    }
    public BigDecimal getRealPay() {
        return realPay;
    }
        
    @FieldInfo(name = "找零金额")
    @Column(name = "CHANGE_PAY", length = 19, nullable = false)
    private BigDecimal changePay;
    public void setChangePay(BigDecimal changePay) {
        this.changePay = changePay;
    }
    public BigDecimal getChangePay() {
        return changePay;
    }
        
    @FieldInfo(name = "操作人")
    @Column(name = "OPERTIONER", length = 36, nullable = false)
    private String opertioner;
    public void setOpertioner(String opertioner) {
        this.opertioner = opertioner;
    }
    public String getOpertioner() {
        return opertioner;
    }
        
    @FieldInfo(name = "交易状态")
    @Column(name = "CONSUME_STATE", length = 8, nullable = false)
    private String consumeState;
    public void setConsumeState(String consumeState) {
        this.consumeState = consumeState;
    }
    public String getConsumeState() {
        return consumeState;
    }
        
    @FieldInfo(name = "交易时间")
    @Column(name = "CONSUME_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date consumeDate;
    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }
    public Date getConsumeDate() {
        return consumeDate;
    }

    @Column(name = "test_date",length = 32,nullable = true)
    private  String testDate;

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }
    /*    @FieldInfo(name = "交易明细")
    @OneToMany(mappedBy = "expenseserialId", cascade = CascadeType.ALL, fetch = FetchType.LAZY,targetEntity = CashExpensedetail.class)
    private Set<CashExpensedetail> serialDetail = new HashSet<CashExpensedetail>();

    public Set<CashExpensedetail> getSerialDetail() {
        return serialDetail;
    }

    public void setSerialDetail(Set<CashExpensedetail> serialDetail) {
        this.serialDetail = serialDetail;
    }*/
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}