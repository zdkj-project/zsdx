package com.zd.school.jw.train.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.Base64Util;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
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
import com.zd.school.plartform.system.model.CardUserInfoToUP;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: TrainClasstraineeServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 班级学员信息(TRAIN_T_CLASSTRAINEE)实体Service接口实现类. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainClasstraineeServiceImpl extends BaseServiceImpl<TrainClasstrainee>
		implements TrainClasstraineeService {

	private static Object syncObject = new Object();// 同步锁

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

	@Resource
	BuildRoominfoService roominfoService;

	private static Logger logger = Logger.getLogger(TrainClasstraineeServiceImpl.class);

	@Override
	public QueryResult<TrainClasstrainee> list(Integer start, Integer limit, String sort, String filter,
			Boolean isDelete) {
		QueryResult<TrainClasstrainee> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
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
	public Boolean doLogicDeleteByIds(String classId, String ids, SysUser currentUser) {
		Boolean delResult = false;
		try {
			Object[] conditionValue = ids.split(",");
			String[] propertyName = { "isDelete", "updateUser", "updateTime" };
			Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
			this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;

			// 设置班级的状态
			TrainClass trainClass = trainClassService.get(classId);
			Integer isuse = trainClass.getIsuse();
			if (isuse != null && isuse != 0) { // 当班级已经提交过一次之后，每次修改都设置为2
				trainClass.setIsuse(2);
				trainClass.setUpdateTime(new Date()); // 设置修改时间
				trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
				trainClassService.doUpdate(trainClass);
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
	public TrainClasstrainee doAddEntity(TrainClasstrainee entity, SysUser currentUser) {
		TrainClasstrainee saveEntity = new TrainClasstrainee();
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
	public int doUpdateRoomInfo(String classId, String roomId, String roomName, String ids, String xbm,
			SysUser currentUser) {

		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int traineeLength = ids.split(",").length;
		try {
			synchronized (syncObject) {
				// 判断性别是否一致
				String hqlSelect1 = "select count(*) from TrainClasstrainee where classId='" + classId
						+ "' and roomId='" + roomId + "' and xbm!='" + xbm + "'";
				if (this.getCount(hqlSelect1) > 0) {
					return -2; // 性别不一致
				}

				// 判断人数是否符合要求，若大于最大人数，则不允许设置
				String hqlSelect2 = "select count(*) from TrainClasstrainee where classId='" + classId
						+ "' and roomId='" + roomId + "' and uuid not in ('" + ids.replace(",", "','") + "')";

				// 获取此房间的人数
				BuildRoominfo roomInfo = roominfoService.get(roomId);
				int roomNum = 3; // 默认最大3人
				if (roomInfo != null && StringUtils.isNotEmpty(roomInfo.getExtField03())) {
					roomNum = Integer.parseInt(roomInfo.getExtField03());
				}

				if (this.getCount(hqlSelect2) + traineeLength <= roomNum) {
					String hqlUpdate = "update TrainClasstrainee t set t.roomId='" + roomId + "',t.roomName='"
							+ roomName + "'," + "	t.updateUser='" + currentUser.getXm() + "',t.updateTime='"
							+ sdf.format(new Date()) + "' " + "where t.isDelete!=1 and t.uuid in ('"
							+ ids.replace(",", "','") + "')";
					this.doExecuteHql(hqlUpdate);
					result = 1;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			result = -1;
		}

		return result;
	}

	@Override
	public int doCancelRoomInfo(String ids, SysUser currentUser) {
		// TODO Auto-generated method stub
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			String hqlUpdate = "update TrainClasstrainee t set t.roomId=NULL,t.roomName=NULL," + "	t.updateUser='"
					+ currentUser.getXm() + "',t.updateTime='" + sdf.format(new Date()) + "' " + "where t.uuid in ('"
					+ ids.replace(",", "','") + "')";
			this.doExecuteHql(hqlUpdate);
			result = 1;

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			result = -1;
		}

		return result;
	}

	@Override
	public List<ImportNotInfo> doImportTrainee(List<List<Object>> listObject, String classId, String needSync,
			SysUser currentUser) {
		// TODO Auto-generated method stub

		List<ImportNotInfo> listNotExit = new ArrayList<>();
		SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ImportNotInfo notExits = null;
		Integer notCount = 1;

		// XBM;HEADSHIPLEVEL 必填项
		// TRAINEECATEGORY;XWM;XLM;ZZMMM;MZM 同步时需要的项
		Map<String, String> mapHeadshipLevel = new HashMap<>();
		Map<String, String> mapXbm = new HashMap<>();
		Map<String, String> mapClassGroup = new HashMap<>();
		String hql1 = " from BaseDicitem where dicCode in ('HEADSHIPLEVEL','XBM','CLASSGROUP')";
		List<BaseDicitem> listBaseDicItems1 = dicitemService.getQuery(hql1);
		for (BaseDicitem baseDicitem : listBaseDicItems1) {
			if (baseDicitem.getDicCode().equals("XBM"))
				mapXbm.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
			else if (baseDicitem.getDicCode().equals("HEADSHIPLEVEL"))
				mapHeadshipLevel.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
			else
				mapClassGroup.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
		}

		// 若值为1，则要加载这些字典项。
		Map<String, String> mapTraineeCategory = new HashMap<>();
		Map<String, String> mapXwm = new HashMap<>();
		Map<String, String> mapXlm = new HashMap<>();
		Map<String, String> mapZzmm = new HashMap<>();
		Map<String, String> mapMzm = new HashMap<>();
		if (needSync.equals("1")) {
			String hql2 = " from BaseDicitem where dicCode in ('TRAINEECATEGORY','XWM','XLM','ZZMMM','MZM')";
			List<BaseDicitem> listBaseDicItems2 = dicitemService.getQuery(hql2);
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

		// 设置班级的状态
		Integer isDelete = 0; // 当班级提交之后再导入学员，则设置学员isdelete值为2
		TrainClass trainClass = trainClassService.get(classId);
		Integer isuse = trainClass.getIsuse();
		if (isuse != null && isuse != 0) { // 当班级已经提交过一次之后，每次修改都设置为2
			isDelete = 2;
			trainClass.setIsuse(2);
			trainClass.setUpdateTime(new Date()); // 设置修改时间
			trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			// trainClassService.update(trainClass);
		}

		/**
		 * 姓名 性别 移动电话 身份证件号 所在单位 职务 行政级别 学员类型 民族 政治面貌 学历 学位 专业 毕业学校 电子邮件 通讯地址
		 * 党校培训证书号 行院培训证书号 照片
		 * 
		 */
		String doResult = "";
		String title = "";
		String errorLevel = "";
		boolean isError = false;
		TrainTrainee trainTrainee = null;
		for (int i = 0; i < listObject.size(); i++) {
			try {

				List<Object> lo = listObject.get(i);

				// 导入的表格会错误的读取空行的内容，所以，当判断第一列为空，就跳过此行。
				if (!StringUtils.isNotEmpty((String) lo.get(0))) {
					continue;
				}

				title = String.valueOf(lo.get(0));
				doResult = "导入成功"; // 默认是成功
				// isError = false;

				// 查询学员库是否存在此学生（2018-1-22改）
				trainTrainee = trainTraineeServie.getByProerties(new String[] { "sfzjh", "isDelete" }, new Object[] { lo.get(3), 0 });

				// 查询此班级，是否已经存在此学员,则取出来进行数据更新操作 //只要存在即可，isdelete为1的会被转为2
				TrainClasstrainee trainee = this.getByProerties(new String[] { "sfzjh", "classId" },
						new Object[] { lo.get(3), classId });
				if (trainee == null)
					trainee = new TrainClasstrainee();

				trainee.setClassId(classId);
				trainee.setXm(String.valueOf(lo.get(0)));
				trainee.setXbm(mapXbm.get(lo.get(1)));
				//trainee.setMobilePhone(String.valueOf(lo.get(2)));
				//trainee.setSfzjh(String.valueOf(lo.get(3)));
				trainee.setMobilePhone(Base64Util.encodeData(String.valueOf(lo.get(2))));
				trainee.setSfzjh(Base64Util.encodeData(String.valueOf(lo.get(3))));
				
				trainee.setWorkUnit(String.valueOf(lo.get(4)));
				trainee.setPosition(String.valueOf(lo.get(5)));
				trainee.setHeadshipLevel(mapHeadshipLevel.get(lo.get(6)));
				trainee.setClassGroup(mapClassGroup.get(lo.get(8)));
				trainee.setTraineeNumber(String.valueOf(lo.get(9)));	//学号
				trainee.setIsDelete(isDelete); // 设置isdelte值

				if (needSync.equals("1")) { // 同步到学员库
					if (trainTrainee == null) {
						trainTrainee = new TrainTrainee();

						// 当学员库没有此学员的时候，暂时加入这些数据（已存在的学员暂时不处理）					
						trainTrainee.setMzm(mapMzm.get(lo.get(10)));
						trainTrainee.setZzmmm(mapZzmm.get(lo.get(11)));
						trainTrainee.setXlm(mapXlm.get(lo.get(12)));
						trainTrainee.setXwm(mapXwm.get(lo.get(13)));

						trainTrainee.setZym(String.valueOf(lo.get(14)));
						trainTrainee.setGraduateSchool(String.valueOf(lo.get(15)));
						trainTrainee.setDzxx(String.valueOf(lo.get(16)));
						trainTrainee.setAddress(String.valueOf(lo.get(17)));
						trainTrainee.setPartySchoolNumb(String.valueOf(lo.get(18)));
						trainTrainee.setNationalSchoolNumb(String.valueOf(lo.get(19)));

						// trainTrainee.setZp(String.valueOf(lo.get(19)));
						// 照片使用身份证号码.jpg
						trainTrainee.setZp("/static/upload/traineePhoto/" + trainee.getSfzjh() + ".jpg");

					}
					
					trainTrainee.setTraineeCategory(mapTraineeCategory.get(lo.get(7)));
					trainTrainee.setXm(trainee.getXm());
					trainTrainee.setXbm(trainee.getXbm());
					trainTrainee.setMobilePhone(trainee.getMobilePhone());
					trainTrainee.setSfzjh(trainee.getSfzjh());
					trainTrainee.setWorkUnit(trainee.getWorkUnit());
					trainTrainee.setPosition(trainee.getPosition());
					trainTrainee.setHeadshipLevel(trainee.getHeadshipLevel());
					trainTrainee.setTraineeNumber(trainee.getTraineeNumber());	//学号
					trainTrainee.setIsDelete(0);
					trainTrainee.setUpdateTime(new Date()); // 设置修改时间
					trainTrainee.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名

					trainTraineeServie.doMerge(trainTrainee);
				}

				if (trainTrainee != null) {
					trainee.setTraineeId(trainTrainee.getUuid());
				}
				this.doMerge(trainee);

			} catch (Exception e) {
				// return null;
				errorLevel = "错误";
				doResult = "导入失败；异常信息：" + e.getMessage();
			}

			if (!"导入成功".equals(doResult)) {
				// List<Map<String, Object>>
				notExits = new ImportNotInfo();
				notExits.setOrderIndex(notCount);
				notExits.setTitle(title);
				notExits.setErrorLevel(errorLevel);
				notExits.setErrorInfo(doResult);

				listNotExit.add(notExits);
				notCount++;
			}
		}

		// 如果两个容器的大小一样，表明没有导入数据,否则导入了
		if (listObject.size() != listNotExit.size()) {
			trainClassService.doUpdate(trainClass);
		}

		return listNotExit;

	}

	@Override
	public void doSyncClassTrainee(String classId, SysUser currentUser) {
		// TODO Auto-generated method stub
		// 查询班级学员
		String hql = "from TrainClasstrainee where classId='" + classId + "' and isDelete=0 ";
		List<TrainClasstrainee> trainees = this.getQuery(hql);

		for (TrainClasstrainee trainee : trainees) {

			// 查询学员库是否存在此学生
			TrainTrainee trainTrainee = trainTraineeServie.getByProerties("sfzjh", trainee.getSfzjh());

			if (trainTrainee == null) {
				trainTrainee = new TrainTrainee();
			} else {
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
			trainTrainee.setIsDelete(0);
			// 照片使用身份证号码.jpg
			// trainTrainee.setZp("/static/upload/traineePhoto/"
			// +trainee.getSfzjh() + ".jpg");

			trainTraineeServie.doMerge(trainTrainee);
		}
	}

	@Override
	public List<Map<String, Object>> getClassTraineeCreditsList(String classTraineeId) {
		String sql = MessageFormat
				.format("SELECT className,courseName,courseDate,courseTime,courseCredits,changeCredits,"
						+ "realCredits FROM dbo.TRAIN_V_CLASSTRAINEECREDITS WHERE classTraineeId=''{0}'' \n"
						+ "\t order by courseDate,courseTime", classTraineeId);
		List<Map<String, Object>> list = this.getForValuesToSql(sql);
		return list;
	}

	@Override
	public QueryResult<VoTrainClassCheck> getCheckList(Integer start, Integer limit, String classId,
			String classScheduleId, String xm) {
		String sql = "SELECT classTraineeId as classTraineeId, classId as classId, traineeId as traineeId,"
				+ " xm as xm, xbm as xbm, mobilePhone as mobilePhone, workUnit as workUnit,"
				+ " classScheduleId as classScheduleId,incardTime as incardTime,outcardTime as outcardTime,"
				+ " attendResult as attendResult,attendMinute as attendMinute,isLeave as isLeave,"
				+ " remark as remark,traineeNumber as traineeNumber"
				+ " FROM TRAIN_V_CHECKRESULT where 1=1 ";
		
	
		if (StringUtils.isNotEmpty(classId))
			sql += " and classId='" + classId + "'";
		if (StringUtils.isNotEmpty(classScheduleId))
			sql += " and classScheduleId='" + classScheduleId + "'";
		if (StringUtils.isNotEmpty(xm))
			sql += " and xm like '%" + xm + "%'";
		
		sql+=" order by traineeNumber asc,xm asc";
		
		QueryResult<VoTrainClassCheck> qr = this.getQueryResultSqlObject(sql, start, limit, VoTrainClassCheck.class);

		return qr;
	}

	@Override
	public void doSyncUnBindToUP(String ids, String xm) {
		// TODO Auto-generated method stub

		try {
			// 1.查询出这些人员数据(正常卡)
			String sql = "select convert(varchar,a.EMPLOYEEID) as uuid,a.UserId as userId,a.EmployeeStrID as sid,a.EmployeeStatusID as employeeStatusID,"
					+ " convert(varchar,b.CardID) as upCardId,convert(varchar,b.FactoryFixID) as factNumb,"
					+ " convert(int,b.CardStatusIDXF) as useState from Tc_Employee a join TC_Card b "
					+ " on a.CardID=b.CardID and a.EmployeeID=b.EmployeeID " // 双向关联
					+ " where a.UserId in('" + ids.replace(",", "','")
					+ "') and (b.CardStatusIDXF=1 or b.CardStatusIDXF=2)";

			List<CardUserInfoToUP> upCardUserInfos = this.getQuerySqlObject(sql, CardUserInfoToUP.class);

			SimpleDateFormat fmtDateTime = new SimpleDateFormat("yyyy-M-d h:mm");
			String currentDate = fmtDateTime.format(new Date());

			CardUserInfoToUP userInfoToUP = null;
			StringBuffer sqlSb = new StringBuffer();
			String sqlStr = "";

			for (int i = 0; i < upCardUserInfos.size(); i++) {
				if (sqlSb.length() > 3000) {
					this.doExecuteSql(sqlSb.toString());
					sqlSb.setLength(0); // 清空
				}

				userInfoToUP = upCardUserInfos.get(i);

				if (userInfoToUP.getUseState() == 1) {
					// 解绑：将正常卡状态为4
					sqlStr = "UPDATE tc_card SET EMPLOYEEID=0,CardStatusIDXF=4,CardStatusIDJS=4  WHERE CARDID="
							+ userInfoToUP.getUpCardId() + ";"
							+ " UPDATE tc_employee SET EMPLOYEESTRID='',EmployeeStatusID='26' WHERE userId='"
							+ userInfoToUP.getUserId() + "' ;" + " insert into tc_ChangeCard_log values('" + currentDate
							+ "','" + xm + "'," + userInfoToUP.getUpCardId() + "," + userInfoToUP.getUuid()
							+ ",'正常卡解绑');";

				} else if (userInfoToUP.getUseState() == 2) {
					// 解绑：将挂失卡，设置人员绑定卡片id为4
					sqlStr = " UPDATE tc_employee SET CARDID=0,EMPLOYEESTRID='',EmployeeStatusID='26' WHERE userId='"
							+ userInfoToUP.getUserId() + "' ;" + " insert into tc_ChangeCard_log values('" + currentDate
							+ "','" + xm + "'," + userInfoToUP.getUpCardId() + "," + userInfoToUP.getUuid()
							+ ",'挂失卡解绑');";
				}
				sqlSb.append(sqlStr + "  ");
			}
			// 最后执行一次
			if (sqlSb.length() > 0)
				this.doExecuteSql(sqlSb.toString());

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException(e); // 抛出到控制器中捕获，并且自动回滚

		}

	}

	@Override
	public List<Map<String, Object>> doCardBind(String ids) {
		// 绑定卡操作
		String[] idsArray = ids.split(",");
		int idsCount = idsArray.length;
		String sql = "";
		int count = 0;

		try {
			// // 获取未绑定卡的数量
			// sql = "select Count(CARD_ID) from CARD_T_USEINFO where
			// CARD_TYPE_ID=3 AND USER_ID IS NULL AND USE_STATE !=2 and
			// USE_STATE !=3 ";
			// count = this.getForValueToSql(sql);
			//
			// // 判断空闲卡数量
			// if (count < idsCount) {
			// return new ArrayList<>();
			// }

			// 获取空闲卡信息
			sql = "select top " + idsCount
					+ " CARD_ID,UP_CARD_ID,CARD_PRINT_ID,USE_STATE from CARD_T_USEINFO where CARD_TYPE_ID=3 AND USER_ID IS NULL AND USE_STATE !=2 and USE_STATE !=3 order by UP_CARD_ID asc";
			List<Map<String, Object>> cardUnBinds = this.getForValuesToSql(sql);
			// 判断空闲卡数量是否符合
			if (cardUnBinds.size() < idsCount) {
				return new ArrayList<>();
			}

			for (int i = 0; i < idsCount; i++) {

				String cardId = String.valueOf(cardUnBinds.get(i).get("CARD_ID"));

				// 通过卡ID绑定学员更新卡状态
				sql = "update CARD_T_USEINFO SET USE_STATE=1 ,USER_ID='" + idsArray[i] + "' WHERE CARD_ID='" + cardId
						+ "'";
				this.doExecuteSql(sql);

				cardUnBinds.get(i).put("USER_ID", idsArray[i]);
			}

			return cardUnBinds;

		} catch (Exception e) {

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			return null;
		}

	}

	@Override
	public void doSyncBindToUP(List<Map<String, Object>> cardInfoToUp, String xm) {
		// TODO Auto-generated method stub
		try {

			SimpleDateFormat fmtDateTime = new SimpleDateFormat("yyyy-M-d h:mm");
			String currentDate = fmtDateTime.format(new Date());
			StringBuffer sqlSb = new StringBuffer();
			String sqlStr = "";

			Map<String, Object> map = null;
			for (int i = 0; i < cardInfoToUp.size(); i++) {
				if (sqlSb.length() > 3000) {
					this.doExecuteSql(sqlSb.toString());
					sqlSb.setLength(0); // 清空
				}

				map = cardInfoToUp.get(i);
				String empId = this
						.getForValueToSql("select convert(varchar,EMPLOYEEID) as uuid from tc_employee where userId='"
								+ map.get("USER_ID") + "'");
				// 绑定：设置卡状态为1
				sqlStr = "UPDATE tc_card SET EMPLOYEEID='" + empId
						+ "',CardStatusIDXF=1,CardStatusIDJS=1  WHERE CARDID=" + map.get("UP_CARD_ID") + ";"
						+ "	UPDATE tc_employee SET EMPLOYEESTRID='" + map.get("CARD_PRINT_ID")
						+ "',EmployeeStatusID='24',CARDID=" + map.get("UP_CARD_ID") + " WHERE EMPLOYEEID='" + empId
						+ "';" + " insert into tc_ChangeCard_log values('" + currentDate + "','" + xm + "',"
						+ map.get("UP_CARD_ID") + "," + empId + ",'绑定');";
				sqlSb.append(sqlStr + "  ");
			}
			// 最后执行一次
			if (sqlSb.length() > 0)
				this.doExecuteSql(sqlSb.toString());

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException(e); // 抛出到控制器中捕获，并且自动回滚

		}
	}

	@Override
	public void doRestoreCardBind(List<Map<String, Object>> cardInfoToUp) {
		// TODO Auto-generated method stub
		// String cardIds="";
		StringBuffer sqlSb = new StringBuffer();
		String sqlStr = "";
		try {
			for (int i = 0; i < cardInfoToUp.size(); i++) {
				// cardIds +=
				// String.valueOf(cardInfoToUp.get(i).get("CARD_ID"))+",";
				if (sqlSb.length() > 3000) {
					this.doExecuteSql(sqlSb.toString());
					sqlSb.setLength(0); // 清空
				}

				sqlStr = "update CARD_T_USEINFO SET USE_STATE='" + cardInfoToUp.get(i).get("USE_STATE")
						+ "',USER_ID=NULL WHERE CARD_ID = " + cardInfoToUp.get(i).get("CARD_ID");
				sqlSb.append(sqlStr + "  ");
			}
			// 最后执行一次
			if (sqlSb.length() > 0)
				this.doExecuteSql(sqlSb.toString());

		} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException(e); // 抛出到控制器中捕获，并且自动回滚

		}
		// cardIds=cardIds.substring(0,cardIds.length()-1);
		// 一次性重置发卡状态
		// String sql = "update CARD_T_USEINFO SET USE_STATE='0',USER_ID=NULL
		// WHERE CARD_ID in ('" + cardIds.replace(",", "','") + "')";
		// this.executeSql(sql);

	}
}