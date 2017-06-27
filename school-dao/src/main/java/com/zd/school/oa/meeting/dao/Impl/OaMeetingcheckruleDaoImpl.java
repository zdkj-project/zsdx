package com.zd.school.oa.meeting.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.meeting.dao.OaMeetingcheckruleDao ;
import com.zd.school.oa.meeting.model.OaMeetingcheckrule ;


/**
 * 
 * ClassName: OaMeetingcheckruleDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议考勤规则(OA_T_MEETINGCHECKRULE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaMeetingcheckruleDaoImpl extends BaseDaoImpl<OaMeetingcheckrule> implements OaMeetingcheckruleDao {
    public OaMeetingcheckruleDaoImpl() {
        super(OaMeetingcheckrule.class);
        // TODO Auto-generated constructor stub
    }
}