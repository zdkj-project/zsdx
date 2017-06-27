/**
 * Project Name:jw-dao
 * File Name:BaseApplayDaoImpl.java
 * Package Name:com.zd.school.oa.dao.applay.Impl
 * Date:2016年4月20日上午10:30:18
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.core.model.BaseApplayEntity;
import com.zd.school.oa.flow.dao.BaseApplayDao;

/**
 * ClassName:BaseApplayDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年4月20日 上午10:30:18
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */

@Repository
public class BaseApplayDaoImpl extends BaseDaoImpl<BaseApplayEntity> implements BaseApplayDao {

	public BaseApplayDaoImpl() {

		super(BaseApplayEntity.class);
		// TODO Auto-generated constructor stub

	}

}
