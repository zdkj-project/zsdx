package com.zd.school.salary.salary.service;

import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.model.XcSalarybook ;


/**
 * 
 * ClassName: XcSalarybookService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资台账表(XC_T_SALARYBOOK)实体Service接口类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface XcSalarybookService extends BaseService<XcSalarybook> {

    public QueryResult<XcSalarybook> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
    
    /**
     * 获取用户有数据的台帐
     * @param user 当前用户
     * @return 树集合
     */
    public List<CommTree> getUserBookTree(SysUser user);
}