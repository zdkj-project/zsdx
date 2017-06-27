package com.zd.core.model.vo;

/**
 * 排序字段
 * 
 * @author luoyibo
 *
 */
public class DataSort {

    /** 排序字段 */
    public String property;

    /** 排序模式 */
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
