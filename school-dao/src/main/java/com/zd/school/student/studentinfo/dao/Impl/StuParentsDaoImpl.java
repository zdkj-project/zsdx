package com.zd.school.student.studentinfo.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.student.studentinfo.dao.StuParentsDao ;
import com.zd.school.student.studentinfo.model.StuParents ;


/**
 * 
 * ClassName: StuParentsDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生家长信息实体Dao接口实现类.
 * date: 2016-08-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class StuParentsDaoImpl extends BaseDaoImpl<StuParents> implements StuParentsDao {
    public StuParentsDaoImpl() {
        super(StuParents.class);
        // TODO Auto-generated constructor stub
    }
}