package com.zd.school.plartform.baseset.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.baseset.model.BaseSchool ;
import com.zd.school.plartform.baseset.dao.BaseSchoolDao ;
import com.zd.school.plartform.baseset.service.BaseSchoolService ;

/**
 * 
 * ClassName: BaseSchoolServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学校信息实体Service接口实现类.
 * date: 2016-08-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BaseSchoolServiceImpl extends BaseServiceImpl<BaseSchool> implements BaseSchoolService{

    @Resource
    public void setBaseSchoolDao(BaseSchoolDao dao) {
        this.dao = dao;
    }

}