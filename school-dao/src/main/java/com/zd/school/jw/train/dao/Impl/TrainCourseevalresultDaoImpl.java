package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCourseevalresultDao ;
import com.zd.school.jw.train.model.TrainCourseevalresult ;


/**
 * 
 * ClassName: TrainCourseevalresultDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程评价结果(TRAIN_T_COURSEEVALRESULT)实体Dao接口实现类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCourseevalresultDaoImpl extends BaseDaoImpl<TrainCourseevalresult> implements TrainCourseevalresultDao {
    public TrainCourseevalresultDaoImpl() {
        super(TrainCourseevalresult.class);
        // TODO Auto-generated constructor stub
    }
}