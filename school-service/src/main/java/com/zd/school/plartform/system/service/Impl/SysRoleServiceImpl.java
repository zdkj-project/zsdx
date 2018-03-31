package com.zd.school.plartform.system.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.plartform.system.dao.SysRoleDao;
import com.zd.school.plartform.system.model.SysPermission;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysPerimissonService;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

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

    @Resource
    private SysUserService userService;
    
    @Resource
    private SysPerimissonService perimissonSevice;

    @Override
    public SysRole doAddEntity(SysRole entity, SysUser currentUser) throws InvocationTargetException, IllegalAccessException {
        SysRole saveEntity = new SysRole();
        List<String> excludedProp = new ArrayList<>();
        excludedProp.add("uuid");
        BeanUtils.copyProperties(saveEntity, entity, excludedProp);
        saveEntity.setIsHidden("0");
        saveEntity.setCreateUser(currentUser.getUuid());
        saveEntity.setUpdateTime(new Date());
        entity = this.doMerge(saveEntity);// 执行修改方法

        return entity;
    }

    @Override
    public SysRole doUpdateEntity(SysRole entity, SysUser currentUser) throws InvocationTargetException, IllegalAccessException {
        //先拿到已持久化的实体
        SysRole saveEntity = this.get(entity.getUuid());
        List<String> excludedProp = new ArrayList<>();
        excludedProp.add("uuid");
        excludedProp.add("sysPermissions");
        BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludedProp);
        saveEntity.setUpdateUser(currentUser.getUuid());
        saveEntity.setUpdateTime(new Date());
        entity = this.doMerge(saveEntity);

        return entity;
    }

    public QueryResult<SysUser> getRoleUser(String roleId, Integer start, Integer limit) {
        if(limit==0)
            return userService.getUserByRoleId(roleId);
        else
            return userService.getUserByRoleId(roleId,start,limit,"","");
    }

    @Override
    public Boolean doDeleteRoleUser(String ids, String userId) {
        try {
            String temp = userId.replace(",","','");
            String sql = " delete from SYS_T_ROLEUSER where role_id=''{0}'' and user_id in (''{1}'')";
            sql = MessageFormat.format(sql,ids,temp);
            Integer executeCount = this.doExecuteSql(sql);
            if(executeCount>0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean doAddRoleUser(String ids, String userId) {
        String[] userIds = userId.split(",");
        StringBuilder sb = new StringBuilder();
        String sql = " insert into SYS_T_ROLEUSER(role_id,user_id) values(''{0}'',''{1}'') ";
        int userCount = userIds.length;
        for (int i = 0; i < userCount; i++) {
            sb.append(MessageFormat.format(sql, ids, userIds[i]));
        }
        try {
            Integer executeCount = this.doExecuteSql(sb.toString());
            if(executeCount>0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

	@Override
	public List<SysPermission> getSysPermissionList(SysRole role) {
		String sql = "select p.PER_ID as uuid,p.PER_TYPE as perType,p.PER_CODE as perCode,p.PER_PATH as perPath,"
				+ " (SELECT a.NODE_TEXT FROM SYS_T_MENU a WHERE a.MENU_ID=p.PER_CODE) as perText  from SYS_T_PERIMISSON p"
				+ " inner join SYS_T_ROLEPERM rp on p.PER_ID=rp.PER_ID "
				+ " inner join SYS_T_ROLE r on rp.ROLE_ID=r.ROLE_ID where "
				+ " r.ROLE_ID='"+role.getUuid()+"' "
				+ " and r.ISDELETE=0 "
				+ " and p.ISDELETE=0 ";
		
		 
		List<SysPermission> list = perimissonSevice.doQuerySqlObject(sql, SysPermission.class);
		 
		return list;
	}
}