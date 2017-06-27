package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainClassorderDao ;
import com.zd.school.jw.train.model.TrainClassorder ;


/**
 * 
 * ClassName: TrainClassorderDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级订餐/住宿信息(TRAIN_T_CLASSORDER)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainClassorderDaoImpl extends BaseDaoImpl<TrainClassorder> implements TrainClassorderDao {
    public TrainClassorderDaoImpl() {
        super(TrainClassorder.class);
        // TODO Auto-generated constructor stub
    }
}