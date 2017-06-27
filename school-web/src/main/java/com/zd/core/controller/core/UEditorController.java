/**
 * Project Name:school-web
 * File Name:UEditorController.java
 * Package Name:com.zd.core.controller.core
 * Date:2016年6月8日上午10:58:38
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.controller.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.zd.core.model.BaseEntity;

/**
 * ClassName:UEditorController 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016年6月8日 上午10:58:38 
 * @author   luoyibo
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Controller
@RequestMapping("/ueditor")
public class UEditorController extends BaseController<BaseEntity> {
    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
 
        response.setContentType("application/json");
        String rootPath = request.getSession()
                .getServletContext().getRealPath("/");
 
        try {
            String exec = new ActionEnter(request, rootPath).exec();
           PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}

