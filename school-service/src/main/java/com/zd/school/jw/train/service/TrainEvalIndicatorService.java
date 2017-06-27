package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainEvalIndicator;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.plartform.system.model.SysUser;

import java.util.List;

/**
 * Created by luoyibo on 2017-06-18.
 */
public interface TrainEvalIndicatorService extends BaseService<TrainEvalIndicator> {
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
    public QueryResult<TrainEvalIndicator> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete);

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
     * @param stands
     * @param currentUser
     * @return
     */
    public TrainEvalIndicator doUpdateEntity(TrainEvalIndicator entity, List<TrainIndicatorStand> stands, SysUser currentUser);

    /**
     *
     * @param entity
     * @param stands
     * @param currentUser
     * @return
     */
    public TrainEvalIndicator doAddEntity(TrainEvalIndicator entity, List<TrainIndicatorStand> stands, SysUser currentUser);
}
