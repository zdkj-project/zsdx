package com.zd.school.plartform.system.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysPermission;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 
 * ClassName: BaseTRoleService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Service接口类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return 持久化后的实体对象
     */
    public SysRole doAddEntity(SysRole entity, SysUser currentUser) throws InvocationTargetException, IllegalAccessException;

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return 更新后的实体对象
     */
    public SysRole doUpdateEntity(SysRole entity, SysUser currentUser) throws InvocationTargetException, IllegalAccessException;

    /**
     * 获取指定角色包含的用户,带翻页功能
     * @param roleId 角色Id
     * @param start 起始页码
     * @param limit 每面的记录数 如为0表示查询所有记录
     * @return
     */
    public QueryResult<SysUser> getRoleUser(String roleId, Integer start, Integer limit);

    /**
     * 删除指定角色下的指定用户
     * @param ids 要删除用户的角色Id
     * @param userId 要删除的用户Id
     * @return
     */
    public Boolean doDeleteRoleUser(String ids,String userId);

    /**
     * 给指定的角色添加包含的用户
     * @param ids 要添加用户的角色Id
     * @param userId 添加的用户Id
     * @return
     */
    public Boolean doAddRoleUser(String ids, String userId);

	public List<SysPermission> getSysPermissionList(SysRole role);

}