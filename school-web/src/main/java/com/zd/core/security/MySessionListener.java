package com.zd.core.security;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserLoginLog;
import com.zd.school.plartform.system.service.SysUserLoginLogService;

public class MySessionListener implements SessionListener { 
	
	@Resource
	private SysUserLoginLogService sysUserLoginLogService;
	    
	
    @Override  
    public void onStart(Session session) {//会话创建时触发  
        //System.out.println("会话创建：" + session.getId());  
    }  
    @Override  
    public void onExpiration(Session session) {//会话过期时触发 
    	//System.out.println("会话过期：" + session.getId());  
    	 
        SysUser sysuser = (SysUser) session.getAttribute("SESSION_SYS_USER");
        // session.getAttribute("kickout"));  
        
        String userId=sysuser.getUuid();
        String sessionId=(String) session.getId();
     
        String hql="from SysUserLoginLog o where o.userId=? and o.sessionId=? and o.isDelete=0 order by createTime desc";
    	SysUserLoginLog loginLog = sysUserLoginLogService.getForValue(hql, userId,sessionId);	
    	if(loginLog!=null){
    		loginLog.setLastAccessDate(session.getLastAccessTime());
    		loginLog.setOfflineDate(new Date());
    		loginLog.setOfflineIntro("超时退出");
    		sysUserLoginLogService.merge(loginLog);
    	}    	  
    }  
    @Override  
    public void onStop(Session session) {//退出/会话过期时触发    
        //System.out.println("会话停止：" + session.getId());  
             
        SysUser sysuser = (SysUser) session.getAttribute("SESSION_SYS_USER");
              
        String userId=sysuser.getUuid();
        String sessionId=(String) session.getId();
     
        String hql="from SysUserLoginLog o where o.userId=? and o.sessionId=? and o.isDelete=0 order by createTime desc";
    	SysUserLoginLog loginLog = sysUserLoginLogService.getForValue(hql, userId,sessionId);	
    	if(loginLog!=null){
    		loginLog.setLastAccessDate(session.getLastAccessTime());
    		loginLog.setOfflineDate(new Date());
    		
    		if(session.getAttribute("kickout")!=null)
    			loginLog.setOfflineIntro("异地登录退出");
    		else 
    			loginLog.setOfflineIntro("手动退出");

    		sysUserLoginLogService.merge(loginLog);
    	}   
    }    
}  