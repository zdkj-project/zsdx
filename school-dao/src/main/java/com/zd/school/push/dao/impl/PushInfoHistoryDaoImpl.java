package com.zd.school.push.dao.impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.push.dao.PushInfoHistoryDao;
import com.zd.school.push.model.PushInfoHistory;

@Repository
public class PushInfoHistoryDaoImpl extends BaseDaoImpl<PushInfoHistory> implements PushInfoHistoryDao {
    public PushInfoHistoryDaoImpl() {
        super(PushInfoHistory.class);
        // TODO Auto-generated constructor stub
    }
}
