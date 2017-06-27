package com.zd.school.jw.push.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.push.dao.PushInfoDao;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;

@Service
@Transactional
public class PushInfoServiceImpl extends BaseServiceImpl<PushInfo> implements PushInfoService {

    @Resource
    public void setPushInfoDao(PushInfoDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean pushInfo(String empName, String empNo, String eventType, String regStatus) {
        return this.pushInfo(empName, empNo, eventType, regStatus, null);
    }

    @Override
    public boolean pushInfo(String empName, String empNo, String eventType, String regStatus, String pushUrl) {
        Boolean br = false;
        PushInfo pushInfo = new PushInfo();
        pushInfo.setEmplName(empName);
        pushInfo.setEmplNo(empNo);
        pushInfo.setRegTime(new Date());
        pushInfo.setEventType(eventType);
        pushInfo.setPushStatus(0);
        pushInfo.setPushWay(1);
        pushInfo.setRegStatus(regStatus);
        if (StringUtils.isEmpty(pushUrl))
            pushInfo.setPushUrl("");
        else
            pushInfo.setPushUrl(pushUrl);
        this.persist(pushInfo);
        br = true;
        return br;
    }

}
