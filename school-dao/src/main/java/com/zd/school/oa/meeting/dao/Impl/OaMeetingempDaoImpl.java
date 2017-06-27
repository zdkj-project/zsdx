package com.zd.school.oa.meeting.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.meeting.dao.OaMeetingempDao ;
import com.zd.school.oa.meeting.model.OaMeetingemp ;


/**
 * 
 * ClassName: OaMeetingempDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议人员信息(OA_T_MEETINGEMP)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaMeetingempDaoImpl extends BaseDaoImpl<OaMeetingemp> implements OaMeetingempDao {
    public OaMeetingempDaoImpl() {
        super(OaMeetingemp.class);
        // TODO Auto-generated constructor stub
    }
}