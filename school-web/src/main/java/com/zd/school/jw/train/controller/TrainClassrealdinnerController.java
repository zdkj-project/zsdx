
package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassrealdinner ;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.dao.TrainClassrealdinnerDao ;
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
	 * 导出班级住宿安排信息
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
				dinnerMap.put("lunchReal", String.valueOf(classrealdinner.getLunchReal()));
				dinnerMap.put("dinnerReal", String.valueOf(classrealdinner.getDinnerReal()));
				
				exportList.add(dinnerMap);
			}
			// --------2.组装表格数据
			Map<String, Object> dinnerAllMap = new LinkedHashMap<>();
			dinnerAllMap.put("data", exportList);
			dinnerAllMap.put("title", "就餐登记表");
			dinnerAllMap.put("head", new String[] { "班级名称", "就餐日期", "联系人", "联系电话", "预定早餐围/人数", "预定午餐围/人数", "预定晚餐围/人数","实际早餐围/人数","实际午餐围/人数","实际晚餐围/人数" }); // 规定名字相同的，设定为合并
			dinnerAllMap.put("columnWidth", new Integer[] { 15, 15, 15, 15, 16, 16, 16,16, 16, 16 }); // 30代表30个字节，15个字符
			dinnerAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0,0,0,0 }); // 0代表居中，1代表居左，2代表居右
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
}
