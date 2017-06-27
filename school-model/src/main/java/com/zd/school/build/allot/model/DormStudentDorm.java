package com.zd.school.build.allot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: DormStudentdorm Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: (学生分配宿舍)实体类. date: 2016-08-26
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "DORM_T_STUDENTDORM")
@AttributeOverride(name = "uuid", column = @Column(name = "STUDORM_ID", length = 36, nullable = false) )
public class DormStudentDorm extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "班级宿舍主键")
	@Column(name = "CDORM_ID", length = 36, nullable = true)
	private String cdormId;

	@FieldInfo(name = "班级ID")
	@Column(name = "CLAI_ID", length = 36, nullable = true)
	private String claiId;

	@FieldInfo(name = "学生主键")
	@Column(name = "STU_ID", length = 50, nullable = true)
	private String stuId;

	@FieldInfo(name = "柜子编号")
	@Column(name = "ARK_NUM", length = 10, nullable = false)
	private Integer arkNum=0;

	@FieldInfo(name = "床位编号")
	@Column(name = "BED_NUM", length = 10, nullable = false)
	private Integer bedNum=0;

	@FieldInfo(name = "入住时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	@Column(name = "IN_TIME", length = 27, nullable = false)
	private Date inTime;

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@Formula("(SELECT C.ROOM_NAME FROM dbo.JW_T_CLASSDORMALLOT A"
			+ " JOIN dbo.BUILD_T_DORMDEFINE B ON A.DORM_ID=B.DORM_ID"
			+ " JOIN dbo.BUILD_T_ROOMINFO C ON C.ROOM_ID = B.ROOM_ID" + " WHERE A.CDORM_ID =CDORM_ID AND A.ISDELETE=0)")
	@FieldInfo(name = "房间名称")
	private String roomName;

	@Formula("(SELECT A.CLASS_NAME FROM dbo.JW_T_GRADECLASS A WHERE A.CLAI_ID=CLAI_ID)")
	@FieldInfo(name = "班级名称")
	private String claiName;

	@FieldInfo(name = "学号")
	@Formula("(SELECT A.USER_NUMB FROM dbo.SYS_T_USER A WHERE A.USER_ID=STU_ID)")
	private String userNumb;

	@Formula("(SELECT A.XM FROM dbo.SYS_T_USER A WHERE A.USER_ID=STU_ID)")
	@FieldInfo(name = "姓名")
	private String xm;

	@FieldInfo(name = "性别码GB/T 2261.1")
	@Formula("(SELECT A.XBM FROM dbo.SYS_T_USER A WHERE A.USER_ID=STU_ID)")
	private String xbm;

	@Formula("(SELECT A.ISMIXED FROM JW_T_CLASSDORMALLOT A WHERE A.CDORM_ID=CDORM_ID)")
	@FieldInfo(name = "混合宿舍")
	private String ismixed;

	@Transient
	@FieldInfo(name = "所需宿舍")
	private Integer sxDorm;
	@Transient
	@FieldInfo(name = "学生人数")
	private Integer stuCount;
	@Transient
	@FieldInfo(name = "有效宿舍")
	private Integer yxDorm;

	@Transient
	@FieldInfo(name = "有效男宿舍")
	private Integer nanDorm;
	@Transient
	@FieldInfo(name = "有效女宿舍")
	private Integer nvDorm;
	@Transient
	@FieldInfo(name = "有效混班宿舍")
	private Integer hunDorm;
	
	@Transient
	@FieldInfo(name = "男生所需宿舍")
	private Integer nanDormCount;
	@Transient
	@FieldInfo(name = "女生所需宿舍")
	private Integer nvDormCount;
	@Transient
	@FieldInfo(name = "男生数量")
	private Integer nanCount;
	@Transient
	@FieldInfo(name = "女生数量")
	private Integer nvCount;
	
	@Transient
	@FieldInfo(name = "所属楼层")
	private String areaName;
	@Transient
	@FieldInfo(name = "所属楼栋")
	private String upAreaName;
	
	public String getCdormId() {
		return cdormId;
	}

	public void setCdormId(String cdormId) {
		this.cdormId = cdormId;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public Integer getArkNum() {
		return arkNum;
	}

	public void setArkNum(Integer arkNum) {
		this.arkNum = arkNum;
	}

	public Integer getBedNum() {
		return bedNum;
	}

	public void setBedNum(Integer bedNum) {
		this.bedNum = bedNum;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getClaiName() {
		return claiName;
	}

	public void setClaiName(String claiName) {
		this.claiName = claiName;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}


	public String getClaiId() {
		return claiId;
	}

	public void setClaiId(String claiId) {
		this.claiId = claiId;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public String getIsmixed() {
		return ismixed;
	}

	public void setIsmixed(String ismixed) {
		this.ismixed = ismixed;
	}

	public Integer getSxDorm() {
		return sxDorm;
	}

	public void setSxDorm(Integer sxDorm) {
		this.sxDorm = sxDorm;
	}

	public Integer getStuCount() {
		return stuCount;
	}

	public void setStuCount(Integer stuCount) {
		this.stuCount = stuCount;
	}

	public Integer getYxDorm() {
		return yxDorm;
	}

	public void setYxDorm(Integer yxDorm) {
		this.yxDorm = yxDorm;
	}

	public Integer getNanDormCount() {
		return nanDormCount;
	}

	public void setNanDormCount(Integer nanDormCount) {
		this.nanDormCount = nanDormCount;
	}

	public Integer getNvDormCount() {
		return nvDormCount;
	}

	public void setNvDormCount(Integer nvDormCount) {
		this.nvDormCount = nvDormCount;
	}

	public Integer getNanCount() {
		return nanCount;
	}

	public void setNanCount(Integer nanCount) {
		this.nanCount = nanCount;
	}

	public Integer getNvCount() {
		return nvCount;
	}

	public void setNvCount(Integer nvCount) {
		this.nvCount = nvCount;
	}

	public Integer getNanDorm() {
		return nanDorm;
	}

	public void setNanDorm(Integer nanDorm) {
		this.nanDorm = nanDorm;
	}

	public Integer getNvDorm() {
		return nvDorm;
	}

	public void setNvDorm(Integer nvDorm) {
		this.nvDorm = nvDorm;
	}

	public Integer getHunDorm() {
		return hunDorm;
	}

	public void setHunDorm(Integer hunDorm) {
		this.hunDorm = hunDorm;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getUpAreaName() {
		return upAreaName;
	}

	public void setUpAreaName(String upAreaName) {
		this.upAreaName = upAreaName;
	}

	public String getXbm() {
		return xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getUserNumb() {
		return userNumb;
	}

	public void setUserNumb(String userNumb) {
		this.userNumb = userNumb;
	}

}