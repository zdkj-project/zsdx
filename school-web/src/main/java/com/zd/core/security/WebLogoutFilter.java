package com.zd.core.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

/**
 * 注销后，专门返回到Web登录界面
 * @author Administrator
 *
 */
public class WebLogoutFilter  extends LogoutFilter {
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		// 在这里执行退出系统前需要清空的数据
		Subject subject = getSubject(request, response);		
		String redirectUrl = getRedirectUrl(request, response, subject);	
		try {		
			
			Session session = subject.getSession();		
						
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