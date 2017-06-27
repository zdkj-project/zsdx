package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.dao.PtTermDao;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.service.PtTermService;

@Service
@Transactional
public class PtTermServiceImpl extends BaseServiceImpl<PtTerm> implements PtTermService{
	
	@Resource
    public void setPtTermDao(PtTermDao dao) {
        this.dao = dao;
    }
}
