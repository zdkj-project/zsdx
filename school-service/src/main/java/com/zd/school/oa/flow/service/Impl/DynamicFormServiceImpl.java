package com.zd.school.oa.flow.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.flow.dao.DynamicFormDao;
import com.zd.school.oa.flow.model.DynamicForm;
import com.zd.school.oa.flow.service.DynamicFormService;

@Service
@Transactional
public class DynamicFormServiceImpl extends BaseServiceImpl<DynamicForm> implements DynamicFormService{

    @Resource
    public void setDynamicFormDao(DynamicFormDao dao) {
        this.dao = dao;
    }

}