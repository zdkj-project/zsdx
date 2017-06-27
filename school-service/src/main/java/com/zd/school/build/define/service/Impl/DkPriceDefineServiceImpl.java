package com.zd.school.build.define.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.define.dao.DkPriceDefineDao;
import com.zd.school.build.define.model.DkPriceDefine;
import com.zd.school.build.define.service.DkPriceDefineService;

/**
 * 电控费率定义
 * @author hucy
 *
 */
@Service
@Transactional
public class DkPriceDefineServiceImpl extends BaseServiceImpl<DkPriceDefine> implements DkPriceDefineService{

    @Resource
    public void setDkPriceDefineDao(DkPriceDefineDao dao) {
        this.dao = dao;
    }

}