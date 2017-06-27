package com.zd.school.build.define.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.define.dao.SkPriceDefineDao;
import com.zd.school.build.define.model.SkPriceDefine;
import com.zd.school.build.define.service.SkPriceDefineService;

/**
 * 水控费率定义
 * @author hucy
 *
 */
@Service
@Transactional
public class SkPriceDefineServiceImpl extends BaseServiceImpl<SkPriceDefine> implements SkPriceDefineService{

    @Resource
    public void setSkPriceDefineDao(SkPriceDefineDao dao) {
        this.dao = dao;
    }

}