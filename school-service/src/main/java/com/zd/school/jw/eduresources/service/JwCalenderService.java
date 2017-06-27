package com.zd.school.jw.eduresources.service;

import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwCalender ;
import com.zd.school.jw.eduresources.model.JwTGrade;


/**
 * 
 * ClassName: JwCalenderService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历信息(JW_T_CALENDER)实体Service接口类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface JwCalenderService extends BaseService<JwCalender> {
	
	/**
     * 
     * 根据JwCalender得到该班级所对应的校历
     * @param jtg
     * @return
     * @author huangzc
     */
	public JwCalender findJwTcanderByClaiId(JwTGrade  jtg);
	
	public int updateStatu(String uuid);
}