package com.zd.school.jw.eduresources.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.eduresources.model.JwCalender;
import com.zd.school.jw.eduresources.model.JwCalenderdetail ;
import com.zd.school.jw.eduresources.dao.JwCalenderdetailDao ;
import com.zd.school.jw.eduresources.service.JwCalenderdetailService ;

/**
 * 
 * ClassName: JwCalenderdetailServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历节次信息表(JW_T_CALENDERDETAIL)实体Service接口实现类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwCalenderdetailServiceImpl extends BaseServiceImpl<JwCalenderdetail> implements JwCalenderdetailService{

    @Resource
    public void setJwCalenderdetailDao(JwCalenderdetailDao dao) {
        this.dao = dao;
    }

	@Override
	public List<JwCalenderdetail> queryJwTCanderdetailByJwTCander(JwCalender jtc) {
		if(jtc == null)
    		return null;
    	if(jtc.getUuid() == null || jtc.getUuid().trim().equals(""))
    		return null;
    	StringBuffer hql = new StringBuffer(" from JwCalenderdetail where canderId='");
    	hql.append(jtc.getUuid()).append("'");
    	return this.dao.doQuery(hql.toString());
	}

}