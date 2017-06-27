package com.zd.school.jw.schoolcourse.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.schoolcourse.dao.JwStuEnteredCourseDao;
import com.zd.school.jw.schoolcourse.model.JwStuEnteredCourse;
import com.zd.school.jw.schoolcourse.service.JwStuEnteredCourseService;

/**
 * 
 * ClassName: JwPublishcourseServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校本课程发布课程信息(JW_T_PUBLISHCOURSE)实体Service接口实现类.
 * date: 2016-11-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwStuEnteredCourseServiceImpl extends BaseServiceImpl<JwStuEnteredCourse> implements JwStuEnteredCourseService{

    @Resource
    public void setJwStuEnteredCourseDao(JwStuEnteredCourseDao dao) {
        this.dao = dao;
    }

}