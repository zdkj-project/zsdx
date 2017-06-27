package com.zd.core.constant;

/**
 * 树形字段类型枚举
 * 
 * @author luoyibo
 *
 */
public enum TreeNodeType {
    ID, TEXT, CODE, CLS, HREF, HREFTARGET, EXPANDABLE, EXPANDED, DESCRIPTION, ICON, CHECKED, PARENT, LEAF, NODEINFO, NODEINFOTYPE, ORDERINDEX, LAYER, BIGICON, DISABLED,AREADESC,AREAADDR;
    public Boolean equalsType(TreeNodeType other) {
        int i = this.compareTo(other);
        if (i != 0) {
            return false;
        }
        return true;
    }
}
