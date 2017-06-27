package com.zd.school.financial.financial.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.financial.financial.dao.CwFinancialbookitemDao ;
import com.zd.school.financial.financial.model.CwFinancialbookitem ;


/**
 * 
 * ClassName: CwFinancialbookitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOKITEM)实体Dao接口实现类.
 * date: 2017-01-16
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CwFinancialbookitemDaoImpl extends BaseDaoImpl<CwFinancialbookitem> implements CwFinancialbookitemDao {
    public CwFinancialbookitemDaoImpl() {
        super(CwFinancialbookitem.class);
        // TODO Auto-generated constructor stub
    }
}