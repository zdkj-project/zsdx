/**
 * Project Name:zd-core
 * File Name:FieldInfo.java
 * Package Name:com.zd.core.annotation
 * Date:2016年5月30日下午8:18:01
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:FieldInfo Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年5月30日 下午8:18:01
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldInfo {
    /**
     * 字段名称
     * @return
     */
    public String name() default "";

    /**
     * 字段类型
     * @return
     */

    public String type() default "";

    /**
     * 字段说明
     * @return
     */
    public String explain() default "";
}
