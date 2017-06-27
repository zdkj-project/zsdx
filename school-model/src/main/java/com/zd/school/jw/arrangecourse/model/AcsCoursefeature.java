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
 * ClassName: AcsCoursefeature 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_COURSEFEATURE)实体类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ACS_T_COURSEFEATURE")
@AttributeOverride(name = "uuid", column = @Column(name = "featureid", length = 36, nullable = false))
public class AcsCoursefeature extends BaseEntity implements Serializable{
	public AcsCoursefeature(){
	}
    public AcsCoursefeature AcsCoursefeature_Init() {
		this.assign = 0;
		this.apzw = 0;
		this.maincourse = false;
		this.science = false;
		this.cdxz = false;
		this.corplan = 0;
		this.zjs = 0;
		this.yxap = false;
		return this;
	}
	private static final long serialVersionUID = 1L;
    
    public void initData(){
    	
    }
    @FieldInfo(name = "JW_T_BASECOURSE.BASECOURSE_ID")
    @Column(name = "courseid", length = 36, nullable = false)
    private String courseid;
    public void setCourseid(String courssid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }
        
    @FieldInfo(name = "字典任课学段代码初中3、高中4 ")
    @Column(name = "stage", length = 32, nullable = true)
    private String stage;
    public void setStage(String stage) {
        this.stage = stage;
    }
    public String getStage() {
        return stage;
    }
        
    @FieldInfo(name = "JW_T_GRADE.GRADE_CODE")
    @Column(name = "grade", length = 32, nullable = false)
    private String grade;
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }
        
    @FieldInfo(name = "分配 无所谓0、上午多1、下午多2、一样多3")
    @Column(name = "assign", length = 10, nullable = false)
    private Integer assign;
    public void setAssign(Integer assign) {
        this.assign = assign;
    }
    public Integer getAssign() {
        return assign;
    }
        
    @FieldInfo(name = "安排早晚apzw：无所谓0  、尽量早些1、尽量晚些2")
    @Column(name = "apzw", length = 10, nullable = false)
    private Integer apzw;
    public void setApzw(Integer apzw) {
        this.apzw = apzw;
    }
    public Integer getApzw() {
        return apzw;
    }
        
    @FieldInfo(name = "主课maincourse： 不是0  是1")
    @Column(name = "maincourse", length = 1, nullable = false)
    private Boolean maincourse;
    public void setMaincourse(Boolean maincourse) {
        this.maincourse = maincourse;
    }
    public Boolean getMaincourse() {
        return maincourse;
    }
        
    @FieldInfo(name = "理科science： 不是0  是1")
    @Column(name = "science", length = 1, nullable = true)
    private Boolean science;
    public void setScience(Boolean science) {
        this.science = science;
    }
    public Boolean getScience() {
        return science;
    }
        
    @FieldInfo(name = "场地限制cdxz：不要求0，全校平均1（体育课才有此设置全校平均）")
    @Column(name = "cdxz", length = 1, nullable = true)
    private Boolean cdxz;
    public void setCdxz(Boolean cdxz) {
        this.cdxz = cdxz;
    }
    public Boolean getCdxz() {
        return cdxz;
    }
        
    @FieldInfo(name = "进度corplan：各班独立0  2班一致1、3班一致2(和4.2进度设置功能重合)")
    @Column(name = "corplan", length = 10, nullable = true)
    private Integer corplan;
    public void setCorplan(Integer corplan) {
        this.corplan = corplan;
    }
    public Integer getCorplan() {
        return corplan;
    }
        
    @FieldInfo(name = "周节数: zjs一周上多少节课")
    @Column(name = "zjs", length = 10, nullable = false)
    private Integer zjs;
    public void setZjs(Integer zjs) {
        this.zjs = zjs;
    }
    public Integer getZjs() {
        return zjs;
    }
        
    @FieldInfo(name = "优先安排yxap：:正常0，优先安排1")
    @Column(name = "yxap", length = 1, nullable = true)
    private Boolean yxap;
    public void setYxap(Boolean yxap) {
        this.yxap = yxap;
    }
    public Boolean getYxap() {
        return yxap;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}