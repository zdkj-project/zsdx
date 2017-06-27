package com.zd.school.oa.flow.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.flow.dao.LeaveApplayDao;
import com.zd.school.oa.flow.model.LeaveApplay;

@Repository
public class LeaveApplayDaoImpl extends BaseDaoImpl<LeaveApplay> implements LeaveApplayDao {

    public LeaveApplayDaoImpl() {
        super(LeaveApplay.class);
        // TODO Auto-generated constructor stub
    }

}
