package com.zd.school.jw.eduresources.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwGradeteacher;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwGradeteacherService Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 年级组长信息实体Service接口类. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface JwGradeteacherService extends BaseService<JwGradeteacher> {

    /**
     * 
     * getGradeLeader:获取指定学生的所在班级的班主任
     *
     * @author luoyibo
     * @param userId
     * @return String
     * @throws @since
     *             JDK 1.8
     */
    public String getGradeLeader(String userId);

    /**
     * 获取所有的年级组长
     * 
     * @param userId
     * @return
     */
    public String getGradeLeaderList(String userId);

    /**
     * 
     * getDeptUser:查询指定年级的年级组长
     *
     * @author luoyibo
     * @param start
     *            记录起始位置
     * @param limit
     *            查询的最大记录条数
     * @param sort
     *            排序条件
     * @param filter
     *            过滤条件
     * @param isDelete
     * @param deptId
     * @return QueryResult<SysUser>
     * @throws @since
     *             JDK 1.8
     */
    public QueryResult<JwGradeteacher> getGradeTeacher(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, String graiId, SysUser currentUser);

    public JwGradeteacher doAddGradeTeacher(JwGradeteacher entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException;

    public Boolean doDelete(String delIds, SysUser currentUser);

    public List<CommTree> getGradeTree(String viewName, String whereSql, SysUser currentUser);
}