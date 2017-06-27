package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.dao.PtGatewayDao;
import com.zd.school.control.device.model.PtGateway;
import com.zd.school.control.device.service.PtGatewayService;

/**
 * 网关表
 * @author hucy
 *
 */
@Service
@Transactional
public class PtGatewayServiceImpl extends BaseServiceImpl<PtGateway> implements PtGatewayService{
	
	@Resource
    public void setPtGatewayDao(PtGatewayDao dao) {
        this.dao = dao;
    }
}
