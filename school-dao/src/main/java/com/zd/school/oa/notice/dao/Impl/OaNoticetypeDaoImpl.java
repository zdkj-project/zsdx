package com.zd.school.oa.notice.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.notice.dao.OaNoticetypeDao ;
import com.zd.school.oa.notice.model.OaNoticetype ;


/**
 * 
 * ClassName: OaNoticetypeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告类型(OA_T_NOTICETYPE)实体Dao接口实现类.
 * date: 2016-09-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaNoticetypeDaoImpl extends BaseDaoImpl<OaNoticetype> implements OaNoticetypeDao {
    public OaNoticetypeDaoImpl() {
        super(OaNoticetype.class);
        // TODO Auto-generated constructor stub
    }
}