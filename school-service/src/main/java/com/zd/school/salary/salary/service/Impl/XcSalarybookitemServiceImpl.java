package com.zd.school.salary.salary.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.model.XcSalarybookitem ;
import com.zd.school.salary.salary.dao.XcSalarybookitemDao ;
import com.zd.school.salary.salary.service.XcSalarybookitemService ;

/**
 * 
 * ClassName: XcSalarybookitemServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资信息表(XC_T_SALARYBOOKITEM)实体Service接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcSalarybookitemServiceImpl extends BaseServiceImpl<XcSalarybookitem> implements XcSalarybookitemService{

    @Resource
    public void setXcSalarybookitemDao(XcSalarybookitemDao dao) {
        this.dao = dao;
    }

	@Override
	public QueryResult<XcSalarybookitem> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql,SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from XcSalarybookitem o where 1=1 ");
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
        
        QueryResult<XcSalarybookitem> qResult = this.getQueryResult(hql.toString(), start, limit);
		return qResult;
	}
}