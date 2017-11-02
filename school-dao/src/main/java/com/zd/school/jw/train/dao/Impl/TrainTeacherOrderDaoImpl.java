

package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainTeacherOrderDao;
import com.zd.school.jw.train.model.TrainTeacherOrder;


/**
 * 
 * ClassName: TrainClasstraineeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教师订餐信息
 * date: 2017-03-07
 *
 * @author  zzk 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainTeacherOrderDaoImpl extends BaseDaoImpl<TrainTeacherOrder> implements TrainTeacherOrderDao {
    public TrainTeacherOrderDaoImpl() {
        super(TrainTeacherOrder.class);
        // TODO Auto-generated constructor stub
    }
}