/**
 * Project Name:zd-core
 * File Name:TreeNodeEntity.java
 * Package Name:com.zd.core.model
 * Date:2016年5月11日下午4:33:08
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.model;

import com.zd.core.annotation.FieldInfo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * ClassName:TreeNodeEntity Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月11日 下午4:33:08
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@MappedSuperclass
public abstract class TreeNodeEntity extends BaseEntity {

    @FieldInfo(name = "节点编码")
    @Column(name = "NODE_CODE")
    private String nodeCode;

    @FieldInfo(name = "节点名称")
    @Column(name = "NODE_TEXT")
    private String nodeText;

    @FieldInfo(name = "父节点")
    @Column(name = "PARENT_NODE")
    private String parentNode;

    @FieldInfo(name = "是否 叶节点")
    @Column(name = "ISLEAF")
    private Boolean isLeaf;

    @FieldInfo(name = "节点层级")
    @Column(name = "NODE_LEVEL")
    private Integer nodeLevel;

    @FieldInfo(name = "节点标识层次")
    @Column(name = "TREE_IDS", length = 1024)
    private String treeIds;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getTreeIds() {
        return treeIds;
    }

    public void setTreeIds(String treeIds) {
        this.treeIds = treeIds;
    }

    public void BuildNode(TreeNodeEntity parentNode) {
        if (parentNode == null) {
            treeIds = super.getUuid();
            nodeLevel = 1;
        } else {
            treeIds = parentNode.treeIds + "," + super.getUuid();
            nodeLevel = parentNode.nodeLevel + 1;
        }
    }

    public TreeNodeEntity() {

        super();
        // TODO Auto-generated constructor stub

    }

    public TreeNodeEntity(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }
}
