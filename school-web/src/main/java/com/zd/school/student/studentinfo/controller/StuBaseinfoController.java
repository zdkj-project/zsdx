
package com.zd.school.student.studentinfo.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.PinyinUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.controller.util.ExcelUtil;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.baseset.model.BaseDic;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseDicService;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.service.JwClassstudentService;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: StuBaseinfoController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 学生基本信息实体Controller. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/StuBaseinfo")
public class StuBaseinfoController extends FrameWorkController<StuBaseinfo> implements Constant {

	@Resource
	StuBaseinfoService thisService; // service层接口

	@Resource
	BaseDicitemService dictionartItemService;

	@Resource
	BaseDicService dictionaryService;

	@Resource
	SysUserService userService;

	@Resource
	private JwTGradeclassService gcService;

	@Resource
	private BaseJobService jobService;

	@Resource
	private BaseOrgService orgService;
	
	@Resource
	private JwClassstudentService classStudentService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute StuBaseinfo entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		String claiId = request.getParameter("claiId");
		SysUser currentUser = getCurrentSysUser();
		QueryResult<StuBaseinfo> qr = thisService.getStudentList(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true, claiId, currentUser);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param StuBaseinfo
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(@RequestParam MultipartFile file, StuBaseinfo entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

		if (!file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/"
					+ file.getOriginalFilename();
			file.transferTo(new File(filePath));
			String fileName = "/static/upload/" + file.getOriginalFilename();
			entity.setZp(fileName);
		}
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		String sfzjh = entity.getXm();
		String xh = entity.getUserNumb();
		String xjh = entity.getXjh();

		// 判断身份证件号是否重复
		if (thisService.IsFieldExist("sfzjh", sfzjh, "-1")) {
			writeJSON(response, jsonBuilder.returnFailureJson("'身份证件号不能重复！'"));
			return;
		}

		// 判断学号是否重复
		if (thisService.IsFieldExist("xh", xh, "-1")) {
			writeJSON(response, jsonBuilder.returnFailureJson("'学号不能重复！'"));
			return;
		}

		// 判断学籍号是否重复
		if (thisService.IsFieldExist("xjh", xjh, "-1")) {
			writeJSON(response, jsonBuilder.returnFailureJson("'学籍号不能重复！'"));
			return;
		}

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		StuBaseinfo perEntity = new StuBaseinfo();
		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		entity.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		entity.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		entity = thisService.doMerge(entity);

		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	@RequestMapping("/addStudentByRead")
	public @ResponseBody ModelAndView addStudentByRead(String xm, String xbm, String mzm, String csrq, String dz,
			String sfzjh, String sfzjyxq, String zp, String ywxm, String cym, String xmpy, String xh, String sfdszn,
			String sfldrk, String csdm, String sfzjlxm, String gatqwm, String xyzjm, String zzmmm, String jg,
			String gjdqm, String hkszdxzqhm, String hklbm, String hyzkm, String jkzkm, String xxm, String zpbm,
			HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file)
			throws IllegalStateException, IOException {
		StuBaseinfo student = new StuBaseinfo();
		String zp2 = "";

		try {
			zp2 = xm + Math.random();
			if (!file.isEmpty()) {
				file.transferTo(new File(
						request.getSession().getServletContext().getRealPath("/") + "/static/upload/" + zp2 + ".jpg"));
			} else {
				byte[] imgByte = decode(zpbm);
				System.out.println(imgByte);
				InputStream in = new ByteArrayInputStream(imgByte);

				File file1 = new File(request.getSession().getServletContext().getRealPath("/") + "/static/upload/",
						zp2 + ".jpg");// 可以是任何图片格式.jpg,.png等
				FileOutputStream fos = new FileOutputStream(file1);

				byte[] b = new byte[1024];
				int nRead = 0;
				while ((nRead = in.read(b)) != -1) {
					fos.write(b, 0, nRead);
				}
				fos.flush();
				fos.close();
				in.close();
			}

		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}

		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		String fileName = "/static/upload/" + zp2 + ".jpg";
		student.setZp(fileName);
		student.setMzm(mzm);
		student.setXm(xm);
		student.setXbm(xbm);
		//student.setCsrq(csrq);
		student.setSfzjh(sfzjh);
		student.setSfzjyxq(sfzjyxq);
		student.setYwxm(ywxm);
		student.setCym(cym);
		student.setXmpy(xmpy);
		student.setUserNumb(xh);
		student.setSfdszn(sfdszn);
		student.setSfldrk(sfldrk);
		student.setCsdm(csdm);
		student.setSfzjlxm(sfzjlxm);
		student.setGatqwm(gatqwm);
		student.setXyzjm(xyzjm);
		student.setZzmmm(zzmmm);
		student.setJg(jg);
		student.setGjdqm(gjdqm);
		student.setHkszdxzqhm(hkszdxzqhm);
		student.setHklbm(hklbm);
		student.setHyzkm(hyzkm);
		student.setJkzkm(jkzkm);
		student.setXxm(xxm);
		student.setSchoolId(currentUser.getSchoolId());

		// 如果界面有了排序号的输入，则不需要取默认的了
		Integer orderIndex = thisService.getDefaultOrderIndex(student);
		student.setOrderIndex(orderIndex);// 排序

		// 增加时要设置创建人
		student.setCreateUser(userCh); // 创建人
		student = thisService.doMerge(student);

		return new ModelAndView("redirect:/static/core/coreApp/student/studentinfo/view/readSfz.jsp?message=1");
	}

	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bt;
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	/**
	 * doRestore还原删除的记录 @Title: doRestore @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dorestore")
	public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
			return;
		} else {
			boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * StuBaseinfo @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(@RequestParam MultipartFile file, StuBaseinfo entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
		if (!file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/"
					+ file.getOriginalFilename();
			file.transferTo(new File(filePath));
			String fileName = "/static/upload/" + file.getOriginalFilename();
			entity.setZp(fileName);
		}
		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		StuBaseinfo perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh);
		entity = thisService.doMerge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}

	@RequestMapping("/dictionaryMessage")
	public @ResponseBody List<BaseDicitem> dictionaryMessage(String name, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String dicId = "";// 字典ID
		BaseDic dictionary = new BaseDic();
		BaseDicitem dictionaryItem = new BaseDicitem();
		List<BaseDic> dictionaryList = new ArrayList<>();
		List<BaseDicitem> dictionaryItemsList = new ArrayList<>();
		StringBuffer hql = new StringBuffer("from " + dictionary.getClass().getSimpleName());
		if (name != null && "" != name) {
			hql.append(" where nodeText='" + name + "'");

			dictionaryList = dictionaryService.getQuery(hql.toString());
		}

		if (dictionaryList != null && dictionaryList.size() > 0) {
			for (BaseDic dictionary2 : dictionaryList) {
				dicId = dictionary2.getUuid();
			}
		}

		if (dicId != null && !"".equals(dicId)) {
			StringBuffer hqls = new StringBuffer("from " + dictionaryItem.getClass().getSimpleName());

			hqls.append(" where dicId='" + dicId + "' and isDelete='0' order by itemCode asc ");

			dictionaryItemsList = dictionartItemService.getQuery(hqls.toString());
		}
		return dictionaryItemsList;
	}

	// 上传excel
	@RequestMapping("/addExcel")
	public ModelAndView addExcel(@RequestParam MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!file.isEmpty()) {
			// 重命名上传后的文件名
			String myFileName = file.getOriginalFilename();
			String type = myFileName.substring(myFileName.lastIndexOf("."));
			String fileName = String.valueOf(System.currentTimeMillis()) + type;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String url = "/static/upload/file/" + sdf.format(System.currentTimeMillis()) + "/";

			String rootPath = request.getSession().getServletContext().getRealPath("/");
			rootPath = rootPath.replace("\\", "/");

			// 定义上传路径
			String path = rootPath + url + fileName;
			File localFile = new File(path);

			if (!localFile.exists()) { // 判断文件夹是否存在
				localFile.mkdirs(); // 不存在则创建
			}

			file.transferTo(localFile);
			excelImport(path, request, response);
			/*
			 * ModelMap model = new ModelMap(); model.put("filePath",path);
			 * return new ModelAndView("redirect:/StuBaseinfo/doimport",model);
			 */
		}
		return null;
	}

	// 导入数据库
	// @RequestMapping("/doimport")
	public void excelImport(String filePath, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			// 获取当前的操作用户
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();

			// 1、创建excel导入工具
			ExcelUtil excelUtil = new ExcelUtil();
			// 2、创建时间格式
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss");
			// 3、通过工具获取教师数据
			List<List<String>> studentData = excelUtil.importExcel(filePath);
			// 4、创建教师集合
			List<String> studentist = null;
			// 5、创建教师对象
			StuBaseinfo entity = null;

			BaseJob job = jobService.get("8a8a88245629ec06015629f56ecd0000");

			// 6、通过循环将数据放入教师集合中，从集合中获取教师对象，一一设置属性
			for (int i = 0; i < studentData.size(); i++) {
				studentist = studentData.get(i);
				String stuId = studentist.get(5);
				if (stuId.equals("NULL")) {
					entity = new StuBaseinfo();
				} else {
					entity = new StuBaseinfo(stuId);
				}
				entity.setUserNumb(studentist.get(0));
				entity.setXm(studentist.get(1));
				String sex = studentist.get(2).equals("男") ? "1" : "2";
				entity.setXbm(sex);
				entity.setSfzjh(studentist.get(3));
				String idCard = studentist.get(3);
				String tmpStr;
				if (StringUtils.isEmpty(idCard)) {
					tmpStr = "";
				} else {
					tmpStr = idCard.length() == 15 ? "19" + idCard.substring(6, 12) : idCard.substring(6, 14);
					tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6);
				}

				//entity.setCsrq(tmpStr);

				String className = studentist.get(4);
				int gcStrLength = className.length();
				String grade = className.substring(0, 2);
				String gcName;
				if (gcStrLength == 4) {
					String classNum = className.substring(2, 3);
					gcName = grade + "（" + classNum + "）" + "班";
				} else {
					String classNum = className.substring(2, 4);
					gcName = grade + "（" + classNum + "）" + "班";
				}
				String andIsDelete = " and isDelete=0 ";
				String hql = "from JwTGradeclass where className='" + gcName + "'" + andIsDelete;
				JwTGradeclass gc = gcService.getQuery(hql).get(0);

				entity.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
				entity.setSchoolName("深大附中");
				entity.setStudentState("2");
				entity.setClassId(gc.getUuid());
				BaseOrg org = orgService.get(gc.getUuid());

				// 增加时要设置创建人
				entity.setCreateUser(userCh); // 创建人
				entity.setUserName(PinyinUtil.toPinYin(entity.getXm()));
				entity.setUserPwd(new Sha256Hash("123456").toHex());
				entity.setCategory("2");

				SysUser user = new SysUser(entity.getUuid());
				user.setXm(entity.getXm());
				// String sql = "select [dbo].fn_Getquanpin3 ('" +
				// entity.getXm() + "')";
				// List<Object[]> list = thisService.ObjectQuerySql(sql);
				// String name = list.get(0) + "";
				// user.setUserName(PinyinUtil.toPinYin(entity.getXm()));

				// user.setCategory("2");
				// entity.setJobId("8a8a88245629ec06015629f56ecd0000");
				// entity.setJobName("学生");
				// entity.setDeptId("058b21fe-b37f-41c9-ad71-091f97201ff8");
				// entity.setDeptName("临时部门");
				//entity.getUserJobs().add(job);
				//entity.getUserDepts().add(org);
				entity.setState("0");
				entity.setIsDelete(0);
				entity.setIsHidden("0");
				entity.setIssystem(0);

				// 7、存入数据库
				userService.doMerge(entity);
				userService.addUserRole(entity.getUuid(), "0309D5D0-2BD8-454B-9A24-46A8E5AAF655", currentUser);
				
				JwClassstudent cStu=new JwClassstudent();
				cStu.setClaiId(gc.getUuid());
				cStu.setCreateUser(userCh);
				cStu.setStudyYeah("2016");
				cStu.setSemester("2");
				cStu.setStudentId(entity.getUuid());
				classStudentService.doMerge(cStu);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'上传成功'"));
		// return new
		// ModelAndView("redirect:/static/core/coreApp/arrangecourse/Invigilate/iframe/uploadzp.jsp");
	}

}
