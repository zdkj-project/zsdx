package com.zd.school.plartform.baseset.service;

import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseDpetJobTree;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseDeptjobService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 部门岗位信息(BASE_T_DEPTJOB)实体Service接口类. date: 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface BaseDeptjobService extends BaseService<BaseDeptjob> {

	/**
	 * 数据列表
	 * 
	 * @param start
	 *            查询的起始记录数
	 * @param limit
	 *            每页的记录数
	 * @param sort
	 *            排序参数
	 * @param filter
	 *            查询过滤参数
	 * @param isDelete
	 *            为true表示只列出未删除的， 为false表示列出所有
	 * @return
	 */
	public QueryResult<BaseDeptjob> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete);

	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
	 * @param currentUser
	 *            当前操作的用户
	 * @return 操作成功返回true，否则返回false
	 */
	public Boolean doLogicDeleteByIds(String ids, SysUser currentUser);

	/**
	 * 设置部门所包含的岗位
	 * 
	 * @param deptId
	 *            要设置的部门的ID，多个部门用英文逗号隔开
	 * @param jobId
	 *            包含岗位的ID，多个岗位用英文逗号隔开
	 * @param currentUser
	 *            当前操作员
	 * @return
	 */
	public Boolean batchSetDeptJob(String deptId, String jobId, SysUser currentUser);

	/**
	 * 删除部门的岗位
	 * 
	 * @param deptJobId
	 *            要删除的部门岗ID，多个岗位用英文逗号隔开
	 * @param currentUser
	 *            当前操作员
	 * @return
	 */
	public Boolean delDeptJob(String deptJobId, SysUser currentUser);

	/**
	 * 检查指定的部门岗位是否其它部门或岗位的上级
	 * 
	 * @param deptJobId
	 *            部门岗位标识
	 * @return
	 */
	public String chkIsSuperJob(String deptJobId);

	/**
	 * 检查指定部门下的指定岗位是否其它部门或岗位的上级
	 * 
	 * @param deptid
	 *            指定的部门Id
	 * @param jobId
	 *            指定的岗位Id
	 * @return
	 */
	public String chkIsSuperJob(String deptid, String jobId);

	/**
	 * 将指定的部门岗位设置为该部门的负责岗位
	 * 
	 * @param deptJobId
	 * @param currentUser
	 * @return
	 */
	public Boolean setDeptLeaderJob(String deptid, String deptJobId, SysUser currentUser);

	/**
	 * 获取部门岗位的树形结构
	 * 
	 * @param root
	 *            树形结构的根节点
	 * @param whereSql
	 *            查询的过滤语句
	 * @return
	 */
	public BaseDpetJobTree getDeptJobTree(String root, String whereSql);

	/**
	 * 获取部门岗位的树形列表数据
	 * 
	 * @param root
	 * @param whereSql
	 * @return
	 */
	public List<BaseDpetJobTree> getDeptJobTreeList(String root, String whereSql);

	/**
	 * 设置指定的岗位（部门）的上级主管岗位
	 * 
	 * @param ids
	 *            上级主管岗位的ID
	 * @param setIds
	 *            要设置的岗位（部门）的Id,多个用英文逗号隔开
	 * @param setType
	 *            设置类型，是设置的岗位的还是部门的
	 * @return
	 */
	public Boolean setSuperJob(String ids, String setIds, String setType, SysUser currentUser);

}