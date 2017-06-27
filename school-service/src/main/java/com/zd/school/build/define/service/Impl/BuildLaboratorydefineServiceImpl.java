package com.zd.school.build.define.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.define.model.BuildLaboratoryDefine ;
import com.zd.school.build.define.dao.BuildLaboratoryDefineDao ;
import com.zd.school.build.define.service.BuildLaboratoryDefineService ;

/**
 * 
 * ClassName: BuildLaboratorydefinServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: BUILD_T_LABORATORYDEFIN实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BuildLaboratorydefineServiceImpl extends BaseServiceImpl<BuildLaboratoryDefine> implements BuildLaboratoryDefineService{

    @Resource
    public void setBuildLaboratorydefinDao(BuildLaboratoryDefineDao dao) {
        this.dao = dao;
    }

}