package com.zd.school.plartform.system.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.model.SysDatapermission ;
import com.zd.school.plartform.system.dao.SysDatapermissionDao ;
import com.zd.school.plartform.system.service.SysDatapermissionService ;

/**
 * 
 * ClassName: SysDatapermissionServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 用户数据权限表(SYS_T_DATAPERMISSION)实体Service接口实现类.
 * date: 2016-09-01
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysDatapermissionServiceImpl extends BaseServiceImpl<SysDatapermission> implements SysDatapermissionService{

    @Resource
    public void setSysDatapermissionDao(SysDatapermissionDao dao) {
        this.dao = dao;
    }

}