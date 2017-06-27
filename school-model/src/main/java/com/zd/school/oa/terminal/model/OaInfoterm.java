package com.zd.school.oa.terminal.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.excel.annotation.MapperCell;

/**
 * 
 * ClassName: OaInfoterm Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 信息发布终端(OA_T_INFOTERM)实体类. date: 2017-01-14
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "OA_T_INFOTERM")
@AttributeOverride(name = "uuid", column = @Column(name = "TERM_ID", length = 36, nullable = false))
public class OaInfoterm extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@MapperCell(cellName = "终端号", order = 1)
	@FieldInfo(name = "终端号")
	@Column(name = "TERM_CODE", length = 6, nullable = false)
	private String termCode;

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public String getTermCode() {
		return termCode;
	}

	@FieldInfo(name = "终端类型，数据字典INFOTERTYPE")
	@Column(name = "TERM_TYPE", length = 16, nullable = false)
	private String termType;

	public void setTermType(String termType) {
		this.termType = termType;
	}

	public String getTermType() {
		return termType;
	}

	@FieldInfo(name = "规格")
	@Column(name = "TERM_SPEC", length = 32, nullable = true)
	private String termSpec;

	public void setTermSpec(String termSpec) {
		this.termSpec = termSpec;
	}

	public String getTermSpec() {
		return termSpec;
	}

	@FieldInfo(name = "使用状态，0-未使用 1-已使用")
	@Column(name = "IS_USE", length = 10, nullable = false)
	private Integer isUse;

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public Integer getIsUse() {
		return isUse;
	}

	@FieldInfo(name = "使用房间ID")
	@Column(name = "ROOM_ID", length = 36, nullable = true)
	private String roomId;

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}

	@MapperCell(cellName = "使用房间名称", order = 2)
	@FieldInfo(name = "使用房间名称")
	@Column(name = "ROOM_NAME", length = 64, nullable = true)
	private String roomName;

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}

	@MapperCell(cellName = "使用房间门牌号", order = 3)
	@FieldInfo(name = "使用房间名称门牌号")
	@Column(name = "HOUSE_NUMB", length = 64, nullable = true)
	private String houseNumb;

	public String getHouseNumb() {
		return houseNumb;
	}

	public void setHouseNumb(String houseNumb) {
		this.houseNumb = houseNumb;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
}