package com.zd.school.jw.train.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.model.PushInfoHistory;
import com.zd.school.push.service.PushInfoHistoryService;
import com.zd.school.push.service.PushInfoService;

@Controller
@RequestMapping("/PushInfo")
public class PushInfoController extends FrameWorkController<PushInfo> implements Constant {

    @Resource
    PushInfoService thisService; // service层接口
    
    @Resource
    PushInfoHistoryService pushInfoHistoryService; // ser
    
    /**
     * @param entity   实体类
     * @param request
     * @param response
     * @return void 返回类型
     * @throws IOException 设定参数
     * @Title: list
     * @Description: 查询数据列表
     */
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void list(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        String infoType=request.getParameter("infoType");
        
        SysUser currentUser=getCurrentSysUser();
        String roleKey=(String) request.getSession().getAttribute("ROLE_KEY");
   
        if (StringUtils.isNotEmpty(infoType)&&!roleKey.contains("SCHOOLADMIN")&&!roleKey.contains("ROLE_ADMIN")) {
			if ("1".equals(infoType)) { //当为收件
				if (StringUtils.isNotEmpty(filter)) {
					filter = filter.substring(0, filter.length() - 1);
					filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"emplId\"}" + "]";
				} else {
					filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"emplId\"}]";
				}
				
			} else if ("2".equals(infoType)) {					// 当为发件	
				if (StringUtils.isNotEmpty(filter)) {
					filter = filter.substring(0, filter.length() - 1);
					filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"createUser\"}" + "]";
				} else {
					filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"createUser\"}]";
				}			
			}
		}
        
        QueryResult<PushInfo> qResult = thisService.getPaginationQuery(start, limit, sort, filter, false);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    /**
     * @param entity   实体类
     * @param request
     * @param response
     * @return void 返回类型
     * @throws IOException 设定参数
     * @Title: list
     * @Description: 查询数据列表
     */
    @RequestMapping(value = {"/getPushInfoHistory"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void getPushInfoHistory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        String infoType=request.getParameter("infoType");
        
        SysUser currentUser=getCurrentSysUser();
        String roleKey=(String) request.getSession().getAttribute("ROLE_KEY");
        
        //如果不是管理员，那就只查询与自己相关的信息（发件和收件）
        if (!roleKey.contains("SCHOOLADMIN")&&!roleKey.contains("ROLE_ADMIN")) {		
        	if ("3".equals(infoType)) { //当为收件
				if (StringUtils.isNotEmpty(filter)) {
					filter = filter.substring(0, filter.length() - 1);
					filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"emplId\"}" + "]";
				} else {
					filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"emplId\"}]";
				}
				
			} else if ("4".equals(infoType)) {					// 当为发件	
				if (StringUtils.isNotEmpty(filter)) {
					filter = filter.substring(0, filter.length() - 1);
					filter += ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"createUser\"}" + "]";
				} else {
					filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"createUser\"}]";
				}			
			}        				
		}
        
        QueryResult<PushInfoHistory> qResult = pushInfoHistoryService.getPaginationQuery(start, limit, sort, filter, false);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    /**
    * 获取今日收短信的数量
    * @param request
    * @param response
    * @throws IOException
    */
    @RequestMapping(value = {"/getInfoNum"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void getInfoNum(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String filter="";
        
        try{
	        SysUser currentUser=getCurrentSysUser();
	        String roleKey=(String) request.getSession().getAttribute("ROLE_KEY");
	   
	        if (!roleKey.contains("SCHOOLADMIN")&&!roleKey.contains("ROLE_ADMIN")) {        
				filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + currentUser.getUuid()
							+ "\",\"field\":\"emplId\"}]";					   
			}
	        
	        QueryResult<PushInfo> qResult = thisService.getPaginationQuery(0, 0, "", filter, true);
	        
	        //strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
	        writeJSON(response, jsonBuilder.returnSuccessJson("\""+qResult.getTotalCount()+"\""));
	        
        }catch(Exception e){
        	writeJSON(response, jsonBuilder.returnSuccessJson("\"0\""));
        }       
        
    }
}