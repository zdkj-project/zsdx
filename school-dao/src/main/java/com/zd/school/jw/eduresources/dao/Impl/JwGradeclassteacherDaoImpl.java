package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwGradeclassteacherDao ;
import com.zd.school.jw.eduresources.model.JwGradeclassteacher ;


/**
 * 
 * ClassName: JwGradeclassteacherDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 年级组长信息(JW_T_GRADECLASSTEACHER)实体Dao接口实现类.
 * date: 2016-09-20
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwGradeclassteacherDaoImpl extends BaseDaoImpl<JwGradeclassteacher> implements JwGradeclassteacherDao {
    public JwGradeclassteacherDaoImpl() {
        super(JwGradeclassteacher.class);
        // TODO Auto-generated constructor stub
    }
}