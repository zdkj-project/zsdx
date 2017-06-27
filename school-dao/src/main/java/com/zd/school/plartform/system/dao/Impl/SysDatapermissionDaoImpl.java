package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysDatapermissionDao ;
import com.zd.school.plartform.system.model.SysDatapermission ;


/**
 * 
 * ClassName: SysDatapermissionDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 用户数据权限表(SYS_T_DATAPERMISSION)实体Dao接口实现类.
 * date: 2016-09-01
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class SysDatapermissionDaoImpl extends BaseDaoImpl<SysDatapermission> implements SysDatapermissionDao {
    public SysDatapermissionDaoImpl() {
        super(SysDatapermission.class);
        // TODO Auto-generated constructor stub
    }
}