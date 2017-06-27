package com.zd.core.model.vo;

/**
 * 字段过滤器
 * 
 * @author luoyibo
 *
 */

public class DataFilter {
    /** 字段类型 */
    public String type;

    /** 字段值 */
    public String value;

    /** 字段名称 */
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
