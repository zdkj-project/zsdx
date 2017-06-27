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
@Table(name = "FLOW_T_DYNAMICFORM")
@AttributeOverride(name = "uuid", column = @Column(name = "FORMID", length = 36, nullable = false))
public class DynamicForm extends BaseEntity implements Serializable {
	
	/**
	 * extField01 流程分类
	 * extField02 备注
	 */

	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "表单内容")
	@Column(name = "FORM", length = 6000)
	private String form;

	@FieldInfo(name = "表单名")
	@Column(name = "FORMTYPE", length = 100)
	private String formType;

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

}
