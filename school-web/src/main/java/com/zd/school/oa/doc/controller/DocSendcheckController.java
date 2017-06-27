
package com.zd.school.oa.doc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.oa.doc.model.AllUserDoc;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.oa.doc.service.DocSendcheckService;
import com.zd.school.oa.doc.service.DocSenddocService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
/**
 * 
 * ClassName: DocSendcheckController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公文发文核稿人实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/DocSendcheck")
public class DocSendcheckController extends FrameWorkController<DocSendcheck> implements Constant {

	@Resource
	private DocSendcheckService thisService; // service层接口

	@Resource
	private DocSenddocService sendService;
	
	@Resource
	private SysUserService userService;
    @Resource
    private PushInfoService pushService;
	/**
	 * 
	 * list:待核稿公文列表 .
	 * 
	 * @author luoyibo
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute DocSendcheck entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String userId = "";
		SysUser currentUser = getCurrentSysUser();

		if (currentUser != null)
			userId = currentUser.getUuid();
		String strData = ""; // 返回给js的数据
		String sort = request.getParameter("sort");
		String sortSql = "";
		if (sort.length() > 0)
			sortSql = StringUtils.convertSortToSql(sort);
		Boolean isSchoolAdminRole = false;
		List<SysUser> roleUsers = userService.getUserByRoleName("学校管理员");
		for (SysUser su : roleUsers) {
			if(su.getUuid().equals(currentUser.getUuid())){
				isSchoolAdminRole = true;
				break;
			}
		}		
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " o where 1=1 ");
		// 总记录数
		StringBuffer countHql = new StringBuffer(
				"select count(*) from " + entity.getClass().getSimpleName() + " where   1=1");
		if (!isSchoolAdminRole){
			//如果当前人不是schooladmin角色,则只能显示自己的登记
			hql.append(" and userId='" + currentUser.getUuid() + "' ");
			countHql.append(" and userId='" + currentUser.getUuid() + "' ");
		}
		// if (StringUtils.isNotEmpty(userId)) {
		// hql.append(" and userId='" + userId + "' ");
		// countHql.append(" and userId='" + userId + "' ");
		// }
		String whereSql = entity.getWhereSql();// 查询条件
		String parentSql = entity.getParentSql();// 条件
		String querySql = entity.getQuerySql();// 查询条件
		String orderSql = entity.getOrderSql();// 排序

		int start = super.start(request); // 起始记录数
		int limit = entity.getLimit();// 每页记录数
		hql.append(whereSql);
		hql.append(parentSql);
		hql.append(querySql);
		if (orderSql.length() > 0) {
			if (sortSql.length() > 0)
				hql.append(orderSql + " , " + sortSql);
		} else {
			if (sortSql.length() > 0)
				hql.append(" order by  " + sortSql);
		}

		countHql.append(whereSql);
		countHql.append(querySql);
		countHql.append(parentSql);
		List<DocSendcheck> lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		List<DocSendcheck> newLists = new ArrayList<DocSendcheck>();
		for (DocSendcheck dd : lists) {
			String uuid = dd.getUuid();
			DocSenddoc send = sendService.get(dd.getSendId());
			BeanUtils.copyPropertiesExceptNull(dd, send);
			dd.setUuid(uuid);
			newLists.add(dd);
		}
		strData = jsonBuilder.buildObjListToJson(new Long(count), newLists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * doRestore:批量核稿.
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/batchcheck")
	public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		String opininon = request.getParameter("opininon");
		String userCh = "超级管理员";

		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要批量核稿的公文'"));
			return;
		} else {
			// String hql = "UPATE DocSendcheck SET modifyUser='"
			Integer isExecute = thisService.doBatchcheck(opininon, delIds, userCh);

			if (isExecute > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'批量核稿成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'批量核稿失败'"));
			}
		}
	}

	/**
	 * 
	 * doUpdates:设置单个核稿完毕.
	 *
	 * @author luoyibo
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping("/ischeck")
	public void doIsCheck(DocSendcheck entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String distribType = request.getParameter("distribType");
		String distribId = request.getParameter("distribId");
		String progress=request.getParameter("progress");
		entity.setProgress(Integer.parseInt(progress));
		SysUser currentUser = getCurrentSysUser();
		DocSendcheck perEntity = thisService.isDoCheck(distribId, distribType, entity, currentUser);
//		String myState = "1"; // 当前人的核稿意见
//		if (distribType.equals("2"))
//			myState = "2"; // 我的意见为不同意
//		// 获取当前的操作用户
//		String userCh = "超级管理员";
//		SysUser currentUser = getCurrentSysUser();
//		if (currentUser != null)
//			userCh = currentUser.getXm();
//
//		// 先拿到已持久化的实体
//		// entity.getSchoolId()要自己修改成对应的获取主键的方法
//		DocSendcheck perEntity = thisService.get(entity.getUuid());
//
//		// 将entity中不为空的字段动态加入到perEntity中去。
//		BeanUtils.copyPropertiesExceptNull(perEntity, entity);
//
//		perEntity.setUpdateTime(new Date()); // 设置修改时间
//		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
//		perEntity.setState(myState);
//		entity = thisService.merge(perEntity);// 执行修改方法
//
//		String sendId = entity.getSendId();
//		// 插入当前操作人设置的下一步操作人
//		if (distribType.equals("0")) {
//			String[] idStrings = distribId.split(",");
//			for (String st : idStrings) {
//				DocSendcheck saveEntity = new DocSendcheck();
//				saveEntity.setSendId(sendId);
//				saveEntity.setCreateTime(new Date());
//				saveEntity.setCreateUser(currentUser.getXm());
//				saveEntity.setUserId(st);
//				saveEntity.setState("0");
//				thisService.persist(saveEntity);
//	        	SysUser user = userService.get(st);
//	        	String regStatus="您好," + user.getXm() + "老师,有公文需要您尽快核稿!";
//	        	//MessageUtil mag = new MessageUtil();
//	        	pushService.pushInfo(user.getXm(), user.getUserNumb(), "公文核稿提醒", regStatus);		
//			}
//		}
//
//		// 判断这个核稿人是否是最后一个核稿人，是的话就设置发文的状态为2.
//		String hql = "select count(*) from DocSendcheck where sendId='" + sendId + "' and state=0";
//		Integer count = thisService.getCount(hql);
//		String hql2 = "";
//		
//		if (count == 0) {
//			// 当前没有核稿人了
//			if (distribType.equals("1")) {
//				//核稿 人的意见是同意则设置状态为2-盖章中
//				hql2 = "update DocSenddoc set docsendState=2 where uuid='" + sendId + "'";
//			} else 
//				//核稿不通过
//				hql2 = "update DocSenddoc set docsendState=3 where uuid='" + sendId + "'";
//				//PushInfoUtil.pushInfo(empName, empNo, eventType, regStatus)
//			thisService.executeHql(hql2);
//		} else {
//			String hql3 = "update DocSenddoc set docsendState=1 where uuid='" + sendId + "'";
//			thisService.executeHql(hql3);
//		}
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
	}

	@RequestMapping(value = { "/useralldoclist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getUserAlldocList(@ModelAttribute DocSendcheck entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
		String strData = "";
		SysUser currentUser = getCurrentSysUser();
		String whereSql = request.getParameter("whereSql");

		List<AllUserDoc> lists = thisService.getUserAllDoc(currentUser, whereSql);// 执行查询方法

		strData = jsonBuilder.buildObjListToJson(new Long(lists.size()), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}
}
