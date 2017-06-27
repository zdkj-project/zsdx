package com.zd.school.student.studentclass.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.student.studentclass.model.StuDividescore ;
import com.zd.school.student.studentclass.dao.StuDividescoreDao ;
import com.zd.school.student.studentclass.service.StuDividescoreService ;

/**
 * 
 * ClassName: StuDividescoreServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班成绩实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class StuDividescoreServiceImpl extends BaseServiceImpl<StuDividescore> implements StuDividescoreService{

    @Resource
    public void setStuDividescoreDao(StuDividescoreDao dao) {
        this.dao = dao;
    }

}