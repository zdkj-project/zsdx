package com.zd.school.student.studentclass.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.student.studentclass.dao.StuDivideparamDao ;
import com.zd.school.student.studentclass.model.StuDivideparam ;


/**
 * 
 * ClassName: StuDivideparamDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班参数实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class StuDivideparamDaoImpl extends BaseDaoImpl<StuDivideparam> implements StuDivideparamDao {
    public StuDivideparamDaoImpl() {
        super(StuDivideparam.class);
        // TODO Auto-generated constructor stub
    }
}