/**
  * Copyright 2018 bejson.com 
  */
package com.zd.school.opu;

import com.zd.core.util.JsonBuilder;

/**
 * Auto-generated: 2018-09-04 10:29:16
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

  /*  rspCode	int	0表示成功，1表示失败
    rspMsg	String	调用信息说明
    orderid	int	新增订单产生的预订号*/

    private int orderid;
    private int rspCode;
    private String rspMsg;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public void setRspCode(int rspCode) {
         this.rspCode = rspCode;
     }
     public int getRspCode() {
         return rspCode;
     }

    public void setRspMsg(String rspMsg) {
         this.rspMsg = rspMsg;
     }
     public String getRspMsg() {
         return rspMsg;
     }

    public static JsonRootBean fromJson(String json) {
        JsonRootBean result = (JsonRootBean) JsonBuilder.getInstance().fromJson(json, JsonRootBean.class);
        return result;
    }

}