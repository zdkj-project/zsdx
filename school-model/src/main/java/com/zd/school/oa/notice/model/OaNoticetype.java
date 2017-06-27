package com.zd.school.oa.notice.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: OaNoticetype Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公告类型(OA_T_NOTICETYPE)实体类. date: 2016-09-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "OA_T_NOTICETYPE")
@AttributeOverride(name = "uuid", column = @Column(name = "TYPE_ID", length = 36, nullable = false))
public class OaNoticetype extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "类型名称")
    @Column(name = "TYPE_NAME", length = 64, nullable = false)
    private String typeName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */

    @Transient
    private String failed;
    @Transient
    private boolean success;

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}