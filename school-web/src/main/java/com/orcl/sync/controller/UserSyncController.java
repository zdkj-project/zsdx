package com.orcl.sync.controller;

import com.orcl.sync.model.hibernate.hibernate.*;
import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.CustomerContextHolder;
import com.zd.core.util.DBContextHolder;
import com.zd.core.util.DateUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.plartform.baseset.model.*;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.baseset.service.BaseUserdeptjobService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Controller
@RequestMapping("/usersync")
public class UserSyncController extends FrameWorkController<DocSendcheck> implements Constant {

    private static Logger logger = Logger.getLogger(UserSyncController.class);

    private static final HttpServletRequest HttpServletRequest = null;
    private static final HttpServletResponse HttpServletResponse = null;
    private static final HttpServletRequest HttpServletRequest1 = null;
    private static final HttpServletResponse HttpServletResponse1 = null;
    private static final HttpServletRequest HttpServletRequestDeptJob = null;
    private static final HttpServletResponse HttpServletResponseDeptJob = null;
    private static final HttpServletRequest HttpServletRequestUserDeptJob = null;
    private static final HttpServletResponse HttpServletResponseUserDeptJob = null;

    @Resource
    //orcl的session工厂
    private SessionFactory sssssss;
    @Resource
    private BaseOrgService orgService;
    @Resource
    SysRoleService roleService;


    @Resource
    private SysUserService userservice;


    @Resource
    private BaseJobService jobservice;
    @Resource
    private BaseDeptjobService deptjobService;
    @Resource
    private BaseUserdeptjobService userdeptjobService;


    @RequestMapping(value = "list")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("UserSyncIsEnd", "0");
        request.getSession().removeAttribute("UserSyncState");
        try {
            dept(HttpServletRequest, HttpServletResponse);
            job(HttpServletRequest1, HttpServletResponse1);
            deptjob(HttpServletRequestDeptJob, HttpServletResponseDeptJob);

            //启用orcl数据库
            CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

            //3.打开session
            Session session = sssssss.openSession();
            //4.开启事务
            session.beginTransaction();

            Query query = session.createQuery("from HrUser where accounts is not null ");

            List<HrUser> list = query.list();

            //6.提交事务
            session.getTransaction().commit();
            //7.关闭session
            session.flush();
            //8.关闭session工厂
            //  sssssss.close();

            //缺省的角色
            String[] param = {"roleName", "isDelete"};
            Object[] values = {"教师", 0};
            SysRole defaultRole = roleService.getByProerties(param, values);
            Map<String, String> mapDicItme = new HashMap<>();
            mapDicItme.put("男", "1");
            mapDicItme.put("女", "2");
            String defaultPwd = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
            SysUser u = null;
            CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
            userservice.executeSql("delete BASE_T_USERDEPTJOB where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
//            userservice.executeSql("delete SYS_T_ROLEUSER where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
            Date justDate = new Date();
            String justYear = DateUtil.formatDate(justDate,"yyyy");
            Integer integer = 1;
            String userNumb = "";
            for (HrUser d : list) {
                userNumb = "";
                u = userservice.get(d.getId());
                if (u == null) {
                    u = new SysUser(d.getId());
                    userNumb = justYear + StringUtils.addString(integer.toString(), "0", 6,"L");
                    u.setXm(d.getUserName());
                    u.setUserName(d.getAccounts());
                    u.setUserPwd(defaultPwd);
                    u.setUserNumb(userNumb);
                    u.setIsDelete(d.getIsOnthejob() == 0 ? 1 : 0);
                    u.setCategory("1");
                    u.setState(d.getIsEnable() == 0 ? "1" : "0");
                    u.setIssystem(1);
                    u.setIsHidden("0");
                    if (d.getUserSex() != null) {
                        u.setXbm(mapDicItme.get(d.getUserSex()));
                    }
                    u.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
                    u.setCreateTime(d.getCreateDate());
                    u.setCreateUser(d.getCreateName());

                    Set<SysRole> theUserRole = u.getSysRoles();
                    theUserRole.add(defaultRole);
                    u.setSysRoles(theUserRole);
                    integer++;
                    userservice.merge(u);
                }else {
                	 u.setXm(d.getUserName());
                     u.setUserName(d.getAccounts());
                     u.setIsDelete(d.getIsOnthejob() == 0 ? 1 : 0);
//                     u.setState(d.getIsEnable() == 0 ? "1" : "0");
                     if (d.getUserSex() != null) {
                         u.setXbm(mapDicItme.get(d.getUserSex()));
                     }
                     u.setCreateTime(d.getCreateDate());
                     u.setCreateUser(d.getCreateName());
                }
            }

            //}

            userdeptjob(HttpServletRequestUserDeptJob, HttpServletResponseUserDeptJob);
            syncDeptToUp(HttpServletRequestUserDeptJob, HttpServletResponseUserDeptJob);
            request.getSession().setAttribute("UserSyncIsEnd", "1");
            writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute("UserSyncState", "0");
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute("UserSyncState", "0");
        }
    }


