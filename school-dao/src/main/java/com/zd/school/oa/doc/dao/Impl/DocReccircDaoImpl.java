package com.zd.school.oa.doc.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.doc.dao.DocReccircDao ;
import com.zd.school.oa.doc.model.DocReccirc ;


/**
 * 
 * ClassName: DocReccircDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文收文传阅人实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class DocReccircDaoImpl extends BaseDaoImpl<DocReccirc> implements DocReccircDao {
    public DocReccircDaoImpl() {
        super(DocReccirc.class);
        // TODO Auto-generated constructor stub
    }
}