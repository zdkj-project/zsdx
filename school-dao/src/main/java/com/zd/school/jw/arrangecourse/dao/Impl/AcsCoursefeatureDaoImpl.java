package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.AcsCoursefeatureDao ;
import com.zd.school.jw.arrangecourse.model.AcsCoursefeature ;


/**
 * 
 * ClassName: AcsCoursefeatureDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_COURSEFEATURE)实体Dao接口实现类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class AcsCoursefeatureDaoImpl extends BaseDaoImpl<AcsCoursefeature> implements AcsCoursefeatureDao {
    public AcsCoursefeatureDaoImpl() {
        super(AcsCoursefeature.class);
        // TODO Auto-generated constructor stub
    }
}