package com.zd.school.cashier.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.cashier.dao.CashExpenseserialDao ;
import com.zd.school.cashier.model.CashExpenseserial ;


/**
 * 
 * ClassName: CashExpenseserialDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 消费流水(CASH_T_EXPENSESERIAL)实体Dao接口实现类.
 * date: 2017-09-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CashExpenseserialDaoImpl extends BaseDaoImpl<CashExpenseserial> implements CashExpenseserialDao {
    public CashExpenseserialDaoImpl() {
        super(CashExpenseserial.class);
        // TODO Auto-generated constructor stub
    }
}