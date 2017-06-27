package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClassevalresultDao ;
import com.zd.school.jw.train.model.TrainClassevalresult ;


/**
 * 
 * ClassName: TrainClassevalresultDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description:  班级评价结果(TRAIN_T_CLASSEVALRESULT)实体Dao接口实现类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClassevalresultDaoImpl extends BaseDaoImpl<TrainClassevalresult> implements TrainClassevalresultDao {
    public TrainClassevalresultDaoImpl() {
        super(TrainClassevalresult.class);
        // TODO Auto-generated constructor stub
    }
}