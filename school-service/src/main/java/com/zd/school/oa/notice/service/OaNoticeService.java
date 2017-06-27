package com.zd.school.oa.notice.service;

import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticeOther;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaNoticeService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公告信息表(OA_T_NOTICE)实体Service接口类. date: 2016-12-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface OaNoticeService extends BaseService<OaNotice> {

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
	public QueryResult<OaNotice> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete);

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
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public OaNotice doUpdateEntity(OaNotice entity, SysUser currentUser);

	/**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @param deptIds
	 *            公告通知的部门
	 * @param roleIds
	 *            公告通知的角色
	 * @param userIds
	 *            公告通知的用户
	 * @return
	 */
	public OaNotice doUpdateEntity(OaNotice entity, SysUser currentUser, String deptIds, String roleIds,
			String userIds);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public OaNotice doAddEntity(OaNotice entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @param deptIds
	 *            公告通知的部门
	 * @param roleIds
	 *            公告通知的角色
	 * @param userIds
	 *            公告通知的用户
	 * @return
	 */
	public OaNotice doAddEntity(OaNotice entity, SysUser currentUser, String deptIds, String roleIds, String userIds);

	/**
	 * 获取指定公告的通知部门、角色、人员的信息
	 * 
	 * @param id
	 *            指定的公告id
	 * @return
	 */
	public OaNoticeOther getNoticeOther(String id);
	
	public  List<OaNotice> getUserOaNotice(SysUser currentUser);
}