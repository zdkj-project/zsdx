package com.zd.school.salary.salary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.salary.dao.XcSalarybookDao ;
import com.zd.school.salary.salary.model.XcSalarybook ;


/**
 * 
 * ClassName: XcSalarybookDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资台账表(XC_T_SALARYBOOK)实体Dao接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcSalarybookDaoImpl extends BaseDaoImpl<XcSalarybook> implements XcSalarybookDao {
    public XcSalarybookDaoImpl() {
        super(XcSalarybook.class);
        // TODO Auto-generated constructor stub
    }
}