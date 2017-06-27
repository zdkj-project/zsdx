package com.zd.school.jw.eduresources.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: JwTSchoolcourse Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 校本课程实体类. date: 2016-03-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_SCHOOLCOURSE")
@AttributeOverride(name = "uuid", column = @Column(name = "SCHOOLCOURSE_ID", length = 36, nullable = false))
public class JwTSchoolcourse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "课程编码")
    @Column(name = "COURSE_CODE", length = 32, nullable = true)
    private String courseCode;

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @FieldInfo(name = "课程类型")
    @Column(name = "COURSE_TYPE", length = 16, nullable = true)
    private String courseType;

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseType() {
        return courseType;
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

    @FieldInfo(name = "授课方式码")
    @Column(name = "TEACH_WAY", length = 10, nullable = true)
    private String teachWay;

    public void setTeachWay(String teachWay) {
        this.teachWay = teachWay;
    }

    public String getTeachWay() {
        return teachWay;
    }

    @FieldInfo(name = "讲师ID")
    @Column(name = "TEACH_ID", length = 36, nullable = true)
    private String teachID;

    public String getTeachID() {
        return teachID;
    }

    public void setTeachID(String teachID) {
        this.teachID = teachID;
    }

    @FieldInfo(name = "讲师姓名")
    @Column(name = "TEACHER_NAME", length = 16, nullable = true)
    private String teacherName;

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    @FieldInfo(name = "授课对象")
    @Column(name = "TEACHSTUDENT", length = 255, nullable = true)
    private String teachstudent;

    public void setTeachstudent(String teachstudent) {
        this.teachstudent = teachstudent;
    }

    public String getTeachstudent() {
        return teachstudent;
    }

    @FieldInfo(name = "课程目标")
    @Column(name = "COURSE_TARGET", length = 1024, nullable = true)
    private String courseTarget;

    public void setCourseTarget(String courseTarget) {
        this.courseTarget = courseTarget;
    }

    public String getCourseTarget() {
        return courseTarget;
    }

    @FieldInfo(name = "主要内容")
    @Column(name = "COURSE_CONTENT", length = 1024, nullable = true)
    private String courseContent;

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getCourseContent() {
        return courseContent;
    }

    @FieldInfo(name = "备注")
    @Column(name = "REMARK", length = 1024, nullable = true)
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    @FieldInfo(name = "学校主键")
    @Column(name = "SCHOOL_ID", length = 36, nullable = false)
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

    @FieldInfo(name = "课程状态 0-草稿 1-待审核 2-审核不通过 3-审核通过")
    @Column(name = "STATE")
    private Integer state;
        
    public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public JwTSchoolcourse() {

        super();
        // TODO Auto-generated constructor stub

    }

    public JwTSchoolcourse(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}