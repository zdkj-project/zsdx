package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClassscheduleDao ;
import com.zd.school.jw.train.model.TrainClassschedule ;


/**
 * 
 * ClassName: TrainClassscheduleDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级课程日历(TRAIN_T_CLASSSCHEDULE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClassscheduleDaoImpl extends BaseDaoImpl<TrainClassschedule> implements TrainClassscheduleDao {
    public TrainClassscheduleDaoImpl() {
        super(TrainClassschedule.class);
        // TODO Auto-generated constructor stub
    }
}