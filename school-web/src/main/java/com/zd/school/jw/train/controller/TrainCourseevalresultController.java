
package com.zd.school.jw.train.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.model.TrainCourseevalresult;
import com.zd.school.jw.train.service.TrainCourseevalresultService;
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
}
