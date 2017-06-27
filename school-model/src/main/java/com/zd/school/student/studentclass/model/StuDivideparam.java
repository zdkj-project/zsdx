package com.zd.school.student.studentclass.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: StuDivideparam 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班参数实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "STU_T_DIVIDEPARAM")
@AttributeOverride(name = "uuid", column = @Column(name = "PARAM_ID", length = 36, nullable = false))
public class StuDivideparam extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "班级ID")
    @Column(name = "CLAI_ID", length = 36, nullable = true)
    private String claiId;
    public void setClaiId(String claiId) {
        this.claiId = claiId;
    }
    public String getClaiId() {
        return claiId;
    }
        
    @FieldInfo(name = "记录ID")
    @Column(name = "DIVIDE_ID", length = 36, nullable = true)
    private String divideId;
    public void setDivideId(String divideId) {
        this.divideId = divideId;
    }
    public String getDivideId() {
        return divideId;
    }
        
    @FieldInfo(name = "分班类型")
    @Column(name = "DIVIDE_TYPE", length = 4, nullable = false)
    private String divideType;
    public void setDivideType(String divideType) {
        this.divideType = divideType;
    }
    public String getDivideType() {
        return divideType;
    }
        
    @FieldInfo(name = "优先级别，1-重点班-2特长班3-普通班")
    @Column(name = "DIVIDE_LEVEL", length = 10, nullable = false)
    private Integer divideLevel;
    public void setDivideLevel(Integer divideLevel) {
        this.divideLevel = divideLevel;
    }
    public Integer getDivideLevel() {
        return divideLevel;
    }
        
    @FieldInfo(name = "分班人数")
    @Column(name = "DIVIDE_COUNT", length = 10, nullable = false)
    private Integer divideCount;
    public void setDivideCount(Integer divideCount) {
        this.divideCount = divideCount;
    }
    public Integer getDivideCount() {
        return divideCount;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}