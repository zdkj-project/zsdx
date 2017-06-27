/**
 * Project Name:jw-model
 * File Name:WfPorcessDefinition.java
 * Package Name:com.zd.school.jw.model.workflow
 * Date:2016年3月30日上午11:25:28
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.model;

import java.io.Serializable;

import com.zd.core.annotation.FieldInfo;

/**
 * ClassName:WfPorcessDefinition Function:
 * 流程定义实体定义，本实体继承了基础实体，但同时实现了activiti的ProcessDefinition接口. Reason: TODO ADD
 * REASON. Date: 2016年3月30日 上午11:25:28
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class WfPorcessDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "流程ID")
	private String id;

	@FieldInfo(name = "部署ID")
	private String deploymentId;

	@FieldInfo(name = "流程名称")
	private String name;

	@FieldInfo(name = "流程描述")
	private String description;

	@FieldInfo(name = "图片资源")
	private String diagramResourceName;

	@FieldInfo(name = "XML资源")
	private String resourceName;

	@FieldInfo(name = "关键字")
	private String key;

	@FieldInfo(name = "版本")
	private Integer version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
