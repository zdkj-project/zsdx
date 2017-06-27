package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCourseinfoDao ;
import com.zd.school.jw.train.model.TrainCourseinfo ;


/**
 * 
 * ClassName: TrainCourseinfoDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程信息(TRAIN_T_COURSEINFO)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCourseinfoDaoImpl extends BaseDaoImpl<TrainCourseinfo> implements TrainCourseinfoDao {
    public TrainCourseinfoDaoImpl() {
        super(TrainCourseinfo.class);
        // TODO Auto-generated constructor stub
    }
}