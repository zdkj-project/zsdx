package com.zd.school.oa.doc.model;

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
 * ClassName: DocDoctype 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文类型表实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "DOC_T_DOCTYPE")
@AttributeOverride(name = "uuid", column = @Column(name = "DOCTYPE_ID", length = 36, nullable = false))
public class DocDoctype extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "类型名称")
    @Column(name = "DOCTYPE_NAME", length = 36, nullable = false)
    private String doctypeName;
    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }
    public String getDoctypeName() {
        return doctypeName;
    }
        
    @FieldInfo(name = "所属分类")
    @Column(name = "DOC_TYPE", length = 8, nullable = false)
    private String docType;
    public void setDocType(String docType) {
        this.docType = docType;
    }
    public String getDocType() {
        return docType;
    }
        
    @FieldInfo(name = "默认前缀")
    @Column(name = "PREFIX", length = 8, nullable = true)
    private String prefix;
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getPrefix() {
        return prefix;
    }
        
    @FieldInfo(name = "默认后缀")
    @Column(name = "SUFFIX", length = 8, nullable = true)
    private String suffix;
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public String getSuffix() {
        return suffix;
    }
        
    @FieldInfo(name = "类型说明")
    @Column(name = "DOCTYPE_DESC", length = 128, nullable = true)
    private String doctypeDesc;
    public void setDoctypeDesc(String doctypeDesc) {
        this.doctypeDesc = doctypeDesc;
    }
    public String getDoctypeDesc() {
        return doctypeDesc;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}