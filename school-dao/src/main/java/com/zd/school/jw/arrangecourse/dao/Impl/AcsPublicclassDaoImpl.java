package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.AcsPublicclassDao ;
import com.zd.school.jw.arrangecourse.model.AcsPublicclass ;


/**
 * 
 * ClassName: AcsPublicclassDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 2.1公用教室基础数据(ACS_T_PUBLICCLASS)实体Dao接口实现类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class AcsPublicclassDaoImpl extends BaseDaoImpl<AcsPublicclass> implements AcsPublicclassDao {
    public AcsPublicclassDaoImpl() {
        super(AcsPublicclass.class);
        // TODO Auto-generated constructor stub
    }
}