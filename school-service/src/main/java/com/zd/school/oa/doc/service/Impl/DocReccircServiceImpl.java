package com.zd.school.oa.doc.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.doc.model.DocReccirc ;
import com.zd.school.oa.doc.dao.DocReccircDao ;
import com.zd.school.oa.doc.service.DocReccircService ;

/**
 * 
 * ClassName: DocReccircServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文收文传阅人实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocReccircServiceImpl extends BaseServiceImpl<DocReccirc> implements DocReccircService{

    @Resource
    public void setDocReccircDao(DocReccircDao dao) {
        this.dao = dao;
    }

}