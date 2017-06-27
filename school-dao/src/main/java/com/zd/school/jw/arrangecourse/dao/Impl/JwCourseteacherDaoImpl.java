package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.JwCourseteacherDao ;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher ;


/**
 * 
 * ClassName: JwCourseteacherDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教师任课信息(JW_T_COURSETEACHER)实体Dao接口实现类.
 * date: 2016-08-26
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwCourseteacherDaoImpl extends BaseDaoImpl<JwCourseteacher> implements JwCourseteacherDao {
    public JwCourseteacherDaoImpl() {
        super(JwCourseteacher.class);
        // TODO Auto-generated constructor stub
    }
}