package com.zd.core.security;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orcl.sync.model.hibernate.hibernate.HrDepartment;
import com.orcl.sync.model.hibernate.hibernate.HrDeptPosition;
import com.orcl.sync.model.hibernate.hibernate.HrPosition;
import com.orcl.sync.model.hibernate.hibernate.HrUser;
import com.orcl.sync.model.hibernate.hibernate.HrUserDepartmentPosition;
import com.zd.core.util.CustomerContextHolder;
import com.zd.core.util.DBContextHolder;
import com.zd.school.plartform.baseset.model.BaseOrgToUP;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysUserToUP;
import com.zd.school.plartform.system.service.SysUserService;

//@Component(value="SyncJobQuartz")
public class SyncJobQuartz {

	@Resource
	private SysUserService thisService; // service层接口
	
	@Resource
    private SessionFactory sssssss;
    @Resource
    private BaseOrgService orgService;
    
	private static Logger logger = Logger.getLogger(SyncJobQuartz.class);

	protected void execute() {
		try {
			int row=0;
//			// 1：同步OA用户、部门数据
//			row=syncOaUserAndDept();
//			if(row>=0){
//				logger.info("定时同步OA人员部门数据成功！");
//			}else{
//				logger.info("定时同步OA人员部门数据失败，详见错误日志！");
//			}
			
			// 2：同步人员数据到UP
			row=SyncUserIntoUp();
			if(row>=0){
				logger.info("定时同步人员数据,影响"+row+"条数据！");
			}else{
				logger.info("定时同步人员数据失败，详见错误日志！");
			}
			
			// 3：同步UP中的发卡数据
			row=SyncUserCardFromUp();
			if(row>=0){
				logger.info("定时同步人员发卡数据,影响"+row+"条数据！");
			}else{
				logger.info("定时同步人员发卡数据失败，详见错误日志！");
			}
			
			// 4：同步OA会议数据


		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getStackTrace());
		}
	}

	private int SyncUserIntoUp() {
		int row = 0;
		try {
			// 1.查询最新的用户、部门信息
			String sql = "select  u.USER_ID as userId,u.XM as employeeName, u.user_numb as employeeStrId,"
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

			List<SysUserToUP> userInfos = thisService.doQuerySqlObject(sql, SysUserToUP.class);

			// 2.进入事物之前切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
			
			if (userInfos.size() > 0) {
				row = thisService.syncUserInfoToAllUP(userInfos, null);
			} else {
				row = thisService.syncUserInfoToAllUP(null, null);
			}

		} catch (Exception e) {
			row = -1;
			logger.error(e.getStackTrace());
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}

		return row;
	}
	
	private int SyncUserCardFromUp() {
		int row = 0;

		try {
			// 1.切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			// 2.查询UP中所有的发卡信息
//						String sql = "select convert(varchar,a.CardID) as upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as userId,"
//								+ " convert(int,a.CardStatusIDXF) as useState,"
//								+ " b.EmployeeStrID as sid,b.EmployeeStatusID as employeeStatusID "
//								+ " from Tc_Employee b join TC_Card a on b.CardID=a.CardID" + " where b.UserId is not null "
//								+ "	order by a.CardID asc,a.ModifyDate asc";
			
			//(2017-10-11:作废)修改了查询的方式，以发卡表中的最新的一条数据为准
//						String sql="select a.UserId as userId,a.EmployeeStrID as sid,a.EmployeeStatusID as employeeStatusID,"
//								+ " convert(varchar,b.CardID) as upCardId,convert(varchar,b.FactoryFixID) as factNumb,"
//								+ " convert(int,b.CardStatusIDXF) as useState from Tc_Employee a join TC_Card b"
//								+ " on b.CardID=("
//								+ "		select top 1 CardID from TC_Card where EmployeeID=a.EmployeeID order by ModifyDate desc"
//								+ " ) where a.UserId is not null ";
			
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
						
			if (upCardUserInfos.size() > 0) {
				row = thisService.syncAllCardInfoFromUp(upCardUserInfos);
			} else {
				row = thisService.syncAllCardInfoFromUp(null);
			}			

		} catch (Exception e) {
			row = -1;
			logger.error(e.getStackTrace());
		}

		return row;
	}
	
    private int syncOaUserAndDept() {
    	int row = 0;
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
	        
	        Integer state=thisService.doSyncOaUserandDept(deptList,jobList,deptJobList,userDeptList,userList);
	        
	        row=1;
        } catch (Exception e) {
        	row = -1;
            logger.error(e.getMessage());         
        }
        return row;
    }
}
