package com.zd.school.plartform.system.service.Impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.dao.SysOperateLogDao;
import com.zd.school.plartform.system.model.SysOperateLog;
import com.zd.school.plartform.system.service.SysOperateLogService;

@Service
@Transactional
public class SysOperateLogServiceImpl extends BaseServiceImpl<SysOperateLog> implements SysOperateLogService {

    @Resource
    public void setSysOperateLogDao(SysOperateLogDao dao) {
        this.dao = dao;
    }
    
}

