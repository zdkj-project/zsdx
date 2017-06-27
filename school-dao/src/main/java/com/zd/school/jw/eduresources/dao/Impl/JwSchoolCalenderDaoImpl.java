package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwSchoolCalenderDao;
import com.zd.school.jw.eduresources.model.JwSchoolCalender;


/**
 * 
 * ClassName: JwCalenderDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体Dao接口实现类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwSchoolCalenderDaoImpl extends BaseDaoImpl<JwSchoolCalender> implements JwSchoolCalenderDao {
    public JwSchoolCalenderDaoImpl() {
        super(JwSchoolCalender.class);
        // TODO Auto-generated constructor stub
    }
}