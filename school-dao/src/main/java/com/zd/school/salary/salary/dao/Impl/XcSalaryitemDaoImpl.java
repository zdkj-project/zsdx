package com.zd.school.salary.salary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.salary.dao.XcSalaryitemDao ;
import com.zd.school.salary.salary.model.XcSalaryitem ;


/**
 * 
 * ClassName: XcSalaryitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资项定义表(XC_T_SALARYITEM)实体Dao接口实现类.
 * date: 2016-12-12
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcSalaryitemDaoImpl extends BaseDaoImpl<XcSalaryitem> implements XcSalaryitemDao {
    public XcSalaryitemDaoImpl() {
        super(XcSalaryitem.class);
        // TODO Auto-generated constructor stub
    }
}