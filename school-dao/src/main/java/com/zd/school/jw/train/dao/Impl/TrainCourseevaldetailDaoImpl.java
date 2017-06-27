package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCourseevaldetailDao ;
import com.zd.school.jw.train.model.TrainCourseevaldetail ;


/**
 * 
 * ClassName: TrainCourseevaldetailDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程评价明细 (TRAIN_T_COURSEEVALDETAIL)实体Dao接口实现类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCourseevaldetailDaoImpl extends BaseDaoImpl<TrainCourseevaldetail> implements TrainCourseevaldetailDao {
    public TrainCourseevaldetailDaoImpl() {
        super(TrainCourseevaldetail.class);
        // TODO Auto-generated constructor stub
    }
}