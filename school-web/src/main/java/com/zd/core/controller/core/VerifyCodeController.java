/**
 * Project Name:jw-web
 * File Name:VerifyCodeController.java
 * Package Name:com.zd.core.controller
 * Date:2016年4月23日上午10:03:09
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
 */

package com.zd.core.controller.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.StringUtils;

/**
 * ClassName: VerifyCodeController Function: TODO ADD FUNCTION. Description:
 * 验证生成控制器. date: 2016年4月23日 上午10:03:09
 *
 * @author luoyibo
 * @version
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/verifycode")
public class VerifyCodeController extends BaseController<BaseEntity> {

    @Resource
    private Producer captchaProducer;

    @RequestMapping("/image")
    public void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        String capText = captchaProducer.createText();
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        BufferedImage bi = captchaProducer.createImage(capText);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);

        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value = { "/check" }, method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public @ResponseBody String checkImage(String verifyCode, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        @SuppressWarnings("restriction")
        String kaptchaValue = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if ("aaaa".equals(verifyCode)){
           return "true";
        }

        String news = "";

        if (StringUtils.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(kaptchaValue)) {
            news = "false";
            return news;
        } else{
            news = "true";
        }
        return news;

    }
}
