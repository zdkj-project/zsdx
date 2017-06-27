package com.zd.school.student.studentclass.model;

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
 * ClassName: JwClassstudent Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学生分班信息(JW_T_CLASSSTUDENT)实体类. date: 2016-08-25
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_CLASSSTUDENT")
@AttributeOverride(name = "uuid", column = @Column(name = "CSTUDENT_ID", length = 36, nullable = false) )
public class JwClassstudent extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "班级ID")
	@Column(name = "CLAI_ID", length = 36, nullable = true)
	private String claiId;

	@FieldInfo(name = "学生ID")
	@Column(name = "STUDENT_ID", length = 36, nullable = true)
	private String studentId;

	@FieldInfo(name = "学年")
	@Column(name = "STUDY_YEAH", length = 10, nullable = false)
	private String studyYeah;

	@FieldInfo(name = "学期")
	@Column(name = "SEMESTER", length = 8, nullable = false)
	private String semester;

	@FieldInfo(name = "status")
	@Column(name = "STATUS", length = 8, nullable = true)
	private String status="0";

	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */
	@FieldInfo(name = "班级名称")
	@Formula("(SELECT a.CLASS_NAME FROM JW_T_GRADECLASS a WHERE a.CLAI_ID=CLAI_ID)")
	private String className;

	@FieldInfo(name = "学段标识")
	@Formula("(SELECT B.GRADE_CODE FROM dbo.JW_T_GRADECLASS A JOIN dbo.JW_T_GRADE B "
			+ "ON A.GRAI_ID=B.GRAI_ID WHERE A.CLAI_ID=CLAI_ID)")
	private String gradeCode;

	@FieldInfo(name = "学号")
	@Formula("(SELECT A.USER_NUMB FROM dbo.SYS_T_USER A WHERE A.USER_ID=STUDENT_ID)")
	private String userNumb;

	@Formula("(SELECT A.XM FROM dbo.SYS_T_USER A WHERE A.USER_ID=STUDENT_ID)")
	@FieldInfo(name = "姓名")
	private String xm;

	@FieldInfo(name = "性别码GB/T 2261.1")
	@Formula("(SELECT A.XBM FROM dbo.SYS_T_USER A WHERE A.USER_ID=STUDENT_ID)")
	private String xbm;
	
	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getClaiId() {
		return claiId;
	}

	public void setClaiId(String claiId) {
		this.claiId = claiId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXbm() {
		return xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStudyYeah() {
		return studyYeah;
	}

	public void setStudyYeah(String studyYeah) {
		this.studyYeah = studyYeah;
	}

	public String getUserNumb() {
		return userNumb;
	}

	public void setUserNumb(String userNumb) {
		this.userNumb = userNumb;
	}

}