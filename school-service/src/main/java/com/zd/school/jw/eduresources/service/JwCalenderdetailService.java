package com.zd.school.jw.eduresources.service;

import java.util.List;

import com.zd.core.service.BaseService;
import com.zd.school.jw.eduresources.model.JwCalender;
import com.zd.school.jw.eduresources.model.JwCalenderdetail ;


/**
 * 
 * ClassName: JwCalenderdetailService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 校历节次信息表(JW_T_CALENDERDETAIL)实体Service接口类.
 * date: 2016-08-30
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface JwCalenderdetailService extends BaseService<JwCalenderdetail> {
	
	/**
     * 根据JwTCander对象找到校历详细列表
     * @param jtc
     * @return
     * @author huangzc
     */
	public List<JwCalenderdetail> queryJwTCanderdetailByJwTCander(JwCalender jtc);

}