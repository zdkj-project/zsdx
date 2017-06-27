package com.zd.school.oa.doc.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.doc.dao.DocSenddocDao ;
import com.zd.school.oa.doc.model.DocSenddoc ;


/**
 * 
 * ClassName: DocSenddocDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文发文单实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class DocSenddocDaoImpl extends BaseDaoImpl<DocSenddoc> implements DocSenddocDao {
    public DocSenddocDaoImpl() {
        super(DocSenddoc.class);
        // TODO Auto-generated constructor stub
    }
}