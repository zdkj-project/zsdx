package com.zd.school.jw.eduresources.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: JwGradeclassteacher Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 年级组长信息(JW_T_GRADECLASSTEACHER)实体类. date: 2016-09-20
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "JW_T_GRADECLASSTEACHER")
//@AttributeOverride(name = "uuid", column = @Column(name = "UUID", length = 36, nullable = false))
public class JwGradeclassteacher extends BaseEntity implements Serializable {
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
    @Column(name = "TTEAC_ID", length = 36, nullable = false)
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

    @FieldInfo(name = "studyYeahname")
    @Column(name = "STUDY_YEAHNAME", length = 64, nullable = false)
    private String studyYeahname;

    public void setStudyYeahname(String studyYeahname) {
        this.studyYeahname = studyYeahname;
    }

    public String getStudyYeahname() {
        return studyYeahname;
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

    @FieldInfo(name = "身份 0-正年级组长 1-副年级组长 2-班主任 3-副班主任")
    @Column(name = "CATEGORY", length = 10, nullable = false)
    private Integer category;

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCategory() {
        return category;
    }

    @FieldInfo(name = "教师类型 0-年级组长 1-班主任 ")
    @Column(name = "TEA_TYPE", length = 4, nullable = true)
    private String teaType;

    public String getTeaType() {
        return teaType;
    }

    public void setTeaType(String teaType) {
        this.teaType = teaType;
    }

    public JwGradeclassteacher() {

        super();
        // TODO Auto-generated constructor stub

    }

    public JwGradeclassteacher(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "班级名称")
    @Formula("(SELECT a.NODE_TEXT FROM BASE_T_ORG a WHERE a.DEPT_ID=GRAI_ID)")
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @FieldInfo(name = "老师工号")
    @Formula("(SELECT a.USER_NUMB FROM SYS_T_USER a WHERE a.USER_ID=TTEAC_ID )")
    private String userNumb;

    public String getUserNumb() {
        return userNumb;
    }

    public void setUserNumb(String userNumb) {
        this.userNumb = userNumb;
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

    @FieldInfo(name = "老师岗位")
    @Formula("(SELECT a.JOB_NAME FROM SYS_T_USER a WHERE a.USER_ID=TTEAC_ID )")
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}