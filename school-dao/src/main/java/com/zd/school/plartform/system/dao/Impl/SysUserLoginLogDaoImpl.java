package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysUserLoginLogDao;
import com.zd.school.plartform.system.model.SysUserLoginLog;

@Repository
public class SysUserLoginLogDaoImpl  extends BaseDaoImpl<SysUserLoginLog> implements SysUserLoginLogDao {
    public SysUserLoginLogDaoImpl() {
        super(SysUserLoginLog.class);
        // TODO Auto-generated constructor stub
    }
}