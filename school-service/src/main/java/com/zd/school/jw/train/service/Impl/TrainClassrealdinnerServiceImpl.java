package com.zd.school.jw.train.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClassrealdinner ;
import com.zd.school.jw.train.dao.TrainClassrealdinnerDao ;
import com.zd.school.jw.train.service.TrainClassrealdinnerService ;

/**
 * 
 * ClassName: TrainClassrealdinnerServiceImpl
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 班级就餐登记(TRAIN_T_CLASSREALDINNER)实体Service接口实现类.
 * date: 2017-06-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainClassrealdinnerServiceImpl extends BaseServiceImpl<TrainClassrealdinner> implements TrainClassrealdinnerService{

    @Resource
    public void setTrainClassrealdinnerDao(TrainClassrealdinnerDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(TrainClassrealdinnerServiceImpl.class);
	
	@Override
	public QueryResult<TrainClassrealdinner> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainClassrealdinner> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
	public TrainClassrealdinner doUpdateEntity(TrainClassrealdinner entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		TrainClassrealdinner saveEntity = this.get(entity.getUuid());
		try {
			BeanUtils.copyPropertiesExceptNull(saveEntity, entity);
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
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public TrainClassrealdinner doAddEntity(TrainClassrealdinner entity, SysUser currentUser) {
		TrainClassrealdinner saveEntity = new TrainClassrealdinner();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity,excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
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
	@Override
	public void createClassRecord(TrainClass trainClass,SysUser sysUser) {
		// TODO Auto-generated method stub
		//只生成一次，后期也不会删除和重新生成。
		String hql="SELECT COUNT(*) FROM TrainClassrealdinner where isDelete=0 and classId=?";
		Long count=this.getForValue(hql, trainClass.getUuid());
		if(count==0){
			Calendar calendar=Calendar.getInstance();
			Date beginDate=trainClass.getBeginDate();	
			Date endDate=trainClass.getEndDate();
			calendar.setTime(beginDate);
			
			Date currentDate=new Date();
			String userId=sysUser.getUuid();
			String classId=trainClass.getUuid();
			
			long diff = endDate.getTime() - beginDate.getTime();	//得到的差值 
		    long days = diff / (1000 * 60 * 60 * 24)+1;  
		    for(int i=0;i<days;i++ ){	  
		    	TrainClassrealdinner tc=new TrainClassrealdinner();
		    	tc.setClassId(classId);
		    	tc.setBreakfastReal(0);
		    	tc.setLunchReal(0);
		    	tc.setBreakfastStand(trainClass.getBreakfastStand());
		    	tc.setLunchStand(trainClass.getLunchStand());
		    	tc.setDinnerStand(trainClass.getDinnerStand());
		    	tc.setBreakfastStandReal(trainClass.getBreakfastStand());
		    	tc.setLunchStandReal(trainClass.getLunchStand());
		    	tc.setDinnerStandReal(trainClass.getDinnerStand());
		    	tc.setDinnerReal(0);
		    	tc.setDinnerDate(calendar.getTime());
		    	tc.setCreateTime(currentDate);
		    	tc.setCreateUser(userId);
		    	this.merge(tc);
		    	calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		    }
		}
		
	}
}