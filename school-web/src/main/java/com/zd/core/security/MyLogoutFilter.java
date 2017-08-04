package com.zd.core.security;

import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 最新修改：退出，一律返回单点登录界面
 * @author Administrator
 *
 */
public class MyLogoutFilter extends LogoutFilter {
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		// 在这里执行退出系统前需要清空的数据
		Subject subject = getSubject(request, response);		
		String redirectUrl = getRedirectUrl(request, response, subject);	
		try {		
			Properties pros = PropertiesLoaderUtils.loadAllProperties("sso.properties");		
			Session session = subject.getSession();		
				
			//判断是否进行了单点登录，更换响应的地址	
			String accessTokenSession = (String) session.getAttribute("accessToken");
							
	    	//if(StringUtils.isNotEmpty(accessTokenSession)){	    		
	    		//传accessTokenSession是为了清除认证平台的token；传clientId是为了方便退出成功后可以跳转到所对应客户端的登陆页面。
	    		String ssoServerUrl=pros.getProperty("ssoService");
	    		if(!ssoServerUrl.endsWith("/")){
	    		    ssoServerUrl=ssoServerUrl+"/";
	    		}
	    		String clientId = pros.getProperty("clientId");
	    		String serverDelUrl=ssoServerUrl+"oauth/login_out?access_token="+accessTokenSession+"&client_id="+clientId;
	    		    
	    		//System.out.println("loginout：ssoUrl:"+serverDelUrl);
	    		
	    		redirectUrl=serverDelUrl;	    		    	
	    	//}   
	    	
	    	session.removeAttribute("accessToken");	//清除单点登录数据
			session.removeAttribute("accessAccount");	//清除单点登录数据
			
			subject.logout();
			
		} catch (SessionException ise) {

			ise.printStackTrace();

		}
		issueRedirect(request, response, redirectUrl);
		// 返回false表示不执行后续的过滤器，直接返回跳转到登录页面
		return false;

	}
}