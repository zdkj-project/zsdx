package com.zd.school.push.dao.impl;

import com.zd.school.push.dao.PushInfoDao;
import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.push.model.PushInfo;

@Repository
public class PushInfoDaoImpl extends BaseDaoImpl<PushInfo> implements PushInfoDao {
    public PushInfoDaoImpl() {
        super(PushInfo.class);
        // TODO Auto-generated constructor stub
    }
}
