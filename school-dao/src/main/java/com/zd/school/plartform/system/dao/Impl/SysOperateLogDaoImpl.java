
package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysOperateLogDao;
import com.zd.school.plartform.system.model.SysOperateLog;

@Repository
public class SysOperateLogDaoImpl  extends BaseDaoImpl<SysOperateLog> implements SysOperateLogDao {
    public SysOperateLogDaoImpl() {
        super(SysOperateLog.class);
        // TODO Auto-generated constructor stub
    }
}