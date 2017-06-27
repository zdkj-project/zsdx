package com.zd.school.oa.doc.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.zd.core.service.BaseService;
import com.zd.school.oa.doc.model.AllUserDoc;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: DocSendcheckService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公文发文核稿人实体Service接口类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface DocSendcheckService extends BaseService<DocSendcheck> {
	public Integer doBatchcheck(String opininon, String userIds, String operName);

	public List<AllUserDoc> getUserAllDoc(SysUser currentUser, String whereSql);
	
	public DocSendcheck isDoCheck(String distribId,String distribType,DocSendcheck entity,SysUser currentUser) throws IllegalAccessException, InvocationTargetException;

}