package com.zd.school.jw.eduresources.model;

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
 * ClassName: JwGradeteacher Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 年级组长信息实体类. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_GRADETEACHER")
@AttributeOverride(name = "uuid", column = @Column(name = "UUID", length = 36, nullable = false))
public class JwGradeteacher extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "年级ID")
    @Column(name = "GRAI_ID", length = 36, nullable = false)
    private String graiId;

    public void setGraiId(String graiId) {
        this.graiId = graiId;
    }

    public String getGraiId() {
        return graiId;
    }

    @FieldInfo(name = "教职工ID")
    @Column(name = "TTEAC_ID", length = 50, nullable = false)
    private String tteacId;

    public void setTteacId(String tteacId) {
        this.tteacId = tteacId;
    }

    public String getTteacId() {
        return tteacId;
    }

    @FieldInfo(name = "学年")
    @Column(name = "STUDY_YEAH", length = 10, nullable = false)
    private Integer studyYeah;

    public void setStudyYeah(Integer studyYeah) {
        this.studyYeah = studyYeah;
    }

    public Integer getStudyYeah() {
        return studyYeah;
    }

    @FieldInfo(name = "学期")
    @Column(name = "SEMESTER", length = 8, nullable = false)
    private String semester;

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    @FieldInfo(name = "身份")
    @Column(name = "CATEGORY", length = 10, nullable = false)
    private Integer category;

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCategory() {
        return category;
    }

    @FieldInfo(name = "studyYeahname")
    @Column(name = "STUDY_YEAHNAME", length = 64, nullable = false)
    private String studyYeahname;

    public void setStudyYeahname(String studyYeahname) {
        this.studyYeahname = studyYeahname;
    }

    public String getStudyYeahname() {
        return studyYeahname;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */

    @FieldInfo(name = "年级名称")
    @Formula("(SELECT a.GRADE_NAME FROM JW_T_GRADE a WHERE a.GRAI_ID=GRAI_ID )")
    private String gradeName;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    @FieldInfo(name = "老师工号")
    @Formula("(SELECT a.USER_NUMB FROM SYS_T_USER a WHERE a.USER_ID=TTEAC_ID )")
    private String gh;

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    @FieldInfo(name = "老师姓名")
    @Formula("(SELECT a.xm FROM SYS_T_USER a WHERE a.USER_ID=TTEAC_ID )")
    private String xm;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    @FieldInfo(name = "老师性别")
    @Formula("(SELECT a.xbm FROM SYS_T_USER a WHERE a.USER_ID=TTEAC_ID )")
    private String xbm;

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }
}