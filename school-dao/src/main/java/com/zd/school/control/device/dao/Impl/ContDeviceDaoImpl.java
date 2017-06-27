package com.zd.school.control.device.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.control.device.dao.ContDeviceDao ;
import com.zd.school.control.device.model.ContDevice ;


/**
 * 
 * ClassName: ContDeviceDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_DEVICE实体Dao接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class ContDeviceDaoImpl extends BaseDaoImpl<ContDevice> implements ContDeviceDao {
    public ContDeviceDaoImpl() {
        super(ContDevice.class);
        // TODO Auto-generated constructor stub
    }
}