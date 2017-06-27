package com.zd.school.app.oa.notice;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.DateUtil;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.service.OaNoticeService;

@Controller
@RequestMapping("/app/notice")
public class NoticeAppController extends FrameWorkController<OaNotice> implements Constant {
	@Resource
	private OaNoticeService thisService; // service层接口

	/**
	 * app 获取通知公告列表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(String terminalNumb, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		// 提供给APP接口，需要根据时间进行过滤
		// 只提供生效日期<=当前日期并且中止日期>=当前日期
		String filter = "";
		String justDateStr = DateUtil.formatDate(new Date());
		filter = "[{'type':'date','comparison':'<=','value':'" + justDateStr + "','field':'beginDate'}";
		filter += ",{'type':'date','comparison':'>=','value':'" + justDateStr + "','field':'endDate'}]";
		QueryResult<OaNotice> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessAppJson("\"请求成功\"", strData));// 返回数据
	}
}
