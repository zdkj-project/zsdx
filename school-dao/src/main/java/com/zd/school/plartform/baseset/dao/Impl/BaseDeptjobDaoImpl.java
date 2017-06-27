package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseDeptjobDao ;
import com.zd.school.plartform.baseset.model.BaseDeptjob ;


/**
 * 
 * ClassName: BaseDeptjobDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 部门岗位信息(BASE_T_DEPTJOB)实体Dao接口实现类.
 * date: 2017-03-27
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseDeptjobDaoImpl extends BaseDaoImpl<BaseDeptjob> implements BaseDeptjobDao {
    public BaseDeptjobDaoImpl() {
        super(BaseDeptjob.class);
        // TODO Auto-generated constructor stub
    }
}