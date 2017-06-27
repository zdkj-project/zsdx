package com.zd.school.control.device.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 发卡表
 * 
 * @author hucy
 *
 */
@Entity
@Table(name = "PT_CARD")
@AttributeOverride(name = "uuid", column = @Column(name = "CARD_ID", length = 36, nullable = false))
public class Ptcard extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "卡流水号")
	@Column(name = "CARDNO")
	private Long cardNo;

	@FieldInfo(name = "关联SYS_T_USER表")
	@Column(name = "USER_ID", length = 36, nullable = true)
	private String userId;

	@FieldInfo(name = "物理卡号")
	@Column(name = "FACTORYFIXID")
	private Long factoryFixID;

	@FieldInfo(name = "卡类型ID")
	@Column(name = "CARDTYPEID")
	private Integer cardTypeId;

	@FieldInfo(name = "有效期")
	@Column(name = "EXPIRYDATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date expiryDate = new Date();

	@FieldInfo(name = "卡押金")
	@Column(name = "DEPOSIT")
	private BigDecimal deposit;

	@FieldInfo(name = "卡状态 1正常 2挂失 3注销 4换卡 7冻结")
	@Column(name = "CARDSTATUSID")
	private Integer cardStatusID;

	@FieldInfo(name = "卡状态改变时间")
	@Column(name = "STATUSCHANGETIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date statusChangeTime = new Date();

	@FieldInfo(name = "当日消费次数")
	@Column(name = "DAYCOUNT")
	private Integer dayCount;

	@FieldInfo(name = "当餐消费次数")
	@Column(name = "MEALCOUNT")
	private Integer mealCount;

	@FieldInfo(name = "当日交易金额")
	@Column(name = "DAYVALUE")
	private BigDecimal dayValue;

	public Long getCardNo() {
		return cardNo;
	}

	public void setCardNo(Long cardNo) {
		this.cardNo = cardNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getFactoryFixID() {
		return factoryFixID;
	}

	public void setFactoryFixID(Long factoryFixID) {
		this.factoryFixID = factoryFixID;
	}

	public Integer getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Integer cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public Integer getCardStatusID() {
		return cardStatusID;
	}

	public void setCardStatusID(Integer cardStatusID) {
		this.cardStatusID = cardStatusID;
	}

	public Date getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(Date statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	public Integer getDayCount() {
		return dayCount;
	}

	public void setDayCount(Integer dayCount) {
		this.dayCount = dayCount;
	}

	public Integer getMealCount() {
		return mealCount;
	}

	public void setMealCount(Integer mealCount) {
		this.mealCount = mealCount;
	}

	public BigDecimal getDayValue() {
		return dayValue;
	}

	public void setDayValue(BigDecimal dayValue) {
		this.dayValue = dayValue;
	}

	public BigDecimal getMealValue() {
		return mealValue;
	}

	public void setMealValue(BigDecimal mealValue) {
		this.mealValue = mealValue;
	}

	public Date getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(Date lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	public Integer getLastPayMealType() {
		return lastPayMealType;
	}

	public void setLastPayMealType(Integer lastPayMealType) {
		this.lastPayMealType = lastPayMealType;
	}

	@FieldInfo(name = "当餐交易金额")
	@Column(name = "MEALVALUE")
	private BigDecimal mealValue;

	@FieldInfo(name = "最后交易时间")
	@Column(name = "LASTPAYDATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date lastPayDate = new Date();

	@FieldInfo(name = "最后交易餐类")
	@Column(name = "LASTPAYMEALTYPE")
	private Integer lastPayMealType;

}
