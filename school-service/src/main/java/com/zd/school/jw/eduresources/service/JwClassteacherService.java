package com.zd.school.jw.eduresources.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwClassteacherService Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 班主任信息实体Service接口类. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface JwClassteacherService extends BaseService<JwClassteacher> {

    /**
     * 
     * getClassLeader:获取指定学生的所在班级的班主任
     *
     * @author luoyibo
     * @param userId
     * @return String
     * @throws @since
     *             JDK 1.8
     */
    public String getClassLeader(String userId);

    /**
     * 获取指定学生的所有班主任
     * 
     * @param userId
     * @return
     */
    public String getClassLeaderList(String userId);

    /**
     * 
     * getGradeClasTree:获取班级的树形数据
     *
     * @author luoyibo
     * @param viewName
     *            获取数据的视图/表名
     * @param whereSql
     *            额外指定的查询语句
     * @param currentUser
     *            当前操作用户对象
     * @return List<CommTree>
     * @throws @since
     *             JDK 1.8
     */
    public List<CommTree> getGradeClassTree(String viewName, String whereSql, SysUser currentUser);

    /**
     * 
     * getDeptUser:查询指定年级的班主任
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
    public QueryResult<JwClassteacher> getclassTeacher(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, String claiId, SysUser currentUser);

    public JwClassteacher doAddClassTeacher(JwClassteacher entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException;

    public Boolean doDelete(String delIds, SysUser currentUser);
}