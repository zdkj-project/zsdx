package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseDicitemDao ;
import com.zd.school.plartform.baseset.model.BaseDicitem ;


/**
 * 
 * ClassName: BaseDicitemDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 数据字典项实体Dao接口实现类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseDicitemDaoImpl extends BaseDaoImpl<BaseDicitem> implements BaseDicitemDao {
    public BaseDicitemDaoImpl() {
        super(BaseDicitem.class);
        // TODO Auto-generated constructor stub
    }
}