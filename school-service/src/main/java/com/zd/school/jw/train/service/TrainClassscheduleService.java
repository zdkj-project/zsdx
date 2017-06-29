package com.zd.school.jw.train.service;

import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.model.app.CourseEvalApp;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
import com.zd.school.plartform.system.model.SysUser;

import java.util.List;
import java.util.Map;


/**
 * 
 * ClassName: TrainClassscheduleService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级课程日历(TRAIN_T_CLASSSCHEDULE)实体Service接口类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainClassscheduleService extends BaseService<TrainClassschedule> {

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
    public QueryResult<TrainClassschedule> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
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
	public TrainClassschedule doUpdateEntity(TrainClassschedule entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param teachType 
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainClassschedule doAddEntity(TrainClassschedule entity, String teachType, SysUser currentUser);

	public int doUpdateRoomInfo(String roomId, String roomName, String ids, SysUser currentUser);

	public int doCancelRoomInfo(String ids, SysUser currentUser);

	public List<ImportNotInfo> doImportData(List<List<Object>> listObject, String classId, SysUser currentUser);

	/**
	 * 获取课程评价的标准，以便进行评价
	 * @param courseId 要进行评价的课程Id
	 * @return
	 */
	public CourseEvalApp getCourseEvalStand(String courseId);

    QueryResult<TrainClassCourseEval> getClassCourseEval(Integer start,Integer limit,String orderSql,String classId);

    public  List<Map<String,Object>> getClassCourseRanking(String orderSql,String classId);
}