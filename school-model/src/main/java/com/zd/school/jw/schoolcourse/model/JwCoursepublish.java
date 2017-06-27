package com.zd.school.jw.schoolcourse.model;

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
import com.zd.core.util.DateSerializer;
import com.zd.core.util.DateTimeSerializer;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwCoursepublish 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校本课程发布信息(JW_T_COURSEPUBLISH)实体类.
 * date: 2016-11-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_COURSEPUBLISH")
@AttributeOverride(name = "uuid", column = @Column(name = "PUBLISH_ID", length = 36, nullable = false))
public class JwCoursepublish extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "学年")
    @Column(name = "STUDY_YEAR", length = 10, nullable = true)
    private Integer studyYear;
    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }
    public Integer getStudyYear() {
        return studyYear;
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

    @FieldInfo(name = "studyYeahname")
    @Column(name = "STUDY_YEAHNAME", length = 64, nullable = false)
    private String studyYeahname;  

    public void setStudyYeahname(String studyYeahname) {
        this.studyYeahname = studyYeahname;
    }

    public String getStudyYeahname() {
        return studyYeahname;
    }
    
    @FieldInfo(name = "选课开始日期")
    @Column(name = "BEGIN_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    private Date beginDate;
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getBeginDate() {
        return beginDate;
    }
        
    @FieldInfo(name = "选课结束日期")
    @Column(name = "END_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    private Date endDate;
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getEndDate() {
        return endDate;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}