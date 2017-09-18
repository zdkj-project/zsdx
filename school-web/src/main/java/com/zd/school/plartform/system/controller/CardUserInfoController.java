package com.zd.school.plartform.system.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.school.plartform.system.model.CardUserInfo;
import com.zd.school.plartform.system.service.CardUserInfoService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * ClassName: BaseTRoleController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Controller. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/CardUserInfo")
public class CardUserInfoController extends FrameWorkController<CardUserInfo> implements Constant {
	
	 @Resource
	 CardUserInfoService thisService; // service层接口

	    /**
	      * @Title: list
	      * @Description: 查询数据列表
	      * @param entity 实体类
	      * @param request
	      * @param response
	      * @throws IOException    设定参数
	      * @return void    返回类型
	     */
	    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
	            org.springframework.web.bind.annotation.RequestMethod.POST })
	    public void list(@ModelAttribute CardUserInfo entity, HttpServletRequest request, HttpServletResponse response)
	            throws IOException {
	        String strData = ""; // 返回给js的数据
			Integer start = super.start(request);
			Integer limit = super.limit(request);
			String sort = super.sort(request);
			String filter = super.filter(request);
	        QueryResult<CardUserInfo> qResult = thisService.list(start, limit, sort, filter,true);
	        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
	        writeJSON(response, strData);// 返回数据
	    }
	
	
	
	
	
	
	
}
