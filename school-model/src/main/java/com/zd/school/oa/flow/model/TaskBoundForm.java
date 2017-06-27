package com.zd.school.oa.flow.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * 动态form
 *
 */
@Entity
@Table(name = "FLOW_T_TASKBOUNDFORM")
@AttributeOverride(name = "uuid", column = @Column(name = "ID", length = 36, nullable = false))
public class TaskBoundForm extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;



	@FieldInfo(name = "FORMID")
	@Column(name = "FORMID", length = 36)
	private String formId;
	
	@FieldInfo(name = "TASKID")
	@Column(name = "TASKID", length = 200)
	private String taskId;


	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	
}
