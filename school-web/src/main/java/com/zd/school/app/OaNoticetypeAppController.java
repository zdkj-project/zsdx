package com.zd.school.app;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.notice.model.OaNoticetype;
import com.zd.school.oa.notice.service.OaNoticetypeService;
@Controller
@RequestMapping("/app/OaNoticetype")
public class OaNoticetypeAppController  extends FrameWorkController<OaNoticetype> implements Constant{
	
	@Resource
	OaNoticetypeService thisService;
	
	/**
	 * 单个对象添加或修改
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/doSaveOrUpdate")
	public @ResponseBody OaNoticetype doSaveOrUpdate(OaNoticetype entity,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OaNoticetype tempON = thisService.get(entity.getUuid());
		if(tempON==null){
			entity = thisService.doMerge(entity);
			entity.setSuccess(true);
			entity.setFailed("添加成功");
		}else{
			tempON.setTypeName(entity.getTypeName());
			entity = thisService.doMerge(tempON);
			entity.setSuccess(true);
			entity.setFailed("修改成功");
		}
		return entity;
	}
	/**
	 * 删除方法
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dodelete")
	public @ResponseBody String doDelete(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dorestore")
	public @ResponseBody String doRestore(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
	
	
}
