package com.zd.school.jw.arrangecourse.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange ;
import com.zd.school.jw.arrangecourse.dao.JwCourseArrangeDao ;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService ;

/**
 * 
 * ClassName: JwCourseArrangeServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 排课课程表实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwCourseArrangeServiceImpl extends BaseServiceImpl<JwCourseArrange> implements JwCourseArrangeService{

    @Resource
    public void setJwCourseArrangeDao(JwCourseArrangeDao dao) {
        this.dao = dao;
    }

}