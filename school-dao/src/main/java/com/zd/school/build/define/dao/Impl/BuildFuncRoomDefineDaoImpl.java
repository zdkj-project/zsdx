package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.BuildFuncRoomDefineDao ;
import com.zd.school.build.define.model.BuildFuncRoomDefine ;


/**
 * 
 * ClassName: BuildFuncroomdefinDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: BUILD_T_FUNCROOMDEFIN实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BuildFuncRoomDefineDaoImpl extends BaseDaoImpl<BuildFuncRoomDefine> implements BuildFuncRoomDefineDao {
    public BuildFuncRoomDefineDaoImpl() {
        super(BuildFuncRoomDefine.class);
        // TODO Auto-generated constructor stub
    }
}