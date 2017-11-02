
package com.zd.school.jw.train.service.Impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.train.dao.TrainTeacherOrderDescDao;
import com.zd.school.jw.train.model.TrainTeacherOrderDesc;
import com.zd.school.jw.train.service.TrainTeacherOrderDescService;

@Service
@Transactional
public class TrainTeacherOrderDescServiceImpl extends BaseServiceImpl<TrainTeacherOrderDesc> implements TrainTeacherOrderDescService{

    @Resource
    public void setTrainTeacherOrderDescDao(TrainTeacherOrderDescDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(TrainTeacherOrderDescServiceImpl.class);
	
	
}