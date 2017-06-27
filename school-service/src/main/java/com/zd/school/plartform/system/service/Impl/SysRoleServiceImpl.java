package com.zd.school.plartform.system.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.dao.SysRoleDao;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.service.SysRoleService;

/**
 * 
 * ClassName: BaseTRoleServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Service接口实现类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

    @Resource
    public void setBaseTRoleDao(SysRoleDao dao) {
        this.dao = dao;
    }

    //	@Autowired
    //	SysRoleDao sysRoleDao;
    //    
    //    public List<SysRole> doQueryForIn(String hql, Integer start, Integer limit,Object[] objs){    
    //    	
    //		return sysRoleDao.doQueryForIn(hql, start, limit, objs);    
    //    }
}