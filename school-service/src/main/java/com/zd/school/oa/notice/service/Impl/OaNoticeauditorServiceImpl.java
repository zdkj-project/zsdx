package com.zd.school.oa.notice.service.Impl;

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
import com.zd.school.oa.notice.dao.OaNoticeauditorDao ;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticeauditor ;
import com.zd.school.oa.notice.service.OaNoticeService;
import com.zd.school.oa.notice.service.OaNoticeauditorService ;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaNoticeauditorServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公告审核人(OA_T_NOTICEAUDITOR)实体Service接口实现类.
 * date: 2016-12-21
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaNoticeauditorServiceImpl extends BaseServiceImpl<OaNoticeauditor> implements OaNoticeauditorService{

    @Resource
    public void setOaNoticeauditorDao(OaNoticeauditorDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(OaNoticeauditorServiceImpl.class);
	
	@Resource
	private OaNoticeService noticeService;
	
	@Override
	public QueryResult<OaNoticeauditor> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<OaNoticeauditor> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
	public OaNoticeauditor doUpdateEntity(OaNoticeauditor entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		OaNoticeauditor saveEntity = this.get(entity.getUuid());
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
	public OaNoticeauditor doAddEntity(OaNoticeauditor entity, SysUser currentUser) {
		OaNoticeauditor saveEntity = new OaNoticeauditor();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity,excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
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
	public QueryResult<OaNotice> userlist(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);
		StringBuffer hql = new StringBuffer("from OaNotice where 1=1 ");

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

		QueryResult<OaNotice> qResult = noticeService.getQueryResult(hql.toString(), start, limit);
		return qResult;
	}
}