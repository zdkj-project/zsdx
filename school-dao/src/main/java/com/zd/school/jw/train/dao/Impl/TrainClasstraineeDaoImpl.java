package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClasstraineeDao ;
import com.zd.school.jw.train.model.TrainClasstrainee ;


/**
 * 
 * ClassName: TrainClasstraineeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级学员信息(TRAIN_T_CLASSTRAINEE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClasstraineeDaoImpl extends BaseDaoImpl<TrainClasstrainee> implements TrainClasstraineeDao {
    public TrainClasstraineeDaoImpl() {
        super(TrainClasstrainee.class);
        // TODO Auto-generated constructor stub
    }
}