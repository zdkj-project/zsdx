package com.zd.school.salary.jxsalary.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.salary.jxsalary.dao.XcJxplartitemDao ;
import com.zd.school.salary.jxsalary.model.XcJxplartitem ;


/**
 * 
 * ClassName: XcJxplartitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效套账工资项目表(XC_T_JXPLARTITEM)实体Dao接口实现类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class XcJxplartitemDaoImpl extends BaseDaoImpl<XcJxplartitem> implements XcJxplartitemDao {
    public XcJxplartitemDaoImpl() {
        super(XcJxplartitem.class);
        // TODO Auto-generated constructor stub
    }
}