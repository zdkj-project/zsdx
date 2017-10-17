package com.zd.school.jw.train.service;

import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.vo.VoTrainClassCheck;
import com.zd.school.plartform.system.model.SysUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * ClassName: TrainClasstraineeService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级学员信息(TRAIN_T_CLASSTRAINEE)实体Service接口类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainClasstraineeService extends BaseService<TrainClasstrainee> {

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
    public QueryResult<TrainClasstrainee> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
	 * @param delIds 
	 * @param currentUser
	 *            当前操作的用户
	 * @return 操作成功返回true，否则返回false
	 */
	public Boolean doLogicDeleteByIds(String classId, String ids, SysUser currentUser);

	/**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainClasstrainee doUpdateEntity(TrainClasstrainee entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainClasstrainee doAddEntity(TrainClasstrainee entity, SysUser currentUser);

	public int doUpdateRoomInfo(String classId,String roomId, String roomName, String ids, String xbm, SysUser currentUser);

	public int doCancelRoomInfo(String ids, SysUser currentUser);

	public List<ImportNotInfo> doImportTrainee(List<List<Object>> listObject, String classId, String needSync, SysUser currentUser);

	public void doSyncClassTrainee(String classId, SysUser currentUser);

    /**
     * 获取指定学员的学分明细
     * @param classTraineeId 要获取学分的学员Id
     * @return
     */
    public List<Map<String, Object>> getClassTraineeCreditsList(String classTraineeId);

    public QueryResult<VoTrainClassCheck> getCheckList(Integer start, Integer limit, String classId, String classScheduleId, String xm);

	public void doSyncUnBindToUP(String ids, String xm);

	public List<Map<String, Object>> doCardBind(String ids);

	public void doSyncBindToUP(List<Map<String, Object>> cardInfoToUp, String xm);

	public void doRestoreCardBind(List<Map<String, Object>> cardInfoToUp);
}