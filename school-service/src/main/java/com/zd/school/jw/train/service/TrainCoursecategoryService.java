package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainCoursecategory;
import com.zd.school.jw.train.model.TrainCoursecategoryTree;
import com.zd.school.plartform.system.model.SysUser;

import java.util.List;


/**
 * 
 * ClassName: TrainCoursecategoryService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程分类信息(TRAIN_T_COURSECATEGORY)实体Service接口类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface TrainCoursecategoryService extends BaseService<TrainCoursecategory> {

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
    public QueryResult<TrainCoursecategory> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete); 

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
	public TrainCoursecategory doUpdateEntity(TrainCoursecategory entity, SysUser currentUser);

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	public TrainCoursecategory doAddEntity(TrainCoursecategory entity, SysUser currentUser);

	/**
	 * 课程分类树列表
	 * @param whereSql 查询条件
	 * @param orderSql 排序 条件
	 * @return
	 */
	List<TrainCoursecategoryTree> getTreeList(String whereSql, String orderSql);

	/**
	 * 调整分类的顺序
	 * @param ids 要调整顺序的分类id
	 * @param orders 调整后的顺序
	 * @param currentUser 当前操作者
	 */
	public void doChangeOrder(String ids, String orders,SysUser currentUser);
	
	/**
     * 导出课程分类信息
     *
     * @param ids 要导出的课程分类的Id,如果ids为空则为导出所有课程分类
     * @param  orderSql 导出时的排序条件
     * @return
     */
    public List<TrainCoursecategory> listExport(String ids,String orderSql);
}