package com.zd.school.student.studentinfo.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.student.studentinfo.dao.StuBaseinfoDao ;
import com.zd.school.student.studentinfo.model.StuBaseinfo ;


/**
 * 
 * ClassName: StuBaseinfoDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生基本信息实体Dao接口实现类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class StuBaseinfoDaoImpl extends BaseDaoImpl<StuBaseinfo> implements StuBaseinfoDao {
    public StuBaseinfoDaoImpl() {
        super(StuBaseinfo.class);
        // TODO Auto-generated constructor stub
    }
}