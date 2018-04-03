package com.zd.school.salary.jxsalary.service.Impl;

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
import com.zd.school.salary.jxsalary.model.XcJxplartitem ;
import com.zd.school.salary.jxsalary.dao.XcJxplartitemDao ;
import com.zd.school.salary.jxsalary.service.XcJxplartitemService ;
import com.zd.school.salary.salary.model.XcSalaryplatitem;
import com.zd.school.salary.salary.service.XcSalaryitemService;
import com.zd.school.salary.salary.service.Impl.XcSalaryplatitemServiceImpl;

/**
 * 
 * ClassName: XcJxplartitemServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效套账工资项目表(XC_T_JXPLARTITEM)实体Service接口实现类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcJxplartitemServiceImpl extends BaseServiceImpl<XcJxplartitem> implements XcJxplartitemService{
	
	@Resource
	private XcSalaryitemService salaryitemService;

    @Resource
    public void setXcJxplartitemDao(XcJxplartitemDao dao) {
        this.dao = dao;
    }
    
    private static Logger logger = Logger.getLogger(XcJxplartitemServiceImpl.class);

	@Override
	public QueryResult<XcJxplartitem> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql,SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from XcJxplartitem o where 1=1 ");
		hql.append(whereSql);
		hql.append(filterSql);
        if (orderSql.length()>0){
        	if (sortSql.length()>0)
        		hql.append(orderSql+ " , " + sortSql);
        	else 
        		hql.append(orderSql);
        } else {
        	if (sortSql.length()>0)
        		hql.append(" order by  " + sortSql);
        }
        
        QueryResult<XcJxplartitem> qResult = this.getQueryResult(hql.toString(), start, limit);
		return qResult;
	}
	
	/**
	 * 删除工资项 并重新排序
	 * 
	 * @param 需要删除的Id
	 * @return 成功返回true 否则返回False
	 */
	@Override
	public int deleteXcJxplartitem(Serializable... ids) {
		try {
			List<XcJxplartitem> list;
			Serializable id = ids[0];
			String jxplartId = this.get(id).getJxplartId();
			list = this.getQuery("from XcJxplartitem where jxplartId='" + jxplartId + "' order by orderIndex");
			if (list.size()==ids.length) {
				return -2;
			}
			for (int i = 0; i < ids.length; i++) {
				id = ids[i];
				this.doDeleteByPK(id);
			}
			// 对删除后的数据进行重新排序
			list = this.getQuery("from XcJxplartitem where jxplartId='" + jxplartId + "' order by orderIndex");
			for (int i = 0; i < list.size(); i++) {
				XcJxplartitem item = list.get(i);
				item.setOrderIndex(i + 1);
				this.doMerge(item);
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
	public boolean doBatchAdd(String idstr, String jxplartId, String userCh) {
		try {
			this.doDeleteByProperties("jxplartId", jxplartId);
			if (StringUtils.isNotEmpty(idstr)) {
				String[] ids = idstr.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					XcJxplartitem perEntity = new XcJxplartitem();
					perEntity.setSalaryitemId(id);
					perEntity.setJxplartId(jxplartId);
					String salaryitemName = salaryitemService.get(id).getSalaryitemName();
					perEntity.setSalaryitemName(salaryitemName);
					perEntity.setOrderIndex(i + 1);
					perEntity.setUpdateTime(new Date()); // 设置修改时间
					perEntity.setUpdateUser(userCh); // 设置修改人的中文名
					perEntity = this.doMerge(perEntity);// 执行修改方法
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
				XcJxplartitem perEntity = this.get(id);
				String salaryitemName = salaryitemService.get(perEntity.getSalaryitemId()).getSalaryitemName();
				perEntity.setSalaryitemName(salaryitemName);
				perEntity.setOrderIndex(i + 1);
				perEntity.setUpdateTime(new Date()); // 设置修改时间
				perEntity.setUpdateUser(userCh); // 设置修改人的中文名
				perEntity = this.doMerge(perEntity);// 执行修改方法
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}