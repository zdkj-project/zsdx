package com.zd.core.util;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping("/excelreport")
public class ExcelReportController extends FrameWorkController<SysUser> implements Constant {

    @Resource
    SysUserService thisService;

    @RequestMapping("/toExcel")
    // public String reportExcel(String tableCode, String whereSql, String
    // fieldNames, String fieldCodes, String userFlag)
    public void toExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootPath = request.getContextPath();
        String uploadUrl = request.getParameter("uploadUrl");
        String tableCode = request.getParameter("tableCode");
        String fieldNames = request.getParameter("fieldNames");
        String fieldCodes = request.getParameter("fieldCodes");
        String userFlag = "超级管理员";
        String whereSql = "";
        if (StringUtils.isNotEmpty(request.getParameter("whereSql"))) {
            whereSql += request.getParameter("whereSql");
        }
        if (StringUtils.isNotEmpty(request.getParameter("parentSql"))) {
            whereSql += request.getParameter("parentSql");
        }
        if (StringUtils.isNotEmpty(request.getParameter("querySql"))) {
            whereSql += request.getParameter("querySql");
        }
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            //userFlag = currentUser.getDeptName() + "_" + currentUser.getXm();
            userFlag = currentUser.getXm();

        String path = thisService.reportExcel(tableCode, whereSql, fieldNames, fieldCodes, userFlag, uploadUrl);
        if (StringUtils.isNotEmpty(path)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("{url:'" + rootPath + "/static" + path + "'}"));
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("'导出失败！'"));
        }
    }

    @RequestMapping("/addExcel")
    public ModelAndView addExcel(@RequestParam MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!file.isEmpty()) {
            String filePath = request.getSession().getServletContext().getRealPath("/") + "/upload/"
                    + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            ModelMap model = new ModelMap();
            model.put("filePath", filePath);
            return new ModelAndView("redirect:/JwTStudent/doimport", model);
        }
        return null;
    }
}
