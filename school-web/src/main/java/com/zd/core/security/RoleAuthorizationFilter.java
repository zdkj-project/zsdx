package com.zd.core.security;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.zd.core.util.ModelUtil;

public class RoleAuthorizationFilter extends AuthorizationFilter {  
	  
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {  
		  
        HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;  
  
        Subject subject = getSubject(request, response);  
           
        boolean ajax = "XMLHttpRequest".equals( httpRequest.getHeader("X-Requested-With") );
      
        
        if (subject.getPrincipal() == null) {  
            if (ajax==true) {  
               
            	writeJSON(httpResponse,"{ \"success\": false, \"obj\" :\"您尚未登录或登录时间过长,请重新登录!\" }");
               
            } else {  
                saveRequestAndRedirectToLogin(request, response);  
            }  
        } else {  
            if (ajax==true) {  
            	
            	writeJSON(httpResponse,"{ \"success\": false, \"obj\" :\"您没有足够的权限执行该操作!\" }");
                                    
            } else {  
                String unauthorizedUrl = getUnauthorizedUrl();  
                if (StringUtils.hasText(unauthorizedUrl)) {  
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);  
                } else {  
                    WebUtils.toHttp(response).sendError(401);  
                }  
            }  
        }  
        return false;  
    }  
	
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)  
            throws IOException {  
  
        Subject subject = getSubject(request, response);  
         
        //在这里判断，当前用户是否允许访问， 返回False表示不允许，则会调用onAccessDenied方法。
        if (subject.getPrincipal() == null) {                         
            return false;  
        }        
        
        String[] rolesArray = (String[]) mappedValue;  
  
        if (rolesArray == null || rolesArray.length == 0) {  
            // no roles specified, so nothing to check - allow access.  
            return true;  
        }  
  
        Set<String> roles = CollectionUtils.asSet(rolesArray);  
        for (String role : roles) {  
            if (subject.hasRole(role)) {  
                return true;  
            }  
        }  
        return false;  
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
  
}  