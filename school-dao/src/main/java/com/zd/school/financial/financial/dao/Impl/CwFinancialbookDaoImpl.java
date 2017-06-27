package com.zd.school.financial.financial.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.financial.financial.dao.CwFinancialbookDao ;
import com.zd.school.financial.financial.model.CwFinancialbook ;


/**
 * 
 * ClassName: CwFinancialbookDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOK)实体Dao接口实现类.
 * date: 2017-01-16
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CwFinancialbookDaoImpl extends BaseDaoImpl<CwFinancialbook> implements CwFinancialbookDao {
    public CwFinancialbookDaoImpl() {
        super(CwFinancialbook.class);
        // TODO Auto-generated constructor stub
    }
}