package com.zd.school.oa.doc.service;

import java.lang.reflect.InvocationTargetException;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.doc.model.DocRecexamines ;
import com.zd.school.plartform.system.model.SysUser;


/**
 * 
 * ClassName: DocRecexaminesService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文收文批阅人实体Service接口类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface DocRecexaminesService extends BaseService<DocRecexamines> {

    public DocRecexamines setIsPiYue(DocRecexamines entity,String distribType,String distribId,SysUser currentUser) throws IllegalAccessException, InvocationTargetException;
    
    public QueryResult<DocRecexamines> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser);
}