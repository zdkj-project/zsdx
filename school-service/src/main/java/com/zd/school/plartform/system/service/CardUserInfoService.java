package com.zd.school.plartform.system.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.cashier.model.CashDinneritem;
import com.zd.school.plartform.system.model.CardUserInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * ClassName: BaseTRoleService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Service接口类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface CardUserInfoService extends BaseService<CardUserInfo> {

	public QueryResult<CardUserInfo> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete);

}