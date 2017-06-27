package com.zd.school.plartform.system.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.TreeNodeEntity;

/**
 * 
 * ClassName: BaseTMenu Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 系统菜单表实体类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_MENU")
@AttributeOverride(name = "uuid", column = @Column(name = "MENU_ID", length = 36, nullable = false))
public class SysMenu extends TreeNodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "菜单编码")
    @Column(name = "MENU_CODE", length = 32, nullable = false)
    private String menuCode;

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    @FieldInfo(name = "菜单图标")
    @Column(name = "SMALL_ICON", length = 256, nullable = true)
    private String smallIcon;

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    @FieldInfo(name = "菜单大图标")
    @Column(name = "BIG_ICON", length = 256, nullable = true)
    private String bigIcon;

    public void setBigIcon(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public String getBigIcon() {
        return bigIcon;
    }

    @FieldInfo(name = "菜单地址")
    @Column(name = "MENU_TARGET")
    private String menuTarget;

    public String getMenuTarget() {
        return menuTarget;
    }

    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }

    @FieldInfo(name = "菜单类型")
    @Column(name = "MENU_TYPE")
    private String menuType;

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    @FieldInfo(name = "是否叶菜单")
    @Column(name = "MENU_LEAF")
    private String menuLeaf;

    public String getMenuLeaf() {
        return menuLeaf;
    }

    public void setMenuLeaf(String menuLeaf) {
        this.menuLeaf = menuLeaf;
    }

    @FieldInfo(name = "是否系统菜单")
    @Column(name = "ISSYSTEM", length = 10, nullable = false)
    private Integer issystem;

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Integer getIssystem() {
        return issystem;
    }

    @FieldInfo(name = "是否隐藏,0-不隐藏 1-隐藏")
    @Column(name = "ISHIDDEN", length = 10, nullable = true)
    private String isHidden;

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "上级菜单名称")
    @Formula("(SELECT isnull(a.NODE_TEXT,'ROOT') FROM SYS_T_MENU a WHERE a.MENU_ID=parent_node)")
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}