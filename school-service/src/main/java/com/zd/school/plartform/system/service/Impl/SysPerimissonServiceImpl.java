package com.zd.school.plartform.system.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.model.SysPermission ;
import com.zd.school.plartform.system.dao.SysPerimissonDao ;
import com.zd.school.plartform.system.service.SysPerimissonService ;

/**
 * 
 * ClassName: BaseTPerimissonServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 权限表实体Service接口实现类.
 * date: 2016-07-17
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysPerimissonServiceImpl extends BaseServiceImpl<SysPermission> implements SysPerimissonService{

    @Resource
    public void setBaseTPerimissonDao(SysPerimissonDao dao) {
        this.dao = dao;
    }

}