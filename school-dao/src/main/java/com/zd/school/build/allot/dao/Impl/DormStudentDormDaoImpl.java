package com.zd.school.build.allot.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.allot.dao.DormStudentDormDao ;
import com.zd.school.build.allot.model.DormStudentDorm;


/**
 * 
 * ClassName: DormStudentdormDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (学生分配宿舍)实体Dao接口实现类.
 * date: 2016-08-26
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class DormStudentDormDaoImpl extends BaseDaoImpl<DormStudentDorm> implements DormStudentDormDao {
    public DormStudentDormDaoImpl() {
        super(DormStudentDorm.class);
        // TODO Auto-generated constructor stub
    }
}