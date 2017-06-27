package com.zd.school.jw.schoolcourse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import com.zd.core.util.DateSerializer;
import com.zd.school.jw.eduresources.model.JwTGrade;

/**
 * 
 * ClassName: JwPublishcourse Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 校本课程发布课程信息(JW_T_PUBLISHCOURSE)实体类. date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_PUBLISHCOURSE")
@AttributeOverride(name = "uuid", column = @Column(name = "PCOURSE_ID", length = 36, nullable = false))
public class JwPublishcourse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "课程发布ID")
    @Column(name = "PUBLISH_ID", length = 36, nullable = true)
    private String publishId;

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getPublishId() {
        return publishId;
    }

    @FieldInfo(name = "课程ID,对应校本课程的主键字段")
    @Column(name = "COURSE_ID", length = 36, nullable = true)
    private String courseId;

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    @FieldInfo(name = "星期")
    @Column(name = "WEEK", length = 255, nullable = true)
    private String week;

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    @FieldInfo(name = "节次")
    @Column(name = "JC", length = 255, nullable = true)
    private String jc;

    public void setJc(String jc) {
        this.jc = jc;
    }

    public String getJc() {
        return jc;
    }

    @FieldInfo(name = "可报名人数")
    @Column(name = "SIGN_COUNT", length = 10, nullable = true)
    private Integer signCount;

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    @FieldInfo(name = "选课开始日期")
    @Column(name = "BEGIN_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = DateSerializer.class)
    private Date beginDate;

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    @FieldInfo(name = "选课结束日期")
    @Column(name = "END_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = DateSerializer.class)
    private Date endDate;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @JsonIgnore
    @FieldInfo(name = "课程适用选课对象")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "JW_T_PCOURSEGRADE", joinColumns = { @JoinColumn(name = "PCOURSE_ID") }, inverseJoinColumns = {
            @JoinColumn(name = "GRAI_ID") })
    @Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JwTGrade> pcourseGrade = new HashSet<>();

    public Set<JwTGrade> getPcourseGrade() {
        return pcourseGrade;
    }

    public void setPcourseGrade(Set<JwTGrade> pcourseGrade) {
        this.pcourseGrade = pcourseGrade;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    //@Transient
    @Formula("(SELECT a.COURSE_NAME FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=COURSE_ID)")
    @FieldInfo(name = "课程名称")
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Transient
    @FieldInfo(name = "年级ID")
    private String graiId;

    public String getGraiId() {
        return graiId;
    }

    public void setGraiId(String graiId) {
        this.graiId = graiId;
    }
    
    @Formula("(SELECT dbo.fn_GetCOURSESELSTU(PCOURSE_ID))")
    @FieldInfo(name = "年级名称")
    private String gradeName;
    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    
    @Formula("(SELECT a.course_Content FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=COURSE_ID)")
    @FieldInfo(name = "主要内容")
    private String courseContent;
	public String getCourseContent() {
		return courseContent;
	}
	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}
	
	@Formula("(SELECT a.teacher_Name FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=COURSE_ID)")
    @FieldInfo(name = "主讲教师")
    private String teacherName;

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}