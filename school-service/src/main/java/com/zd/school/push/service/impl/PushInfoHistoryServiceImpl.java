package com.zd.school.push.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.push.dao.PushInfoHistoryDao;
import com.zd.school.push.model.PushInfoHistory;
import com.zd.school.push.service.PushInfoHistoryService;

@Service
@Transactional
public class PushInfoHistoryServiceImpl extends BaseServiceImpl<PushInfoHistory> implements PushInfoHistoryService {

    @Resource
    public void setPushInfoHistoryDao(PushInfoHistoryDao dao) {
        this.dao = dao;
    }


}
