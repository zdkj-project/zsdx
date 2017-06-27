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
 * 钱包表
 * 
 * @author hucy
 *
 */

@Entity
@Table(name = "PT_CARD_BAGS")
@AttributeOverride(name = "uuid", column = @Column(name = "BAGS_ID", length = 36, nullable = false))
public class PtcardBags extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "钱包ID 1现金钱包 2水控钱包 3补助钱包 4信用钱包 5订餐钱包")
	@Column(name = "BAGID")
	private Integer bagId;

	@FieldInfo(name = "关联字典中的钱包定义")
	@Column(name = "BAGCODE", length = 36, nullable = true)
	private String bagCode;

	@FieldInfo(name = "关联SYS_T_USER表")
	@Column(name = "USER_ID", length = 36, nullable = true)
	private String userId;

	@FieldInfo(name = "钱包金额")
	@Column(name = "CARDVALUE")
	private BigDecimal cardValue;

	@FieldInfo(name = "消费总额")
	@Column(name = "XFMONEYTOTAL")
	private BigDecimal xfMoneyTotal;

	@FieldInfo(name = "消费次数")
	@Column(name = "XFCOUNT")
	private Integer xfCount;

	@FieldInfo(name = "充值总额")
	@Column(name = "CZMONEYTOTAL")
	private BigDecimal czMoneyTotal;

	@FieldInfo(name = "充值次数")
	@Column(name = "TERMTYPEID")
	private Integer czCount;

	@FieldInfo(name = "钱包金额最后更新时间")
	@Column(name = "BAGUPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date bagUpdateDate = new Date();

	@FieldInfo(name = "钱包状态 1正常 0未启用")
	@Column(name = "BAGSTATUSID")
	private Integer bagStatusId;

	public Integer getBagId() {
		return bagId;
	}

	public void setBagId(Integer bagId) {
		this.bagId = bagId;
	}

	public String getBagCode() {
		return bagCode;
	}

	public void setBagCode(String bagCode) {
		this.bagCode = bagCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getCardValue() {
		return cardValue;
	}

	public void setCardValue(BigDecimal cardValue) {
		this.cardValue = cardValue;
	}

	public BigDecimal getXfMoneyTotal() {
		return xfMoneyTotal;
	}

	public void setXfMoneyTotal(BigDecimal xfMoneyTotal) {
		this.xfMoneyTotal = xfMoneyTotal;
	}

	public Integer getXfCount() {
		return xfCount;
	}

	public void setXfCount(Integer xfCount) {
		this.xfCount = xfCount;
	}

	public BigDecimal getCzMoneyTotal() {
		return czMoneyTotal;
	}

	public void setCzMoneyTotal(BigDecimal czMoneyTotal) {
		this.czMoneyTotal = czMoneyTotal;
	}

	public Integer getCzCount() {
		return czCount;
	}

	public void setCzCount(Integer czCount) {
		this.czCount = czCount;
	}

	public Date getBagUpdateDate() {
		return bagUpdateDate;
	}

	public void setBagUpdateDate(Date bagUpdateDate) {
		this.bagUpdateDate = bagUpdateDate;
	}

	public Integer getBagStatusId() {
		return bagStatusId;
	}

	public void setBagStatusId(Integer bagStatusId) {
		this.bagStatusId = bagStatusId;
	}

	public String getCardValue_Enrypt() {
		return cardValue_Enrypt;
	}

	public void setCardValue_Enrypt(String cardValue_Enrypt) {
		this.cardValue_Enrypt = cardValue_Enrypt;
	}

	@FieldInfo(name = "卡片余额加密字段")
	@Column(name = "CARDVALUE_ENRYPT", length = 512, nullable = true)
	private String cardValue_Enrypt;
}
