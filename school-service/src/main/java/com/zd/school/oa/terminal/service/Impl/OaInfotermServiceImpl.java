package com.zd.school.oa.terminal.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.terminal.dao.OaInfotermDao;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.model.OaInfotermuse;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.oa.terminal.service.OaInfotermuseService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaInfotermServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 信息发布终端(OA_T_INFOTERM)实体Service接口实现类. date:
 * 2017-01-14
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaInfotermServiceImpl extends BaseServiceImpl<OaInfoterm> implements OaInfotermService {

	@Resource
	public void setOaInfotermDao(OaInfotermDao dao) {
		this.dao = dao;
	}

	@Resource
	private OaInfotermuseService useHistoryService;

	private static Logger logger = Logger.getLogger(OaInfotermServiceImpl.class);

	@Override
	public QueryResult<OaInfoterm> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
		QueryResult<OaInfoterm> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
		return qResult;
	}

	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
	 * @param currentUser
	 *            当前操作的用户
	 * @return 操作成功返回true，否则返回false
	 */
	@Override
	public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
		Boolean delResult = false;
		try {
			Object[] conditionValue = ids.split(",");
			String[] propertyName = { "isUse", "updateUser", "updateTime", "roomId", "roomName", "houseNumb" };
			Object[] propertyValue = { 0, currentUser.getXm(), new Date(), "", "", "" };
			this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			delResult = false;
		}
		return delResult;
	}

	/**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaInfoterm doUpdateEntity(OaInfoterm entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		OaInfoterm saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyProperties(saveEntity, entity);
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.merge(saveEntity);// 执行修改方法

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
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param beforeNumber
	 *            批量增加的起始终端号
	 * @param termCount
	 *            批量增加的终端 个数
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaInfoterm doAddEntity(OaInfoterm entity, SysUser currentUser, Integer beforeNumber, Integer termCount) {
		List<String> excludedProp = new ArrayList<>();
		excludedProp.add("uuid");
		OaInfoterm saveEntity = null;
		Integer newNumber = beforeNumber;
		try {
			for (int i = 0; i < termCount; i++) {
				saveEntity = new OaInfoterm();
				BeanUtils.copyProperties(saveEntity, entity, excludedProp);
				saveEntity.setIsUse(0);
				saveEntity.setOrderIndex(newNumber);
				saveEntity.setTermCode(StringUtils.addString(newNumber.toString(), "0", 6, "L"));
				saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名

				entity = this.merge(saveEntity);// 执行修改方法
				newNumber++;
			}

			return saveEntity;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		/*
		 * OaInfoterm saveEntity = new OaInfoterm(); try { List<String>
		 * excludedProp = new ArrayList<>();
		 * 
		 * BeanUtils.copyProperties(saveEntity, entity,excludedProp);
		 * saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名 entity =
		 * this.merge(saveEntity);// 执行修改方法
		 * 
		 * return entity; } catch (IllegalAccessException e) {
		 * logger.error(e.getMessage()); return null; } catch
		 * (InvocationTargetException e) { logger.error(e.getMessage()); return
		 * null; }
		 */
	}

	@Override
	public Boolean doSetTerminal(List<OaInfoterm> terminals, String roomId, String roomName, SysUser currentUser) {
		try {
			String[] propName = { "roomId", "houseNumb" };

			for (OaInfoterm oaInfoterm : terminals) {
				String houseNumb = oaInfoterm.getHouseNumb();
				String termId = oaInfoterm.getUuid();
				Object[] propValue = { roomId, houseNumb };
				OaInfoterm saveEntity = this.getByProerties(propName, propValue);
				if (ModelUtil.isNotNull(saveEntity)) {
					// 原来给此门牌分配过终端
					if (!saveEntity.getUuid().equals(termId)) {
						// 当前设置的和原来的不一致，需要清除原来的，再更新
						String conditionValue = saveEntity.getUuid();
						String[] propertyName = { "isUse", "updateUser", "updateTime", "roomId", "roomName",
								"houseNumb" };
						Object[] propertyValue = { 0, currentUser.getXm(), new Date(), "", "", "" };
						this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
						saveEntity = this.get(oaInfoterm.getUuid());
					}
				} else {
					// 原来没有分配，取当前设置的
					saveEntity = this.get(oaInfoterm.getUuid());
				}
				// OaInfoterm saveEntity = this.get(oaInfoterm.getUuid());
				saveEntity.setRoomId(roomId);
				saveEntity.setRoomName(roomName);
				saveEntity.setIsUse(1);
				saveEntity.setHouseNumb(houseNumb);
				saveEntity.setUpdateTime(new Date()); // 设置修改时间
				saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名

				this.merge(saveEntity);// 执行修改方法

				// 写入分配历史记录
				OaInfotermuse useHistory = new OaInfotermuse();
				useHistory.setTermId(oaInfoterm.getUuid());
				useHistory.setTermCode(oaInfoterm.getTermCode());
				useHistory.setRoomId(roomId);
				useHistory.setRoomName(roomName);
				useHistory.setCreateUser(currentUser.getXm());

				useHistoryService.persist(useHistory);
			}

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}