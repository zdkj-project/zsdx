/**
 * Project Name:jw-service
 * File Name:FlowTypeService.java
 * Package Name:com.zd.school.oa.service.flow
 * Date:2016年5月24日上午9:04:11
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.service;

import java.util.List;

import com.zd.core.service.BaseService;
import com.zd.school.oa.flow.model.FlowType;
import com.zd.school.oa.flow.model.FlowTypeTree;

/**
 * ClassName:FlowTypeService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月24日 上午9:04:11
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public interface FlowTypeService extends BaseService<FlowType> {

	public List<FlowTypeTree> listTree(String whereSql, String orderSql, String parentSql, String querySql);

}
