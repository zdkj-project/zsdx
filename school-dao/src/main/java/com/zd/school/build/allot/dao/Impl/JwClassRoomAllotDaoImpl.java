package com.zd.school.build.allot.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.allot.dao.JwClassRoomAllotDao;
import com.zd.school.build.allot.model.JwClassRoomAllot;


/**
 * 
 * ClassName: JwClassroomallotDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_CLASSROOMALLOT实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwClassRoomAllotDaoImpl extends BaseDaoImpl<JwClassRoomAllot> implements JwClassRoomAllotDao {
    public JwClassRoomAllotDaoImpl() {
        super(JwClassRoomAllot.class);
        // TODO Auto-generated constructor stub
    }
}