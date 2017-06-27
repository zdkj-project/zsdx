package com.zd.school.salary.salary.service.Impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.model.XcSalaryplatitem;
import com.zd.school.salary.salary.dao.XcSalaryplatitemDao;
import com.zd.school.salary.salary.service.XcSalaryitemService;
import com.zd.school.salary.salary.service.XcSalaryplatitemService;

/**
 * 
 * ClassName: XcSalaryplatitemServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description:
 * 工资套账工资项目表(XC_T_SALARYPLATITEM)实体Service接口实现类. date: 2016-12-05
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcSalaryplatitemServiceImpl extends BaseServiceImpl<XcSalaryplatitem> implements XcSalaryplatitemService {

	@Resource
	private XcSalaryitemService salaryitemService;

	@Resource
	public void setXcSalaryplatitemDao(XcSalaryplatitemDao dao) {
		this.dao = dao;
	}

	private static Logger logger = Logger.getLogger(XcSalaryplatitemServiceImpl.class);

	@Override
	public QueryResult<XcSalaryplatitem> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from XcSalaryplatitem o where 1=1 ");
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

		QueryResult<XcSalaryplatitem> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

	/**
	 * 删除工资项 并重新排序
	 * 
	 * @param 需要删除的Id
	 * @return 成功返回true 否则返回False
	 */
	@Override
	public int deleteXcSalaryplatitem(Serializable... ids) {
		try {
			List<XcSalaryplatitem> list;
			Serializable id = ids[0];
			String salaryplatId = this.get(id).getSalaryplatId();
			list = this.doQuery("from XcSalaryplatitem where salaryplatId='" + salaryplatId + "' order by orderIndex");
			if (list.size()==ids.length) {
				return -2;
			}
			for (int i = 0; i < ids.length; i++) {
				id = ids[i];
				this.deleteByPK(id);
			}
			// 对删除后的数据进行重新排序
			list = this.doQuery("from XcSalaryplatitem where salaryplatId='" + salaryplatId + "' order by orderIndex");
			for (int i = 0; i < list.size(); i++) {
				XcSalaryplatitem item = list.get(i);
				item.setOrderIndex(i + 1);
				this.merge(item);
			}
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -2;
		}

	}

	/**
     * 批量增加工资项
     * @param ids 工资项id
     * @param salaryplatId 工资套账Id
     * @return
     */
	@Override
	public boolean doBatchAdd(String idstr, String salaryplatId, String userCh) {
		try {
			this.deleteByProperties("salaryplatId", salaryplatId);
			if (StringUtils.isNotEmpty(idstr)) {
				String[] ids = idstr.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					XcSalaryplatitem perEntity = new XcSalaryplatitem();
					perEntity.setSalaryitemId(id);
					perEntity.setSalaryplatId(salaryplatId);
					String salaryitemName = salaryitemService.get(id).getSalaryitemName();
					perEntity.setSalaryitemName(salaryitemName);
					perEntity.setOrderIndex(i + 1);
					perEntity.setUpdateTime(new Date()); // 设置修改时间
					perEntity.setUpdateUser(userCh); // 设置修改人的中文名
					perEntity = this.merge(perEntity);// 执行修改方法
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
     * 批量排序
     * @param ids  需要排序的id
     * @param userCh
     * @return
     */
	@Override
	public boolean doBatchUpdate(String[] ids, String userCh) {
		try {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				// 先拿到已持久化的实体
				XcSalaryplatitem perEntity = this.get(id);
				String salaryitemName = salaryitemService.get(perEntity.getSalaryitemId()).getSalaryitemName();
				perEntity.setSalaryitemName(salaryitemName);
				perEntity.setOrderIndex(i + 1);
				perEntity.setUpdateTime(new Date()); // 设置修改时间
				perEntity.setUpdateUser(userCh); // 设置修改人的中文名
				perEntity = this.merge(perEntity);// 执行修改方法
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}