    @RequestMapping(value = "dept")
    public void dept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createQuery("from HrDepartment");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<HrDepartment> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        //session.flush();

        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        String sql = "update BASE_T_ORG set isdelete=1 where parent_node!='ROOT'";
        orgService.executeSql(sql);
        BaseOrg org = null;
        for (HrDepartment hd : list) {
            org = orgService.get(hd.getId());
            if (org == null)
                org = new BaseOrg(hd.getId());
            org.setCreateTime(hd.getCreateDate());
            org.setCreateUser(hd.getCreateName());
            org.setIsDelete(hd.getIsEnable() == 0 ? 1 : 0);
            org.setUpdateTime(hd.getUpdateDate());
            org.setUpdateUser(hd.getUpdateName());
            org.setDeptType("03");
            org.setParentNode(hd.getParentid());
            org.setNodeCode(hd.getCode());
            org.setNodeText(hd.getNames());
            org.setIssystem(0);
            org.setOrderIndex(hd.getOrderby());
            org.setExtField01(hd.getParentid());

            orgService.merge(org);
        }
        //更新一些非同步内容
        String rootId = "2851655E-3390-4B80-B00C-52C7CA62CB39";
        String rootCode = "001";
        sql = MessageFormat.format("EXECUTE [ORG_P_UPDATESYNC] ''{0}'',''{1}'',''{2}''", rootId, rootId, rootCode);
        List<?> alist = orgService.doQuerySql(sql);
        writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));

    }

    @RequestMapping(value = "job")
    public void job(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createQuery("from HrPosition");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<HrPosition> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        //session.flush();

        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        String sql = "update BASE_T_JOB set isdelete=1";
        jobservice.executeSql(sql);
        BaseJob b = null;
        for (HrPosition h : list) {
            b = jobservice.get(h.getId());
            if (b == null) {
                b = new BaseJob(h.getId());
            }
            b.setCreateTime(h.getCreateDate());
            b.setCreateUser(h.getCreateName());
            b.setUpdateTime(h.getUpdateDate());
            b.setCreateUser(h.getUpdateName());
            b.setOrderIndex(h.getOrderby());
            b.setIsDelete(0);
            b.setJobName(h.getNames());
            b.setRemark(h.getDuty());

            jobservice.merge(b);
        }

        writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
    }

    //新岗位对应部门
    @RequestMapping(value = "deptjob")
    public void deptjob(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createQuery("from HrDeptPosition");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<HrDeptPosition> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        //session.flush();
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        String sql = "delete from BASE_T_DEPTJOB";
        deptjobService.executeSql(sql);
        BaseDeptjob dj = null;
        for (HrDeptPosition hdp : list) {
            dj = deptjobService.get(hdp.getId());
            if (dj == null) {
                dj = new BaseDeptjob(hdp.getId());
            }
            dj.setDeptId(hdp.getDepartmentId());
            dj.setJobId(hdp.getPositionId());
            dj.setParentdeptId(hdp.getPredepartmentId());
            dj.setParentjobId(hdp.getPrepositionId());
            dj.setCreateUser(hdp.getCreateName());
            dj.setCreateTime(hdp.getCreateDate());
            dj.setUpdateUser(hdp.getUpdateName());
            dj.setUpdateTime(hdp.getUpdateDate());
            dj.setIsDelete(0);
            dj.setJobType(2);
            deptjobService.merge(dj);
        }

        writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
    }

    //新人员对应部门对应岗位
    @RequestMapping(value = "userdeptjob")
    public void userdeptjob(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //启用orcl数据库
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);

        //3.打开session
        Session session = sssssss.openSession();
        //4.开启事务
        session.beginTransaction();

        Query query = session.createQuery("from HrUserDepartmentPosition where departmentId is not null and deptPositionId is not null");
        //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
        List<HrUserDepartmentPosition> list = query.list();

        //6.提交事务
        session.getTransaction().commit();
        //7.关闭session
        //session.flush();
        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
        String mapKdy = "";
        Map<String, String> mapDeptJob = new HashMap<>();
        List<BaseDeptjob> deptjobList = deptjobService.queryByProerties("isDelete", 0);
        for (BaseDeptjob baseDeptjob : deptjobList) {
            mapKdy = baseDeptjob.getDeptId() + "," + baseDeptjob.getJobId();
            mapDeptJob.put(mapKdy, baseDeptjob.getUuid());
            mapKdy = "";
        }
        String sql = "delete from BASE_T_USERDEPTJOB";
        userdeptjobService.executeSql(sql);
        BaseUserdeptjob udj = null;
        for (HrUserDepartmentPosition hudp : list) {
            udj = new BaseUserdeptjob(hudp.getId());
            udj.setUserId(hudp.getSysUserId());
            udj.setDeptId(hudp.getDepartmentId());
            udj.setJobId(hudp.getDeptPositionId());
            udj.setMasterDept(hudp.getMasterflag());
            udj.setCreateUser(hudp.getCreateBy());
            udj.setCreateTime(hudp.getCreateDate());
            mapKdy = hudp.getDepartmentId() + "," + hudp.getDeptPositionId();
            udj.setDeptjobId(mapDeptJob.get(mapKdy));
            userdeptjobService.merge(udj);
            mapKdy = "";
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
    }

    @RequestMapping("/checkUserSyncEnd")
    public void checkUserSyncEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object isEnd = request.getSession().getAttribute("UserSyncIsEnd");
        Object state = request.getSession().getAttribute("UserSyncState");
        if (isEnd != null) {
            if ("1".equals(isEnd.toString())) {
                writeJSON(response, jsonBuilder.returnSuccessJson("\"同步完成！\""));
            } else if (state != null && state.equals("0")) {
                writeJSON(response, jsonBuilder.returnFailureJson("0"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("\"同步未完成！\""));
            }
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("\"同步未完成！\""));
        }
    }

    @RequestMapping(value = "deptToUp")
    public void syncDeptToUp(HttpServletRequest request, HttpServletResponse response) throws IOException{
        StringBuffer returnJson = null;
        // 1.查询这个smallDeptId的最新的部门信息
        String sql = "select EXT_FIELD04 as departmentId,EXT_FIELD05 as parentDepartmentId,"
                + "	NODE_TEXT as departmentName,convert(varchar,NODE_LEVEL) as layer,"
                + " convert(varchar,ORDER_INDEX) as layerorder  "
                + " from BASE_T_ORG"
                + " where isdelete=0"
                + " order by DepartmentID asc";

        List<BaseOrgToUP> deptInfo = orgService.doQuerySqlObject(sql, BaseOrgToUP.class);

        //2.进入事物之前切换数据源
        DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
        int row = 0;
        if(deptInfo.size()!=0){
            row = orgService.syncAllDeptInfoToUP(deptInfo);
        }
        DBContextHolder.clearDBType();
        if(row==0){
            returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"未有部门数据需要同步！\"}");
        }else if(row>0){
            returnJson = new StringBuffer("{ \"succes\" : true, \"msg\":\"同步部门数据成功！\"}");
        }else{
            returnJson = new StringBuffer("{ \"succes\" : false, \"msg\":\"同步部门数据到UP失败，请联系管理员！\"}");
        }

        writeJSON(response, returnJson.toString());
    }
    
    /**
     * zzk修改
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "syncOaUserAndDept")
    public void syncOaUserAndDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("UserSyncIsEnd", "0");
        request.getSession().removeAttribute("UserSyncState");
        try {
        	 //启用orcl数据库
	        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
	        Session session = sssssss.openSession();
	        session.beginTransaction();
	        
	        //1:查询OA的部门数据
	        Query query = session.createQuery("from HrDepartment");
	        List<HrDepartment> deptList = query.list();
	        
	        //2:查询OA的岗位数据
	        query = session.createQuery("from HrPosition");
	        List<HrPosition> jobList = query.list();
	        
	        //3:查询OA的部门岗位数据
	        query = session.createQuery("from HrDeptPosition");
	        List<HrDeptPosition> deptJobList = query.list();
	        
	        query = session.createQuery("from HrUserDepartmentPosition where departmentId is not null and deptPositionId is not null");
	        List<HrUserDepartmentPosition> userDeptList = query.list();
	        
	        //4：查询用户数据
            query = session.createQuery("from HrUser where accounts is not null ");
            List<HrUser> userList = query.list();

	        //提交事务
	        session.getTransaction().commit();
	        //关闭session
	        session.flush();
	        session.close();
	        
	        //切换回Q1
	        CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
	        
	        Integer state=userservice.doSyncOaUserandDept(deptList,jobList,deptJobList,userDeptList,userList);
	        
	        if(state==1){
	        
	        	//同步成功之后，同步数据到UP库
	        	// 1.查询这个smallDeptId的最新的部门信息
	            String sql = "select EXT_FIELD04 as departmentId,EXT_FIELD05 as parentDepartmentId,"
	                    + "	NODE_TEXT as departmentName,convert(varchar,NODE_LEVEL) as layer,"
	                    + " convert(varchar,ORDER_INDEX) as layerorder  "
	                    + " from BASE_T_ORG"
	                    + " where isdelete=0"
	                    + " order by DepartmentID asc";

	            List<BaseOrgToUP> deptInfo = orgService.doQuerySqlObject(sql, BaseOrgToUP.class);
	            
	           // 2.查询最新的用户、部门信息
				sql = "select  u.USER_ID as userId,u.XM as employeeName, u.user_numb as employeeStrId,"
						+ "'' as employeePwd,CASE u.XBM WHEN '2' THEN '0' ELSE '1' END AS sexId,u.isDelete as isDelete,"
						+ "u.SFZJH AS identifier,'1' AS cardState, " // cardState
																		// 和 sid
																		// 都置默认值，现在不做特定的处理
						+ "'' as sid,org.EXT_FIELD04 as departmentId,"
						// + "job.JOB_NAME as jobName " 不使用这个job数据了，转而使用编制来判断是否为合同工
						+ "(" + "	select ITEM_NAME from BASE_T_DICITEM " + "		where ITEM_CODE=u.ZXXBZLB "
						+ "			and DIC_ID= (select top 1 DIC_ID from BASE_T_DIC where DIC_CODE='ZXXBZLB')"
						+ ") as jobName " + " from SYS_T_USER u" + " join BASE_T_ORG org on "
						+ "		(select top 1 DEPT_ID from BASE_T_UserDeptJOB where USER_ID=u.USER_ID and ISDELETE=0 order by master_dept desc,CREATE_TIME desc)=org.dept_ID "
						// + " join BASE_T_JOB job on "
						// + " (select top 1 JOB_ID from BASE_T_UserDeptJOB where
						// USER_ID=u.USER_ID and ISDELETE=0 order by master_dept
						// desc,CREATE_TIME desc)=job.JOB_ID "
						+ " where xm not like '%管理员%' and XM not Like '%测试%' and XM not like '%test%'"
						+ " order by userId asc";

				List<SysUserToUP> userInfos = userservice.doQuerySqlObject(sql, SysUserToUP.class);

				
	            //2.进入事物之前切换数据源
	            DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);	         
	            if(deptInfo.size()!=0){
	                orgService.syncAllDeptInfoToUP(deptInfo);
	            }
				if (userInfos.size() > 0) {
					userservice.syncUserInfoToAllUP(userInfos, null);
				} 
				
			    DBContextHolder.clearDBType();
			       
	          
	            request.getSession().setAttribute("UserSyncIsEnd", "1");
	        }else{
	        	request.getSession().setAttribute("UserSyncIsEnd", "0");
				request.getSession().setAttribute("UserSyncState", "0");
	        }
	        
        } catch (Exception e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute("UserSyncState", "0");
        }
    }
}
