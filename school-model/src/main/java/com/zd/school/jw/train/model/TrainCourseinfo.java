package com.zd.school.jw.train.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.excel.annotation.MapperCell;

/**
 * ClassName: TrainCourseinfo
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选).
 * Description: 课程信息(TRAIN_T_COURSEINFO)实体类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TRAIN_T_COURSEINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "COURSE_ID", length = 36, nullable = false))
public class TrainCourseinfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "课程分类ID")
    @Column(name = "CATEGORY_ID", length = 36, nullable = false)
    private String categoryId;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @MapperCell(cellName = "课程名称", order = 2, width = 40)
    @FieldInfo(name = "课程名称")
    @Column(name = "COURSE_NAME", length = 128, nullable = false)
    private String courseName;

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    @MapperCell(cellName = "课程编码", order = 1)
    @FieldInfo(name = "课程编码")
    @Column(name = "COURSE_CODE", length = 32, nullable = true)
    private String courseCode;

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @MapperCell(cellName = "课时", order = 7)
    @FieldInfo(name = "课时")
    @Column(name = "PERIOD", length = 5, nullable = false)
    private Integer period = 150;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @MapperCell(cellName = "教学时长", order = 5)
    @FieldInfo(name = "课时时长")
    @Column(name = "PERIOD_TIME", length = 5, nullable = false)
    private Integer periodTime = 150;

    public Integer getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Integer periodTime) {
        this.periodTime = periodTime;
    }

    @MapperCell(cellName = "课程学分", order = 6)
    @FieldInfo(name = "学分")
    @Column(name = "CREDITS", length = 5, nullable = false)
    private Integer credits = 4;

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @FieldInfo(name = "主讲老师ID")
    @Column(name = "MAIN_TEACHER_ID", length = 1024, nullable = true)
    private String mainTeacherId;

    public void setMainTeacherId(String mainTeacherId) {
        this.mainTeacherId = mainTeacherId;
    }

    public String getMainTeacherId() {
        return mainTeacherId;
    }

    @MapperCell(cellName = "主讲教师", order = 8,width = 30)
    @FieldInfo(name = "主讲老师")
    @Column(name = "MAIN_TEACHER_NAME", length = 1024, nullable = true)
    private String mainTeacherName;

    public void setMainTeacherName(String mainTeacherName) {
        this.mainTeacherName = mainTeacherName;
    }

    public String getMainTeacherName() {
        return mainTeacherName;
    }

    @MapperCell(cellName = "课程简介", order = 10,width = 60)
    @FieldInfo(name = "课程简介")
    @Column(name = "COURSE_DESC", columnDefinition = "varchar(max)", nullable = true)
    private String courseDesc;

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    @FieldInfo(name = "授课模式,主要是针对有些课多个讲师分别主讲,有的要多个讲师一起上课 1-单一模式 2-群组模式")
    @Column(name = "COURSE_MODE", length = 5, nullable = false)
    private Integer courseMode = 1;

    public Integer getCourseMode() {
        return courseMode;
    }

    public void setCourseMode(Integer courseMode) {
        this.courseMode = courseMode;
    }

    /**
     * 教学形式字典项
     */
    @FieldInfo(name = "教学形式")
    @Column(name = "TEACH_TYPE", length = 16, nullable = true)
    private String teachType;

    public String getTeachType() {
        return teachType;
    }

    public void setTeachType(String teachType) {
        this.teachType = teachType;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     *
     * @Transient
     * @FieldInfo(name = "")
     * private String field1;
     */
    @MapperCell(cellName = "所属类别", order = 3)
    @FieldInfo(name = "课程分类名称")
    @Formula("(SELECT a.CATEGORY_NAME FROM TRAIN_T_COURSECATEGORY a where a.CATEGORY_ID=CATEGORY_ID)")
    private String categoryName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @FieldInfo(name = "课程分类排序号")
    @Formula("(SELECT a.ORDER_INDEX FROM TRAIN_T_COURSECATEGORY a where a.CATEGORY_ID=CATEGORY_ID)")
    private Integer categoryOrderindex;

    public Integer getCategoryOrderindex() {
        return categoryOrderindex;
    }

    public void setCategoryOrderindex(Integer categoryOrderindex) {
        this.categoryOrderindex = categoryOrderindex;
    }

    @FieldInfo(name = "课程分类编码")
    @Formula("(SELECT a.CATEGORY_CODE FROM TRAIN_T_COURSECATEGORY a where a.CATEGORY_ID=CATEGORY_ID)")
    private String categoryCode;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @MapperCell(cellName = "手机号码", order = 9,width = 40)
    @FieldInfo(name = "主讲教师手机")
//    @Formula("(SELECT a.MOBILE_PHONE FROM TRAIN_T_TEACHER a WHERE a.TEA_ID=MAIN_TEACHER_ID)")
    @Formula("(SELECT dbo.TRAIN_F_GETCOURSEMAINTEACHERMOBILE(MAIN_TEACHER_ID))")
    private String mobilePhone;

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    @MapperCell(cellName = "教学形式", order = 4)
    @FieldInfo(name = "教学形式名称")
    @Formula("(SELECT a.ITEM_NAME FROM dbo.BASE_T_DICITEM a WHERE a.isDelete=0 and a.DIC_ID=(SELECT b.DIC_ID FROM BASE_T_DIC b WHERE b.DIC_CODE='TEACHTYPE') AND a.ITEM_CODE=TEACH_TYPE)")
    private String teachTypeName;

    public String getTeachTypeName() {
        return teachTypeName;
    }

    public void setTeachTypeName(String teachTypeName) {
        this.teachTypeName = teachTypeName;
    }
}