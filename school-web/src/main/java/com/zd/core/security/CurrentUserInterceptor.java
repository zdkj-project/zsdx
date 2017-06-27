package com.zd.core.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

@Component
public class CurrentUserInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private SysUserService sysUserService;

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) throws Exception {
        // Add the current user into the request
        final String currentUserId = (String) SecurityUtils.getSubject().getPrincipal();
        SysUser currentUser = sysUserService.get(currentUserId);
        if (currentUser != null) {
            httpServletRequest.setAttribute("currentUser", currentUser);
        }
    }
}
