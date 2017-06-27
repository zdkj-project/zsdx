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
 * ClassName: JwInvigilate 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_INVIGILATE实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_INVIGILATE")
@AttributeOverride(name = "uuid", column = @Column(name = "TTEAC_ID", length = 36, nullable = false))
public class JwInvigilate extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "学校名称")
    @Column(name = "SCHOOL", length = 36, nullable = true)
    private String school;
    public void setSchool(String school) {
        this.school = school;
    }
    public String getSchool() {
        return school;
    }
        
    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 36, nullable = false)
    private String xm;
    public void setXm(String xm) {
        this.xm = xm;
    }
    public String getXm() {
        return xm;
    }
        
    @FieldInfo(name = "照片")
    @Column(name = "ZP", length = 200, nullable = true)
    private String zp;
    public void setZp(String zp) {
        this.zp = zp;
    }
    public String getZp() {
        return zp;
    }
        
    @FieldInfo(name = "联系电话")
    @Column(name = "BH", length = 30, nullable = true)
    private String bh;
    public void setBh(String bh) {
        this.bh = bh;
    }
    public String getBh() {
        return bh;
    }
        
    @FieldInfo(name = "lxdh")
    @Column(name = "LXDH", length = 30, nullable = true)
    private String lxdh;
    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }
    public String getLxdh() {
        return lxdh;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}