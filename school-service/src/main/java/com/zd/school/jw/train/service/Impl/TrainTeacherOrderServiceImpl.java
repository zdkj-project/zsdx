

package com.zd.school.jw.train.service.Impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.train.dao.TrainTeacherOrderDao;
import com.zd.school.jw.train.model.TrainTeacherOrder;
import com.zd.school.jw.train.service.TrainTeacherOrderService;

@Service
@Transactional
public class TrainTeacherOrderServiceImpl extends BaseServiceImpl<TrainTeacherOrder> implements  TrainTeacherOrderService{

    @Resource
    public void setTrainTeacherOrderDao( TrainTeacherOrderDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(TrainTeacherOrderServiceImpl.class);
	
	
}