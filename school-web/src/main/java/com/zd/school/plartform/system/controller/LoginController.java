package com.zd.school.plartform.system.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.DateUtil;
import com.zd.core.util.ModelUtil;
import com.zd.school.plartform.baseset.service.DictionaryItemCache;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserLoginLog;
import com.zd.school.plartform.system.service.SysUserLoginLogService;
import com.zd.school.plartform.system.service.SysUserService;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/login")
public class LoginController extends FrameWorkController<SysUser> implements Constant {

	private static final Logger logger = LoggerFactory.getLogger(SysUser.class);
	@Resource
	private SysUserService sysUserService;

	@Resource
	private SysUserLoginLogService sysUserLoginLogService;

	@Resource
	private SessionDAO sessionDAO;

	@RequestMapping("/login")
	public void login(SysUser sysUserModel, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		SysUser sysUser = sysUserService.getByProerties("userName", sysUserModel.getUserName());
		// if (sysUser == null || "1".equals(sysUser.getState())) { //
		// 用户名有误或已被禁用
		if (sysUser == null) { // 用户名有误,根据编号进行查询
			sysUser = sysUserService.getByProerties("userNumb", sysUserModel.getUserName());
			if (sysUser == null || "1".equals(sysUser.getState())) {// 根据编号也未查询到
				result.put("result", -1);
				writeJSON(response, jsonBuilder.toJson(result));
				return;
			}
		}

		String pwd = Base64.decodeToString(sysUserModel.getUserPwd());

		if (!sysUser.getUserPwd().equals(new Sha256Hash(pwd).toHex())) { // 密码错误
			result.put("result", -2);
			writeJSON(response, jsonBuilder.toJson(result));
			return;
		}
		sysUser.setLoginTime(new Date());
		sysUserService.merge(sysUser);
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		// SecurityUtils.getSubject().getSession().setTimeout(-1000l); //永不过期
		session.setTimeout(1000 * 60 * 30 * 8);   //超时时间为4小时
		// login失败，要捕获相应异常
		try {
			// 执行login之后，会立即执行Realm的getAuthenticationInfo方法，用来判断token信息是否正确。
			subject.login(new UsernamePasswordToken(sysUser.getUserName(), pwd, sysUserModel.isRememberMe()));

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
			/*
			 * 不处理重复登录 else{ String hql=
			 * "from SysUserLoginLog o where o.userId=? and o.sessionId=? and o.isDelete=0 order by createTime desc"
			 * ; SysUserLoginLog loginLog =
			 * sysUserLoginLogService.getForValue(hql, userId,sessionId); }
			 */
		} catch (AuthenticationException e) { // 这里只捕获了AuthenticationException这个超类，其他更详细的异常子类，暂时不处理
			result.put("result", -2);
			writeJSON(response, jsonBuilder.toJson(result));
			return;
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

		session.setAttribute(SESSION_SYS_USER, sysUser);
		session.setAttribute("ROLE_KEY", sysUser.getSysRoles().iterator().next().getRoleCode());
		
		session.removeAttribute("accessToken");	//非单点登录，清除它
		session.removeAttribute("accessAccount");	//非单点登录，清除它
		
		result.put("result", 1);
		writeJSON(response, jsonBuilder.toJson(result));
	}

	@RequestMapping("/getCurrentUser")
	public void getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser sysUser = getCurrentSysUser();
		if (sysUser != null) {
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(sysUser)));
		} else
			writeJSON(response, jsonBuilder.returnFailureJson("'没有得到登录用户'"));
	}

	@RequestMapping("/desktop")
	public ModelAndView desktop(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		if (session.getAttribute(SESSION_SYS_USER) == null) {
			return new ModelAndView("login");
		} else {
			SysUser sysUser = (SysUser) session.getAttribute(SESSION_SYS_USER);
			String globalRoleKey = sysUser.getSysRoles().iterator().next().getRoleCode();
			try {
				// List<Authority> allMenuList =
				// authorityService.queryAllMenuList(globalRoleKey);
				return new ModelAndView("redirect:/index.jsp", "authorityList", null);
			} catch (Exception e) {
				logger.error(e.toString());
				return new ModelAndView("login");
			}
		}
	}
	
	@RequestMapping("/ssoIndex")
    public ModelAndView ssoIndex(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	//System.out.println("进入主界面！");
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
    	//System.out.println("session内容："+session.getAttribute(SESSION_SYS_USER));
    	
        if (session.getAttribute(SESSION_SYS_USER) == null) {
            return new ModelAndView("login");
        } else {
            SysUser sysUser = (SysUser) session.getAttribute(SESSION_SYS_USER);
            String globalRoleKey = sysUser.getSysRoles().iterator().next().getRoleCode();
            try {
            	//System.out.println("进入到index页面!");
                // List<Authority> allMenuList =
                // authorityService.queryAllMenuList(globalRoleKey);
                return new ModelAndView("redirect:/index.jsp", "authorityList", null);
            } catch (Exception e) {
                logger.error(e.toString());
                //System.out.println("进入到index错误页面!");
                return new ModelAndView("login");
            }
        }
    }
	

	@RequestMapping("/changepwd")
	public void changePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("oldPwd");
		String newUserPwd = request.getParameter("newPwd");
		String[] propName = { "userName", "userPwd" };
		String[] propValue = { userName, new Sha256Hash(userPwd).toHex() };

		SysUser sysUser = sysUserService.getByProerties(propName, propValue);
		if (ModelUtil.isNotNull(sysUser)) {
			// 更新到数据库
			sysUserService.updateByProperties(propName, propValue, "userPwd", new Sha256Hash(newUserPwd).toHex());
			// 返回处理结果
			writeJSON(response, jsonBuilder.returnSuccessJson("1"));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("-1"));
		}
	}
	/*
	 * @RequestMapping("/loginout") public void logout(HttpServletRequest
	 * request, HttpServletResponse response) throws IOException { //因为配置拦截中加入了
	 * /login/logout = logout ；所以不需要手动去执行logout
	 * //SecurityUtils.getSubject().logout();
	 * response.sendRedirect("login.jsp"); }
	 */
	@RequestMapping("/loginout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Properties pros = PropertiesLoaderUtils.loadAllProperties("sso.properties");
		
		HttpSession session = request.getSession();
		
		//清除shiro的登录状态
		SecurityUtils.getSubject().logout();
		
		 //判断是否进行了单点登录 	
		String accessTokenSession = (String) session.getAttribute("accessToken");
    	if(StringUtils.isNotEmpty(accessTokenSession)){
    		
    		//传accessTokenSession是为了清除认证平台的token；传clientId是为了方便退出成功后可以跳转到所对应客户端的登陆页面。
    		String ssoServerUrl=pros.getProperty("ssoService");
    		if(!ssoServerUrl.endsWith("/")){
    		    ssoServerUrl=ssoServerUrl+"/";
    		}
    		String clientId = pros.getProperty("clientId");
    		String serverDelUrl=ssoServerUrl+"oauth/login_out?access_token="+accessTokenSession+"&client_id="+clientId;
    		
    	    /*清除子系统自己的session 开始*/
    	    session.removeAttribute("accessToken");
    		session.removeAttribute("accessAccount");		
    		session.invalidate();
    		response.sendRedirect(serverDelUrl);
    		
    	}else{
    		//否则，进入自己的登录界面
    		response.sendRedirect("login.jsp");
    	}
    	
    }
	

	@RequestMapping("/getOnlineCount")
	public void getOnlineCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int count = sessionDAO.getActiveSessions().size();
		/*测试使用redis后，执行存储过程是否变缓慢
		StringBuffer sql = new StringBuffer("EXEC [dbo].[test_sql] ");
		List<Map<String, Object>> lists=sysUserService.getForValuesToSql(sql.toString());
		System.out.println(lists);
		*/
		writeJSON(response, jsonBuilder.returnSuccessJson(String.valueOf(count)));
	}

	/**
	 * 清除数据字典缓存
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/clearCache")
	public void clearCache(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DictionaryItemCache.clearAll();
		writeJSON(response, jsonBuilder.returnSuccessJson("'缓存清除成功'"));
	}


	
}
