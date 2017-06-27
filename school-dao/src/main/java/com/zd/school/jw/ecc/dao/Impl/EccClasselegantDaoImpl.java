package com.zd.school.jw.ecc.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.ecc.dao.EccClasselegantDao;
import com.zd.school.jw.ecc.model.EccClasselegant;


/**
 * 
 * ClassName: EccClassstarDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级评星信息(ECC_T_CLASSSTAR)实体Dao接口实现类.
 * date: 2016-12-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class EccClasselegantDaoImpl extends BaseDaoImpl<EccClasselegant> implements EccClasselegantDao {
    public EccClasselegantDaoImpl() {
        super(EccClasselegant.class);
        // TODO Auto-generated constructor stub
    }
}