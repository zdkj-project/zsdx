package com.zd.school.plartform.baseset.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.ModelUtil;
import com.zd.school.plartform.baseset.dao.BaseUserdeptjobDao;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseUserdeptjob;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.baseset.service.BaseUserdeptjobService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: BaseUserdeptjobServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 用户部门岗位(BASE_T_USERDEPTJOB)实体Service接口实现类. date:
 * 2017-03-27
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BaseUserdeptjobServiceImpl extends BaseServiceImpl<BaseUserdeptjob> implements BaseUserdeptjobService {

	@Resource
	public void setBaseUserdeptjobDao(BaseUserdeptjobDao dao) {
		this.dao = dao;
	}
	@Resource
	private SysUserService userService;
	
	@Resource
	private BaseDeptjobService baseDeptjobService;
	
	private static Logger logger = Logger.getLogger(BaseUserdeptjobServiceImpl.class);

	@Override
	public List<BaseUserdeptjob> getUserDeptJobList(SysUser currentUser) {
		Map<String, String> sortedCondition = new HashMap<>();
		sortedCondition.put("masterDept", "desc");
		sortedCondition.put("jobLevel", "asc");
		String[] propName = { "userId", "isDelete" };
		Object[] propValue = { currentUser.getUuid(), 0 };

		List<BaseUserdeptjob> list = this.queryByProerties(propName, propValue, sortedCondition);

		return list;
	}

	@Override
	public Map<String, BaseUserdeptjob> getUserDeptJobMaps(SysUser currentUser) {
		Map<String, BaseUserdeptjob> maps = new HashMap<>();
		List<BaseUserdeptjob> list = this.getUserDeptJobList(currentUser);
		for (BaseUserdeptjob job : list) {
			maps.put(job.getDeptjobId(), job);
		}
		return maps;
	}

	@Override
	public BaseUserdeptjob getUserMasterDeptJob(SysUser currentUser) {
		String[] propName = { "userId", "masterDept", "isDelete" };
		Object[] propValue = { currentUser.getUuid(), 1, 0 };
		try {
			BaseUserdeptjob isMasterDeptJob = this.getByProerties(propName, propValue);
			return isMasterDeptJob;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	// @Override
	// public QueryResult<BaseUserdeptjob> list(Integer start, Integer limit,
	// String sort, String filter, Boolean isDelete) {
	// QueryResult<BaseUserdeptjob> qResult = this.doPaginationQuery(start,
	// limit, sort, filter, isDelete);
	// return qResult;
	// }
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
			Object[] propertyValue = { 1, currentUser.getUuid(), new Date() };
			this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			delResult = false;
		}
		return delResult;
	}

	@Override
	public boolean addUserToDeptJob(String deptJobId, String userId, SysUser currentUser) {
		Boolean reResult = false;
		String[] userIds = userId.split(",");
		try {
			// 所有要设置的用户
			List<SysUser> users = userService.queryByProerties("uuid", userIds);
			
			String hql = " select a from BaseDeptjob a  where a.uuid in ('" + deptJobId.replace(",", "','")
					+ "') order by a.jobLevel asc ";
			List<BaseDeptjob> deptjobs = baseDeptjobService.getQuery(hql);
			
			for (SysUser user : users) {
				// 查询当前用户已有的部门岗位
				Map<String, BaseUserdeptjob> userHasJobMap = this.getUserDeptJobMaps(user);
				BaseUserdeptjob isMasterDeptJob = this.getUserMasterDeptJob(user);
				for (int i = 0; i < deptjobs.size(); i++) {
					String uuid = deptjobs.get(i).getUuid(); // 选择的部门岗位Id
					if (userHasJobMap.get(uuid) == null) {
						// 如果当前岗位还没有设置成此教师的部门岗位
						BaseUserdeptjob userDeptJob = new BaseUserdeptjob();
						userDeptJob.setDeptId(deptjobs.get(i).getDeptId());
						userDeptJob.setJobId(deptjobs.get(i).getJobId());
						userDeptJob.setDeptjobId(uuid);
						userDeptJob.setUserId(user.getUuid());
						userDeptJob.setCreateTime(new Date());
						userDeptJob.setCreateUser(currentUser.getUuid());
						// 当前人没有主工作部门时将一个岗位设置为主部门
						if (!ModelUtil.isNotNull(isMasterDeptJob) && i == 0) {
							userDeptJob.setMasterDept(1);
						} else
							userDeptJob.setMasterDept(0);

						this.doMerge(userDeptJob);
					}
				}
				// 将老师从临时部门删除
				user.setDeptId("");
				user.setUpdateTime(new Date());
				user.setUpdateUser(currentUser.getUuid());
				userService.doMerge(user);
			}

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			//手动回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	@Override
	public boolean removeUserFromDeptJob(String delIds, SysUser currentUser) {
		try {
			return this.doLogicDeleteByIds(delIds, currentUser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	@Override
	public boolean setMasterDeptJob(String delIds, String userId, SysUser currentUser) {
		try {
			// 先将原来的主部门岗位设置成非主部门岗位
			SysUser user = userService.get(userId);
			BaseUserdeptjob oldMaster = this.getUserMasterDeptJob(user);
			if (ModelUtil.isNotNull(oldMaster)) {
				oldMaster.setMasterDept(0);
				oldMaster.setUpdateTime(new Date());
				oldMaster.setUpdateUser(currentUser.getUuid());
				this.doMerge(oldMaster);
			}
			
			// 将新的部门岗位设置为主部门岗位
			String[] propertyName = { "masterDept", "updateTime", "updateUser" };
			Object[] propertyValue = { 1, new Date(), currentUser.getUuid() };
			this.doUpdateByProperties("uuid", delIds, propertyName, propertyValue);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
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
	/*
	 * @Override public BaseUserdeptjob doUpdateEntity(BaseUserdeptjob entity,
	 * SysUser currentUser) { // 先拿到已持久化的实体 BaseUserdeptjob saveEntity =
	 * this.get(entity.getUuid()); try { BeanUtils.copyProperties(saveEntity,
	 * entity); saveEntity.setUpdateTime(new Date()); // 设置修改时间
	 * saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名 entity =
	 * this.merge(saveEntity);// 执行修改方法
	 * 
	 * return entity; } catch (IllegalAccessException e) {
	 * logger.error(e.getMessage()); return null; } catch
	 * (InvocationTargetException e) { logger.error(e.getMessage()); return
	 * null; } }
	 */
	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	/*
	 * @Override public BaseUserdeptjob doAddEntity(BaseUserdeptjob entity,
	 * SysUser currentUser) { BaseUserdeptjob saveEntity = new
	 * BaseUserdeptjob(); try { List<String> excludedProp = new ArrayList<>();
	 * excludedProp.add("uuid"); BeanUtils.copyProperties(saveEntity, entity,
	 * excludedProp); saveEntity.setCreateUser(currentUser.getXm()); //
	 * 设置修改人的中文名 entity = this.merge(saveEntity);// 执行修改方法
	 * 
	 * return entity; } catch (IllegalAccessException e) {
	 * logger.error(e.getMessage()); return null; } catch
	 * (InvocationTargetException e) { logger.error(e.getMessage()); return
	 * null; } } }
	 */}