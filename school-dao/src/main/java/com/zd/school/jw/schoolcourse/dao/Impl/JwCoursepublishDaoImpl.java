package com.zd.school.jw.schoolcourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.schoolcourse.dao.JwCoursepublishDao ;
import com.zd.school.jw.schoolcourse.model.JwCoursepublish ;


/**
 * 
 * ClassName: JwCoursepublishDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校本课程发布信息(JW_T_COURSEPUBLISH)实体Dao接口实现类.
 * date: 2016-11-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwCoursepublishDaoImpl extends BaseDaoImpl<JwCoursepublish> implements JwCoursepublishDao {
    public JwCoursepublishDaoImpl() {
        super(JwCoursepublish.class);
        // TODO Auto-generated constructor stub
    }
}