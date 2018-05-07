package com.zd.school.oa.meeting.service;

import java.util.List;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.plartform.system.model.SysUser;


/**
 * 
 * ClassName: OaMeetingService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议信息(OA_T_MEETING)实体Service接口类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface OaMeetingService extends BaseService<OaMeeting> {

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
    public QueryResult<OaMeeting> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

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
	public OaMeeting doUpdateEntity(OaMeeting entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public OaMeeting doAddEntity(OaMeeting entity, SysUser currentUser);

    /**
     * 增加新的参会人员
     * @param ids 要增加参会人员的会议Id
     * @param userId 要增加的人员Id,多人用英文逗号隔开
     * @param userName 要增加的人员姓名,多人用英文逗号隔开
     * @param currentUser 当前操作人员
     * @return
     */
	public  Boolean doAddMeetingUser(String ids,String userId,String userName,SysUser currentUser);

    /**
     * 删除指定的参会人员
     * @param ids 要删除参会人员的会议
     * @param userId 要删除参会人员的Id，多人用英文逗号隔开
     * @param currentUser 当前操作人员
     * @return
     */
    public Boolean doDeleteMeetingUser(String ids, String userId, SysUser currentUser);

	public Integer syncMetting(List<Object[]> meetingList, List<Object[]> empList);
}