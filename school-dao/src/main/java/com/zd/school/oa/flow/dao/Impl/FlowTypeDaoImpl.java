/**
 * Project Name:jw-dao
 * File Name:FlowTypeDaoImpl.java
 * Package Name:com.zd.school.oa.dao.workflow.Impl
 * Date:2016年5月23日下午3:40:28
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.flow.dao.FlowTypeDao;
import com.zd.school.oa.flow.model.FlowType;

/**
 * ClassName:FlowTypeDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月23日 下午3:40:28
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Repository
public class FlowTypeDaoImpl extends BaseDaoImpl<FlowType> implements FlowTypeDao {

	public FlowTypeDaoImpl() {

		super(FlowType.class);
		// TODO Auto-generated constructor stub

	}

}
