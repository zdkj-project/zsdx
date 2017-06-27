package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseCampus Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 校区信息实体类. date: 2016-08-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_CAMPUS")
@AttributeOverride(name = "uuid", column = @Column(name = "CAMPUS_ID", length = 36, nullable = false))
public class BaseCampus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "学校主键")
    @Column(name = "SCHOOL_ID", length = 36, nullable = true)
    private String schoolId;

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    @FieldInfo(name = "校区编码")
    @Column(name = "CAMPUS_CODE", length = 32, nullable = true)
    private String campusCode;

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }

    public String getCampusCode() {
        return campusCode;
    }

    @FieldInfo(name = "校区名称")
    @Column(name = "CAMPUS_NAME", length = 64, nullable = false)
    private String campusName;

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() {
        return campusName;
    }

    @FieldInfo(name = "校区地址")
    @Column(name = "CAMPUS_ADDR", length = 180, nullable = true)
    private String campusAddr;

    public void setCampusAddr(String campusAddr) {
        this.campusAddr = campusAddr;
    }

    public String getCampusAddr() {
        return campusAddr;
    }

    @FieldInfo(name = "邮政编码")
    @Column(name = "ZIP_CODE", length = 16, nullable = true)
    private String zipCode;

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    @FieldInfo(name = "校区联系电话")
    @Column(name = "CAMPUS_PHONE", length = 30, nullable = true)
    private String campusPhone;

    public void setCampusPhone(String campusPhone) {
        this.campusPhone = campusPhone;
    }

    public String getCampusPhone() {
        return campusPhone;
    }

    @FieldInfo(name = "校区传真电话")
    @Column(name = "CAMPUS_FAX", length = 30, nullable = true)
    private String campusFax;

    public void setCampusFax(String campusFax) {
        this.campusFax = campusFax;
    }

    public String getCampusFax() {
        return campusFax;
    }

    @FieldInfo(name = "校区负责人号")
    @Column(name = "CAMPUS_HEAD", length = 32, nullable = true)
    private String campusHead;

    public void setCampusHead(String campusHead) {
        this.campusHead = campusHead;
    }

    public String getCampusHead() {
        return campusHead;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "schoolName")
    @Formula("(SELECT a.SCHOOL_NAME FROM BASE_T_SCHOOL a WHERE a.SCHOOL_ID=SCHOOL_ID )")
    private String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

}