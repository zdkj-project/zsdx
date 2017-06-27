package com.zd.school.build.define.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BuildRoominfo Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 教室信息实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BUILD_T_ROOMINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "ROOM_ID", length = 36, nullable = false) )
public class BuildRoominfo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "区域ID")
	@Column(name = "AREA_ID", length = 36, nullable = false)
	private String areaId;

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaId() {
		return areaId;
	}

	@FieldInfo(name = "教室编码")
	@Column(name = "ROOM_CODE", length = 32, nullable = true)
	private String roomCode;

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomCode() {
		return roomCode;
	}

	@FieldInfo(name = "房间名称")
	@Column(name = "ROOM_NAME", length = 32, nullable = true)
	private String roomName;

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}

	@FieldInfo(name = "房间类型")
	@Column(name = "ROOM_TYPE", length = 16, nullable = true)
	private String roomType = "0";

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getRoomType() {
		return roomType;
	}

	@FieldInfo(name = "班额代码")
	@Column(name = "ROOM_CAPACITY", length = 32, nullable = true)
	private String roomCapacity;

	public void setRoomCapacity(String roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public String getRoomCapacity() {
		return roomCapacity;
	}

	@FieldInfo(name = "是否多媒体教室-0是,1否")
	@Column(name = "ISMEDIAROOM", length = 10, nullable = true)
	private String ismediaroom = "1";

	public void setIsmediaroom(String ismediaroom) {
		this.ismediaroom = ismediaroom;
	}

	public String getIsmediaroom() {
		return ismediaroom;
	}

	@FieldInfo(name = "网络状态：0有网络，1无网络")
	@Column(name = "ROOM_NET", length = 10, nullable = true)
	private String roomNet = "0";

	public void setRoomNet(String roomNet) {
		this.roomNet = roomNet;
	}

	public String getRoomNet() {
		return roomNet;
	}

	@FieldInfo(name = "房间状态")
	@Column(name = "AREA_STATU", length = 10, nullable = true)
	private Integer areaStatu;

	public void setAreaStatu(Integer areaStatu) {
		this.areaStatu = areaStatu;
	}

	public Integer getAreaStatu() {
		return areaStatu;
	}

	@FieldInfo(name = "教室说明")
	@Column(name = "ROOM_DESC", length = 128, nullable = true)
	private String roomDesc;

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@FieldInfo(name = "区域名称")
	@Formula("(SELECT isnull(a.NODE_TEXT,'ROOT') FROM BUILD_T_ROOMAREA a WHERE a.AREA_ID=AREA_ID)")
	private String areaName;

	@Transient
	@FieldInfo(name = "批量添加房间数量")
	private int roomCount;

	@FieldInfo(name = "上级名称")
	@Formula("(SELECT R.NODE_TEXT  FROM dbo.BUILD_T_ROOMAREA R WHERE R.AREA_ID="
			+ "(SELECT A.PARENT_NODE FROM dbo.BUILD_T_ROOMAREA A WHERE A.AREA_ID=AREA_ID))")
	private String areaUpName;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaUpName() {
		return areaUpName;
	}

	public void setAreaUpName(String areaUpName) {
		this.areaUpName = areaUpName;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

}