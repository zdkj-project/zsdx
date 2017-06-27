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
 * ClassName: AcsTeacherfeature 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_TEACHERFEATURE)实体类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ACS_T_TEACHERFEATURE")
@AttributeOverride(name = "uuid", column = @Column(name = "featureid", length = 36, nullable = false))
public class AcsTeacherfeature extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = " TEA_T_TEACHERBASE   USER_ID")
    @Column(name = "teacherid", length = 36, nullable = false)
    private String teacherid;
    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }
    public String getTacherid() {
        return teacherid;
    }
        
    @FieldInfo(name = "周课时分布 zksfb：较均匀0 较集中1")
    @Column(name = "zksfb", length = 1, nullable = true)
    private Boolean zksfb;
    public void setZksfb(Boolean zksfb) {
        this.zksfb = zksfb;
    }
    public Boolean getZksfb() {
        return zksfb;
    }
        
    @FieldInfo(name = "任课分布 rkfb：较连续 较分散")
    @Column(name = "rkfb", length = 1, nullable = true)
    private Boolean rkfb;
    public void setRkfb(Boolean rkfb) {
        this.rkfb = rkfb;
    }
    public Boolean getRkfb() {
        return rkfb;
    }
        
    @FieldInfo(name = "两个连课 lglk：排一天内0 排不同天1")
    @Column(name = "lglk", length = 1, nullable = true)
    private Boolean lglk;
    public void setLglk(Boolean lglk) {
        this.lglk = lglk;
    }
    public Boolean getLglk() {
        return lglk;
    }
        
    @FieldInfo(name = "日课限制 rkxz：不限制、 一天最多允许上n节 n={1 2 3 4…}")
    @Column(name = "rkxz", length = 10, nullable = true)
    private Integer rkxz;
    public void setRkxz(Integer rkxz) {
        this.rkxz = rkxz;
    }
    public Integer getRkxz() {
        return rkxz;
    }
        
    @FieldInfo(name = "上午限制 swxz： 不限制0、 上午最多允许上n节 n={1 2 3 4…}")
    @Column(name = "swxz", length = 10, nullable = true)
    private Integer swxz;
    public void setSwxz(Integer swxz) {
        this.swxz = swxz;
    }
    public Integer getSwxz() {
        return swxz;
    }
        
    @FieldInfo(name = "下午限制 xwxz：不限制0、 下午最多允许上n节 n={1 2 3 4…}")
    @Column(name = "xwxz", length = 10, nullable = true)
    private Integer xwxz;
    public void setXwxz(Integer xwxz) {
        this.xwxz = xwxz;
    }
    public Integer getXwxz() {
        return xwxz;
    }
        
    @FieldInfo(name = "互斥老师 fcls：互斥的两个老师在同一时间只有一个老师有课 ")
    @Column(name = "fcls", length = 36, nullable = true)
    private String fcls;
    public void setFcls(String fcls) {
        this.fcls = fcls;
    }
    public String getFcls() {
        return fcls;
    }
        
    @FieldInfo(name = "优先安排 yxap：正常0，优先安排1")
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