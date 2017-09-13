package com.zd.school.cashier.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.cashier.dao.CashDinnertypeDao ;
import com.zd.school.cashier.model.CashDinnertype ;


/**
 * 
 * ClassName: CashDinnertypeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 餐类管理(CASH_T_DINNERTYPE)实体Dao接口实现类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CashDinnertypeDaoImpl extends BaseDaoImpl<CashDinnertype> implements CashDinnertypeDao {
    public CashDinnertypeDaoImpl() {
        super(CashDinnertype.class);
        // TODO Auto-generated constructor stub
    }
}