package com.zd.school.oa.flow.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.flow.dao.DynamicFormDao;
import com.zd.school.oa.flow.model.DynamicForm;

@Repository
public class DynamicFormImpl extends BaseDaoImpl<DynamicForm> implements DynamicFormDao {

	public DynamicFormImpl() {

		super(DynamicForm.class);
		// TODO Auto-generated constructor stub

	}

}
