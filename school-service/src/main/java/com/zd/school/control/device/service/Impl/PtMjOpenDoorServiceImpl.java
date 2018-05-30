package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.dao.PtMjOpenDoorDao;
import com.zd.school.control.device.model.PtMjOpenDoor;
import com.zd.school.control.device.service.PtMjOpenDoorService;

@Service
@Transactional
public class PtMjOpenDoorServiceImpl extends BaseServiceImpl<PtMjOpenDoor> implements PtMjOpenDoorService{

    @Resource
    public void setPtMjOpenDoorDao(PtMjOpenDoorDao dao) {
        this.dao = dao;
    }

}