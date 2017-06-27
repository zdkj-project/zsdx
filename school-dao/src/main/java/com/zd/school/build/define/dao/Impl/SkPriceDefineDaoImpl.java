package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.SkPriceDefineDao;
import com.zd.school.build.define.model.SkPriceDefine;


/**
 * 水控费率定义
 * @author hucy
 *
 */
@Repository
public class SkPriceDefineDaoImpl extends BaseDaoImpl<SkPriceDefine> implements SkPriceDefineDao {
    public SkPriceDefineDaoImpl() {
        super(SkPriceDefine.class);
        // TODO Auto-generated constructor stub
    }
}