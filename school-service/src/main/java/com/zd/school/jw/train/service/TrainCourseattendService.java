package com.zd.school.jw.train.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.train.model.TrainCourseattend;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ClassName: TrainCourseattendService
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选).
 * Description: 课程考勤刷卡结果(TRAIN_T_COURSEATTEND)实体Service接口类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface TrainCourseattendService extends BaseService<TrainCourseattend> {

    /**
     * 数据列表
     *
     * @param start    查询的起始记录数
     * @param limit    每页的记录数
     * @param sort     排序参数
     * @param filter   查询过滤参数
     * @param isDelete 为true表示只列出未删除的， 为false表示列出所有
     * @return
     */
    public QueryResult<TrainCourseattend> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete);

    /**
     * 根据主键逻辑删除数据
     *
     * @param ids         要删除数据的主键
     * @param currentUser 当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser);

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    public TrainCourseattend doUpdateEntity(TrainCourseattend entity, SysUser currentUser);

    /**
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    public TrainCourseattend doAddEntity(TrainCourseattend entity, SysUser currentUser);

    /**
     * @param list         班级课程日历
     * @param classId      班级ID
     * @param userId       学员ID
     * @param attendResult 签到结果
     * @param needCheckout 是否需要签退
     * @return void
     * @description 签到
     * @author yz
     * @date 2018/9/26 9:47
     * @method installTrainCourseattend
     */
    public void installTrainCourseattend(List<Map<String, Object>> list, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception;

    public void installTrainCourseattends(List<TrainClassschedule> list, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception;

    public void installTrainCourseattend(Map<String, Object> map, String classId, String userId, String attendResult, Short needCheckout, Date date) throws Exception;

    /**
     * @param list    班级课程日历
     * @param classId 班级ID
     * @param userId  学员ID
     * @return void
     * @description 签退
     * @author yz
     * @date 2018/9/26 9:47
     * @method updateTrainCourseattend
     */
    public void updateTrainCourseattend(List<Map<String, Object>> list, String classId, String userId, Date date);

    public void updateTrainCourseattends(List<TrainClassschedule> list, String classId, String userId, Date date);

    public void updateTrainCourseattend(Map<String, Object> list, String classId, String userId, Date date);

}