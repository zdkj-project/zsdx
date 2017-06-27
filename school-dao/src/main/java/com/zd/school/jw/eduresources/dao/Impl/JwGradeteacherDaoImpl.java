package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwGradeteacherDao ;
import com.zd.school.jw.eduresources.model.JwGradeteacher ;


/**
 * 
 * ClassName: JwGradeteacherDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 年级组长信息实体Dao接口实现类.
 * date: 2016-08-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwGradeteacherDaoImpl extends BaseDaoImpl<JwGradeteacher> implements JwGradeteacherDao {
    public JwGradeteacherDaoImpl() {
        super(JwGradeteacher.class);
        // TODO Auto-generated constructor stub
    }
}