package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainCourseevalresult;
import com.zd.school.plartform.system.model.SysUser;

import java.util.List;
import java.util.Map;


/**
 * 
 * ClassName: TrainCourseevalresultService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程评价结果(TRAIN_T_COURSEEVALRESULT)实体Service接口类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainCourseevalresultService extends BaseService<TrainCourseevalresult> {

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
    public QueryResult<TrainCourseevalresult> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

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
	public TrainCourseevalresult doUpdateEntity(TrainCourseevalresult entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainCourseevalresult doAddEntity(TrainCourseevalresult entity, SysUser currentUser);

	/**
	 * 启动课程评价
	 * @param ids
	 * @return
	 */
	public Boolean doStartCourseEval(String ids);

	/**
	 * 汇总课程评价
	 * @param ids
	 * @return
	 */
	public Boolean doSumCourseEval(String ids);

    /**
     * 关闭课程评价
     * @param ids
     * @param currentUser
     * @return
     */
    public Boolean doEndCourseEval(String ids, SysUser currentUser);

    /**
     * 获取导出时班级下所有课程的指标标准数据
     * @param classId 班级ID
     * @return
     */
    public  Map<String, Map<String,List<Map<String, Object>>>> getClassCourseEvalResult(String classId);

    /**
     * 获取导出时指定班级的指标增标准数据
     * @param ids
     * @return
     */
    public Map<String, List<Map<String, Object>>>  getClassEvalResult(String ids);


    /**
     * 评价汇总完成后更班级下新课程的排名
     * @param classId 班级Id
     * @return
     */
    public Boolean resetCourseEvalRanking(String classId);

    /**
     * 获取指定课程的评价结果信息
     * @param courseId 要获取评价结果的课程
     * @return
     */
    public Map<String, List<Map<String, Object>>> getCourseEvalResult(String courseId);

	/**
	 * 获取指定课程评价结果的详细
	 * @param courseId 要获取的课程的Id
	 */
	public Map<String, Object>  getCourseEvalResultDetail(String courseId);
}