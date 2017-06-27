package com.zd.school.salary.salary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.salary.dao.XcSalaryplatDao ;
import com.zd.school.salary.salary.model.XcSalaryplat ;


/**
 * 
 * ClassName: XcSalaryplatDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资套账表(XC_T_SALARYPLAT)实体Dao接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcSalaryplatDaoImpl extends BaseDaoImpl<XcSalaryplat> implements XcSalaryplatDao {
    public XcSalaryplatDaoImpl() {
        super(XcSalaryplat.class);
        // TODO Auto-generated constructor stub
    }
}