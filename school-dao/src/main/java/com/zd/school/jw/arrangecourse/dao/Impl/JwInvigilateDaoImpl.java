package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.JwInvigilateDao ;
import com.zd.school.jw.arrangecourse.model.JwInvigilate ;


/**
 * 
 * ClassName: JwInvigilateDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_INVIGILATE实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwInvigilateDaoImpl extends BaseDaoImpl<JwInvigilate> implements JwInvigilateDao {
    public JwInvigilateDaoImpl() {
        super(JwInvigilate.class);
        // TODO Auto-generated constructor stub
    }
}