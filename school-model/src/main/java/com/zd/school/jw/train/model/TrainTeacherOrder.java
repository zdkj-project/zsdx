
package com.zd.school.jw.train.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

/**
 * 
 * ClassName: TrainClassorder 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教师订餐信息(TRAIN_T_CLASSORDER)实体类.
 * date: 2017-10-30
 *
 * @author  zzk 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_TEACHERORDER")
@AttributeOverride(name = "uuid", column = @Column(name = "ORDER_ID", length = 36, nullable = false))
public class TrainTeacherOrder extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "教师ID")
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
        
    @FieldInfo(name = "用餐类别,1-A套餐、2-B套餐")
    @Column(name = "DINNER_GROUP", length = 1, nullable = false)
    private Short dinnerGroup;
    public void setDinnerGroup(Short dinnerGroup) {
        this.dinnerGroup = dinnerGroup;
    }
    public Short getDinnerGroup() {
        return dinnerGroup;
    }
        
    @FieldInfo(name = "就餐日期")
    @Column(name = "DINNER_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date dinnerDate;
    public void setDinnerDate(Date dinnerDate) {
        this.dinnerDate = dinnerDate;
    }
    public Date getDinnerDate() {
        return dinnerDate;
    }
    
    @FieldInfo(name = "备注")
    @Column(name = "REMARK", length = 500, nullable = true)
    private String remark;
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }
       
    @FieldInfo(name = "姓名")
    @Formula("(SELECT a.XM FROM dbo.SYS_T_USER a WHERE a.USER_ID=USER_ID)")
    private String xm;

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXm() {
        return xm;
    }
        
}