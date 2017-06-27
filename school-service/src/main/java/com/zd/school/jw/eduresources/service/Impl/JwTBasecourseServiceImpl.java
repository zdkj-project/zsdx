package com.zd.school.jw.eduresources.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.eduresources.dao.JwTBasecourseDao;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.jw.eduresources.service.JwTBasecourseService;

/**
 * 
 * ClassName: JwTBasecourseServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 基础课程信息实体Service接口实现类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwTBasecourseServiceImpl extends BaseServiceImpl<JwTBasecourse> implements JwTBasecourseService {

    @Resource
    public void setJwTBasecourseDao(JwTBasecourseDao dao) {
        this.dao = dao;
    }

}