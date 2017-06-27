package com.zd.school.teacher.teacherinfo.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;

/**
 * 
 * ClassName: TeaTeacherbaseService Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 教职工基本数据实体Service接口类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface TeaTeacherbaseService extends BaseService<TeaTeacherbase> {

	/**
	 * 
	 * getDeptTeacher:查询指定部门的教师
	 *
	 * @author luoyibo
	 * @param start
	 *            翻页的开始页
	 * @param limit
	 *            每页的记录数
	 * @param sort
	 *            排序字段
	 * @param filter过滤字段
	 * @param whereSql
	 *            外部的查询条件
	 * @param orderSql
	 *            外部的排序条件
	 * @param isDelete
	 *            是否删除标记
	 * @param deptId
	 *            指定的部门ID
	 * @param currentUser
	 *            当前操作的用户
	 * @return QueryResult<TeaTeacherbase>
	 * @throws @since
	 *             JDK 1.8
	 */
	public QueryResult<TeaTeacherbase> getDeptTeacher(Integer start, Integer limit, String sort, String filter,
			String whereSql, String orderSql, String querySql, Boolean isDelete, String deptId, SysUser currentUser);

	public QueryResult<TeaTeacherbase> getDeptTeacher(Integer start, Integer limit,String whereSql);
	/**
	 * 
	 * batchSetDept:批量设置教师的所属部门.
	 *
	 * @author luoyibo
	 * @param deptId
	 * @param userIds
	 * @param cuurentUser
	 * @return Boolean
	 * @throws @since
	 *             JDK 1.8
	 */
	public Boolean batchSetDept(String deptId, String userIds, SysUser cuurentUser);

	/**
	 * 
	 * delTeaFromDept:将指定的用户从指定的部门解除绑定.
	 * 
	 * @author luoyibo
	 * @param deptId
	 * @param userIds
	 * @param currentUser
	 * @return Boolean
	 * @throws @since
	 *             JDK 1.8
	 */
	public Boolean delTeaFromDept(String deptId, String userIds, SysUser currentUser);

	public Boolean setTeaToJob(String jobId, String userIds, SysUser cuurentUser);

	public Boolean delTeaFromJob(String deptId, String userIds, SysUser cuurentUser);

	public QueryResult<BaseJob> getTeahcerJobList(TeaTeacherbase teahcher, SysUser currentUser);

	/**
	 * 拼装指定教师的岗位的数据
	 * 
	 * @param teacher
	 *            要拼装的教师
	 * @return 返回格式如 岗位ID1|岗位ID2,岗位名称1,岗位名称2
	 */	
	public String getTeacherJobs(TeaTeacherbase teacher);

	/**
	 * 拼装指定教师的部门的数据
	 * 
	 * @param teacher
	 *            要拼装的教师
	 * @return 返回格式如 部门ID1|部门ID2,部门名称1,部门名称2
	 */
	public String getTeacherDepts(TeaTeacherbase teacher);
	
	public QueryResult<TeaTeacherbase> getCourseTeacherlist(Integer start, Integer limit, String sort, String filter,
			String whereSql, String orderSql, String querySql, Boolean isDelete);

}