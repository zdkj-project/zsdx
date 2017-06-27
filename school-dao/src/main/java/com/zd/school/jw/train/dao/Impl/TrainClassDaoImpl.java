package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClassDao ;
import com.zd.school.jw.train.model.TrainClass ;


/**
 * 
 * ClassName: TrainClassDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 培训开班信息(TRAIN_T_CLASS)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClassDaoImpl extends BaseDaoImpl<TrainClass> implements TrainClassDao {
    public TrainClassDaoImpl() {
        super(TrainClass.class);
        // TODO Auto-generated constructor stub
    }
}