package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.model.app.ClassEvalApp;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.jw.train.model.vo.TrainClassEval;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;

import java.util.HashMap;
import java.util.List;


/**
 * 
 * ClassName: TrainClassService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 培训开班信息(TRAIN_T_CLASS)实体Service接口类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainClassService extends BaseService<TrainClass> {

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
    public QueryResult<TrainClass> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

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
	 *
	 * @param entity
	 * @param currentUser
	 * @return
	 */
	public TrainClass doUpdateEntity(TrainClass entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainClass doAddEntity(TrainClass entity, SysUser currentUser);

	public HashMap<String, String> getClassInfo(String id);

	public int doEditClassFood(TrainClass entity, String classFoodInfo, SysUser currentUser);

	public int doEditClassRoom(TrainClass entity, String classRoomInfo, SysUser currentUser);

	public int syncTraineeClassInfoToAllUP(String departmentId, TrainClass trainClass, List<SysUserToUP> userInfos);

	/**
	 *
	 * @param classId
	 * @return
	 */
	public List<TrainTeacher> getClassBzrList(String classId);

	/**
	 * 获取对班级进行评价的指标及标准
	 * @param classId 要评价的班级Id
	 * @return
	 */
	public ClassEvalApp getClassEvalStand(String classId);

	public int syncClassTraineeFoodsToUP(TrainClass trainClass, List<TrainClasstrainee> traineeFoods);

    public TrainClassEval getClassEvalInfo(String ids);

    /**
     * 汇总指定班级学员的学分
     * @param classId 要汇总学分的班级
     * @return
     */
    public Boolean doSumCredit(String classId);

    public Boolean doSendInfoUser( String sendUserId,  String sendInfo, SysUser currentUser);
}