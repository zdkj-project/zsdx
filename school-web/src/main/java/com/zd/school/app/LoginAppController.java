package com.zd.school.app;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

@Controller
@RequestMapping("/app/login/")
public class LoginAppController {
	@Resource
	private SysUserService sysUserService;

	@RequestMapping(value = { "/login" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody Map<String, Object> login(@RequestParam("userName") String userName,
			@RequestParam("passWord") String passWord) {

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			SysUser sysUser = sysUserService.getByProerties("userName", userName);

			if (sysUser == null) { // 用户名有误,根据编号进行查询
				sysUser = sysUserService.getByProerties("userNumb", userName);
				if (sysUser == null || "1".equals(sysUser.getState())) {// 根据编号也未查询到
					result.put("success", false);
					result.put("msg", "用户名或密码有误！");
				}
			} else if (!sysUser.getUserPwd().equals(new Sha256Hash(passWord).toHex())) { // 密码错误
				result.put("success", false);
				result.put("msg", "用户名或密码有误！");
			} else {
				result.put("success", true);
				result.put("msg", "登录成功！");
			}
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "请重试或联系管理员！");
		}
		return result;
	}

}