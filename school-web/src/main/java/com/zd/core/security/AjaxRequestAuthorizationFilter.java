package com.zd.core.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.zd.core.constant.Constant;
import com.zd.core.util.ModelUtil;
import com.zd.school.plartform.system.model.SysUser;

public class AjaxRequestAuthorizationFilter extends PassThruAuthenticationFilter {
	// TODO - complete JavaDoc

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			return true;
		} else {

			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			boolean ajax = "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));

			if (ajax == true) {
				//Subject subject = getSubject(request, response);
				//subject.logout(); 未完成：应该不需要
				writeJSON(httpResponse, "{ \"success\": false, \"obj\" :\"您已登录超时,请重新登录!\" }");

			} else {
				saveRequestAndRedirectToLogin(request, response);
			}

			return false;
		}
	}

	protected void writeJSON(HttpServletResponse response, String contents) throws IOException {
		if (ModelUtil.isNotNull(response)) {
			response.setContentType("text/html;charset=UTF-8;");
			Writer writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
					response.flushBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = getSubject(request, response);
		
		/*session的这个user实体的uuid总是变化，原因不明，所以强行在这里设置回去*/
		HttpSession session = WebUtils.toHttp(request).getSession(false);
		if(session!=null){
			SysUser sysuser = (SysUser) session.getAttribute(Constant.SESSION_SYS_USER);
			if (sysuser != null && !sysuser.getUuid().equals(subject.getPrincipal().toString())) {
				sysuser.setUuid(subject.getPrincipal().toString());
			}
		}	
		return subject.isAuthenticated();
	}
	
}
