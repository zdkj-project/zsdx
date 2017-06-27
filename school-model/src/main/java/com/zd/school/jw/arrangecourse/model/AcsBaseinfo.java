package com.zd.school.jw.arrangecourse.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: AcsBaseinfo 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_BASEINFO)实体类.
 * date: 2016-11-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ACS_T_BASEINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "acsid", length = 36, nullable = false))
public class AcsBaseinfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "名称")
    @Column(name = "pkname", length = 36, nullable = false)
    private String pkname;
    public void setPkname(String pkname) {
        this.pkname = pkname;
    }
    public String getPkname() {
        return pkname;
    }
        
    @FieldInfo(name = "每周多少天")
    @Column(name = "weekday", length = 10, nullable = false)
    private Integer weekday;
    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }
    public Integer getWeekday() {
        return weekday;
    }
        
    @FieldInfo(name = "每天上午节数")
    @Column(name = "numam", length = 10, nullable = false)
    private Integer numam;
    public void setNumam(Integer numam) {
        this.numam = numam;
    }
    public Integer getNumam() {
        return numam;
    }
        
    @FieldInfo(name = "小节数")
    @Column(name = "daynum", length = 10, nullable = false)
    private Integer daynum;
    public void setDaynum(Integer daynum) {
        this.daynum = daynum;
    }
    public Integer getDaynum() {
        return daynum;
    }
        
    @FieldInfo(name = "每天上午节数")
    @Column(name = "numpm", length = 10, nullable = false)
    private Integer numpm;
    public void setNumpm(Integer numpm) {
        this.numpm = numpm;
    }
    public Integer getNumpm() {
        return numpm;
    }
        
    @FieldInfo(name = "全学期周数")
    @Column(name = "fullweek", length = 10, nullable = false)
    private Integer fullweek;
    public void setFullweek(Integer fullweek) {
        this.fullweek = fullweek;
    }
    public Integer getFullweek() {
        return fullweek;
    }
        
    @FieldInfo(name = "学段")
    @Column(name = "stage", length = 10, nullable = false)
    private String stage;
    public void setStage(String stage) {
        this.stage = stage;
    }
    public String getStage() {
        return stage;
    }
        
    @FieldInfo(name = "班级连课在一块上完")
    @Column(name = "lkzyksw", length = 1, nullable = false)
    private Boolean lkzyksw;
    public void setLkzyksw(Boolean lkzyksw) {
        this.lkzyksw = lkzyksw;
    }
    public Boolean getLkzyksw() {
        return lkzyksw;
    }
        
    @FieldInfo(name = "上次排课时间")
    @Column(name = "pktime", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date pktime;
    public void setPktime(Date pktime) {
        this.pktime = pktime;
    }
    public Date getPktime() {
        return pktime;
    }
    
    @FieldInfo(name = "学年")
    @Column(name = "schoolyear", length = 8, nullable = false)
    private String schoolyear;
    public void setSchoolyear(String schoolyear) {
        this.schoolyear = schoolyear;
    }
    public String getSchoolyear() {
        return schoolyear;
    }
    
    @FieldInfo(name = "学期")
    @Column(name = "schoolterm", length = 8, nullable = false)
    private String schoolterm;
    public void setSchoolterm(String schoolterm) {
        this.schoolterm = schoolterm;
    }
    public String getSchoolterm() {
        return schoolterm;
    }
    
    @Transient
    public int repeatScheduleNum=0;

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}