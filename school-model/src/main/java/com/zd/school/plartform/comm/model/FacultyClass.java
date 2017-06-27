/**
 * Project Name:school-model
 * File Name:FacultyClass.java
 * Package Name:com.zd.school.plartform.comm.model
 * Date:2016年7月21日上午11:31:48
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.comm.model;

/**
 * ClassName:FacultyClass Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年7月21日 上午11:31:48
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class FacultyClass {
    /** 节点ID */
    public String id;

    /** 节点文本 */
    public String text;

    /** 节点样式 */
    public String iconCls;

    /** 是否叶节点 */
    public Integer leaf;

    /** 节点层级 */
    public Integer level;

    /** 上级层次 */
    public String parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}
