package com.zd.school.plartform.system.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: SysDatapermission Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户数据权限表(SYS_T_DATAPERMISSION)实体类. date: 2016-09-01
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_DATAPERMISSION")
@AttributeOverride(name = "uuid", column = @Column(name = "DPER_ID", length = 36, nullable = false))
public class SysDatapermission extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "主键")
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @FieldInfo(name = "实体名称")
    @Column(name = "ENTITY_NAME", length = 32, nullable = false)
    private String entityName;

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @FieldInfo(name = "权限控制字段")
    @Column(name = "RIGHT_FIELD", length = 32, nullable = false)
    private String rightField;

    public void setRightField(String rightField) {
        this.rightField = rightField;
    }

    public String getRightField() {
        return rightField;
    }

    @FieldInfo(name = "父节点字段:针对树形数据配置")
    @Column(name = "PARENT_FIELD", length = 36, nullable = true)
    private String parentField;

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    public String getParentField() {
        return parentField;
    }

    @FieldInfo(name = "权限类型:0-所有数据,1-本单元,2-本单元及下级单元,3-指定数据")
    @Column(name = "RIGHT_TYPE", length = 16, nullable = false)
    private String rightType;

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public String getRightType() {
        return rightType;
    }

    @FieldInfo(name = "数据展现方式:0-列表,1-树形,默认列表")
    @Column(name = "DISPLAY_MODE", length = 16, nullable = false)
    private String displayMode;

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}