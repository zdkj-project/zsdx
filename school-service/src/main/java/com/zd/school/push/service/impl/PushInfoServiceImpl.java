package com.zd.school.push.service.impl;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.push.dao.PushInfoDao;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class PushInfoServiceImpl extends BaseServiceImpl<PushInfo> implements PushInfoService {

    @Resource
    public void setPushInfoDao(PushInfoDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean pushInfo(String empName, String mobile, String eventType, String info, Integer pushWay) {
        Boolean br = false;
        PushInfo pushInfo = new PushInfo();
        pushInfo.setEmplName(empName);
        pushInfo.setEmplNo(mobile);
        pushInfo.setRegTime(new Date());
        pushInfo.setEventType(eventType);
        pushInfo.setPushStatus(0);
        pushInfo.setPushWay(pushWay);
        pushInfo.setRegStatus(info);
        pushInfo.setPushUrl("");
        this.persist(pushInfo);
        br = true;
        return br;
    }

    @Override
    public boolean pushInfo(String empName, String empNo, String eventType, String regStatus, Integer pushWay, String pushUrl) {
        Boolean br = false;
        PushInfo pushInfo = new PushInfo();
        pushInfo.setEmplName(empName);
        pushInfo.setEmplNo(empNo);
        pushInfo.setRegTime(new Date());
        pushInfo.setEventType(eventType);
        pushInfo.setPushStatus(0);
        pushInfo.setPushWay(pushWay);
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
