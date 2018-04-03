
package com.zd.school.student.studentclass.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ExcelUtil;
import com.zd.core.util.JsonBuilder;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentclass.model.StuDividerecord;
import com.zd.school.student.studentclass.model.StuDividescore ;
import com.zd.school.student.studentclass.service.StuDividerecordService;
import com.zd.school.student.studentclass.service.StuDividescoreService ;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;

/**
 * 
 * ClassName: StuDividescoreController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班成绩实体Controller.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/StuDividescore")
public class StuDividescoreController extends FrameWorkController<StuDividescore> implements Constant {

    @Resource
    StuDividescoreService thisService; // service层接口

    @Resource
    StuBaseinfoService stuBaseService;//学生

    @Resource
    StuDividerecordService stuDividerecordService;//分班记录
    
    /**
      * list查询
      * @Title: list
      * @Description: TODO
      * @param @param entity 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })

    public void list(@ModelAttribute StuDividescore entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<StuDividescore> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    /**
     * 分班导入成绩
     * @param file
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addExcels")
	public ModelAndView addExcels(@RequestParam MultipartFile file , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String rootPath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/excel";
		// 定义上传路径
		File localFile = new File(rootPath);
		if (!localFile.exists()) { // 判断文件夹是否存在
			localFile.mkdirs(); // 不存在则创建
		}
		if (!file.isEmpty()) {
			String filePath = localFile + file.getOriginalFilename();

			file.transferTo(new File(filePath));
			ModelMap model = new ModelMap();
			model.put("filePath", filePath);
			return new ModelAndView("redirect:/StuDividescore/doimport", model);
		}
		return null;
	}

	/**
	 * 将分班导入的成绩添加到数据库中
	 * @param filePath
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/doimport")
	public ModelAndView excelImport(String filePath, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			boolean flag = thisService.IsFieldExist("isDelete", "0", "-1");
			if (flag) {
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'请勿重复导入相同数据'"));
				return null;
			}
			int count = 0;
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();
			ExcelUtil excelUtil = new ExcelUtil();
			List<List<String>> studentData = excelUtil.importExcel(filePath);
			List<String> studentList = null;
			StuDividescore student = null;
			StuDividerecord code = stuDividerecordService.getByProerties("isDelete", 0);
			if (code != null)
				for (int i = 0; i < studentData.size(); i++) {
					studentList = studentData.get(i);
					// 判断是否存在学生信息表
					boolean s = stuBaseService.IsFieldExist("xh", studentList.get(1), "-1", "isDelete=0");
					if (s) {
						student = new StuDividescore();
						student.setDivideId(code.getUuid());// 步骤id
						student.setExamNumb(studentList.get(0));// 考号
						student.setXh(studentList.get(1));// 学生学号
						student.setXm(studentList.get(2));// 姓名
						student.setXbm(studentList.get(3));// 性别码
						student.setArtsSciences(studentList.get(4));// 设置文理类型
						student.setScore(new BigDecimal(studentList.get(5))); // 设置分数
						student.setCreateTime(new Date());// 设置创建时间
						student.setCreateUser(userCh);// 创建人
						thisService.doMerge(student);
						++count;
					}

				}
			
			if (count > 0) {
				writeJSON(response,
						JsonBuilder.getInstance().returnSuccessJson("'已经将存在学生信息表中的学生导入，没有存在学生信息表中的学生未能导入！'"));
			}else{
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'请将第一步数据填写完整再导入数据'"));
			}
		} catch (Exception e) {
			writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'出现未知问题，请联系攻城狮...'"));
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 添加excel
     * @param file
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addCatchExcels")
	public ModelAndView addCatchExcels(@RequestParam MultipartFile file , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String rootPath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/excel";
		// 定义上传路径
		File localFile = new File(rootPath);
		if (!localFile.exists()) { // 判断文件夹是否存在
			localFile.mkdirs(); // 不存在则创建
		}
		if (!file.isEmpty()) {
			String filePath = localFile + file.getOriginalFilename();

			file.transferTo(new File(filePath));
			ModelMap model = new ModelMap();
			model.put("filePath", filePath);
			return new ModelAndView("redirect:/StuDividescore/catchDoimport", model);
		}
		return null;
	}

	/**
	 * 将导入的Excel文件在此处理 excelImport(这里用一句话描述这个方法的作用) TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选) TODO(这里描述这个方法的使用方法 – 可选) TODO(这里描述这个方法的注意事项 –
	 * 可选)
	 *
	 * @Title: excelImport @Description: TODO @param @param
	 *         filePath @param @param request @param @param
	 *         response @param @return @param @throws IOException 设定参数 @return
	 *         ModelAndView 返回类型 @throws
	 */
	@RequestMapping("/catchDoimport")
	public ModelAndView catchDoimport(String filePath, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			boolean flag = thisService.IsFieldExist("isDelete", "0", "-1");
			if (flag) {
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'请勿重复导入相同数据'"));
				return null;
			}
			int count = 0;
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();
			// 1、创建excel导入工具
			ExcelUtil excelUtil = new ExcelUtil();
			// 3、通过工具获取品牌数据
			List<List<String>> studentData = excelUtil.importExcel(filePath);
			// 4、创建学生集合
			List<String> studentList = null;
			// 5、创建学生对象
			StuDividescore student = null;
			// 6、通过循环将数据放入学生集合中，从集合中获取学生对象，一一设置属性
				for (int i = 1; i < studentData.size(); i++) {
					studentList = studentData.get(i);
					// 判断是否存在学生信息表
					boolean s = stuBaseService.IsFieldExist("xh", studentList.get(1), "-1", "isDelete=0");
					if (s) {
						student = new StuDividescore();
						student.setExamNumb(studentList.get(0));// 考号
						student.setXh(studentList.get(1));// 学生学号
						student.setXm(studentList.get(2));// 姓名
						student.setXbm(studentList.get(3));// 性别码
						student.setArtsSciences(studentList.get(4));// 设置文理类型
						student.setScore(new BigDecimal(studentList.get(5))); // 设置分数
						student.setCreateTime(new Date());// 设置创建时间
						student.setCreateUser(userCh);// 创建人
						thisService.doMerge(student);
						++count;
					}

				}
			
			if (count > 0) {
				writeJSON(response,
						JsonBuilder.getInstance().returnSuccessJson("'已经将存在学生信息表中的学生导入，没有存在学生信息表中的学生未能导入！'"));
			}else{
				writeJSON(response, JsonBuilder.getInstance().returnFailureJson("'请将第一步数据填写完整再导入数据'"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
