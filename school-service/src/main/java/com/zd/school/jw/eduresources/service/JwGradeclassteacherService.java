package com.zd.school.jw.eduresources.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwGradeclassteacher;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;

/**
 * 
 * ClassName: JwGradeclassteacherService Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 年级组长信息(JW_T_GRADECLASSTEACHER)实体Service接口类.
 * date: 2016-09-20
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface JwGradeclassteacherService extends BaseService<JwGradeclassteacher> {

    public QueryResult<TeaTeacherbase> getGradeClassTeacher(Integer start, Integer limit, String sort,
            String filter, Boolean isDelete, SysUser currentUser, String claiId, Integer claiLevel);
}