package com.zd.school.oa.notice.service.Impl;

import com.zd.core.constant.InfoPushWay;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DateUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.oa.notice.dao.OaNoticeDao;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticeOther;
import com.zd.school.oa.notice.model.OaNoticeauditor;
import com.zd.school.oa.notice.service.OaNoticeService;
import com.zd.school.oa.notice.service.OaNoticeauditorService;
import com.zd.school.oa.notice.service.OaNoticerightService;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 
 * ClassName: OaNoticeServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公告信息表(OA_T_NOTICE)实体Service接口实现类. date: 2016-12-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaNoticeServiceImpl extends BaseServiceImpl<OaNotice> implements OaNoticeService {

	@Resource
	public void setOaNoticeDao(OaNoticeDao dao) {
		this.dao = dao;
	}

	private static Logger logger = Logger.getLogger(OaNoticeServiceImpl.class);

	@Resource
	private BaseOrgService orgService;

	@Resource
	private SysUserService userService;

	@Resource
	private SysRoleService roleService;

	@Resource
	private OaNoticerightService rightService;

	@Resource
	private OaNoticeauditorService auditorService;

	@Resource
	private PushInfoService pushService;

	@Override
	public QueryResult<OaNotice> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
		QueryResult<OaNotice> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
		return qResult;
	}

	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
	 * @param currentUser
	 *            当前操作的用户
	 * @return 操作成功返回true，否则返回false
	 */
	@Override
	public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
		Boolean delResult = false;
		try {
			Object[] conditionValue = ids.split(",");
			String[] propertyName = { "isDelete", "updateUser", "updateTime" };
			Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
			this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			delResult = false;
		}
		return delResult;
	}

	/**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaNotice doUpdateEntity(OaNotice entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		OaNotice saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyProperties(saveEntity, entity);
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.merge(saveEntity);// 执行修改方法

			return entity;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @param deptIds
	 *            公告通知的部门
	 * @param roleIds
	 *            公告通知的角色
	 * @param userIds
	 *            公告通知的用户
	 * @return
	 */
	@Override
	public OaNotice doUpdateEntity(OaNotice entity, SysUser currentUser, String deptIds, String roleIds,
			String userIds) {
		Object[] propValue = {};
		// 先拿到已持久化的实体
		OaNotice saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyProperties(saveEntity, entity);
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			// 根据传入的部门、人员与角色的id处理
			OaNoticeOther otherInfo = this.getNoticeOther(entity.getUuid());
			if (!deptIds.equals(otherInfo.getDeptIds())) {
				propValue = deptIds.split(",");
				Set<BaseOrg> orgs = saveEntity.getNoticeDepts();
				List<BaseOrg> setOrgs = orgService.queryByProerties("uuid", propValue);
				orgs.addAll(setOrgs);
				saveEntity.setNoticeDepts(orgs);
			}
			if (!roleIds.equals(otherInfo.getRoleIds())) {
				propValue = roleIds.split(",");
				Set<SysRole> roles = saveEntity.getNoticeRoles();
				List<SysRole> setRoles = roleService.queryByProerties("uuid", propValue);
				roles.addAll(setRoles);
				saveEntity.setNoticeRoles(roles);
			}
			if (!userIds.equals(otherInfo.getUserIds())) {
				propValue = userIds.split(",");
				Set<SysUser> users = saveEntity.getNoticeUsers();
				List<SysUser> setUsers = userService.queryByProerties("uuid", propValue);
				users.addAll(setUsers);
				saveEntity.setNoticeUsers(users);
			}
			entity = this.merge(saveEntity);// 执行修改方法

			return entity;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaNotice doAddEntity(OaNotice entity, SysUser currentUser) {
		OaNotice saveEntity = new OaNotice();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity, excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.merge(saveEntity);// 执行修改方法

			return entity;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @param deptIds
	 *            公告通知的部门
	 * @param roleIds
	 *            公告通知的角色
	 * @param userIds
	 *            公告通知的用户
	 * @return
	 */
	@Override
	public OaNotice doAddEntity(OaNotice entity, SysUser currentUser, String deptIds, String roleIds, String userIds) {
		OaNotice saveEntity = new OaNotice();
		try {
			List<String> excludedProp = new ArrayList<>();
			String[] propValue = {};
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity, excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
			// 如果通知部门不为空时处理
			if (StringUtils.isNotEmpty(deptIds)) {
				propValue = deptIds.split(",");
				Set<BaseOrg> orgs = saveEntity.getNoticeDepts();
				List<BaseOrg> setOrgs = orgService.queryByProerties("uuid", propValue);
				orgs.addAll(setOrgs);
				saveEntity.setNoticeDepts(orgs);
			}
			// 如果通知人员不为空时处理
			if (StringUtils.isNotEmpty(userIds)) {
				propValue = userIds.split(",");
				Set<SysUser> users = saveEntity.getNoticeUsers();
				List<SysUser> setUsers = userService.queryByProerties("uuid", propValue);
				users.addAll(setUsers);
				saveEntity.setNoticeUsers(users);
			}
			// 如果通知角色不为空时处理
			if (StringUtils.isNotEmpty(roleIds)) {
				propValue = roleIds.split(",");
				Set<SysRole> roles = saveEntity.getNoticeRoles();
				List<SysRole> setRoles = roleService.queryByProerties("uuid", propValue);
				roles.addAll(setRoles);
				saveEntity.setNoticeRoles(roles);
			}
			

			SysUser approveUser = rightService.getApproveUser(currentUser);
			if (approveUser != null) {
				entity = this.merge(saveEntity);// 执行修改方法
				saveEntity.setIsCheck("1");
				OaNoticeauditor auditor = new OaNoticeauditor();
				auditor.setXm(approveUser.getXm());
				auditor.setUserId(approveUser.getUuid());
				auditor.setAuditState(0);
				auditor.setOaNotice(saveEntity);
				auditorService.merge(auditor);
				String regStatus = "您好," + approveUser.getXm() + "老师,有通知公告需要您尽快处理!";
				pushService.pushInfo(approveUser.getXm(), approveUser.getUserNumb(), "通知公告审批", regStatus, InfoPushWay.WX.getCode());
				return entity;
			}
			entity = this.merge(saveEntity);// 执行修改方法
			return entity;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	/**
	 * 获取指定公告的通知部门、角色、人员的信息
	 * 
	 * @param id
	 *            指定的公告id
	 * @return
	 */
	@Override
	public OaNoticeOther getNoticeOther(String id) {

		OaNoticeOther otherEntity = new OaNoticeOther();
		OaNoticeOther otherDept = this.getNoticeDeptInfo(id);
		OaNoticeOther otherRole = this.getNoticeRoleInfo(id);
		OaNoticeOther otherUser = this.getNoticeUserInfo(id);

		otherEntity.setNoticeId(id);
		otherEntity.setDeptNames(otherDept.getDeptNames());
		otherEntity.setDeptIds(otherDept.getDeptIds());
		otherEntity.setRoleIds(otherRole.getRoleIds());
		otherEntity.setRoleNames(otherRole.getRoleNames());
		otherEntity.setUserIds(otherUser.getUserIds());
		otherEntity.setUserNames(otherUser.getUserNames());

		return otherEntity;
	}

	public OaNoticeOther getNoticeDeptInfo(String id) {
		StringBuffer sbIds = new StringBuffer();
		StringBuffer sbNames = new StringBuffer();

		OaNotice getEntity = this.get(id);
		OaNoticeOther otherEntity = new OaNoticeOther();
		otherEntity.setNoticeId(id);
		// 通知部门信息
		Set<BaseOrg> orgs = getEntity.getNoticeDepts();
		for (BaseOrg baseOrg : orgs) {
			sbIds.append(baseOrg.getUuid() + ",");
			sbNames.append(baseOrg.getNodeText() + ",");
		}
		if (sbIds.length() > 0) {
			sbIds.deleteCharAt(sbIds.length() - 1);
			sbNames.deleteCharAt(sbNames.length() - 1);

			otherEntity.setDeptIds(sbIds.toString());
			otherEntity.setDeptNames(sbNames.toString());
		}

		return otherEntity;
	}

	public OaNoticeOther getNoticeRoleInfo(String id) {
		StringBuffer sbIds = new StringBuffer();
		StringBuffer sbNames = new StringBuffer();

		OaNotice getEntity = this.get(id);
		OaNoticeOther otherEntity = new OaNoticeOther();
		otherEntity.setNoticeId(id);
		Set<SysRole> roles = getEntity.getNoticeRoles();
		for (SysRole role : roles) {
			sbIds.append(role.getUuid() + ",");
			sbNames.append(role.getRoleName() + ",");
		}
		if (sbIds.length() > 0) {
			sbIds.deleteCharAt(sbIds.length() - 1);
			sbNames.deleteCharAt(sbNames.length() - 1);

			otherEntity.setRoleIds(sbIds.toString());
			otherEntity.setRoleNames(sbNames.toString());
		}
		return otherEntity;
	}

	public OaNoticeOther getNoticeUserInfo(String id) {
		StringBuffer sbIds = new StringBuffer();
		StringBuffer sbNames = new StringBuffer();

		OaNotice getEntity = this.get(id);
		OaNoticeOther otherEntity = new OaNoticeOther();
		otherEntity.setNoticeId(id);
		Set<SysUser> users = getEntity.getNoticeUsers();
		for (SysUser user : users) {
			sbIds.append(user.getUuid() + ",");
			sbNames.append(user.getXm() + ",");
		}
		if (sbIds.length() > 0) {
			sbIds.deleteCharAt(sbIds.length() - 1);
			sbNames.deleteCharAt(sbNames.length() - 1);

			otherEntity.setUserIds(sbIds.toString());
			otherEntity.setUserNames(sbNames.toString());
		}
		return otherEntity;
	}

	@Override
	public List<OaNotice> getUserOaNotice(SysUser currentUser) {
		// String hql = "from OaNotice as o inner join fetch o.noticeUsers as u
		// inner join fetch o.noticeRoles as r inner join fetch o.noticeDepts as
		// d where o.isDelete=0";
		String today = DateUtil.formatDate(new Date());
		StringBuffer hql = new StringBuffer("from OaNotice as o ");
		hql.append(" left join fetch o.noticeUsers as u ");
		hql.append(" left join fetch o.noticeRoles as r ");
		hql.append(" left join fetch o.noticeDepts as d ");
		hql.append(" where o.isDelete=0 ");
		hql.append(" and o.beginDate<='" + today + "' ");
		hql.append(" and o.endDate>='" + today + "' ");
		List<OaNotice> list = this.doQuery(hql.toString());
		String userId = currentUser.getUuid();
		StringBuffer hql2 = new StringBuffer("from SysUser as u ");
		hql2.append(" inner join fetch u.sysRoles as r ");
		hql2.append(" inner join fetch u.userDepts as d ");
		hql2.append(" where u.uuid='" + userId + "' ");
		currentUser = userService.doQuery(hql2.toString()).get(0);
/*		Set<SysRole> userRoles = currentUser.getSysRoles();
		Set<BaseOrg> userDepts = currentUser.getUserDepts();*/
		// Set<OaNotice> set = new HashSet<OaNotice>();
		List<OaNotice> list2 = new ArrayList<OaNotice>();
/*		NEXT: for (OaNotice oaNotice : list) {
			Set<SysUser> noticeUsers = oaNotice.getNoticeUsers();
			for (SysUser sysUser : noticeUsers) {
				if (sysUser.getUuid().equals(userId)) {
					list2.add(oaNotice);
					continue NEXT;
				}
			}
			Set<SysRole> noticeRoles = oaNotice.getNoticeRoles();
			for (SysRole sysRole : noticeRoles) {
				for (SysRole userRole : userRoles) {
					if (sysRole.getUuid().equals(userRole.getUuid())) {
						list2.add(oaNotice);
						continue NEXT;
					}
				}
			}
			Set<BaseOrg> noticeDepts = oaNotice.getNoticeDepts();
			for (BaseOrg baseOrg : noticeDepts) {
				for (BaseOrg userOrg : userDepts) {
					if (baseOrg.getUuid().equals(userOrg.getUuid())) {
						list2.add(oaNotice);
						continue NEXT;
					}
				}
			}
		}*/
		return list2;
	}
}