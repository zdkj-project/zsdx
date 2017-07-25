 package com.zd.core.security;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.zd.core.constant.Constant;
import com.zd.core.util.DateUtil;
import com.zd.core.util.ModelUtil;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserLoginLog;
import com.zd.school.plartform.system.service.SysUserLoginLogService;
import com.zd.school.plartform.system.service.SysUserService;

public class AjaxRequestAuthorizationFilter extends PassThruAuthenticationFilter {
	// TODO - complete JavaDoc
	@Resource
    private SysUserService sysUserService;
	@Resource
	private SysUserLoginLogService sysUserLoginLogService;
	
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
		Session session = subject.getSession();	
		
		//System.out.println("开始认证状态："+subject.isAuthenticated());
		//System.out.println("shiro：sessionId"+session.getId());
		
		//当shiro没有登录的时候，判断sso是否登录了
		if(subject.isAuthenticated()==false){	
			
			//判断单点登录处，是否存在账户信息,若登录了，就手动进行shiro登录			
			String accessAccountSession=null;
			if(session!=null){
				accessAccountSession = (String) session.getAttribute("accessAccount");
			}
						
			if(accessAccountSession!=null){
				try {
					//System.out.println("单点登录用户名："+accessAccountSession);
				
					SysUser sysUser = sysUserService.getByProerties("userName", accessAccountSession);
			        if (sysUser == null || "1".equals(sysUser.getState())) { // 用户名有误或已被禁用		        
						//WebUtils.issueRedirect(request, response, "/noAuth.jsp");
			        	
						String accessTokenSession = (String) session.getAttribute("accessToken");
						Properties pros = PropertiesLoaderUtils.loadAllProperties("sso.properties");
						String ssoServerUrl=pros.getProperty("ssoService");
			    		if(!ssoServerUrl.endsWith("/")){
			    		    ssoServerUrl=ssoServerUrl+"/";
			    		}
			    		String clientId = pros.getProperty("clientId");
			    		
						String returnUrl = ssoServerUrl + "oauth/login_out?access_token=" + accessTokenSession + "&client_id="
								+ clientId;
						
						WebUtils.issueRedirect(request, response, returnUrl);
						
						return true;				
			        }
			        
			        //System.out.println("模拟登录："+sysUser);
			        
			        //模拟登录
			        sysUser.setLoginTime(new Date());
			        sysUserService.merge(sysUser);
			        			    		
			    	session.setTimeout(1000 * 60 * 30 * 8);
			        
			        //特别声明：由于单点登录只返回用户名，而没有明文密码，所以在这里统一设定为123456
			        //（若直接使用sysUser.getUserPwd()会报错，因为它会自动进行加密转换，可能是realm中处理的）
			        subject.login(new UsernamePasswordToken(sysUser.getUserName(),"123456",
			        		sysUser.isRememberMe()));	
			        
			        // 判断 用户ID和会话ID是否已经存在数据库中
					String userId = sysUser.getUuid();
					String sessionId = (String) session.getId();

					// 先判断此sessionID是否已经存在，若存在且userid不等于当前的，且没有登记退出时间，则设置为退出
					String updateTime = DateUtil.formatDateTime(new Date());		
					String updateHql = "update SysUserLoginLog o set o.offlineDate=CONVERT(datetime,'" + updateTime
							+ "'),o.offlineIntro='切换账户退出' where o.offlineDate is null and o.isDelete=0 and o.sessionId='"
							+ sessionId + "' and o.userId!='"+userId+"'";
					sysUserLoginLogService.executeHql(updateHql);
					
					if (!sysUserLoginLogService.IsFieldExist("userId", userId, "-1", " o.sessionId='" + sessionId + "'")) {
						SysUserLoginLog loginLog = new SysUserLoginLog();
						loginLog.setUserId(userId);
						loginLog.setSessionId(sessionId);
						loginLog.setUserName(sysUser.getUserName());
						loginLog.setIpHost(session.getHost());
						loginLog.setLoginDate(session.getLastAccessTime());
						loginLog.setLastAccessDate(session.getLastAccessTime());
						sysUserLoginLogService.merge(loginLog);
					}
					
					Calendar a = Calendar.getInstance();
					Integer studyYear = a.get(Calendar.YEAR);
					Integer studyMonty = a.get(Calendar.MONTH) + 1;
					String semester = "";
					if (studyMonty >= 8) {
						semester = "2";
					} else {
						semester = "1";
					}
					Integer i = studyYear + 1;
					String studyYeahname = studyYear.toString() + "-" + i.toString() + "学年";
					sysUser.setStudyYear(studyYear);
					sysUser.setSemester(semester);
					sysUser.setStudyYearname(studyYeahname);
					
			        //System.out.println("模拟登录成功！");			        
					session.setAttribute(Constant.SESSION_SYS_USER, sysUser);
					//session.setAttribute("ROLE_KEY", sysUser.getSysRoles().iterator().next().getRoleCode());	
			        StringBuilder sb = new StringBuilder();
			        for (SysRole s:sysUser.getSysRoles()) {
			            sb.append(s.getRoleCode());
			            sb.append(",");
			        }
			        if(sb.length()>0)
			            sb = sb.deleteCharAt(sb.length()-1);
					session.setAttribute("ROLE_KEY", sb.toString());
					
			        //System.out.println("模拟登录完毕！");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}		
		}
		
		/*session的这个user实体的uuid总是变化，原因不明，所以强行在这里设置回去*/	
		if(session!=null){
			SysUser sysuser = (SysUser) session.getAttribute(Constant.SESSION_SYS_USER);
			if (sysuser != null && !sysuser.getUuid().equals(subject.getPrincipal().toString())) {
				sysuser.setUuid(subject.getPrincipal().toString());
			}
		}	
		
		//System.out.println("结束认证状态："+subject.isAuthenticated());
		
		return subject.isAuthenticated();
		
	}
	
}
