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

    private static NotSendUtil notSendUtil = null;

    private static String host = "172.23.6.2";
    private static String dbName = "mas";
    private static String apiId = "dxykt";
    private static String name = "dxykt";
    private static String pwd = "xxwlzx88889827";
    private static String smID = "10";
    private static String dbPort = "3306";
    private static APIClient handler = new APIClient();
    BufferedReader in = null;

    private NotSendUtil() {
        // 初始化连接信息
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("notsend.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = properties.getProperty("ip");
        apiId = properties.getProperty("apiCode");
        name = properties.getProperty("loginName");
        pwd = properties.getProperty("loginPwd");
        dbName = properties.getProperty("dbName");
        smID = properties.getProperty("strSmId");
        dbPort = properties.getProperty("dbPort");
    }

    public static synchronized NotSendUtil notSendUtil() {
        if (notSendUtil == null) {
            synchronized (NotSendUtil.class) {
                if (notSendUtil == null) {
                    notSendUtil = new NotSendUtil();
                }
            }
        }
        return notSendUtil;
    }

    public boolean isStrNull(String str) {
        if (str == "" || str == null || str.equals("") || str.equals(null))
            return true;
        return false;
    }

    /**
     * 初始化连接
     * @return
     */
    private int init() {
        return handler.init(host, name, pwd, apiId, dbName, Integer.valueOf(dbPort));
    }

    /**
     * 关闭连接
     */
    private void release() {
        handler.release();
    }

    /**
     * 单个发送短信
     * @param mobile 发送的手机号
     * @param content 消息内容
     * @return
     */


    public String sendSMS(String mobile, String content) {
        int status = 1;
        String sendResult = "";
        status = init();
        if (status == APIClient.IMAPI_SUCC) {
            status = handler.sendSM(mobile, content, Long.valueOf(smID));
            switch (status) {
                case APIClient.IMAPI_SUCC:
                    sendResult = "发送成功";
                    break;
                case APIClient.IMAPI_INIT_ERR:
                    sendResult = "未初始化";
                    break;
                case APIClient.IMAPI_CONN_ERR:
                    sendResult = "连接失败";
                    break;
                case APIClient.IMAPI_DATA_ERR:
                    sendResult = "参数错误";
                    break;
                case APIClient.IMAPI_DATA_TOOLONG:
                    sendResult = "消息内容太长";
                    break;
                case APIClient.IMAPI_INS_ERR:
                    sendResult = "数据库插入错误";
                    break;
                default:
                    sendResult = "其它错误";
            }
            release();
        } else {
            sendResult = "初始化错误";
        }

        return sendResult;
    }

    /**
     * 群发短信
     * @param mobiles 群发的手机号
     * @param content 发送的信息
     * @return
     */
    public  String sendSMSs(String[] mobiles, String content) {
        int status = 1;
        String sendResult = "";
        status = init();
        if (status == APIClient.IMAPI_SUCC) {
            status = handler.sendSM(mobiles, content, Long.valueOf(smID));
            switch (status) {
                case APIClient.IMAPI_SUCC:
                    sendResult = "发送成功";
                    break;
                case APIClient.IMAPI_INIT_ERR:
                    sendResult = "未初始化";
                    break;
                case APIClient.IMAPI_CONN_ERR:
                    sendResult = "连接失败";
                    break;
                case APIClient.IMAPI_DATA_ERR:
                    sendResult = "参数错误";
                    break;
                case APIClient.IMAPI_DATA_TOOLONG:
                    sendResult = "消息内容太长";
                    break;
                case APIClient.IMAPI_INS_ERR:
                    sendResult = "数据库插入错误";
                    break;
                default:
                    sendResult = "其它错误";
            }
            release();
        } else {
            sendResult = "初始化错误";
        }
        return sendResult;
    }
}
