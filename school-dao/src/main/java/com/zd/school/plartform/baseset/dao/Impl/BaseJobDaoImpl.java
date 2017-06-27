package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseJobDao;
import com.zd.school.plartform.baseset.model.BaseJob;

/**
 * 
 * ClassName: BizTJobDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 岗位信息实体Dao接口实现类.
 * date: 2016-05-16
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseJobDaoImpl extends BaseDaoImpl<BaseJob> implements BaseJobDao {
    public BaseJobDaoImpl() {
        super(BaseJob.class);
        // TODO Auto-generated constructor stub
    }
}