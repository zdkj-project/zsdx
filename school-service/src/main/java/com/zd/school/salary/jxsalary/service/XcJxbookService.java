package com.zd.school.salary.jxsalary.service;

import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.jxsalary.model.XcJxbook ;


/**
 * 
 * ClassName: XcJxbookService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOK)实体Service接口类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface XcJxbookService extends BaseService<XcJxbook> {

    public QueryResult<XcJxbook> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
    
    public List<CommTree> getBookTree(String viewName, String whereSql, SysUser currentUser);
    
    Boolean doDelete(String ids, SysUser currentUser);
}