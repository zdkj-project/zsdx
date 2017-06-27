/**
 * Project Name:zd-core File Name:PropertyType.java Package
 * Name:com.zd.core.constant Date:2016年4月27日上午9:18:36 Copyright (c) 2016 ZDKJ
 * All Rights Reserved.
 *
 */

package com.zd.core.constant;

import java.util.Date;

/**
 * ClassName:PropertyType Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年4月27日 上午9:18:36
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public enum PropertyType {
    S(String.class), I(Integer.class), L(Long.class), F(Float.class), N(Double.class), D(Date.class), SD(
            java.sql.Date.class), B(Boolean.class);

    private Class<?> clazz;

    private PropertyType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getValue() {
        return clazz;
    }
}
