package com.zd.school.salary.jxsalary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.jxsalary.dao.XcJxbookDao ;
import com.zd.school.salary.jxsalary.model.XcJxbook ;


/**
 * 
 * ClassName: XcJxbookDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOK)实体Dao接口实现类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcJxbookDaoImpl extends BaseDaoImpl<XcJxbook> implements XcJxbookDao {
    public XcJxbookDaoImpl() {
        super(XcJxbook.class);
        // TODO Auto-generated constructor stub
    }
}