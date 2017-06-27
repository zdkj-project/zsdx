/**
 * Project Name:jw-model
 * File Name:FlowType.java
 * Package Name:com.zd.school.jw.model.workflow
 * Date:2016年5月23日下午3:20:40
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.TreeNodeEntity;

/**
 * ClassName:FlowType Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年5月23日 下午3:20:40
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "OA_T_FLOWTYPE")
@AttributeOverride(name = "uuid", column = @Column(name = "FLOWTYPE_ID", length = 36, nullable = false) )
public class FlowType extends TreeNodeEntity implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "标识键")
	@Column(name = "FLOW_KEY", length = 255)
	private String flowKey;

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	@FieldInfo(name = "备注")
	@Column(name = "REMARK", length = 255)
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public FlowType() {
		super();
	}
}
