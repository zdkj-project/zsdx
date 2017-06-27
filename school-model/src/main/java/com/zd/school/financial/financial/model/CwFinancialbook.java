package com.zd.school.financial.financial.model;

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
 * ClassName: CwFinancialbook 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOK)实体类.
 * date: 2017-01-16
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CW_T_FINANCIALBOOK")
@AttributeOverride(name = "uuid", column = @Column(name = "FINANCIALBOOK_ID", length = 36, nullable = false))
public class CwFinancialbook extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "xcYear")
    @Column(name = "XC_YEAR", length = 10, nullable = true)
    private Integer xcYear;
    public void setXcYear(Integer xcYear) {
        this.xcYear = xcYear;
    }
    public Integer getXcYear() {
        return xcYear;
    }
        
    @FieldInfo(name = "xcMonth")
    @Column(name = "XC_MONTH", length = 10, nullable = true)
    private Integer xcMonth;
    public void setXcMonth(Integer xcMonth) {
        this.xcMonth = xcMonth;
    }
    public Integer getXcMonth() {
        return xcMonth;
    }
        
    @FieldInfo(name = "financialName")
    @Column(name = "FINANCIAL_NAME", length = 36, nullable = true)
    private String financialName;
    public void setFinancialName(String financialName) {
        this.financialName = financialName;
    }
    public String getFinancialName() {
        return financialName;
    }
        
    @FieldInfo(name = "ispushed")
    @Column(name = "ISPUSHED", length = 10, nullable = true)
    private Integer ispushed=0;
    public void setIspushed(Integer ispushed) {
        this.ispushed = ispushed;
    }
    public Integer getIspushed() {
        return ispushed;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}