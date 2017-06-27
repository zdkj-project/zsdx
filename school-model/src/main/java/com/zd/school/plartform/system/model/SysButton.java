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
 * ClassName: BaseTButton Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 功能按钮实体类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_BUTTON")
@AttributeOverride(name = "uuid", column = @Column(name = "BUTTON_ID", length = 36, nullable = false))
public class SysButton extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "按钮名称")
    @Column(name = "BUTTON_NAME", length = 32, nullable = false)
    private String buttonName;

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }

    @FieldInfo(name = "提示信息")
    @Column(name = "BUTTON_TITLE", length = 32, nullable = false)
    private String buttonTitle;

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    @FieldInfo(name = "按钮编码")
    @Column(name = "BUTTON_CODE", length = 8, nullable = false)
    private String buttonCode;

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    @FieldInfo(name = "按钮图标")
    @Column(name = "ICON_URL", length = 128, nullable = false)
    private String iconUrl;

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @FieldInfo(name = "是否系统按钮")
    @Column(name = "ISSYSTEM", length = 10, nullable = false)
    private Integer issystem;

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Integer getIssystem() {
        return issystem;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}