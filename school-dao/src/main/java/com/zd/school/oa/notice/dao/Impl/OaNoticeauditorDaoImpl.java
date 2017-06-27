package com.zd.school.oa.notice.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.notice.dao.OaNoticeauditorDao ;
import com.zd.school.oa.notice.model.OaNoticeauditor ;


/**
 * 
 * ClassName: OaNoticeauditorDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告审核人(OA_T_NOTICEAUDITOR)实体Dao接口实现类.
 * date: 2016-12-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaNoticeauditorDaoImpl extends BaseDaoImpl<OaNoticeauditor> implements OaNoticeauditorDao {
    public OaNoticeauditorDaoImpl() {
        super(OaNoticeauditor.class);
        // TODO Auto-generated constructor stub
    }
}