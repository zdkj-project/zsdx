package com.zd.school.oa.doc.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.doc.model.DocDoctype ;
import com.zd.school.oa.doc.dao.DocDoctypeDao ;
import com.zd.school.oa.doc.service.DocDoctypeService ;

/**
 * 
 * ClassName: DocDoctypeServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文类型表实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocDoctypeServiceImpl extends BaseServiceImpl<DocDoctype> implements DocDoctypeService{

    @Resource
    public void setDocDoctypeDao(DocDoctypeDao dao) {
        this.dao = dao;
    }

}