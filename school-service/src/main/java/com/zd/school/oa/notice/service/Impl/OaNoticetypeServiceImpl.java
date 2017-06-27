package com.zd.school.oa.notice.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.notice.model.OaNoticetype ;
import com.zd.school.oa.notice.dao.OaNoticetypeDao ;
import com.zd.school.oa.notice.service.OaNoticetypeService ;

/**
 * 
 * ClassName: OaNoticetypeServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告类型(OA_T_NOTICETYPE)实体Service接口实现类.
 * date: 2016-09-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaNoticetypeServiceImpl extends BaseServiceImpl<OaNoticetype> implements OaNoticetypeService{

    @Resource
    public void setOaNoticetypeDao(OaNoticetypeDao dao) {
        this.dao = dao;
    }

}