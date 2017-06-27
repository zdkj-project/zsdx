package com.zd.school.build.allot.model;

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
 * ClassName: JwClassdorm Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 班级分配宿舍(JW_T_CLASSDORM)实体类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_CLASSDORMALLOT")
@AttributeOverride(name = "uuid", column = @Column(name = "CDORM_ID", length = 36, nullable = false) )
public class JwClassDormAllot extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "宿舍ID")
	@Column(name = "DORM_ID", length = 36, nullable = true)
	private String dormId;

	@FieldInfo(name = "班级ID")
	@Column(name = "CLAI_ID", length = 36, nullable = true)
	private String claiId;

	@FieldInfo(name = "是否是混班宿舍：0否-1是")
	@Column(name = "ISMIXED", length = 10, nullable = true)
	private String ismixed = "0";

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@Formula("(SELECT A.DORM_TYPE FROM dbo.BUILD_T_DORMDEFINE A  WHERE A.DORM_ID=DORM_ID AND A.ISDELETE=0)")
	@FieldInfo(name = "宿舍类型")
	private String dormType;
	
	@Transient
	@FieldInfo(name = "已入住人数")
	private String stuCount;
	
	@Formula("(SELECT A.DORM_BEDCOUNT FROM dbo.BUILD_T_DORMDEFINE A  WHERE A.DORM_ID=DORM_ID AND A.ISDELETE=0)")
	@FieldInfo(name = "床位数")
	private String dormBedCount;
	
	@FieldInfo(name = "班级名称")
	@Formula("(SELECT A.CLASS_NAME FROM dbo.JW_T_GRADECLASS A  WHERE A.CLAI_ID=CLAI_ID)")
	private String clainame;

	@Formula("(SELECT B.ROOM_NAME FROM  dbo.BUILD_T_DORMDEFINE A JOIN dbo.BUILD_T_ROOMINFO B "
			+ "ON A.ROOM_ID=B.ROOM_ID WHERE  A.ISDELETE=0 AND A.DORM_ID=DORM_ID)")
	@FieldInfo(name = "宿舍名称")
	private String dormName;

	@Formula("(SELECT D.NODE_TEXT FROM dbo.BUILD_T_DORMDEFINE B  JOIN dbo.BUILD_T_ROOMINFO C ON "
			+ "B.ROOM_ID=C.ROOM_ID JOIN dbo.BUILD_T_ROOMAREA D ON C.AREA_ID=D.AREA_ID WHERE b.DORM_ID=DORM_ID)")
	@FieldInfo(name = "所属楼栋")
	private String areaName;

	public String getDormType() {
		return dormType;
	}

	public void setDormType(String dormType) {
		this.dormType = dormType;
	}

	public String getDormName() {
		return dormName;
	}

	public void setDormName(String dormName) {
		this.dormName = dormName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDormId() {
		return dormId;
	}

	public void setDormId(String dormId) {
		this.dormId = dormId;
	}

	public String getClaiId() {
		return claiId;
	}

	public void setClaiId(String claiId) {
		this.claiId = claiId;
	}

	public String getClainame() {
		return clainame;
	}

	public void setClainame(String clainame) {
		this.clainame = clainame;
	}

	public void setIsmixed(String ismixed) {
		this.ismixed = ismixed;
	}

	public String getIsmixed() {
		return ismixed;
	}

	public String getDormBedCount() {
		return dormBedCount;
	}

	public void setDormBedCount(String dormBedCount) {
		this.dormBedCount = dormBedCount;
	}

	public String getStuCount() {
		return stuCount;
	}

	public void setStuCount(String stuCount) {
		this.stuCount = stuCount;
	}

}