package com.zd.school.salary.salary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.salary.dao.XcSalaryplatitemDao ;
import com.zd.school.salary.salary.model.XcSalaryplatitem ;


/**
 * 
 * ClassName: XcSalaryplatitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资套账工资项目表(XC_T_SALARYPLATITEM)实体Dao接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcSalaryplatitemDaoImpl extends BaseDaoImpl<XcSalaryplatitem> implements XcSalaryplatitemDao {
    public XcSalaryplatitemDaoImpl() {
        super(XcSalaryplatitem.class);
        // TODO Auto-generated constructor stub
    }
}