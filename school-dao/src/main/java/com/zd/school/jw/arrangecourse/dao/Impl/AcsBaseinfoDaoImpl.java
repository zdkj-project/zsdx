package com.zd.school.jw.arrangecourse.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.arrangecourse.dao.AcsBaseinfoDao ;
import com.zd.school.jw.arrangecourse.model.AcsBaseinfo ;


/**
 * 
 * ClassName: AcsBaseinfoDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (ACS_T_BASEINFO)实体Dao接口实现类.
 * date: 2016-11-25
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class AcsBaseinfoDaoImpl extends BaseDaoImpl<AcsBaseinfo> implements AcsBaseinfoDao {
    public AcsBaseinfoDaoImpl() {
        super(AcsBaseinfo.class);
        // TODO Auto-generated constructor stub
    }
}