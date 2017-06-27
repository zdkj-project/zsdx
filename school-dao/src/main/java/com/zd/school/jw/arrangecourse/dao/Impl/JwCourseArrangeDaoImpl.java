package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.JwCourseArrangeDao ;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange ;


/**
 * 
 * ClassName: JwCourseArrangeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 排课课程表实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwCourseArrangeDaoImpl extends BaseDaoImpl<JwCourseArrange> implements JwCourseArrangeDao {
    public JwCourseArrangeDaoImpl() {
        super(JwCourseArrange.class);
        // TODO Auto-generated constructor stub
    }
}