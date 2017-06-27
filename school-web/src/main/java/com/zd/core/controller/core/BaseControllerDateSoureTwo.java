package com.zd.core.controller.core;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.zd.core.constant.Constant;
import com.zd.core.service.BaseService;
import com.zd.core.util.CustomDateEditor;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;

public class BaseControllerDateSoureTwo implements Constant{

    /** 日志输出对象 */
    // private static Logger logger =
    // LoggerFactory.getLogger(BaseController.class);


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
                    //writer.flush();
                    writer.close();
                    //response.flushBuffer();
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
    
    public SysUser getCurrentSysUser(HttpServletRequest request){
		return (SysUser) request.getSession().getAttribute(SESSION_SYS_USER);
	}
    
    protected  Long  getMaxId(BaseService<?> thisService,Class<?> thisClass){
    	System.out.println(thisClass.getSimpleName());
    	List<?> temp = thisService.doQuery(" select max(uuid) from "+thisClass.getSimpleName());
		return (temp == null?0L:Long.parseLong(temp.get(0).toString()))+1;
    }
}
