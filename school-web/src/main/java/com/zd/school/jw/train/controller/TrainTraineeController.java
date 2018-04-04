
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.Base64Util;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainTrainee;
import com.zd.school.jw.train.service.TrainTraineeService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ClassName: TrainTraineeController
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 学员信息(TRAIN_T_TRAINEE)实体Controller.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainTrainee")
public class TrainTraineeController extends FrameWorkController<TrainTrainee> implements Constant {

    private static Logger logger = Logger.getLogger(TrainTraineeController.class);

    @Resource
    TrainTraineeService thisService; // service层接口

    /**
     * @param entity   实体类
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException 设定参数
     * @Title: list
     * @Description: 查询数据列表
     */
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void list(@ModelAttribute TrainTrainee entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        QueryResult<TrainTrainee> qResult = thisService.list(start, limit, sort, filter, true);
        for(int i=0;i<qResult.getResultList().size();i++) {
            if(Base64Util.isBase64(qResult.getResultList().get(i).getMobilePhone())){
                qResult.getResultList().get(i).setMobilePhone(Base64Util.decodeData(qResult.getResultList().get(i).getMobilePhone()));
            }
            if(Base64Util.isBase64(qResult.getResultList().get(i).getSfzjh())){
                qResult.getResultList().get(i).setSfzjh(Base64Util.decodeData(qResult.getResultList().get(i).getSfzjh()));
            }
        }
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * @param entity
     * @param file
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = {"/doadd"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void doAdd(TrainTrainee entity, @RequestParam("file") MultipartFile file, HttpServletRequest request,
                      HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
        // 此处为放在入库前的一些检查的代码，如唯一校验等
        try {
        	
			String hql1 = " o.isDelete='0'";
            if(null==entity.getSfzjh()||"".equals(entity.getSfzjh())){
                writeJSON(response, jsonBuilder.returnFailureJson("\"学员的身份证件号不能为空！\""));
                return;
            }
            if(null==entity.getMobilePhone()||"".equals(entity.getMobilePhone())){
                writeJSON(response, jsonBuilder.returnFailureJson("\"学员的电话号码不能为空！\""));
                return;
            }
			if (thisService.IsFieldExist("sfzjh", entity.getSfzjh(), "-1", hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"学员的身份证件号不能重复！\""));
				return;
			}

			//前端处理
            //entity.setMobilePhone(Base64Util.encodeData(entity.getMobilePhone()));
            //entity.setSfzjh(Base64Util.encodeData(entity.getSfzjh()));
			
            if (!file.isEmpty() && file.getSize() > 0) {
                // 重命名上传后的文件名
                String myFileName = file.getOriginalFilename();
                String type = myFileName.substring(myFileName.lastIndexOf(".")).trim();
                if (!type.equalsIgnoreCase(".png") && !type.equalsIgnoreCase(".jpg")
                        && !type.equalsIgnoreCase(".jpeg")) {
                    writeJSON(response, jsonBuilder.returnFailureJson("\"上传失败,请选择PNG|JPG|JPEG类型的图片文件！\""));
                    return;
                }

                String fileName = String.valueOf(System.currentTimeMillis()) + type;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String url = "/static/upload/trainTraineePhoto/" + sdf.format(System.currentTimeMillis()) + "/";
                /// static/upload/trainTeacherPhoto/1111.png
                String rootPath = request.getSession().getServletContext().getRealPath("/");
                // String rootPath="G:\\ZSDX_FILE";
                rootPath = rootPath.replace("\\", "/");

                // 定义上传路径
                String path = rootPath + url + fileName;
                File localFile = new File(path);

                if (!localFile.exists()) { // 判断文件夹是否存在
                    localFile.mkdirs(); // 不存在则创建
                }

                file.transferTo(localFile);

                // 压缩图片
//				BufferedImage bufferedImage = ImageIO.read(localFile);
//				int width = bufferedImage.getWidth();
//				int height = bufferedImage.getHeight();
//				int mode = 0;
//				if (width > height) {
//					mode = ImageUtil.DefineWidth;
//				} else {
//					mode = ImageUtil.DefineHeight;
//				}
//				ImageUtil imageUtil = ImageUtil.getInstance();
//				imageUtil.resize(mode, path, path, 800, 600, 0, 0);

                entity.setZp(url + fileName);
            }

            // 获取当前操作用户
            SysUser currentUser = getCurrentSysUser();
            entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法

            // 返回实体到前端界面
            writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));

        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请联系管理员！\""));
        }
    }


    /**
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException 抛出异常
     * @Title: doDelete
     * @Description: 逻辑删除指定的数据
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            SysUser currentUser = getCurrentSysUser();
            try {
                boolean flag = thisService.doLogicDeleteByIds(delIds, currentUser);
                if (flag) {
                    writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
                } else {
                    writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
                }
            } catch (Exception e) {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
            }
        }
    }

    /**
     * @param entity
     * @param file
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/doupdate")
    public void doUpdates(TrainTrainee entity, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码
        try {
        	String hql1 = " o.isDelete='0'";
			if (thisService.IsFieldExist("sfzjh", entity.getSfzjh(), entity.getUuid(), hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"学员的身份证件号不能重复！\""));
				return;
			}
			
			//前端处理
			//entity.setMobilePhone(Base64Util.encodeData(entity.getMobilePhone()));
	        //entity.setSfzjh(Base64Util.encodeData(entity.getSfzjh()));
	            
			
            if (!file.isEmpty() && file.getSize() > 0) {
                // 重命名上传后的文件名
                String myFileName = file.getOriginalFilename();
                String type = myFileName.substring(myFileName.lastIndexOf(".")).trim();
                if (!type.equalsIgnoreCase(".png") && !type.equalsIgnoreCase(".jpg")
                        && !type.equalsIgnoreCase(".jpeg")) {
                    writeJSON(response, jsonBuilder.returnFailureJson("\"上传失败,请选择PNG|JPG|JPEG类型的图片文件！\""));
                    return;
                }

                String fileName = String.valueOf(System.currentTimeMillis()) + type;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String url = "/static/upload/trainTraineePhoto/" + sdf.format(System.currentTimeMillis()) + "/";
                /// static/upload/trainTeacherPhoto/1111.png
                String rootPath = request.getSession().getServletContext().getRealPath("/");
                // String rootPath="G:\\ZSDX_FILE";
                rootPath = rootPath.replace("\\", "/");

                // 定义上传路径
                String path = rootPath + url + fileName;
                File localFile = new File(path);

                if (!localFile.exists()) { // 判断文件夹是否存在
                    localFile.mkdirs(); // 不存在则创建
                }

                file.transferTo(localFile);

                // 压缩图片
//				BufferedImage bufferedImage = ImageIO.read(localFile);
//				int width = bufferedImage.getWidth();
//				int height = bufferedImage.getHeight();
//				int mode = 0;
//				if (width > height) {
//					mode = ImageUtil.DefineWidth;
//				} else {
//					mode = ImageUtil.DefineHeight;
//				}
//				ImageUtil imageUtil = ImageUtil.getInstance();
//				imageUtil.resize(mode, path, path, 800, 600, 0, 0);

                entity.setZp(url + fileName);

            }

            //获取当前的操作用户
            SysUser currentUser = getCurrentSysUser();
            entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法

            writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));

        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败,请联系管理员！\""));
        }

    }


    /**
     * 描述：通过传统方式form表单提交方式导入excel文件
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/importData", method = {RequestMethod.GET, RequestMethod.POST})
    public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            InputStream in = null;
            List<List<Object>> listObject = null;
            SysUser currentUser = getCurrentSysUser();
            if (!file.isEmpty()) {
                in = file.getInputStream();
                listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
                in.close();
                thisService.doImportTrainee(listObject, currentUser);
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
            }

            writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
        }
    }

    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("exportTeacherIsEnd", "0");
        request.getSession().removeAttribute("exportTeacherIsState");
        String ids = request.getParameter("ids");
        String orderSql = request.getParameter("orderSql");

        try {
            List<TrainTrainee> list = thisService.listExport(ids, orderSql);
            FastExcel.exportExcel(response, "学员信息", list);
            request.getSession().setAttribute("exportTeacherIsEnd", "1");
        } catch (IOException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute("exportTeacherIsEnd", "0");
			request.getSession().setAttribute("exportTeacherIsState", "0");
            //writeJSON(response,jsonBuilder.returnFailureJson("\"文件导出失败，详情请见错误日志\""));
        }
    }

    @RequestMapping("/checkExportEnd")
    public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
 
        Object isEnd = request.getSession().getAttribute("exportTeacherIsEnd");
        Object state = request.getSession().getAttribute("exportTeacherIsState");
        
        if (isEnd != null) {
        	if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
			}
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
            request.getSession().setAttribute("exportTeacherIsEnd", "0");
        }
    }
}
