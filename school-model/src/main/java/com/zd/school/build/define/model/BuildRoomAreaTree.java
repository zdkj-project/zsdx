/**
 * Project Name:school-model
 * File Name:BuildRoomAreaTree.java
 * Package Name:com.zd.school.build.define.model
 * Date:2016年8月24日上午9:55:33
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.build.define.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNode;

import java.util.List;

/**
 * ClassName:BuildRoomAreaTree Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年8月24日 上午9:55:33
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class BuildRoomAreaTree extends ExtTreeNode<BuildRoomAreaTree> {
    @FieldInfo(name = "区域编码")
    private String areaCode;

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    @FieldInfo(name = "区域类型")
    private String areaType;

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaType() {
        return areaType;
    }

    @FieldInfo(name = "区域状态")
    private Integer areaStatu;

    public void setAreaStatu(Integer areaStatu) {
        this.areaStatu = areaStatu;
    }

    public Integer getAreaStatu() {
        return areaStatu;
    }

    @FieldInfo(name = "区域说明")
    private String areaDesc;

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public String getAreaDesc() {
        return areaDesc;
    }

    @FieldInfo(name = "区域地址")
    private String areaAddr;

    public void setAreaAddr(String areaAddr) {
        this.areaAddr = areaAddr;
    }

    public String getAreaAddr() {
        return areaAddr;
    }

    @FieldInfo(name = "上级区域")
    private String parentNode;

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }


    public BuildRoomAreaTree(String id, List<BuildRoomAreaTree> children) {

        super(id, children);

    }

    @FieldInfo(name = "区域房间数")
    private Integer roomCount;

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public BuildRoomAreaTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid, String parent,Integer orderIndex,
            List<BuildRoomAreaTree> children, String areaCode, String areaType, Integer areaStatu, String areaDesc,
            String areaAddr, Integer roomCount) {

        super(id, text, iconCls, leaf, level, treeid,parent,orderIndex, children);
        this.areaCode = areaCode;
        this.areaType = areaType;
        this.areaStatu = areaStatu;
        this.areaDesc = areaDesc;
        this.areaAddr = areaAddr;
        this.roomCount = roomCount;
    }
}
