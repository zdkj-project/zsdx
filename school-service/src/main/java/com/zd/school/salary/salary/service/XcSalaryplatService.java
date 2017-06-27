package com.zd.school.salary.salary.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.model.XcSalaryplat ;


/**
 * 
 * ClassName: XcSalaryplatService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资套账表(XC_T_SALARYPLAT)实体Service接口类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface XcSalaryplatService extends BaseService<XcSalaryplat> {

    public QueryResult<XcSalaryplat> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
}