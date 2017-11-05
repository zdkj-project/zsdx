
package com.zd.school.plartform.system.controller;

import com.zd.core.constant.AuthorType;
import com.zd.core.constant.Constant;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.DBContextHolder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.PoiExportExcel;
import com.zd.core.util.StringUtils;
import com.zd.core.util.exportMeetingInfo;
import com.zd.school.excel.FastExcel;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.model.BaseUserdeptjob;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.baseset.service.BaseUserdeptjobService;
import com.zd.school.plartform.system.model.*;
import com.zd.school.plartform.system.service.SysMenuService;
import com.zd.school.plartform.system.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 
 * ClassName: BaseTUserController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户管理实体Controller. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends FrameWorkController<SysUser> implements Constant {

	@Resource
	SysUserService thisService; // service层接口

	@Resource
	SysMenuService sysMenuService;

	@Resource
	BaseUserdeptjobService userDeptjobService;
	
	@Resource
	BaseDicitemService dicitemService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute SysUser entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		String deptId = request.getParameter("deptId");

		deptId = deptId == null ? "2851655E-3390-4B80-B00C-52C7CA62CB39" : deptId;

		// 若为学校部门，则查询出所有的用户
		if (deptId.equals("2851655E-3390-4B80-B00C-52C7CA62CB39")) {
			
			QueryResult<SysUser> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
					super.sort(request), super.filter(request), true);
			strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		
		} else {
			List<BaseUserdeptjob> udj = userDeptjobService.queryByProerties("deptId", deptId);
			
			String userIds = "";
			for (int i = 0; i < udj.size(); i++) {
				userIds += "'" + udj.get(i).getUserId() + "',";
			}

			if (userIds.trim().length() > 0){	//若又在用户，就去查询
				
				userIds = StringUtils.trimLast(userIds);
				
				SysUser currentUser = getCurrentSysUser();
				QueryResult<SysUser> qr = thisService.getDeptUser(super.start(request), super.limit(request),
						super.sort(request), super.filter(request), true, userIds, currentUser);
				strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
			
			}else{
				strData = jsonBuilder.buildObjListToJson((long) 0, new ArrayList<>(), true);// 处理数据
			}			
		}

		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @throws Exception
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BaseTUser
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(SysUser entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userName = entity.getUserName();
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		if (thisService.IsFieldExist("userName", userName, "-1")) {
			writeJSON(response, jsonBuilder.returnFailureJson("'用户名不能重复！'"));
			return;
		}

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();

		entity = thisService.doAddUser(entity, currentUser);

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
		String orgId = request.getParameter("deptId");
		SysUser currentUser = getCurrentSysUser();
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.doDeleteUser(delIds, orgId, currentUser);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * BaseTUser @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(SysUser entity, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userName = entity.getUserName();
		String userId = entity.getUuid();
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		if (thisService.IsFieldExist("userName", userName, userId)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'用户名不能重复！'"));
			return;
		}
		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();

		entity = thisService.doUpdateUser(entity, currentUser);

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	@RequestMapping("/getUserMenuTree")
	public void getUserMenuTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String node = request.getParameter("node");
		String excludes = request.getParameter("excludes");
		String strData = null;
		if (StringUtils.isEmpty(node) || TreeVeriable.ROOT.equalsIgnoreCase(node)) {
			node = TreeVeriable.ROOT;
		}
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		SysUser currentUser = (SysUser) session.getAttribute(SESSION_SYS_USER);

		List<SysMenuTree> lists = sysMenuService.getPermTree(node, currentUser.getUuid(), AuthorType.USER, true, false);
		strData = jsonBuilder.buildList(lists, excludes);
		writeJSON(response, strData);
	}

	/**
	 * 获取部分菜单的任务数量
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getUserMenuTask")
	public void getUserMenuTask(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			String hql = "select COUNT(*) from TrainClass o where o.isDelete=0 and ((o.isuse=1 and o.isarrange!=1) or o.isuse=3 )";
			Integer count1 = thisService.getCount(hql);

			List<Map<String, Object>> returnList = new ArrayList<>();

			Map<String, Object> map1 = new HashMap<>();
			map1.put("name", "TRAINARRANGE");
			map1.put("value", count1);

			returnList.add(map1);

			String strData = jsonBuilder.toJson(returnList);
			writeJSON(response, jsonBuilder.returnSuccessJson(strData));

		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请重试或联系管理员！\""));
		}
	}

	/**
	 * 
	 * getUserRolelist:用户所属角色列表
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping(value = { "/userrolelist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getUserRolelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userId = request.getParameter("userId"); // 获得传过来的roleId
		SysUser sysUser = thisService.get(userId);
		Integer count = 0;
		List<SysRole> userRole = new ArrayList<SysRole>();
		if (ModelUtil.isNotNull(sysUser)) {
			//userRole = sysUser.getSysRoles();
			userRole=thisService.getSysRoleList(sysUser);
			count = userRole.size();
		}
		String strData = jsonBuilder.buildObjListToJson(new Long(count), userRole, true);
		writeJSON(response, strData);
	}
	
	

	/**
	 * 
	 * deleteUserRole:删除用户所属的角色.
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping(value = { "/deleteUserRole" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void deleteUserRole(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userId = request.getParameter("userId"); // 获得传过来的用户ID
		String ids = request.getParameter("ids");
		SysUser currentUser = getCurrentSysUser();

		if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(userId)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要删除的数据'"));
			return;
		} else {
			boolean flag = thisService.deleteUserRole(userId, ids, currentUser);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	/**
	 * 
	 * addUserRole:增加用户所属的角色.
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping(value = { "/addUserRole" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void addUserRole(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userId = request.getParameter("userId"); // 获得传过来的用户ID
		String ids = request.getParameter("ids");
		SysUser currentUser = getCurrentSysUser();

		if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(userId)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要添加的数据'"));
			return;
		} else {
			boolean flag = thisService.addUserRole(userId, ids, currentUser);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'添加成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'添加失败'"));
			}
		}
	}

	@RequestMapping("/dolock")
	public void doLock(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解锁的账户'"));
			return;
		} else {
			String[] delId = delIds.split(",");
			thisService.updateByProperties("uuid", delId, "state", "1");
			writeJSON(response, jsonBuilder.returnSuccessJson("'锁定成功'"));
		}
	}

	@RequestMapping("/dounlock")
	public void doUnlock(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解锁的账户'"));
			return;
		} else {
			String[] delId = delIds.split(",");
			thisService.updateByProperties("uuid", delId, "state", "0");
			writeJSON(response, jsonBuilder.returnSuccessJson("'解锁成功'"));
		}
	}

	@RequestMapping("/dosetpwd")
	public void doSetpwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要设置密码的账户'"));
			return;
		} else {
			String[] delId = delIds.split(",");
			String userPwd = new Sha256Hash("123456").toHex();
			thisService.updateByProperties("uuid", delId, "userPwd", userPwd);
			writeJSON(response, jsonBuilder.returnSuccessJson("'重置密码成功'"));
		}
	}

	/**
	 * 
	 * batchSetDept:批量将选择的用户加入到指定的部门
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	// @RequestMapping("/batchSetDept")
	// public void batchSetDept(HttpServletRequest request, HttpServletResponse
	// response) throws IOException {
	// String delIds = request.getParameter("ids");
	// String deptId = request.getParameter("deptId");
	// if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(deptId)) {
	// writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要设置的账户'"));
	// return;
	// } else {
	// //String[] delId = delIds.split(",");
	// //thisService.updateByProperties("uuid", delId, "deptId", deptId);
	// SysUser cuurentUser = getCurrentSysUser();
	// boolean flag = thisService.batchSetDept(deptId, delIds, cuurentUser);
	// if (flag)
	// writeJSON(response, jsonBuilder.returnSuccessJson("'添加用户成功'"));
	// else
	// writeJSON(response, jsonBuilder.returnSuccessJson("'添加用户失败'"));
	// }
	// }

	@RequestMapping(value = { "/teacherlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getTeacherlist(@ModelAttribute SysDatapermission entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<SysUser> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping(value = { "/selectedUserlist" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getSelectedUserlist(@ModelAttribute SysDatapermission entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		String ids = request.getParameter("ids");
		String hql = "from SysUser a where uuid in ('" + ids.replace(",", "','") + "')";
		List<SysUser> userList = thisService.doQuery(hql);

		strData = jsonBuilder.buildObjListToJson((long) userList.size(), userList, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}
	
	
	@RequestMapping(value = { "/userDeptJobList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getUserDeptJobList(@ModelAttribute SysDatapermission entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		
		String userId = request.getParameter("userId");
		
		String propName[]={"isDelete","userId"};
		Object propValue[]={0,userId};
		List<BaseUserdeptjob> list = userDeptjobService.queryByProerties(propName, propValue);
	
		strData = jsonBuilder.buildObjListToJson((long) list.size(),list, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}
	
	/**
	 * 将指定的用户绑定到指定的部门岗位上
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/addUserToDeptJob")
	public void addUserToDeptJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String deptJobId = request.getParameter("ids");
		String userId = request.getParameter("setIds");
		if (StringUtils.isEmpty(deptJobId) || StringUtils.isEmpty(userId)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入设置的参数'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			boolean flag = userDeptjobService.addUserToDeptJob(deptJobId, userId, currentUser);
			if (flag)
				writeJSON(response, jsonBuilder.returnSuccessJson("'设置成功'"));
			else
				writeJSON(response, jsonBuilder.returnSuccessJson("'设置失败'"));
		}
	}
	
	/**
	 * 删除用户所在的部门岗位，只是逻辑删除
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/removeUserFromDeptJob")
	public void removeTeacherFromDeptJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解除绑定的部门岗位'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			boolean flag = userDeptjobService.removeUserFromDeptJob(delIds, currentUser);
			if (flag)
				writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定成功'"));
			else
				writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定失败'"));
		}
	}
	
	/**
	 * 调整指定用户的主部门岗位
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/setMasterDeptJob")
	public void setMasterDeptJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		String userId = request.getParameter("setIds");
		if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(userId)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要设置部门岗位'"));
			return;
		} else {
			SysUser currentUser = getCurrentSysUser();
			boolean flag = userDeptjobService.setMasterDeptJob(delIds, userId, currentUser);
			if (flag)
				writeJSON(response, jsonBuilder.returnSuccessJson("'设置主部门成功'"));
			else
				writeJSON(response, jsonBuilder.returnSuccessJson("'设置主部门失败'"));
		}
	}
	

	/*
	 * 单条数据调用同步UP的方式 用于修改单条人员数据的时候进行同步（貌似目前暂时未使用到）
	 */
	@RequestMapping("/doSyncUserInfoToUp/{userId}")
	public void doSyncUserInfoToUp(@PathVariable("userId") String userId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		StringBuffer returnJson = null;
		try {

			// String userId="8a8a8834533a065601533a065ae80234";

			// 1.查询这个userId的最新的用户、部门信息
			String sql = "select top 1 u.USER_ID as userId,u.XM as employeeName, u.user_numb as employeeStrId,"
					+ "'' as employeePwd,CASE u.XBM WHEN '2' THEN '0' ELSE '1' END AS sexId,u.isDelete as isDelete,"
					+ "u.SFZJH AS identifier,'1' AS cardState, " // cardState
																	// 和 sid
																	// 都置默认值，现在不做特定的处理
					+ "'' as sid,org.EXT_FIELD04 as departmentId,"
					//+ "job.JOB_NAME as jobName  "	不使用这个job数据了，转而使用编制来判断是否为合同工
					+"("
					+ "	select ITEM_NAME from BASE_T_DICITEM "
					+ "		where ITEM_CODE=u.ZXXBZLB "
					+ "			and DIC_ID= (select top 1 DIC_ID from BASE_T_DIC where DIC_CODE='ZXXBZLB')"
					+ ") as jobName "
					+ " from SYS_T_USER u" + " join BASE_T_ORG org on "
					+ "		(select top 1 DEPT_ID from BASE_T_UserDeptJOB where USER_ID=u.USER_ID and ISDELETE=0 order by master_dept desc,CREATE_TIME desc)=org.dept_ID "
					//+ " join BASE_T_JOB job on "
					//+ "		(select top 1 JOB_ID from BASE_T_UserDeptJOB where USER_ID=u.USER_ID and ISDELETE=0 order by master_dept desc,CREATE_TIME desc)=job.JOB_ID "
					+ " where u.ISDELETE=0 and u.USER_ID='" + userId + "'";
			

			List<SysUserToUP> userInfo = thisService.doQuerySqlObject(sql, SysUserToUP.class);

			// 2.进入事物之前切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
			int row = 0;
			if (userInfo.size() != 0) {
				row = thisService.syncUserInfoToUP(userInfo.get(0), userId);
			} else {
				row = thisService.syncUserInfoToUP(null, userId);
			}

			if (row == 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"未有人员数据需要同步！\"}");
			} else if (row > 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"同步人员数据成功！\"}");
			} else {
				returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员数据到UP失败，请联系管理员！\"}");
			}

		} catch (Exception e) {
			returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员数据到UP失败，请联系管理员！\"}");
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}

		writeAppJSON(response, returnJson.toString());
	}

	/*
	 * 一键同步UP的方式
	 */
	@RequestMapping("/doSyncAllUserInfoToUp")
	public void doSyncAllUserInfoToUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuffer returnJson = null;
		try {

			// String userId="8a8a8834533a065601533a065ae80234";

			// 1.查询最新的用户、部门信息
			String sql = "select  u.USER_ID as userId,u.XM as employeeName, u.user_numb as employeeStrId,"
					+ "'' as employeePwd,CASE u.XBM WHEN '2' THEN '0' ELSE '1' END AS sexId,u.isDelete as isDelete,"
					+ "u.SFZJH AS identifier,'1' AS cardState, " // cardState
																	// 和 sid
																	// 都置默认值，现在不做特定的处理
					+ "'' as sid,org.EXT_FIELD04 as departmentId,"
					//+ "job.JOB_NAME as jobName  "	不使用这个job数据了，转而使用编制来判断是否为合同工
					+"("
					+ "	select ITEM_NAME from BASE_T_DICITEM "
					+ "		where ITEM_CODE=u.ZXXBZLB "
					+ "			and DIC_ID= (select top 1 DIC_ID from BASE_T_DIC where DIC_CODE='ZXXBZLB')"
					+ ") as jobName "
					+ " from SYS_T_USER u" + " join BASE_T_ORG org on "
					+ "		(select top 1 DEPT_ID from BASE_T_UserDeptJOB where USER_ID=u.USER_ID and ISDELETE=0 order by master_dept desc,CREATE_TIME desc)=org.dept_ID "
					//+ " join BASE_T_JOB job on "
					//+ "		(select top 1 JOB_ID from BASE_T_UserDeptJOB where USER_ID=u.USER_ID and ISDELETE=0 order by master_dept desc,CREATE_TIME desc)=job.JOB_ID "
					+ " where xm not like '%管理员%' and XM not Like '%测试%' and XM not like '%test%'"
					+ " order by userId asc";

			List<SysUserToUP> userInfos = thisService.doQuerySqlObject(sql, SysUserToUP.class);

			// 2.进入事物之前切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
			int row = 0;
			if (userInfos.size() > 0) {
				row = thisService.syncUserInfoToAllUP(userInfos, null);
			} else {
				row = thisService.syncUserInfoToAllUP(null, null);
			}

			if (row == 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"未有人员数据需要同步！\"}");
			} else if (row > 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"同步人员数据成功！\"}");
			} else {
				returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员数据到UP失败，请联系管理员！\"}");
			}

		} catch (Exception e) {
			returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员数据到UP失败，请联系管理员！\"}");
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}

		writeAppJSON(response, returnJson.toString());
	}

	/*
	 * 一键发卡信息的方式
	 */
	@RequestMapping("/doSyncAllCardInfoFromUp")
	public void doSyncAllCardInfoFromUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuffer returnJson = null;
		try {

			// 1.切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			// 2.查询UP中所有的发卡信息
//			String sql = "select convert(varchar,a.CardID) as upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as userId,"
//					+ " convert(int,a.CardStatusIDXF) as useState,"
//					+ " b.EmployeeStrID as sid,b.EmployeeStatusID as employeeStatusID "
//					+ " from Tc_Employee b join TC_Card a on b.CardID=a.CardID" + " where b.UserId is not null "
//					+ "	order by a.CardID asc,a.ModifyDate asc";
			
			//(2017-10-11:作废)修改了查询的方式，以发卡表中的最新的一条数据为准
//			String sql="select a.UserId as userId,a.EmployeeStrID as sid,a.EmployeeStatusID as employeeStatusID,"
//					+ " convert(varchar,b.CardID) as upCardId,convert(varchar,b.FactoryFixID) as factNumb,"
//					+ " convert(int,b.CardStatusIDXF) as useState from Tc_Employee a join TC_Card b"
//					+ " on b.CardID=("
//					+ "		select top 1 CardID from TC_Card where EmployeeID=a.EmployeeID order by ModifyDate desc"
//					+ " ) where a.UserId is not null ";
			
			//(2017-10-11:使用人员表和卡片表，双向关联查出最精确的发卡数据)
			String sql="select B.UserId as userId,replace(B.EmployeeStrID,'NO','') as sid,B.EmployeeStatusID as employeeStatusID,"
					+ "	convert(varchar,A.CardID) as upCardId,convert(varchar,A.FactoryFixID) as factNumb,"
					+ "	convert(int,A.CardStatusIDXF) as useState, convert(int,A.CardTypeID) as cardTypeId "
					+ " from TC_Card A left join Tc_Employee B"
					+ " on A.CardID=B.CardID and A.EmployeeID=B.EmployeeID "
					+ " where A.EmployeeID=B.EmployeeID or A.EmployeeID=0"
					+ " order by A.CardID asc";
					
			
			List<CardUserInfoToUP> upCardUserInfos = thisService.doQuerySqlObject(sql, CardUserInfoToUP.class);

			// 3.恢复数据源
			DBContextHolder.clearDBType();

			int row = 0;
			if (upCardUserInfos.size() > 0) {
				row = thisService.syncAllCardInfoFromUp(upCardUserInfos);
			} else {
				row = thisService.syncAllCardInfoFromUp(null);
			}

			if (row == 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"未有人员数据需要同步！\"}");
			} else if (row > 0) {
				returnJson = new StringBuffer("{ \"success\" : true, \"msg\":\"同步人员数据成功！\"}");
			} else {
				returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员发卡数据失败，请联系管理员！\"}");
			}

		} catch (Exception e) {
			returnJson = new StringBuffer("{ \"success\" : false, \"msg\":\"同步人员发卡数据失败，请联系管理员！\"}");
		}

		writeAppJSON(response, returnJson.toString());
	}

    @RequestMapping(value = {"/getUserNotInRoleId"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public void getUserNotInRoleId(String roleId,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        int start = super.start(request); // 起始记录数
        int limit = super.limit(request);// 每页记录数
        String sort = StringUtils.convertSortToSql(super.sort(request));
        String filter = StringUtils.convertFilterToSql(super.filter(request));
        try {
            QueryResult<SysUser> qr = thisService.getUserNotInRoleId(roleId, start, limit, sort, filter);
            strData = jsonBuilder.buildObjListToJson(new Long(qr.getTotalCount()), qr.getResultList(), true);// 处理数据
            writeJSON(response, strData);// 返回数据
        } catch (IOException e) {
            e.printStackTrace();
            writeJSON(response, jsonBuilder.returnSuccessJson("'数据查询失败'"));
        }
    }
    
    /**
	 * 导出班级学员卡的信息
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/exportCardExcel")
	public void exportCardExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("exportTrainClassTraineeCardIsEnd", "0");
		request.getSession().removeAttribute("exportTrainClassTraineeCardIIsState");

		List<Map<String, Object>> allList = new ArrayList<>();
		Integer[] columnWidth = new Integer[] { 15, 15, 20,20, 20, 20, 15, 20, 15 };

		// 1.班级信息
		String deptId = request.getParameter("deptId"); // 程序中限定每次只能导出一个班级
		
		//数据字典项
		String mapKey = null;
		String[] propValue = { "XBM", "CARDSTATE","ACCOUNTSTATE" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
				mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
				mapDicItem.put(mapKey, baseDicitem.getItemName());
			}

		// 2.班级学员信息
		List<SysUser> sysUserList = null;
		String hql = " from SysUser where (isDelete=0 or isDelete=2) ";
		if (StringUtils.isNotEmpty(deptId)) {
			hql += " and deptId ='" + deptId + "'";
		}
		hql += " order by jobId asc";
		sysUserList = thisService.doQuery(hql);

		// 处理班级基本数据
		List<Map<String, String>> traineeList = new ArrayList<>();
		Map<String, String> traineeMap = null;
		String ClassName="";
		int i=1;
		for (SysUser sysUser : sysUserList) {
			traineeMap = new LinkedHashMap<>();
			ClassName = sysUser.getDeptName();
			traineeMap.put("xh",i+"");
			traineeMap.put("name", sysUser.getXm());
			traineeMap.put("xb",  mapDicItem.get(sysUser.getXbm()+"XBM"));
			traineeMap.put("phone", sysUser.getMobile());
			traineeMap.put("job", sysUser.getJobName());
			traineeMap.put("stustatus", mapDicItem.get(sysUser.getState()+"ACCOUNTSTATE"));
			traineeMap.put("cardNo", String.valueOf((sysUser.getUpCardId()==null)?" ":sysUser.getUpCardId()));
			traineeMap.put("cardPrintNo", sysUser.getCardPrintId());
			traineeMap.put("useState", mapDicItem.get((sysUser.getUseState()==null?0:sysUser.getUseState())+"CARDSTATE"));
			i++;
			traineeList.add(traineeMap);
		}
		// --------2.组装课程表格数据
		Map<String, Object> courseAllMap = new LinkedHashMap<>();
		courseAllMap.put("data", traineeList);
		courseAllMap.put("title", null);
		courseAllMap.put("head", new String[] { "序号","姓名", "性别", "电话","岗位", "账号状态", "卡编号","印刷卡号","卡状态" }); // 规定名字相同的，设定为合并
		courseAllMap.put("columnWidth", columnWidth); // 30代表30个字节，15个字符
		courseAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 , 0}); // 0代表居中，1代表居左，2代表居右
		courseAllMap.put("mergeCondition", null); // 合并行需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
		allList.add(courseAllMap);

		// 在导出方法中进行解析
		boolean result = PoiExportExcel.exportExcel(response, ClassName+"部门卡详细", ClassName+"部门卡信息", allList);
		if (result == true) {
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsEnd", "1");
		} else {
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsEnd", "0");
			request.getSession().setAttribute("exportTrainClassTraineeCardIIsState", "0");
		}
	}
	
	/**
	 * 判断导出时，是否导出完毕
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkExportCardEnd")
	public void checkExportCardEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object isEnd = request.getSession().getAttribute("exportTrainClassTraineeCardIIsEnd");
		Object state = request.getSession().getAttribute("exportTrainClassTraineeCardIIsState");
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
