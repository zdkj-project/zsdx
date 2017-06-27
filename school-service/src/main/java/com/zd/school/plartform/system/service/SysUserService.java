package com.zd.school.plartform.system.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;

/**
 * 
 * ClassName: BaseTUserService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户管理实体Service接口类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface SysUserService extends BaseService<SysUser> {

    /**
     * 
     * doAddUser:增加新用户
     * 
     * @author luoyibo
     * @param entity
     * @param currentUser
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     *             SysUser
     * @throws @since
     *             JDK 1.8
     */
    public SysUser doAddUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException;

    /**
     * 
     * doUpdateUser:修改用户信息
     *
     * @author luoyibo
     * @param entity
     * @param currentUser
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     *             SysUser
     * @throws @since
     *             JDK 1.8
     */
    public SysUser doUpdateUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException;

    /**
     * 
     * deleteUserRole:删除指定用户的所属角色.
     * 
     * @author luoyibo
     * @param userId
     *            要删除角色的用户ID
     * @param delRoleIds
     *            要删除的角色ID,多个用英文逗号隔开
     * @param currentUser
     *            当前操作员对象
     * @return String
     * @throws @since
     *             JDK 1.8
     */
    public Boolean deleteUserRole(String userId, String delRoleIds, SysUser currentUser);

    /**
     * 
     * addUserRole:给指定用户添加角色.
     *
     * @author luoyibo
     * @param userId
     *            要添加角色的用户ID
     * @param addRoleIds
     *            要添加的角色ID,多个用英文逗号隔开
     * @param currentUser
     *            当前操作员对象
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    public Boolean addUserRole(String userId, String addRoleIds, SysUser currentUser);

    /**
     * 
     * getDeptUser:查询指定部门的用户,带翻页功能
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
    public QueryResult<SysUser> getDeptUser(Integer start, Integer limit, String sort, String filter, Boolean isDelete,
            String userIds, SysUser currentUser);

    /**
     * 
     * batchSetDept:批量设置用户的所属部门.
     *
     * @author luoyibo
     * @param deptId
     * @param userIds
     * @param cuurentUser
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    //    public Boolean batchSetDept(String deptId, String userIds, SysUser cuurentUser);

    public List<SysUser> getUserByRoleName(String roleName);

    /**
     * 
     * doDeleteUser:删除指定的用户 说明：此处删除用户是删除当前用户点击时选择的部门
     * 
     * @author luoyibo
     * @param delIds
     *            要删除的用户的ID
     * @param orgId
     *            点击的部门ID
     * @param currentUser当前操作者
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    public Boolean doDeleteUser(String delIds, String orgId, SysUser currentUser);

    public QueryResult<SysUser> getUserByRoleId(String roleId);
    
    public int syncUserInfoToUP(SysUserToUP sysUserInfo, String userId);

	public int syncUserInfoToAllUP(List<SysUserToUP> userInfos);

	public int syncAllCardInfoFromUp(List<CardUserInfoToUP> cardUserInfos);
}