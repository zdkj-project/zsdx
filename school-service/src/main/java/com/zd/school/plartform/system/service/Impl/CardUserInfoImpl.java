package com.zd.school.plartform.system.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.system.dao.CardUserInfoDao;
import com.zd.school.plartform.system.model.CardUserInfo;
import com.zd.school.plartform.system.service.CardUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
public class CardUserInfoImpl extends BaseServiceImpl<CardUserInfo> implements CardUserInfoService {

    @Resource
    public void setBaseTRoleDao(CardUserInfoDao dao) {
        this.dao = dao;
    }
    
    @Override
	public QueryResult<CardUserInfo> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<CardUserInfo> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
		return qResult;
	}
    
}