package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainTraineeDao ;
import com.zd.school.jw.train.model.TrainTrainee ;


/**
 * 
 * ClassName: TrainTraineeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学员信息(TRAIN_T_TRAINEE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainTraineeDaoImpl extends BaseDaoImpl<TrainTrainee> implements TrainTraineeDao {
    public TrainTraineeDaoImpl() {
        super(TrainTrainee.class);
        // TODO Auto-generated constructor stub
    }
}