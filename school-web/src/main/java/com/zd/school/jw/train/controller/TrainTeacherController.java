
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.Base64Util;
import com.zd.core.util.ImportExcelUtil;
import com.zd.core.util.NotSendUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.jw.train.model.TrainTrainee;
import com.zd.school.jw.train.service.TrainTeacherService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName: TrainTeacherController
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 师资信息(TRAIN_T_TEACHER)实体Controller.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainTeacher")
public class TrainTeacherController extends FrameWorkController<TrainTeacher> implements Constant {

    @Resource
    TrainTeacherService thisService; // service层接口

    @Resource
    private BaseDicitemService dicitemService;

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
    public void list(@ModelAttribute TrainTeacher entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String sort = super.sort(request);
        String filter = super.filter(request);
        QueryResult<TrainTeacher> qResult = thisService.list(start, limit, sort, filter, true);
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
    public void doAdd(TrainTeacher entity, @RequestParam("file") MultipartFile file, HttpServletRequest request,
                      HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
        // 此处为放在入库前的一些检查的代码，如唯一校验等
        try {
        	String hql1 = " o.isDelete='0'";
        	if(null==entity.getSfzjh()||"".equals(entity.getSfzjh())){
                writeJSON(response, jsonBuilder.returnFailureJson("\"教师的身份证件号不能为空！\""));
                return;
            }
            if(null==entity.getMobilePhone()||"".equals(entity.getMobilePhone())){
                writeJSON(response, jsonBuilder.returnFailureJson("\"教师的电话号码不能为空！\""));
                return;
            }
			if (thisService.IsFieldExist("sfzjh", entity.getSfzjh(), "-1", hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"教师的身份证件号不能重复！\""));
				return;
			}
			entity.setMobilePhone(Base64Util.encodeData(entity.getMobilePhone()));
			entity.setSfzjh(Base64Util.encodeData(entity.getSfzjh()));
			
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
                String url = "/static/upload/trainTeacherPhoto/" + sdf.format(System.currentTimeMillis()) + "/";
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
    public void doUpdates(TrainTeacher entity, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码
        try {
        	String hql1 = " o.isDelete='0'";
			if (thisService.IsFieldExist("sfzjh", entity.getSfzjh(), entity.getUuid(), hql1)) {
				writeJSON(response, jsonBuilder.returnFailureJson("\"教师的身份证件号不能重复！\""));
				return;
			}
			
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
                String url = "/static/upload/trainTeacherPhoto/" + sdf.format(System.currentTimeMillis()) + "/";
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
        	SysUser currentUser=getCurrentSysUser();
            InputStream in = null;
            List<List<Object>> listObject = null;
            if (!file.isEmpty()) {
                in = file.getInputStream();
                listObject = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
                in.close();

                /**
                 * 格式
                 * 第一行为列头【姓名	性别	职务	职称	学历	手机号码	校内/外	工作单位	专业	电子邮件	通讯地址	教师简介	主要研究方向	主要研究成果	主要讲授专题】
                 * 第二行开始为数据 【老师3	 2	18776196954	450321199404014054	21	的⒈阿萨德	阿萨德	发愤忘食	阿萨德	啥啊	54032322@qq.com	阿斯达斯啊	阿萨德撒旦撒旦	  /static/upload/trainTeacherPhoto/20170406/1491446354821.jpg】
                 */
                //要转换的数据字典
                String mapKey = null;
                String[] propValue = {"TECHNICAL","XBM","XLM","INOUT"};
                Map<String, String> mapDicItem = new HashMap<>();
                List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
                for (BaseDicitem baseDicitem : listDicItem) {
                    mapKey = baseDicitem.getItemName() + baseDicitem.getDicCode();
                    mapDicItem.put(mapKey, baseDicitem.getItemCode());
                }
                TrainTeacher teacher=null;
                for (int i = 0; i < listObject.size(); i++) {
                    List<Object> lo = listObject.get(i);
                    
                    // 查询师资库是否存在此教师，如果存在，则直接修改，不添加
                    if(StringUtils.isNotEmpty(String.valueOf(lo.get(15))))
                    	teacher = thisService.getByProerties(new String[] { "sfzjh", "isDelete" }, new Object[] { lo.get(15), 0 });
                    else
                    	teacher=null;
                    
        			if (teacher == null) {
        				teacher = new TrainTeacher();
        				teacher.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
        			}
        			
                    teacher.setXm(String.valueOf(lo.get(0)));
                    teacher.setXbm(mapDicItem.get(String.valueOf(lo.get(1)) + "XBM"));
                    teacher.setPosition(String.valueOf(lo.get(2)));
                    teacher.setTechnical(mapDicItem.get(String.valueOf(lo.get(3))+"TECHNICAL"));

                    teacher.setXlm(mapDicItem.get(String.valueOf(lo.get(4))+"XLM"));
                    teacher.setMobilePhone(String.valueOf(lo.get(5)));
                    teacher.setInout(mapDicItem.get(String.valueOf(lo.get(6))+"INOUT"));

                    teacher.setWorkUnits(String.valueOf(lo.get(7)));
                    teacher.setZym(String.valueOf(lo.get(8)));
                    teacher.setDzxx(String.valueOf(lo.get(9)));
                    teacher.setAddress(String.valueOf(lo.get(10)));

                    teacher.setTeaDesc(String.valueOf(lo.get(11)));
                    teacher.setResearchArea(String.valueOf(lo.get(12)));
                    teacher.setResearchResult(String.valueOf(lo.get(13)));
                    teacher.setTeachingProject(String.valueOf(lo.get(14)));
                    teacher.setSfzjh(String.valueOf(lo.get(15)));
                    teacher.setZp("/static/upload/trainTeacherPhoto/" + String.valueOf(lo.get(15)) + ".jpg");
                    
                    teacher.setUpdateTime(new Date());
                    teacher.setUpdateUser(currentUser.getXm());
        			
                    thisService.doMerge(teacher);

                }
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("\"文件不存在！\""));
            }

            writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导入成功！\""));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导入失败,请联系管理员！\""));
        }
    }

    /**
     * 导出师资信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("exportTeacherIsEnd","0");
        request.getSession().removeAttribute("exportTeacherIsState");
        String ids = request.getParameter("ids");
        List<TrainTeacher> list = null;
        String hql = " from TrainTeacher where isDelete=0 ";
        if (StringUtils.isNotEmpty(ids)) {
            hql += " and uuid in ('" + ids.replace(",", "','") + "')";
        }
        hql += " order by sfzjh";
        list = thisService.getQuery(hql);
        List<TrainTeacher> exportList = new ArrayList<>();

        //职称数据字典
        String mapKey = null;
        String[] propValue = {"TECHNICAL","XBM","XLM","INOUT"};
        Map<String, String> mapDicItem = new HashMap<>();
        List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
        for (BaseDicitem baseDicitem : listDicItem) {
            mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
            mapDicItem.put(mapKey, baseDicitem.getItemName());
        }

        for (TrainTeacher trainTeacher : list) {
            trainTeacher.setTechnicalName(mapDicItem.get(trainTeacher.getTechnical() + "TECHNICAL"));
            trainTeacher.setXbmName(mapDicItem.get(trainTeacher.getXbm() + "XBM"));
            trainTeacher.setXlmName(mapDicItem.get(trainTeacher.getXlm()+"XLM"));
            trainTeacher.setInoutName(mapDicItem.get(trainTeacher.getInout() + "INOUT"));
        }
        exportList.addAll(list);
        //Thread.sleep(10000);
        boolean result = FastExcel.exportExcel(response, "师资信息", exportList);
        
        if (result == true) {
			request.getSession().setAttribute("exportTeacherIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTeacherIsEnd", "0");
			request.getSession().setAttribute("exportTeacherIsState", "0");
		}
    }
    /**
	 * 判断导出时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportEnd")
	public void checkExportRoomEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

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
		}
	}


    @RequestMapping("/sendMessage")
    public void sendMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strData = "";
        String[] mobileStr = {"18018711239","13682353948"};
        String content = "短信发送测试内容 via  IM JAVA API ";
        strData = NotSendUtil.notSendUtil().sendSMSs(mobileStr, content);
        if("发送成功".equals(strData))
            writeJSON(response, jsonBuilder.returnSuccessJson("\"发送成功！\""));
        else
            writeJSON(response, jsonBuilder.returnFailureJson(strData));
    }
}
