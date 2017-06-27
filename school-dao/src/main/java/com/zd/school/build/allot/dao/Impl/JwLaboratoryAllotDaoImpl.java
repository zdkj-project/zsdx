package com.zd.school.build.allot.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.allot.dao.JwLaboratoryAllotDao;
import com.zd.school.build.allot.model.JwLaboratoryAllot;


/**
 * 
 * ClassName: JwLaboratoryallotDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_LABORATORYALLOT实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwLaboratoryAllotDaoImpl extends BaseDaoImpl<JwLaboratoryAllot> implements JwLaboratoryAllotDao {
    public JwLaboratoryAllotDaoImpl() {
        super(JwLaboratoryAllot.class);
        // TODO Auto-generated constructor stub
    }
}