package com.zd.school.salary.salary.model;

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
 * ClassName: XcSalaryplatitem 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资套账工资项目表(XC_T_SALARYPLATITEM)实体类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "XC_T_SALARYPLATITEM")
@AttributeOverride(name = "uuid", column = @Column(name = "SALARYPLATITEM_ID", length = 36, nullable = false))
public class XcSalaryplatitem extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "工资项ID")
    @Column(name = "SALARYITEM_ID", length = 36, nullable = true)
    private String salaryitemId;
    public void setSalaryitemId(String salaryitemId) {
        this.salaryitemId = salaryitemId;
    }
    public String getSalaryitemId() {
        return salaryitemId;
    }
        
    @FieldInfo(name = "工资套账ID")
    @Column(name = "SALARYPLAT_ID", length = 36, nullable = true)
    private String salaryplatId;
    public void setSalaryplatId(String salaryplatId) {
        this.salaryplatId = salaryplatId;
    }
    public String getSalaryplatId() {
        return salaryplatId;
    }
        
    @FieldInfo(name = "工资项名称")
    @Column(name = "SALARYITEM_NAME", length = 36, nullable = false)
    private String salaryitemName;
    public void setSalaryitemName(String salaryitemName) {
        this.salaryitemName = salaryitemName;
    }
    public String getSalaryitemName() {
        return salaryitemName;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}