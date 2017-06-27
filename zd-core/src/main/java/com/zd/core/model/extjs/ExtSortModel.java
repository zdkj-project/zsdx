/**
 * Project Name:zd-core
 * File Name:ExtSortModel.java
 * Package Name:com.zd.core.model.extjs
 * Date:2016年6月3日上午11:44:33
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.model.extjs;

/**
 * ClassName:ExtSortModel Reason: Ext排序的model Date: 2016年6月3日 上午11:44:33
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class ExtSortModel {

    /** 排序的属性字段 */
    public String property;

    /** 排序的模式 */
    public String direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
