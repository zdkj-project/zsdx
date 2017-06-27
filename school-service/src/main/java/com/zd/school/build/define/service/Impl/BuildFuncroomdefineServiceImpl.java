package com.zd.school.build.define.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.build.define.model.BuildFuncRoomDefine ;
import com.zd.school.build.define.dao.BuildFuncRoomDefineDao ;
import com.zd.school.build.define.service.BuildFuncRoomDefineService ;

/**
 * 
 * ClassName: BuildFuncroomdefinServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: BUILD_T_FUNCROOMDEFIN实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BuildFuncroomdefineServiceImpl extends BaseServiceImpl<BuildFuncRoomDefine> implements BuildFuncRoomDefineService{

    @Resource
    public void setBuildFuncroomdefinDao(BuildFuncRoomDefineDao dao) {
        this.dao = dao;
    }

}