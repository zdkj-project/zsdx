package com.zd.core.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.w3c.dom.Document;

public class WebServiceUtil {
	// 避免重复读取,已经读取过的缓存进来
	private static Map<Properties, WebServiceUtil> webServiceMap = new HashMap<Properties, WebServiceUtil>();

	private String nameSpace = "";
	private String wsdlUrl = "";
	private String serviceName = "";
	private String portName = "";
	private String elementName = "";
	private String responseName = "";
	int timeout = 20000;
	

	public WebServiceUtil(Properties prop, String element, String responseName) {
		if (webServiceMap.get(prop) == null) {
			// System.out.println("null");
			this.nameSpace = prop.getProperty("nameSpace");
			this.wsdlUrl = prop.getProperty("wsdlUrl");
			this.serviceName = prop.getProperty("serviceName");
			this.portName = prop.getProperty("portName");
			webServiceMap.put(prop, this);
		} else {
			// System.out.println("notnull");
			WebServiceUtil client = webServiceMap.get(prop);
			this.nameSpace = client.nameSpace;
			this.wsdlUrl = client.wsdlUrl;
			this.serviceName = client.serviceName;
			this.portName = client.portName;
		}
		this.elementName = element;
		this.responseName = responseName;
	}

	public WebServiceUtil(String nameSpace, String wsdlUrl, String serviceName, String portName, String element,
			String responseName) {
		this.nameSpace = nameSpace;
		this.wsdlUrl = wsdlUrl;
		this.serviceName = serviceName;
		this.portName = portName;
		this.elementName = element;
		this.responseName = responseName;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param wsdlUrl
	 * @param serviceName
	 * @param portName
	 * @param elementName
	 * @param requestName
	 * @param responseName
	 * @param timeOut
	 *            毫秒
	 */

//	public WebServiceUtil(String nameSpace, String wsdlUrl, String serviceName, String portName, String element,
//			String responseName, int timeOut) {
//		this.nameSpace = nameSpace;
//		this.wsdlUrl = wsdlUrl;
//		this.serviceName = serviceName;
//		this.portName = portName;
//		this.elementName = element;
//		this.responseName = responseName;
//		this.timeout = timeOut;
//	}

	public String sendMessage(Object bean) throws Exception {
		HashMap<String, String> inMsg = new HashMap<String, String>();
		for (Field f : bean.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			inMsg.put(f.getName(), f.get(bean).toString());
		}
		return sendMessage(inMsg);
	}

	public String sendMessage(HashMap<String, String> inMsg) throws Exception {
		// 创建URL对象
		URL url = null;
		try {
			url = new URL(wsdlUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return "创建URL对象异常";
		}
		// 创建服务(Service)
		QName sname = new QName(nameSpace, serviceName);
		Service service = Service.create(url, sname);

		// 创建Dispatch对象
		Dispatch<SOAPMessage> dispatch = null;
		try {
			dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			return "创建Dispatch对象异常";
		}

		// 创建SOAPMessage
		try {
			SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
			msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

			// 创建SOAPHeader(不是必需)
			// SOAPHeader header = envelope.getHeader();
			// if (header == null)
			// header = envelope.addHeader();
			// QName hname = new QName(nameSpace, "username", "nn");
			// header.addHeaderElement(hname).setValue("huoyangege");

			// 创建SOAPBody
			SOAPBody body = envelope.getBody();
			QName ename = new QName(nameSpace, elementName, "q0");
			SOAPBodyElement ele = body.addBodyElement(ename);
			// 增加Body元素和值
			for (Map.Entry<String, String> entry : inMsg.entrySet()) {
				ele.addChildElement(new QName(nameSpace, entry.getKey())).setValue(entry.getValue());
			}

			// 超时设置
			//dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, timeout);
			//dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, timeout);

			// 通过Dispatch传递消息,会返回响应消息
			SOAPMessage response = dispatch.invoke(msg);

			// 响应消息处理,将响应的消息转换为doc对象
			Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
			String ret = doc.getElementsByTagName(responseName).item(0).getTextContent();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public void setResponseName(String responseName) {
		this.responseName = responseName;
	}
}
