package com.zd.school.cashier.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.cashier.dao.CashExpensedetailDao ;
import com.zd.school.cashier.model.CashExpensedetail ;


/**
 * 
 * ClassName: CashExpensedetailDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 流水明细(CASH_T_EXPENSEDETAIL)实体Dao接口实现类.
 * date: 2017-09-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CashExpensedetailDaoImpl extends BaseDaoImpl<CashExpensedetail> implements CashExpensedetailDao {
    public CashExpensedetailDaoImpl() {
        super(CashExpensedetail.class);
        // TODO Auto-generated constructor stub
    }
}