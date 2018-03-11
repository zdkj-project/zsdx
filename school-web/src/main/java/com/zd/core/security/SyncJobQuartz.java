package com.zd.core.security;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.zd.core.util.DBContextHolder;
import com.zd.school.oa.meeting.service.OaMeetingService;
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
	private BaseOrgService orgService;

	@Resource
	private OaMeetingService meetingService;

	private static Logger logger = Logger.getLogger(SyncJobQuartz.class);

	protected void execute() {
		try {
			int row = 0;
			// 1：同步OA用户、部门数据
			row = syncOaUserAndDept();
			if (row >= 0) {
				logger.info("定时同步OA人员部门数据成功！");
			} else {
				logger.info("定时同步OA人员部门数据失败，详见错误日志！");
			}

			// 2：同步部门数据到UP
			row = SyncDeptIntoUp();
			if (row >= 0) {
				logger.info("定时同步部门数据成功！");
			} else {
				logger.info("定时同步部门数据失败，详见错误日志！");
			}

			// 3：同步人员数据到UP
			row = SyncUserIntoUp();
			if (row >= 0) {
				logger.info("定时同步人员数据成功！");
			} else {
				logger.info("定时同步人员数据失败，详见错误日志！");
			}

			// 4：同步UP中的发卡数据
			row = SyncUserCardFromUp();
			if (row >= 0) {
				logger.info("定时同步人员发卡数据成功！");
			} else {
				logger.info("定时同步人员发卡数据失败，详见错误日志！");
			}

			// 5：同步OA会议数据
			row = syncOaMeeting();
			if (row >= 0) {
				logger.info("定时同步OA会议数据成功！");
			} else {
				logger.info("定时同步OA会议数据失败，详见错误日志！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getStackTrace());
		}
	}

	private int SyncDeptIntoUp() {
		int row = 0;
		try {
			// 1.查询最新的用户、部门信息
			String sql = "select EXT_FIELD04 as departmentId,EXT_FIELD05 as parentDepartmentId,"
					+ "	NODE_TEXT as departmentName,convert(varchar,NODE_LEVEL) as layer,"
					+ " convert(varchar,ORDER_INDEX) as layerorder  " + " from BASE_T_ORG" + " where isdelete=0"
					+ " order by DepartmentID asc";

			List<BaseOrgToUP> deptInfos = thisService.doQuerySqlObject(sql, BaseOrgToUP.class);

			// 2.进入事物之前切换数据源
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

			if (deptInfos.size() > 0) {
				row = orgService.syncAllDeptInfoToUP(deptInfos);
			}

		} catch (Exception e) {
			row = -1;
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
		} finally {
			// 恢复数据源
			DBContextHolder.clearDBType();
		}

		return row;
	}

	private int SyncUserIntoUp() {
		int row = 0;
		try {
			// 1.查询最新的用户、部门信息
			String sql = "select  u.USER_ID as userId,u.XM as employeeName, u.user_numb as employeeStrId,"
					+ "'' as employeePwd,CASE u.XBM WHEN '2' THEN '0' ELSE '1' END AS sexId,u.isDelete as isDelete,"
					+ "u.SFZJH AS identifier,u.MOBILE as employeeTel,'1' AS cardState, " // cardState
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
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
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
			// String sql = "select convert(varchar,a.CardID) as
			// upCardId,convert(varchar,a.FactoryFixID) as factNumb,b.UserId as
			// userId,"
			// + " convert(int,a.CardStatusIDXF) as useState,"
			// + " b.EmployeeStrID as sid,b.EmployeeStatusID as employeeStatusID
			// "
			// + " from Tc_Employee b join TC_Card a on b.CardID=a.CardID" + "
			// where b.UserId is not null "
			// + " order by a.CardID asc,a.ModifyDate asc";

			// (2017-10-11:作废)修改了查询的方式，以发卡表中的最新的一条数据为准
			// String sql="select a.UserId as userId,a.EmployeeStrID as
			// sid,a.EmployeeStatusID as employeeStatusID,"
			// + " convert(varchar,b.CardID) as
			// upCardId,convert(varchar,b.FactoryFixID) as factNumb,"
			// + " convert(int,b.CardStatusIDXF) as useState from Tc_Employee a
			// join TC_Card b"
			// + " on b.CardID=("
			// + " select top 1 CardID from TC_Card where
			// EmployeeID=a.EmployeeID order by ModifyDate desc"
			// + " ) where a.UserId is not null ";

			// (2017-10-11:使用人员表和卡片表，双向关联查出最精确的发卡数据)
			String sql = "select B.UserId as userId,replace(B.EmployeeStrID,'NO','') as sid,B.EmployeeStatusID as employeeStatusID,"
					+ "	convert(varchar,A.CardID) as upCardId,convert(varchar,A.FactoryFixID) as factNumb,"
					+ "	convert(int,A.CardStatusIDXF) as useState, convert(int,A.CardTypeID) as cardTypeId "
					+ " from TC_Card A left join Tc_Employee B" + " on A.CardID=B.CardID and A.EmployeeID=B.EmployeeID "
					+ " where A.EmployeeID=B.EmployeeID or A.EmployeeID=0" + " order by A.CardID asc";

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
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
		}finally {
			DBContextHolder.clearDBType();
		}

		return row;
	}

	private int syncOaUserAndDept() {
		int row = 0;
		try {
			// 切换oracle数据库
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_OA);

			// 1:查询OA的部门数据
			// 如下方式，必须让实体类与数据库进行绑定之后才能使用，否则不能映射。 故使用sql的方式读取数据
			// List<HrDepartment> deptList = userservice.getForValues("from
			// HrDepartment");
			List<Map<String, Object>> deptList = thisService.getForValuesToSql("select * from ZSDX_SYNC.HR_DEPARTMENT");
			logger.info("查询OA的部门数据");
			
			// 2查询OA的岗位数据
			// List<HrPosition> jobList = userservice.getForValues("from
			// HrPosition");
			List<Map<String, Object>> jobList = thisService.getForValuesToSql("select * from ZSDX_SYNC.HR_POSITION");
			logger.info("查询OA的岗位数据");
			
			// 3:查询OA的部门岗位数据
			// List<HrDeptPosition> deptJobList =userservice.getForValues("from
			// HrDeptPosition");
			List<Map<String, Object>> deptJobList = thisService
					.getForValuesToSql("select * from ZSDX_SYNC.HR_DEPT_POSITION");
			logger.info("查询OA的部门岗位数据");
			
			// 4:查询OA用户部门岗位数据
			// List<HrUserDepartmentPosition> userDeptList
			// =userservice.getForValues("from HrUserDepartmentPosition where
			// departmentId is not null and deptPositionId is not null");
			List<Map<String, Object>> userDeptList = thisService
					.getForValuesToSql("select * from ZSDX_SYNC.HR_USER_DEPARTMENT_POSITION");
			logger.info("查询OA的用户部门岗位数据");
			
			// 5：查询用户数据
			List<Map<String, Object>> userList = thisService.getForValuesToSql(
					"select ID,USER_NAME,ACCOUNTS,IS_ONTHEJOB,IS_ENABLE,USER_SEX,CREATE_DATE,CREATE_NAME  from ZSDX_SYNC.HR_USER where ACCOUNTS is not null");
			logger.info("查询OA的用户数据");
			
			// 重置数据库源，切换回Q1
			DBContextHolder.clearDBType();

			Integer state = thisService.doSyncOaUserandDept(deptList, jobList, deptJobList, userDeptList, userList);

			row = state;
		} catch (Exception e) {
			row = -1;
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
		} finally {
			DBContextHolder.clearDBType();
		}
		return row;
	}

	private int syncOaMeeting() {
		int row = 0;
		try {
			// 切换oracle数据库
			DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_OA);

			// 从这个表直接获取人员数据，并进行拆分入库
			// 目前MEETING_USER_IDS 的字符长度为4000，可能会存在隐患，今后OA那边可能会处理，改为clob类型
			List<Object[]> meetingList = thisService.ObjectQuerySql(
					"select MEETING_ID,cast(MEETING_TITLE as VARCHAR2(255)),cast(CONTENT as VARCHAR2(2048)),"
							+ " cast(MEETING_CATEGORY as VARCHAR2(255)),BEGIN_TIME,END_TIME,"
							+ " cast(ROOM_NAME as VARCHAR2(255)),cast(CREATE_BY as VARCHAR2(255)),"
							+ " cast(MEETING_USER_IDS as  VARCHAR2(4000)), cast(MEETING_USER_NAMES as  VARCHAR2(4000))"
							+ " from zsdx_sync.meeting_msg where BEGIN_TIME >=(sysdate-7) ORDER BY  BEGIN_TIME ASC");

			// 重置数据库源，切换回Q1
			DBContextHolder.clearDBType();

			Integer state = meetingService.doSyncMetting(meetingList);

			row = state;
		} catch (Exception e) {
			row = -1;
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
		}finally {
			DBContextHolder.clearDBType();
		}
		return row;
	}
}
