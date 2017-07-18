
package com.zd.school.plartform.baseset.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseDpetJobTree;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseDeptjobController Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 部门岗位信息(BASE_T_DEPTJOB)实体Controller. date: 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseDeptjob")
public class BaseDeptjobController extends FrameWorkController<BaseDeptjob> implements Constant {

	@Resource
	BaseDeptjobService thisService; // service层接口

	/**
	 * @Title: list
	 * @Description: 查询数据列表
	 * @param entity
	 *            实体类
	 * @param request
	 * @param response
	 * @throws IOException
	 *             设定参数
	 * @return void 返回类型
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute BaseDeptjob entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<BaseDeptjob> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 设置指定部门包含的岗位
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/batchSetDeptJob")
	public void batchSetDeptJob(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String deptId = request.getParameter("deptId");
		String jobId = request.getParameter("ids");
		if (StringUtils.isEmpty(jobId) || StringUtils.isEmpty(jobId)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入设置的参数'"));
			return;
		}
		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		try {
			Boolean flag = thisService.batchSetDeptJob(deptId, jobId, currentUser);
			if (flag)
				writeJSON(response, jsonBuilder.returnSuccessJson("'添加岗位成功'"));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'添加岗位失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'添加岗位失败，请参见错误日志'"));
		}
	}

	/**
	 * 
	 * @Title: doDelete
	 * @Description: 逻辑删除指定的数据
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws IOException
	 *             抛出异常
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
				Boolean flag = thisService.delDeptJob(delIds, currentUser);
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

	@RequestMapping("/chkIsSuperJob")
	public void chkIsSuperJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("ids");
		if (StringUtils.isEmpty(ids)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'没有传入删除的参数'"));
			return;
		}
		try {
			String flag = thisService.chkIsSuperJob(ids);
			if (StringUtils.isEmpty(flag))
				writeJSON(response, jsonBuilder.returnSuccessJson("''"));
			else
				writeJSON(response, jsonBuilder.returnSuccessJson("'" + flag + "'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'检查是否上级岗位失败，请参见错误日志'"));
		}
	}

	@RequestMapping("/setLeaderJob")
	public void setLeaderJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptJobId = request.getParameter("ids");
		String deptId = request.getParameter("deptId");
		if (StringUtils.isEmpty(deptJobId) || StringUtils.isEmpty(deptId)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'参数传入错误'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			try {
				Boolean flag = thisService.setDeptLeaderJob(deptId, deptJobId, currentUser);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'设置成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'设置失败,详情见错误日志'"));
				}
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("'设置失败,详情见错误日志'"));
			}
		}
	}

	@RequestMapping("/getDeptJobTree")
	public void getDeptJobTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		// 得到根节点ID
		String node = request.getParameter("node");
		String nodeId = request.getParameter("nodeId");
		String excludes = request.getParameter("excludes");
		if (!(StringUtils.isNotEmpty(node) && node.equalsIgnoreCase(TreeVeriable.ROOT))) {
			node = TreeVeriable.ROOT;
		}
		if (StringUtils.isNotEmpty(nodeId)) {
			node = nodeId;
		}
		BaseDpetJobTree root = thisService.getDeptJobTree(node, "");
		if (node.equalsIgnoreCase(TreeVeriable.ROOT)) {
			strData = jsonBuilder.buildList(root.getChildren(), excludes);
		} else {
			List<BaseDpetJobTree> alist = new ArrayList<BaseDpetJobTree>();
			alist.add(root);
			strData = jsonBuilder.buildList(root.getChildren(), excludes);
		}
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping("/setSuperJob")
	public void setSuperJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("ids");
		String setIds = request.getParameter("setIds");
		String setType = request.getParameter("types");
		SysUser currentUser = getCurrentSysUser();

		try {
			Boolean flag = thisService.setSuperJob(ids, setIds, setType, currentUser);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'设置成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'设置失败,详情见错误日志'"));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'设置失败,详情见错误日志'"));
		}
	}
	
	
}
