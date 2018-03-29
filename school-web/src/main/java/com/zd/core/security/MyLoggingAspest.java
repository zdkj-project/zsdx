package com.zd.core.security;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zd.core.constant.Constant;
import com.zd.core.util.JsonBuilder;
import com.zd.school.plartform.system.model.SysOperateLog;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysOperateLogService;

@Component
@Aspect
public class MyLoggingAspest {
	private static Logger logger = Logger.getLogger(MyLoggingAspest.class);

	@Resource
	private SysOperateLogService logService; 
	
	@Pointcut("execution(* *..service..*.do*(..))")
	public void pointService() {
	}

	// @Pointcut("execution(* *..controller..*.do*(..))")
	// public void pointController(){}

	@Around("pointService()")
	public Object aroundMethod(ProceedingJoinPoint pjd) {
		Object result = null;
		String methodName = pjd.getSignature().getName();
		String targetName = pjd.getTarget().getClass().getName();  
		SysOperateLog operteLog = new SysOperateLog();
		operteLog.setUuid(null);
		try {
			HttpServletRequest request = this.getHttpServletRequest();
			
			String ip = this.getIpAddress(request);
			String userName = "未知用户"; // 当接口不需要登录时，就直接使用这个名称。
			String userId = "";
			String methodParams = "";
			
			Object[] args=pjd.getArgs();
			JsonBuilder jsonBuilder=JsonBuilder.getInstance();
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					methodParams += jsonBuilder.toJson(args[i]) + ";";
				}
			}	
					
			SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.SESSION_SYS_USER);
			if (sysUser != null) {
				userName = sysUser.getUserName();
				userId = sysUser.getUuid();
			}
			
			operteLog.setUserId(userId);
			operteLog.setIpHost(ip);
			operteLog.setMethodName(targetName+"."+methodName);
			operteLog.setMethodParams(methodParams);
			operteLog.setOperateDate(new Date());
			// 前置通知
			logger.info("【请求开始】用户名：" + userName + "；用户ID：" + userId + "；用户IP：" + ip + "；方法名：" +(targetName+"."+ methodName) + "；参数："
					+ methodParams);

			result = pjd.proceed();
			
			//返回通知
			operteLog.setMethodResult(jsonBuilder.toJson(result));
			logger.info("【请求成功】返回值：" + operteLog.getMethodResult());
			
		} catch (Throwable e) {
			//异常通知
			logger.error("【请求失败】异常原因：" + e.getMessage());
		
			operteLog.setExceptionClass(e.getClass().getName());
			operteLog.setExceptionDetail(e.getMessage());
			
			logService.merge(operteLog);	//发生异常后，也存入数据库
			
			//发现错误后，要抛出运行时异常，让程序自动回滚，并让上层控制器自动捕获异常并返回数据给前端；
			throw new RuntimeException(e);		//抛出后，下面的代码不再执行。
		}
		// 后置通知
		logger.info("【请求结束】");

		logService.merge(operteLog);	//成功执行后，也存入数据库
		
		return result;
	}

	/**
	 * 获取request
	 * 
	 * @return
	 */
	protected HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取用户的ip
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
