package com.zd.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * 短信操作类
 * 
 * @author zuyubin
 */
public class NotSendUtil {

	private static NotSendUtil notSend;

	private NotSendUtil() {
		// 初始化连接信息
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("notsend.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = properties.getProperty("ip");
		webport = properties.getProperty("webport");
		dbName = properties.getProperty("dbName");
		apiCode = properties.getProperty("apiCode");
		loginName = properties.getProperty("loginName");
		loginPwd = properties.getProperty("loginPwd");
		strSmId = properties.getProperty("strSmId");
		strSrcId = properties.getProperty("strSrcId");
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(ip).append(":").append(webport).append("/axis/services/SMsg?wsdl");
		url = sb.toString();
	}

	public static synchronized NotSendUtil getNotSendUtil() {
		if (notSend == null) {
			synchronized (NotSendUtil.class) {
				if (notSend == null) {
					notSend = new NotSendUtil();
					notSend.init();
				}
			}
		}
		return notSend;
	}

	private String ip, webport, dbName, apiCode, loginName, loginPwd, strSmId, strSrcId;
	private String url;

	/**
	 * 短信发送
	 * 
	 * @param content
	 *            发送内容
	 * @param date
	 *            发送时间 格式为yyyy-MM-dd HH:mm:ss
	 * @param mobiles
	 *            发送的手机号集
	 */
	public String send(String content, Date date, String... mobiles) {
		String result = "0";
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			// 格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			call.setTargetEndpointAddress(new java.net.URL(url));
			call.setOperationName("sendSM");
			Object[] objs = new Object[] { apiCode, loginName, loginPwd, mobiles, content, new Long(strSmId),
					new Long(strSrcId), url, sdf.format(date) };
			result = call.invoke(objs).toString();
		} catch (Exception e) {
			result = "-12";
			System.err.println(e.toString());
		}
		return result;
	}

	// 连接db库
	private String init() {
		String result = "";
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(url));
			call.setOperationName("init");
			result = String.valueOf((call.invoke(new Object[] { ip, dbName, webport, loginName, loginPwd })));
		} catch (Exception e) {
			result = "-12";
		}
		return result;
	}

	// 释放数据库连接
	private String release() {
		String result = "";
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(url));
			call.setOperationName("release");
			result = String.valueOf(call.invoke(new Object[] {}));
		} catch (Exception e) {
			result = "-12";
			System.err.println(e.toString());
		}
		return result;
	}

	/**
	 * 发送结果
	 * 
	 * @param result
	 */
	public void showResult(String result) {
		System.out.println("________________________________________");
		if (result.equals("0")) {
			System.out.println("操作成功\n");
		}
		if (result.equals("-1")) {
			System.out.println("连接数据库出错\n");
		} else if (result.equals("-2")) {
			System.out.println("数据库关闭失败");
		} else if (result.equals("-3")) {
			System.out.println("数据库插入错误");
		} else if (result.equals("-4")) {
			System.out.println("数据库删除错误");
		} else if (result.equals("-5")) {
			System.out.println("数据库查询错误");
		} else if (result.equals("-6")) {
			System.out.println("参数错误");
		} else if (result.equals("-7")) {
			System.out.println("API编码非法");
		} else if (result.equals("-8")) {
			System.out.println("参数超长");
		} else if (result.equals("-9")) {
			System.out.println("没有初始化或初始化失败");
		} else if (result.equals("-10")) {
			System.out.println("API接口处于暂停（失效）状态");
		} else if (result.equals("-11")) {
			System.out.println("短信网关未连接");
		} else if (result.equals("-12")) {
			System.out.println("出现其他错误");
		}
	}
}
