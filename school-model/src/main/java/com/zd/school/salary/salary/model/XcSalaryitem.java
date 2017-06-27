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
import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: XcSalaryitem 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资项定义表(XC_T_SALARYITEM)实体类.
 * date: 2016-12-12
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "XC_T_SALARYITEM")
@AttributeOverride(name = "uuid", column = @Column(name = "SALARYITEM_ID", length = 36, nullable = false))
public class XcSalaryitem extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "工资项名称")
    @Column(name = "SALARYITEM_NAME", length = 36, nullable = false)
    private String salaryitemName;
    public void setSalaryitemName(String salaryitemName) {
        this.salaryitemName = salaryitemName;
    }
    public String getSalaryitemName() {
        return salaryitemName;
    }
        
    @FieldInfo(name = "工资项类型")
    @Column(name = "SALARYITEM_TYPE", length = 36, nullable = false)
    private String salaryitemType;
    public void setSalaryitemType(String salaryitemType) {
        this.salaryitemType = salaryitemType;
    }
    public String getSalaryitemType() {
        return salaryitemType;
    }
    
    @FieldInfo(name = "工资项类别")
	@Formula("(SELECT a.ITEM_NAME FROM BASE_T_DICITEM a WHERE a.DIC_ID=(SELECT b.DIC_ID FROM BASE_T_DIC b WHERE b.NODE_TEXT='工资项类型') AND  a.ITEM_CODE=SALARYITEM_TYPE)")
    private String salaryitemTypeName;
	public String getSalaryitemTypeName() {
		return salaryitemTypeName;
	}
	public void setSalaryitemTypeName(String salaryitemTypeName) {
		this.salaryitemTypeName = salaryitemTypeName;
	}
	
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}