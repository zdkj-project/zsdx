package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwCalenderdetailDao ;
import com.zd.school.jw.eduresources.model.JwCalenderdetail ;


/**
 * 
 * ClassName: JwCalenderdetailDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历节次信息表(JW_T_CALENDERDETAIL)实体Dao接口实现类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwCalenderdetailDaoImpl extends BaseDaoImpl<JwCalenderdetail> implements JwCalenderdetailDao {
    public JwCalenderdetailDaoImpl() {
        super(JwCalenderdetail.class);
        // TODO Auto-generated constructor stub
    }
}