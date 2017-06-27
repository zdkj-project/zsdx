package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.model.ContDevice ;
import com.zd.school.control.device.dao.ContDeviceDao ;
import com.zd.school.control.device.service.ContDeviceService ;

/**
 * 
 * ClassName: ContDeviceServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_DEVICE实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class ContDeviceServiceImpl extends BaseServiceImpl<ContDevice> implements ContDeviceService{

    @Resource
    public void setContDeviceDao(ContDeviceDao dao) {
        this.dao = dao;
    }

}