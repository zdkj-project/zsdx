package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClasssevaldetailDao ;
import com.zd.school.jw.train.model.TrainClassevaldetail;


/**
 * 
 * ClassName: TrainClasssevaldetailDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级评价明细(TRAIN_T_CLASSSEVALDETAIL)实体Dao接口实现类.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClasssevaldetailDaoImpl extends BaseDaoImpl<TrainClassevaldetail> implements TrainClasssevaldetailDao {
    public TrainClasssevaldetailDaoImpl() {
        super(TrainClassevaldetail.class);
        // TODO Auto-generated constructor stub
    }
}