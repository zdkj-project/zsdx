/**
 * Project Name:jw-model
 * File Name:FlowTypeTree.java
 * Package Name:com.zd.school.jw.model.workflow
 * Date:2016年5月24日上午9:11:36
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNode;

import java.util.List;

/**
 * ClassName:FlowTypeTree Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年5月24日 上午9:11:36
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class FlowTypeTree extends ExtTreeNode<FlowTypeTree> {

	@FieldInfo(name = "分类编码")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@FieldInfo(name = "标识键")
	private String flowKey;

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	@FieldInfo(name = "备注")
	private String remark;

	public String getRemark() {
		return remark;
	}

	@FieldInfo(name = "上级分类")
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

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public FlowTypeTree() {
		super();
	}

	public FlowTypeTree(String id, List<FlowTypeTree> children) {

		super(id, children);
		// TODO Auto-generated constructor stub
	}

	public FlowTypeTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,String parent,
						Integer orderIndex,List<FlowTypeTree> children, String flowKey, String remark, String code) {

		super(id, text, iconCls, leaf, level, treeid,parent,orderIndex, children);
		this.flowKey = flowKey;
		this.remark = remark;
		this.code = code;
/*		this.parent = parent;
		this.orderIndex = orderIndex;*/
	}
}
