package com.zd.school.build.define.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: 功能室定义 Function: TODO ADD FUNCTION. Reason: TODO ADD REASON(可选).
 * Description: BUILD_T_FUNCROOMDEFIN实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BUILD_T_FUNCROOMDEFINE")
@AttributeOverride(name = "uuid", column = @Column(name = "FUNCTIONROOM_ID", length = 36, nullable = false) )
public class BuildFuncRoomDefine extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "areaId")
	@Column(name = "AREA_ID", length = 36, nullable = true)
	private String areaId;

	@FieldInfo(name = "roomId")
	@Column(name = "ROOM_ID", length = 36, nullable = true)
	private String roomId;

	@FieldInfo(name = "状态,用于标识是否分配：0未分配。1已分配")
	@Column(name = "ROOM_STATUS", length = 8, nullable = true)
	private String roomStatus = "0";

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@Formula("(SELECT A.ROOM_NAME FROM dbo.BUILD_T_ROOMINFO A WHERE A.ROOM_ID=ROOM_ID)")
	@FieldInfo(name = "房间名称")
	private String roomName;

	@Formula("(SELECT A.NODE_TEXT FROM dbo.BUILD_T_ROOMAREA A WHERE A.AREA_ID=AREA_ID)")
	@FieldInfo(name = "楼层名称")
	private String areaName;

	@Formula("(SELECT A.NODE_TEXT FROM dbo.BUILD_T_ROOMAREA A"
			+ " WHERE A.AREA_ID=(SELECT B.PARENT_NODE "
			+ " FROM dbo.BUILD_T_ROOMAREA B WHERE B.AREA_ID=AREA_ID))")
	@FieldInfo(name = "楼栋名称")
	private String upAreaName;
	public String getRoomName() {
		return roomName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public String getUpAreaName() {
		return upAreaName;
	}

	public void setUpAreaName(String upAreaName) {
		this.upAreaName = upAreaName;
	}

}