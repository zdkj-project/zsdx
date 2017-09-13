package com.zd.school.cashier.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.cashier.dao.CashDishesDao ;
import com.zd.school.cashier.model.CashDishes ;


/**
 * 
 * ClassName: CashDishesDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 菜品管理(CASH_T_DISHES)实体Dao接口实现类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CashDishesDaoImpl extends BaseDaoImpl<CashDishes> implements CashDishesDao {
    public CashDishesDaoImpl() {
        super(CashDishes.class);
        // TODO Auto-generated constructor stub
    }
}