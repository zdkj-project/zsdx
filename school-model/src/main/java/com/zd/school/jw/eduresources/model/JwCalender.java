package com.zd.school.jw.eduresources.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: JwCalender 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_CALENDER")
@AttributeOverride(name = "uuid", column = @Column(name = "CANDER_ID", length = 36, nullable = false))
public class JwCalender extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "学校主键")
    @Column(name = "SCHOOL_ID", length = 36, nullable = true)
    private String schoolId;
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
    public String getSchoolId() {
        return schoolId;
    }
        
    @FieldInfo(name = "学校名称")
    @Column(name = "SCHOOL_NAME", length = 64, nullable = true)
    private String schoolName;
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getSchoolName() {
        return schoolName;
    }
        
    @FieldInfo(name = "校历名称")
    @Column(name = "CANDER_NAME", length = 64, nullable = false)
    private String canderName;
    public void setCanderName(String canderName) {
        this.canderName = canderName;
    }
    public String getCanderName() {
        return canderName;
    }
        
    @FieldInfo(name = "适用校区ID")
    @Column(name = "CAMPUS_ID", length = 36, nullable = true)
    private String campusId;


    public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
    @FieldInfo(name = "校区名称")
    @Column(name = "CAMPUS_NAME", length = 64, nullable = true)
    private String campusName;

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() {
        return campusName;
    }

    @FieldInfo(name = "学段编码")
    @Column(name = "SECTION_CODE", length = 32, nullable = true)
    private String sectionCode;

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }
    
	@FieldInfo(name = "生效状态")
    @Column(name = "ACTIVITY_STATE", length = 10, nullable = true)
    private Integer activityState;
    public void setActivityState(Integer activityState) {
        this.activityState = activityState;
    }
    public Integer getActivityState() {
        return activityState;
    }
    
    @FieldInfo(name = "生效时间")
    @Column(name = "ACTIVITY_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date activityTime;
    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
    public Date getActivityTime() {
        return activityTime;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
    @FieldInfo(name = "作息时间明细数")
    @Formula("(SELECT COUNT(a.CANDER_ID) FROM JW_T_CALENDERDETAIL a WHERE a.CANDER_ID=CANDER_ID )")
    private Integer detailCount;
	public Integer getDetailCount() {
		return detailCount;
	}
	public void setDetailCount(Integer detailCount) {
		this.detailCount = detailCount;
	} 
    
}