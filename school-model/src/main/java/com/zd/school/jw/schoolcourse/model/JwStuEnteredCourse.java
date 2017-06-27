package com.zd.school.jw.schoolcourse.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 学生报名的课程
 */
@Entity
@Table(name = "JW_T_STUENTEREDCOURSE")
@AttributeOverride(name = "uuid", column = @Column(name = "STUCOURSE_ID", length = 36, nullable = false))
public class JwStuEnteredCourse extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "校本课程发布课程信息ID")
	@Column(name = "PCOURSE_ID", length = 36, nullable = true)
	private String pcourseId;

	public String getPcourseId() {
		return pcourseId;
	}

	public void setPcourseId(String pcourseId) {
		this.pcourseId = pcourseId;
	}

	@FieldInfo(name = "学生ID")
	@Column(name = "STUDENT_ID", length = 36, nullable = true)
	private String studentId;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Formula("(SELECT a.PUBLISH_ID FROM JW_T_PUBLISHCOURSE a WHERE a.PCOURSE_ID=PCOURSE_ID)")
	@FieldInfo(name = "校本课程发布ID")
	private String publishId;

	public String getPublishId() {
		return publishId;
	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}

	@Formula("(SELECT a.COURSE_NAME FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=(SELECT b.COURSE_ID FROM JW_T_PUBLISHCOURSE b WHERE b.PCOURSE_ID=PCOURSE_ID))")
	@FieldInfo(name = "课程名称")
	private String courseName;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Formula("(SELECT a.XM FROM SYS_T_USER a WHERE a.USER_ID=STUDENT_ID)")
	@FieldInfo(name = "学生名称")
	private String studentName;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Formula("(SELECT a.COURSE_CONTENT FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=(SELECT b.COURSE_ID FROM JW_T_PUBLISHCOURSE b WHERE b.PCOURSE_ID=PCOURSE_ID))")
	@FieldInfo(name = "主要内容")
	private String courseContent;

	public String getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}

	@FieldInfo(name = "讲师姓名")
	@Formula("(SELECT a.TEACHER_NAME FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID=(SELECT b.COURSE_ID FROM JW_T_PUBLISHCOURSE b WHERE b.PCOURSE_ID=PCOURSE_ID))")
	private String teacherName;

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
