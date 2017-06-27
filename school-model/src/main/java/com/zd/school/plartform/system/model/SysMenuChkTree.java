/**
 * Project Name:school-model
 * File Name:SysMenuChkTree.java
 * Package Name:com.zd.school.plartform.system.model
 * Date:2016年7月31日上午12:09:14
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.system.model;

import java.util.List;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNodeChk;

/**
 * ClassName:SysMenuChkTree 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016年7月31日 上午12:09:14 
 * @author   luoyibo
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class SysMenuChkTree extends ExtTreeNodeChk<SysMenuChkTree>{
    @FieldInfo(name = "菜单编码")
    private String menuCode;

    @FieldInfo(name = "小图标")
    private String smallIcon;

    @FieldInfo(name = "大图标")
    private String bigIcon;

    @FieldInfo(name = "菜单地址")
    private String menuTarget;

    @FieldInfo(name = "菜单类型")
    private String menuType;

    @FieldInfo(name = "是否叶菜单")
    private String menuLeaf;

    @FieldInfo(name = "上级菜单")
    private String parent;

    @FieldInfo(name = "排序字段")
    private Integer orderIndex;

    @FieldInfo(name = "节点编码")
    private String nodeCode;

    @FieldInfo(name = "是否系统菜单")
    private Integer issystem;

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Integer getIssystem() {
        return issystem;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getBigIcon() {
        return bigIcon;
    }

    public void setBigIcon(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public String getMenuTarget() {
        return menuTarget;
    }

    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuLeaf() {
        return menuLeaf;
    }

    public void setMenuLeaf(String menuLeaf) {
        this.menuLeaf = menuLeaf;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public SysMenuChkTree(String id, List<SysMenuChkTree> children) {

        super(id, children);
        // TODO Auto-generated constructor stub

    }

    public SysMenuChkTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,
            List<SysMenuChkTree> children,Boolean checked, String menuCode, String smallIcon, String bigIcon, String menuTarget,
            String menuType, String menuLeaf, String nodeCode, String parentNode, Integer orderIndex,
            Integer issystem) {

        super(id, text, iconCls, leaf, level, treeid, children, checked);
        this.menuCode = menuCode;
        this.smallIcon = smallIcon;
        this.bigIcon = bigIcon;
        this.menuTarget = menuTarget;
        this.menuType = menuType;
        this.menuLeaf = menuLeaf;
        this.nodeCode = nodeCode;
        this.parent = parentNode;
        this.orderIndex = orderIndex;
        this.issystem = issystem;
        // TODO Auto-generated constructor stub

    }
}

