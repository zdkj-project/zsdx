package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCourseattendDao ;
import com.zd.school.jw.train.model.TrainCourseattend ;


/**
 * 
 * ClassName: TrainCourseattendDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程考勤刷卡结果(TRAIN_T_COURSEATTEND)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCourseattendDaoImpl extends BaseDaoImpl<TrainCourseattend> implements TrainCourseattendDao {
    public TrainCourseattendDaoImpl() {
        super(TrainCourseattend.class);
        // TODO Auto-generated constructor stub
    }
}