
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.model.TrainCoursecategory;
import com.zd.school.jw.train.model.TrainCourseevalresult;
import com.zd.school.jw.train.service.TrainCourseevalresultService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: TrainCourseevalresultController
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 课程评价结果(TRAIN_T_COURSEEVALRESULT)实体Controller.
 * date: 2017-06-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/TrainCourseevalresult")
public class TrainCourseevalresultController extends FrameWorkController<TrainCourseevalresult> implements Constant {
	private static Logger logger = Logger.getLogger(TrainCourseevalresult.class);
    @Resource
    TrainCourseevalresultService thisService; // service层接口
    
    @Resource
   	BaseDicitemService dicitemService;

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
    public void list(@ModelAttribute TrainCourseevalresult entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<TrainCourseevalresult> qResult = thisService.list(start, limit, sort, filter,true);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

	/**
	 *
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    @RequestMapping("/doadd")
    public void doAdd(TrainCourseevalresult entity, HttpServletRequest request, HttpServletResponse response)
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
	 *
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    @RequestMapping("/doupdate")
    public void doUpdates(TrainCourseevalresult entity, HttpServletRequest request, HttpServletResponse response)
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
	/**
	 * 启动课程评价
	 * @param ids
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/starteval")
	public void doStartEval(String ids,HttpServletRequest request,HttpServletResponse response) throws  IOException{
        request.getSession().setAttribute("startCourseEvalIsEnd", "0");
        request.getSession().removeAttribute("startCourseEvalIsState");
		if(StringUtils.isEmpty(ids)){
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入评价课程,请重新设置'"));
            request.getSession().setAttribute("startCourseEvalIsState", "0");
		}else {
			try {
				Boolean result = thisService.doStartCourseEval(ids);
				if (result == true) {
					request.getSession().setAttribute("startCourseEvalIsEnd", "1");
				} else {
					request.getSession().setAttribute("startCourseEvalIsEnd", "0");
					request.getSession().setAttribute("startCourseEvalIsState", "0");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.getSession().setAttribute("startCourseEvalIsState", "0");
			}

		}
	}
	@RequestMapping("/checkStartEnd")
	public void doCheckStartEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object isEnd = request.getSession().getAttribute("startCourseEvalIsEnd");
		Object state = request.getSession().getAttribute("startCourseEvalIsState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"启动评价完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"启动评价未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"启动评价未完成！\""));
		}
	}
	/**
	 * 汇总课程评价
	 * @param ids
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/sumeval")
	public void doSumEval(String ids,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		if(StringUtils.isEmpty(ids)){
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入汇总课程,请重新设置'"));
		}else {
			try {
				request.getSession().setAttribute("sumClassEvalIsEnd", "0");
				request.getSession().removeAttribute("sumClasEvalIsState");

				Boolean result = thisService.doSumCourseEval(ids);
				if (result == true) {
					request.getSession().setAttribute("sumClassEvalIsEnd", "1");
				} else {
					request.getSession().setAttribute("sumClassEvalIsEnd", "0");
					request.getSession().setAttribute("sumClasEvalIsState", "0");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.getSession().setAttribute("sumClasEvalIsState", "0");
			}

		}
	}
	@RequestMapping("/checkSumEnd")
	public void docheckSumEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object isEnd = request.getSession().getAttribute("sumClassEvalIsEnd");
		Object state = request.getSession().getAttribute("sumClasEvalIsState");
		if (isEnd != null) {
			if ("1".equals(isEnd.toString())) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"汇总完成！\""));
			} else if (state != null && state.equals("0")) {
				writeJSON(response, jsonBuilder.returnFailureJson("0"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"汇总未完成！\""));
			}
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("\"汇总未完成！\""));
		}
	}

    /**
     * 关闭评价
     * @param ids
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/endeval")
    public void doEndEval(String ids, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        if(StringUtils.isEmpty(ids)){
            writeJSON(response, jsonBuilder.returnFailureJson("'没有传入要关闭评价的课程'"));
            return;
        }
        //获取当前的操作用户
        SysUser currentUser = getCurrentSysUser();
        try {
            Boolean endResult = thisService.doEndCourseEval(ids, currentUser);// 执行修改方法
            if (endResult)
                writeJSON(response, jsonBuilder.returnSuccessJson("'关闭评价成功'"));
            else
                writeJSON(response, jsonBuilder.returnFailureJson("'关闭评价失败,详情见错误日志'"));
        } catch (Exception e) {
            writeJSON(response, jsonBuilder.returnFailureJson("'关闭评价失败,详情见错误日志'"));
        }
    }

    /**
     * 获取指定课程的评价结果表
     * @param ids 要获取评价课程Id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getCourseEvalResult")
    public void getCourseEvalResult(String ids, HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String strData = "";
        if(StringUtils.isEmpty(ids)) {
            if (StringUtils.isEmpty(ids)) {
                writeJSON(response, jsonBuilder.returnFailureJson("'没有传入要获取评价结果的课程'"));
                return;
            }
        }
//        Map<String, List<Map<String, Object>>> mapCouseEvalResult = thisService.getCourseEvalResult(ids);
		Map<String, Object> mapCouseEvalResult = thisService.getCourseEvalResultDetail(ids);
		strData = jsonBuilder.toJson(mapCouseEvalResult);
        writeJSON(response, strData);// 返回数据
    }
    
//    @RequestMapping("/exportExcel")
//    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        request.getSession().setAttribute("exportTrainCoursecategoryIsEnd", "0");
//        request.getSession().removeAttribute("exportTrainCoursecategoryIsState");
//        String ids = request.getParameter("ids");
//        String orderSql = request.getParameter("orderSql");
//        String whereSql = request.getParameter("whereSql");
//		Map<String, String> mapHeadshipLevel = new HashMap<>();
//		Map<String, String> mapXbm = new HashMap<>();
//		Map<String, String> mapClassCategory = new HashMap<>();
//		String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM','ZXXBJLX')";
//		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
//		for (BaseDicitem baseDicitem : listBaseDicItems1) {
//			if (baseDicitem.getDicCode().equals("XBM"))
//				mapXbm.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
//			else if (baseDicitem.getDicCode().equals("HEADSHIPLEVEL"))
//				mapHeadshipLevel.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
//			else
//				mapClassCategory.put(baseDicitem.getItemCode(), baseDicitem.getItemName());
//		}
//
//		List<Map<String, Object>> allList = new ArrayList<>();
//
//		// 1.班级信息
//		List<TrainCoursecategory> list = thisService.listExport(ids, orderSql);
//		
//		// 处理学员基本数据
//		List<Map<String, String>> roomList = new ArrayList<>();
//		List<Map<String, String>> parentList = new ArrayList<>();
//		Map<String, String> roomMap = null;
//		Map<String, String> parentMap =null;
//		for (TrainCoursecategory classTrainee : list) {
//			roomMap= new LinkedHashMap<>();
//			String pcn="";
//			if(classTrainee.getNodeLevel()==1){
//				pcn=classTrainee.getParantCategoryName()+"（一级分类）";
//			}else if(classTrainee.getNodeLevel()==2){
//				pcn=classTrainee.getParantCategoryName()+"（二级分类）";
//			}else if(classTrainee.getNodeLevel()==3){
//				pcn=classTrainee.getParantCategoryName()+"（三级分类）";
//			}else{
//				break;
//			}
//			roomMap.put("type", pcn);
//			roomMap.put("name", classTrainee.getNodeText());
//			roomMap.put("code", classTrainee.getNodeCode());
//			roomMap.put("note", classTrainee.getCategoryDesc());
//			roomList.add(roomMap);
//		}
//		
//		Integer[] columnWidth = new Integer[] {35, 35, 20, 45 };
//		
//		Map<String, Object> roomAllMap = new LinkedHashMap<>();
//		roomAllMap.put("data", roomList);
//		roomAllMap.put("head", new String[] { "分类名称","课程名称", "分类编码", "分类说明"}); // 规定名字相同的，设定为合并
//		roomAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
//		roomAllMap.put("columnAlignment", new Integer[] {0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
//		roomAllMap.put("mergeCondition",  new String[] { "type" }); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
//		allList.add(roomAllMap);
//
//		// 在导出方法中进行解析
//		String sheetTitle = "课程分类信息";
//		boolean result = PoiExportExcel.exportExcel(response, "课程分类信息", sheetTitle, allList);
//		if (result == true) {
//			request.getSession().setAttribute("exportTrainClassRoomIsEnd", "1");
//		} else {
//			request.getSession().setAttribute("exportTrainClassRoomIsEnd", "0");
//			request.getSession().setAttribute("exportTrainClassRoomIsState", "0");
//		}
//    }
//
//
//
//    @RequestMapping("/checkExportEnd")
//    public void checkExportEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
// 
//        Object isEnd = request.getSession().getAttribute("exportTrainClassRoomIsEnd");
//        Object state = request.getSession().getAttribute("exportTrainClassRoomIsState");
//        
//        if (isEnd != null) {
//        	if ("1".equals(isEnd.toString())) {
//				writeJSON(response, jsonBuilder.returnSuccessJson("\"文件导出完成！\""));
//			} else if (state != null && state.equals("0")) {
//				writeJSON(response, jsonBuilder.returnFailureJson("0"));
//			} else {
//				writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
//			}
//        } else {
//            writeJSON(response, jsonBuilder.returnFailureJson("\"文件导出未完成！\""));
//            request.getSession().setAttribute("exportTrainClassRoomIsEnd", "0");
//        }
//    }
    
    
    
}
