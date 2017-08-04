/**
 * Project Name:jw-service
 * File Name:BaseOrgService.java
 * Package Name:com.zd.school.base.service
 * Date:2016年5月18日上午11:27:25
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.baseset.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseService;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.model.BaseOrgChkTree;
import com.zd.school.plartform.baseset.model.BaseOrgToUP;
import com.zd.school.plartform.baseset.model.BaseOrgTree;
import com.zd.school.plartform.system.model.SysUser;

/**
 * ClassName:BaseOrgService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月18日 上午11:27:25
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Transactional
public interface BaseOrgService extends BaseService<BaseOrg> {

    /**
     * 
     * getOrgTreeList:获取部门树形数据
     *
     * @author luoyibo
     * @param whereSql
     *            获取指定的查询条件
     * @param orderSql
     *            获取时指定的排序条件
     * @param currentUser
     *            当前操作用户对象 *
     * @return List<BaseOrgTree>
     * @throws @since
     *             JDK 1.8
     */
    public List<BaseOrgTree> getOrgTreeList(String whereSql, String orderSql, SysUser currentUser);

    /**
     * 
     * delOrg:删除指定的部门.
     *
     * @author luoyibo
     * @param delIds
     *            要删除的部门的标识
     * @param currentUser
     *            当前操作的用户对象
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    public Boolean delOrg(String delIds, SysUser currentUser);

    /**
     * 
     * addOrg:增加新部门.
     *
     * @author luoyibo
     * @param entity
     *            要增加的部门实体
     * @param currentUser
     *            当前操作的用户对象
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *             BaseOrg
     * @throws @since
     *             JDK 1.8
     */
    public BaseOrg addOrg(BaseOrg entity, SysUser currentUser) throws IllegalAccessException, InvocationTargetException;

    /**
     * 
     * getOrgList:部门列表数据
     *
     * @author luoyibo
     * @param whereSql
     * @param orderSql
     * @param currentUser
     * @return List<BaseOrg>
     * @throws @since
     *             JDK 1.8
     */
    public List<BaseOrg> getOrgList(String whereSql, String orderSql, SysUser currentUser);

    /**
     * 
     * getOrgAndChildList:得到指定部门及其子部门数据.
     *
     * @author luoyibo
     * @param deptId
     *            指定的部门ID
     * @param orderSql
     *            指定的排序条件
     * @param currentUser
     *            当前操作用户对象
     * @param isRight
     *            是否加上权限控制 true 表示加上
     * @return List<BaseOrg>
     * @throws @since
     *             JDK 1.8
     */
    public List<BaseOrg> getOrgAndChildList(String deptId, String orderSql, SysUser currentUser, Boolean isRight);

    public Integer getChildCount(String deptId);

    /**
     * 
     * getOrgChkTreeList:查询带checkbox框的部门树形数据
     *
     * @author luoyibo
     * @param whereSql
     * @param orderSql
     * @param currentUser
     * @return List<BaseOrgChkTree>
     * @throws @since
     *             JDK 1.8
     */
    public List<BaseOrgChkTree> getOrgChkTreeList(String whereSql, String orderSql, SysUser currentUser);

    /**
     * 
     * getOrgChildIds:获取指定部门的所有子部门的ID的拼接串.
     *
     * @author luoyibo
     * @param orgId
     *            指定的部门的ID
     * @param isSelf
     *            返回字符串是否包含本部门 为true则包含，或者不包含，默认为true
     * @return String
     * @throws @since
     *             JDK 1.8
     */
    public String getOrgChildIds(String orgId, boolean isSelf);

    /**
     * 
     * getOrgChildMaps:获取指定部门的所有子部门的Map
     *
     * @author luoyibo
     * @param OrgId
     * @param isSelf
     * @return Map<String,BaseOrg>
     * @throws @since
     *             JDK 1.8
     */
    public Map<String, BaseOrg> getOrgChildMaps(String OrgId, boolean isSelf);

	public int syncDeptInfoToUP(BaseOrgToUP baseOrgToUP, String smallDeptId);

	public int syncAllDeptInfoToUP(List<BaseOrgToUP> deptInfo);
}
