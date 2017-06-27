package com.zd.school.jw.eduresources.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwTGradeclassService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学校班级信息实体Service接口类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface JwTGradeclassService extends BaseService<JwTGradeclass> {
    /**
     * 根据班级ID得到年级对象
     * 
     * @param claiId
     * @return
     * @author huangzc
     */
    public JwTGrade findJwTGradeByClaiId(String claiId);

    public QueryResult<JwTGradeclass> getGradeClassList(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, SysUser currentUser);
    
    public QueryResult<JwTGradeclass> getGradeClassList(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, SysUser currentUser,String claiId,String claiLevel);    
}