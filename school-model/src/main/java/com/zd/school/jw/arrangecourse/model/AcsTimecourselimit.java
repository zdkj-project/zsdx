package com.zd.school.jw.arrangecourse.model;

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
 * ClassName: AcsTimecourselimit 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_TIMECOURSELIMIT)实体类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ACS_T_TIMECOURSELIMIT")
@AttributeOverride(name = "uuid", column = @Column(name = "limitid", length = 36, nullable = false))
public class AcsTimecourselimit extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "classid：保存班级ID")
    @Column(name = "classid", length = 36, nullable = false)
    private String classid;
    public void setClassid(String classid) {
        this.classid = classid;
    }
    public String getClassid() {
        return classid;
    }
    
    @FieldInfo(name = "teacherid：老师的ID")
    @Column(name = "teacherid", length = 36, nullable = false)
    private String teacherid;
    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }
    public String getTeacherid() {
        return teacherid;
    }
    
    @FieldInfo(name = "节数 lessonnum： [11-1n][21-2n]…[m1-mn] n=小节数 m=每周多少天（可设置字典）")
    @Column(name = "lessonnum", length = 10, nullable = false)
    private Integer lessonnum;
    public void setLessonnum(Integer lessonnum) {
        this.lessonnum = lessonnum;
    }
    public Integer getLessonnum() {
        return lessonnum;
    }
        
    @FieldInfo(name = "是否排课 isschedu： 排1不排0")
    @Column(name = "isschedu", length = 1, nullable = true)
    private Boolean isschedu;
    public void setIsschedu(Boolean isschedu) {
        this.isschedu = isschedu;
    }
    public Boolean getIsschedu() {
        return isschedu;
    }
        
    @FieldInfo(name = "课程ID  courseid: 课程表ACS_T_COURSEFEATURE ID")
    @Column(name = "courseid", length = 36, nullable = true)
    private String courseid;
    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }
        
    @FieldInfo(name = "自定义课程：nickname")
    @Column(name = "nickname", length = 50, nullable = true)
    private String nickname;
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }
        
    @FieldInfo(name = "类型limittype: 班级0、老师1 、课程2")
    @Column(name = "limittype", length = 10, nullable = false)
    private Integer limittype;
    public void setLimittype(Integer limittype) {
        this.limittype = limittype;
    }
    public Integer getLimittype() {
        return limittype;
    }
        
    @FieldInfo(name = "是否自定义课 customcor: 1是0不是")
    @Column(name = "customcor", length = 1, nullable = true)
    private Boolean customcor;
    public void setCustomcor(Boolean customcor) {
        this.customcor = customcor;
    }
    public Boolean getCustomcor() {
        return customcor;
    }
    @Transient
    public String teacherName;
    @Transient
    public String CourseName;
    @Transient
    public String teachergh;
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}