package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseDicDao ;
import com.zd.school.plartform.baseset.model.BaseDic ;


/**
 * 
 * ClassName: BaseDicDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 数据字典实体Dao接口实现类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseDicDaoImpl extends BaseDaoImpl<BaseDic> implements BaseDicDao {
    public BaseDicDaoImpl() {
        super(BaseDic.class);
        // TODO Auto-generated constructor stub
    }
}