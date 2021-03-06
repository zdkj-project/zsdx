package com.zd.school.plartform.system.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.orcl.sync.model.hibernate.hibernate.HrDepartment;
import com.orcl.sync.model.hibernate.hibernate.HrDeptPosition;
import com.orcl.sync.model.hibernate.hibernate.HrPosition;
import com.orcl.sync.model.hibernate.hibernate.HrUser;
import com.orcl.sync.model.hibernate.hibernate.HrUserDepartmentPosition;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DateUtil;
import com.zd.core.util.SortListUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.model.BaseUserdeptjob;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.baseset.service.BaseUserdeptjobService;
import com.zd.school.plartform.system.dao.SysUserDao;
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: BaseTUserServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户管理实体Service接口实现类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

	@Resource
	public void setBaseTUserDao(SysUserDao dao) {
		this.dao = dao;
	}
	private static Logger logger = Logger.getLogger(SysUserServiceImpl.class);
	@Resource
	private SysRoleService roleService; // 角色数据服务接口

	@Resource
	private BaseOrgService orgService; // 部门数据服务接口

	@Resource
	private BaseJobService jobservice;

	@Resource
	private TeaTeacherbaseService teacherService;
	
	@Resource
    private BaseDeptjobService deptjobService;
    @Resource
    private BaseUserdeptjobService userdeptjobService;

	@Override
	public SysUser doAddUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException {

		String userPwd = entity.getUserPwd();
		userPwd = new Sha256Hash(userPwd).toHex();
		// String roleId = entity.getRoleId();

		SysUser saveEntity = new SysUser();
		BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

		// 处理用户所属的角色
		/*
		 * if (roleId.length() > 0) { String[] roleList = roleId.split(",");
		 * Set<SysRole> isUserRoles = entity.getSysRoles(); for (String ids :
		 * roleList) { SysRole role = roleService.get(ids);
		 * isUserRoles.add(role); }
		 * 
		 * entity.setSysRoles(isUserRoles); }
		 */
		entity.setUserPwd(userPwd);
		entity.setIssystem(1);
		entity.setIsHidden("0");
		entity.setCreateUser(currentUser.getXm()); // 创建人
		// 持久化到数据库
		entity = this.doMerge(entity);

		return entity;
	}

	@Override
	public SysUser doUpdateUser(SysUser entity, SysUser currentUser) throws Exception, InvocationTargetException {

		// 先拿到已持久化的实体
		SysUser perEntity = this.get(entity.getUuid());

		Set<SysRole> isUserRoles = perEntity.getSysRoles();
		/* Set<BaseOrg> userDept = perEntity.getUserDepts(); */

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
		perEntity.setSysRoles(isUserRoles);
		/* perEntity.setUserDepts(userDept); */
		// entity = thisService.merge(perEntity);// 执行修改方法

		// 处理用户所属的角色
		/*
		 * if (roleId.length() > 0) { String[] roleList = roleId.split(",");
		 * Set<SysRole> isUserRoles = entity.getSysRoles(); //先清除所有的角色
		 * isUserRoles.removeAll(isUserRoles); for (String ids : roleList) {
		 * SysRole role = roleService.get(ids); isUserRoles.add(role); }
		 * 
		 * perEntity.setSysRoles(isUserRoles); }
		 */
		// 持久化到数据库
		entity = this.doMerge(perEntity);

		return entity;
	}

	@Override
	public Boolean deleteUserRole(String userId, String delRoleIds, SysUser currentUser) {
		Boolean delReurn = false;
		// 获取当前用户的信息
		SysUser theUser = this.get(userId);
		Set<SysRole> theUserRole = theUser.getSysRoles();

		String[] delId = delRoleIds.split(",");
		List<SysRole> delRoles = roleService.queryByProerties("uuid", delId);

		theUserRole.removeAll(delRoles);

		theUser.setSysRoles(theUserRole);

		this.doMerge(theUser);

		delReurn = true;
		// TODO Auto-generated method stub
		return delReurn;
	}

	@Override
	public Boolean addUserRole(String userId, String addRoleIds, SysUser currentUser) {

		Boolean addResult = false;
		// 获取当前用户的信息
		SysUser theUser = this.get(userId);
		Set<SysRole> theUserRole = theUser.getSysRoles();

		String[] addId = addRoleIds.split(",");
		List<SysRole> addRoles = roleService.queryByProerties("uuid", addId);

		theUserRole.addAll(addRoles);

		theUser.setSysRoles(theUserRole);

		this.doMerge(theUser);

		addResult = true;

		return addResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<SysUser> getDeptUser(Integer start, Integer limit, String sort, String filter, Boolean isDelete,
			String userIds, SysUser currentUser) {

		String sortSql = StringUtils.convertSortToSql(sort);
		String userId = userIds;
		StringBuffer hql = new StringBuffer();
		String countHql = "";
		String filterSql = "";
		if (StringUtils.isEmpty(userIds)) {
			return null;
		} else {
			if (!StringUtils.isEmpty(filter)) {
				filterSql = StringUtils.convertFilterToSql(filter);
			}
			/*
			 * BaseOrg org = orgService.get(deptId); deptIds = org.getTreeIds();
			 * hql.append(
			 * " from SysUser as o inner join fetch o.userDepts as r where r.treeIds like '"
			 * + deptIds + "%' and r.isDelete=0 "); if (isDelete) hql.append(
			 * " and o.isDelete=0 "); hql.append(filterSql); hql.append(
			 * " order by  o.jobCode "); QueryResult<SysUser> qr =
			 * this.doQueryResult(hql.toString(), start, limit);
			 */
			hql.append(" from SysUser where uuid in(" + userId + ")");
			if (isDelete)
				hql.append(" and isDelete=0 ");
			hql.append(filterSql);
			QueryResult<SysUser> qr = this.getQueryResult(hql.toString(), start, limit);

			return qr;
		}
	}

	@Override
	public List<SysUser> getUserByRoleName(String roleName) {
		String hql = "from SysUser as u inner join fetch u.sysRoles as r where r.roleName='" + roleName
				+ "' and r.isDelete=0 and u.isDelete=0";
		return this.getQuery(hql);
	}

	@Override
	public Boolean doDeleteUser(String delIds, String orgId, SysUser currentUser) {
		String[] ids = delIds.split(",");
		boolean flag = false;
		/*
		 * for (String id : ids) { SysUser user = this.get(id); BaseOrg org =
		 * orgService.get(orgId); Set<BaseOrg> userDept = user.getUserDepts();
		 * userDept.remove(org);
		 * 
		 * user.setUpdateTime(new Date());
		 * user.setUpdateUser(currentUser.getXm()); user.setUserDepts(userDept);
		 * 
		 * this.merge(user);
		 * 
		 * flag = true; }
		 */
		// TODO Auto-generated method stub
		return flag;
	}

	@Override
	public QueryResult<SysUser> getUserByRoleId(String roleId) {
		QueryResult<SysUser> qr = new QueryResult<SysUser>();
		String hql = "from SysUser as u inner join fetch u.sysRoles as r where r.uuid='" + roleId
				+ "' and r.isDelete=0 and u.isDelete=0 ";
		List<SysUser> list = this.getQuery(hql);

		SortListUtil<SysUser> sortJob = new SortListUtil<SysUser>();
		sortJob.Sort(list, "jobCode", "String");
		qr.setResultList(list);
		qr.setTotalCount((long) list.size());

		return qr;
	}

	@Override
	public QueryResult<SysUser> getUserByRoleId(String roleId, Integer start, Integer limit, String sort,
			String filter) {
		String hql = "from SysUser as u inner join fetch u.sysRoles as r where r.uuid='" + roleId
				+ "' and r.isDelete=0 and u.isDelete=0 ";
		QueryResult<SysUser> qr = this.getQueryResult(hql, start, limit);
		return qr;
	}

	@Override
	public QueryResult<SysUser> getUserNotInRoleId(String roleId, Integer start, Integer limit, String sort,
			String filter) {
		String hql = "from SysUser as o where o.isDelete=0  and state='0' "; // 只列出状态正常的用户
		if (StringUtils.isNotEmpty(roleId)) {
			String hql1 = " from SysUser as u inner join fetch u.sysRoles as k where k.uuid='" + roleId
					+ "' and k.isDelete=0 and u.isDelete=0 ";
			List<SysUser> tempList = this.getQuery(hql1);
			if (tempList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (SysUser sysUser : tempList) {
					sb.append(sysUser.getUuid());
					sb.append(",");
				}
				sb = sb.deleteCharAt(sb.length() - 1);
				String str = sb.toString().replace(",", "','");
				hql += " and o.uuid not in ('" + str + "')";
			}
		}
		if (StringUtils.isNotEmpty(filter)) {
			hql += filter;
		}
		if (StringUtils.isNotEmpty(sort)) {
			hql += " order by ";
			hql += sort;
		}
		QueryResult<SysUser> qr = this.getQueryResult(hql, start, limit);
		return qr;
	}

	/* 此代码已废弃 */
	@Override
	public int syncUserInfoToUP(SysUserToUP sysUserInfo, String userId) {
		int row = 0;
		try {
			// 1.查询该数据源中的此用户的信息
			String sql = "select top 1 UserId as userId,convert(varchar,EmployeeID) as employeeId,DepartmentID as departmentId,"
					+ " convert(varchar(36),EmployeeName) as employeeName,"
					+ " employeeStrId,sid,convert(varchar(1),sexId) sexId,identifier " + " from Tc_Employee "
					+ " where UserId='" + userId + "'";

			List<SysUserToUP> upUserInfos = this.getQuerySqlObject(sql, SysUserToUP.class);

			// 2.判断用户信息该作哪种处理
			if (upUserInfos.isEmpty()) { // 若UP没有此数据，则增加
				if (sysUserInfo != null) {
					String sqlInsert = "insert into Tc_Employee(UserId,DepartmentID,EmployeeName,EmployeeStrID,SID,EmployeePWD,SexID,identifier,cardid,CardTypeID,EmployeeStatusID) "
							+ "values('" + sysUserInfo.getUserId() + "','" + sysUserInfo.getDepartmentId() + "','"
							+ sysUserInfo.getEmployeeName() + "'," + "'" + sysUserInfo.getEmployeeStrId() + "','"
							+ sysUserInfo.getSid() + "','" + sysUserInfo.getEmployeePwd() + "','"
							+ sysUserInfo.getSexId() + "','" + sysUserInfo.getIdentifier() + "',0,1,24)";

					row = this.doExecuteSql(sqlInsert);
				}
			} else { // 若存在，则判断是修改还是删除
				SysUserToUP upUserInfo = upUserInfos.get(0);

				if (sysUserInfo == null) { // 没有此人数据，则删除
					String sqlDelete = "update Tc_Employee set EmployeeStatusID='26' where UserId='" + userId + "';";// 逻辑删除

					// 更改此人的卡片状态为2
					sqlDelete += " update TC_Card set CardStatusIDXF='2' where EmployeeID='"
							+ upUserInfo.getEmployeeId() + "';";// 逻辑删除

					row = this.doExecuteSql(sqlDelete);

				} else { // 若数据都存在，则判断是否有修改
					/*
					 * web平台中不维护卡片信息，故注释掉 if
					 * (!sysUserInfo.getCardState().equals("0")) { //
					 * 预设：CardState!=0;卡片状态，在web系统中强行设置为退卡状态 String sqlDelete =
					 * " delete from TC_Card_Bags where EMPLOYEEID='" +
					 * upUserInfo.getEmployeeId() + "'"; // 物理删除 //
					 * this.executeSql(sqlDelete);
					 * 
					 * sqlDelete += " delete from TC_Card where EMPLOYEEID='" +
					 * upUserInfo.getEmployeeId() + "'"; // 物理删除 //
					 * this.executeSql(sqlDelete);
					 * 
					 * sqlDelete +=
					 * " delete from Tc_Employee where EMPLOYEEID='" +
					 * upUserInfo.getEmployeeId() + "'"; // 物理删除
					 * 
					 * row = this.executeSql(sqlDelete);
					 * 
					 * } else
					 */
					if (!sysUserInfo.equals(upUserInfo)) { // 对比部分数据是否一致
						String sqlUpdate = " update Tc_Employee set DepartmentID='" + sysUserInfo.getDepartmentId()
								+ "'," + "EmployeeName='" + sysUserInfo.getEmployeeName() + "',EmployeeStrID='"
								+ sysUserInfo.getEmployeeStrId() + "'," + "SexID='" + sysUserInfo.getSexId()
								+ "',identifier='" + sysUserInfo.getIdentifier() + "' where UserId='" + userId + "'";

						row = this.doExecuteSql(sqlUpdate);
					}
				}
			}

		} catch (Exception e) {
			row = -1;
		}

		return row;
	}

	@Override
	public int syncUserInfoToAllUP(List<SysUserToUP> userInfos, String departmentId) {
		int row = 0;
		try {
			// 1.查询该数据源中的此用户的信息
			String sql = "select UserId as userId,convert(varchar,EmployeeID) as employeeId,DepartmentID as departmentId,"
					+ " convert(varchar(36),EmployeeName) as employeeName,"
					+ " employeeStrId,sid,convert(varchar(1),sexId) sexId,identifier " + " from Tc_Employee ";
			// + " where DepartmentID='"+departmentId+"'"
			// + " order by userId asc";

			if (departmentId == null) // 当此值为null，表明同步的是系统的教师人员数据
				sql += " where DepartmentID not like '%Train%' ";
			else // 当此值为具体的值的时候，表明同步的是某个班级的学员
				sql += " where DepartmentID='" + departmentId + "'";

			sql += " order by userId asc";

			List<SysUserToUP> upUserInfos = this.getQuerySqlObject(sql, SysUserToUP.class);

			// 循环对比
			SysUserToUP currentUser = null;
			SysUserToUP upUser = null;
			boolean isExist = false;
			StringBuffer sqlSb = new StringBuffer();
			for (int i = 0; i < userInfos.size(); i++) {
				currentUser = userInfos.get(i);
				isExist = false;

				for (int j = 0; j < upUserInfos.size(); j++) {
					upUser = upUserInfos.get(j);
					if (currentUser.getUserId().equals(upUser.getUserId())) {
						// 执行代码
						isExist = true;
						if (currentUser.getIsDelete() == 1) {
							// sqlDelete = "delete from Tc_Employee where
							// UserId='" + UserId + "'"; 物理删除
							// 现在每次同步都会更新这个值，理应判断之后就不同步的，但是影响不大。
							sqlSb.append(" update Tc_Employee set EmployeeStatusID='26' where UserId='"
									+ currentUser.getUserId() + "';");// 逻辑删除

							// 更改此人的卡片状态为2
							sqlSb.append(" update TC_Card set CardStatusIDXF='2' where EmployeeID='"
									+ upUser.getEmployeeId() + "';");// 逻辑删除

						}
						/*
						 * web平台中不维护卡片信息，故注释掉 else if
						 * (!currentUser.getCardState().equals("0")) {//
						 * 预设：CardState!=0;卡片状态，在web系统中强行设置为退卡状态
						 * 
						 * // String sqlDelete =
						 * "delete from TC_Card_Bags where EMPLOYEEID='" +
						 * upUser.getEmployeeId() // + "'"; // 物理删除 // //
						 * sqlDelete +=
						 * " delete from TC_Card where EMPLOYEEID='" +
						 * upUser.getEmployeeId() + "'"; // 物理删除 // // sqlDelete
						 * += " delete from Tc_Employee where EMPLOYEEID='" +
						 * upUser.getEmployeeId() + "'"; // 物理删除 // //
						 * this.executeSql(sqlDelete);
						 * 
						 * sqlSb.append(
						 * " delete from TC_Card_Bags where EMPLOYEEID='" +
						 * upUser.getEmployeeId()+ "'"); sqlSb.append(
						 * " delete from TC_Card where EMPLOYEEID='" +
						 * upUser.getEmployeeId() + "'"); sqlSb.append(
						 * " delete from Tc_Employee where EMPLOYEEID='" +
						 * upUser.getEmployeeId() + "'");
						 * 
						 * }
						 */
						else if (!currentUser.equals(upUser)) { // 对比数据（一部分需要判断的数据）是否一致
							// 更新卡片状态； 2017-11-3现在不更新卡类了（胡洋确定及肯定）
							// int cardTypeId=1;
							// if(currentUser.getJobName()!=null){
							// if(currentUser.getJobName().contains("合同工"))
							// cardTypeId=2;
							// else if(currentUser.getJobName().equals("学员"))
							// cardTypeId=3;
							// }

							String sqlUpdate = " update Tc_Employee set DepartmentID='" + currentUser.getDepartmentId()
									+ "'," + "EmployeeName='" + currentUser.getEmployeeName() + "'";

							// update时不更新人员编号字段了，因为教职工发卡之后，会把印刷卡号绑定到EmployeeStrID字段。
							// ,EmployeeStrID='" +
							// currentUser.getEmployeeStrId() + "'";

							if (currentUser.getSexId() == null)
								sqlUpdate += ",SexID=NULL";
							else
								sqlUpdate += ",SexID='" + currentUser.getSexId() + "'";

							if (currentUser.getIdentifier() == null)
								sqlUpdate += ",identifier=NULL";
							else
								sqlUpdate += ",identifier='" + currentUser.getIdentifier() + "'";

							if (currentUser.getEmployeeTel() == null)
								sqlUpdate += ",employeeTel=NULL";
							else
								sqlUpdate += ",employeeTel='" + currentUser.getEmployeeTel() + "'";

							sqlUpdate += ",EmployeeStatusID='24' " // ,CardTypeID="+cardTypeId+"现在不更新卡类了（胡洋确定及肯定）
									+ " where UserId='" + currentUser.getUserId() + "';";

							// this.executeSql(sqlUpdate);

							sqlSb.append(sqlUpdate);
						}

						upUserInfos.remove(j);
						break; // 跳出

					}
				}

				// 若上面的循环无法找到对应的人员，表明UP中不存在此用户
				if (!isExist && currentUser.getIsDelete() != 1) {
					int cardTypeId = 1;
					if (currentUser.getJobName() != null) {
						if (currentUser.getJobName().contains("合同工"))
							cardTypeId = 2;
						else if (currentUser.getJobName().equals("学员"))
							cardTypeId = 3;
					}

					String sqlInsert = "insert into Tc_Employee(UserId,DepartmentID,EmployeeName,EmployeeStrID,SID,EmployeePWD,SexID,identifier,employeeTel,cardid,CardTypeID,EmployeeStatusID,PositionId) "
							+ "values('" + currentUser.getUserId() + "','" + currentUser.getDepartmentId() + "','"
							+ currentUser.getEmployeeName() + "'," + "'" + currentUser.getEmployeeStrId() + "','"
							+ currentUser.getSid() + "','" + currentUser.getEmployeePwd() + "'";

					if (currentUser.getSexId() == null)
						sqlInsert += ",NULL";
					else
						sqlInsert += ",'" + currentUser.getSexId() + "'";

					if (currentUser.getIdentifier() == null)
						sqlInsert += ",NULL";
					else
						sqlInsert += ",'" + currentUser.getIdentifier() + "'";

					if (currentUser.getEmployeeTel() == null)
						sqlInsert += ",NULL";
					else
						sqlInsert += ",'" + currentUser.getEmployeeTel() + "'";

					sqlInsert += ",0," + cardTypeId + ",24,19);";

					sqlSb.append(sqlInsert);
					// this.executeSql(sqlInsert);
				}

				// 若积累的语句长度大于2000（大约50条语句左右），则执行
				if (sqlSb.length() > 2000) {
					//row += this.doExecuteSql(sqlSb.toString());
					row += this.getBaseDao().executeSql(sqlSb.toString());
					sqlSb.setLength(0); // 清空
				}
			}

			// 最后执行一次
			if (sqlSb.length() > 0){
				//row += this.doExecuteSql(sqlSb.toString());
				row += this.getBaseDao().executeSql(sqlSb.toString());
			}

			// 剩下的，表明不存在平台的库中，进行删除
			/*
			 * 暂时不开放 for (int k = 0; k < upUserInfos.size(); k++) { upUser =
			 * userInfos.get(k);
			 * 
			 * //sqlDelete=
			 * "update Tc_Employee set EmployeeStatusID='26' where UserId='" +
			 * UserId + "'";//逻辑删除 //this.executeSql(sqlDelete);
			 * 
			 * String sqlDelete = "delete from TC_Card_Bags where EMPLOYEEID='"
			 * + upUser.getEmployeeId() + "'"; //物理删除
			 * 
			 * sqlDelete += " delete from TC_Card where EMPLOYEEID='" +
			 * upUser.getEmployeeId() + "'"; //物理删除
			 * 
			 * sqlDelete += "delete from Tc_Employee where EMPLOYEEID='" +
			 * upUser.getEmployeeId() + "'"; //物理删除
			 * 
			 * this.executeSql(sqlDelete);
			 * 
			 * }
			 */

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚； 还需要进行验证测试是否完全正确。
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			row = -1;
		}

		return row;
	}

	@Override
	public int syncAllCardInfoFromUp(List<CardUserInfoToUP> upCardUserInfos) {
		int row = 0;
		try {
			// 1.查询web平台的发卡信息
			String sql = "select CARD_ID as uuid,convert(varchar,FACT_NUMB) as factNumb,USE_STATE as useState,"
					+ " USER_ID as userId,convert(varchar,UP_CARD_ID) as upCardId,CARD_PRINT_ID as sid"
					+ " from CARD_T_USEINFO where ISDELETE=0 " + " order by upCardId asc";

			List<CardUserInfoToUP> webCardUserInfos = this.getQuerySqlObject(sql, CardUserInfoToUP.class);

			String updateTime = null;

			// 循环对比
			CardUserInfoToUP webCardUser = null;
			CardUserInfoToUP upCardUser = null;
			boolean isExist = false;
			StringBuffer sqlSb = new StringBuffer();
			String sqlStr = "";
			// 当参数为null或内容为空，则直接删除发卡信息
			if (upCardUserInfos == null || upCardUserInfos.size() == 0) {
				sqlStr = "	delete from CARD_T_USEINFO ";
				//this.doExecuteSql(sqlSb.toString());
				this.getBaseDao().executeSql(sqlSb.toString());
			} else {
				for (int i = 0; i < upCardUserInfos.size(); i++) {
					upCardUser = upCardUserInfos.get(i);
					sqlStr = "";
					isExist = false;

					// 【new：放在前面，因为下面的一个if会continue】若积累的语句长度大于3000（大约50条语句左右），则执行
					if (sqlSb.length() > 3000) {
						//row += this.doExecuteSql(sqlSb.toString());
						row += this.getBaseDao().executeSql(sqlSb.toString());
						sqlSb.setLength(0); // 清空
					}

					if (upCardUser.getUpCardId() == null) {
						sqlStr = "delete from CARD_T_USEINFO where USER_ID='" + upCardUser.getUserId() + "';";
						sqlSb.append(sqlStr + "  ");
						continue;
					}
					/*
					 * （2017-10-10：不再处理这种卡片信息 ） else
					 * if(upCardUser.getUseState()!=1){ //若卡片状态不为1，则直接设置为卡片无效
					 * 
					 * updateTime = DateUtil.formatDateTime(new Date()); sqlStr
					 * = "update CARD_T_USEINFO set " + "	FACT_NUMB='" + 0 +
					 * "',USE_STATE='" + upCardUser.getUseState() + "'," +
					 * "	UP_CARD_ID='" + 0 +
					 * "',UPDATE_TIME=CONVERT(datetime,'" + updateTime + "')" +
					 * " where USER_ID='" + upCardUser.getUserId() + "';";
					 * 
					 * sqlSb.append(sqlStr + "  "); continue; }
					 */
					for (int j = 0; j < webCardUserInfos.size(); j++) {
						webCardUser = webCardUserInfos.get(j);
						// 若web库中存在此发卡信息
						if (upCardUser.getUserId() != null && upCardUser.getUserId().equals(webCardUser.getUserId())) {
							// 执行代码
							isExist = true;
							if (!upCardUser.equals(webCardUser)) { // 对比数据（一部分需要判断的数据）是否一致
								// 若发卡ID为null，表明没有发卡，所以物理删除卡片信息
								if (upCardUser.getUpCardId() == null) {
									sqlStr = "delete from CARD_T_USEINFO where USER_ID='" + upCardUser.getUserId()
											+ "';";
									sqlSb.append(sqlStr + "  ");
								} else { // 否则更新数据
									updateTime = DateUtil.formatDateTime(new Date());

									sqlStr = "update CARD_T_USEINFO set " + "	FACT_NUMB='" + upCardUser.getFactNumb()
											+ "',USE_STATE='" + upCardUser.getUseState() + "'," + "	UP_CARD_ID='"
											+ upCardUser.getUpCardId() + "'," + "	CARD_TYPE_ID='"
											+ upCardUser.getCardTypeId() + "',";

									// 当up同步过来的印刷卡号不为null的时候，才更新这个印刷卡号
									if (StringUtils.isNotEmpty(upCardUser.getSid()))
										sqlStr += " CARD_PRINT_ID='" + upCardUser.getSid() + "',";

									sqlStr += " UPDATE_TIME=CONVERT(datetime,'" + updateTime + "')" + " where USER_ID='"
											+ upCardUser.getUserId() + "'";

									sqlSb.append(sqlStr + "  ");
								}
							}

							webCardUserInfos.remove(j);
							break; // 跳出

						} else if (upCardUser.getUpCardId().equals(webCardUser.getUpCardId())) { // 如果卡片id相同，也处理
							isExist = true;
							updateTime = DateUtil.formatDateTime(new Date());
							if (!upCardUser.equals(webCardUser)) {
								sqlStr = "update CARD_T_USEINFO set " + "	FACT_NUMB='" + upCardUser.getFactNumb()
										+ "',USE_STATE='" + upCardUser.getUseState() + "'," + "	CARD_TYPE_ID='"
										+ upCardUser.getCardTypeId() + "',";

								// 当up同步过来的印刷卡号不为null的时候，才更新这个印刷卡号
								if (StringUtils.isNotEmpty(upCardUser.getSid()))
									sqlStr += " CARD_PRINT_ID='" + upCardUser.getSid() + "',";

								if (upCardUser.getUserId() == null)
									sqlStr += " USER_ID=NULL,";
								else
									sqlStr += " USER_ID='" + upCardUser.getUserId() + "',";

								sqlStr += " UPDATE_TIME=CONVERT(datetime,'" + updateTime + "')" + " where UP_CARD_ID='"
										+ upCardUser.getUpCardId() + "'";

								sqlSb.append(sqlStr + "  ");
							}

							webCardUserInfos.remove(j);
							break; // 跳出
						}
					}

					// 若上面的循环无法找到对应的卡片信息，表明UP中不存在此卡片信息
					if (!isExist && upCardUser.getUpCardId() != null) {
						updateTime = DateUtil.formatDateTime(new Date());

						sqlStr = "insert into CARD_T_USEINFO(CARD_ID,CREATE_TIME,CREATE_USER,"
								+ "ISDELETE,FACT_NUMB,USE_STATE,UP_CARD_ID,CARD_TYPE_ID,USER_ID,CARD_PRINT_ID)"
								+ " values ('" + UUID.randomUUID().toString() + "',CONVERT(datetime,'" + updateTime
								+ "'),'超级管理员'," + "0,'" + upCardUser.getFactNumb() + "'," + upCardUser.getUseState()
								+ "," + "'" + upCardUser.getUpCardId() + "','" + upCardUser.getCardTypeId() + "',";

						if (upCardUser.getUserId() == null)
							sqlStr += "NULL,";
						else
							sqlStr += "'" + upCardUser.getUserId() + "',";

						if (upCardUser.getSid() == null)
							sqlStr += "NULL)";
						else
							sqlStr += "'" + upCardUser.getSid() + "')";

						sqlSb.append(sqlStr + "  ");
					}

				}

				// 最后执行一次
				if (sqlSb.length() > 0){
					//row += this.doExecuteSql(sqlSb.toString());
					row += this.getBaseDao().executeSql(sqlSb.toString());
				}
					

				// 如果还有没执行到的发卡数据，则进行循环删除(2017-10-10不再删除未绑定的卡,置为空卡)
				if (webCardUserInfos.size() > 0) {
					sqlSb.setLength(0); // 清空
					for (int k = 0; k < webCardUserInfos.size(); k++) {
						webCardUser = webCardUserInfos.get(k);
						sqlStr = "delete from CARD_T_USEINFO where CARD_ID='" + webCardUser.getUuid() + "';";
						// sqlStr = "UPDATE CARD_T_USEINFO SET
						// user_id=null,use_state=0 where CARD_ID='" +
						// webCardUser.getUuid() + "';";
						sqlSb.append(sqlStr + "  ");
					}
					//this.doExecuteSql(sqlSb.toString());
					this.getBaseDao().executeSql(sqlSb.toString());
					// 若web库中存在此发卡信息
				}
			}

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			row = -1;
		}

		return row;
	}

	// 同步某个班级的发卡信息
	@Deprecated
	@Override
	public int syncClassCardInfoFromUp(List<CardUserInfoToUP> upCardUserInfos, String classId) {
		int row = 0;
		try {
			// 1.查询web平台的发卡信息
			String sql = "select convert(varchar,a.FACT_NUMB) as factNumb,a.USE_STATE as useState,"
					+ " a.USER_ID as userId,convert(varchar,a.UP_CARD_ID) as upCardId,CARD_PRINT_ID as sid "
					+ " from CARD_T_USEINFO a join TRAIN_T_CLASSTRAINEE b on a.USER_ID=b.CLASS_TRAINEE_ID"
					+ " where a.ISDELETE=0 and b.CLASS_ID='" + classId + "'" + " order by a.UP_CARD_ID asc";

			List<CardUserInfoToUP> webCardUserInfos = this.getQuerySqlObject(sql, CardUserInfoToUP.class);

			String updateTime = null;

			// 循环对比
			CardUserInfoToUP webCardUser = null;
			CardUserInfoToUP upCardUser = null;
			boolean isExist = false;
			StringBuffer sqlSb = new StringBuffer();
			String sqlStr = "";

			// 当参数为null或内容为空，则直接删除这个班级的发卡信息
			if (upCardUserInfos == null || upCardUserInfos.size() == 0) {
				sqlStr = "	delete from CARD_T_USEINFO where USER_ID in "
						+ "(	select CLASS_TRAINEE_ID from TRAIN_T_CLASSTRAINEE where CLASS_ID='" + classId + "')";
				this.doExecuteSql(sqlSb.toString());

			} else {// 否则循环判断。
				for (int i = 0; i < upCardUserInfos.size(); i++) {
					upCardUser = upCardUserInfos.get(i);
					sqlStr = "";
					isExist = false;

					// 【new：放在前面，因为下面的一个if会continue】若积累的语句长度大于3000（大约50条语句左右），则执行
					if (sqlSb.length() > 3000) {
						row += this.doExecuteSql(sqlSb.toString());
						sqlSb.setLength(0); // 清空
					}

					if (upCardUser.getUpCardId() == null) {
						sqlStr = "delete from CARD_T_USEINFO where USER_ID='" + upCardUser.getUserId() + "';";
						sqlSb.append(sqlStr + "  ");
						continue;
					} else if (upCardUser.getUseState() != 1) { // 若卡片状态不为1，则直接设置为卡片无效

						updateTime = DateUtil.formatDateTime(new Date());
						sqlStr = "update CARD_T_USEINFO set " + "	FACT_NUMB='" + 0 + "',USE_STATE='"
								+ upCardUser.getUseState() + "'," + "	UP_CARD_ID='" + 0
								+ "',UPDATE_TIME=CONVERT(datetime,'" + updateTime + "')" + " where USER_ID='"
								+ upCardUser.getUserId() + "';";

						sqlSb.append(sqlStr + "  ");
						continue;
					}

					for (int j = 0; j < webCardUserInfos.size(); j++) {
						webCardUser = webCardUserInfos.get(j);
						// 若web库中存在此发卡信息
						if (upCardUser.getUserId().equals(webCardUser.getUserId())) {
							// 执行代码
							isExist = true;

							if (!upCardUser.equals(webCardUser)) { // 对比数据（一部分需要判断的数据）是否一致

								if (upCardUser.getUpCardId() == null) { // 若发卡ID为null，表明没有发卡，所以物理删除卡片信息
									sqlStr = "delete from CARD_T_USEINFO where USER_ID='" + upCardUser.getUserId()
											+ "';";
									sqlSb.append(sqlStr + "  ");
								} else { // 否则更新数据
									updateTime = DateUtil.formatDateTime(new Date());
									sqlStr = "update CARD_T_USEINFO set " + "	FACT_NUMB='" + upCardUser.getFactNumb()
											+ "',USE_STATE='" + upCardUser.getUseState() + "'," + " CARD_PRINT_ID='"
											+ upCardUser.getSid() + "'," + "	UP_CARD_ID='" + upCardUser.getUpCardId()
											+ "',UPDATE_TIME=CONVERT(datetime,'" + updateTime + "')"
											+ " where USER_ID='" + upCardUser.getUserId() + "';";

									sqlSb.append(sqlStr + "  ");
								}
							}

							webCardUserInfos.remove(j);
							break; // 跳出
						}
					}

					// 若上面的循环无法找到对应的卡片信息，表明UP中不存在此卡片信息
					// 并且发卡ID不为null，表明有新的发卡数据
					if (!isExist && upCardUser.getUpCardId() != null) {

						updateTime = DateUtil.formatDateTime(new Date());

						sqlStr = "insert into CARD_T_USEINFO(CARD_ID,CREATE_TIME,CREATE_USER,"
								+ "ISDELETE,FACT_NUMB,USE_STATE,USER_ID,UP_CARD_ID,CARD_PRINT_ID)" + " values ('"
								+ UUID.randomUUID().toString() + "',CONVERT(datetime,'" + updateTime + "'),'超级管理员',"
								+ "0,'" + upCardUser.getFactNumb() + "'," + upCardUser.getUseState() + "," + "'"
								+ upCardUser.getUserId() + "','" + upCardUser.getUpCardId() + "','"
								+ upCardUser.getSid() + "');";

						sqlSb.append(sqlStr + "  ");

					}

					// 若积累的语句长度大于3000（大约50条语句左右），则执行
					if (sqlSb.length() > 3000) {
						row += this.doExecuteSql(sqlSb.toString());
						sqlSb.setLength(0); // 清空
					}
				}

				// 最后执行一次
				if (sqlSb.length() > 0)
					row += this.doExecuteSql(sqlSb.toString());
			}

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			row = -1;
		}

		return row;
	}

	@Override
	public List<SysRole> getSysRoleList(SysUser sysUser) {
		// TODO Auto-generated method stub

		String sql = "select r.ROLE_ID as uuid,r.ROLE_CODE as	roleCode,r.ROLE_NAME as roleName from"
				+ " SYS_T_ROLE r inner join" + " SYS_T_ROLEUSER ru on r.ROLE_ID=ru.ROLE_ID inner join "
				+ " SYS_T_USER u on ru.USER_ID=u.USER_ID  " + " where" + " u.USER_ID='" + sysUser.getUuid() + "'  "
				+ " and u.ISDELETE=0 " + " and r.ISDELETE=0 ";

		List<SysRole> list = roleService.getQuerySqlObject(sql, SysRole.class);

		return list;
	}
	
	/**
	 * 各个数据改为map的形式去存取
	 * @param deptList
	 * @param jobList
	 * @param deptJobList
	 * @param userDeptList
	 * @param userList
	 * @return
	 */
	@Override
	public Integer syncOaUserandDept(List<Map<String,Object>> deptList, List<Map<String,Object>> jobList,
			List<Map<String,Object>> deptJobList, List<Map<String,Object>> userDeptList,List<Map<String,Object>> userList) {
		int row = 1;
		try {
			Map<String,Object> map=null;
			
			/**
			 * 1：处理部门数据
			 */
			String sql = "update BASE_T_ORG set isdelete=1 where parent_node!='ROOT'";
			//orgService.doExecuteSql(sql);
			orgService.getBaseDao().executeSql(sql);
			BaseOrg org = null;
			for(int i=0;i<deptList.size();i++){
				map=deptList.get(i);
				String id=String.valueOf(map.get("ID"));
				org = orgService.get(id);
				if (org == null)
					org = new BaseOrg(id);
				org.setCreateTime((Date) map.get("CREATE_DATE"));
				org.setCreateUser((String) map.get("CREATE_NAME"));
				String idEnable=String.valueOf(map.get("IS_ENABLE"));
				org.setIsDelete("0".equals(idEnable) ? 1 : 0);
				org.setUpdateTime((Date) map.get("UPDATE_DATE"));
				org.setUpdateUser((String) map.get("UPDATE_NAME"));
				org.setDeptType("03");
				
				//如果为0，那么上级节点设置为root节点
				String parentId=String.valueOf(map.get("PARENTID"));
				if("0".equals(parentId.trim()))
					org.setParentNode("2851655E-3390-4B80-B00C-52C7CA62CB39");
				else
					org.setParentNode(parentId);
				
				org.setNodeCode((String)map.get("CODE"));
				org.setNodeText((String)map.get("NAMES"));
				org.setIssystem(0);
				org.setOrderIndex(Integer.parseInt(String.valueOf(map.get("ORDERBY"))));
				org.setExtField01(parentId);

				//orgService.doMerge(org);
				orgService.getBaseDao().merge(org);
			}
			logger.info("处理部门数据");
			
			// 更新一些非同步内容（生成副ID）
			String rootId = "2851655E-3390-4B80-B00C-52C7CA62CB39";
			String rootCode = "001";
			sql = MessageFormat.format("EXECUTE [ORG_P_UPDATESYNC] ''{0}'',''{1}'',''{2}''", rootId, rootId, rootCode);
			orgService.getQuerySql(sql);
			
			/**
			 * 2：处理岗位数据
			 */
			sql = "update BASE_T_JOB set isdelete=1";
			//jobservice.doExecuteSql(sql);
			jobservice.getBaseDao().executeSql(sql);
			BaseJob b = null;
			for (int i=0;i<jobList.size();i++) {
				map=jobList.get(i);
				String id=String.valueOf(map.get("ID"));
				
				b = jobservice.get(id);
				if (b == null) {
					b = new BaseJob(id);
				}
				b.setCreateTime((Date) map.get("CREATE_DATE"));
				b.setCreateUser((String) map.get("CREATE_NAME"));
				b.setUpdateTime((Date) map.get("UPDATE_DATE"));
				b.setUpdateUser((String) map.get("UPDATE_NAME"));
				b.setOrderIndex(Integer.parseInt(String.valueOf(map.get("ORDERBY"))));
				b.setIsDelete(0);
				b.setJobName((String) map.get("NAMES"));
				b.setRemark((String) map.get("DUTY"));

				//jobservice.doMerge(b);
				jobservice.getBaseDao().merge(b);
			}		
			logger.info("处理岗位数据");
			
			/**
			 * 3：处理部门岗位数据
			 */
			sql = "delete from BASE_T_DEPTJOB";
//	        deptjobService.doExecuteSql(sql);
	        deptjobService.getBaseDao().executeSql(sql);
	        BaseDeptjob dj = null;
	        for (int i=0;i<deptJobList.size();i++) {
	        	map=deptJobList.get(i);
				String id=String.valueOf(map.get("ID"));
				
	            dj = deptjobService.get(id);
	            if (dj == null) {
	                dj = new BaseDeptjob(id);
	            }
	            dj.setDeptId((String) map.get("DEPARTMENT_ID"));
	            dj.setJobId((String) map.get("POSITION_ID"));
	            dj.setParentdeptId((String) map.get("PREDEPARTMENT_ID"));
	            dj.setParentjobId((String) map.get("PREPOSITION_ID"));
	            dj.setCreateUser((String) map.get("CREATE_NAME"));
	            dj.setCreateTime((Date) map.get("CREATE_DATE"));
	            dj.setUpdateUser((String) map.get("UPDATE_NAME"));
	            dj.setUpdateTime((Date) map.get("UPDATE_DATE"));
	            dj.setIsDelete(0);
	            dj.setJobType(2);
//	            deptjobService.doMerge(dj);
	            deptjobService.getBaseDao().merge(dj);
	        }	      
	        logger.info("处理部门岗位数据");
	        
	        /**
	         * 4：处理用户数据
	         */
	        //默认数据
            String[] param = {"roleName", "isDelete"};
            Object[] values = {"教师", 0};
            SysRole defaultRole = roleService.getByProerties(param, values);
            Map<String, String> mapDicItme = new HashMap<>();
            mapDicItme.put("男", "1");
            mapDicItme.put("女", "2");
            String defaultPwd = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
           
            //this.doExecuteSql("delete BASE_T_USERDEPTJOB where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
            this.getBaseDao().executeSql("delete BASE_T_USERDEPTJOB where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
            
            Date justDate = new Date();
            String justYear = DateUtil.formatDate(justDate,"yyyy");
            Integer integer = 1;
            String userNumb = "";
            SysUser u = null;
            for (int i=0;i<userList.size();i++) {
                userNumb = "";
                map=userList.get(i);
				String id=String.valueOf(map.get("ID"));
				
                u = this.get(id);
                if (u == null) {
                    u = new SysUser(id);
                    userNumb = justYear + StringUtils.addString(integer.toString(), "0", 6,"L");
                    u.setXm((String) map.get("USER_NAME"));
                    u.setUserName((String) map.get("ACCOUNTS"));
                    u.setUserPwd(defaultPwd);
                    u.setUserNumb(userNumb);
                                                      
                    u.setIsDelete("0".equals(String.valueOf(map.get("IS_ONTHEJOB"))) ? 1: 0);
                                                          
                    String idEnable=String.valueOf(map.get("IS_ENABLE"));
                    u.setState("0".equals(idEnable) ? "1" : "0");
    				
                    u.setCategory("1");
                    u.setIssystem(1);
                    u.setIsHidden("0");
                    if (map.get("USER_SEX") != null) {
                        u.setXbm(mapDicItme.get((String) map.get("USER_SEX")));
                    }
                    u.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
                    u.setCreateTime((Date) map.get("CREATE_DATE"));
                    u.setCreateUser((String) map.get("CREATE_NAME"));

                    Set<SysRole> theUserRole = u.getSysRoles();
                    theUserRole.add(defaultRole);
                    u.setSysRoles(theUserRole);
                    integer++;
                    //this.doMerge(u);
                    this.getBaseDao().merge(u);
                }else {
                	 u.setXm((String) map.get("USER_NAME"));
                     u.setUserName((String) map.get("ACCOUNTS"));
                     u.setIsDelete("0".equals(String.valueOf(map.get("IS_ONTHEJOB"))) ? 1: 0);                     
                     if ( map.get("USER_SEX") != null) {
                         u.setXbm(mapDicItme.get((String) map.get("USER_SEX")));
                     }
                     u.setCreateTime((Date) map.get("CREATE_DATE"));
                     u.setCreateUser((String) map.get("CREATE_NAME"));
                }
            }
            logger.info("处理用户数据");
            
            /**
             * 5：处理用户部门岗位
             */
            String mapKdy = "";
            Map<String, String> mapDeptJob = new HashMap<>();
            List<BaseDeptjob> deptjobList = deptjobService.queryByProerties("isDelete", 0);
            for (BaseDeptjob baseDeptjob : deptjobList) {
                mapKdy = baseDeptjob.getDeptId() + "," + baseDeptjob.getJobId();
                mapDeptJob.put(mapKdy, baseDeptjob.getUuid());
                mapKdy = "";
            }
            sql = "delete from BASE_T_USERDEPTJOB";
//            userdeptjobService.doExecuteSql(sql);
            userdeptjobService.getBaseDao().executeSql(sql);
            BaseUserdeptjob udj = null;
            for (int i=0;i<userDeptList.size();i++) {
            	map=userDeptList.get(i);
   
				String id=String.valueOf(map.get("ID"));
				String job=(String) map.get("DEPT_POSITION_ID");
	            String dept=(String) map.get("DEPARTMENT_ID");
	            
	            if(dept==null||job==null)
	            	continue;
	            
                udj = new BaseUserdeptjob(id);
                udj.setUserId((String) map.get("SYS_USER_ID"));
                
             
                udj.setDeptId(dept);
                
                             
                udj.setJobId(job);
                udj.setMasterDept(Integer.parseInt(String.valueOf(map.get("MASTERFLAG"))));
                udj.setCreateUser((String) map.get("CREATE_BY"));
                udj.setCreateTime((Date) map.get("CREATE_DATE"));
                
                mapKdy = dept + "," + job;
                String deptJobId=mapDeptJob.get(mapKdy);
                udj.setDeptjobId(deptJobId==null?"":deptJobId);
                
//                userdeptjobService.doMerge(udj);
                userdeptjobService.getBaseDao().merge(udj);
                mapKdy = "";
            }                   
            logger.info("处理用户部门岗位数据");
            
		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
			row = -1;
		}

		return row;

	}
}