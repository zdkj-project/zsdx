package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.TreeNodeEntity;

/**
 * 
 * ClassName: BaseDic Function: TODO ADD FUNCTION. Reason: TODO ADD REASON(可选).
 * Description: 数据字典实体类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_DIC")
@AttributeOverride(name = "uuid", column = @Column(name = "DIC_ID", length = 36, nullable = false))
public class BaseDic extends TreeNodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "字典编码")
    @Column(name = "DIC_CODE", length = 16, nullable = false)
    private String dicCode;

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicCode() {
        return dicCode;
    }

    @FieldInfo(name = "字典类型，目前就LIST与TREE两类")
    @Column(name = "DIC_TYPE", length = 8, nullable = false)
    private String dicType;

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicType() {
        return dicType;
    }

    @FieldInfo(name = "引用实体路径")
    @Column(name = "REF_MODEL", length = 256, nullable = true)
    private String refModel;

    public void setRefModel(String refModel) {
        this.refModel = refModel;
    }

    public String getRefModel() {
        return refModel;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "上级字典名称")
    @Transient
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }    
}