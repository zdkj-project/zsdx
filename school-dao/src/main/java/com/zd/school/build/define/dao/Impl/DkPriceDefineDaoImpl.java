package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.DkPriceDefineDao;
import com.zd.school.build.define.model.DkPriceDefine;


/**
 * 电控费率定义
 * @author hucy
 *
 */
@Repository
public class DkPriceDefineDaoImpl extends BaseDaoImpl<DkPriceDefine> implements DkPriceDefineDao {
    public DkPriceDefineDaoImpl() {
        super(DkPriceDefine.class);
        // TODO Auto-generated constructor stub
    }
}