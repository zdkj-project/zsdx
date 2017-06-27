package com.zd.school.jw.train.model;

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
 * ClassName: TrainCourseevaldetail 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程评价明细 (TRAIN_T_COURSEEVALDETAIL)实体类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_COURSEEVALDETAIL")
@AttributeOverride(name = "uuid", column = @Column(name = "COURSEEVALDETAIL_ID", length = 36, nullable = false))
public class TrainCourseevaldetail extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "课程安排ID")
    @Column(name = "CLASS_SCHEDULE_ID", length = 36, nullable = false)
    private String classScheduleId;
    public void setClassScheduleId(String classScheduleId) {
        this.classScheduleId = classScheduleId;
    }
    public String getClassScheduleId() {
        return classScheduleId;
    }
        
    @FieldInfo(name = "课程ID")
    @Column(name = "COURSE_ID", length = 36, nullable = true)
    private String courseId;
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public String getCourseId() {
        return courseId;
    }
        
    @FieldInfo(name = "课程名称")
    @Column(name = "COURSE_NAME", length = 128, nullable = false)
    private String courseName;
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return courseName;
    }
        
    @FieldInfo(name = "指标ID")
    @Column(name = "INDICATOR_ID", length = 36, nullable = false)
    private String indicatorId;
    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }
    public String getIndicatorId() {
        return indicatorId;
    }
        
    @FieldInfo(name = "指标名称")
    @Column(name = "INDICATOR_NAME", length = 36, nullable = false)
    private String indicatorName;
    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }
    public String getIndicatorName() {
        return indicatorName;
    }
        
    @FieldInfo(name = "标准ID")
    @Column(name = "STAND_ID", length = 36, nullable = false)
    private String standId;
    public void setStandId(String standId) {
        this.standId = standId;
    }
    public String getStandId() {
        return standId;
    }
        
    @FieldInfo(name = "标准名称")
    @Column(name = "INDICATOR_STAND", length = 256, nullable = false)
    private String indicatorStand;
    public void setIndicatorStand(String indicatorStand) {
        this.indicatorStand = indicatorStand;
    }
    public String getIndicatorStand() {
        return indicatorStand;
    }
        
    @FieldInfo(name = "非常满意人数")
    @Column(name = "VERY_SATISFACTIONCOUNT", length = 10, nullable = false)
    private Integer verySatisfactioncount;
    public void setVerySatisfactioncount(Integer verySatisfactioncount) {
        this.verySatisfactioncount = verySatisfactioncount;
    }
    public Integer getVerySatisfactioncount() {
        return verySatisfactioncount;
    }
        
    @FieldInfo(name = "满意人数")
    @Column(name = "SATISFACTIONCOUNT", length = 10, nullable = false)
    private Integer satisfactioncount;
    public void setSatisfactioncount(Integer satisfactioncount) {
        this.satisfactioncount = satisfactioncount;
    }
    public Integer getSatisfactioncount() {
        return satisfactioncount;
    }
        
    @FieldInfo(name = "基本满意人数")
    @Column(name = "BAS_SATISFACTIONCOUNT", length = 10, nullable = false)
    private Integer basSatisfactioncount;
    public void setBasSatisfactioncount(Integer basSatisfactioncount) {
        this.basSatisfactioncount = basSatisfactioncount;
    }
    public Integer getBasSatisfactioncount() {
        return basSatisfactioncount;
    }
        
    @FieldInfo(name = "不满意人数")
    @Column(name = "NO_SATISFACTIONCOUNT", length = 10, nullable = false)
    private Integer noSatisfactioncount;
    public void setNoSatisfactioncount(Integer noSatisfactioncount) {
        this.noSatisfactioncount = noSatisfactioncount;
    }
    public Integer getNoSatisfactioncount() {
        return noSatisfactioncount;
    }
        
    @FieldInfo(name = "意见与建议")
    @Column(name = "ADVISE", length = 2048, nullable = true)
    private String advise;
    public void setAdvise(String advise) {
        this.advise = advise;
    }
    public String getAdvise() {
        return advise;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}