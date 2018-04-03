
package com.zd.school.financial.financial.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zd.core.util.ModelUtil;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.salary.salary.service.XcSalarybookitemService;
import com.zd.school.salary.salary.service.XcSalarybooksalaryService;
import com.zd.school.salary.salary.service.XcSalaryplatitemService;
import com.zd.school.financial.financial.model.CwFinancialbook;
import com.zd.school.financial.financial.model.CwFinancialbookitem;
import com.zd.school.financial.financial.model.CwFinancialbooks ;
import com.zd.school.financial.financial.dao.CwFinancialbooksDao ;
import com.zd.school.financial.financial.service.CwFinancialbookService;
import com.zd.school.financial.financial.service.CwFinancialbookitemService;
import com.zd.school.financial.financial.service.CwFinancialbooksService ;
import com.zd.school.jw.controller.util.ExcelUtil;

/**
 * 
 * ClassName: CwFinancialbooksController
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: (CW_T_FINANCIALBOOKS)实体Controller.
 * date: 2017-02-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/CwFinancialbooks")
public class CwFinancialbooksController extends FrameWorkController<CwFinancialbooks> implements Constant {

    @Resource
    CwFinancialbooksService thisService; // service层接口
    
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

    /**
      * @Title: list
      * @Description: 查询数据列表
      * @param entity 实体类
      * @param request
      * @param response
      * @throws IOException    设定参数
      * @return void    返回类型
     */
	@RequestMapping("/importSalary")
	public void importSalary(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String hql="delete CwFinancialbooks";
		thisService.doExecuteHql(hql);
		
		if (!file.isEmpty()) {
			SysUser currentUser = getCurrentSysUser();
			String userCh = currentUser.getXm();
			ExcelUtil excelUtil = new ExcelUtil();
			List<List<String>> financialData = excelUtil.importExcel(file);
			List<String> financialList = null;
			CwFinancialbooks entity = null;
			
			BigDecimal lastyear=null;
			BigDecimal thisyear=null;
			BigDecimal one=null;
			BigDecimal two=null;
			BigDecimal three=null;
			BigDecimal four=null;
			BigDecimal five=null;
			BigDecimal six=null;
			BigDecimal seven=null;
			BigDecimal eight=null;
			BigDecimal nine=null;
			BigDecimal ten=null;
			BigDecimal eleven=null;
			BigDecimal twelve=null;
			BigDecimal totals=null;
			BigDecimal balance=null;
			
			//项目资金总和
			BigDecimal sun=null;
			//消费合计
			BigDecimal num=null;
			
			
			for (int i = 0; i < financialData.size(); i++) {
				financialList = financialData.get(i);
				entity = new CwFinancialbooks();

				sun=new BigDecimal(0);
				num=new BigDecimal(0);
				
				// 设置基本属性
				entity.setCreateUser(userCh);
				entity.setCreateTime(new Date());
				entity.setProjects(financialList.get(0));
				entity.setPropertiess(financialList.get(1));
				try{
					lastyear=new BigDecimal(financialList.get(2));
				}catch(Exception e){
					lastyear=new BigDecimal(0);
				}
				entity.setLastYear(lastyear);
				try{
					thisyear=new BigDecimal(financialList.get(3));
				}catch(Exception e){
					thisyear=new BigDecimal(0);
				}
				entity.setThisYear(thisyear);
				
				try{
					one=new BigDecimal(financialList.get(4));
				}catch(Exception e){
					one=new BigDecimal(0);
				}
				entity.setOne(one);
				try{
					two=new BigDecimal(financialList.get(5));
				}catch(Exception e){
					two=new BigDecimal(0);
				}
				entity.setTwo(two);
				try{
					three=new BigDecimal(financialList.get(6));
				}catch(Exception e){
					three=new BigDecimal(0);
				}
				entity.setThree(three);
				try{
					four=new BigDecimal(financialList.get(7));
				}catch(Exception e){
					four=new BigDecimal(0);
				}
				entity.setFour(four);
				try{
					five=new BigDecimal(financialList.get(8));
				}catch(Exception e){
					five=new BigDecimal(0);
				}
				entity.setFive(five);
				try{
					six=new BigDecimal(financialList.get(9));
				}catch(Exception e){
					six=new BigDecimal(0);
				}
				entity.setSix(six);
				try{
					seven=new BigDecimal(financialList.get(10));
				}catch(Exception e){
					seven=new BigDecimal(0);
				}
				entity.setSeven(seven);
				try{
					eight=new BigDecimal(financialList.get(11));
				}catch(Exception e){
					eight=new BigDecimal(0);
				}
				entity.setEight(eight);
				try{
					nine=new BigDecimal(financialList.get(12));
				}catch(Exception e){
					nine=new BigDecimal(0);
				}
				entity.setNine(nine);
				try{
					ten=new BigDecimal(financialList.get(13));
				}catch(Exception e){
					ten=new BigDecimal(0);
				}
				entity.setTen(ten);
				try{
					eleven=new BigDecimal(financialList.get(14));
				}catch(Exception e){
					eleven=new BigDecimal(0);
				}
				entity.setEleven(eleven);
				try{
					twelve=new BigDecimal(financialList.get(15));
				}catch(Exception e){
					twelve=new BigDecimal(0);
				}
				entity.setTwelve(twelve);
				
				sun=sun.add(lastyear.add(thisyear));
				num=num.add(one.add(two.add(three.add(four.add(five.add(six.add(seven.add(eight.add(nine.add(ten.add(eleven.add(twelve))))))))))));
				
				try{
					totals=new BigDecimal(financialList.get(16));
				}catch(Exception e){
					totals=new BigDecimal(0);
				}
				entity.setTotals(totals);
				balance=sun.subtract(num);

				entity.setBalance(balance);


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
				thisService.doMerge(entity);
			}
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson("'工资数据导入成功'")));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'工资数据导入失败'")));
		}
		
	}
	
	
		@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
				org.springframework.web.bind.annotation.RequestMethod.POST })
			public @ResponseBody List<CwFinancialbooks> list(HttpServletRequest request, HttpServletResponse response)
				throws IOException {
			String strData = ""; // 返回给js的数据
			List<CwFinancialbooks> list=new ArrayList<>();
			
			BigDecimal lastyear=new BigDecimal(0);
			BigDecimal thisyear=new BigDecimal(0);
			BigDecimal one=new BigDecimal(0);
			BigDecimal two=new BigDecimal(0);
			BigDecimal three=new BigDecimal(0);
			BigDecimal four=new BigDecimal(0);
			BigDecimal five=new BigDecimal(0);
			BigDecimal six=new BigDecimal(0);
			BigDecimal seven=new BigDecimal(0);
			BigDecimal eight=new BigDecimal(0);
			BigDecimal nine=new BigDecimal(0);
			BigDecimal ten=new BigDecimal(0);
			BigDecimal eleven=new BigDecimal(0);
			BigDecimal twelve=new BigDecimal(0);
			BigDecimal totals=new BigDecimal(0);
			BigDecimal balance=new BigDecimal(0);
			
			
			String querySql=request.getParameter("querySql");
			CwFinancialbooks book=new CwFinancialbooks();
			String hql="";
			if (querySql!= null &&!querySql.equals("")) {
				hql="from CwFinancialbooks where 1=1 "+querySql+" order by createTime asc";
			}else{
				hql="from CwFinancialbooks order by createTime asc";
			}
			List<CwFinancialbooks> lists=thisService.getQuery(hql);
			for(int i=0;i<lists.size();i++){
				 lastyear=lastyear.add(lists.get(i).getLastYear());
				 thisyear=thisyear.add(lists.get(i).getThisYear());
				 one=one.add(lists.get(i).getOne());
				 two=two.add(lists.get(i).getTwo());
				 three=three.add(lists.get(i).getThree());
				 four=four.add(lists.get(i).getFour());
				 five=five.add(lists.get(i).getFive());
				 six=six.add(lists.get(i).getSix());
				 seven=seven.add(lists.get(i).getSeven());
				 eight=eight.add(lists.get(i).getEight());
				 nine=nine.add(lists.get(i).getNine());
				 ten=ten.add(lists.get(i).getTen());
				 eleven=eleven.add(lists.get(i).getEleven());
				 twelve=twelve.add(lists.get(i).getTwelve());
				 totals=totals.add(lists.get(i).getTotals());
				 balance=balance.add(lists.get(i).getBalance());	
			}
			
			book.setProjects("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合&nbsp;&nbsp;&nbsp;计");
			book.setPropertiess("");
			book.setLastYear(lastyear);
			book.setThisYear(thisyear);
			book.setOne(one);
			book.setTwo(two);
			book.setThree(three);
			book.setFour(four);
			book.setFive(five);
			book.setSix(six);
			book.setSeven(seven);
			book.setEight(eight);
			book.setNine(nine);
			book.setTen(ten);
			book.setEleven(eleven);
			book.setTwelve(twelve);
			book.setTotals(totals);
			book.setBalance(balance);
			lists.add(book);
			
			return lists;
			}
			


    /**
     * 
      * @Title: doadd
      * @Description: 增加新实体信息至数据库
      * @param CwFinancialbooks 实体类
      * @param request
      * @param response
      * @return void    返回类型
      * @throws IOException    抛出异常
     */
    @RequestMapping("/doadd")
    public void doAdd(CwFinancialbooks entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
		//此处为放在入库前的一些检查的代码，如唯一校验等
		
		//获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		}
    }

    /**
      * 
      * @Title: doDelete
      * @Description: 逻辑删除指定的数据
      * @param request
      * @param response
      * @return void    返回类型
      * @throws IOException  抛出异常
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
     * @Title: doUpdate
     * @Description: 编辑指定记录
     * @param CwFinancialbooks
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException  抛出异常
    */
    @RequestMapping("/doupdate")
    public void doUpdates(CwFinancialbooks entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		
		//获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		}
    }
}
