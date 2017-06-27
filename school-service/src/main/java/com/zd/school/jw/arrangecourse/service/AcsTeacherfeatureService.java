package com.zd.school.jw.arrangecourse.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.arrangecourse.model.AcsTeacherfeature ;


/**
 * 
 * ClassName: AcsTeacherfeatureService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_TEACHERFEATURE)实体Service接口类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface AcsTeacherfeatureService extends BaseService<AcsTeacherfeature> {

    public QueryResult<AcsTeacherfeature> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
}