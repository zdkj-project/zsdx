package com.zd.school.jw.arrangecourse.controller;



import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.JsonBuilder;
import com.zd.school.jw.arrangecourse.model.JwInvigilate;
import com.zd.school.jw.arrangecourse.service.JwInvigilateService;
import com.zd.school.jw.controller.util.ExcelUtil;


/**
 * 
 * ClassName: JwTCourseStudyController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 自习课程表实体Controller.
 * date: 2016-04-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwTInvigilate")
public class JwInvigilateController extends FrameWorkController<JwInvigilate> implements Constant {

    @Resource
    JwInvigilateService thisService;
    


    //上传excel
	@RequestMapping("/addExcel")
	public ModelAndView addExcel(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!file.isEmpty()) {
                // 重命名上传后的文件名
                String myFileName = file.getOriginalFilename();
                String type = myFileName.substring(myFileName.lastIndexOf("."));
                String fileName = String.valueOf(System.currentTimeMillis()) + type;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String url = "/static/upload/zp/" + sdf.format(System.currentTimeMillis()) + "/";

                String rootPath = request.getSession().getServletContext().getRealPath("/");
                rootPath = rootPath.replace("\\", "/");

                // 定义上传路径
                String path = rootPath + url + fileName;
                File localFile = new File(path);

                if (!localFile.exists()) { // 判断文件夹是否存在
                    localFile.mkdirs(); // 不存在则创建
                }
			
			file.transferTo(localFile);
			
			ModelMap model = new ModelMap();
			model.put("filePath",path);
			return new ModelAndView("redirect:/JwTInvigilate/doimport",model);
		}
		return null;
	}
    
	//导入数据库
	@RequestMapping("/doimport")
	public ModelAndView excelImport(String filePath, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		try {
			// 1、创建excel导入工具
			ExcelUtil excelUtil = new ExcelUtil();
			// 2、创建时间格式
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 3、通过工具获取教师数据
			List<List<String>> InvigilateData = excelUtil.importExcel(filePath);
			// 4、创建教师集合
			List<String> InvigilateList = null;
			// 5、创建教师对象
			JwInvigilate invigilate = null;
			
			String sql="delete dbo.JW_T_INVIGILATE";
			this.thisService.executeSql(sql);
			// 6、通过循环将数据放入教师集合中，从集合中获取教师对象，一一设置属性
			for (int i = 0; i < InvigilateData.size(); i++) {
				InvigilateList = InvigilateData.get(i);
				invigilate = new JwInvigilate();
				invigilate.setXm(InvigilateList.get(0));
				invigilate.setSchool(InvigilateList.get(1));
				invigilate.setBh(InvigilateList.get(2));

				// 7、存入数据库

				this.thisService.merge(invigilate);
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/static/core/coreApp/arrangecourse/Invigilate/iframe/uploadzp.jsp");
	}
	
    //查询所有教师
	@RequestMapping("/list")
	public @ResponseBody List<JwInvigilate> list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String hql="from JwInvigilate";
		List<JwInvigilate> list=thisService.doQuery(hql);
		return list;
	}
    //根据分页查询
	@RequestMapping("/grouplist")
	public @ResponseBody List<JwInvigilate> grouplist(String number,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int page = Integer.parseInt(number);
		int page1=4*(page-1);
		String sql="select top 4 * from dbo.JW_T_INVIGILATE where TTEAC_ID not in(select top "+page1+" TTEAC_ID from dbo.JW_T_INVIGILATE order by TTEAC_ID) order by TTEAC_ID";
		List<JwInvigilate> list=thisService.doQuerySql(sql);
		return list;
	}
	
	
    //查询所有教师
	@RequestMapping("/countAll")
	public @ResponseBody Integer countAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String hql="select count(*)  from  JwInvigilate";
		Integer count=thisService.getCount(hql);
		Integer pageCount=0;
		if(count%4==0){
			pageCount=count/4;
		}else{
			pageCount=count/4+1;
		}
		return pageCount;
	}
	
	
	
    //查询所有教师
	@SuppressWarnings("restriction")
	@RequestMapping("/photo")
	public ModelAndView photo(@RequestParam("files") MultipartFile[] files,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        if(files!=null&&files.length>0){  
            //循环获取file数组中得文件  
            for(int i = 0;i<files.length;i++){  
                MultipartFile file = files[i];  
                
        		// 文件保存路径  
                String filePath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/"
        				+ file.getOriginalFilename();
                /*String teacherid=request.getParameter("key"+i);
                String zp="/static/upload/"+ file.getOriginalFilename();
                String hql="update JwTInvigilate set zp='"+zp+"' where tteacId='"+teacherid+"'";
                thisService.executeHql(hql);*/
                //保存文件  
                saveFile(file,filePath); 
                String zp=null;
                String teacherid=request.getParameter("key"+i);
                if(file.getOriginalFilename()==null||file.getOriginalFilename()==""){
                	zp="";
                }else{
                	zp="/static/upload/"+ file.getOriginalFilename();
                }
                String hql="update JwInvigilate set zp='"+zp+"' where uuid='"+teacherid+"'";
                thisService.executeHql(hql);
            }  
        }
        return new ModelAndView("redirect:/static/core/coreApp/arrangecourse/Invigilate/iframe/certificate.jsp");
	}


    private boolean saveFile(MultipartFile file,String filePath) {  
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {   
                // 转存文件  
                file.transferTo(new File(filePath));  
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }

}
