
package com.zd.school.financial.financial.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.financial.financial.model.CwFinancialbook;
import com.zd.school.financial.financial.model.CwFinancialbookitem;
import com.zd.school.financial.financial.service.CwFinancialbookService;
import com.zd.school.financial.financial.service.CwFinancialbookitemService;
import com.zd.school.jw.controller.util.ExcelUtil;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.salary.salary.model.XcSalarybook;
import com.zd.school.salary.salary.model.XcSalarybookitem;
import com.zd.school.salary.salary.model.XcSalarybooksalary;
import com.zd.school.salary.salary.model.XcSalaryplatitem;
import com.zd.school.salary.salary.service.XcSalarybookService;
import com.zd.school.salary.salary.service.XcSalarybookitemService;
import com.zd.school.salary.salary.service.XcSalarybooksalaryService;
import com.zd.school.salary.salary.service.XcSalaryplatitemService;

/**
 * 
 * ClassName: XcSalarybookController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 工资台账表(XC_T_SALARYBOOK)实体Controller. date:
 * 2016-12-05
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/CwFinancialbook")
public class CwFinancialbookController extends FrameWorkController<CwFinancialbook> implements Constant {

	@Resource
	private CwFinancialbookService thisService; // service层接口
	@Resource
	private CwFinancialbookitemService itemService;
	
	@Resource
	private CommTreeService treeService;
	@Resource
	private XcSalarybookitemService bookitemService;
	@Resource
	private XcSalaryplatitemService plartitemService;
	@Resource
	private SysUserService userService;
	@Resource
	private XcSalarybooksalaryService booksalaryService;

/*	@RequestMapping("/getSalaryItems")
	public @ResponseBody List<XcSalarybooksalary> getSalaryItems(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String salarybookId = request.getParameter("salarybookId");
		// XcSalarybook book = thisService.get(salarybookId);
		String hql = "from XcSalarybooksalary where salarybookId='" + salarybookId + "' order by orderIndex";
		List<XcSalarybooksalary> list = booksalaryService.doQuery(hql);
		List<XcSalarybooksalary> list2 = new ArrayList<XcSalarybooksalary>();
		for (XcSalarybooksalary temp : list) {
			XcSalarybooksalary entity = new XcSalarybooksalary();
			entity.setExtField01(temp.getSalaryitemName());
			entity.setExtField02("gz" + (temp.getOrderIndex() - 1));
			list2.add(entity);
		}
		return list2;
	}*/

	@RequestMapping("/importSalary")
	public void importSalary(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!file.isEmpty()) {
			String financialbookId = request.getParameter("financialbookId");
			CwFinancialbook book = thisService.get(financialbookId);
			Integer xcYear = book.getXcYear();
			Integer xcMonth = book.getXcMonth();
			SysUser currentUser = getCurrentSysUser();
			String userCh = currentUser.getXm();
			ExcelUtil excelUtil = new ExcelUtil();
			List<List<String>> financialData = excelUtil.importExcel(file);
			List<String> financialList = null;
			CwFinancialbookitem entity = null;
			BigDecimal expend=null;
			BigDecimal balance=null;
			for (int i = 0; i < financialData.size(); i++) {
				financialList = financialData.get(i);
				entity = new CwFinancialbookitem();

				// 设置基本属性
				entity.setCreateUser(userCh);
				entity.setCreateTime(new Date());
				entity.setXcYear(xcYear);
				entity.setXcMonth(xcMonth);
				entity.setJyrq(financialList.get(0));
				entity.setAbstracts(financialList.get(1));
				entity.setProject(financialList.get(2));
				System.out.println(financialList.get(3));
				expend=new BigDecimal(financialList.get(3));
				entity.setExpend(expend);
				balance=new BigDecimal(financialList.get(4));
				entity.setBalance(balance);
				entity.setFinancialbookId(financialbookId);

/*				*//**
				 * 反射 设置工资 因为职务工资在gz0 所以取得下标减1
				 *//*
				Class clazz = entity.getClass();
				for (String key : map.keySet()) {
					Integer index = map.get(key);
					String methodName = "setGz" + (index - 1);
					Method method = clazz.getDeclaredMethod(methodName, BigDecimal.class);
					BigDecimal gz;
					try {
						gz = new BigDecimal(salaryList.get(index+1));
					} catch (Exception e) {
						gz = null;
					}
					method.invoke(entity, gz);
				}*/
				itemService.merge(entity);
			}
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson("'工资数据导入成功'")));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'工资数据导入失败'")));
		}
	}

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
/*	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute XcSalarybook entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<XcSalarybook> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql,
				currentUser);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}*/

/*	@RequestMapping(value = { "/getBysalarybookId" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody XcSalarybook getBysalarybookId(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String salarybookId = request.getParameter("salarybookId");
		XcSalarybook entity = thisService.get(salarybookId);
		return entity;
	}*/

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param XcSalarybook
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(CwFinancialbook entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		CwFinancialbook perEntity = new CwFinancialbook();
		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
		// 生成默认的orderindex
		// 如果界面有了排序号的输入，则不需要取默认的了
		// Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		// entity.setOrderIndex(orderIndex);//排序

		// 增加时要设置创建人
		entity.setCreateUser(userCh); // 创建人

		// 持久化到数据库
		entity = thisService.merge(entity);


		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
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
			CwFinancialbook entity = thisService.get(delIds);
			if (entity.getIspushed()==1) {
				writeJSON(response, jsonBuilder.returnFailureJson("'当前流水已经发布不能删除'"));
			}else {
				itemService.deleteByProperties("financialbookId", delIds);
				boolean flag = thisService.deleteByPK(delIds);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
				}
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
			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * XcSalarybook @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(CwFinancialbook entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		CwFinancialbook perEntity = thisService.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		entity = thisService.merge(perEntity);// 执行修改方法

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}
	
	@RequestMapping("/doPush")
	public void doPush( HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String delIds = request.getParameter("ids");
		
		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		CwFinancialbook perEntity = thisService.get(delIds);

	
		perEntity.setIspushed(1);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		perEntity = thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'发布成功'"));
		

	}

	@RequestMapping("/getBookTree")
	public void getBookTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");

		List<CommTree> lists = treeService.getCommTree("CW_V_FINANCIALBOOK", whereSql);

		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

/*	@RequestMapping("/getUserBookTree")
	public void getUserBookTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SysUser user = getCurrentSysUser();
		String strData = "";
		List<CommTree> lists = thisService.getUserBookTree(user);

		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}*/
}
