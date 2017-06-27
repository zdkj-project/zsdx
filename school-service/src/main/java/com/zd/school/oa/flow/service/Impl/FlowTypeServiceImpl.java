/**
 * Project Name:jw-service
 * File Name:FlowTypeServiceImpl.java
 * Package Name:com.zd.school.oa.service.flow.Impl
 * Date:2016年5月24日上午9:04:31
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.service.Impl;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.oa.flow.dao.FlowTypeDao;
import com.zd.school.oa.flow.model.FlowType;
import com.zd.school.oa.flow.model.FlowTypeTree;
import com.zd.school.oa.flow.service.FlowTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:FlowTypeServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年5月24日 上午9:04:31
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional
public class FlowTypeServiceImpl extends BaseServiceImpl<FlowType> implements FlowTypeService {

	@Resource
	public void setFlowTypeDao(FlowTypeDao dao) {
		this.dao = dao;
	}

	public List<FlowTypeTree> listTree(String whereSql, String orderSql, String parentSql, String querySql) {

		StringBuffer hql = new StringBuffer("from FlowType where 1=1");
		hql.append(whereSql);
		hql.append(parentSql);
		hql.append(querySql);
		hql.append(orderSql);

		// 总记录数
		StringBuffer countHql = new StringBuffer("select count(*) from FlowType where  1=1");
		countHql.append(whereSql);
		countHql.append(querySql);
		countHql.append(parentSql);

		List<FlowType> typeList = super.doQuery(hql.toString());
		List<FlowTypeTree> result = new ArrayList<FlowTypeTree>();
		// 构建Tree数据
		recursion(new FlowTypeTree("0", new ArrayList<FlowTypeTree>()), result, typeList);

		return result;
	}

	private void recursion(FlowTypeTree parentNode, List<FlowTypeTree> result, List<FlowType> list) {
		List<FlowType> childs = new ArrayList<FlowType>();
		for (FlowType FlowType : list) {
			if (FlowType.getParentNode().equals(parentNode.getId())) {
				childs.add(FlowType);
			}
		}
		for (FlowType FlowType : childs) {
			FlowTypeTree child = new FlowTypeTree(FlowType.getUuid(), FlowType.getNodeText(), "", FlowType.getLeaf(),
					FlowType.getNodeLevel(), FlowType.getTreeIds(), FlowType.getParentNode(),FlowType.getOrderIndex(),new ArrayList<FlowTypeTree>(),
					FlowType.getFlowKey(), FlowType.getRemark(), FlowType.getNodeCode());
			if (FlowType.getParentNode().equals("0")) {
				result.add(child);
			} else {
				List<FlowTypeTree> trees = parentNode.getChildren();
				trees.add(child);
				parentNode.setChildren(trees);
			}
			recursion(child, result, list);
		}

	}
}
