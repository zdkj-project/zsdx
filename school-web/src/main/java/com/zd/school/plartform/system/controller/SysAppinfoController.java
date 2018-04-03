
package com.zd.school.plartform.system.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.school.plartform.system.model.SysAppinfo ;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysAppinfoService ;

/**
 * 
 * ClassName: SysAppinfoController
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: (SYS_T_APPINFO)实体Controller.
 * date: 2017-05-15
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/SysAppinfo")
public class SysAppinfoController extends FrameWorkController<SysAppinfo> implements Constant {

    @Resource
    SysAppinfoService thisService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<SysAppinfo> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    
    /**
	 * 上传文件
	 * 
	 * @param sendId
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    @RequestMapping(value = {"/doUploadApp"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void doUpload(SysAppinfo entity,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		try{
	        if (!file.isEmpty()) {
	            // 重命名上传后的文件名
	            String myFileName = file.getOriginalFilename();
	            String type = myFileName.substring(myFileName.lastIndexOf(".")).trim();
	            if(!type.equalsIgnoreCase(".apk")&&!type.equalsIgnoreCase(".ipa")){
	            	writeJSON(response, jsonBuilder.returnFailureJson("'上传失败,请选择APK|IPA类型的文件！'"));
	            	return;
	            }
	            	
	            String fileName = String.valueOf(System.currentTimeMillis()) + type;
	
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	            String url = "/static/upload/apk/" + sdf.format(System.currentTimeMillis()) + "/";
	            ///static/upload/video/Baby(1.16)20160822205.mp3
	            String rootPath = request.getSession().getServletContext().getRealPath("/");
	            //String rootPath="G:\\PSTX_FILE";
	            rootPath = rootPath.replace("\\", "/");
	
	            // 定义上传路径
	            String path = rootPath + url + fileName;
	            File localFile = new File(path);
	
	            if (!localFile.exists()) { // 判断文件夹是否存在
	                localFile.mkdirs(); // 不存在则创建
	            }
	
	            file.transferTo(localFile);
	           
	    		
	            // 获取当前操作用户
	            String userCh = "超级管理员";
	            SysUser currentUser = getCurrentSysUser();
	            if (currentUser != null)
	                userCh = currentUser.getXm();

	            SysAppinfo perEntity = new SysAppinfo();
	            BeanUtils.copyPropertiesExceptNull(entity, perEntity);
	            Integer orderIndex = thisService.getDefaultOrderIndex(entity);
	            entity.setOrderIndex(orderIndex);// 排序
	            entity.setCreateUser(userCh); // 创建人
	            
	            entity.setAppUrl(url + fileName);	           
	            entity.setAppIsuse(0);
	            // 持久化到数据库
	            entity = thisService.doMerge(entity);

	            // 返回实体到前端界面
	            //writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));	          
	        }
	        
	        writeJSON(response, jsonBuilder.returnSuccessJson("\"上传文件成功！\""));
        }catch(Exception e){
        	 writeJSON(response, jsonBuilder.returnFailureJson("\"上传失败,请联系管理员！\""));
        }
	}
    
	
	/***
	 * 更新状态，启用或不启用
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/doUpdateState")	
    public void doUpdateState( HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
    	try{
    		String id=request.getParameter("id");
    		String appType=request.getParameter("appType");
    		String appUrl=request.getParameter("appUrl");
    		Integer appIsuse=Integer.parseInt(request.getParameter("appIsuse"));
    		
    		// 获取当前的操作用户
            String userCh = "超级管理员";
            SysUser currentUser = getCurrentSysUser();
            if (currentUser != null)
                userCh = currentUser.getXm();
        
            String loadUrl="";
        	switch(appType){
	        	case "1": loadUrl="/static/upload/appUpdate/metting_attendance.apk"; break;
	        	case "2": loadUrl="/static/upload/appUpdate/course_attendance.apk"; break;
	    	}
        	String rootPath = request.getSession().getServletContext().getRealPath("/");
	            //String rootPath="G:\\PSTX_FILE";
	        rootPath = rootPath.replace("\\", "/");
	        String loadUrlPath = rootPath + loadUrl;
	        String appUrlPath = rootPath+ appUrl;
	        
    		if(appIsuse==1){
    			//图片复制到指定文件中，给用户提供最新下载
    			if(copyFile(appUrlPath,loadUrlPath)!=true){
    				writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请稍后尝试！\""));
    				return;
    			}
 	    		
	    		String hql1="update SysAppinfo g  set g.appIsuse=0,g.updateUser='"+userCh+"' where g.isDelete=0 and g.appIsuse=1 and g.appType='"+appType+"' ";
	    		String hql2="update SysAppinfo g  set g.appIsuse=1,g.updateUser='"+userCh+"' where g.isDelete=0 and g.uuid='"+id+"' ";
	    		thisService.doExecuteHql(hql1);
	    		thisService.doExecuteHql(hql2);
    		}else{    		    		
    			//指定文件夹中的的apk文件删除，防止再次下载    	
    			File file = new File(loadUrlPath);
    			if(file.exists()){
    				if(file.delete()!=true){
    					writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请稍后尝试！\""));
        				return;
    				}
    			}
    			
	    		String hql1="update SysAppinfo g  set g.appIsuse=0,g.updateUser='"+userCh+"' where g.isDelete=0 and g.uuid='"+id+"' ";
	    		thisService.doExecuteHql(hql1);
    		}
    		    	
    		writeJSON(response, jsonBuilder.returnSuccessJson("\"操作成功！\""));
    	}catch(Exception e){
    		writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员！\""));
    	}

    }

	public boolean copyFile(String src,String dest){
		boolean returnVal=true;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			//1.提供读入、写出的文件
			File file1 = new File(src);
			File file2 = new File(dest);
			
			//2.想创建相应的节点流：FileInputStream、FileOutputStream
			FileInputStream fis = new FileInputStream(file1);
			FileOutputStream fos = new FileOutputStream(file2);
			//3.将创建的节点流的对象作为形参传递给缓冲流的构造器中
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(fos);
			//4.具体的实现文件复制的操作
			byte[] b = new byte[1024];
			int len;
			while((len = bis.read(b)) != -1){
				bos.write(b, 0, len);
				bos.flush();
			}
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnVal=false;
		}finally{
			//5.关闭相应的流
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return returnVal;
	}
	
    
    /**
     * 获取app升级信息
     * @return
     * @throws IOException 
     */
    @RequestMapping("/up/{type}")
    public void getUp(HttpServletResponse response,HttpServletRequest request,@PathVariable("type") Integer type) throws IOException{
    	SysAppinfo appinfo = this.thisService.getQuery("FROM SysAppinfo WHERE appIsuse = "+1+" and appType ="+type).get(0);
    	String requestURL = request.getRequestURL()+"";
		String [] strs= requestURL.split("/");
    	//String url =strs[0]+strs[1]+"//"+strs[2]+appinfo.getAppUrl();
		String url =strs[0]+strs[1]+"//"+strs[2];
				
    	switch(type){
	      	case 1: url+="/static/upload/appUpdate/metting_attendance.apk" ; break;
	      	case 2: url+="/static/upload/appUpdate/course_attendance.apk" ; break;
	  	}
	    	
    	String json = "{\"version\":\""+appinfo.getAppVersion()+"\",\"url\":\""+url+"\"}";
    	writeJSON(response,json);
    }     
  	
}
