package com.zd.school.oa.flow.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.flow.dao.TaskBoundFormDao;
import com.zd.school.oa.flow.model.TaskBoundForm;
import com.zd.school.oa.flow.service.TaskBoundFormService;

@Service
@Transactional
public class TaskBoundFormServiceImpl extends BaseServiceImpl<TaskBoundForm> implements TaskBoundFormService{

    @Resource
    public void setDynamicFormDao(TaskBoundFormDao dao) {
        this.dao = dao;
    }

}