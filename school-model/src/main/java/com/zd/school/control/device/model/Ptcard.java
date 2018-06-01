package com.zd.school.control.device.model;

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
 * ClassName: PtCard 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (PT_CARD)实体类.
 * date: 2017-04-20
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "PT_CARD")
@AttributeOverride(name = "uuid", column = @Column(name = "CARD_ID", length = 36, nullable = false))
public class Ptcard extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    

	@FieldInfo(name = "卡流水号")
    @Column(name = "CARDNO", length = 19, nullable = true)
    private Long cardno;
    public void setCardno(Long cardno) {
        this.cardno = cardno;
    }
    public Long getCardno() {
        return cardno;
    }
        
	@FieldInfo(name = "卡状态 1正常 2挂失 3注销 4换卡 7冻结")
    @Column(name = "CARDSTATUSID", length = 10, nullable = true)
    private Integer cardstatusid;
    public void setCardstatusid(Integer cardstatusid) {
        this.cardstatusid = cardstatusid;
    }
    public Integer getCardstatusid() {
        return cardstatusid;
    }
        

	@FieldInfo(name = "卡类型ID")
    @Column(name = "CARDTYPEID", length = 10, nullable = true)
    private Integer cardtypeid;
    public void setCardtypeid(Integer cardtypeid) {
        this.cardtypeid = cardtypeid;
    }
    public Integer getCardtypeid() {
        return cardtypeid;
    }
        
	@FieldInfo(name = "当日消费次数")
    @Column(name = "DAYCOUNT", length = 10, nullable = true)
    private Integer daycount;
    public void setDaycount(Integer daycount) {
        this.daycount = daycount;
    }
    public Integer getDaycount() {
        return daycount;
    }
        
	@FieldInfo(name = "当日交易金额")
    @Column(name = "DAYVALUE", length = 19, nullable = true)
    private BigDecimal dayvalue;
    public void setDayvalue(BigDecimal dayvalue) {
        this.dayvalue = dayvalue;
    }
    public BigDecimal getDayvalue() {
        return dayvalue;
    }
        
    @FieldInfo(name = "卡押金")
    @Column(name = "DEPOSIT", length = 19, nullable = true)
    private BigDecimal deposit;
    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }
    public BigDecimal getDeposit() {
        return deposit;
    }
        
	@FieldInfo(name = "有效期")
    @Column(name = "EXPIRYDATE", length = 27, columnDefinition = "datetime", nullable = true)
    private Date expirydate= new Date();;
    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }
    public Date getExpirydate() {
        return expirydate;
    }
        
	@FieldInfo(name = "物理卡号")
    @Column(name = "FACTORYFIXID", length = 19, nullable = true)
    private Long factoryfixid;
    public void setFactoryfixid(Long factoryfixid) {
        this.factoryfixid = factoryfixid;
    }
    public Long getFactoryfixid() {
        return factoryfixid;
    }
        
    @FieldInfo(name = "最后交易时间")
    @Column(name = "LASTPAYDATE", length = 27, columnDefinition = "datetime", nullable = true)
    private Date lastpaydate =new Date();
    public void setLastpaydate(Date lastpaydate) {
        this.lastpaydate = lastpaydate;
    }
    public Date getLastpaydate() {
        return lastpaydate;
    }
        
	@FieldInfo(name = "最后交易餐类")
    @Column(name = "LASTPAYMEALTYPE", length = 10, nullable = true)
    private Integer lastpaymealtype;
    public void setLastpaymealtype(Integer lastpaymealtype) {
        this.lastpaymealtype = lastpaymealtype;
    }
    public Integer getLastpaymealtype() {
        return lastpaymealtype;
    }
        
    @FieldInfo(name = "当餐消费次数")
    @Column(name = "MEALCOUNT", length = 10, nullable = true)
    private Integer mealcount;
    public void setMealcount(Integer mealcount) {
        this.mealcount = mealcount;
    }
    public Integer getMealcount() {
        return mealcount;
    }
        
	@FieldInfo(name = "当日交易金额")
    @Column(name = "MEALVALUE", length = 19, nullable = true)
    private BigDecimal mealvalue;
    public void setMealvalue(BigDecimal mealvalue) {
        this.mealvalue = mealvalue;
    }
    public BigDecimal getMealvalue() {
        return mealvalue;
    }
        
	@FieldInfo(name = "卡状态改变时间")
    @Column(name = "STATUSCHANGETIME", length = 27, columnDefinition = "datetime", nullable = true)
    private Date statuschangetime=new Date();
    public void setStatuschangetime(Date statuschangetime) {
        this.statuschangetime = statuschangetime;
    }
    public Date getStatuschangetime() {
        return statuschangetime;
    }
        
    @FieldInfo(name = "userId")
    @Column(name = "USER_ID", length = 36, nullable = true)
    private String userId;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

	@FieldInfo(name = "卡类")
	@Formula("(select t.CardType from TC_CardType t where t.CardTypeNO= CARDTYPEID)")
    private String cardTypeName;
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
    
    

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}