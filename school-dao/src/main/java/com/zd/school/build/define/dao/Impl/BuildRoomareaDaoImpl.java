package com.zd.school.build.define.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.build.define.dao.BuildRoomareaDao ;
import com.zd.school.build.define.model.BuildRoomarea ;


/**
 * 
 * ClassName: BuildRoomareaDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教室区域实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BuildRoomareaDaoImpl extends BaseDaoImpl<BuildRoomarea> implements BuildRoomareaDao {
    public BuildRoomareaDaoImpl() {
        super(BuildRoomarea.class);
        // TODO Auto-generated constructor stub
    }
}