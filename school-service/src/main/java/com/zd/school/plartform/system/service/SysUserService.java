package com.zd.school.plartform.system.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;

/**
 * ClassName: BaseTUserService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户管理实体Service接口类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface SysUserService extends BaseService<SysUser> {

    /**
     * doAddUser:增加新用户
     *
     * @param entity
     * @param currentUser
     * @return
     * @throws Exception
     * @throws InvocationTargetException SysUser
     * @throws @since                    JDK 1.8
     * @author luoyibo
     */
    public SysUser doAddUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException;

    /**
     * doUpdateUser:修改用户信息
     *
     * @param entity
     * @param currentUser
     * @return
     * @throws Exception
     * @throws InvocationTargetException SysUser
     * @throws @since                    JDK 1.8
     * @author luoyibo
     */
    public SysUser doUpdateUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException;

    /**
     * deleteUserRole:删除指定用户的所属角色.
     *
     * @param userId      要删除角色的用户ID
     * @param delRoleIds  要删除的角色ID,多个用英文逗号隔开
     * @param currentUser 当前操作员对象
     * @return String
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public Boolean deleteUserRole(String userId, String delRoleIds, SysUser currentUser);

    /**
     * addUserRole:给指定用户添加角色.
     *
     * @param userId      要添加角色的用户ID
     * @param addRoleIds  要添加的角色ID,多个用英文逗号隔开
     * @param currentUser 当前操作员对象
     * @return Boolean
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public Boolean addUserRole(String userId, String addRoleIds, SysUser currentUser);

    /**
     * getDeptUser:查询指定部门的用户,带翻页功能
     *
     * @param start    记录起始位置
     * @param limit    查询的最大记录条数
     * @param sort     排序条件
     * @param filter   过滤条件
     * @param isDelete
     * @param deptId
     * @return QueryResult<SysUser>
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public QueryResult<SysUser> getDeptUser(Integer start, Integer limit, String sort, String filter, Boolean isDelete,
                                            String userIds, SysUser currentUser);

    /**
     * 根据角色名称查询角色下所有的用户
     *
     * @param roleName 角色名称
     * @return
     */
    public List<SysUser> getUserByRoleName(String roleName);

    /**
     * 删除指定的用户
     * @param delIds
     * @param orgId
     * @param currentUser
     * @return
     */
    public Boolean doDeleteUser(String delIds, String orgId, SysUser currentUser);

    /**
     * 查询指定角色包括的所有用户 不带翻页功能
     * @param roleId 角色Id
     * @return
     */
    public QueryResult<SysUser> getUserByRoleId(String roleId);

    /**
     * 查询指定角色包括的所有用户 不翻页功能
     * @param roleId 角色Id
     * @param start 起始页码
     * @param limit 每面记录数
     * @param sort 排序条件
     * @param filter 过滤条件
     * @return
     */
    public QueryResult<SysUser> getUserByRoleId(String roleId,Integer start, Integer limit, String sort, String filter);

    /**
     * 查询i不包括在指定角色中的用户
     * @param roleId 角色Id
     * @param start 起始页码
     * @param limit 每面记录数
     * @param sort
     * @param filter
     * @return
     */
    public QueryResult<SysUser> getUserNotInRoleId(String roleId,Integer start, Integer limit, String sort, String filter);

    public int syncUserInfoToUP(SysUserToUP sysUserInfo, String userId);

    public int syncUserInfoToAllUP(List<SysUserToUP> userInfos, String departmentId);

    public int syncAllCardInfoFromUp(List<CardUserInfoToUP> cardUserInfos);

    public int syncClassCardInfoFromUp(List<CardUserInfoToUP> upCardUserInfos, String classId);

	public List<SysRole> getSysRoleList(SysUser sysUser);
}