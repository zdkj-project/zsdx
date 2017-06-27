package com.zd.school.jw.schoolcourse.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.schoolcourse.model.JwPublishcourse;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwPublishcourseService Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 校本课程发布课程信息(JW_T_PUBLISHCOURSE)实体Service接口类.
 * date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface JwPublishcourseService extends BaseService<JwPublishcourse> {

    public QueryResult<JwPublishcourse> list(Integer start, Integer limit, String sort, String filter, String whereSql,
            String orderSql, SysUser currentUser);

    public Boolean doAdd(JwPublishcourse course, SysUser currentUser);
    
    public Boolean doUpdate(JwPublishcourse course, SysUser currentUser);
}