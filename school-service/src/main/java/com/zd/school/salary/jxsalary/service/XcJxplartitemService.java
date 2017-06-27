package com.zd.school.salary.jxsalary.service;

import java.io.Serializable;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.jxsalary.model.XcJxplartitem ;


/**
 * 
 * ClassName: XcJxplartitemService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效套账工资项目表(XC_T_JXPLARTITEM)实体Service接口类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface XcJxplartitemService extends BaseService<XcJxplartitem> {

    public QueryResult<XcJxplartitem> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
    
    /**
     * 删除工资项 并重新排序
     * @param 需要删除的Id
     * @return 成功返回true 否则返回False
     */
    public int deleteXcJxplartitem(Serializable... ids);
    
    /**
     * 批量增加工资项
     * @param ids 工资项id
     * @param salaryplatId 工资套账Id
     * @return
     */
    public boolean doBatchAdd(String idstr,String jxplartId,String userCh);
    
    /**
     * 批量排序
     * @param ids  需要排序的id
     * @param userCh
     * @return
     */
    public boolean doBatchUpdate(String[] ids,String userCh);
}