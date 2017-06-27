package com.zd.school.student.studentinfo.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.student.studentinfo.model.StuParents ;
import com.zd.school.student.studentinfo.dao.StuParentsDao ;
import com.zd.school.student.studentinfo.service.StuParentsService ;

/**
 * 
 * ClassName: StuParentsServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生家长信息实体Service接口实现类.
 * date: 2016-08-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class StuParentsServiceImpl extends BaseServiceImpl<StuParents> implements StuParentsService{

    @Resource
    public void setStuParentsDao(StuParentsDao dao) {
        this.dao = dao;
    }

}