package com.zd.school.oa.notice.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.notice.model.OaNoticeright;
import com.zd.school.plartform.system.model.SysUser;


 
public interface OaNoticerightService extends BaseService<OaNoticeright> {
	
	public QueryResult<OaNoticeright> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser); 
	
	/**
	 * 获取审核用户
	 * @param submitUser 提交的用户
	 * @return 审核用户
	 */
	public SysUser getApproveUser(SysUser submitUser);

}