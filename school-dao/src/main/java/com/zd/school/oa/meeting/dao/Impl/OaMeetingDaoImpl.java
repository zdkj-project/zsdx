package com.zd.school.oa.meeting.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.meeting.dao.OaMeetingDao ;
import com.zd.school.oa.meeting.model.OaMeeting ;


/**
 * 
 * ClassName: OaMeetingDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议信息(OA_T_MEETING)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaMeetingDaoImpl extends BaseDaoImpl<OaMeeting> implements OaMeetingDao {
    public OaMeetingDaoImpl() {
        super(OaMeeting.class);
        // TODO Auto-generated constructor stub
    }
}