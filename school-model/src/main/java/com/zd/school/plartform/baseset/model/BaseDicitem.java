package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BaseDicitem Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 数据字典项实体类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_DICITEM")
@AttributeOverride(name = "uuid", column = @Column(name = "DICITEM_ID", length = 36, nullable = false))
public class BaseDicitem extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "字典ID")
    @Column(name = "DIC_ID", length = 36, nullable = true)
    private String dicId;

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicId() {
        return dicId;
    }

    @FieldInfo(name = "字典项编码")
    @Column(name = "ITEM_CODE", length = 16, nullable = false)
    private String itemCode;

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    @FieldInfo(name = "字典项名称")
    @Column(name = "ITEM_NAME", length = 128, nullable = false)
    private String itemName;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    @FieldInfo(name = "字典项说明")
    @Column(name = "ITEM_DESC", length = 128, nullable = true)
    private String itemDesc;

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "字典代码")
    @Formula("(SELECT a.DIC_CODE FROM BASE_T_DIC a WHERE a.DIC_ID=DIC_ID)")
    private String dicCode;

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    @FieldInfo(name = "字典名称")
    @Formula("(SELECT a.NODE_TEXT FROM BASE_T_DIC a WHERE a.DIC_ID=DIC_ID)")
    private String dicName;

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }
}