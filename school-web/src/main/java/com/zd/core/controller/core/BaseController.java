/**
 * Project Name:school-web
 * File Name:BaseController.java
 * Package Name:com.zd.core.controller.core
 * Date:2016年6月1日下午2:06:15
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.controller.core;

import com.zd.core.model.BaseEntity;
import com.zd.core.service.BaseService;
import com.zd.core.util.CustomDateEditor;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

/**
 * ClassName:BaseController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年6月1日 下午2:06:15
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public abstract class BaseController<E extends BaseEntity> {
	/** 日志输出对象 */
	// private static Logger logger =
	// LoggerFactory.getLogger(BaseController.class);

	protected BaseService<E> service;

	/** Json工具类 */
	protected static JsonBuilder jsonBuilder;

	static {
		jsonBuilder = JsonBuilder.getInstance();
	}

	protected void writeJSON(HttpServletResponse response, String contents) throws IOException {
		if (ModelUtil.isNotNull(response)) {
			response.setContentType("text/html;charset=UTF-8;");
			Writer writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
					response.flushBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected void writeAppJSON(HttpServletResponse response, String contents) throws IOException {
		if (ModelUtil.isNotNull(response)) {
			response.setContentType("application/json;charset=UTF-8;");
			response.setHeader("Cache-Control", "no-cache");
			Writer writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					// writer.flush();
					writer.close();
					// response.flushBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor());
	}

	protected String sort(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getParameter("sort"))) {
			return request.getParameter("sort");
		} else
			return "";
	}

	protected Integer start(HttpServletRequest request) {
		return Integer.parseInt(request.getParameter("start"));
	}

	protected Integer limit(HttpServletRequest request) {
		return Integer.parseInt(request.getParameter("limit"));
	}

	protected String filter(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getParameter("filter"))) {
			return request.getParameter("filter");
		} else
			return "";
	}

	protected String whereSql(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getParameter("whereSql"))) {
			return request.getParameter("whereSql");
		} else
			return "";
	}

	protected String querySql(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getParameter("querySql"))) {
			return request.getParameter("querySql");
		} else
			return "";
	}

	protected String orderSql(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getParameter("orderSql"))) {
			return request.getParameter("orderSql");
		} else
			return "";
	}

	protected String excludes(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(request.getParameter("excludes"))) {
            return request.getParameter("excludes");
        } else
            return "";
    }
    protected String group(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(request.getParameter("group"))) {
            return request.getParameter("group");
        } else
            return "";
    }
}
