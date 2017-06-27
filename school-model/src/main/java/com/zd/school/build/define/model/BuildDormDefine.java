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
 * 宿舍定义
 * 
 * @ClassName: BuildDormDefine
 * @Description: TODO
 * @author: hucy
 * @date: 2016年4月20日 下午3:45:12
 *
 */
@Entity
@Table(name = "BUILD_T_DORMDEFINE")
@AttributeOverride(name = "uuid", column = @Column(name = "DORM_ID", length = 36, nullable = false) )
public class BuildDormDefine extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "房间主键")
	@Column(name = "ROOM_ID", length = 36, nullable = true)
	private String roomId;

	@FieldInfo(name = "楼层主键")
	@Column(name = "AREA_ID", length = 36, nullable = true)
	private String areaId;

	@FieldInfo(name = "类型:1男、2女、3不限")
	@Column(name = "DORM_TYPE", length = 2, nullable = true)
	private String dormType;

	@FieldInfo(name = "床位数")
	@Column(name = "DORM_BEDCOUNT", length = 8, nullable = true)
	private Integer dormBedCount;

	@FieldInfo(name = "柜子数")
	@Column(name = "DORM_CHESTCOUNT", length = 20, nullable = true)
	private Integer dormChestCount;

	@FieldInfo(name = "宿舍管理员(教师ID)")
	@Column(name = "DORM_ADMIN", length = 50, nullable = true)
	private String dormAdmin;

	@FieldInfo(name = "管理员姓名")
	@Column(name = "XM", length = 20, nullable = true)
	private String xm;

	@FieldInfo(name = "电话")
	@Column(name = "DORM_PHONE", length = 20, nullable = true)
	private String dormPhone;

	@FieldInfo(name = "传真")
	@Column(name = "DORM_FAX", length = 20, nullable = true)
	private String dormFax;

	@FieldInfo(name = "状态,用于标识是否分配：0未分配。1已分配")
	@Column(name = "ROOM_STATUS", length = 8, nullable = true)
	private String roomStatus = "0";

	@FieldInfo(name = "是否混班宿舍,0否,1是")
	@Column(name = "ISMIXED", length = 8, nullable = true)
	private String isMixed = "0";

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 8
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
	
	@Transient
	@FieldInfo(name = "用于教师id标识")
	private String tteacId;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDormType() {
		return dormType;
	}

	public void setDormType(String dormType) {
		this.dormType = dormType;
	}

	public String getDormAdmin() {
		return dormAdmin;
	}

	public void setDormAdmin(String dormAdmin) {
		this.dormAdmin = dormAdmin;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getDormPhone() {
		return dormPhone;
	}

	public void setDormPhone(String dormPhone) {
		this.dormPhone = dormPhone;
	}

	public String getDormFax() {
		return dormFax;
	}

	public void setDormFax(String dormFax) {
		this.dormFax = dormFax;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public String getIsMixed() {
		return isMixed;
	}

	public void setIsMixed(String isMixed) {
		this.isMixed = isMixed;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTteacId() {
		return tteacId;
	}

	public void setTteacId(String tteacId) {
		this.tteacId = tteacId;
	}

	public Integer getDormBedCount() {
		return dormBedCount;
	}

	public void setDormBedCount(Integer dormBedCount) {
		this.dormBedCount = dormBedCount;
	}

	public Integer getDormChestCount() {
		return dormChestCount;
	}

	public void setDormChestCount(Integer dormChestCount) {
		this.dormChestCount = dormChestCount;
	}

	public String getUpAreaName() {
		return upAreaName;
	}

	public void setUpAreaName(String upAreaName) {
		this.upAreaName = upAreaName;
	}

}
