package com.zd.school.jw.train.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.PoiExportExcel;
import com.zd.school.cashier.model.CashExpensedetail;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainTeacherOrder;
import com.zd.school.jw.train.service.TrainTeacherOrderDescService;
import com.zd.school.jw.train.service.TrainTeacherOrderService;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/TrainTeacherOrder")
public class TrainTeacherOrderController extends FrameWorkController<TrainTeacherOrder> implements Constant {

	private static Logger logger = Logger.getLogger(TrainTeacherOrderController.class);

	@Resource
	TrainTeacherOrderService thisService; // service层接口

	@Resource
	TrainTeacherOrderDescService descService; // service层接口


	@RequestMapping("/getOrderList")
	public void getOrderDesc(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		// 创建 Calendar 对象
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar1.getTime());
		calendar2.add(Calendar.DAY_OF_MONTH, 14);
		
		SysUser currentUser = getCurrentSysUser();

		// beginTime Between '" + s + " 06:00:00" + "' And '"+ sdf.format(date2)
		// + "'
		// 当前节点
		String hql = "from TrainTeacherOrder where userId=? and dinnerDate>=? and dinnerDate<? order by dinnerDate asc";
		List<TrainTeacherOrder> list = thisService.getForValues(hql, currentUser.getUuid(), calendar1.getTime(), calendar2.getTime());
		int len = list.size();
		
		List<TrainTeacherOrder> returnlist=new ArrayList<>();
		returnlist.addAll(list);
		if (len < 14) {
			TrainTeacherOrder temp = null;
		
			boolean isExist = false;
			int day = 0;
			int day2 = 0;
			for (int i = 0; i < 14; i++) {
				isExist = false;
				day = calendar1.get(Calendar.DAY_OF_YEAR);
				for (int j = 0; j < len; j++) {
					calendar2.setTime(list.get(j).getDinnerDate());
					day2 = calendar2.get(Calendar.DAY_OF_YEAR);
					if (day == day2) {
						isExist = true;
						break;
					}
				}
				if (isExist == false) {
					temp = new TrainTeacherOrder();
					temp.setDinnerDate(calendar1.getTime());
					temp.setDinnerGroup((short) 0);
					temp.setUserId(currentUser.getUuid());
					temp.setUuid(null);
					returnlist.add(i, temp);
				}
				calendar1.set(Calendar.DAY_OF_MONTH, calendar1.get(Calendar.DAY_OF_MONTH) + 1);
			}
		}
		String strData = jsonBuilder.buildObjListToJson((long) returnlist.size(), returnlist, true);
		writeJSON(response, strData);

