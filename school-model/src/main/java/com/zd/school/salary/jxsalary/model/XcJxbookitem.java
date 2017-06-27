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
import com.zd.school.excel.annotation.MapperCell;

/**
 * 
 * ClassName: XcJxbookitem 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOKITEM)实体类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "XC_T_JXBOOKITEM")
@AttributeOverride(name = "uuid", column = @Column(name = "JXSALARY_ID", length = 36, nullable = false))
public class XcJxbookitem extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "台账ID")
    @Column(name = "JXBOOK_ID", length = 36, nullable = true)
    private String jxbookId;
    public void setJxbookId(String jxbookId) {
        this.jxbookId = jxbookId;
    }
    public String getJxbookId() {
        return jxbookId;
    }
        
    @FieldInfo(name = "人员ID")
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
        
    @FieldInfo(name = "人员姓名")
    @Column(name = "XM", length = 36, nullable = true)
    private String xm;
    public void setXm(String xm) {
        this.xm = xm;
    }
    public String getXm() {
        return xm;
    }
        
    @FieldInfo(name = "银行账号")
    @Column(name = "BANK_NUMB", length = 36, nullable = true)
    private String bankNumb;
    public void setBankNumb(String bankNumb) {
        this.bankNumb = bankNumb;
    }
    public String getBankNumb() {
        return bankNumb;
    }
    
    @FieldInfo(name = "备注字段")
    @Column(name = "remark", length = 36, nullable = true)
    private String remark;
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
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

    @MapperCell(cellName="职务工资",order=5)
    @FieldInfo(name = "职务工资")
    @Column(name = "GZ0", length = 18, nullable = true)
    private BigDecimal gz0;
    public void setGz0(BigDecimal gz0) {
        this.gz0 = gz0;
    }
    public BigDecimal getGz0() {
        return gz0;
    }
    
    @MapperCell(cellName="级别工资",order=6)
    @FieldInfo(name = "级别工资")
    @Column(name = "GZ1", length = 18, nullable = true)
    private BigDecimal gz1;
    public void setGz1(BigDecimal gz1) {
        this.gz1 = gz1;
    }
    public BigDecimal getGz1() {
        return gz1;
    }
    
    @MapperCell(cellName="特区津贴",order=7)
    @FieldInfo(name = "特区津贴")
    @Column(name = "GZ2", length = 18, nullable = true)
    private BigDecimal gz2;
    public void setGz2(BigDecimal gz2) {
        this.gz2 = gz2;
    }
    public BigDecimal getGz2() {
        return gz2;
    }
    
    @MapperCell(cellName="岗位津贴",order=8)
    @FieldInfo(name = "岗位津贴")
    @Column(name = "GZ3", length = 18, nullable = true)
    private BigDecimal gz3;
    public void setGz3(BigDecimal gz3) {
        this.gz3 = gz3;
    }
    public BigDecimal getGz3() {
        return gz3;
    }
    
    @MapperCell(cellName="见习工资",order=1)
    @FieldInfo(name = "见习工资")
    @Column(name = "GZ4", length = 18, nullable = true)
    private BigDecimal gz4;
    public void setGz4(BigDecimal gz4) {
        this.gz4 = gz4;
    }
    public BigDecimal getGz4() {
        return gz4;
    }
    
    @MapperCell(cellName="小计1",order=1)    
    @FieldInfo(name = "小计1")
    @Column(name = "GZ5", length = 18, nullable = true)
    private BigDecimal gz5;
    public void setGz5(BigDecimal gz5) {
        this.gz5 = gz5;
    }
    public BigDecimal getGz5() {
        return gz5;
    }
    
    @MapperCell(cellName="特津10%",order=1)    
    @FieldInfo(name = "特津10%")
    @Column(name = "GZ6", length = 18, nullable = true)
    private BigDecimal gz6;
    public void setGz6(BigDecimal gz6) {
        this.gz6 = gz6;
    }
    public BigDecimal getGz6() {
        return gz6;
    }
    
    @MapperCell(cellName="教龄",order=1)    
    @FieldInfo(name = "教龄")
    @Column(name = "GZ7", length = 18, nullable = true)
    private BigDecimal gz7;
    public void setGz7(BigDecimal gz7) {
        this.gz7 = gz7;
    }
    public BigDecimal getGz7() {
        return gz7;
    }
    
    @MapperCell(cellName="班主任",order=1)    
    @FieldInfo(name = "班主任")
    @Column(name = "GZ8", length = 18, nullable = true)
    private BigDecimal gz8;
    public void setGz8(BigDecimal gz8) {
        this.gz8 = gz8;
    }
    public BigDecimal getGz8() {
        return gz8;
    }
    
    @MapperCell(cellName="专业津贴",order=1)    
    @FieldInfo(name = "专业津贴")
    @Column(name = "GZ9", length = 18, nullable = true)
    private BigDecimal gz9;
    public void setGz9(BigDecimal gz9) {
        this.gz9 = gz9;
    }
    public BigDecimal getGz9() {
        return gz9;
    }
    
    @MapperCell(cellName="小计2",order=1)
    @FieldInfo(name = "小计2")
    @Column(name = "GZ10", length = 18, nullable = true)
    private BigDecimal gz10;
    public void setGz10(BigDecimal gz10) {
        this.gz10 = gz10;
    }
    public BigDecimal getGz10() {
        return gz10;
    }
    
    @MapperCell(cellName="绩效工资",order=1)
    @FieldInfo(name = "绩效工资")
    @Column(name = "GZ11", length = 18, nullable = true)
    private BigDecimal gz11;
    public void setGz11(BigDecimal gz11) {
        this.gz11 = gz11;
    }
    public BigDecimal getGz11() {
        return gz11;
    }
    
    @MapperCell(cellName="独生子女费",order=1)
    @FieldInfo(name = "独生子女费")
    @Column(name = "GZ12", length = 18, nullable = true)
    private BigDecimal gz12;
    public void setGz12(BigDecimal gz12) {
        this.gz12 = gz12;
    }
    public BigDecimal getGz12() {
        return gz12;
    }
    
    @MapperCell(cellName="应发工资",order=1)
    @FieldInfo(name = "gz13")
    @Column(name = "GZ13", length = 18, nullable = true)
    private BigDecimal gz13;
    public void setGz13(BigDecimal gz13) {
        this.gz13 = gz13;
    }
    public BigDecimal getGz13() {
        return gz13;
    }
    
    @MapperCell(cellName="个人医疗",order=1)
    @FieldInfo(name = "个人医疗")
    @Column(name = "GZ14", length = 18, nullable = true)
    private BigDecimal gz14;
    public void setGz14(BigDecimal gz14) {
        this.gz14 = gz14;
    }
    public BigDecimal getGz14() {
        return gz14;
    }
    
    @MapperCell(cellName="个人养老",order=1)
    @FieldInfo(name = "个人养老")
    @Column(name = "GZ15", length = 18, nullable = true)
    private BigDecimal gz15;
    public void setGz15(BigDecimal gz15) {
        this.gz15 = gz15;
    }
    public BigDecimal getGz15() {
        return gz15;
    }
    
    @MapperCell(cellName="所得税",order=1)
    @FieldInfo(name = "所得税")
    @Column(name = "GZ16", length = 18, nullable = true)
    private BigDecimal gz16;
    public void setGz16(BigDecimal gz16) {
        this.gz16 = gz16;
    }
    public BigDecimal getGz16() {
        return gz16;
    }
    
    @MapperCell(cellName="个人公积金",order=1)
    @FieldInfo(name = "个人公积金")
    @Column(name = "GZ17", length = 18, nullable = true)
    private BigDecimal gz17;
    public void setGz17(BigDecimal gz17) {
        this.gz17 = gz17;
    }
    public BigDecimal getGz17() {
        return gz17;
    }
    
    @MapperCell(cellName="代缴",order=1)
    @FieldInfo(name = "代缴")
    @Column(name = "GZ18", length = 18, nullable = true)
    private BigDecimal gz18;
    public void setGz18(BigDecimal gz18) {
        this.gz18 = gz18;
    }
    public BigDecimal getGz18() {
        return gz18;
    }
    
    @MapperCell(cellName="实发工资",order=1)
    @FieldInfo(name = "实发工资")
    @Column(name = "GZ19", length = 18, nullable = true)
    private BigDecimal gz19;
    public void setGz19(BigDecimal gz19) {
        this.gz19 = gz19;
    }
    public BigDecimal getGz19() {
        return gz19;
    }
    
    @MapperCell(cellName="GZ20",order=1)
    @FieldInfo(name = "GZ20")
    @Column(name = "GZ20", length = 18, nullable = true)
    private BigDecimal gz20;
    public void setGz20(BigDecimal gz20) {
        this.gz20 = gz20;
    }
    public BigDecimal getGz20() {
        return gz20;
    }
    
    @MapperCell(cellName="gz21",order=1)
    @FieldInfo(name = "gz21")
    @Column(name = "GZ21", length = 18, nullable = true)
    private BigDecimal gz21;
    public void setGz21(BigDecimal gz21) {
        this.gz21 = gz21;
    }
    public BigDecimal getGz21() {
        return gz21;
    }
    
    @MapperCell(cellName="gz22",order=1)
    @FieldInfo(name = "gz22")
    @Column(name = "GZ22", length = 18, nullable = true)
    private BigDecimal gz22;
    public void setGz22(BigDecimal gz22) {
        this.gz22 = gz22;
    }
    public BigDecimal getGz22() {
        return gz22;
    }
    
    @MapperCell(cellName="gz23",order=1)
    @FieldInfo(name = "gz23")
    @Column(name = "GZ23", length = 18, nullable = true)
    private BigDecimal gz23;
    public void setGz23(BigDecimal gz23) {
        this.gz23 = gz23;
    }
    public BigDecimal getGz23() {
        return gz23;
    }

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}