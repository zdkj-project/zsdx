package com.zd.school.control.device.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.control.device.dao.PtGatewayDao;
import com.zd.school.control.device.model.PtGateway;

/**
 * 网关表
 * @author hucy
 *
 */
@Repository
public class PtGatewayDaoImpl extends BaseDaoImpl<PtGateway> implements PtGatewayDao{
	
	public PtGatewayDaoImpl() {
		super(PtGateway.class);
	}

}
