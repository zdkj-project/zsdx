package com.zd.school.control.device.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.control.device.dao.ContPerimissonDao ;
import com.zd.school.control.device.model.ContPerimisson ;


/**
 * 
 * ClassName: ContPerimissonDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_PERIMISSON实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class ContPerimissonDaoImpl extends BaseDaoImpl<ContPerimisson> implements ContPerimissonDao {
    public ContPerimissonDaoImpl() {
        super(ContPerimisson.class);
        // TODO Auto-generated constructor stub
    }
}