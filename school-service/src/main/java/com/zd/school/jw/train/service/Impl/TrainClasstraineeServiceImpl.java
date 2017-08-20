package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.dao.TrainClasstraineeDao;
import com.zd.school.jw.train.model.TrainClass;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.model.TrainTrainee;
import com.zd.school.jw.train.model.vo.VoTrainClassCheck;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.jw.train.service.TrainTraineeService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * ClassName: TrainClasstraineeServiceImpl
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 班级学员信息(TRAIN_T_CLASSTRAINEE)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainClasstraineeServiceImpl extends BaseServiceImpl<TrainClasstrainee> implements TrainClasstraineeService{

	private static Object syncObject = new Object();//同步锁
	
    @Resource
    public void setTrainClasstraineeDao(TrainClasstraineeDao dao) {
        this.dao = dao;
    }
    @Resource
	private TrainClassService trainClassService;
    
    @Resource
	TrainTraineeService trainTraineeServie;

	@Resource
	BaseDicitemService dicitemService;
	
	private static Logger logger = Logger.getLogger(TrainClasstraineeServiceImpl.class);
	
	@Override
	public QueryResult<TrainClasstrainee> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainClasstrainee> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
	public Boolean doLogicDeleteByIds(String classId,String ids, SysUser currentUser) {
		Boolean delResult = false;
		try {
			Object[] conditionValue = ids.split(",");
			String[] propertyName = { "isDelete", "updateUser", "updateTime" };
			Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
			this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;
			
			//设置班级的状态
			TrainClass trainClass = trainClassService.get(classId);
			Integer isuse=trainClass.getIsuse();
			if(isuse!=null&&isuse!=0){	//当班级已经提交过一次之后，每次修改都设置为2
				trainClass.setIsuse(2);
				trainClass.setUpdateTime(new Date()); // 设置修改时间
				trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
				trainClassService.update(trainClass);
			}	
			
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
	public TrainClasstrainee doUpdateEntity(TrainClasstrainee entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		TrainClasstrainee saveEntity = this.get(entity.getUuid());
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
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public TrainClasstrainee doAddEntity(TrainClasstrainee entity, SysUser currentUser) {
		TrainClasstrainee saveEntity = new TrainClasstrainee();
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
	public int doUpdateRoomInfo(String roomId, String roomName, String ids,String xbm, SysUser currentUser) {
		
		int result=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int traineeLength=ids.split(",").length;
		try {		
			synchronized (syncObject) {
				//判断性别是否一致
				String hqlSelect1="select count(*) from TrainClasstrainee where roomId='"+roomId+"' and xbm!='"+xbm+"'";
				if(this.getCount(hqlSelect1)>0){
					return -2;	//性别不一致
				}
							
				//判断人数是否符合要求，若大于最大人数，则不允许设置
				String hqlSelect2="select count(*) from TrainClasstrainee where roomId='"+roomId+"' and uuid not in ('" + ids.replace(",", "','") + "')";
							
				if(this.getCount(hqlSelect2)+traineeLength<=3){
					String hqlUpdate = "update TrainClasstrainee t set t.roomId='"+roomId+"',t.roomName='"+roomName+"',"
							+ "	t.updateUser='" + currentUser.getXm()+ "',t.updateTime='" +  sdf.format(new Date()) + "' "
							+ "where t.isDelete!=1 and t.uuid in ('" + ids.replace(",", "','") + "')";
					this.executeHql(hqlUpdate);
					result=1;
				}																							
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception	
			logger.error(e.getMessage());
			result=-1;
		}
		
		return result;	
	}
	@Override
	public int doCancelRoomInfo(String ids, SysUser currentUser) {
		// TODO Auto-generated method stub
		int result=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		try {		
					
			String hqlUpdate = "update TrainClasstrainee t set t.roomId=NULL,t.roomName=NULL,"
					+ "	t.updateUser='" + currentUser.getXm()+ "',t.updateTime='" +  sdf.format(new Date()) + "' "
					+ "where t.uuid in ('" + ids.replace(",", "','") + "')";
			this.executeHql(hqlUpdate);
			result=1;
			
		} catch (Exception e) {
			// TODO: handle exception	
			logger.error(e.getMessage());
			result=-1;
		}
		
		return result;	
	}
	@Override
	public void doImportTrainee(List<List<Object>> listObject, String classId, String needSync,SysUser currentUser) {
		// TODO Auto-generated method stub
	
		// XBM;HEADSHIPLEVEL 必填项
		// TRAINEECATEGORY;XWM;XLM;ZZMMM;MZM 同步时需要的项
		Map<String, String> mapHeadshipLevel = new HashMap<>();
		Map<String, String> mapXbm = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.doQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			if (baseDicitem.getDicCode().equals("XBM"))
				mapXbm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
			else
				mapHeadshipLevel.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
		}

		// 若值为1，则要加载这些字典项。
		Map<String, String> mapTraineeCategory = new HashMap<>();
		Map<String, String> mapXwm = new HashMap<>();
		Map<String, String> mapXlm = new HashMap<>();
		Map<String, String> mapZzmm = new HashMap<>();
		Map<String, String> mapMzm = new HashMap<>();
		if (needSync.equals("1")) {
			String hql2 = " from BaseDicitem where dicCode in ('TRAINEECATEGORY','XWM','XLM','ZZMMM','MZM')";
			List<BaseDicitem> listBaseDicItems2 = dicitemService.doQuery(hql2);
			for (BaseDicitem baseDicitem : listBaseDicItems2) {
				switch (baseDicitem.getDicCode()) {
				case "TRAINEECATEGORY":
					mapTraineeCategory.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
					break;
				case "XWM":
					mapXwm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
					break;
				case "XLM":
					mapXlm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
					break;
				case "ZZMMM":
					mapZzmm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
					break;
				case "MZM":
					mapMzm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
					break;
				}
			}
		}
		
		//设置班级的状态
		Integer isDelete=0;	//当班级提交之后再导入学员，则设置学员isdelete值为2
		TrainClass trainClass = trainClassService.get(classId);
		Integer isuse=trainClass.getIsuse();
		if(isuse!=null&&isuse!=0){	//当班级已经提交过一次之后，每次修改都设置为2
			isDelete=2;
			trainClass.setIsuse(2);
			trainClass.setUpdateTime(new Date()); // 设置修改时间
			trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			trainClassService.update(trainClass);
		}
				
		/**
		 * 姓名 性别 移动电话 身份证件号 所在单位 职务 行政级别 学员类型 民族 政治面貌 学历 学位 专业 毕业学校 电子邮件
		 * 通讯地址 党校培训证书号 行院培训证书号 照片
		 * 
		 */
		TrainTrainee trainTrainee = null;
		for (int i = 0; i < listObject.size(); i++) {
			List<Object> lo = listObject.get(i);
			
			//导入的表格会错误的读取空行的内容，所以，当判断第一列为空，就跳过此行。
			if(!StringUtils.isNotEmpty((String) lo.get(0))){
				continue;
			}
					
			//查询学员库是否存在此学生
			trainTrainee = trainTraineeServie.getByProerties("sfzjh", lo.get(3));
			
			//查询此班级，是否已经存在此学员,则取出来进行数据更新操作 	//只要存在即可，isdelete为1的会被转为2
			TrainClasstrainee trainee = this.getByProerties(new String[]{"sfzjh","classId"}, new Object[]{lo.get(3),classId});
			if(trainee==null)
				trainee = new TrainClasstrainee();
			
			trainee.setClassId(classId);
			trainee.setXm(String.valueOf(lo.get(0)));
			trainee.setXbm(mapXbm.get(lo.get(1)));
			trainee.setMobilePhone(String.valueOf(lo.get(2)));
			trainee.setSfzjh(String.valueOf(lo.get(3)));
			trainee.setWorkUnit(String.valueOf(lo.get(4)));
			trainee.setPosition(String.valueOf(lo.get(5)));
			trainee.setHeadshipLevel(mapHeadshipLevel.get(lo.get(6)));
			trainee.setIsDelete(isDelete);	//设置isdelte值
				
			if (needSync.equals("1")) { // 同步到学员库
				if (trainTrainee == null){
					trainTrainee = new TrainTrainee();
					
					//当学员库没有此学员的时候，暂时加入这些数据（已存在的学员暂时不处理）
					trainTrainee.setTraineeCategory(mapTraineeCategory.get(lo.get(7)));
					trainTrainee.setMzm(mapMzm.get(lo.get(8)));
					trainTrainee.setZzmmm(mapZzmm.get(lo.get(9)));
					trainTrainee.setXlm(mapXlm.get(lo.get(10)));
					trainTrainee.setXwm(mapXwm.get(lo.get(11)));
					
					trainTrainee.setZym(String.valueOf(lo.get(12)));
					trainTrainee.setGraduateSchool(String.valueOf(lo.get(13)));
					trainTrainee.setDzxx(String.valueOf(lo.get(14)));
					trainTrainee.setAddress(String.valueOf(lo.get(15)));
					trainTrainee.setPartySchoolNumb(String.valueOf(lo.get(16)));
					trainTrainee.setNationalSchoolNumb(String.valueOf(lo.get(17)));
					
					//trainTrainee.setZp(String.valueOf(lo.get(18)));
					//照片使用身份证号码.jpg
					trainTrainee.setZp("/static/upload/traineePhoto/" +trainee.getSfzjh() + ".jpg");	
					
				}

				trainTrainee.setXm(trainee.getXm());
				trainTrainee.setXbm(trainee.getXbm());
				trainTrainee.setMobilePhone(trainee.getMobilePhone());
				trainTrainee.setSfzjh(trainee.getSfzjh());
				trainTrainee.setWorkUnit(trainee.getWorkUnit());
				trainTrainee.setPosition(trainee.getPosition());
				trainTrainee.setHeadshipLevel(trainee.getHeadshipLevel());
				
				trainTrainee.setUpdateTime(new Date()); // 设置修改时间
				trainTrainee.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
				
				trainTraineeServie.merge(trainTrainee);
			}

			if (trainTrainee != null){
				trainee.setTraineeId(trainTrainee.getUuid());			
			}
			this.merge(trainee);
		}
		
	}
	@Override
	public void doSyncClassTrainee(String classId,SysUser currentUser) {
		// TODO Auto-generated method stub
		//查询班级学员
		String hql ="from TrainClasstrainee where classId='"+classId+"' and isDelete=0 ";
		List<TrainClasstrainee> trainees = this.doQuery(hql);
		
		for(TrainClasstrainee trainee : trainees){
			
			//查询学员库是否存在此学生
			TrainTrainee trainTrainee = trainTraineeServie.getByProerties("sfzjh",trainee.getSfzjh());
			
			if(trainTrainee==null){
				trainTrainee=new TrainTrainee();
			}else{
				trainTrainee.setUpdateTime(new Date()); // 设置修改时间
				trainTrainee.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			}
			
			trainTrainee.setXm(trainee.getXm());
			trainTrainee.setXbm(trainee.getXbm());
			trainTrainee.setMobilePhone(trainee.getMobilePhone());
			trainTrainee.setSfzjh(trainee.getSfzjh());
			trainTrainee.setWorkUnit(trainee.getWorkUnit());
			trainTrainee.setPosition(trainee.getPosition());
			trainTrainee.setHeadshipLevel(trainee.getHeadshipLevel());
				
			//照片使用身份证号码.jpg
			//trainTrainee.setZp("/static/upload/traineePhoto/" +trainee.getSfzjh() + ".jpg");
			
			trainTraineeServie.merge(trainTrainee);
		}
	}

    @Override
    public List<Map<String, Object>> getClassTraineeCreditsList(String classTraineeId) {
        String sql = MessageFormat.format("SELECT className,courseName,courseDate,courseTime,courseCredits,changeCredits," +
                "realCredits FROM dbo.TRAIN_V_CLASSTRAINEECREDITS WHERE classTraineeId=''{0}'' \n" +
                "\t order by courseDate,courseTime",classTraineeId);
        List<Map<String, Object>> list = this.getForValuesToSql(sql);
        return list;
    }

    @Override
    public QueryResult<VoTrainClassCheck> getCheckList(Integer start, Integer limit, String classId, String classScheduleId,String xm) {
        String sql = "SELECT classTraineeId, classId, traineeId, xm, xbm, mobilePhone, workUnit, classScheduleId,incardTime,outcardTime,attendResult FROM TRAIN_V_CHECKRESULT where 1=1 ";
        if(StringUtils.isNotEmpty(classId))
            sql += " and classId='" + classId +"'";
        if(StringUtils.isNotEmpty(classScheduleId))
            sql += " and classScheduleId='" + classScheduleId +"'";
        if(StringUtils.isNotEmpty(xm))
            sql += " and xm like '%" + xm +"%'";
        QueryResult<VoTrainClassCheck> qr = this.doQueryResultSqlObject(sql, start,limit,VoTrainClassCheck.class);

        return  qr;
    }
}