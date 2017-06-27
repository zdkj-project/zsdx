package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.model.MjUserright ;
import com.zd.school.control.device.dao.MjUserrightDao ;
import com.zd.school.control.device.service.MjUserrightService ;

/**
 * 
 * ClassName: MjUserrightServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 门禁权限表(MJ_UserRight)实体Service接口实现类.
 * date: 2016-09-08
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class MjUserrightServiceImpl extends BaseServiceImpl<MjUserright> implements MjUserrightService{

    @Resource
    public void setMjUserrightDao(MjUserrightDao dao) {
        this.dao = dao;
    }

}