/**
 * Project Name:jw-model
 * File Name:BaseOrgTree.java
 * Package Name:com.zd.school.base.model
 * Date:2016年5月11日下午5:45:31
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.baseset.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNode;

import java.util.List;

/**
 * ClassName:BaseOrgTree Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年5月11日 下午5:45:31
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class BaseOrgTree extends ExtTreeNode<BaseOrgTree> {

    @FieldInfo(name = "部门编码")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @FieldInfo(name = "主负责岗位")
    private String mainLeader;

    public String getMainLeader() {
        return mainLeader;
    }

    public void setMainLeader(String mainLeader) {
        this.mainLeader = mainLeader;
    }

    @FieldInfo(name = "副负责岗位")
    private String viceLeader;

    public String getViceLeader() {
        return viceLeader;
    }

    public void setViceLeader(String viceLeader) {
        this.viceLeader = viceLeader;
    }

    @FieldInfo(name = "外线电话")
    private String outPhone;

    public String getOutPhone() {
        return outPhone;
    }

    public void setOutPhone(String outPhone) {
        this.outPhone = outPhone;
    }

    @FieldInfo(name = "内线电话")
    private String inPhone;

    public String getInPhone() {
        return inPhone;
    }

    public void setInPhone(String inPhone) {
        this.inPhone = inPhone;
    }

    @FieldInfo(name = "传真")
    private String fax;

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @FieldInfo(name = "是否系统内置")
    private Integer isSystem;

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    @FieldInfo(name = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @FieldInfo(name = "上级部门")
    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @FieldInfo(name = "排序字段")
    private Integer orderIndex;

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @FieldInfo(name = "部门类型")
    private String deptType;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    @FieldInfo(name = "上级部门类型")
    private String parentType;

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    @FieldInfo(name = "主负责岗位名称")
    private String mainLeaderName;

    public String getMainLeaderName() {
        return mainLeaderName;
    }

    public void setMainLeaderName(String mainLeaderName) {
        this.mainLeaderName = mainLeaderName;
    }

    @FieldInfo(name = "副负责岗位名称")
    private String viceLeaderName;

    public String getViceLeaderName() {
        return viceLeaderName;
    }

    public void setViceLeaderName(String viceLeaderName) {
        this.viceLeaderName = viceLeaderName;
    }

    @FieldInfo(name = "是否有权限")
    private String isRight;

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }

    public BaseOrgTree(String id, List<BaseOrgTree> children) {

        super(id, children);
        // TODO Auto-generated constructor stub

    }

    public BaseOrgTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,String parent,Integer orderIndex,
            List<BaseOrgTree> children, String mainLeader, String viceLeader, String outPhone, String inPhone,
            String fax, Integer isSystem, String remark, String code,
            String deptType, String parentType, String mainLeaderName, String viceLeaderName, String isRight) {
        super(id, text, iconCls, leaf, level, treeid,parent,orderIndex, children);
        this.mainLeader = mainLeader;
        this.viceLeader = viceLeader;
        this.outPhone = outPhone;
        this.inPhone = inPhone;
        this.fax = fax;
        this.isSystem = isSystem;
        this.remark = remark;
        this.code = code;
        this.deptType = deptType;
        this.parentType = parentType;
        this.mainLeaderName = mainLeaderName;
        this.viceLeaderName = viceLeaderName;
        this.isRight = isRight;
    }

}
