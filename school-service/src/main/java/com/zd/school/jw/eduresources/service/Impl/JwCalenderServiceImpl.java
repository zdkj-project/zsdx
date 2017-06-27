package com.zd.school.jw.eduresources.service.Impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.jw.eduresources.model.JwCalender ;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.dao.JwCalenderDao ;
import com.zd.school.jw.eduresources.service.JwCalenderService ;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwCalenderServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体Service接口实现类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwCalenderServiceImpl extends BaseServiceImpl<JwCalender> implements JwCalenderService{

    @Resource
    public void setJwCalenderDao(JwCalenderDao dao) {
        this.dao = dao;
    }

	@Override
	public JwCalender  findJwTcanderByClaiId(JwTGrade  jtg) {
		if(jtg == null)
    		return null;
    	if(jtg.getSectionCode() == null || jtg.getSectionCode().trim().equals(""))
    		return null;
    	 return this.dao.getByProerties("sectionCode", jtg.getSectionCode());
	}

	@Override
	public int updateStatu(String uuid) {
		// TODO Auto-generated method stub
		try{
			String hql1="update JwCalender set activityState=0 ";
			String hql2="update JwCalender set activityState=1 where uuid='"+uuid+"'";
			this.executeHql(hql1);
			this.executeHql(hql2);
			return 1;
		}catch(Exception e){
			return 0;
		}		     
	}

}