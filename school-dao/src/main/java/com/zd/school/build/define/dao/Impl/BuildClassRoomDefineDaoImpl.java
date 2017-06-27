package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.BuildClassRoomDefineDao ;
import com.zd.school.build.define.model.BuildClassRoomDefine;


/**
 * 
 * ClassName: BuildClassroomDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教室信息实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BuildClassRoomDefineDaoImpl extends BaseDaoImpl<BuildClassRoomDefine> implements BuildClassRoomDefineDao {
    public BuildClassRoomDefineDaoImpl() {
        super(BuildClassRoomDefine.class);
        // TODO Auto-generated constructor stub
    }
}