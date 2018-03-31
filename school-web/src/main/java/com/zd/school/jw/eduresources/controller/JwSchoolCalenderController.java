package com.zd.school.jw.eduresources.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.eduresources.model.JwCalender;
import com.zd.school.jw.eduresources.model.JwSchoolCalender;
import com.zd.school.jw.eduresources.service.JwSchoolCalenderService;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/JwSchoolCalender")
public class JwSchoolCalenderController extends FrameWorkController<JwSchoolCalender> implements Constant {

	@Resource
	JwSchoolCalenderService thisService; // service层接口

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<JwSchoolCalender> list(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<JwSchoolCalender> lists = thisService.getQueryAll();// 执行查询方法
		return lists;
	}

	@RequestMapping("/doadd")
	public void doAdd(@ModelAttribute JwSchoolCalender calForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		calForm.setId(calForm.getUuid());

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(calForm);
		calForm.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		calForm.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		calForm = thisService.doMerge(calForm);

		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(calForm)));
	}

	@RequestMapping("/doupdate")
	public void doUpdate(@ModelAttribute JwSchoolCalender calForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		// calForm.setUuid(calForm.getId());

		JwSchoolCalender entity = thisService.get(calForm.getId());
		BeanUtils.copyPropertiesExceptNull(entity, calForm);

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 修改时要设置修改人
		entity.setUpdateUser(userCh); // 修改人
		entity.setUpdateTime(new Date());
		entity.setUuid(entity.getId());
		// 更新到数据库
		thisService.doUpdate(entity);

		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	@RequestMapping("/dodelete")
	public void doDelete(@ModelAttribute JwSchoolCalender calForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
		String id = request.getParameter("id");
		boolean flag = thisService.doDeleteByPK(id);
		if (flag) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
		}
	}

}
