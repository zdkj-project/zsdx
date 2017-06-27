package com.zd.school.financial.financial.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.financial.financial.dao.CwFinancialbooksDao ;
import com.zd.school.financial.financial.model.CwFinancialbooks ;


/**
 * 
 * ClassName: CwFinancialbooksDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOKS)实体Dao接口实现类.
 * date: 2017-02-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CwFinancialbooksDaoImpl extends BaseDaoImpl<CwFinancialbooks> implements CwFinancialbooksDao {
    public CwFinancialbooksDaoImpl() {
        super(CwFinancialbooks.class);
        // TODO Auto-generated constructor stub
    }
}