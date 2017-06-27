package com.zd.school.jw.schoolcourse.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.schoolcourse.model.JwCoursepublish ;


/**
 * 
 * ClassName: JwCoursepublishService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校本课程发布信息(JW_T_COURSEPUBLISH)实体Service接口类.
 * date: 2016-11-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface JwCoursepublishService extends BaseService<JwCoursepublish> {

    public QueryResult<JwCoursepublish> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
}