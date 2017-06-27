package com.zd.school.salary.jxsalary.model;

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
 * ClassName: XcJxbook 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOK)实体类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "XC_T_JXBOOK")
@AttributeOverride(name = "uuid", column = @Column(name = "JXBOOK_ID", length = 36, nullable = false))
public class XcJxbook extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "绩效台账名称")
    @Column(name = "JXBOOK_NAME", length = 36, nullable = true)    
    private String jxbookName;
        
    public String getJxbookName() {
		return jxbookName;
	}
	public void setJxbookName(String jxbookName) {
		this.jxbookName = jxbookName;
	}

	@FieldInfo(name = "绩效套账ID")
    @Column(name = "JXPLART_ID", length = 36, nullable = true)
    private String jxplartId;
    public void setJxplartId(String jxplartId) {
        this.jxplartId = jxplartId;
    }
    public String getJxplartId() {
        return jxplartId;
    }
        
    @FieldInfo(name = "绩效年份")
    @Column(name = "XC_YEAR", length = 10, nullable = true)
    private Integer xcYear;
    public void setXcYear(Integer xcYear) {
        this.xcYear = xcYear;
    }
    public Integer getXcYear() {
        return xcYear;
    }
        
    @FieldInfo(name = "绩效月份")
    @Column(name = "XC_MONTH", length = 10, nullable = true)
    private Integer xcMonth;
    public void setXcMonth(Integer xcMonth) {
        this.xcMonth = xcMonth;
    }
    public Integer getXcMonth() {
        return xcMonth;
    }
    
    @FieldInfo(name = "是否发布,0=未发布,1=已发布")
	@Column(name = "ISPUSHED", length = 10, nullable = true)
	private Integer isPushed=0;

	public Integer getIsPushed() {
		return isPushed;
	}

	public void setIsPushed(Integer isPushed) {
		this.isPushed = isPushed;
	}
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}