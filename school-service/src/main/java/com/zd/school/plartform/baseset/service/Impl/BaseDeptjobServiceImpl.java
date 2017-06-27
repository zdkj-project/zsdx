package com.zd.school.plartform.baseset.service.Impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.dao.BaseDeptjobDao;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseDpetJobTree;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseDeptjobServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 部门岗位信息(BASE_T_DEPTJOB)实体Service接口实现类. date:
 * 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BaseDeptjobServiceImpl extends BaseServiceImpl<BaseDeptjob> implements BaseDeptjobService {

	@Resource
	public void setBaseDeptjobDao(BaseDeptjobDao dao) {
		this.dao = dao;
	}

	@Resource
	private BaseOrgService deptService;

	@Resource
	BaseJobService jobService;

	private static Logger logger = Logger.getLogger(BaseDeptjobServiceImpl.class);

	@Override
	public QueryResult<BaseDeptjob> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
		QueryResult<BaseDeptjob> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
	 * 设置部门所包含的岗位
	 * 
	 * @param deptId
	 *            要设置的部门的ID,多个部门用英文逗号隔开
	 * @param jobId
	 *            包含岗位的ID，多个岗位用英文逗号隔开
	 * @param currentUser
	 *            当前操作员
	 * @return
	 */
	@Override
	public Boolean batchSetDeptJob(String deptId, String jobId, SysUser currentUser) {
		String[] jobIds = jobId.split(",");
		String[] deptIds = deptId.split(",");
		try {
			// 待设置的部门清单
			List<BaseOrg> setDeptList = deptService.queryByProerties("uuid", deptIds);

			// 待设置部门的负责岗位清单
			String[] propName = { "deptId", "jobType" };
			Object[] propValue = { deptIds, 0 };
			List<BaseDeptjob> mainJob = this.queryByProerties(propName, propValue);
			Map<String, BaseDeptjob> maps = new HashMap<String, BaseDeptjob>();
			for (BaseDeptjob baseDeptjob : mainJob) {
				maps.put(baseDeptjob.getDeptId(), baseDeptjob);
			}
			// 待设置部门已有岗位清单
			Map<String, String> mapHasJob = new HashMap<String, String>();
			List<BaseDeptjob> deptHasJob = this.queryByProerties("deptId", deptIds);
			String key = "";
			for (BaseDeptjob baseDeptjob : deptHasJob) {
				key = baseDeptjob.getDeptId() + "," + baseDeptjob.getJobId();
				mapHasJob.put(key, baseDeptjob.getJobId());
				key = "";
			}

			BaseDeptjob deptjob = null;
			for (BaseOrg setDept : setDeptList) {
				for (int i = 0; i < jobIds.length; i++) {
					key = setDept.getUuid() + "," + jobIds[i];
					if (mapHasJob.get(key) == null) {
						BaseJob setJob = jobService.get(jobIds[i]);
						deptjob = new BaseDeptjob();
						deptjob.setDeptId(setDept.getUuid()); // 部门ID
						deptjob.setDeptName(setDept.getNodeText()); // 部门名称
						deptjob.setParentdeptId(setDept.getSuperDept());// 上级部门Id,默认为所在部门的上级主管岗位
						deptjob.setParentdeptName(setDept.getSuperdeptName());
						deptjob.setAllDeptName(setDept.getAllDeptName());

						deptjob.setJobId(setJob.getUuid());
						deptjob.setJobName(setJob.getJobName());
						deptjob.setParentjobId(setDept.getSuperJob());
						deptjob.setParentjobName(setDept.getSuperjobName());

						deptjob.setCreateUser(currentUser.getUuid());
						// 将第一个岗位设置成部门的负责岗位
						if (i == 0 && maps.get(setDept.getUuid()) == null)
							deptjob.setJobType(0);
						else
							deptjob.setJobType(2);

						this.merge(deptjob);
					}
					key = "";
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 删除部门的岗位
	 * 
	 * @param deptJobId
	 *            要删除的部门岗ID，多个岗位用英文逗号隔开
	 * @param currentUser
	 *            当前操作员
	 * @return
	 */
	@Override
	public Boolean delDeptJob(String deptJobId, SysUser currentUser) {
		try {
			this.deleteByPK(deptJobId);
			return true;
			/*
			 * String[] ids = deptJobId.split(","); StringBuffer canDelId = new
			 * StringBuffer(); String checkStr = ""; try { for (String uuid :
			 * ids) { BaseDeptjob deptJob = this.get(uuid); String deptid =
			 * deptJob.getDeptId(); String jobid = deptJob.getJobId();
			 * 
			 * // 检查要删除的部门岗位是否是其它的部门岗位的上级 String[] propName = { "parentdeptId",
			 * "parentjobId" }; Object[] propValue = { deptid, jobid };
			 * List<BaseDeptjob> isSuperJob = this.queryByProerties(propName,
			 * propValue); if (isSuperJob.size() > 0) { checkStr =
			 * "有些岗位是其它岗位的上级，不能删除"; continue; } canDelId.append(uuid + ","); }
			 * if (canDelId.length() > 0) { canDelId =
			 * canDelId.deleteCharAt(canDelId.length() - 1);
			 * this.deleteByPK(canDelId.toString().split(",")); } return
			 * checkStr;
			 */
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public String chkIsSuperJob(String deptJobId) {
		BaseDeptjob deptjob = this.get(deptJobId);

		return this.chkIsSuperJob(deptjob.getDeptId(), deptjob.getJobId());
	}

	@Override
	public String chkIsSuperJob(String deptid, String jobId) {
		StringBuffer sbCheck = new StringBuffer();

		// 检查指定部门的指定岗位是否其它部门岗位的上级
		String[] propName = { "parentdeptId", "parentjobId" };
		Object[] propValue = { deptid, jobId };
		List<BaseDeptjob> isParentJob = this.queryByProerties(propName, propValue);
		for (BaseDeptjob baseDeptjob : isParentJob) {
			sbCheck.append(MessageFormat.format("{0},", baseDeptjob.getAlldeptjobName()));
		}
		// 检查指定部门的指定岗位是否其它部门的上级
		propName[0] = "superDept";
		propName[1] = "superJob";
		List<BaseOrg> isSuperJob = deptService.queryByProerties(propName, propValue);
		for (BaseOrg baseOrg : isSuperJob) {
			sbCheck.append(MessageFormat.format("{0},", baseOrg.getAllDeptName()));
		}
		if (sbCheck.length() > 0)
			sbCheck = sbCheck.deleteCharAt(sbCheck.length() - 1);

		return sbCheck.toString();
	}

	@Override
	public Boolean setDeptLeaderJob(String deptId, String deptJobId, SysUser currentUser) {
		try {
			String[] conditionName = { "deptId", "jobType" };
			Object[] conditionValue = { deptId, 0 };
			String[] propertyName = { "jobType", "updateUser", "updateTime" };
			Object[] propertyValue = { 2, currentUser.getUuid(), new Date() };

			// 将该部门原来的负责岗位设置为不是负责岗位
			this.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);

			// 将将当前的岗位设置为部门负责岗位
			propertyValue[0] = 0;
			propertyValue[1] = currentUser.getUuid();
			propertyValue[2] = new Date();
			this.updateByProperties("uuid", deptJobId, propertyName, propertyValue);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public BaseDpetJobTree getDeptJobTree(String rootId, String whereSql) {
		List<BaseDpetJobTree> list = getDeptJobTreeList(rootId, whereSql);
		BaseDpetJobTree root = new BaseDpetJobTree();
		for (BaseDpetJobTree node : list) {
			if (!(StringUtils.isNotEmpty(node.getParent()) && !node.getId().equals(rootId))) {
				root = node;
				list.remove(node);
				break;
			}
		}
		createTreeChildren(list, root);
		return root;
	}

	@Override
	public List<BaseDpetJobTree> getDeptJobTreeList(String root, String whereSql) {
		// StringBuilder sbSql = new StringBuilder("WITH ctr_child(id,text,
		// iconcls,leaf,level,treeids,parent)");
		// sbSql.append(" AS ( SELECT id,text, iconcls,leaf,level,treeids,parent
		// FROM BASE_V_DEPTJOBTREE WHERE id = '"
		// + root + "'");
		// sbSql.append(
		// " UNION ALL SELECT a.id,a.text,
		// a.iconcls,a.leaf,a.level,a.treeids,a.parent FROM BASE_V_DEPTJOBTREE a
		// INNER JOIN ctr_child b");
		// sbSql.append(" ON a.parent=b.id) SELECT * FROM ctr_child ");
		String sbSql = " EXEC BASE_P_GETDEPTJOBTREE";
		List<BaseDpetJobTree> chilrens = new ArrayList<BaseDpetJobTree>();
		List<?> alist = this.doQuerySql(sbSql);
		for (int i = 0; i < alist.size(); i++) {
			Object[] obj = (Object[]) alist.get(i);
			BaseDpetJobTree node = new BaseDpetJobTree();
			node.setId((String) obj[0]);
			node.setText((String) obj[1]);
			node.setIconCls((String) obj[2]);

			if ("true".equals(obj[3])) {
				node.setLeaf(true);
			} else {
				node.setLeaf(false);
			}
			node.setLevel((Integer) obj[4]);
			node.setTreeid((String) obj[5]);
			node.setParent((String) obj[6]);

			chilrens.add(node);
		}
		return chilrens;
	}

	private void createTreeChildren(List<BaseDpetJobTree> childrens, BaseDpetJobTree root) {
		String parentId = root.getId();
		for (int i = 0; i < childrens.size(); i++) {
			BaseDpetJobTree node = childrens.get(i);
			if (StringUtils.isNotEmpty(node.getParent()) && node.getParent().equals(parentId)) {
				root.getChildren().add(node);
				createTreeChildren(childrens, node);
			}
			if (i == childrens.size() - 1) {
				if (root.getChildren().size() < 1) {
					root.setLeaf(true);
				}
				return;
			}
		}
	}

	@Override
	public Boolean setSuperJob(String ids, String setIds, String setType, SysUser currentUser) {
		String[] setId = setIds.split(",");
		BaseDeptjob deptjob = this.get(ids);
		String deptId = deptjob.getDeptId();
		String deptName = deptjob.getDeptName();
		String jobId = deptjob.getJobId();
		String jobName = deptjob.getJobName();
		try {
			if ("dept".equals(setType)) {
				List<BaseOrg> depts = deptService.queryByProerties("uuid", setId);
				for (BaseOrg baseOrg : depts) {
					baseOrg.setSuperDept(deptId);
					baseOrg.setSuperdeptName(deptName);
					baseOrg.setSuperJob(jobId);
					baseOrg.setSuperjobName(jobName);
					baseOrg.setUpdateUser(currentUser.getUuid());
					baseOrg.setUpdateTime(new Date());

					deptService.merge(baseOrg);
				}
			} else {
				List<BaseDeptjob> setDeptJob = this.queryByProerties("uuid", setId);
				for (BaseDeptjob baseDeptjob : setDeptJob) {
					baseDeptjob.setParentdeptId(deptId);
					baseDeptjob.setParentdeptName(deptName);
					baseDeptjob.setParentjobId(jobId);
					baseDeptjob.setParentjobName(jobName);
					baseDeptjob.setUpdateUser(currentUser.getUuid());
					baseDeptjob.setUpdateTime(new Date());

					this.merge(baseDeptjob);
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}