		// if(result.getTotalCount()>0)
		// writeJSON(response,
		// jsonBuilder.returnSuccessJson(jsonBuilder.toJson(result.getResultList().get(0))));
		// else
		// writeJSON(response, jsonBuilder.returnFailureJson("\"暂无数据\""));

	}
	
	
	@RequestMapping("/getOrderTotal")
	public void getOrderTotal(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		// 创建 Calendar 对象
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar1.getTime());
		calendar2.add(Calendar.DAY_OF_MONTH, 14);
	

		// beginTime Between '" + s + " 06:00:00" + "' And '"+ sdf.format(date2)
		// + "'
		// 当前节点
		String hql = "from TrainTeacherOrder where dinnerDate>=? and dinnerDate<? order by dinnerDate asc";
		List<TrainTeacherOrder> list = thisService.getForValues(hql, calendar1.getTime(), calendar2.getTime());
		int len = list.size();

		List<Map<String,Object>> mapList=new ArrayList<>();
		Map<String,Object> map=null;
		
		int day = 0;
		int day2 = 0;
		int count=0,countA=0,countB=0;
		for (int i = 0; i < 14; i++) {
			map=new HashMap<String,Object>();
			map.put("dinnerDate", calendar1.getTime());
			day = calendar1.get(Calendar.DAY_OF_YEAR);
			count=0;
			countA=0;
			countB=0;
			for (int j = 0; j < len; j++) {
				calendar2.setTime(list.get(j).getDinnerDate());
				day2 = calendar2.get(Calendar.DAY_OF_YEAR);
				
				if (day == day2) {	
					if(1==list.get(j).getDinnerGroup()){
						countA++;
						count++;
					}else if(2==list.get(j).getDinnerGroup()){
						countB++;
						count++;
					}
				}
			}
			map.put("count", count);
			map.put("countA", countA);
			map.put("countB", countB);
			mapList.add(map);
			calendar1.set(Calendar.DAY_OF_MONTH, calendar1.get(Calendar.DAY_OF_MONTH) + 1);
		}
		
		String strData = jsonBuilder.buildObjListToJson((long) mapList.size(), mapList, true);
		writeJSON(response, strData);

		// if(result.getTotalCount()>0)
		// writeJSON(response,
		// jsonBuilder.returnSuccessJson(jsonBuilder.toJson(result.getResultList().get(0))));
		// else
		// writeJSON(response, jsonBuilder.returnFailureJson("\"暂无数据\""));

	}
	
	@RequestMapping("/getOrderUserList")
	public void getOrderUserList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dinnerDate=request.getParameter("dinnerDate");
		
		Date date=sdf.parse(dinnerDate);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		
		// 创建 Calendar 对象
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar1.getTime());
		calendar2.add(Calendar.DAY_OF_MONTH, 1);
		
		// beginTime Between '" + s + " 06:00:00" + "' And '"+ sdf.format(date2)
		// + "'
		// 当前节点
		String hql = "from TrainTeacherOrder where "
				+ " dinnerDate>=CONVERT(datetime,'"+sdf.format(calendar1.getTime())+"') "
					+ " and dinnerDate<CONVERT(datetime,'"+sdf.format(calendar2.getTime())+"') order by createTime asc";
		//List<TrainTeacherOrder> list = thisService.getForValues(hql, calendar1.getTime(), calendar2.getTime());
		QueryResult<TrainTeacherOrder> result = thisService.doQueryResult(hql, start, limit);
		
		String strData = jsonBuilder.buildObjListToJson(result.getTotalCount(), result.getResultList(), true);
		writeJSON(response, strData);

		// if(result.getTotalCount()>0)
		// writeJSON(response,
		// jsonBuilder.returnSuccessJson(jsonBuilder.toJson(result.getResultList().get(0))));
		// else
		// writeJSON(response, jsonBuilder.returnFailureJson("\"暂无数据\""));

	}
	
	/**
	 * 
	 * doAdd @Title: doAdd @Description: TODO @param @param BizTJob
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping("/doAdd")
	public void doAdd(TrainTeacherOrder entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
			Date date = new Date();
			// 创建 Calendar 对象
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);	
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(entity.getDinnerDate());
			if(calendar1.compareTo(calendar2)==1){
				writeJSON(response, jsonBuilder.returnFailureJson("\"当前时间不允许再修改订餐！\""));
				return;
			}else{
				calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH)-1);
				if(calendar2.compareTo(calendar1)!=1){
					calendar2.set(Calendar.HOUR_OF_DAY, 17);
					calendar2.set(Calendar.MINUTE, 0);
					calendar2.set(Calendar.SECOND, 0);
					calendar2.set(Calendar.MILLISECOND, 0);
					if(calendar2.compareTo(calendar1)!=1){
						writeJSON(response, jsonBuilder.returnFailureJson("\"超过17点钟，就不可再预定明天的就餐！\""));
						return;
					}
				}
			}
				
			// 当前节点
			TrainTeacherOrder saveEntity = new TrainTeacherOrder();
			List<String> excludeList = new ArrayList<>();
			excludeList.add("uuid");
			BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludeList);
			saveEntity.setUserId(currentUser.getUuid());

			// 增加时要设置创建人
			entity.setCreateUser(userCh); // 创建人
			// 持久化到数据库
			saveEntity = thisService.merge(saveEntity);

			// 返回实体到前端界面
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(saveEntity)));
		} catch (Exception e) {
			// 返回实体到前端界面
			writeJSON(response, jsonBuilder.returnFailureJson("\"订餐失败，请联系管理员！\""));
		}

	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * BizTJob @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doUpdate")
	public void doUpdate(TrainTeacherOrder entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		try {
			// 先拿到已持久化的实体
			TrainTeacherOrder perEntity = thisService.get(entity.getUuid());
						
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
			Date date = new Date();
			// 创建 Calendar 对象
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(perEntity.getDinnerDate());
			if(calendar1.compareTo(calendar2)==1){
				writeJSON(response, jsonBuilder.returnFailureJson("\"当前时间不允许再修改订餐！\""));
				return;
			}else{
				calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH)-1);
				if(calendar2.compareTo(calendar1)!=1){
					calendar2.set(Calendar.HOUR_OF_DAY, 17);
					calendar2.set(Calendar.MINUTE, 0);
					calendar2.set(Calendar.SECOND, 0);
					calendar2.set(Calendar.MILLISECOND, 0);
					if(calendar2.compareTo(calendar1)!=1){
						writeJSON(response, jsonBuilder.returnFailureJson("\"超过17点钟，就不可再修改明天的就餐！\""));
						return;
					}
				}
			}

			// 将entity中不为空的字段动态加入到perEntity中去。
			BeanUtils.copyPropertiesExceptNull(perEntity, entity);

			perEntity.setUpdateTime(new Date()); // 设置修改时间
			perEntity.setUpdateUser(userCh); // 设置修改人的中文名
			entity = thisService.merge(perEntity);// 执行修改方法

			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
		} catch (Exception e) {
			// 返回实体到前端界面
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员！\""));
		}
	}

	@RequestMapping("/doCancel")
	public void doCancel(TrainTeacherOrder entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		try {
			// 先拿到已持久化的实体
			TrainTeacherOrder perEntity = thisService.get(entity.getUuid());
						
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
			Date date = new Date();
			// 创建 Calendar 对象
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);	
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(perEntity.getDinnerDate());
			if(calendar1.compareTo(calendar2)==1){
				writeJSON(response, jsonBuilder.returnFailureJson("\"当前时间不允许再取消订餐！\""));
				return;
			}else{
				calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH)-1);
				if(calendar2.compareTo(calendar1)!=1){
					calendar2.set(Calendar.HOUR_OF_DAY, 17);
					calendar2.set(Calendar.MINUTE, 0);
					calendar2.set(Calendar.SECOND, 0);
					calendar2.set(Calendar.MILLISECOND, 0);
					if(calendar2.compareTo(calendar1)!=1){
						writeJSON(response, jsonBuilder.returnFailureJson("\"超过17点钟，就不可再取消明天的就餐信息！\""));
						return;
					}
				}
			}
		

			thisService.delete(perEntity);
			writeJSON(response, jsonBuilder.returnSuccessJson("\"处理成功！\""));
		} catch (Exception e) {
			// 返回实体到前端界面
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员！\""));
		}
	}
	
	
	/**
	 * 导出订餐汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportOrderTotalExcel")
	public void exportOrderTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportOrderTotalIsEnd", "0");
		request.getSession().removeAttribute("exportOrderTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			Date date = new Date();
			// 创建 Calendar 对象
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			calendar1.set(Calendar.HOUR_OF_DAY, 0);
			calendar1.set(Calendar.MINUTE, 0);
			calendar1.set(Calendar.SECOND, 0);
			calendar1.set(Calendar.MILLISECOND, 0);
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(calendar1.getTime());
			calendar2.add(Calendar.DAY_OF_MONTH, 14);
		

			// beginTime Between '" + s + " 06:00:00" + "' And '"+ sdf.format(date2)
			// + "'
			// 当前节点
			String hql = "from TrainTeacherOrder where dinnerDate>=? and dinnerDate<? order by dinnerDate asc";
			List<TrainTeacherOrder> list = thisService.getForValues(hql, calendar1.getTime(), calendar2.getTime());
			int len = list.size();

			List<Map<String,String>> mapList=new ArrayList<>();
			Map<String,String> map=null;
			
			int day = 0;
			int day2 = 0;
			int count=0,countA=0,countB=0;
			for (int i = 0; i < 14; i++) {
				map=new LinkedHashMap<String,String>();
				map.put("rownum", String.valueOf(i+1));
				map.put("dinnerDate", sdf.format(calendar1.getTime()));
				day = calendar1.get(Calendar.DAY_OF_YEAR);
				count=0;
				countA=0;
				countB=0;
				for (int j = 0; j < len; j++) {
					calendar2.setTime(list.get(j).getDinnerDate());
					day2 = calendar2.get(Calendar.DAY_OF_YEAR);
					
					if (day == day2) {	
						if(1==list.get(j).getDinnerGroup()){
							countA++;
							count++;
						}else if(2==list.get(j).getDinnerGroup()){
							countB++;
							count++;
						}
					}
				}
				map.put("count", String.valueOf(count));
				map.put("countA", String.valueOf(countA));
				map.put("countB", String.valueOf(countB));
				mapList.add(map);
				calendar1.set(Calendar.DAY_OF_MONTH, calendar1.get(Calendar.DAY_OF_MONTH) + 1);
			}	
		
			// --------2.组装表格数据
			Map<String, Object> orderAllMap = new LinkedHashMap<>();
			orderAllMap.put("data", mapList);
			orderAllMap.put("title", "订餐汇总表");
			orderAllMap.put("head", new String[] { "序号","订餐日期", "总数", "A套餐数", "B套餐数" }); // 规定名字相同的，设定为合并
			orderAllMap.put("columnWidth", new Integer[] { 10, 22, 20, 20, 20 }); // 30代表30个字节，15个字符
			orderAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0}); // 0代表居中，1代表居左，2代表居右
			orderAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(orderAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, "订餐汇总信息", "订餐汇总信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportOrderTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportOrderTotalIsEnd", "0");
				request.getSession().setAttribute("exportOrderTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportOrderTotalIsEnd", "0");
			request.getSession().setAttribute("exportOrderTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportOrderTotalEnd")
	public void checkExportOrderTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportOrderTotalIsEnd");
		Object state = request.getSession().getAttribute("exportOrderTotalIsState");
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
	 * 导出订餐人员信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportOrderUsersExcel")
	public void exportOrderUsersExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportOrderUsersIsEnd", "0");
		request.getSession().removeAttribute("exportOrderUsersIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd h:mm");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String dinnerDate=request.getParameter("dinnerDate");			
			Date date=sdf.parse(dinnerDate);
			
			// 创建 Calendar 对象
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			calendar1.set(Calendar.HOUR_OF_DAY, 0);
			calendar1.set(Calendar.MINUTE, 0);
			calendar1.set(Calendar.SECOND, 0);
			calendar1.set(Calendar.MILLISECOND, 0);
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(calendar1.getTime());
			calendar2.add(Calendar.DAY_OF_MONTH, 1);
			
			// beginTime Between '" + s + " 06:00:00" + "' And '"+ sdf.format(date2)
			// + "'
			// 当前节点
			String hql = "from TrainTeacherOrder where dinnerDate>=? and dinnerDate<? order by createTime asc";
			List<TrainTeacherOrder> lists = thisService.getForValues(hql, calendar1.getTime(), calendar2.getTime());

			
			// 处理基本数据		
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;		
			TrainTeacherOrder trainTeacherOrder=null;
			String dinnerGroup="";
			for (int i=0;i<lists.size();i++) {
				trainTeacherOrder=lists.get(i);
				
				if(1==trainTeacherOrder.getDinnerGroup())
					dinnerGroup="A套餐";
				else if(2==trainTeacherOrder.getDinnerGroup())
					dinnerGroup="B套餐";
				else
					dinnerGroup="其他";
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum",String.valueOf(i+1));
				tempMap.put("xm", String.valueOf(trainTeacherOrder.getXm()));
				tempMap.put("dinnerGroup", String.valueOf(dinnerGroup));
				tempMap.put("createTime", String.valueOf(sdf2.format(trainTeacherOrder.getCreateTime())));
				tempMap.put("remark", String.valueOf(trainTeacherOrder.getRemark()==null?"":trainTeacherOrder.getRemark()));	
				exportList.add(tempMap);
			}
		
			// --------2.组装表格数据
			Map<String, Object> orderAllMap = new LinkedHashMap<>();
			orderAllMap.put("data", exportList);
			orderAllMap.put("title", dinnerDate+"订餐人员信息表");
			orderAllMap.put("head", new String[] { "序号","姓名","预定套餐","预定时间","备注" }); // 规定名字相同的，设定为合并
			orderAllMap.put("columnWidth", new Integer[] { 10, 15, 15, 20, 30 }); // 30代表30个字节，15个字符
			orderAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0}); // 0代表居中，1代表居左，2代表居右
			orderAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(orderAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, dinnerDate+" 订餐人员信息", "订餐人员信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportOrderUsersIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportOrderUsersIsEnd", "0");
				request.getSession().setAttribute("exportOrderUsersIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportOrderUsersIsEnd", "0");
			request.getSession().setAttribute("exportOrderUsersIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportOrderUsersEnd")
	public void checkExportOrderUsersEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportOrderUsersIsEnd");
		Object state = request.getSession().getAttribute("exportOrderUsersIsState");
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
	 * 订餐差异汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getDinnerDiffTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String startDate=request.getParameter("beginDate");
		startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
		
		String endDate=request.getParameter("endDate");
		endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));
				
		String groupType = request.getParameter("GROUP_TYPE");
		groupType=groupType==null?"":groupType;
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_DinnerDiffReportSearch] ");
		sql.append("'" + startDate + "',");
		sql.append("'" + endDate + "',");		
		sql.append("'"+groupType+"',");
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		request.getSession().setAttribute("DinnerDiffTotalDatas", lists.get(0));	//将统计信息存放到session中
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/getDinnerDiffTotalDatas" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getDinnerTotalDatas( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		Object obj = request.getSession().getAttribute("DinnerDiffTotalDatas");
		String strData = JsonBuilder.getInstance().toJson(obj);// 处理数据
		writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据	
	}
	
	/**
	 * 导出订餐差异汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportDinnerDiffTotalExcel")
	public void exportCashTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportDinnerDiffTotalIsEnd", "0");
		request.getSession().removeAttribute("exportDinnerDiffTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String startDate=request.getParameter("beginDate");
			startDate=startDate==null?"1900-1-1":sdf.format(sdf.parse(startDate));
			
			String endDate=request.getParameter("endDate");
			endDate=endDate==null?"2999-12-12":sdf.format(sdf.parse(endDate));			
			String groupType = request.getParameter("GROUP_TYPE");
			groupType=groupType==null?"":groupType;
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_DinnerDiffReportSearch] ");
			sql.append("'" + startDate + "',");
			sql.append("'" + endDate + "',");			
			sql.append("'"+groupType+"',");
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);	
			Map<String, Object> totalMap=lists.get(0);
			lists.remove(0);
					
			// 处理基本数据				
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> dinnerDiffMap : lists) {
								
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(dinnerDiffMap.get("rownum")));
				tempMap.put("dinnerDate", String.valueOf(dinnerDiffMap.get("dinnerDate")));
				tempMap.put("dinnerCount", String.valueOf(dinnerDiffMap.get("dinnerCount")));
				tempMap.put("dinnerCountA", String.valueOf(dinnerDiffMap.get("dinnerCountA")));
				tempMap.put("dinnerCountB", String.valueOf(dinnerDiffMap.get("dinnerCountB")));
				tempMap.put("realCount", String.valueOf(dinnerDiffMap.get("realCount")));
				tempMap.put("realDinnerCount", String.valueOf(dinnerDiffMap.get("realDinnerCount")));			
				tempMap.put("notDinnerEatCount", String.valueOf(dinnerDiffMap.get("notDinnerEatCount")));
				tempMap.put("exceedEatCount", String.valueOf(dinnerDiffMap.get("exceedEatCount")));
				tempMap.put("notEatdinnerCount", String.valueOf(dinnerDiffMap.get("notEatdinnerCount")));		
				exportList.add(tempMap);
			}
			
			//最后一行加入汇总
			tempMap=new LinkedHashMap<>();
			tempMap.put("rownum","");
			tempMap.put("dinnerDate", "汇总");
			tempMap.put("dinnerCount", String.valueOf(totalMap.get("dinnerCount")));
			tempMap.put("dinnerCountA",  String.valueOf(totalMap.get("dinnerCountA")));
			tempMap.put("dinnerCountB", String.valueOf(totalMap.get("dinnerCountB")));
			tempMap.put("realCount", String.valueOf(totalMap.get("realCount")));
			tempMap.put("realDinnerCount", String.valueOf(totalMap.get("realDinnerCount")));
			tempMap.put("notDinnerEatCount", String.valueOf(totalMap.get("notDinnerEatCount")));
			tempMap.put("exceedEatCount", String.valueOf(totalMap.get("exceedEatCount")));
			tempMap.put("notEatdinnerCount",  String.valueOf(totalMap.get("notEatdinnerCount")));
			exportList.add(tempMap);
						
			// --------2.组装表格数据
			Map<String, Object> orderAllMap = new LinkedHashMap<>();
			orderAllMap.put("data", exportList);
			orderAllMap.put("title", "订餐就餐汇总信息表");
			orderAllMap.put("head", new String[] { "序号","就餐日期","订餐总数","A套餐总数","B套餐总数","实际就餐总数","就餐总数（已订餐）","就餐总数（未订餐）","订餐且多刷总数","未就餐总数（已订餐）", }); // 规定名字相同的，设定为合并
			orderAllMap.put("columnWidth", new Integer[] { 10, 15, 12, 12, 12,15,20,20,20,22 }); // 30代表30个字节，15个字符
			orderAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0,0,0,0,0}); // 0代表居中，1代表居左，2代表居右
			orderAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(orderAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, " 订餐就餐汇总信息", "订餐就餐汇总信息", allList);
			if (result == true) {
				request.getSession().setAttribute("exportDinnerDiffTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportDinnerDiffTotalIsEnd", "0");
				request.getSession().setAttribute("exportDinnerDiffTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportDinnerDiffTotalIsEnd", "0");
			request.getSession().setAttribute("exportDinnerDiffTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportDinnerDiffTotalEnd")
	public void checkExportDinnerDiffTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportDinnerDiffTotalIsEnd");
		Object state = request.getSession().getAttribute("exportDinnerDiffTotalIsState");
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
	 * 获取每天的教师是否订餐就餐情况
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/getOrderDetailList")
	public void getOrderDetailList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		String type=request.getParameter("type");
		
		String year=request.getParameter("YEAR");
		String month=request.getParameter("MONTH");	
		String day=request.getParameter("DAY");
		
		if(month.length()==1)
			month="0"+month;
		if(day.length()==1)
			day="0"+day;	
		String date=year+"-"+month+"-"+day;
		
		String sql="";
		if("1".equals(type)){	//就餐总数（已订餐）
			sql="SELECT distinct convert(varchar,a.EmployeeName) as EmployeeName,"
					+ " convert(varchar,a.EmployeeStrID) as EmployeeStrID "
					+ " FROM YCSCMDB_up6.dbo.TE_ConsumeDetailXF a"
					+ " join YCSCMDB_up6.dbo.TC_EMPLOYEE b on a.employeeid=b.employeeid"
					+ " join TRAIN_T_TEACHERORDER c on b.userId=c.user_id"
					+ " where a.mealTypeName='早餐' and a.departmentId not like 'Train%' "
					+ " and c.dinner_date='"+date+"' "
					+ " and CONVERT(varchar(10),consumeDate, 23)=c.dinner_date"
					+ " and a.employeeName not like '%测%试%'"
					+ " order by EmployeeStrID asc";
			
		}else if("2".equals(type)){	//未就餐总数（已订餐）
			sql="SELECT convert(varchar,b.EmployeeName) as EmployeeName,"
					+ " convert(varchar,b.EmployeeStrID) as EmployeeStrID"
					+ " FROM TRAIN_T_TEACHERORDER a "
					+ " join YCSCMDB_up6.dbo.TC_EMPLOYEE b on  a.user_id=b.userid "
					+ " where a.dinner_date='"+date+"' "
					+ " and b.employeeId not in ("
					+ " SELECT employeeId "
					+ " FROM YCSCMDB_up6.dbo.TE_ConsumeDetailXF "
					+ "	where mealTypeName='早餐' and departmentId not like 'Train%'	"
					+ " and CONVERT(varchar(10),consumeDate, 23)='"+date+"'"
					+ " and employeeName not like '%测%试%' )"
					+ " order by EmployeeStrID asc";
			
		}else if("3".equals(type)){	//就餐总数（未订餐）
			sql="SELECT convert(varchar,a.EmployeeName) as EmployeeName,"
					+ " convert(varchar,a.EmployeeStrID) as EmployeeStrID"
					+ " FROM YCSCMDB_up6.dbo.TE_ConsumeDetailXF a"
					+ " join YCSCMDB_up6.dbo.TC_EMPLOYEE b on a.employeeid=b.employeeid"
					+ " where "
					+ " a.mealTypeName='早餐' and a.departmentId not like 'Train%' "
					+ " and CONVERT(varchar(10),a.consumeDate, 23)='"+date+"' "
					+ " and a.employeeName not like '%测%试%' "
					+ " and b.userid not in ("
					+ " SELECT user_id"
					+ " FROM TRAIN_T_TEACHERORDER"
					+ " where dinner_date='"+date+"'"
					+ " )"
					+ "order by EmployeeStrID asc";
			
		}else if("4".equals(type)){	//订餐且多刷总数
			sql="SELECT distinct convert(varchar,a.EmployeeName) as EmployeeName,"
					+ " convert(varchar,a.EmployeeStrID) as EmployeeStrID,"
					+ " count(a.EmployeeName)-1 as number "
					+ " FROM YCSCMDB_up6.dbo.TE_ConsumeDetailXF a"
					+ " join YCSCMDB_up6.dbo.TC_EMPLOYEE b on a.employeeid=b.employeeid"
					+ " join TRAIN_T_TEACHERORDER c on b.userId=c.user_id"
					+ " where a.mealTypeName='早餐' and a.departmentId not like 'Train%' "
					+ " and c.dinner_date='"+date+"' "
					+ " and CONVERT(varchar(10),consumeDate, 23)=c.dinner_date "
					+ " and a.employeeName not like '%测%试%' "
					+ " group by a.EmployeeName,a.EmployeeStrID"
					+ " having count(a.EmployeeName)>1"
					+ " order by EmployeeStrID desc";
			
		}
		
		
		List<Map<String,Object>> lists=thisService.getForValuesToSql(sql);
		
		String strData = jsonBuilder.buildObjListToJson((long) lists.size(), lists, true);
		writeJSON(response, strData);

	}
	
	
	
	/**
	 * 教职工订餐就餐差异汇总数据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "/getTeacherOrderDiffTotalList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTeacherOrderDiffTotalList( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
	     
		String strData = ""; // 返回给js的数据
		String year=request.getParameter("YEAR");	
		String month=request.getParameter("MONTH");
		month=month==null?"":month;	
		
		Integer pageIndex=Integer.parseInt(request.getParameter("page"));;	//page
		Integer pageSize=Integer.parseInt(request.getParameter("limit"));;	//limit
		
		StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TeacherDinnerTotalReportSearch] ");
		sql.append("'" + year + "',");
		sql.append("'" + month + "',");		
		String page=pageIndex + "," + pageSize;

		List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);
			

		int count=Integer.parseInt(lists.get(0).get("rownum").toString());
		
		lists.remove(0);
		
		strData = jsonBuilder.buildObjListToJson(Long.valueOf(count), lists, true);// 处理数据
		
		writeJSON(response, strData);// 返回数据
	}
	
	/**
	 * 导出订餐差异汇总信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportTeacherOrderDiffTotalExcel")
	public void exportTeacherOrderDiffTotalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTeacherOrderDiffTotalIsEnd", "0");
		request.getSession().removeAttribute("exportTeacherOrderDiffTotalIsState");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		//SimpleDateFormat fmtDateWeek = new SimpleDateFormat("yyyy年M月d日 （E）");
		//SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm");
		
		List<Map<String, Object>> allList = new ArrayList<>();
			
		try{
			String year=request.getParameter("YEAR");	
			String month=request.getParameter("MONTH");
					
			String date=year+"年";
			if(month!=null)
				date+=month+"月";
			else{
				month="";	
			}
			
			Integer pageIndex=0;
			Integer pageSize=Integer.MAX_VALUE;
			
			StringBuffer sql = new StringBuffer("EXEC [dbo].[Usp_RPT_TeacherDinnerTotalReportSearch] ");
			sql.append("'" + year + "',");
			sql.append("'" + month + "',");		
			String page=pageIndex + "," + pageSize;

			List<Map<String, Object>> lists=thisService.getForValuesToSql(sql.toString()+page);
					
			lists.remove(0);
					
			// 处理基本数据				
			List<Map<String, String>> exportList = new ArrayList<>();
			Map<String, String> tempMap = null;
			for (Map<String, Object> dinnerDiffMap : lists) {
								
				tempMap=new LinkedHashMap<>();
				tempMap.put("rownum", String.valueOf(dinnerDiffMap.get("rownum")));
				tempMap.put("EmployeeName", String.valueOf(dinnerDiffMap.get("EmployeeName")));
				tempMap.put("EmployeeStrID", String.valueOf(dinnerDiffMap.get("EmployeeStrID")));
				tempMap.put("realCount", String.valueOf(dinnerDiffMap.get("realCount")));
				tempMap.put("dinnerCount", String.valueOf(dinnerDiffMap.get("dinnerCount")));
				tempMap.put("realDinnerCount", String.valueOf(dinnerDiffMap.get("realDinnerCount")));
				tempMap.put("notDinnerEatCount", String.valueOf(dinnerDiffMap.get("notDinnerEatCount")));
				tempMap.put("notEatdinnerCount", String.valueOf(dinnerDiffMap.get("notEatdinnerCount")));
				tempMap.put("exceedEatCount", String.valueOf(dinnerDiffMap.get("exceedEatCount")));			
				exportList.add(tempMap);
			}
						
			// --------2.组装表格数据
			Map<String, Object> orderAllMap = new LinkedHashMap<>();
			orderAllMap.put("data", exportList);
			orderAllMap.put("title", date+"-订餐就餐差异汇总信息表");
			orderAllMap.put("head", new String[] { "序号","姓名","编号","就餐份数","订餐份数","就餐份数（已订餐）","就餐份数（未订餐）","多刷卡份数","未就餐份数（已订餐）" }); // 规定名字相同的，设定为合并
			orderAllMap.put("columnWidth", new Integer[] { 10, 15, 12, 12, 12,20,20,20,22 }); // 30代表30个字节，15个字符
			orderAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0,0,0,0,0}); // 0代表居中，1代表居左，2代表居右
			orderAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
			allList.add(orderAllMap);
	
			// 在导出方法中进行解析		
			boolean result = PoiExportExcel.exportExcel(response, date+"-订餐就餐差异汇总信息表", "订餐就餐差异汇总信息表", allList);
			if (result == true) {
				request.getSession().setAttribute("exportTeacherOrderDiffTotalIsEnd", "1");
			} else {
				request.getSession().setAttribute("exportTeacherOrderDiffTotalIsEnd", "0");
				request.getSession().setAttribute("exportTeacherOrderDiffTotalIsState", "0");
			}
		}catch(Exception e){
			request.getSession().setAttribute("exportTeacherOrderDiffTotalIsEnd", "0");
			request.getSession().setAttribute("exportTeacherOrderDiffTotalIsState", "0");
		}
	}
	
	/**
	 * 判断是否导出完毕
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportTeacherOrderDiffTotalEnd")
	public void checkExportTeacherOrderDiffTotalEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTeacherOrderDiffTotalIsEnd");
		Object state = request.getSession().getAttribute("exportTeacherOrderDiffTotalIsState");
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
