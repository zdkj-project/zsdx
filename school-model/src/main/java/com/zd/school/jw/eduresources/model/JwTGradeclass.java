package com.zd.school.jw.eduresources.model;

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
 * ClassName: JwTGradeclass Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学校班级信息实体类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_GRADECLASS")
@AttributeOverride(name = "uuid", column = @Column(name = "CLAI_ID", length = 36, nullable = false))
public class JwTGradeclass extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "年级ID")
    @Column(name = "GRAI_ID", length = 100, nullable = true)
    private String graiId;

    public String getGraiId() {
        return graiId;
    }

    public void setGraiId(String graiId) {
        this.graiId = graiId;
    }

    @FieldInfo(name = "班级类型")
    @Column(name = "CLASS_TYPE", length = 100, nullable = true)
    private String classType;

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassType() {
        return classType;
    }

    @FieldInfo(name = "文理类型")
    @Column(name = "ARTS_SCIENCES", length = 16, nullable = true)
    private String artsSciences;

    public void setArtsSciences(String artsSciences) {
        this.artsSciences = artsSciences;
    }

    public String getArtsSciences() {
        return artsSciences;
    }

    @FieldInfo(name = "少数民族双语教学班")
    @Column(name = "MINORITY_BILINGUAL", length = 10, nullable = true)
    private String minorityBilingual;

    public void setMinorityBilingual(String minorityBilingual) {
        this.minorityBilingual = minorityBilingual;
    }

    public String getMinorityBilingual() {
        return minorityBilingual;
    }

    @FieldInfo(name = "双语教学模式")
    @Column(name = "SYJXMS", length = 100, nullable = true)
    private String syjxms;

    public void setSyjxms(String syjxms) {
        this.syjxms = syjxms;
    }

    public String getSyjxms() {
        return syjxms;
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

    @FieldInfo(name = "班主任ID")
    @Column(name = "TEACHER_ID", length = 36, nullable = true)
    private String teacherId;

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    @FieldInfo(name = "建班年月")
    @Column(name = "FOUND_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)

    private Date foundDate;

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }

    @FieldInfo(name = "年度")
    @Column(name = "CLASS_YEAR", length = 50, nullable = true)
    private String classYear;

    public String getClassYear() {
        return classYear;
    }

    public void setClassYear(String classYear) {
        this.classYear = classYear;
    }

    @FieldInfo(name = "毕业日期")
    @Column(name = "GRADUATE_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date graduateDate;

    public void setGraduateDate(Date graduateDate) {
        this.graduateDate = graduateDate;
    }

    public Date getGraduateDate() {
        return graduateDate;
    }
	
    @FieldInfo(name = "年级")
    @Column(name = "NJ", length = 32, nullable = true)
    private String nj;

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    @FieldInfo(name = "班训")
    @Column(name = "CLASS_MOTTO", length = 36, nullable = true)
    private String classMotto;
    
    public String getClassMotto() {
		return classMotto;
	}

	public void setClassMotto(String classMotto) {
		this.classMotto = classMotto;
	}

	public JwTGradeclass() {

        super();
        // TODO Auto-generated constructor stub

    }

    public JwTGradeclass(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "年级名称")
    @Formula("(SELECT a.GRADE_NAME from JW_T_GRADE a where a.GRAI_ID=GRAI_ID)")
    private String gradeName;

    @FieldInfo(name = "学段")
    @Formula("(SELECT a.SECTION_CODE from JW_T_GRADE a where a.GRAI_ID=GRAI_ID)")
    private String sectionCode;
    
    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradeName() {
        return gradeName;
    }

    @Formula("(SELECT A.GRADE_CODE FROM dbo.JW_T_GRADE A WHERE A.GRAI_ID=GRAI_ID)")
    @FieldInfo(name = "年级编码")
    private String gradeCode;

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeCode() {
        return gradeCode;
    }

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
}