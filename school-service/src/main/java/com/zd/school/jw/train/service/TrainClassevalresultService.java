package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainClassevalresult;
import com.zd.school.jw.train.model.vo.TrainClassEval;
import com.zd.school.plartform.system.model.SysUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * 
 * ClassName: TrainClassevalresultService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description:  班级评价结果(TRAIN_T_CLASSEVALRESULT)实体Service接口类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainClassevalresultService extends BaseService<TrainClassevalresult> {

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
    public QueryResult<TrainClassevalresult> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

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
	public TrainClassevalresult doUpdateEntity(TrainClassevalresult entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainClassevalresult doAddEntity(TrainClassevalresult entity, SysUser currentUser);

	/**
	 * 启动班级评价
	 * @param ids
	 * @return
	 */
	public Boolean doStartClassEval(String ids) throws InvocationTargetException, IllegalAccessException;

    /**
     * 关闭班级评价
     * @param ids
     * @return
     */
	public Boolean doEndStartClassEval(String ids,SysUser currentUser);
	/**
	 * 汇总班级的评价结果
	 * @param ids
	 * @return
	 */
	public  Boolean doSumClassEval(String ids);

    /**
     * 获取导出时班级下课程的评价排名
     * @param classId
     * @return
     */
    public Map<String, Object> getExportRankingData(String classId);

    /**
     * 获取导出时班级的评价结果
     * @param ids
     * @param trainClass
     * @return
     */
    public Map<String, Object> getClassEvalResult(String ids, TrainClassEval trainClass);

    /**
     * 获取班级下课程的评价结果
     * @param ids
     * @param orderSql
     * @return
     */
    public List<Map<String, Object>> getClassCourseEvalResult(String ids, String orderSql);
}