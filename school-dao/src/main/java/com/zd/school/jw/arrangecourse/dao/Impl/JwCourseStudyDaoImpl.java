package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.JwCourseStudyDao ;
import com.zd.school.jw.arrangecourse.model.JwCourseStudy ;


/**
 * 
 * ClassName: JwCourseStudyDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 自习课程表实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwCourseStudyDaoImpl extends BaseDaoImpl<JwCourseStudy> implements JwCourseStudyDao {
    public JwCourseStudyDaoImpl() {
        super(JwCourseStudy.class);
        // TODO Auto-generated constructor stub
    }
}