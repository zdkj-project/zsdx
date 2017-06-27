package com.zd.school.jw.ecc.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.ecc.dao.EccClassstarDao ;
import com.zd.school.jw.ecc.model.EccClassstar ;


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
public class EccClassstarDaoImpl extends BaseDaoImpl<EccClassstar> implements EccClassstarDao {
    public EccClassstarDaoImpl() {
        super(EccClassstar.class);
        // TODO Auto-generated constructor stub
    }
}