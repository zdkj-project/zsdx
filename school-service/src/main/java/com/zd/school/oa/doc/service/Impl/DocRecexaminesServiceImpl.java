package com.zd.school.oa.doc.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.oa.doc.dao.DocRecexaminesDao;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.service.DocRecexaminesService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: DocRecexaminesServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 公文收文批阅人实体Service接口实现类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocRecexaminesServiceImpl extends BaseServiceImpl<DocRecexamines> implements DocRecexaminesService {

	@Resource
	public void setDocRecexaminesDao(DocRecexaminesDao dao) {
		this.dao = dao;
	}
    @Resource
    private SysUserService userService;
    
    @Resource
    private PushInfoService pushService;
    
	@Override
	public DocRecexamines setIsPiYue(DocRecexamines entity, String distribType, String distribId, SysUser currentUser)
			throws IllegalAccessException, InvocationTargetException {

		// 保存当前操作的批阅信息
		DocRecexamines perEntity = this.get(entity.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);
		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
		perEntity.setState("1");
		entity = this.merge(perEntity);// 执行修改方法

		String docrecId = entity.getDocrecId();
		// 插入当前操作人设置的下一步操作人
		if (!distribType.equals("2")) {
			String[] idStrings = distribId.split(",");
			for (String st : idStrings) {
				DocRecexamines saveEntity = new DocRecexamines();
				saveEntity.setDocrecId(docrecId);
				saveEntity.setCreateTime(new Date());
				saveEntity.setCreateUser(currentUser.getXm());
				saveEntity.setUserId(st);
				saveEntity.setState("0");
				saveEntity.setRecexamType(distribType);
				this.persist(saveEntity);
				
	        	SysUser user = userService.get(st);
	        	String regStatus="您好," + user.getXm() + "老师,有公文需要您尽快批阅!";
	        	pushService.pushInfo(user.getXm(), user.getUserNumb(), "公文批阅提醒", regStatus);				
			}
		}
		// 根据当前操作人操作后的情况更新公文的传阅状态
		String hql = "select count(*) from DocRecexamines where docrecId='" + docrecId
				+ "' and recexamType=0 and state=0";
		Integer count = this.getCount(hql);
		if (count == 0) {
			String hql2 = "update DocReceive set docrecState=3 where uuid='" + docrecId + "'";
			this.executeHql(hql2);
		} else {
			String hql2 = "update DocReceive set docrecState=2 where uuid='" + docrecId + "'";
			this.executeHql(hql2);
		}
		// TODO Auto-generated method stub
		return perEntity;
	}

	@Override
	public QueryResult<DocRecexamines> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);
		Boolean isSchoolAdminRole = false;
		List<SysUser> roleUsers = userService.getUserByRoleName("学校管理员");
		for (SysUser su : roleUsers) {
			if(su.getUuid().equals(currentUser.getUuid())){
				isSchoolAdminRole = true;
				break;
			}
		}	
		StringBuffer hql = new StringBuffer("from DocRecexamines o where 1=1 ");
		if (!isSchoolAdminRole){
			//如果当前人不是schooladmin角色,则只能显示自己的登记
			hql.append(" and userId='" + currentUser.getUuid() + "' ");
		}		
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
        
        QueryResult<DocRecexamines> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

}