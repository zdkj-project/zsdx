package com.zd.school.build.allot.model;

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
 * ClassName: JwClassroomallot Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 班级分配教室实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_CLASSROOMALLOT")
@AttributeOverride(name = "uuid", column = @Column(name = "CLASSROOMALLOT_ID", length = 36, nullable = false) )
public class JwClassRoomAllot extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "班级主键")
	@Column(name = "CLAI_ID", length = 36, nullable = true)
	private String claiId;

	@FieldInfo(name = "房间主键")
	@Column(name = "ROOM_ID", length = 36, nullable = true)
	private String roomId;

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */

	@Formula("(SELECT A.ROOM_NAME FROM dbo.BUILD_T_ROOMINFO A WHERE A.ROOM_ID=ROOM_ID)")
	@FieldInfo(name = "房间名称")
	private String roomName;

	@Formula("(SELECT B.NODE_TEXT FROM dbo.BUILD_T_ROOMINFO A"
			+ " JOIN dbo.BUILD_T_ROOMAREA B ON A.AREA_ID=B.AREA_ID"
			+ " WHERE A.ROOM_ID=ROOM_ID AND A.ISDELETE=0)")
	@FieldInfo(name = "楼层名称")
	private String areaName;

	@Formula("(SELECT A.CLASS_NAME FROM dbo.JW_T_GRADECLASS A WHERE A.CLAI_ID=CLAI_ID)")
	@FieldInfo(name = "班级名称")
	private String className;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClaiId() {
		return claiId;
	}

	public void setClaiId(String claiId) {
		this.claiId = claiId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}