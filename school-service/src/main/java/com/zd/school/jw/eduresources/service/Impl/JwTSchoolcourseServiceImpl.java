package com.zd.school.jw.eduresources.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.eduresources.dao.JwTSchoolcourseDao;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.service.JwTSchoolcourseService;

/**
 * 
 * ClassName: JwTSchoolcourseServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 校本课程实体Service接口实现类. date: 2016-03-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwTSchoolcourseServiceImpl extends BaseServiceImpl<JwTSchoolcourse> implements JwTSchoolcourseService {

    @Resource
    public void setJwTSchoolcourseDao(JwTSchoolcourseDao dao) {
        this.dao = dao;
    }

}