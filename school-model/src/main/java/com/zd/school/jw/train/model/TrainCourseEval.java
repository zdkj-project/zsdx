package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by luoyibo on 2017-06-07.
 */
@Entity
@Table(name = "TRAIN_T_COURSEEVAL")
@AttributeOverride(name = "uuid", column = @Column(name = "COURSE_EVAL_ID", length = 36, nullable = false))
public class TrainCourseEval extends BaseEntity implements Serializable {
    @FieldInfo(name = "课程ID")
    @Column(name = "COURSE_ID",length = 36,nullable = true)
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @FieldInfo(name = "班级ID")
    @Column(name = "CLASS_ID",length = 36,nullable = true)
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @FieldInfo(name = "班级名称")
    @Column(name = "CLASS_NAME",length = 128,nullable = true)
    private String className;

    public String getClassName() {
        return className;
    }

    @FieldInfo(name = "上课时间")
    @Column(name = "COURSE_TIME",length = 36,nullable = true)
    private String courseTime;

    public String getCourseTime() {
        return courseTime;
    }

    @FieldInfo(name = "很满意度")
    @Column(name = "VERY_SATISFACTION",columnDefinition = "numeric(10,2) default 0",nullable = true)
    private BigDecimal verySatisfaction;

    public BigDecimal getVerySatisfaction() {
        return verySatisfaction;
    }

    public void setVerySatisfaction(BigDecimal verySatisfaction) {
        this.verySatisfaction = verySatisfaction;
    }

    @FieldInfo(name = "满意度")
    @Column(name = "SATISFACTION",columnDefinition = "numeric(10,2) default 0",nullable = true)
    private BigDecimal satisfaction;

    public BigDecimal getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(BigDecimal satisfaction) {
        this.satisfaction = satisfaction;
    }

    @FieldInfo(name = "排名")
    @Column(name = "RANKING")
    private Integer ranking;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}