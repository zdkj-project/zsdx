/**
 * Project Name:school-model
 * File Name:CommBase.java
 * Package Name:com.zd.school.plartform.comm.model
 * Date:2016年8月23日下午1:57:02
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.comm.model;

/**
 * ClassName:CommBase Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年8月23日 下午1:57:02
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class CommBase {
    /** 节点ID */
    public String id;

    /** 节点文本 */
    public String text;

    /** 节点样式 */
    public String iconCls;

    /** 是否叶节点 */
    public String leaf;

    /** 节点层级 */
    public Integer level;

    /** 上级层次 */
    public String parent;

    /** 节点层次 */
    private String treeIds;

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

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
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

    public String getTreeIds() {
        return treeIds;
    }

    public void setTreeIds(String treeIds) {
        this.treeIds = treeIds;
    }

}
