package com.zd.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: FlowEntity 
 * Function: TODO ADD FUNCTION. 
 * Reason: 流程功能实体基类. 
 * Description: 因为原流程相关的数据表有自己的定义而实际业务管理中对流程有些需要扩展的字段，因此重新定义了流程的扩展基类.
 * date: 2016年3月30日 上午11:48:26 
 *
 * @author luoyibo
 * @version 
 * @since JDK 1.8
 */

@MappedSuperclass
public abstract class FlowEntity {

    @FieldInfo(name = "创建时间")
    @Column(name = "CREATE_TIME", nullable = false, columnDefinition = "datetime", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date createTime = new Date();

    @FieldInfo(name = "创建人")
    @Column(name = "CREATE_USER")
    private String createUser;

    @FieldInfo(name = "最后修改时间")
    @Column(name = "UPDATE_TIME", nullable = false, columnDefinition = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date updateTime = new Date();

    @FieldInfo(name = "修改人")
    @Column(name = "UPDATE_USER")
    private String updateUser;

    @FieldInfo(name = "备用字段1")
    @Column(name = "EXT_FIELD01", length = 1000)
    private String extField01;

    @FieldInfo(name = "备用字段2")
    @Column(name = "EXT_FIELD02", length = 1000)
    private String extField02;

    @FieldInfo(name = "备用字段3")
    @Column(name = "EXT_FIELD03", length = 1000)
    private String extField03;

    @FieldInfo(name = "备用字段4")
    @Column(name = "EXT_FIELD04", length = 1000)
    private String extField04;

    @FieldInfo(name = "备用字段5")
    @Column(name = "EXT_FIELD05", length = 1000)
    private String extField05;

    @FieldInfo(name = "是否删除")
    @Column(name = "ISDELETE")
    private Integer isDelete = 0;

    @FieldInfo(name = "排序字段")
    @Column(name = "ORDER_INDEX")
    private Integer orderIndex;

    @JsonIgnore
    @Transient
    @FieldInfo(name = "每页记录数")
    private Integer limit = Integer.valueOf(30);

    @JsonIgnore
    @Transient
    @FieldInfo(name = "起始记录数")
    private Integer start = Integer.valueOf(0);

    @JsonIgnore
    @Transient
    @FieldInfo(name = "查询条件")
    protected String whereSql = "";

    @JsonIgnore
    @Transient
    @FieldInfo(name = "主功能过滤条件")
    protected String parentSql = "";

    @JsonIgnore
    @Transient
    @FieldInfo(name = "查询条件")
    protected String querySql = "";

    @JsonIgnore
    @Transient
    @FieldInfo(name = "排序条件")
    protected String orderSql = "";

    @JsonIgnore
    @Transient
    @FieldInfo(name = "前端的操作")
    protected String operation = "";
    
    @JsonIgnore
    @Transient
    @FieldInfo(name = "字段过滤")
    private String filter;
        
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @JsonIgnore
    @Transient
    @FieldInfo(name = "字段排序")
    private String sort;
    
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    
    public String getExtField01() {
        return extField01;
    }

    public void setExtField01(String extField01) {
        this.extField01 = extField01;
    }

    public String getExtField02() {
        return extField02;
    }

    public void setExtField02(String extField02) {
        this.extField02 = extField02;
    }

    public String getExtField03() {
        return extField03;
    }

    public void setExtField03(String extField03) {
        this.extField03 = extField03;
    }

    public String getExtField04() {
        return extField04;
    }

    public void setExtField04(String extField04) {
        this.extField04 = extField04;
    }

    public String getExtField05() {
        return extField05;
    }

    public void setExtField05(String extField05) {
        this.extField05 = extField05;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public String getParentSql() {
        return parentSql;
    }

    public void setParentSql(String parentSql) {
        this.parentSql = parentSql;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }

    
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}
