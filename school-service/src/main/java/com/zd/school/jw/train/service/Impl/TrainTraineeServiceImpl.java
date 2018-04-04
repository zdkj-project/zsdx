package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.Base64Util;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.dao.TrainTraineeDao;
import com.zd.school.jw.train.model.TrainTrainee;
import com.zd.school.jw.train.service.TrainTraineeService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * ClassName: TrainTraineeServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 学员信息(TRAIN_T_TRAINEE)实体Service接口实现类. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainTraineeServiceImpl extends BaseServiceImpl<TrainTrainee> implements TrainTraineeService {

	@Resource
	public void setTrainTraineeDao(TrainTraineeDao dao) {
		this.dao = dao;
	}

	private static Logger logger = Logger.getLogger(TrainTraineeServiceImpl.class);

	@Resource
	private BaseDicitemService dicitemService;

	@Override
	public QueryResult<TrainTrainee> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
		QueryResult<TrainTrainee> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
			String[] propertyName = { "isDelete", "updateUser", "updateTime" };
			Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
			this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
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
	public TrainTrainee doUpdateEntity(TrainTrainee entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		TrainTrainee saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyProperties(saveEntity, entity);
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(saveEntity);// 执行修改方法

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
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public TrainTrainee doAddEntity(TrainTrainee entity, SysUser currentUser) {
		TrainTrainee saveEntity = new TrainTrainee();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity, excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(saveEntity);// 执行修改方法

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
	public void doImportTrainee(List<List<Object>> listTrainee, SysUser currentUser) {
		// 要转换的数据字典
		String mapKey = null;
		String[] propValue = { "HEADSHIPLEVEL", "XBM", "TRAINEECATEGORY", "XWM", "XLM", "ZZMMM", "MZM" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemName() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemCode());
		}
		/**
		 * 姓名 性别 移动电话 身份证件号 所在单位 职务 行政级别 学员类型 学号 民族 政治面貌 学历 学位 专业 毕业学校 电子邮件 通讯地址
		 * 党校培训证书号 行院培训证书号 照片
		 *
		 */
		TrainTrainee trainee = null;
		// <editor-fold desc="循环处理导入的学员名单">
		for (int i = 0; i < listTrainee.size(); i++) {
			List<Object> lo = listTrainee.get(i);

			// 查询学员库是否存在此学员，如果存在，则直接修改，不添加
			trainee = this.getByProerties(new String[] { "sfzjh", "isDelete" }, new Object[] { lo.get(3), 0 });

			if (trainee == null) {
				trainee = new TrainTrainee();
				trainee.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
			}
			trainee.setXm(String.valueOf(lo.get(0)));
			trainee.setXbm(mapDicItem.get(lo.get(1) + "XBM"));
			//trainee.setMobilePhone(String.valueOf(lo.get(2)));
			//trainee.setSfzjh(String.valueOf(lo.get(3)));
			trainee.setMobilePhone(Base64Util.encodeData(String.valueOf(lo.get(2))));
			trainee.setSfzjh(Base64Util.encodeData(String.valueOf(lo.get(3))));
			
			trainee.setWorkUnit(String.valueOf(lo.get(4)));
			trainee.setPosition(String.valueOf(lo.get(5)));
			if (lo.get(6) != null)
				trainee.setHeadshipLevel(mapDicItem.get(lo.get(6) + "HEADSHIPLEVEL"));
			if (lo.get(7) != null)
				trainee.setTraineeCategory(mapDicItem.get(lo.get(7) + "TRAINEECATEGORY"));

			trainee.setTraineeNumber(String.valueOf(lo.get(8)));

			if (lo.get(9) != null)
				trainee.setMzm(mapDicItem.get(lo.get(9) + "MZM"));
			if (lo.get(10) != null)
				trainee.setZzmmm(mapDicItem.get(lo.get(10) + "ZZMMM"));
			if (lo.get(11) != null)
				trainee.setXlm(mapDicItem.get(lo.get(11) + "XLM"));
			if (lo.get(12) != null)
				trainee.setXwm(mapDicItem.get(lo.get(12) + "XWM"));

			trainee.setZym(String.valueOf(lo.get(13)));
			trainee.setGraduateSchool(String.valueOf(lo.get(14)));
			trainee.setDzxx(String.valueOf(lo.get(15)));
			trainee.setAddress(String.valueOf(lo.get(16)));
			trainee.setPartySchoolNumb(String.valueOf(lo.get(17)));
			trainee.setNationalSchoolNumb(String.valueOf(lo.get(18)));

			// if (lo.get(19) != null)
			// trainee.setZp("/static/upload/traineePhoto/" +
			// String.valueOf(lo.get(19)) + ".jpg");

			trainee.setUpdateTime(new Date());
			trainee.setUpdateUser(currentUser.getXm());
			this.doMerge(trainee);
		}
		// </editor-fold>
	}

	@Override
	public List<TrainTrainee> listExport(String ids, String orderSql) {
		String hql = " from TrainTrainee where isDelete=0 ";
		if (StringUtils.isNotEmpty(ids)) {
			hql += " and uuid in ('" + ids.replace(",", "','") + "')";
		}
		if ("".equals(orderSql))
			hql += " order by traineeCategory,sfzjh";
		else
			hql += orderSql;
		List<TrainTrainee> list = this.getQuery(hql);
		List<TrainTrainee> exportList = new ArrayList<>();

		// 导出时要转换字典项
		String mapKey = null;
		String[] propValue = { "HEADSHIPLEVEL", "XBM", "TRAINEECATEGORY", "XWM", "XLM", "ZZMMM", "MZM" };
		Map<String, String> mapDicItem = new HashMap<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
			mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
			mapDicItem.put(mapKey, baseDicitem.getItemName());
		}
		for (TrainTrainee trainee : list) {
			trainee.setHeadshipLevelName(mapDicItem.get(trainee.getHeadshipLevel() + "HEADSHIPLEVEL"));
			trainee.setXbmName(mapDicItem.get(trainee.getXbm() + "XBM"));
			trainee.setTraineeCategoryName(mapDicItem.get(trainee.getTraineeCategory() + "TRAINEECATEGORY"));
			trainee.setXwmName(mapDicItem.get(trainee.getXwm() + "XWM"));
			trainee.setXlmName(mapDicItem.get(trainee.getXlm() + "XLM"));
			trainee.setZzmmmName(mapDicItem.get(trainee.getZzmmm() + "ZZMMM"));
			trainee.setMzmName(mapDicItem.get(trainee.getMzm() + "MZM"));
		}
		exportList.addAll(list);

		return exportList;
	}
}