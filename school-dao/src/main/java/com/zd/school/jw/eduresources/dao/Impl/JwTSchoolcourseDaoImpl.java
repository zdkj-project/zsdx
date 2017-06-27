package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwTSchoolcourseDao;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;

/**
 * 
 * ClassName: JwTSchoolcourseDaoImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 校本课程实体Dao接口实现类. date: 2016-03-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwTSchoolcourseDaoImpl extends BaseDaoImpl<JwTSchoolcourse> implements JwTSchoolcourseDao {
    public JwTSchoolcourseDaoImpl() {
        super(JwTSchoolcourse.class);
        // TODO Auto-generated constructor stub
    }
}