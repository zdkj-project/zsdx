package com.zd.school.plartform.baseset.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.baseset.model.BaseDicitem ;
import com.zd.school.plartform.baseset.dao.BaseDicitemDao ;
import com.zd.school.plartform.baseset.service.BaseDicitemService ;

/**
 * 
 * ClassName: BaseDicitemServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 数据字典项实体Service接口实现类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BaseDicitemServiceImpl extends BaseServiceImpl<BaseDicitem> implements BaseDicitemService{

    @Resource
    public void setBaseDicitemDao(BaseDicitemDao dao) {
        this.dao = dao;
    }

}