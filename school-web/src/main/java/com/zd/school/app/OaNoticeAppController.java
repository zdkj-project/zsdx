package com.zd.school.app;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticetype;
import com.zd.school.oa.notice.service.OaNoticeService;
import com.zd.school.oa.notice.service.OaNoticetypeService;

@Controller
@RequestMapping("/app/OaNotice")
public class OaNoticeAppController extends FrameWorkController<OaNotice> implements Constant {
	@Resource
	OaNoticeService thisService; // service层接口

	@Resource
	OaNoticetypeService ontService;

	/**
	 * 查询所有公告
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<OaNotice> list(@ModelAttribute OaNotice entity, HttpServletRequest request,
			HttpServletResponse response) {
		List<OaNotice> onList = thisService.getQuery(" from OaNotice where isDelete =0");
		return onList;
	}

	/**
	 * 删除方法
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dodelete")
	public @ResponseBody String doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		String str;
		if (StringUtils.isEmpty(delIds)) {
			str = "没有传入删除主键";
		} else {
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
			if (flag) {
				str = "删除成功";
			} else {
				str = "删除失败";
			}
		}
		return str;
	}

	/**
	 * 还原方法
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dorestore")
	public @ResponseBody String doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		String str;
		if (StringUtils.isEmpty(delIds)) {
			str = "没有传入删除主键";
		} else {
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
			if (flag) {
				str = "还原成功";
			} else {
				str = "还原失败";
			}
		}
		return str;
	}

	/**
	 * 单个对象添加或修改
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/doSaveOrUpdate")
	public void doSaveOrUpdate(OaNotice entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OaNotice temp = new OaNotice();
		String strData = "";
		// if (StringUtils.isEmpty(entity.getTypeId())) {
		// temp.setSuccess(false);
		// temp.setFailed("公告类型不能为空");
		//
		// strData = jsonBuilder.toJson(temp);
		// writeAppJSON(response, strData);// 返回数据
		// return;
		// }
		// OaNoticetype ont = ontService.get(entity.getTypeId());
		// if (ont == null || ont.getIsDelete() != 0) {
		// temp.setSuccess(false);
		// temp.setFailed("未找到该公告类型");
		// //return temp;
		// strData = jsonBuilder.toJson(temp);
		// writeAppJSON(response, strData);// 返回数据
		// return;
		// }
		// OaNotice tempON = thisService.get(entity.getUuid());
		// if (tempON == null) {
		// //entity.setSuccess(true);
		// //entity.setFailed("添加成功");
		// temp = thisService.merge(entity);
		// temp.setSuccess(true);
		// temp.setFailed("添加成功");
		// } else {
		// // tempON.setSuccess(true);
		// // tempON.setFailed("修改成功");
		// tempON.setTypeId(entity.getTypeId());
		// tempON.setNoticeTitle(entity.getNoticeTitle());
		// tempON.setNoticeBrief(entity.getNoticeBrief());
		// tempON.setNoticeContent(entity.getNoticeContent());
		// temp = thisService.merge(tempON);
		// temp.setSuccess(true);
		// temp.setFailed("添加成功");
		// }
		// return temp;
//		temp.setSuccess(true);
//		temp.setFailed("添加成功");
//		strData = jsonBuilder.toJson(temp);
		writeAppJSON(response, strData);// 返回数据
	}

}
