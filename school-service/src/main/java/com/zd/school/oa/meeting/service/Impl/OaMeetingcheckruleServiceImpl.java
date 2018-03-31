package com.zd.school.oa.meeting.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.meeting.dao.OaMeetingcheckruleDao;
import com.zd.school.oa.meeting.model.OaMeetingcheckrule;
import com.zd.school.oa.meeting.service.OaMeetingcheckruleService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: OaMeetingcheckruleServiceImpl
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 会议考勤规则(OA_T_MEETINGCHECKRULE)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaMeetingcheckruleServiceImpl extends BaseServiceImpl<OaMeetingcheckrule> implements OaMeetingcheckruleService{

    @Resource
    public void setOaMeetingcheckruleDao(OaMeetingcheckruleDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(OaMeetingcheckruleServiceImpl.class);
	
	@Override
	public QueryResult<OaMeetingcheckrule> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<OaMeetingcheckrule> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
	public OaMeetingcheckrule doUpdateEntity(OaMeetingcheckrule entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		OaMeetingcheckrule saveEntity = this.get(entity.getUuid());
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
	public OaMeetingcheckrule doAddEntity(OaMeetingcheckrule entity, SysUser currentUser) {
		OaMeetingcheckrule saveEntity = new OaMeetingcheckrule();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity,excludedProp);
			saveEntity.setCreateUser(currentUser.getUuid()); // 设置修改人的中文名
            saveEntity.setUpdateUser(currentUser.getUuid());
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

    @Override
    public QueryResult<OaMeetingcheckrule> list(Integer start, Integer limit, String sort, String filter, String whereSql) {
        String orderSql = "";
        if (StringUtils.isNotEmpty(sort)) {
            orderSql = " order by " + StringUtils.convertSortToSql(sort);
        }
        if (StringUtils.isNotEmpty(filter)) {
            whereSql += StringUtils.convertFilterToSql(filter);
        }
        String hql = " from OaMeetingcheckrule o where o.isDelete=0 " + whereSql + orderSql;
        QueryResult<OaMeetingcheckrule> queryResult = this.getQueryResult(hql, start, limit);
        if (queryResult.getResultList().size() > 0)
            return queryResult;
        else
            return null;
    }
}