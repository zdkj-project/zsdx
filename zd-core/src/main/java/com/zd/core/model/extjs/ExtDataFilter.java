/**
 * Project Name:zd-core
 * File Name:ExtDataFilter.java
 * Package Name:com.zd.core.model.extjs
 * Date:2016年6月10日下午12:05:45
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.model.extjs;

/**
 * ClassName:ExtDataFilter Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年6月10日 下午12:05:45
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class ExtDataFilter {

    /** 数据类型 */
    public String type;

    /** 值 */
    public String value;

    /** 字段名 */
    public String field;

    /** 比较规则 */
    public String comparison;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }
}
