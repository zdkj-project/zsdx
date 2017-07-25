package com.zd.core.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * sso过滤器
 * 
 * @author hxk
 *
 */
public class SSOFilter implements Filter {

	private static final String NO_CHECK = "noCheck";
	private List<String> noCheckList = new ArrayList<String>();
	private String ssoService = "";
	private String clientId = "";

	public void init(FilterConfig init) throws ServletException {

		ssoService = init.getInitParameter("ssoService");
		if (!ssoService.endsWith("/")) {
			ssoService = ssoService + "/";
		}
		clientId = init.getInitParameter("clientId");

		String noChecks = init.getInitParameter(NO_CHECK);
		if (StringUtils.isNotBlank(noChecks)) {
			if (StringUtils.indexOf(noChecks, ",") != -1) {
				for (String no : noChecks.split(",")) {
					noCheckList.add(StringUtils.trimToEmpty(no));
				}
			} else {
				noCheckList.add(noChecks);
			}
		}
	}

	private boolean check(String path) {
		if (noCheckList == null || noCheckList.size() <= 0)
			return false;
		for (String s : noCheckList) {
			if (path.indexOf(s) > -1) {
				return true;
			}
		}
		return false;
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain filter)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String contextpath = request.getContextPath();
		if ("/".equals(contextpath)) {
			contextpath = "";
		}

		if (check(request.getRequestURI())) {// 白名单直接放行
			filter.doFilter(request, response);
		} else {
			try {
				checkLogin(request, response, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void checkLogin(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
			throws Exception {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		
		HttpSession httpSession = request.getSession();
		
		String accessTokenSession = (String) httpSession.getAttribute("accessToken");
		String accessAccountSession = (String) httpSession.getAttribute("accessAccount");
		String accessTokenRequest = (String) request.getParameter("access_token");
				
		// 查看当前子系统session中是否有token和account
		if (StringUtils.isNotEmpty(accessTokenSession) && StringUtils.isNotEmpty(accessAccountSession)) {
			if (StringUtils.isNotEmpty(accessTokenRequest)) {// 这里检验request里面的token主要是解决从其他子系统跳回当前系统，应该要刷新session为最新的账号
				if (accessTokenSession.equals(accessTokenRequest)) {// 如果token相等，则表示子系统跳转过来的和当前的session账号是相同的
					response.sendRedirect(basePath);
					return;
				} else {
					boolean isValidateToken = validateToken(accessTokenRequest);// 验证传过来的token是否有效，以免被恶意传值
					if (isValidateToken) {// token有效，则清除目前子系统的session，然后通过新token重新去认证平台获取新的账号
						httpSession.removeAttribute("accessToken");
						httpSession.removeAttribute("accessAccount");
						//httpSession.removeAttribute(Globals.USER_SESSION);
						response.sendRedirect(basePath + "?accessToken=" + accessTokenRequest);
						return;
					} else {// token无效则重新刷新子系统
						response.sendRedirect(basePath);
						return;
					}
				}
			} 
			/*这一步在shiro中验证
			 * else {
				boolean isExistUser = checkUser(accessAccountSession, request);
				if (isExistUser) {
					filter.doFilter(request, response);
				} else {// 如果在当前子系统发现没有该用户（理论上子系统的组织架构和认证平台的应该是同步的），则跳回到认证平台去退出登录。如果不跳回去退出登录，而是直接跳回去登录，则认证平台会认为这个token有效，然后又把这个token传回来，导致死循环
					String returnUrl = ssoService + "oauth/login_out?access_token=" + accessTokenSession + "&client_id="
							+ clientId;
					response.sendRedirect(returnUrl);
					return;
				}
			}*/
		} else {
			if (StringUtils.isNotEmpty(accessTokenRequest)) {
				//System.out.println("filter：已登录： "+accessTokenRequest);
				
				// 表示从认证平台有传递accessToken过来。则需要将accessToken提交到认证平台做下验证并获取该token对应的账号。
				/* 提交到认证平台进行验证开始 */
				String accessAccountRequest = getAccountFromSSO(accessTokenRequest);
				
				//System.out.println("filter：账户名称： "+accessAccountRequest);
				
				/* 提交到认证平台进行验证结束 */
				if (StringUtils.isNotEmpty(accessAccountRequest)) {// account有效,则需要往session插入数据，同时往cookies插入
					httpSession.setAttribute("accessToken", accessTokenRequest);
					httpSession.setAttribute("accessAccount", accessAccountRequest);
					
					//System.out.println("filter：session中的账户名称： "+httpSession.getAttribute("accessAccount"));
					
					//System.out.println("filter：返回路径： "+basePath);
					
					response.sendRedirect(basePath);
					return;
				} else {// 验证不通过则跳回到验证平台重新申请token
					String returnUrl = ssoService + "sso/tologin" + "?client_id=" + clientId;
					response.sendRedirect(returnUrl);
					return;
				}
			}
			// 如果session和request中都没有token，再查找认证平台的cookie中是否有token，直接提交到认证平台的toLogin方法，会自动去验证认证平台是否有cookie，如果有会再次将token当做request参数传回给子系统。
			String returnUrl = ssoService + "sso/tologin" + "?client_id=" + clientId;
			response.sendRedirect(returnUrl);
			return;
		}
	}

	/**
	 * 通过token去认证平台认证token是否有效
	 * 
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return false;
		} else {
			String url = ssoService + "resource.do?validateToken&access_token=" + token;
			try {
				JSONObject jsonObject = JSONObject.fromObject(getJsonString(url));
				boolean isCheck = jsonObject.getBoolean("success");
				if (isCheck) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 通过token去认证平台获取对应的账号
	 * 
	 * @param token
	 * @return
	 */
	public String getAccountFromSSO(String token) {
		String account = "";
		if (StringUtils.isEmpty(token)) {
			account = "";
		} else {
			String url = ssoService + "resource/tokeninfo?access_token=" + token + "&client_id=" + clientId;
			try {
				JSONObject jsonObject = JSONObject.fromObject(getJsonString(url));
				boolean isCheck = jsonObject.getBoolean("success");
				Map<String, Object> m = jsonObject.getJSONObject("attributes");
				if (isCheck && m.get("account") != null) {
					account = m.get("account").toString();
				} else {
					account = "";
				}
			} catch (Exception e) {
				account = "";
			}
		}
		return account;
	}

	protected String getJsonString(String urlPath) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		// 对应的字符编码转换
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str);
		}
		reader.close();
		connection.disconnect();
		return sb.toString();
	}

	
	public void destroy() {

	}

}