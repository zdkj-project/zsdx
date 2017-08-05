package com.zd.core.constant;

/**
 * 消息发送方式定义
 */
public enum InfoPushWay {
    WX("微信", 1), APP("APP", 2),DX("短信",3);
    /**
     * 发送方式名称
     */
    private String name;

    /**
     * 发送方式编码
     */
    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    InfoPushWay(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
