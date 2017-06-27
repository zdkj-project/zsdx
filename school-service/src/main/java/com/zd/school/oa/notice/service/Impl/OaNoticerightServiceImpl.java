package com.zd.school.oa.notice.service.Impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.notice.dao.OaNoticerightDao;
import com.zd.school.oa.notice.model.OaNoticeright;
import com.zd.school.oa.notice.service.OaNoticerightService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

@Service
@Transactional
public class OaNoticerightServiceImpl extends BaseServiceImpl<OaNoticeright> implements OaNoticerightService {

	@Resource
	private SysRoleService roleService; // 角色数据服务接口

	@Resource
	private SysUserService userService;
	
	private static Logger logger = Logger.getLogger(OaNoticerightServiceImpl.class);

	@Resource
	public void setOaNoticerightDao(OaNoticerightDao dao) {
		this.dao = dao;
	}

	@Override
	public QueryResult<OaNoticeright> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);
		StringBuffer hql = new StringBuffer("from SysRole as r inner join fetch r.sysPermissions as p ");
		hql.append("where p.perText='通知公告' and r.isDelete=0 and p.isDelete=0 ");
		List<SysRole> roleList = roleService.doQuery(hql.toString());

		hql = new StringBuffer("from OaNoticeright o where 1=1 ");
		List<OaNoticeright> noticeList = this.doQuery(hql.toString());

		if (noticeList == null || noticeList.size() == 0) {
			for (int i = 0; i < roleList.size(); i++) {
				SysRole role = roleList.get(i);
				OaNoticeright notice = new OaNoticeright();
				notice.setOwnRoleid(role.getUuid());
				this.merge(notice);
			}
		} else {
			for (int i = 0; i < noticeList.size(); i++) {
				boolean found = false;
				OaNoticeright notice = noticeList.get(i);
				for (int j = 0; j < roleList.size(); j++) {
					SysRole role = roleList.get(j);
					if (role.getUuid().equals(notice.getOwnRoleid())) {
						found = true;
						break;
					}
					if (j == roleList.size() - 1 && !found) {
						this.delete(notice);
					}
				}
			}
			noticeList = this.doQuery(hql.toString());
			for (int i = 0; i < roleList.size(); i++) {
				SysRole role = roleList.get(i);
				boolean found = false;
				for (int j = 0; j < noticeList.size(); j++) {
					OaNoticeright notice = noticeList.get(j);
					if (role.getUuid().equals(notice.getOwnRoleid())) {
						found = true;
						break;
					}
					if (j == noticeList.size() - 1 && !found) {
						notice = new OaNoticeright();
						notice.setOwnRoleid(role.getUuid());
						this.merge(notice);
					}

				}
			}
		}

		hql.append(whereSql);
		hql.append(filterSql);
		if (orderSql.length() > 0) {
			if (sortSql.length() > 0)
				hql.append(orderSql + " , " + sortSql);
			else
				hql.append(orderSql);
		} else {
			if (sortSql.length() > 0)
				hql.append(" order by  " + sortSql);
		}

		QueryResult<OaNoticeright> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

	@Override
	public SysUser getApproveUser(SysUser submitUser) {
		SysUser approveUser = null;
		try {
			String hql = "from SysUser as u inner join fetch u.sysRoles as r where u.uuid='" + submitUser.getUuid()
					+ "' and r.isDelete=0 and u.isDelete=0 ";
			SysUser user = userService.doQuery(hql).get(0);
			Set<SysRole> userRoles = user.getSysRoles();
			List<OaNoticeright> list = this.doQueryAll();
			for (SysRole sysRole : userRoles) {
				for (OaNoticeright oaNoticeright : list) {
					if (sysRole.getUuid().equals(oaNoticeright.getOwnRoleid())) {
						QueryResult<SysUser> qResult = userService.getUserByRoleId(oaNoticeright.getCheckRoleid());
						approveUser = qResult.getResultList().get(0);
						return approveUser;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return approveUser;
	}

}