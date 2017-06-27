package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseCampusDao ;
import com.zd.school.plartform.baseset.model.BaseCampus ;


/**
 * 
 * ClassName: BaseCampusDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校区信息实体Dao接口实现类.
 * date: 2016-08-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseCampusDaoImpl extends BaseDaoImpl<BaseCampus> implements BaseCampusDao {
    public BaseCampusDaoImpl() {
        super(BaseCampus.class);
        // TODO Auto-generated constructor stub
    }
}