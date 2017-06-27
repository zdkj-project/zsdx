package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.BuildLaboratoryDefineDao ;
import com.zd.school.build.define.model.BuildLaboratoryDefine ;


/**
 * 
 * ClassName: BuildLaboratorydefinDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: BUILD_T_LABORATORYDEFIN实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BuildLaboratoryDefineDaoImpl extends BaseDaoImpl<BuildLaboratoryDefine> implements BuildLaboratoryDefineDao {
    public BuildLaboratoryDefineDaoImpl() {
        super(BuildLaboratoryDefine.class);
        // TODO Auto-generated constructor stub
    }
}