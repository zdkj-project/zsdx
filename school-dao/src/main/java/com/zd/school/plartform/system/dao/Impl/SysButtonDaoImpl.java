package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysButtonDao ;
import com.zd.school.plartform.system.model.SysButton ;


/**
 * 
 * ClassName: BaseTButtonDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 功能按钮实体Dao接口实现类.
 * date: 2016-07-17
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class SysButtonDaoImpl extends BaseDaoImpl<SysButton> implements SysButtonDao {
    public SysButtonDaoImpl() {
        super(SysButton.class);
        // TODO Auto-generated constructor stub
    }
}