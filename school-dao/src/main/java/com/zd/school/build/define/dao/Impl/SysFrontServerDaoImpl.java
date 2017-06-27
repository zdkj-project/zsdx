package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.SysFrontServerDao;
import com.zd.school.build.define.model.SysFrontServer;


/**
 * 综合前置管理
 * @author hucy
 *
 */
@Repository
public class SysFrontServerDaoImpl extends BaseDaoImpl<SysFrontServer> implements SysFrontServerDao {
    public SysFrontServerDaoImpl() {
        super(SysFrontServer.class);
        // TODO Auto-generated constructor stub
    }
}