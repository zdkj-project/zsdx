package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseOrgDao ;
import com.zd.school.plartform.baseset.model.BaseOrg ;


/**
 * 
 * ClassName: BaseOrgDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: BASE_T_ORG实体Dao接口实现类.
 * date: 2016-07-26
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseOrgDaoImpl extends BaseDaoImpl<BaseOrg> implements BaseOrgDao {
    public BaseOrgDaoImpl() {
        super(BaseOrg.class);
        // TODO Auto-generated constructor stub
    }
}