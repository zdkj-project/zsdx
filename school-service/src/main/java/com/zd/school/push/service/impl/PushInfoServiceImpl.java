package com.zd.school.push.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.push.dao.PushInfoDao;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;

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
        this.doPersist(pushInfo);
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
        this.doPersist(pushInfo);
        br = true;
        return br;
    }
    
    @Override
    public boolean pushInfo(String userId,String empId,String empName, String mobile, String eventType, String info, Integer pushWay) {
        Boolean br = false;
        PushInfo pushInfo = new PushInfo();
        pushInfo.setEmplName(empName);	//收信人
        pushInfo.setEmplNo(mobile);		//收信人手机号
        pushInfo.setRegTime(new Date());	//发信息时间
        pushInfo.setEventType(eventType);	//事件类型
        pushInfo.setPushStatus(0);		//推送的状态  0：未推送	 1：正在推送	2：已推送	 -1：推送失败
        pushInfo.setPushWay(pushWay);	//推送类型  1:微信 2:APP 3:短信
        pushInfo.setRegStatus(info);	//推送的信息
        pushInfo.setPushUrl("");
        pushInfo.setCreateUser(userId);	//发信人的人员id
        pushInfo.setEmplId(empId);		//收信人的人员id
        this.doPersist(pushInfo);
        br = true;
        return br;
    }

}
