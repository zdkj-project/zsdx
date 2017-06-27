package com.zd.school.teacher.teacherinfo.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.teacher.teacherinfo.dao.TeaTeacherbaseDao ;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase ;


/**
 * 
 * ClassName: TeaTeacherbaseDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教职工基本数据实体Dao接口实现类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TeaTeacherbaseDaoImpl extends BaseDaoImpl<TeaTeacherbase> implements TeaTeacherbaseDao {
    public TeaTeacherbaseDaoImpl() {
        super(TeaTeacherbase.class);
        // TODO Auto-generated constructor stub
    }
}