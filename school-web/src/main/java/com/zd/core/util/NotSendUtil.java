package com.zd.core.util;

import com.jasson.im.api.APIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 短信操作类
 * 
 * @author zuyubin
 */
public class NotSendUtil {
    private APIClient handler = new APIClient();
    BufferedReader in = null;
    private static NotSendUtil notSendUtil;
    private String ip, dbPort, dbName, apiCode, loginName, loginPwd, strSmId, strSrcId;
    private String url;

    private NotSendUtil() {
        // 初始化连接信息
        Properties properties = new Properties();
        System.out.println(getClass().getClassLoader());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("notsend.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ip = properties.getProperty("ip");
        dbPort = properties.getProperty("dbPort");
        apiCode = properties.getProperty("apiCode");
        loginName = properties.getProperty("loginName");
        loginPwd = properties.getProperty("loginPwd");
        strSmId = properties.getProperty("strSmId");
        strSrcId = properties.getProperty("strSrcId");
        dbName = properties.getProperty("dbName");
    }

    public static synchronized NotSendUtil notSendUtil() {
        if (notSendUtil == null) {
            synchronized (NotSendUtil.class) {
                if (notSendUtil == null) {
                    notSendUtil = new NotSendUtil();
                    //初始化连接
                    notSendUtil().init();
                }
            }
        }
        return notSendUtil;
    }

    public void release()
    {
        handler.release();
        Thread.currentThread().interrupt();
    }

    private void init(){
        int connectRe = handler.init(ip, loginName, loginPwd, apiCode,dbName);
        if(connectRe == APIClient.IMAPI_SUCC)
            info("初始化成功");
        else if(connectRe == APIClient.IMAPI_CONN_ERR)
            info("连接失败");
        else if(connectRe == APIClient.IMAPI_API_ERR)
            info("apiID不存在");
        if(connectRe != APIClient.IMAPI_SUCC)
        {
            System.exit(-1);
        }
    }

    public void info(Object obj)
    {
        System.out.println(obj);
    }

    public void Send(String tmpContent,String...mobiles) {
        //发送短信返回发送结果
        int result = handler.sendSM(mobiles, tmpContent, Long.valueOf(strSmId));
        if (result == APIClient.IMAPI_SUCC) {
            info("发送成功\n");
        } else if (result == APIClient.IMAPI_INIT_ERR)
            info("未初始化");
        else if (result == APIClient.IMAPI_CONN_ERR)
            info("数据库连接失败");
        else if (result == APIClient.IMAPI_DATA_ERR)
            info("参数错误");
        else if (result == APIClient.IMAPI_DATA_TOOLONG)
            info("消息内容太长");
        else if (result == APIClient.IMAPI_INS_ERR)
            info("数据库插入错误");
        else
            info("出现其他错误");
    }
}
