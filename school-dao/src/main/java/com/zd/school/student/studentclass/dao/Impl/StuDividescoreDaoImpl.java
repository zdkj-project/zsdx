package com.zd.school.student.studentclass.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.student.studentclass.dao.StuDividescoreDao ;
import com.zd.school.student.studentclass.model.StuDividescore ;


/**
 * 
 * ClassName: StuDividescoreDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班成绩实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class StuDividescoreDaoImpl extends BaseDaoImpl<StuDividescore> implements StuDividescoreDao {
    public StuDividescoreDaoImpl() {
        super(StuDividescore.class);
        // TODO Auto-generated constructor stub
    }
}