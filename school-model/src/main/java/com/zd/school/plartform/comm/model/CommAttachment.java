package com.zd.school.plartform.comm.model;

import com.zd.core.annotation.FieldInfo;

/**
 * c通用提供给前端的附件下载路径
 */
public class CommAttachment {
    @FieldInfo(name = "附件名称")
    private String attachName;

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    @FieldInfo(name = "下载路径")
    private String attachUrl;

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }
}
