package com.zd.school.student.studentclass.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.student.studentclass.dao.JwClassstudentDao ;
import com.zd.school.student.studentclass.model.JwClassstudent ;


/**
 * 
 * ClassName: JwClassstudentDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班信息(JW_T_CLASSSTUDENT)实体Dao接口实现类.
 * date: 2016-08-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwClassstudentDaoImpl extends BaseDaoImpl<JwClassstudent> implements JwClassstudentDao {
    public JwClassstudentDaoImpl() {
        super(JwClassstudent.class);
        // TODO Auto-generated constructor stub
    }
}