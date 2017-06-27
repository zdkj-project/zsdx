package com.zd.school.salary.salary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.salary.dao.XcSalarybookitemDao ;
import com.zd.school.salary.salary.model.XcSalarybookitem ;


/**
 * 
 * ClassName: XcSalarybookitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资信息表(XC_T_SALARYBOOKITEM)实体Dao接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcSalarybookitemDaoImpl extends BaseDaoImpl<XcSalarybookitem> implements XcSalarybookitemDao {
    public XcSalarybookitemDaoImpl() {
        super(XcSalarybookitem.class);
        // TODO Auto-generated constructor stub
    }
}