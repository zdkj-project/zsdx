package com.zd.school.build.allot.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.allot.model.JwOfficeAllot ;
import com.zd.school.build.allot.dao.JwOfficeAllotDao ;
import com.zd.school.build.allot.service.JwOfficeAllotService ;

/**
 * 
 * ClassName: JwOfficeallotServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_OFFICEALLOT实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwOfficeAllotServiceImpl extends BaseServiceImpl<JwOfficeAllot> implements JwOfficeAllotService{

    @Resource
    public void setJwOfficeallotDao(JwOfficeAllotDao dao) {
        this.dao = dao;
    }

}