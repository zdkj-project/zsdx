/**
 * Project Name:school-model
 * File Name:BaseDicTree.java
 * Package Name:com.zd.school.plartform.baseset.model
 * Date:2016年7月19日下午4:13:01
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.baseset.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNode;

import java.util.List;

/**
 * ClassName:BaseDicTree Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年7月19日 下午4:13:01
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class BaseDicTree extends ExtTreeNode<BaseDicTree> {

    @FieldInfo(name = "字典编码")
    private String dicCode;

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicCode() {
        return dicCode;
    }

    @FieldInfo(name = "字典类型，目前就LIST与TREE两类")
    private String dicType;

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicType() {
        return dicType;
    }

    @FieldInfo(name = "引用实体路径")
    private String refModel;

    public void setRefModel(String refModel) {
        this.refModel = refModel;
    }

    public String getRefModel() {
        return refModel;
    }

    @FieldInfo(name = "上级字典")
    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @FieldInfo(name = "排序号")
    private Integer orderIndex;

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public BaseDicTree(String id, List<BaseDicTree> children) {

        super(id, children);
    }

    public BaseDicTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,
            List<BaseDicTree> children, String dicCode, String dicType, String refModel, String parent,
            Integer orderIndex) {
        super(id, text, iconCls, leaf, level, treeid,parent,orderIndex, children);
        this.dicCode = dicCode;
        this.dicType = dicType;
        this.refModel = refModel;
/*        this.parent = parent;
        this.orderIndex = orderIndex;*/
    }
}
