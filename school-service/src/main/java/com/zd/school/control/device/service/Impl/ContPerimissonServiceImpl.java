package com.zd.school.control.device.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.control.device.model.ContPerimisson ;
import com.zd.school.control.device.dao.ContPerimissonDao ;
import com.zd.school.control.device.service.ContPerimissonService ;

/**
 * 
 * ClassName: ContPerimissonServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_PERIMISSON实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class ContPerimissonServiceImpl extends BaseServiceImpl<ContPerimisson> implements ContPerimissonService{

    @Resource
    public void setContPerimissonDao(ContPerimissonDao dao) {
        this.dao = dao;
    }

}