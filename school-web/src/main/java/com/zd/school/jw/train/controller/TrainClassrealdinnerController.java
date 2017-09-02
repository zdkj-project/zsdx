
package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.core.util.exportMeetingInfo;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassrealdinner ;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.dao.TrainClassrealdinnerDao ;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClassrealdinnerService ;

/**
 * 
 * ClassName: TrainClassrealdinnerController
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 班级就餐登记(TRAIN_T_CLASSREALDINNER)实体Controller.
 * date: 2017-06-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainClassrealdinner")
public class TrainClassrealdinnerController extends FrameWorkController<TrainClassrealdinner> implements Constant {

    @Resource
    TrainClassrealdinnerService thisService; // service层接口
    
    @Resource
	TrainClassService trainClassService; // service层接口

    /**
      * @Title: list
      * @Description: 查询数据列表
      * @param entity 实体类
      * @param request
      * @param response
      * @throws IOException    设定参数
      * @return void    返回类型
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute TrainClassrealdinner entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<TrainClassrealdinner> qResult = thisService.list(start, limit, sort, filter,true);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
      * @Title: doadd
      * @Description: 增加新实体信息至数据库
      * @param TrainClassrealdinner 实体类
      * @param request
      * @param response
      * @return void    返回类型
      * @throws IOException    抛出异常
     */
    @RequestMapping("/doadd")
    public void doAdd(TrainClassrealdinner entity, HttpServletRequest request, HttpServletResponse response)
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
     * @param TrainClassrealdinner
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException  抛出异常
    */
    @RequestMapping("/doupdate")
    public void doUpdates(TrainClassrealdinner entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		
		//获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请重试或联系管理员！\""));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请重试或联系管理员！\""));
		}
    }
    
    /**
	 * 导出某天登记信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportExcel")
	public void exportSiteExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassrealdinnerIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassrealdinnerIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
		
		String date = request.getParameter("date"); 
	
		try{
			String hql="from TrainClassrealdinner a where a.isDelete=0 and a.dinnerDate=CONVERT(datetime,'"
                         + date + "') order by a.createTime asc";
			List<TrainClassrealdinner> lists=thisService.getForValues(hql);
			
			// 处理课程基本数据
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> dinnerMap = null;
			for (TrainClassrealdinner classrealdinner : lists) {
				dinnerMap = new LinkedHashMap<>();
				
				dinnerMap.put("className", classrealdinner.getClassName());
				dinnerMap.put("date", fmtDate.format(classrealdinner.getDinnerDate()));
				dinnerMap.put("person",classrealdinner.getContactPerson());
				dinnerMap.put("phone", classrealdinner.getContactPhone());
				dinnerMap.put("breakfast", String.valueOf(classrealdinner.getBreakfastCount()));
				dinnerMap.put("lunch", String.valueOf(classrealdinner.getLunchCount()));
				dinnerMap.put("dinner", String.valueOf(classrealdinner.getDinnerCount()));
				dinnerMap.put("breakfastReal", String.valueOf(classrealdinner.getBreakfastReal()));
				dinnerMap.put("breakfastStandReal", String.valueOf(classrealdinner.getBreakfastStandReal()));
				dinnerMap.put("lunchReal", String.valueOf(classrealdinner.getLunchReal()));
				dinnerMap.put("lunchStandReal", String.valueOf(classrealdinner.getLunchStandReal()));
				dinnerMap.put("dinnerReal", String.valueOf(classrealdinner.getDinnerReal()));
				dinnerMap.put("dinnerStandReal", String.valueOf(classrealdinner.getDinnerStandReal()));
				
				exportList.add(dinnerMap);
			}
			// --------2.组装表格数据
			Map<String, Object> dinnerAllMap = new LinkedHashMap<>();
			dinnerAllMap.put("data", exportList);
			dinnerAllMap.put("title", "就餐登记表");
			dinnerAllMap.put("head", new String[] { "班级名称", "就餐日期", "联系人", "联系电话", "预定早餐围/人数", "预定午餐围/人数", "预定晚餐围/人数","实际早餐围/人数","早餐餐标","实际午餐围/人数","午餐餐标","实际晚餐围/人数","晚餐餐标" }); // 规定名字相同的，设定为合并
			dinnerAllMap.put("columnWidth", new Integer[] { 15, 15, 15, 15, 17, 17, 17,17, 15, 17,15,17,15 }); // 30代表30个字节，15个字符
			dinnerAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0,0}); // 0代表居中，1代表居左，2代表居右
			dinnerAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(dinnerAllMap);
	
			// 在导出方法中进行解析
			String beginDate=fmtDate.format(lists.get(0).getDinnerDate());
			
			boolean result = PoiExportExcel.exportExcel(response, "就餐登记信息", beginDate, allList);
			if (result == true) {
				request.getSession().setAttribute("exportTrainClassrealdinnerIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTrainClassrealdinnerIsEnd", "0");
				request.getSession().setAttribute("exportTrainClassrealdinnerIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTrainClassrealdinnerIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassrealdinnerIsState", "0");
		}
	}
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportEnd")
	public void checkExportSiteEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassrealdinnerIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassrealdinnerIsState");
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
	 * 就餐汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getDinnerTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
		
		String className = request.getParameter("className");
		className=className==null?"":className;
		String classNumb = request.getParameter("classNumb");
		classNumb=classNumb==null?"":classNumb;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_PPT_TrainDinnerTotal] ");
		sql.append("'" + className + "',");
		sql.append("'" + classNumb + "',");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");
		
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("TrainDinnerTotalDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getDinnerTotalDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("TrainDinnerTotalDatas");
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
	@RequestMapping("/exportTotalExcel")
	public void exportTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassDinnerTotalIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassDinnerTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
					     	
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
			
			String className = request.getParameter("className");
			className=className==null?"":className;
			String classNumb = request.getParameter("classNumb");
			classNumb=classNumb==null?"":classNumb;
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_PPT_TrainDinnerTotal] ");
			sql.append("'" + className + "',");
			sql.append("'" + classNumb + "',");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");
			
			String page=pageIndex + "," + pageSize;
			List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);						
			lists.remove(0);
					
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> dinnerMap = null;
			for (Map<String, Object> classrealdinner : lists) {
				Object dinnerType=classrealdinner.get("DINNER_TYPE");
				if("1".equals(dinnerType)){
					dinnerType="围餐";
				}else if("2".equals(dinnerType)){
					dinnerType="自助餐";
				}else{
					dinnerType="快餐";
				}
				dinnerMap=new LinkedHashMap<>();
				dinnerMap.put("rownum", String.valueOf(classrealdinner.get("rownum")));
				dinnerMap.put("CLASS_NAME", String.valueOf(classrealdinner.get("CLASS_NAME")));
				dinnerMap.put("CLASS_NUMB", String.valueOf(classrealdinner.get("CLASS_NUMB")));
				dinnerMap.put("DINNER_TYPE", String.valueOf(dinnerType));
				dinnerMap.put("BREAKFAST_COUNT", String.valueOf(classrealdinner.get("BREAKFAST_COUNT")));
				dinnerMap.put("LUNCH_COUNT", String.valueOf(classrealdinner.get("LUNCH_COUNT")));
				dinnerMap.put("DINNER_COUNT", String.valueOf(classrealdinner.get("DINNER_COUNT")));
				dinnerMap.put("BREAKFAST_REAL", String.valueOf(classrealdinner.get("BREAKFAST_REAL")));
				dinnerMap.put("LUNCH_REAL", String.valueOf(classrealdinner.get("LUNCH_REAL")));
				dinnerMap.put("DINNER_REAL", String.valueOf(classrealdinner.get("DINNER_REAL")));
				dinnerMap.put("COUNT_MONEY_PLAN", String.valueOf(classrealdinner.get("COUNT_MONEY_PLAN")));
				dinnerMap.put("COUNT_MONEY_REAL", String.valueOf(classrealdinner.get("COUNT_MONEY_REAL")));
				exportList.add(dinnerMap);
			}
			// --------2.组装表格数据
			Map<String, Object> dinnerAllMap = new LinkedHashMap<>();
			dinnerAllMap.put("data", exportList);
			dinnerAllMap.put("title", "就餐汇总表");
			dinnerAllMap.put("head", new String[] { "序号","班级名称", "班级编号", "就餐类型", "计划早餐围/人数", "计划午餐围/人数", "计划晚餐围/人数","实际早餐围/人数","实际午餐围/人数","实际晚餐围/人数","计划总额","实际总额" }); // 规定名字相同的，设定为合并
			dinnerAllMap.put("columnWidth", new Integer[] { 10, 20, 15, 12, 17, 17, 17,17, 17, 17,15,15 }); // 30代表30个字节，15个字符
			dinnerAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			dinnerAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(dinnerAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "就餐汇总信息", "就餐汇总信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportTrainClassDinnerTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTrainClassDinnerTotalIsEnd", "0");
				request.getSession().setAttribute("exportTrainClassDinnerTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTrainClassDinnerTotalIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassDinnerTotalIsState", "0");
		}
	}
	
	/**
	 * 导出班级详情信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportDinnerDetailExcel")
	public void exportDinnerDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainDinnerDetailIsEnd", "0");
		request.getSession().removeAttribute("exportTrainDinnerDetailIsState");

		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy年MM月dd日");
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
		
		String classId = request.getParameter("classId"); 
	
		try{
			String hql="from TrainClassrealdinner a where a.isDelete=0 and a.classId=? order by a.createTime asc";
			List<TrainClassrealdinner> lists=thisService.getForValues(hql,classId);
			String sheetTitle="";
			
			// 处理课程基本数据
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> dinnerMap = null;
			for (TrainClassrealdinner classrealdinner : lists) {
				dinnerMap = new LinkedHashMap<>();
				
				if(sheetTitle.isEmpty())
					sheetTitle=classrealdinner.getClassName();
				
				dinnerMap.put("className", classrealdinner.getClassName());
				dinnerMap.put("date", fmtDate.format(classrealdinner.getDinnerDate()));
				dinnerMap.put("person",classrealdinner.getContactPerson());
				dinnerMap.put("phone", classrealdinner.getContactPhone());
				dinnerMap.put("breakfastReal", String.valueOf(classrealdinner.getBreakfastReal()));
				dinnerMap.put("breakfastStandReal", String.valueOf(classrealdinner.getBreakfastStandReal()));
				dinnerMap.put("lunchReal", String.valueOf(classrealdinner.getLunchReal()));
				dinnerMap.put("lunchStandReal", String.valueOf(classrealdinner.getLunchStandReal()));
				dinnerMap.put("dinnerReal", String.valueOf(classrealdinner.getDinnerReal()));
				dinnerMap.put("dinnerStandReal", String.valueOf(classrealdinner.getDinnerStandReal()));
				dinnerMap.put("moneyReal", String.valueOf(classrealdinner.getCountMoneyReal()));			
				dinnerMap.put("version", classrealdinner.getVersion()==0?"未登记":"已登记");
				dinnerMap.put("remark", classrealdinner.getRemark());			
				exportList.add(dinnerMap);
			}
			// --------2.组装表格数据
			Map<String, Object> dinnerAllMap = new LinkedHashMap<>();
			dinnerAllMap.put("data", exportList);
			dinnerAllMap.put("title", "班级就餐登记详情表");
			dinnerAllMap.put("head", new String[] { "班级名称", "就餐日期", "联系人", "联系电话","实际早餐围/人数","早餐餐标","实际午餐围/人数","午餐餐标","实际晚餐围/人数","晚餐餐标","实际金额","是否登记","备注" }); // 规定名字相同的，设定为合并
			dinnerAllMap.put("columnWidth", new Integer[] { 15, 15, 15, 15, 17, 15, 17,15, 17, 15,10,10,20 }); // 30代表30个字节，15个字符
			dinnerAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			dinnerAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(dinnerAllMap);
	
			// 在导出方法中进行解析
			
			boolean result = PoiExportExcel.exportExcel(response, "班级就餐登记详情信息", sheetTitle, allList);
			if (result == true) {
				request.getSession().setAttribute("exportTrainDinnerDetailIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTrainDinnerDetailIsEnd", "0");
				request.getSession().setAttribute("exportTrainDinnerDetailIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTrainDinnerDetailIsEnd", "0");
			request.getSession().setAttribute("exportTrainDinnerDetailIsState", "0");
		}
	}
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportDinnerDetailEnd")
	public void checkExportDinnerDetailEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainDinnerDetailIsEnd");
		Object state = request.getSession().getAttribute("exportTrainDinnerDetailIsState");
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
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportTotalEnd")
	public void checkExportTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassDinnerTotalIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassDinnerTotalIsState");
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
	 * 导出财务要求汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportClassTotalExcel")
	public void exportClassTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassTotalIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassTotalIsState");

		SimpleDateFormat fmtmDate = new SimpleDateFormat("MM");
		SimpleDateFormat fmtdDate = new SimpleDateFormat("dd");
		List<Map<String, Object>> allList = new ArrayList<>();
		String classId = request.getParameter("classId"); 
	
		try{
			String hql="from TrainClassrealdinner a where a.isDelete=0 and a.classId=? order by a.dinnerDate asc";
			List<TrainClassrealdinner> lists=thisService.getForValues(hql,classId);
			int listsCount = lists.size();
			
			//定义单据编号
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String date = df.format(new Date());
			String like ="XN"+date;
			String sql = "SELECT EXT_FIELD05 FROM dbo.TRAIN_T_CLASS WHERE EXT_FIELD05 LIKE '%"+like+"%'"+" order by EXT_FIELD05 asc";
			String trainClass = trainClassService.getForValueToSql(sql);
			String djbh ;
			if(trainClass==null) {
				djbh = null;
			}else {
				djbh = trainClass;
			}
			int times = (listsCount%6==0)?(listsCount/6):((listsCount/6)+1);
			List<String> djbhs = new ArrayList<>();
			if(djbh==null||djbh.substring(0, 10).equals("XN"+date)==false) {
				for(int time=0;time<times;time++){
					if(time<10) {
						djbh = "XN"+date+"00"+(time+1);
					}else if(10<=time&&time<100) {
						djbh = "XN"+date+"0"+(time+1);
					}else {
						djbh = "XN"+date+(time+1);
					}
					djbhs.add(djbh);
				}
			}else if(djbh!=null&&djbh.substring(0, 10).equals("XN"+date)) {
				int oldTime = Integer.parseInt(djbh.substring(10));
				for(int time=0;time<times;time++){
					if((oldTime+time+1)<10) {
						djbh = "XN"+date+"00"+(oldTime+time+1);
					}else if(10<=(oldTime+time+1)&&(oldTime+time+1)<100) {
						djbh = "XN"+date+"0"+(oldTime+time+1);
					}else {
						djbh = "XN"+date+(oldTime+time+1);
					}
					djbhs.add(djbh);
				}
			}
			TrainClass trainClasss = trainClassService.get(classId);
			trainClasss.setExtField05(djbh);
			trainClassService.update(trainClasss);
			//定义sheet标题
			String sheetTitle="";
			String dinnerType= "";
			
			//定义总合计￥
			List<BigDecimal> totals =new ArrayList<>();
			
			//定义总合计大写
			List<String> totalTexts = new ArrayList<>();
			
			// 处理课程基本数据
			List<List<Map<String, String>>> exportLists = new ArrayList<>();
			List<Map<String, String>>  exportList=null;
			Map<String, String> dinnerMap = null;
			for(int i=0;i<times;i++) {
				exportList =new ArrayList<>();
				BigDecimal total =new BigDecimal(0);
				for (int j=0;j<6;j++) {
					if((6*i+j)<listsCount) {
					dinnerMap = new LinkedHashMap<>();
					sheetTitle=lists.get(6*i+j).getClassName();
					dinnerType = (lists.get(6*i+j).getDinnerType()==1)?"围餐":"自助";
					BigDecimal moneyTotal=new BigDecimal(0);
					int breakfastReal = lists.get(6*i+j).getBreakfastReal();
					BigDecimal breakfastStandReal =lists.get(6*i+j).getBreakfastStandReal();
					if(breakfastStandReal==null) {
						breakfastStandReal=new BigDecimal(0);
					}
					BigDecimal breakfastTotal =breakfastStandReal.multiply(new BigDecimal(breakfastReal));
					
					int lunchReal = lists.get(6*i+j).getLunchReal();
					BigDecimal lunchStandReal =lists.get(6*i+j).getLunchStandReal ();
					if(lunchStandReal==null) {
						lunchStandReal=new BigDecimal(0);
					}
					BigDecimal lunchTotal =lunchStandReal.multiply(new BigDecimal(lunchReal));
					
					int dinnerReal = lists.get(6*i+j).getDinnerReal();
					BigDecimal dinnerStandReal =lists.get(6*i+j).getDinnerStandReal();
					if(dinnerStandReal==null) {
						dinnerStandReal=new BigDecimal(0);
					}
					BigDecimal dinnerTotal =dinnerStandReal.multiply(new BigDecimal(dinnerReal));
					moneyTotal = breakfastTotal.add(lunchTotal).add(dinnerTotal);
					
					total = total.add(moneyTotal);
					
					dinnerMap.put("month", fmtmDate.format(lists.get(6*i+j).getDinnerDate()));
					dinnerMap.put("day", fmtdDate.format(lists.get(6*i+j).getDinnerDate()));
					
					dinnerMap.put("breakfastReal", String.valueOf(breakfastReal));
					dinnerMap.put("breakfastStandReal", String.valueOf(breakfastStandReal));
					dinnerMap.put("breakfastTotal", String.valueOf(breakfastTotal));
					
					dinnerMap.put("lunchReal", String.valueOf(lunchReal));
					dinnerMap.put("lunchStandReal", String.valueOf(lunchStandReal));
					dinnerMap.put("lunchTotal", String.valueOf(lunchTotal));
					
					dinnerMap.put("dinnerReal", String.valueOf(dinnerReal));
					dinnerMap.put("dinnerStandReal", String.valueOf(dinnerStandReal));
					dinnerMap.put("dinnerTotal", String.valueOf(dinnerTotal));
					
					dinnerMap.put("other", String.valueOf("加菜[   ]"+'\n'+"纸巾[   ]"+'\n'+"其他[   ]"));			
					dinnerMap.put("moneyTotal",String.valueOf(moneyTotal) );			
					exportList.add(dinnerMap);
					}
				}
				int  a = total.divide(new BigDecimal(100000)).intValue()%10;
				int  b = total.divide(new BigDecimal(10000)).intValue()%10;
				int  c = total.divide(new BigDecimal(1000)).intValue()%10;
				int  d = total.divide(new BigDecimal(100)).intValue()%10;
				int  e = total.divide(new BigDecimal(10)).intValue()%10;
				int  f = total.divide(new BigDecimal(1)).intValue()%10;
				int  g = total.multiply(new BigDecimal(10)).divide(new BigDecimal(1)).intValue()%10;
				int  h = total.multiply(new BigDecimal(100)).divide(new BigDecimal(1)).intValue()%10;
				int[] ss = new int[] {a,b,c,d,e,f,g,h};
				List<String> sss = new ArrayList<>();
				for(int k =0;k<ss.length;k++) {
					switch(ss[k]) {
					case 0:sss.add("零");break;
					case 1:sss.add("壹");break;
					case 2:sss.add("贰");break;
					case 3:sss.add("叁");break;
					case 4:sss.add("肆");break;
					case 5:sss.add("伍");break;
					case 6:sss.add("陆");break;
					case 7:sss.add("柒");break;
					case 8:sss.add("捌");break;
					case 9:sss.add("玖");break;
					}
				}
				String totaltext = sss.get(0)+" 拾   "+sss.get(1)+" 万  "+sss.get(2)+" 仟  "+sss.get(3)+" 佰  "+sss.get(4)+" 拾  "+sss.get(5)+" 元  "+sss.get(6)+" 角  "+sss.get(7)+" 分  ";
				totals.add(total);
				totalTexts.add(totaltext);
				exportLists.add(exportList);
			}
			

			// --------2.组装表格数据
			Map<String, Object> dinnerAllMap = new LinkedHashMap<>();
			dinnerAllMap.put("data", exportLists);
			dinnerAllMap.put("title", "班级就餐登记详情表");
			dinnerAllMap.put("dinnerType", dinnerType);
			dinnerAllMap.put("djbhs",djbhs );
			dinnerAllMap.put("totalTexts",totalTexts );
			dinnerAllMap.put("totals",totals );
			dinnerAllMap.put("head", new String[] { "月", "日", "数量", "餐标","小计","数量","餐标","小计","数量","餐标","小计","其他","金额合计" }); // 规定名字相同的，设定为合并
			dinnerAllMap.put("columnWidth", new Integer[] { 10, 10, 15, 15, 15, 15, 15, 15, 15, 15, 15, 20, 20 }); // 30代表30个字节，15个字符
			dinnerAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
			dinnerAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(dinnerAllMap);
	
			// 在导出方法中进行解析
			boolean result = exportMeetingInfo.exportClassTotalsExcel(response, sheetTitle+"就餐内结单", sheetTitle, allList);
			if (result == true) {
				request.getSession().setAttribute("exportTrainClassTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTrainClassTotalIsEnd", "0");
				request.getSession().setAttribute("exportTrainClassTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTrainClassTotalIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportClassTotalEnd")
	public void checkExportClassTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassTotalIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassTotalIsState");
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
