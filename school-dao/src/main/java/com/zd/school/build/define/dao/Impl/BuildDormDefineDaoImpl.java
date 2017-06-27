package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.BuildDormDefineDao;
import com.zd.school.build.define.model.BuildDormDefine;


/**
 * 
 * ClassName: BuildOfficeDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 宿舍定义接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BuildDormDefineDaoImpl extends BaseDaoImpl<BuildDormDefine> implements BuildDormDefineDao {
    public BuildDormDefineDaoImpl() {
        super(BuildDormDefine.class);
        // TODO Auto-generated constructor stub
    }
}