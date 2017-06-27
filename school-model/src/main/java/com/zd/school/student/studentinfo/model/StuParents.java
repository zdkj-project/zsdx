package com.zd.school.student.studentinfo.model;

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
 * ClassName: StuParents 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生家长信息实体类.
 * date: 2016-08-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "STU_T_PARENTS")
@AttributeOverride(name = "uuid", column = @Column(name = "PARENT_ID", length = 36, nullable = false))
public class StuParents extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "学生ID")
    @Column(name = "STU_ID", length = 36, nullable = true)
    private String stuId;
    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
    public String getStuId() {
        return stuId;
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
        
    @FieldInfo(name = "性别码")
    @Column(name = "XBM", length = 10, nullable = true)
    private String xbm;
    public void setXbm(String xbm) {
        this.xbm = xbm;
    }
    public String getXbm() {
        return xbm;
    }
        
    @FieldInfo(name = "身份证件类型码")
    @Column(name = "SFZJLXM", length = 10, nullable = true)
    private String sfzjlxm;
    public void setSfzjlxm(String sfzjlxm) {
        this.sfzjlxm = sfzjlxm;
    }
    public String getSfzjlxm() {
        return sfzjlxm;
    }
        
    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 20, nullable = true)
    private String sfzjh;
    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }
    public String getSfzjh() {
        return sfzjh;
    }
        
    @FieldInfo(name = "与学生关系")
    @Column(name = "XSGXM", length = 16, nullable = false)
    private String xsgxm;
    public void setXsgxm(String xsgxm) {
        this.xsgxm = xsgxm;
    }
    public String getXsgxm() {
        return xsgxm;
    }
        
    @FieldInfo(name = "电子邮箱")
    @Column(name = "E_MAIL", length = 128, nullable = true)
    private String eMail;
    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
    public String getEMail() {
        return eMail;
    }
        
    @FieldInfo(name = "移动电话")
    @Column(name = "MOBILE", length = 64, nullable = true)
    private String mobile;
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }
        
    @FieldInfo(name = "固定电话")
    @Column(name = "TELPHONE", length = 64, nullable = true)
    private String telphone;
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
    public String getTelphone() {
        return telphone;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}