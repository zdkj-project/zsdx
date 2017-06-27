package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCreditsruleDao ;
import com.zd.school.jw.train.model.TrainCreditsrule ;


/**
 * 
 * ClassName: TrainCreditsruleDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学分计算规则(TRAIN_T_CREDITSRULE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCreditsruleDaoImpl extends BaseDaoImpl<TrainCreditsrule> implements TrainCreditsruleDao {
    public TrainCreditsruleDaoImpl() {
        super(TrainCreditsrule.class);
        // TODO Auto-generated constructor stub
    }
}