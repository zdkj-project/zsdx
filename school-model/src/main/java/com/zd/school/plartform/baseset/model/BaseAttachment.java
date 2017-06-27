package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseTAttachment Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公共附件信息表实体类. date: 2016-07-06
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_ATTACHMENT")
//@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AttributeOverride(name = "uuid", column = @Column(name = "ATTACH_ID", length = 36, nullable = false))
public class BaseAttachment extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "实体名称")
    @Column(name = "ENTITY_NAME", length = 36, nullable = false)
    private String entityName;

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @FieldInfo(name = "记录ID")
    @Column(name = "RECORD_ID", length = 36, nullable = false)
    private String recordId;

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }

    @FieldInfo(name = "存放路径")
    @Column(name = "ATTACH_URL", length = 128, nullable = false)
    private String attachUrl;

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    @FieldInfo(name = "文件名称")
    @Column(name = "ATTACH_NAME", length = 64, nullable = false)
    private String attachName;

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }
    public String getAttachName() {
        return attachName;
    }
    
    @FieldInfo(name = "文件大小")
    @Column(name = "ATTACH_SIZE", nullable = false)
    private Long attachSize;
    public void setAttachSize(Long attachSize) {
        this.attachSize = attachSize;
    }
    public Long getAttachSize() {
        return attachSize;
    }
    
    @FieldInfo(name = "文件类型")
    @Column(name = "ATTACH_TYPE", length = 20, nullable = false)
    private String attachType;
    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }
    public String getAttachType() {
        return attachType;
    }
    
    @FieldInfo(name = "是否是正文文件")
    @Column(name = "ATTACH_ISMAIN", nullable = true,columnDefinition="INT default 0")
    private int attachIsMain;
    public void setAttachIsMain(int attachIsMain) {
        this.attachIsMain = attachIsMain;
    }
    public int getAttachIsMain() {
        return attachIsMain;
    }
    
    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}