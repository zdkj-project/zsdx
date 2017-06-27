package com.zd.school.jw.arrangecourse.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: JwCourseStudy 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 自习课程表实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_COURSE_STUDY")
@AttributeOverride(name = "uuid", column = @Column(name = "ZXID", length = 36, nullable = false))
public class JwCourseStudy extends BaseEntity implements Serializable{
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
        
    @FieldInfo(name = "学年")
    @Column(name = "SCHOOL_YEAR", length = 32, nullable = true)
    private String schoolYear;
    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }
    public String getSchoolYear() {
        return schoolYear;
    }
        
    @FieldInfo(name = "学期")
    @Column(name = "SCHOOL_TERM", length = 32, nullable = true)
    private String schoolTerm;
    public void setSchoolTerm(String schoolTerm) {
        this.schoolTerm = schoolTerm;
    }
    public String getSchoolTerm() {
        return schoolTerm;
    }
        
    @FieldInfo(name = "班级ID")
    @Column(name = "CLAI_ID", length = 36, nullable = true)
    private String claiId;
    public void setClaiId(String claiId) {
        this.claiId = claiId;
    }
    public String getClaiId() {
        return claiId;
    }
        
    @FieldInfo(name = "班号")
    @Column(name = "CLASS_CODE", length = 36, nullable = true)
    private String classCode;
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    public String getClassCode() {
        return classCode;
    }
        
    @FieldInfo(name = "班级名称")
    @Column(name = "CLASS_NAME", length = 36, nullable = true)
    private String className;
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }
        
    @FieldInfo(name = "自习类别（1为早自习。2，3为晚自习）")
    @Column(name = "STUDY_CATEGORY", length = 64, nullable = true)
    private String studyCategory;
    public void setStudyCategory(String studyCategory) {
        this.studyCategory = studyCategory;
    }
    public String getStudyCategory() {
        return studyCategory;
    }
        
    @FieldInfo(name = "课程ID1")
    @Column(name = "COURSE_ID01", length = 36, nullable = true)
    private String courseId01;
    public void setCourseId01(String courseId01) {
        this.courseId01 = courseId01;
    }
    public String getCourseId01() {
        return courseId01;
    }
        
    @FieldInfo(name = "课程名1")
    @Column(name = "COURSE_NAME01", length = 64, nullable = true)
    private String courseName01;
    public void setCourseName01(String courseName01) {
        this.courseName01 = courseName01;
    }
    public String getCourseName01() {
        return courseName01;
    }
        
    @FieldInfo(name = "任课教师ID1")
    @Column(name = "TTEAC_ID01", length = 36, nullable = true)
    private String tteacId01;
    public void setTteacId01(String tteacId01) {
        this.tteacId01 = tteacId01;
    }
    public String getTteacId01() {
        return tteacId01;
    }
        
    @FieldInfo(name = "任课教师工号1")
    @Column(name = "TEACHER_GH01", length = 32, nullable = true)
    private String teacherGh01;
    public void setTeacherGh01(String teacherGh01) {
        this.teacherGh01 = teacherGh01;
    }
    public String getTeacherGh01() {
        return teacherGh01;
    }
        
    @FieldInfo(name = "任课教师姓名1")
    @Column(name = "TEACHER_NAME01", length = 64, nullable = true)
    private String teacherName01;
    public void setTeacherName01(String teacherName01) {
        this.teacherName01 = teacherName01;
    }
    public String getTeacherName01() {
        return teacherName01;
    }
        
    @FieldInfo(name = "课程ID2")
    @Column(name = "COURSE_ID02", length = 36, nullable = true)
    private String courseId02;
    public void setCourseId02(String courseId02) {
        this.courseId02 = courseId02;
    }
    public String getCourseId02() {
        return courseId02;
    }
        
    @FieldInfo(name = "课程名2")
    @Column(name = "COURSE_NAME02", length = 64, nullable = true)
    private String courseName02;
    public void setCourseName02(String courseName02) {
        this.courseName02 = courseName02;
    }
    public String getCourseName02() {
        return courseName02;
    }
        
    @FieldInfo(name = "任课教师ID2")
    @Column(name = "TTEAC_ID02", length = 36, nullable = true)
    private String tteacId02;
    public void setTteacId02(String tteacId02) {
        this.tteacId02 = tteacId02;
    }
    public String getTteacId02() {
        return tteacId02;
    }
        
    @FieldInfo(name = "任课教师工号2")
    @Column(name = "TEACHER_GH02", length = 32, nullable = true)
    private String teacherGh02;
    public void setTeacherGh02(String teacherGh02) {
        this.teacherGh02 = teacherGh02;
    }
    public String getTeacherGh02() {
        return teacherGh02;
    }
        
    @FieldInfo(name = "任课教师姓名2")
    @Column(name = "TEACHER_NAME02", length = 64, nullable = true)
    private String teacherName02;
    public void setTeacherName02(String teacherName02) {
        this.teacherName02 = teacherName02;
    }
    public String getTeacherName02() {
        return teacherName02;
    }
        
    @FieldInfo(name = "课程ID3")
    @Column(name = "COURSE_ID03", length = 36, nullable = true)
    private String courseId03;
    public void setCourseId03(String courseId03) {
        this.courseId03 = courseId03;
    }
    public String getCourseId03() {
        return courseId03;
    }
        
    @FieldInfo(name = "课程名3")
    @Column(name = "COURSE_NAME03", length = 64, nullable = true)
    private String courseName03;
    public void setCourseName03(String courseName03) {
        this.courseName03 = courseName03;
    }
    public String getCourseName03() {
        return courseName03;
    }
        
    @FieldInfo(name = "任课教师ID3")
    @Column(name = "TTEAC_ID03", length = 36, nullable = true)
    private String tteacId03;
    public void setTteacId03(String tteacId03) {
        this.tteacId03 = tteacId03;
    }
    public String getTteacId03() {
        return tteacId03;
    }
        
    @FieldInfo(name = "任课教师工号3")
    @Column(name = "TEACHER_GH03", length = 32, nullable = true)
    private String teacherGh03;
    public void setTeacherGh03(String teacherGh03) {
        this.teacherGh03 = teacherGh03;
    }
    public String getTeacherGh03() {
        return teacherGh03;
    }
        
    @FieldInfo(name = "任课教师姓名3")
    @Column(name = "TEACHER_NAME03", length = 64, nullable = true)
    private String teacherName03;
    public void setTeacherName03(String teacherName03) {
        this.teacherName03 = teacherName03;
    }
    public String getTeacherName03() {
        return teacherName03;
    }
        
    @FieldInfo(name = "课程ID4")
    @Column(name = "COURSE_ID04", length = 36, nullable = true)
    private String courseId04;
    public void setCourseId04(String courseId04) {
        this.courseId04 = courseId04;
    }
    public String getCourseId04() {
        return courseId04;
    }
        
    @FieldInfo(name = "课程名4")
    @Column(name = "COURSE_NAME04", length = 64, nullable = true)
    private String courseName04;
    public void setCourseName04(String courseName04) {
        this.courseName04 = courseName04;
    }
    public String getCourseName04() {
        return courseName04;
    }
        
    @FieldInfo(name = "任课教师ID4")
    @Column(name = "TTEAC_ID04", length = 36, nullable = true)
    private String tteacId04;
    public void setTteacId04(String tteacId04) {
        this.tteacId04 = tteacId04;
    }
    public String getTteacId04() {
        return tteacId04;
    }
        
    @FieldInfo(name = "任课教师工号4")
    @Column(name = "TEACHER_GH04", length = 32, nullable = true)
    private String teacherGh04;
    public void setTeacherGh04(String teacherGh04) {
        this.teacherGh04 = teacherGh04;
    }
    public String getTeacherGh04() {
        return teacherGh04;
    }
        
    @FieldInfo(name = "任课教师姓名4")
    @Column(name = "TEACHER_NAME04", length = 64, nullable = true)
    private String teacherName04;
    public void setTeacherName04(String teacherName04) {
        this.teacherName04 = teacherName04;
    }
    public String getTeacherName04() {
        return teacherName04;
    }
        
    @FieldInfo(name = "课程ID5")
    @Column(name = "COURSE_ID05", length = 36, nullable = true)
    private String courseId05;
    public void setCourseId05(String courseId05) {
        this.courseId05 = courseId05;
    }
    public String getCourseId05() {
        return courseId05;
    }
        
    @FieldInfo(name = "课程名5")
    @Column(name = "COURSE_NAME05", length = 64, nullable = true)
    private String courseName05;
    public void setCourseName05(String courseName05) {
        this.courseName05 = courseName05;
    }
    public String getCourseName05() {
        return courseName05;
    }
        
    @FieldInfo(name = "任课教师ID5")
    @Column(name = "TTEAC_ID05", length = 36, nullable = true)
    private String tteacId05;
    public void setTteacId05(String tteacId05) {
        this.tteacId05 = tteacId05;
    }
    public String getTteacId05() {
        return tteacId05;
    }
        
    @FieldInfo(name = "任课教师工号5")
    @Column(name = "TEACHER_GH05", length = 32, nullable = true)
    private String teacherGh05;
    public void setTeacherGh05(String teacherGh05) {
        this.teacherGh05 = teacherGh05;
    }
    public String getTeacherGh05() {
        return teacherGh05;
    }
        
    @FieldInfo(name = "任课教师姓名5")
    @Column(name = "TEACHER_NAME05", length = 64, nullable = true)
    private String teacherName05;
    public void setTeacherName05(String teacherName05) {
        this.teacherName05 = teacherName05;
    }
    public String getTeacherName05() {
        return teacherName05;
    }
        
    @FieldInfo(name = "课程ID6")
    @Column(name = "COURSE_ID06", length = 36, nullable = true)
    private String courseId06;
    public void setCourseId06(String courseId06) {
        this.courseId06 = courseId06;
    }
    public String getCourseId06() {
        return courseId06;
    }
        
    @FieldInfo(name = "课程名6")
    @Column(name = "COURSE_NAME06", length = 64, nullable = true)
    private String courseName06;
    public void setCourseName06(String courseName06) {
        this.courseName06 = courseName06;
    }
    public String getCourseName06() {
        return courseName06;
    }
        
    @FieldInfo(name = "任课教师ID6")
    @Column(name = "TTEAC_ID06", length = 36, nullable = true)
    private String tteacId06;
    public void setTteacId06(String tteacId06) {
        this.tteacId06 = tteacId06;
    }
    public String getTteacId06() {
        return tteacId06;
    }
        
    @FieldInfo(name = "任课教师工号6")
    @Column(name = "TEACHER_GH06", length = 32, nullable = true)
    private String teacherGh06;
    public void setTeacherGh06(String teacherGh06) {
        this.teacherGh06 = teacherGh06;
    }
    public String getTeacherGh06() {
        return teacherGh06;
    }
        
    @FieldInfo(name = "任课教师姓名6")
    @Column(name = "TEACHER_NAME06", length = 64, nullable = true)
    private String teacherName06;
    public void setTeacherName06(String teacherName06) {
        this.teacherName06 = teacherName06;
    }
    public String getTeacherName06() {
        return teacherName06;
    }
        
    @FieldInfo(name = "课程ID7")
    @Column(name = "COURSE_ID07", length = 36, nullable = true)
    private String courseId07;
    public void setCourseId07(String courseId07) {
        this.courseId07 = courseId07;
    }
    public String getCourseId07() {
        return courseId07;
    }
        
    @FieldInfo(name = "课程名7")
    @Column(name = "COURSE_NAME07", length = 64, nullable = true)
    private String courseName07;
    public void setCourseName07(String courseName07) {
        this.courseName07 = courseName07;
    }
    public String getCourseName07() {
        return courseName07;
    }
        
    @FieldInfo(name = "任课教师ID7")
    @Column(name = "TTEAC_ID07", length = 36, nullable = true)
    private String tteacId07;
    public void setTteacId07(String tteacId07) {
        this.tteacId07 = tteacId07;
    }
    public String getTteacId07() {
        return tteacId07;
    }
        
    @FieldInfo(name = "任课教师工号7")
    @Column(name = "TEACHER_GH07", length = 32, nullable = true)
    private String teacherGh07;
    public void setTeacherGh07(String teacherGh07) {
        this.teacherGh07 = teacherGh07;
    }
    public String getTeacherGh07() {
        return teacherGh07;
    }
        
    @FieldInfo(name = "任课教师姓名7")
    @Column(name = "TEACHER_NAME07", length = 64, nullable = true)
    private String teacherName07;
    public void setTeacherName07(String teacherName07) {
        this.teacherName07 = teacherName07;
    }
    public String getTeacherName07() {
        return teacherName07;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}