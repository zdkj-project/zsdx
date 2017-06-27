package com.zd.school.build.define.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 水控费率定义
 * 
 * @author hucy
 *
 */
@Entity
@Table(name = "SK_PRICE")
@AttributeOverride(name = "uuid", column = @Column(name = "SK_PRICEID", length = 36, nullable = false) )
public class SkPriceDefine extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "费率名称")
	@Column(name = "PRICE_NAME", length = 36, nullable = true)
	private String priceName;

	@FieldInfo(name = "费率价格")
	@Column(name = "PRICE_VALUE")
	private double priceValue;

	@FieldInfo(name = "状态,用于标识是否分配：0启动。1禁用")
	@Column(name = "PRICE_STATUS", length = 8, nullable = true)
	private String priceStatus;

	@FieldInfo(name = "货币种类")
	@Column(name = "CURRENCY_TYPE", length = 36, nullable = true)
	private String currencyType = "RMB(人民币)";

	@FieldInfo(name = "备注")
	@Column(name = "PRICE_NOTES", length = 500, nullable = true)
	private String priceNotes;

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public double getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(double priceValue) {
		this.priceValue = priceValue;
	}

	public String getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getPriceNotes() {
		return priceNotes;
	}

	public void setPriceNotes(String priceNotes) {
		this.priceNotes = priceNotes;
	}


	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}