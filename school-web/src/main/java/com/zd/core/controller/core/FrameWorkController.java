/**
 * Project Name:school-web
 * File Name:FrameWorkController.java
 * Package Name:com.zd.core.controller.core
 * Date:2016年6月1日下午2:15:33
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.controller.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zd.core.constant.Constant;
import com.zd.core.model.BaseEntity;
import com.zd.school.plartform.system.model.SysUser;

/**
 * ClassName:FrameWorkController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年6月1日 下午2:15:33
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public abstract class FrameWorkController<E extends BaseEntity> extends BaseController<E> implements Constant {

    public SysUser getCurrentSysUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return (SysUser) request.getSession().getAttribute(SESSION_SYS_USER);
    }
}
