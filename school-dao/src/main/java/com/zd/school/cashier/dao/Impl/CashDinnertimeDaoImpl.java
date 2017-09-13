package com.zd.school.cashier.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.cashier.dao.CashDinnertimeDao ;
import com.zd.school.cashier.model.CashDinnertime ;


/**
 * 
 * ClassName: CashDinnertimeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 快餐时间段定义(CASH_T_DINNERTIME)实体Dao接口实现类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CashDinnertimeDaoImpl extends BaseDaoImpl<CashDinnertime> implements CashDinnertimeDao {
    public CashDinnertimeDaoImpl() {
        super(CashDinnertime.class);
        // TODO Auto-generated constructor stub
    }
}