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
 * ClassName: DocKeyword 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文主题词实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "DOC_T_KEYWORD")
@AttributeOverride(name = "uuid", column = @Column(name = "DOCKEY_ID", length = 36, nullable = false))
public class DocKeyword extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "名称")
    @Column(name = "DOCKEY_NAME", length = 16, nullable = false)
    private String dockeyName;
    public void setDockeyName(String dockeyName) {
        this.dockeyName = dockeyName;
    }
    public String getDockeyName() {
        return dockeyName;
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
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}