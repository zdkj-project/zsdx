package com.zd.school.control.device.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: MjUserright Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 门禁权限表(MJ_UserRight)实体类. date: 2016-09-08
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "MJ_USERRIGHT")
@AttributeOverride(name = "uuid", column = @Column(name = "USERRIGHT_ID", length = 36, nullable = false) )
public class MjUserright extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "设备主键")
	@Column(name = "TERM_ID", length = 36, nullable = false)
	private String termId;

	@FieldInfo(name = "人员主键")
	@Column(name = "STU_ID", length = 36, nullable = false)
	private String stuId;

	@FieldInfo(name = "卡流水号")
	@Column(name = "CARD_ID", length = 36, nullable = true)
	private String cardId;

	@FieldInfo(name = "物理卡号")
	@Column(name = "CARDSER_NO", length = 36, nullable = true)
	private String cardserNo;


	@FieldInfo(name = "时段ID")
	@Column(name = "CONTROLSEG_ID")
	private Integer controlsegId;

	@FieldInfo(name = "卡片状态，在卡片挂失、解挂、换卡、补卡、退卡、销户等操作时更新")
	@Column(name = "CARDSTATUS_ID")
	private Integer cardstatusId;

	@FieldInfo(name = "是否下载（更新CardStatusID的同时更新此字段为False）")
	@Column(name = "ISDOWN_LOAD")
	private Boolean isdownLoad;


	@FieldInfo(name = "数据状态对应数据字典（0正常，1	删除，2无效，3过期，4历史）")
	@Column(name = "STATUS_ID")
	private Integer statusID;

	@FieldInfo(name = "卡片状态日期，在卡片挂失、解挂、换卡、补卡、退卡、销户等操作时更新")
	@Column(name = "STATUSCHANGE_TIME", length = 23, nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date statuschangeTime;

	@FieldInfo(name = "学生名称")
	@Formula("(SELECT A.XM FROM dbo.SYS_T_USER A WHERE A.USER_ID=STU_ID)")
	private String xm;

	@FieldInfo(name = "设备名称")
	@Formula("(SELECT A.TERMNAME FROM dbo.PT_TERM A WHERE A.TERM_ID=TERM_ID)")
	private String termName;
	
	@FieldInfo(name = "设备序列号")
	@Formula("(SELECT A.TERMSN FROM dbo.PT_TERM A WHERE A.TERM_ID=TERM_ID)")
	private String termSN;
	
	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardserNo() {
		return cardserNo;
	}

	public void setCardserNo(String cardserNo) {
		this.cardserNo = cardserNo;
	}

	public Integer getControlsegId() {
		return controlsegId;
	}

	public void setControlsegId(Integer controlsegId) {
		this.controlsegId = controlsegId;
	}

	public Integer getCardstatusId() {
		return cardstatusId;
	}

	public void setCardstatusId(Integer cardstatusId) {
		this.cardstatusId = cardstatusId;
	}

	public Boolean getIsdownLoad() {
		return isdownLoad;
	}

	public void setIsdownLoad(Boolean isdownLoad) {
		this.isdownLoad = isdownLoad;
	}

	public Integer getStatusID() {
		return statusID;
	}

	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	public Date getStatuschangeTime() {
		return statuschangeTime;
	}

	public void setStatuschangeTime(Date statuschangeTime) {
		this.statuschangeTime = statuschangeTime;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getTermId() {
		return termId;
	}

	public String getTermName() {
		return termName;
	}

	public String getTermSN() {
		return termSN;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public void setTermSN(String termSN) {
		this.termSN = termSN;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}