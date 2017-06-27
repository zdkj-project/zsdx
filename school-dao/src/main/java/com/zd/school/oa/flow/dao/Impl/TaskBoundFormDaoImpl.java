package com.zd.school.oa.flow.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.flow.dao.TaskBoundFormDao;
import com.zd.school.oa.flow.model.TaskBoundForm;

@Repository
public class TaskBoundFormDaoImpl extends BaseDaoImpl<TaskBoundForm> implements TaskBoundFormDao {

	public TaskBoundFormDaoImpl() {

		super(TaskBoundForm.class);
		// TODO Auto-generated constructor stub

	}

}
