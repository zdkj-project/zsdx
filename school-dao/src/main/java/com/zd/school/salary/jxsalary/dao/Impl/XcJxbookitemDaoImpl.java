package com.zd.school.salary.jxsalary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.jxsalary.dao.XcJxbookitemDao ;
import com.zd.school.salary.jxsalary.model.XcJxbookitem ;


/**
 * 
 * ClassName: XcJxbookitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOKITEM)实体Dao接口实现类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcJxbookitemDaoImpl extends BaseDaoImpl<XcJxbookitem> implements XcJxbookitemDao {
    public XcJxbookitemDaoImpl() {
        super(XcJxbookitem.class);
        // TODO Auto-generated constructor stub
    }
}