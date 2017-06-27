package com.zd.school.build.allot.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.allot.dao.DormStudentDormDao;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.service.DormStudentdormService ;

/**
 * 
 * ClassName: DormStudentdormServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (DORM_T_STUDENTDORM)实体Service接口实现类.
 * date: 2016-08-26
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DormStudentdormServiceImpl extends BaseServiceImpl<DormStudentDorm> implements DormStudentdormService{

    @Resource
    public void setDormStudentdormDao(DormStudentDormDao dao) {
        this.dao = dao;
    }

}