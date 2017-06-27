package com.zd.school.plartform.system.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.dao.SysUserLoginLogDao;
import com.zd.school.plartform.system.model.SysUserLoginLog;
import com.zd.school.plartform.system.service.SysUserLoginLogService;

@Service
@Transactional
public class SysUserLoginLogServiceImpl extends BaseServiceImpl<SysUserLoginLog> implements SysUserLoginLogService {

    @Resource
    public void setSysUserLoginLogDao(SysUserLoginLogDao dao) {
        this.dao = dao;
    }
    
}
