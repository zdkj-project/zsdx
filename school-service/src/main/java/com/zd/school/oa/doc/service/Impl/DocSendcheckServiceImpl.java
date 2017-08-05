package com.zd.school.oa.doc.service.Impl;

import com.zd.core.constant.InfoPushWay;
import com.zd.core.constant.StringVeriable;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DateUtil;
import com.zd.school.oa.doc.dao.DocSendcheckDao;
import com.zd.school.oa.doc.model.AllUserDoc;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.oa.doc.service.DocSendcheckService;
import com.zd.school.oa.doc.service.DocSenddocService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.push.service.PushInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: DocSendcheckServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公文发文核稿人实体Service接口实现类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocSendcheckServiceImpl extends BaseServiceImpl<DocSendcheck> implements DocSendcheckService {

	@Resource
	public void setDocSendcheckDao(DocSendcheckDao dao) {
		this.dao = dao;
	}
	@Resource
	private SysUserService userService;
	
	@Resource
	private PushInfoService pushService;
	
	@Resource
	private DocSenddocService docSendService;
	
	@Override
	public Integer doBatchcheck(String opininon, String ids, String userCh) {
		try {
			StringBuffer hql = new StringBuffer("UPDATE DocSendcheck SET updateUser='");
			String doIds = "'" + ids.replace(",", "','") + "'";
			hql.append(userCh + "', updateTime=CONVERT(datetime,'");
			hql.append(DateUtil.formatDateTime(new Date()) + "'),state='1',opininon='");
			hql.append(opininon + "' WHERE uuid IN (" + doIds + ")");
			Integer isExecute = this.executeHql(hql.toString());

			// 判断这个核稿人是否是最后一个核稿人，是的话就设置发文的状态为2.
			String[] list = ids.split(",");
			for (int i = 0; i < list.length; i++) {
				DocSendcheck docSendcheck = this.get(list[i]);
				String sendId = docSendcheck.getSendId();
				String hql1 = "select count(*) from DocSendcheck where sendId='" + sendId + "' and state=0";
				Integer count = this.getCount(hql1);
				if (count == 0) {
					String hql2 = "update DocSenddoc set docsendState=2 where uuid='" + sendId + "'";
					this.executeHql(hql2);
				} else {
					String hql3 = "update DocSenddoc set docsendState=1 where uuid='" + sendId + "'";
					this.executeHql(hql3);
				}
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public List<AllUserDoc> getUserAllDoc(SysUser currentUser,String whereSql) {
		String userId = currentUser.getUuid();
		String sql = "SELECT recordID,docId,userId,docTitle,doResult,doState,createTime,docNumb,docEmg,operType,allOpininon,classification,wenzhong,keyword,createDate,doctypeName,progress FROM DOC_V_USERALLDOCS WHERE userId='"
				+ userId + "' " + whereSql + " ORDER BY operType,createTime DESC";
		List<AllUserDoc> lists = this.doQuerySqlObject(sql, AllUserDoc.class);

		return lists;
	}

	@Override
	public DocSendcheck isDoCheck(String distribId, String distribType, DocSendcheck entity, SysUser currentUser) throws IllegalAccessException, InvocationTargetException {
		String myState = "1"; //当前核稿人的意见
		if (distribType.equals("2"))
			myState = "2"; // 当前核稿人意见为不同意
		// 先拿到已持久化的实体
		DocSendcheck perEntity = this.get(entity.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
		perEntity.setState(myState);
		entity = this.merge(perEntity);// 执行修改方法

		String sendId = entity.getSendId();
		DocSenddoc doc = docSendService.get(sendId);
		String hql = "";
		String regStatus = "";
		SysUser user = null;
		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
		switch (distribType) {
		case "0":
			//当前核稿人同意并转下一核稿人
			String[] idStrings = distribId.split(",");
			for (String st : idStrings) {
				DocSendcheck saveEntity = new DocSendcheck();
				saveEntity.setSendId(sendId);
				saveEntity.setCreateTime(new Date());
				saveEntity.setCreateUser(currentUser.getXm());
				saveEntity.setUserId(st);
				saveEntity.setState("0");
				this.persist(saveEntity);
	        	user = userService.get(st);
	    		url.append("app/DocSendcheck/detailed?");
	    		url.append("id=" + saveEntity.getUuid());
	    		url.append("&personUserId=" + st);        	
	        	regStatus="您好," + user.getXm() + "老师,有公文需要您尽快处理!";
	        	pushService.pushInfo(user.getXm(), user.getUserNumb(), "公文核稿提醒", regStatus, InfoPushWay.WX.getCode(),url.toString());
			}
			//设置公文的状态为核稿中
			hql = "update DocSenddoc set docsendState=1 where uuid='" + sendId + "'";
			break;
		case "1":
			//当前核稿人同意并完成 核稿
			user = userService.get(doc.getDrafterId());
			//通知拟稿人
			regStatus="您好," + user.getXm() + "老师,您的公文已核稿通过!";
			pushService.pushInfo(user.getXm(), user.getUserNumb(), "公文核稿提醒", regStatus,InfoPushWay.WX.getCode());
			
			//通知盖章人
			List<SysUser> listSealUser = userService.getUserByRoleName("公文盖章管理员");
			for (SysUser sealUser : listSealUser) {
//	    		url.append("app/DocSendcheck/detailed?");
//	    		url.append("id=" + sendId);
//	    		url.append("&personUserId=" + sealUser.getUuid());      				
				regStatus="您好," + sealUser.getXm() + "老师,有公文需要您盖章，请尽快处理!";
				pushService.pushInfo(sealUser.getXm(), sealUser.getUserNumb(), "公文盖章提醒", regStatus,InfoPushWay.WX.getCode());
			}
			//设置公文状态 为盖章中
			hql = "update DocSenddoc set docsendState=2 where uuid='" + sendId + "'";
			break;
		case "2":
			//当前核稿人不同意，退回
			//通知拟稿人
			user = userService.get(doc.getDrafterId());
			regStatus="您好," + user.getXm() + "老师,您的公文核稿未通过!请重新修改";
			pushService.pushInfo(user.getXm(), user.getUserNumb(), "公文核稿提醒", regStatus,InfoPushWay.WX.getCode());
			
			//设置公文状态为核稿不通过
			hql = "update DocSenddoc set docsendState=3 where uuid='" + sendId + "'";
			break;
		}
		
		this.executeHql(hql);

		return entity;
	}

}