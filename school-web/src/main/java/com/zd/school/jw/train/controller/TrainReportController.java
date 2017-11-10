package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.BaseEntity;
import com.zd.core.service.BaseService;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.PoiExportExcel;
import com.zd.school.cashier.model.CashExpensedetail;
import com.zd.school.cashier.service.CashExpensedetailService;


@Controller
@RequestMapping("/TrainReport")
public class TrainReportController extends FrameWorkController<BaseEntity> implements Constant {

    @Resource
    BaseService<BaseEntity> baseService; // service层接口
    
    @Resource
    CashExpensedetailService cashExpensedetailService; // service层接口
    
    /**
	 * 收银汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getCashTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
		
		String consumeSerial = request.getParameter("CONSUME_SERIAL");
		consumeSerial=consumeSerial==null?"":consumeSerial;
		String consumeUser = request.getParameter("CONSUME_USER");
		consumeUser=consumeUser==null?"":consumeUser;
		String consumeType = request.getParameter("CONSUME_TYPE");
		consumeType=consumeType==null?"":consumeType;
		String receptionDept = request.getParameter("RECEPTION_DEPT");
		receptionDept=receptionDept==null?"":receptionDept;
		String visitorUnit = request.getParameter("VISITOR_UNIT");
		visitorUnit=visitorUnit==null?"":visitorUnit;
		String consumeState = request.getParameter("CONSUME_STATE");
		consumeState=consumeState==null?"":consumeState;
		String clearingForm = request.getParameter("CLEARING_FORM");
		clearingForm=clearingForm==null?"":clearingForm;
		String mealType = request.getParameter("MEAL_TYPE");
		mealType=mealType==null?"":mealType;
		String groupType = request.getParameter("GROUP_TYPE");
		groupType=groupType==null?"":groupType;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_CashReportSearch] ");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");
		sql.append("'"+consumeSerial+"',");
		sql.append("'"+consumeUser+"',");
		sql.append("'"+receptionDept+"',");
		sql.append("'"+visitorUnit+"',");
		sql.append("'',");
		sql.append("'"+consumeType+"',");
		sql.append("'"+consumeState+"',");
		sql.append("'"+clearingForm+"',");
		sql.append("'"+mealType+"',");
		sql.append("'"+groupType+"',");
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("CashTotalDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getCashTotalDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("CashTotalDatas");
		String strData = JsonBuilder.getInstance().toJson(obj);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据	
	}
	
	/**
	 * 导出就餐汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCashTotalExcel")
	public void exportCashTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportCashTotalIsEnd", "0");
		request.getSession().removeAttribute("exportCashTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
			
			String consumeSerial = request.getParameter("CONSUME_SERIAL");
			consumeSerial=consumeSerial==null?"":consumeSerial;
			String consumeUser = request.getParameter("CONSUME_USER");
			consumeUser=consumeUser==null?"":consumeUser;
			String consumeType = request.getParameter("CONSUME_TYPE");
			consumeType=consumeType==null?"":consumeType;
			String receptionDept = request.getParameter("RECEPTION_DEPT");
			receptionDept=receptionDept==null?"":receptionDept;
			String visitorUnit = request.getParameter("VISITOR_UNIT");
			visitorUnit=visitorUnit==null?"":visitorUnit;
			String consumeState = request.getParameter("CONSUME_STATE");
			consumeState=consumeState==null?"":consumeState;
			String clearingForm = request.getParameter("CLEARING_FORM");
			clearingForm=clearingForm==null?"":clearingForm;
			String mealType = request.getParameter("MEAL_TYPE");
			mealType=mealType==null?"":mealType;
			String groupType = request.getParameter("GROUP_TYPE");
			groupType=groupType==null?"":groupType;
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_CashReportSearch] ");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");
			sql.append("'"+consumeSerial+"',");
			sql.append("'"+consumeUser+"',");
			sql.append("'"+receptionDept+"',");
			sql.append("'"+visitorUnit+"',");
			sql.append("'',");
			sql.append("'"+consumeType+"',");
			sql.append("'"+consumeState+"',");
			sql.append("'"+clearingForm+"',");
			sql.append("'"+mealType+"',");
			sql.append("'"+groupType+"',");
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);	
			Map<String, Object> totalMap=lists.get(0);
			lists.remove(0);
					
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> cashMap : lists) {
				
				Object cf = cashMap.get("CLEARING_FORM");
				if("1".equals(cf.toString())){
					cf="现金";
				}else if("2".equals(cf.toString())){
					cf="刷卡";
				}else if("3".equals(cf.toString())){ 
					cf="签单";
				}				
				
				Object mt = cashMap.get("MEAL_TYPE");
				if("1".equals(mt.toString())){
					mt="早餐";
				}else if("2".equals(mt.toString())){
					mt="午餐";
				}else if("3".equals(mt.toString())){ 
					mt="晚餐";
				}else if("4".equals(mt.toString())){ 
					mt="夜宵";
				}
			
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(cashMap.get("rownum")));
				tempMap.put("CONSUME_DATE", String.valueOf(cashMap.get("CONSUME_DATE")));
				tempMap.put("CONSUME_SERIAL", String.valueOf(cashMap.get("CONSUME_SERIAL")));
				tempMap.put("CONSUME_USER", String.valueOf(cashMap.get("CONSUME_USER")));
				tempMap.put("RECEPTION_DEPT", String.valueOf(cashMap.get("RECEPTION_DEPT")));
				tempMap.put("VISITOR_UNIT", String.valueOf(cashMap.get("VISITOR_UNIT")));
				tempMap.put("CONSUME_TYPE", String.valueOf(cashMap.get("CONSUME_TYPE")));
				tempMap.put("CONSUME_STATE", String.valueOf(cashMap.get("CONSUME_STATE")));
				tempMap.put("CLEARING_FORM", String.valueOf(cf));
				tempMap.put("MEAL_TYPE", String.valueOf(mt));
				//tempMap.put("CASH_NUMBER", String.valueOf(cashMap.get("CASH_NUMBER")));
				tempMap.put("CONSUME_TOTAL", String.valueOf(cashMap.get("CONSUME_TOTAL")));
				tempMap.put("REAL_PAY", String.valueOf(cashMap.get("REAL_PAY")));
				//tempMap.put("CHANGE_PAY", String.valueOf(cashMap.get("CHANGE_PAY")));
				exportList.add(tempMap);
			}
			
			//最后一行加入汇总
			tempMap=new LinkedHashMap<>();
			tempMap.put("rownum","");
			tempMap.put("CONSUME_DATE", "");
			tempMap.put("CONSUME_SERIAL","");
			tempMap.put("CONSUME_USER", "");
			tempMap.put("RECEPTION_DEPT", "");
			tempMap.put("VISITOR_UNIT", "");
			tempMap.put("CONSUME_TYPE", "");
			tempMap.put("CONSUME_STATE", "");
			tempMap.put("CLEARING_FORM","");
			tempMap.put("MEAL_TYPE", "汇总");
			//tempMap.put("CASH_NUMBER", String.valueOf(cashMap.get("CASH_NUMBER")));
			tempMap.put("CONSUME_TOTAL", String.valueOf(totalMap.get("CONSUME_TOTAL")));
			tempMap.put("REAL_PAY", String.valueOf(totalMap.get("REAL_PAY")));
			//tempMap.put("CHANGE_PAY", String.valueOf(cashMap.get("CHANGE_PAY")));
			exportList.add(tempMap);
			
			// --------2.组装表格数据
			Map<String, Object> cashAllMap = new LinkedHashMap<>();
			cashAllMap.put("data", exportList);
			cashAllMap.put("title", "收银汇总表");
			cashAllMap.put("head", new String[] { "序号","日期", "流水号", "消费人", "部门", "单位", "消费类型","交易状态","结算方式","快餐类型","消费总额","实付金额"}); // 规定名字相同的，设定为合并
			cashAllMap.put("columnWidth", new Integer[] { 10, 22, 22, 10, 12, 12, 10,10, 10, 10,10,10}); // 30代表30个字节，15个字符
			cashAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0}); // 0代表居中，1代表居左，2代表居右
			cashAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(cashAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "收银汇总信息", "收银汇总信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportCashTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportCashTotalIsEnd", "0");
				request.getSession().setAttribute("exportCashTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportCashTotalIsEnd", "0");
			request.getSession().setAttribute("exportCashTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportCashTotalEnd")
	public void checkExportCashTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportCashTotalIsEnd");
		Object state = request.getSession().getAttribute("exportCashTotalIsState");
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
	
	
	/**
	 * 导出流水明细汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCashDetailExcel")
	public void exportCashDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportCashDetailIsEnd", "0");
		request.getSession().removeAttribute("exportCashDetailIsState");
		
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String consumeSerial=request.getParameter("consumeSerial");
			String expenseserialId=request.getParameter("expenseserialId");
			String hql="from CashExpensedetail a where a.isDelete=0 and a.expenseserialId=?";
			List<CashExpensedetail>  lists=cashExpensedetailService.getForValues(hql, expenseserialId);
			
			
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;		
			for (int i=0;i<lists.size();i++) {
				CashExpensedetail cashDetail=lists.get(i);
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum",String.valueOf(i+1));
				tempMap.put("detailName", String.valueOf(cashDetail.getDetailName()));
				tempMap.put("detailCount", String.valueOf(cashDetail.getDetailCount()));
				tempMap.put("detailPrice", String.valueOf(cashDetail.getDetailPrice()));
				tempMap.put("countPrice", String.valueOf(cashDetail.getDetailPrice().multiply(cashDetail.getDetailPrice())));	
				exportList.add(tempMap);
			}
			// --------2.组装表格数据
			Map<String, Object> cashAllMap = new LinkedHashMap<>();
			cashAllMap.put("data", exportList);
			cashAllMap.put("title", "流水号【"+consumeSerial+"】收银明细表");
			cashAllMap.put("head", new String[] { "序号","消费项目名称","数量","单价","总价"  }); // 规定名字相同的，设定为合并
			cashAllMap.put("columnWidth", new Integer[] { 10, 22, 15, 15, 20, }); // 30代表30个字节，15个字符
			cashAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
			cashAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(cashAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "收银明细信息", "收银明细信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportCashDetailIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportCashDetailIsEnd", "0");
				request.getSession().setAttribute("exportCashDetailIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportCashDetailIsEnd", "0");
			request.getSession().setAttribute("exportCashDetailIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkCashDetailEnd")
	public void checkCashDetailEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportCashDetailIsEnd");
		Object state = request.getSession().getAttribute("exportCashDetailIsState");
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
	
	
	
	/**
	 * 教师消费汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getTeacherConsumeTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTeacherConsumeTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
		
		String employeeName = request.getParameter("EmployeeName");
		employeeName=employeeName==null?"":employeeName;
		String employeeStrID = request.getParameter("EmployeeStrID");
		employeeStrID=employeeStrID==null?"":employeeStrID;
		String termName = request.getParameter("TermName");
		termName=termName==null?"":termName;
		String accountName = request.getParameter("AccountName");
		accountName=accountName==null?"":accountName;
		String mealTypeID = request.getParameter("MealTypeID");
		mealTypeID=mealTypeID==null?"":mealTypeID;
		String groupType = request.getParameter("GROUP_TYPE");
		groupType=groupType==null?"":groupType;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TeacherConsumeReportSearch] ");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");
		sql.append("'"+employeeName+"',");
		sql.append("'"+employeeStrID+"',");
		sql.append("'"+termName+"',");
		sql.append("'"+accountName+"',");
		sql.append("'"+mealTypeID+"',");
		sql.append("'"+groupType+"',");
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("TeacherConsumeTotalDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getTeacherConsumeTotalDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTeacherConsumeTotalDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("TeacherConsumeTotalDatas");
		String strData = JsonBuilder.getInstance().toJson(obj);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据	
	}
	
	/**
	 * 导出教师消费汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportTeacherConsumeTotalExcel")
	public void exportTeacherConsumeTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTeacherConsumeTotalIsEnd", "0");
		request.getSession().removeAttribute("exportTeacherConsumeTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
			
			String employeeName = request.getParameter("EmployeeName");
			employeeName=employeeName==null?"":employeeName;
			String employeeStrID = request.getParameter("EmployeeStrID");
			employeeStrID=employeeStrID==null?"":employeeStrID;
			String termName = request.getParameter("TermName");
			termName=termName==null?"":termName;
			String accountName = request.getParameter("AccountName");
			accountName=accountName==null?"":accountName;
			String mealTypeID = request.getParameter("MealTypeID");
			mealTypeID=mealTypeID==null?"":mealTypeID;
			String groupType = request.getParameter("GROUP_TYPE");
			groupType=groupType==null?"":groupType;
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TeacherConsumeReportSearch] ");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");
			sql.append("'"+employeeName+"',");
			sql.append("'"+employeeStrID+"',");
			sql.append("'"+termName+"',");
			sql.append("'"+accountName+"',");
			sql.append("'"+mealTypeID+"',");
			sql.append("'"+groupType+"',");
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);	
			Map<String, Object> totalMap=lists.get(0);
			lists.remove(0);
					
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> cashMap : lists) {
			
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(cashMap.get("rownum")));
				tempMap.put("ConsumeDate", String.valueOf(cashMap.get("ConsumeDate")));
				tempMap.put("EmployeeName", String.valueOf(cashMap.get("EmployeeName")));
				tempMap.put("EmployeeStrID", String.valueOf(cashMap.get("EmployeeStrID")));
				tempMap.put("TermName", String.valueOf(cashMap.get("TermName")));
				tempMap.put("MealTypeName", String.valueOf(cashMap.get("MealTypeName")));
				tempMap.put("AccountName", String.valueOf(cashMap.get("AccountName")));				
				tempMap.put("ConsumeValue", String.valueOf(cashMap.get("ConsumeValue")));
				tempMap.put("ConsumeNumber", String.valueOf(cashMap.get("ConsumeNumber")));		
				exportList.add(tempMap);
			}
			//最后一行加入汇总
			tempMap=new LinkedHashMap<>();
			tempMap.put("rownum", "");
			tempMap.put("ConsumeDate","");
			tempMap.put("EmployeeName", "");
			tempMap.put("EmployeeStrID", "");
			tempMap.put("TermName", "");
			tempMap.put("MealTypeName", "");
			tempMap.put("AccountName", "汇总");			
			tempMap.put("ConsumeValue", String.valueOf(totalMap.get("ConsumeValue")));
			tempMap.put("ConsumeNumber", String.valueOf(totalMap.get("ConsumeNumber")));		
			exportList.add(tempMap);
			
			// --------2.组装表格数据
			Map<String, Object> cashAllMap = new LinkedHashMap<>();
			cashAllMap.put("data", exportList);
			cashAllMap.put("title", "教职工消费汇总表");
			cashAllMap.put("head", new String[] { "序号","日期", "人员姓名", "人员编号", "消费设备", "就餐类型", "结算账户名","消费金额","消费笔数" }); // 规定名字相同的，设定为合并
			cashAllMap.put("columnWidth", new Integer[] { 10, 22, 15, 15, 15, 15,15,20,10 }); // 30代表30个字节，15个字符
			cashAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			cashAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(cashAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "教职工消费汇总表", "教职工消费汇总表", allList);
			if (result == true) {
				request.getSession().setAttribute("exportTeacherConsumeTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTeacherConsumeTotalIsEnd", "0");
				request.getSession().setAttribute("exportTeacherConsumeTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTeacherConsumeTotalIsEnd", "0");
			request.getSession().setAttribute("exportTeacherConsumeTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportTeacherConsumeTotalEnd")
	public void checkExportTeacherConsumeTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTeacherConsumeTotalIsEnd");
		Object state = request.getSession().getAttribute("exportTeacherConsumeTotalIsState");
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
	
	
	
	/**
	 * 班级学员消费汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getTraineeConsumeTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTraineeConsumeTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
		
		String className = request.getParameter("CLASS_NAME");
		className=className==null?"":className;
		String classNumb = request.getParameter("CLASS_NUMB");
		classNumb=classNumb==null?"":classNumb;
		String dinnerType = request.getParameter("DINNER_TYPE");
		dinnerType=dinnerType==null?"":dinnerType;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_ClassConsumeReportSearch] ");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");
		sql.append("'"+className+"',");
		sql.append("'"+classNumb+"',");
		sql.append("'"+dinnerType+"',");
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("TraineeConsumeTotalDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getTraineeConsumeTotalDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTraineeConsumeTotalDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("TraineeConsumeTotalDatas");
		String strData = JsonBuilder.getInstance().toJson(obj);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据	
	}
	
	/**
	 * 导出班级学员消费汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportTraineeConsumeTotalExcel")
	public void exportTraineeConsumeTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTraineeConsumeTotalIsEnd", "0");
		request.getSession().removeAttribute("exportTraineeConsumeTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
			
			String className = request.getParameter("CLASS_NAME");
			className=className==null?"":className;
			String classNumb = request.getParameter("CLASS_NUMB");
			classNumb=classNumb==null?"":classNumb;
			String dinnerType = request.getParameter("DINNER_TYPE");
			dinnerType=dinnerType==null?"":dinnerType;
		
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_ClassConsumeReportSearch] ");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");
			sql.append("'"+className+"',");
			sql.append("'"+classNumb+"',");
			sql.append("'"+dinnerType+"',");
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);		
			Map<String, Object> totalMap=lists.get(0);
			lists.remove(0);
					
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> cashMap : lists) {
				
				Object dt = cashMap.get("DINNER_TYPE");
				if("1".equals(dt.toString())){
					dt="围餐";
				}else if("2".equals(dt.toString())){
					dt="自助餐";
				}else if("3".equals(dt.toString())){ 
					dt="快餐";
				}	
				String bd=sdf.format(cashMap.get("BEGIN_DATE"));
				String ed=sdf.format(cashMap.get("END_DATE"));
				
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(cashMap.get("rownum")));
				tempMap.put("CLASS_NAME", String.valueOf(cashMap.get("CLASS_NAME")));
				tempMap.put("CLASS_NUMB", String.valueOf(cashMap.get("CLASS_NUMB")));
				tempMap.put("BEGIN_DATE",bd );
				tempMap.put("END_DATE", ed);
				tempMap.put("DINNER_TYPE", String.valueOf(dt));
				tempMap.put("BREAKFAST_COUNT", String.valueOf(cashMap.get("BREAKFAST_COUNT")));				
				tempMap.put("LUNCH_COUNT", String.valueOf(cashMap.get("LUNCH_COUNT")));
				tempMap.put("DINNER_COUNT", String.valueOf(cashMap.get("DINNER_COUNT")));	
				tempMap.put("BREAKFAST_REAL", String.valueOf(cashMap.get("BREAKFAST_REAL")));				
				tempMap.put("LUNCH_REAL", String.valueOf(cashMap.get("LUNCH_REAL")));
				tempMap.put("DINNER_REAL", String.valueOf(cashMap.get("DINNER_REAL")));		
				tempMap.put("COUNT_MONEY_PLAN", String.valueOf(cashMap.get("COUNT_MONEY_PLAN")));				
				tempMap.put("COUNT_MONEY_REAL", String.valueOf(cashMap.get("COUNT_MONEY_REAL")));
				exportList.add(tempMap);
			}
			//最后一行加入汇总
			tempMap=new LinkedHashMap<>();
			tempMap.put("rownum", "");
			tempMap.put("CLASS_NAME", "");
			tempMap.put("CLASS_NUMB", "");
			tempMap.put("BEGIN_DATE","");
			tempMap.put("END_DATE","");
			tempMap.put("DINNER_TYPE", "汇总");
			tempMap.put("BREAKFAST_COUNT", String.valueOf(totalMap.get("BREAKFAST_COUNT")));				
			tempMap.put("LUNCH_COUNT", String.valueOf(totalMap.get("LUNCH_COUNT")));
			tempMap.put("DINNER_COUNT", String.valueOf(totalMap.get("DINNER_COUNT")));	
			tempMap.put("BREAKFAST_REAL", String.valueOf(totalMap.get("BREAKFAST_REAL")));				
			tempMap.put("LUNCH_REAL", String.valueOf(totalMap.get("LUNCH_REAL")));
			tempMap.put("DINNER_REAL", String.valueOf(totalMap.get("DINNER_REAL")));		
			tempMap.put("COUNT_MONEY_PLAN", String.valueOf(totalMap.get("COUNT_MONEY_PLAN")));				
			tempMap.put("COUNT_MONEY_REAL", String.valueOf(totalMap.get("COUNT_MONEY_REAL")));
			exportList.add(tempMap);
			
			// --------2.组装表格数据
			Map<String, Object> cashAllMap = new LinkedHashMap<>();
			cashAllMap.put("data", exportList);
			cashAllMap.put("title", "班级就餐情况汇总表");
			cashAllMap.put("head", new String[] { "序号","班级名称", "班级编号", "开始日期", "结束日期", "就餐类型", "计划早餐围/人数","计划午餐围/人数","计划晚餐围/人数","实际早餐围/人数","实际午餐围/人数","实际晚餐围/人数" ,"计划总额","实际总额" }); // 规定名字相同的，设定为合并
			cashAllMap.put("columnWidth", new Integer[] { 10, 15, 10, 20, 20, 10,18,18,18,18,18,18,15,15 }); // 30代表30个字节，15个字符
			cashAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0,0,0,0,0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			cashAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(cashAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "班级就餐情况汇总表", "班级就餐情况汇总表", allList);
			if (result == true) {
				request.getSession().setAttribute("exportTraineeConsumeTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTraineeConsumeTotalIsEnd", "0");
				request.getSession().setAttribute("exportTraineeConsumeTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTraineeConsumeTotalIsEnd", "0");
			request.getSession().setAttribute("exportTraineeConsumeTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportTraineeConsumeTotalEnd")
	public void checkExportTraineeConsumeTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTraineeConsumeTotalIsEnd");
		Object state = request.getSession().getAttribute("exportTraineeConsumeTotalIsState");
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
	
	
	
	/**
	 * 班级学员的消费明细数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getTraineeConsumeDetailList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTraineeConsumeDetailList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
		
		String classId = request.getParameter("CLASS_ID");
		classId=classId==null?"":classId;
		
		String employeeName = request.getParameter("EmployeeName");
		employeeName=employeeName==null?"":employeeName;
		String employeeStrID = request.getParameter("EmployeeStrID");
		employeeStrID=employeeStrID==null?"":employeeStrID;
		String termName = request.getParameter("TermName");
		termName=termName==null?"":termName;
		String accountName = request.getParameter("AccountName");
		accountName=accountName==null?"":accountName;
		String mealTypeID = request.getParameter("MealTypeID");
		mealTypeID=mealTypeID==null?"":mealTypeID;
		String groupType = request.getParameter("GROUP_TYPE");
		groupType=groupType==null?"":groupType;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TraineeConsumeReportSearch] ");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");
		sql.append("'"+classId+"',");
		sql.append("'"+employeeName+"',");
		sql.append("'"+employeeStrID+"',");
		sql.append("'"+termName+"',");
		sql.append("'"+accountName+"',");
		sql.append("'"+mealTypeID+"',");
		sql.append("'"+groupType+"',");
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("TraineeConsumeDetailDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getTeacherConsumeDetailDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTeacherConsumeDetailDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("TraineeConsumeDetailDatas");
		String strData = JsonBuilder.getInstance().toJson(obj);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据	
	}
	
	/**
	 * 导出班级学员消费明细信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportTraineeConsumeDetailExcel")
	public void exportTraineeConsumeDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTraineeConsumeDetailIsEnd", "0");
		request.getSession().removeAttribute("exportTraineeConsumeDetailIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
			
			String classId = request.getParameter("CLASS_ID");
			classId=classId==null?"":classId;
			
			String employeeName = request.getParameter("EmployeeName");
			employeeName=employeeName==null?"":employeeName;
			String employeeStrID = request.getParameter("EmployeeStrID");
			employeeStrID=employeeStrID==null?"":employeeStrID;
			String termName = request.getParameter("TermName");
			termName=termName==null?"":termName;
			String accountName = request.getParameter("AccountName");
			accountName=accountName==null?"":accountName;
			String mealTypeID = request.getParameter("MealTypeID");
			mealTypeID=mealTypeID==null?"":mealTypeID;
			String groupType = request.getParameter("GROUP_TYPE");
			groupType=groupType==null?"":groupType;
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TraineeConsumeReportSearch] ");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");
			sql.append("'"+classId+"',");
			sql.append("'"+employeeName+"',");
			sql.append("'"+employeeStrID+"',");
			sql.append("'"+termName+"',");
			sql.append("'"+accountName+"',");
			sql.append("'"+mealTypeID+"',");
			sql.append("'"+groupType+"',");
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=baseService.getForValuesToSql(sql.toString()+page);			
			lists.remove(0);
					
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> cashMap : lists) {
			
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(cashMap.get("rownum")));
				tempMap.put("ConsumeDate", String.valueOf(cashMap.get("ConsumeDate")));
				tempMap.put("EmployeeName", String.valueOf(cashMap.get("EmployeeName")));
				tempMap.put("EmployeeStrID", String.valueOf(cashMap.get("EmployeeStrID")));
				tempMap.put("TermName", String.valueOf(cashMap.get("TermName")));
				tempMap.put("MealTypeName", String.valueOf(cashMap.get("MealTypeName")));
				tempMap.put("AccountName", String.valueOf(cashMap.get("AccountName")));				
				tempMap.put("ConsumeValue", String.valueOf(cashMap.get("ConsumeValue")));
				tempMap.put("ConsumeNumber", String.valueOf(cashMap.get("ConsumeNumber")));		
				exportList.add(tempMap);
			}
			// --------2.组装表格数据
			Map<String, Object> cashAllMap = new LinkedHashMap<>();
			cashAllMap.put("data", exportList);
			cashAllMap.put("title", "班级学员点券消费汇总表");
			cashAllMap.put("head", new String[] { "序号","日期", "人员姓名", "人员编号", "消费设备", "就餐类型", "结算账户名","消费金额","消费笔数" }); // 规定名字相同的，设定为合并
			cashAllMap.put("columnWidth", new Integer[] { 10, 22, 15, 15, 15, 15,15,20,10 }); // 30代表30个字节，15个字符
			cashAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			cashAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(cashAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "班级学员点券消费汇总表", "班级学员点券消费汇总表", allList);
			if (result == true) {
				request.getSession().setAttribute("exportTraineeConsumeDetailIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTraineeConsumeDetailIsEnd", "0");
				request.getSession().setAttribute("exportTraineeConsumeDetailIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTraineeConsumeDetailIsEnd", "0");
			request.getSession().setAttribute("exportTraineeConsumeDetailIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportTraineeConsumeDetailEnd")
	public void checkExportTraineeConsumeDetailEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTraineeConsumeDetailIsEnd");
		Object state = request.getSession().getAttribute("exportTraineeConsumeDetailIsState");
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
	
}