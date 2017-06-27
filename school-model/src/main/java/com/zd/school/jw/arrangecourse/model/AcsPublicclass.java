package com.zd.school.jw.arrangecourse.model;

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
 * ClassName: AcsPublicclass 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 2.1公用教室基础数据(ACS_T_PUBLICCLASS)实体类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "ACS_T_PUBLICCLASS")
@AttributeOverride(name = "uuid", column = @Column(name = "uuid", length = 36, nullable = false))
public class AcsPublicclass extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "教室名称")
    @Column(name = "classname", length = 50, nullable = true)
    private String classname;
    public void setClassname(String classname) {
        this.classname = classname;
    }
    public String getClassname() {
        return classname;
    }
        
    @FieldInfo(name = "字典任课学段代码初中3、高中4 JW_T_GRADE SECTION_CODE")
    @Column(name = "stage", length = 32, nullable = true)
    private String stage;
    public void setStage(String stage) {
        this.stage = stage;
    }
    public String getStage() {
        return stage;
    }
        
    @FieldInfo(name = "优先安排 正常0 优先排1")
    @Column(name = "yxap", length = 1, nullable = true)
    private Boolean yxap;
    public void setYxap(Boolean yxap) {
        this.yxap = yxap;
    }
    public Boolean getYxap() {
        return yxap;
    }
        
    @FieldInfo(name = " 容量 几个班")
    @Column(name = "volumen", length = 10, nullable = true)
    private Integer volumen;
    public void setVolumen(Integer volumen) {
        this.volumen = volumen;
    }
    public Integer getVolumen() {
        return volumen;
    }
        
    @FieldInfo(name = "人数")
    @Column(name = "num", length = 10, nullable = true)
    private Integer num;
    public void setNum(Integer num) {
        this.num = num;
    }
    public Integer getNum() {
        return num;
    }
        
    @Transient
    @FieldInfo(name = "用来转换布尔类型在UI中显示")
    private Integer yxaps;
	public Integer getYxaps() {
		return yxaps;
	}
	public void setYxaps(Integer yxaps) {
		this.yxaps = yxaps;
	}
	
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}