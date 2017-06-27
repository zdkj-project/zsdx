package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.AcsTimecourselimitDao ;
import com.zd.school.jw.arrangecourse.model.AcsTimecourselimit ;


/**
 * 
 * ClassName: AcsTimecourselimitDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_TIMECOURSELIMIT)实体Dao接口实现类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class AcsTimecourselimitDaoImpl extends BaseDaoImpl<AcsTimecourselimit> implements AcsTimecourselimitDao {
    public AcsTimecourselimitDaoImpl() {
        super(AcsTimecourselimit.class);
        // TODO Auto-generated constructor stub
    }
}