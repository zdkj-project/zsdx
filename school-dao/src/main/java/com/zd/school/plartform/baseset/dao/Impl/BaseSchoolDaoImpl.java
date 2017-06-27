package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseSchoolDao ;
import com.zd.school.plartform.baseset.model.BaseSchool ;


/**
 * 
 * ClassName: BaseSchoolDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学校信息实体Dao接口实现类.
 * date: 2016-08-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseSchoolDaoImpl extends BaseDaoImpl<BaseSchool> implements BaseSchoolDao {
    public BaseSchoolDaoImpl() {
        super(BaseSchool.class);
        // TODO Auto-generated constructor stub
    }
}