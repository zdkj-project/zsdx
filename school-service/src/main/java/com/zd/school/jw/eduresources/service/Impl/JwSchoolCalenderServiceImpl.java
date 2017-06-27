package com.zd.school.jw.eduresources.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.eduresources.dao.JwSchoolCalenderDao;
import com.zd.school.jw.eduresources.model.JwSchoolCalender;
import com.zd.school.jw.eduresources.service.JwSchoolCalenderService;

/**
 * 
 * ClassName: JwCalenderServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体Service接口实现类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwSchoolCalenderServiceImpl extends BaseServiceImpl<JwSchoolCalender> implements JwSchoolCalenderService{

    @Resource
    public void setJwSchoolCalenderDao(JwSchoolCalenderDao dao) {
        this.dao = dao;
    }

	

}