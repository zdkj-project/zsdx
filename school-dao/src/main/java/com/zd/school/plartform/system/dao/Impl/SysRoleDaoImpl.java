package com.zd.school.plartform.system.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.system.dao.SysRoleDao;
import com.zd.school.plartform.system.model.SysRole;

/**
 * 
 * ClassName: BaseTRoleDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Dao接口实现类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class SysRoleDaoImpl extends BaseDaoImpl<SysRole> implements SysRoleDao {
    public SysRoleDaoImpl() {
        super(SysRole.class);
        // TODO Auto-generated constructor stub
    }

    //	@Override
    //	public List<SysRole> doQueryForIn(String hql, Integer start, Integer limit, Object[] objs) {
    //		// TODO Auto-generated method stub		
    //		Query query = this.getSession().createQuery(hql);
    //		query.setParameterList("roles", objs);
    //        if (limit > 0) {
    //            query.setFirstResult(start);
    //            query.setMaxResults(limit);
    //            
    //        }
    //        return query.list();
    //	}

}