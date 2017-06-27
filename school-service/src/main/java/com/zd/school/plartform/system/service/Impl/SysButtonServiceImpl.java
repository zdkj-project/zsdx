package com.zd.school.plartform.system.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.model.SysButton ;
import com.zd.school.plartform.system.dao.SysButtonDao ;
import com.zd.school.plartform.system.service.SysButtonService ;

/**
 * 
 * ClassName: BaseTButtonServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 功能按钮实体Service接口实现类.
 * date: 2016-07-17
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysButtonServiceImpl extends BaseServiceImpl<SysButton> implements SysButtonService{

    @Resource
    public void setBaseTButtonDao(SysButtonDao dao) {
        this.dao = dao;
    }

}