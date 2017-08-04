package com.zd.school.oa.flow.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/MyMessage")
public class MyMessageController extends FrameWorkController<PushInfo> implements Constant {

	@Resource
	private PushInfoService pushInfoService;

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute PushInfo entity,HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser currentUser = getCurrentSysUser();
		String userNum = currentUser.getUserNumb();
		String hql="from PushInfo where emplNo='"+userNum+"' and eventType='消息提醒'";
		
		String countHql="select count(*) from PushInfo where emplNo='"+userNum+"' and eventType='消息提醒'";
		List<PushInfo> list = pushInfoService.doQuery(hql.toString(), super.start(request), entity.getLimit());
		Integer count = pushInfoService.getCount(countHql.toString());// 查询总记录数
		String strData = jsonBuilder.buildObjListToJson(new Long(count), list, true);// 处理数据
        writeJSON(response, strData);// 返回数据
	}

}
