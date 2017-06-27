/**
 * Project Name:school-model
 * File Name:UpGradeRule.java
 * Package Name:com.zd.school.plartform.comm.model
 * Date:2016年8月25日下午1:08:07
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.comm.model;

import com.zd.core.annotation.FieldInfo;

/**
 * ClassName:UpGradeRule Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年8月25日 下午1:08:07
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class UpGradeRule {
    @FieldInfo(name = "升级前名称")
    private String beforeName;

    public String getBeforeName() {
        return beforeName;
    }

    public void setBeforeName(String beforeName) {
        this.beforeName = beforeName;
    }

    @FieldInfo(name = "升级的名称")
    private String afterName;

    public String getAfterName() {
        return afterName;
    }

    public void setAfterName(String afterName) {
        this.afterName = afterName;
    }

    @FieldInfo(name = "升级方向")
    private String upDirect;

    public String getUpDirect() {
        return upDirect;
    }

    public void setUpDirect(String upDirect) {
        this.upDirect = upDirect;
    }
}
