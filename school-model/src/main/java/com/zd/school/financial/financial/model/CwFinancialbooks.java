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
 * ClassName: CwFinancialbooks 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOKS)实体类.
 * date: 2017-02-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CW_T_FINANCIALBOOKS")
@AttributeOverride(name = "uuid", column = @Column(name = "PROJECTS_ID", length = 36, nullable = false))
public class CwFinancialbooks extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "projects")
    @Column(name = "PROJECTS", length = 255, nullable = true)
    private String projects;
    public void setProjects(String projects) {
        this.projects = projects;
    }
    public String getProjects() {
        return projects;
    }
        
    @FieldInfo(name = "propertiess")
    @Column(name = "PROPERTIESS", length = 255, nullable = true)
    private String propertiess;
    public void setPropertiess(String propertiess) {
        this.propertiess = propertiess;
    }
    public String getPropertiess() {
        return propertiess;
    }
        
    @FieldInfo(name = "lastYear")
    @Column(name = "LAST_YEAR", length = 255, nullable = true)
    private BigDecimal lastYear;
    public void setLastYear(BigDecimal lastYear) {
        this.lastYear = lastYear;
    }
    public BigDecimal getLastYear() {
        return lastYear;
    }
        
    @FieldInfo(name = "thisYear")
    @Column(name = "THIS_YEAR", length = 255, nullable = true)
    private BigDecimal thisYear;
    public void setThisYear(BigDecimal thisYear) {
        this.thisYear = thisYear;
    }
    public BigDecimal getThisYear() {
        return thisYear;
    }
        
    @FieldInfo(name = "one")
    @Column(name = "ONE", length = 19, nullable = true)
    private BigDecimal one;
    public void setOne(BigDecimal one) {
        this.one = one;
    }
    public BigDecimal getOne() {
        return one;
    }
        
    @FieldInfo(name = "two")
    @Column(name = "TWO", length = 19, nullable = true)
    private BigDecimal two;
    public void setTwo(BigDecimal two) {
        this.two = two;
    }
    public BigDecimal getTwo() {
        return two;
    }
        
    @FieldInfo(name = "three")
    @Column(name = "THREE", length = 19, nullable = true)
    private BigDecimal three;
    public void setThree(BigDecimal three) {
        this.three = three;
    }
    public BigDecimal getThree() {
        return three;
    }
        
    @FieldInfo(name = "four")
    @Column(name = "FOUR", length = 19, nullable = true)
    private BigDecimal four;
    public void setFour(BigDecimal four) {
        this.four = four;
    }
    public BigDecimal getFour() {
        return four;
    }
        
    @FieldInfo(name = "five")
    @Column(name = "FIVE", length = 19, nullable = true)
    private BigDecimal five;
    public void setFive(BigDecimal five) {
        this.five = five;
    }
    public BigDecimal getFive() {
        return five;
    }
        
    @FieldInfo(name = "six")
    @Column(name = "SIX", length = 19, nullable = true)
    private BigDecimal six;
    public void setSix(BigDecimal six) {
        this.six = six;
    }
    public BigDecimal getSix() {
        return six;
    }
        
    @FieldInfo(name = "seven")
    @Column(name = "SEVEN", length = 19, nullable = true)
    private BigDecimal seven;
    public void setSeven(BigDecimal seven) {
        this.seven = seven;
    }
    public BigDecimal getSeven() {
        return seven;
    }
        
    @FieldInfo(name = "eight")
    @Column(name = "EIGHT", length = 19, nullable = true)
    private BigDecimal eight;
    public void setEight(BigDecimal eight) {
        this.eight = eight;
    }
    public BigDecimal getEight() {
        return eight;
    }
        
    @FieldInfo(name = "nine")
    @Column(name = "NINE", length = 19, nullable = true)
    private BigDecimal nine;
    public void setNine(BigDecimal nine) {
        this.nine = nine;
    }
    public BigDecimal getNine() {
        return nine;
    }
        
    @FieldInfo(name = "ten")
    @Column(name = "TEN", length = 19, nullable = true)
    private BigDecimal ten;
    public void setTen(BigDecimal ten) {
        this.ten = ten;
    }
    public BigDecimal getTen() {
        return ten;
    }
        
    @FieldInfo(name = "eleven")
    @Column(name = "ELEVEN", length = 19, nullable = true)
    private BigDecimal eleven;
    public void setEleven(BigDecimal eleven) {
        this.eleven = eleven;
    }
    public BigDecimal getEleven() {
        return eleven;
    }
        
    @FieldInfo(name = "twelve")
    @Column(name = "TWELVE", length = 19, nullable = true)
    private BigDecimal twelve;
    public void setTwelve(BigDecimal twelve) {
        this.twelve = twelve;
    }
    public BigDecimal getTwelve() {
        return twelve;
    }
        
    @FieldInfo(name = "totals")
    @Column(name = "TOTALS", length = 19, nullable = true)
    private BigDecimal totals;
    public void setTotals(BigDecimal totals) {
        this.totals = totals;
    }
    public BigDecimal getTotals() {
        return totals;
    }
        
    @FieldInfo(name = "balance")
    @Column(name = "BALANCE", length = 19, nullable = true)
    private BigDecimal balance;
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getBalance() {
        return balance;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}