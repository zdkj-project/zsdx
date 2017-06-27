package com.zd.school.jw.arrangecourse.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.arrangecourse.model.JwInvigilate ;
import com.zd.school.jw.arrangecourse.dao.JwInvigilateDao ;
import com.zd.school.jw.arrangecourse.service.JwInvigilateService ;

/**
 * 
 * ClassName: JwInvigilateServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_INVIGILATE实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwInvigilateServiceImpl extends BaseServiceImpl<JwInvigilate> implements JwInvigilateService{

    @Resource
    public void setJwInvigilateDao(JwInvigilateDao dao) {
        this.dao = dao;
    }

}