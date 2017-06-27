package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClassrealdinnerDao ;
import com.zd.school.jw.train.model.TrainClassrealdinner ;


/**
 * 
 * ClassName: TrainClassrealdinnerDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级就餐登记(TRAIN_T_CLASSREALDINNER)实体Dao接口实现类.
 * date: 2017-06-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClassrealdinnerDaoImpl extends BaseDaoImpl<TrainClassrealdinner> implements TrainClassrealdinnerDao {
    public TrainClassrealdinnerDaoImpl() {
        super(TrainClassrealdinner.class);
        // TODO Auto-generated constructor stub
    }
}