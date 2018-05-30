package com.zd.school.control.device.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.core.util.TLVUtils;
import com.zd.school.control.device.dao.PtGatewayDao;
import com.zd.school.control.device.model.PtGateway;
import com.zd.school.control.device.model.TLVModel;
import com.zd.school.control.device.service.PtGatewayService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 网关表
 * @author hucy
 *
 */
@Service
@Transactional
public class PtGatewayServiceImpl extends BaseServiceImpl<PtGateway> implements PtGatewayService{

	@Resource
	public void setBaseGatewayDao(PtGatewayDao dao) {
		this.dao = dao;
	}

	private static Logger logger = Logger.getLogger(PtGatewayServiceImpl.class);

	@Override
	public PtGateway doUpdateEntity(PtGateway entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		PtGateway perEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyPropertiesExceptNull(perEntity, entity);
			perEntity.setUpdateTime(new Date()); // 设置修改时间
			perEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(perEntity);// 执行修改方法

			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public PtGateway doAddEntity(PtGateway entity, SysUser currentUser) {
		try {
			Integer orderIndex = this.getDefaultOrderIndex(entity);
			PtGateway perEntity = new PtGateway();
			perEntity.setCreateUser(currentUser.getXm());
			perEntity.setOrderIndex(orderIndex);
			// perEntity.setPriceValue(entity.getPriceValue());
			// perEntity.setPriceStatus(entity.getPriceStatus());
			BeanUtils.copyPropertiesExceptNull(entity, perEntity);
			// 持久化到数据库
			entity = this.doMerge(entity);
			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	/**
	 * 设置网关参数 若设置失败，自动进入异常捕获返回出错信息。
	 */
	@Override
	public void doSetGatewayParam(HttpServletRequest request, TLVModel tlvs, String userCh) {
		// TODO Auto-generated method stub
		byte[] result = null;
		PtGateway perEntity = this.get(tlvs.getUuid());
		result = TLVUtils.encode(tlvs.getTlvs());
		perEntity.setNetParam(result);
		perEntity.setGatewayIP(request.getParameter("gatewayIP"));
		perEntity.setNetgatewayIp(request.getParameter("netGatewayIp"));
		perEntity.setGatewayMask(request.getParameter("gatewayMask"));
		perEntity.setGatewayMac(request.getParameter("gatewayMac"));
		
		//不能更改服务器参数
		//perEntity.setFrontServerIP(request.getParameter("frontServerIP"));
		//perEntity.setFrontServerPort(Integer.parseInt(request.getParameter("frontServerPort")));
		//perEntity.setFrontServerStatus(Integer.parseInt(request.getParameter("frontServerStatus")));

		perEntity.setUpdateUser(userCh);
		perEntity.setUpdateTime(new Date());
		this.doMerge(perEntity);
	}
	/**
	 * 处理单个网关数据
	 */
	@Override
	public void doUpdateBaseHighParam(TLVModel tlvs, String xm) {
		// TODO Auto-generated method stub
		
		byte[] baseResult = null;
		baseResult = TLVUtils.encode(tlvs.getTlvs().subList(0, 2));
		byte[] advResult = null;
		advResult = TLVUtils.encode(tlvs.getTlvs().subList(2, 3));

		PtGateway perEntity = this.get(tlvs.getUuid());

		// 将entity中不为空的字段动态加入到perEntity中去。
		perEntity.setUpdateUser(xm);
		perEntity.setUpdateTime(new Date());
		perEntity.setBaseParam(baseResult);
		perEntity.setAdvParam(advResult);
		this.doMerge(perEntity);// 执行修改方法
	}
	/**
	 * 批量处理勾选的网关列表
	 */
	@Override
	public void doUpdateBaseHighParamToIds(TLVModel tlvs, String gatewayIds, String xm) {
		if(StringUtils.isEmpty(gatewayIds)){
			gatewayIds=tlvs.getUuid();
		}else if(!gatewayIds.contains(tlvs.getUuid())){
			gatewayIds=tlvs.getUuid()+","+gatewayIds;
		}
		
		byte[] baseResult =TLVUtils.encode(tlvs.getTlvs().subList(0, 2));
		byte[] advResult =TLVUtils.encode(tlvs.getTlvs().subList(2, 3));
		
		String hql="update PtGateway a set a.baseParam = ?,a.advParam=?,a.updateTime=?,a.updateUser=? where a.uuid in ('"+gatewayIds.replace(",", "','")+"')";
		Query query = this.getSession().createQuery(hql);
		query.setBinary(0, baseResult);
		query.setBinary(1, advResult);
		query.setDate(2, new Date());
		query.setString(3, xm);
		query.executeUpdate();
		
//		for(int ){
//		// TODO Auto-generated method stub
//			PtGateway perEntity = this.get(tlvs.getUuid());
//			
//			// 将entity中不为空的字段动态加入到perEntity中去。
//			perEntity.setUpdateUser(xm);
//			perEntity.setUpdateTime(new Date());
//				
//				
//			perEntity.setBaseParam(baseResult);
//			perEntity.setAdvParam(advResult);
//			this.merge(perEntity);// 执行修改方法
//		}
		
	}


	/**
	 * 批量处理当前服务器下所有网关数据
	 */
	@Override
	public void doUpdateBaseHighParamToAll(TLVModel tlvs, String xm) {
		// TODO Auto-generated method stub
		PtGateway perEntity = this.get(tlvs.getUuid());
		String frontServerId=perEntity.getFrontserverId();
		
		byte[] baseResult =TLVUtils.encode(tlvs.getTlvs().subList(0, 2));
		byte[] advResult =TLVUtils.encode(tlvs.getTlvs().subList(2, 3));
		
		String hql="update PtGateway a set a.baseParam = ?,a.advParam=?,a.updateTime=?,a.updateUser=? where a.isDelete=0 and a.frontserverId=?";
		Query query = this.getSession().createQuery(hql);
		query.setBinary(0, baseResult);
		query.setBinary(1, advResult);
		query.setDate(2, new Date());
		query.setString(3, xm);
		query.setString(4, frontServerId);
		query.executeUpdate();
	}
	
	/**
	 * 批量设置前置服务器
	 */
	@Override
	public void doUpdateBatchFront(PtGateway entity, String xm) {
		// TODO Auto-generated method stub
		String uuids[] =entity.getUuid().split(",");
		
		String hql="update PtGateway a set a.frontserverId = ?,a.updateTime=?,a.updateUser=? where a.uuid in (:ids)";
		Query query = this.getSession().createQuery(hql);
		query.setString(0, entity.getFrontserverId());
		query.setDate(1, new Date());
		query.setString(2, xm);
		query.setParameterList("ids", uuids);
		query.executeUpdate();
		
//		PtGateway ptGateway=null;
//		for (int i = 0; i < uuids.length; i++) {
//			ptGateway=this.get(uuid[i]);
//			ptGateway.setFrontserverId(entity.getFrontserverId());
//			ptGateway.setUpdateUser(xm);
//			ptGateway.setUpdateTime(new Date());
//			this.merge(ptGateway);
//		}
	}
}
