package com.zd.school.jw.ecc.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.ecc.model.EccClassstar ;
import com.zd.school.jw.ecc.dao.EccClassstarDao ;
import com.zd.school.jw.ecc.service.EccClassstarService ;

/**
 * 
 * ClassName: EccClassstarServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 班级评星信息(ECC_T_CLASSSTAR)实体Service接口实现类.
 * date: 2016-12-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class EccClassstarServiceImpl extends BaseServiceImpl<EccClassstar> implements EccClassstarService{

    @Resource
    public void setEccClassstarDao(EccClassstarDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(EccClassstarServiceImpl.class);
	
	@Override
	public QueryResult<EccClassstar> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<EccClassstar> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
			this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
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
	public EccClassstar doUpdateEntity(EccClassstar entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		EccClassstar saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyProperties(saveEntity, entity);
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(saveEntity);// 执行修改方法

			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
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
	public EccClassstar doAddEntity(EccClassstar entity, SysUser currentUser) {
		List<String> excludedProp = new ArrayList<>();
		excludedProp.add("uuid");
		excludedProp.add("claiId");
		excludedProp.add("className");
		try {
			String [] claiIds = entity.getClaiId().split(",");
			String [] classNames = entity.getClassName().split(",");
			for (int i = 0; i < claiIds.length; i++) {
				EccClassstar saveEntity = new EccClassstar();

				BeanUtils.copyProperties(saveEntity, entity,excludedProp);
				saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
				saveEntity.setClaiId(claiIds[i]);
				saveEntity.setClassName(classNames[i]);
				entity = this.doMerge(saveEntity);// 执行修改方法
			}
			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}