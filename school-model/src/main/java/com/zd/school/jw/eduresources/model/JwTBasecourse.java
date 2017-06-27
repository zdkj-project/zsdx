package com.zd.school.jw.eduresources.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: JwTBasecourse Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 基础课程信息实体类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_BASECOURSE")
@AttributeOverride(name = "uuid", column = @Column(name = "BASECOURSE_ID", length = 36, nullable = false))
public class JwTBasecourse extends BaseEntity implements Serializable {
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

    @FieldInfo(name = "课程编码")
    @Column(name = "COURSE_CODE", length = 32, nullable = true)
    private String courseCode;

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @FieldInfo(name = "课程名称")
    @Column(name = "COURSE_NAME", length = 60, nullable = true)
    private String courseName;

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    @FieldInfo(name = "课程类别码")
    @Column(name = "COURSE_TYPE", length = 10, nullable = true)
    private String courseType;

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseType() {
        return courseType;
    }

    @FieldInfo(name = "课程等级码")
    @Column(name = "COURSE_LEVEL", length = 10, nullable = true)
    private String courseLevel;

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    @FieldInfo(name = "课程别名")
    @Column(name = "ALIAS_NAME", length = 60, nullable = true)
    private String aliasName;

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasName() {
        return aliasName;
    }

    @FieldInfo(name = "总学时")
    @Column(name = "TOTAL_HOUR", length = 3, nullable = true)
    private BigDecimal totalHour;

    public void setTotalHour(BigDecimal totalHour) {
        this.totalHour = totalHour;
    }

    public BigDecimal getTotalHour() {
        return totalHour;
    }

    @FieldInfo(name = "周学时")
    @Column(name = "WEEK_HOUR", length = 2, nullable = true)
    private BigDecimal weekHour;

    public void setWeekHour(BigDecimal weekHour) {
        this.weekHour = weekHour;
    }

    public BigDecimal getWeekHour() {
        return weekHour;
    }

    @FieldInfo(name = "自学学时")
    @Column(name = "ONESELF_HOUR", length = 3, nullable = true)
    private BigDecimal oneselfHour;

    public void setOneselfHour(BigDecimal oneselfHour) {
        this.oneselfHour = oneselfHour;
    }

    public BigDecimal getOneselfHour() {
        return oneselfHour;
    }

    @FieldInfo(name = "授课方式码")
    @Column(name = "TEACH_WAY", length = 10, nullable = true)
    private String teachWay;

    public void setTeachWay(String teachWay) {
        this.teachWay = teachWay;
    }

    public String getTeachWay() {
        return teachWay;
    }

    @FieldInfo(name = "课程简介")
    @Column(name = "COURSE_DESC", length = 1024, nullable = true)
    private String courseDesc;

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    @FieldInfo(name = "课程要求")
    @Column(name = "COURSE_REQUEST", length = 1024, nullable = true)
    private String courseRequest;

    public void setCourseRequest(String courseRequest) {
        this.courseRequest = courseRequest;
    }

    public String getCourseRequest() {
        return courseRequest;
    }

    public JwTBasecourse() {

        super();
        // TODO Auto-generated constructor stub

    }

    public JwTBasecourse(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "学校名称")
    @Formula("(SELECT a.NODE_TEXT from BASE_T_ORG a where a.DEPT_ID=SCHOOL_ID)")
    private String schoolName;

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
    }

}