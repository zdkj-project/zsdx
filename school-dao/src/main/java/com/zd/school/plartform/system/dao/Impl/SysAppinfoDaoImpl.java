package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysAppinfoDao ;
import com.zd.school.plartform.system.model.SysAppinfo ;


/**
 * 
 * ClassName: SysAppinfoDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (SYS_T_APPINFO)实体Dao接口实现类.
 * date: 2017-05-15
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class SysAppinfoDaoImpl extends BaseDaoImpl<SysAppinfo> implements SysAppinfoDao {
    public SysAppinfoDaoImpl() {
        super(SysAppinfo.class);
        // TODO Auto-generated constructor stub
    }
}