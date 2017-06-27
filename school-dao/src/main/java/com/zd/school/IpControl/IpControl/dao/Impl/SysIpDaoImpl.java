package com.zd.school.IpControl.IpControl.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.IpControl.IpControl.dao.SysIpDao ;
import com.zd.school.IpControl.IpControl.model.SysIp ;


/**
 * 
 * ClassName: SysIpDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (SYS_T_IP)实体Dao接口实现类.
 * date: 2017-05-10
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class SysIpDaoImpl extends BaseDaoImpl<SysIp> implements SysIpDao {
    public SysIpDaoImpl() {
        super(SysIp.class);
        // TODO Auto-generated constructor stub
    }
}