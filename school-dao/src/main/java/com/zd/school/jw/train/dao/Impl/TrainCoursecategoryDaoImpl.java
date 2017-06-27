package com.zd.school.jw.train.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainCoursecategoryDao ;
import com.zd.school.jw.train.model.TrainCoursecategory ;


/**
 * 
 * ClassName: TrainCoursecategoryDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程分类信息(TRAIN_T_COURSECATEGORY)实体Dao接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class TrainCoursecategoryDaoImpl extends BaseDaoImpl<TrainCoursecategory> implements TrainCoursecategoryDao {
    public TrainCoursecategoryDaoImpl() {
        super(TrainCoursecategory.class);
        // TODO Auto-generated constructor stub
    }
}