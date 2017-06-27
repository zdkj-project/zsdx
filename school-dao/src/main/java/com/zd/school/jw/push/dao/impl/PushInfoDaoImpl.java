package com.zd.school.jw.push.dao.impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.push.dao.PushInfoDao;
import com.zd.school.jw.push.model.PushInfo;

@Repository
public class PushInfoDaoImpl extends BaseDaoImpl<PushInfo> implements PushInfoDao {
    public PushInfoDaoImpl() {
        super(PushInfo.class);
        // TODO Auto-generated constructor stub
    }
}
