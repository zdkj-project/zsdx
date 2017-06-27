package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCheckruleDao ;
import com.zd.school.jw.train.model.TrainCheckrule ;


/**
 * 
 * ClassName: TrainCheckruleDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程考勤规则(TRAIN_T_CHECKRULE)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCheckruleDaoImpl extends BaseDaoImpl<TrainCheckrule> implements TrainCheckruleDao {
    public TrainCheckruleDaoImpl() {
        super(TrainCheckrule.class);
        // TODO Auto-generated constructor stub
    }
}