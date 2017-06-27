package com.zd.school.oa.notice.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.notice.dao.OaNoticeDao ;
import com.zd.school.oa.notice.model.OaNotice ;


/**
 * 
 * ClassName: OaNoticeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告信息表(OA_T_NOTICE)实体Dao接口实现类.
 * date: 2016-12-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaNoticeDaoImpl extends BaseDaoImpl<OaNotice> implements OaNoticeDao {
    public OaNoticeDaoImpl() {
        super(OaNotice.class);
        // TODO Auto-generated constructor stub
    }
}