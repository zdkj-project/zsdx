package com.zd.school.plartform.baseset.service;

import java.lang.reflect.InvocationTargetException;

import com.zd.core.service.BaseService;
import com.zd.school.plartform.baseset.model.BaseCampus;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseCampusService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 校区信息实体Service接口类. date: 2016-08-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface BaseCampusService extends BaseService<BaseCampus> {

    public BaseCampus doAdd(BaseCampus entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException;

    public BaseCampus doUpdate(BaseCampus entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException;

    public boolean doDelete(String delIds, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException;
}