package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.ImportNotInfo;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.model.app.CourseEvalApp;
import com.zd.school.jw.train.dao.TrainClassscheduleDao;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;
import com.zd.school.jw.train.service.*;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * ClassName: TrainClassscheduleServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 班级课程日历(TRAIN_T_CLASSSCHEDULE)实体Service接口实现类. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainClassscheduleServiceImpl extends BaseServiceImpl<TrainClassschedule>
		implements TrainClassscheduleService {

	@Resource
	public void setTrainClassscheduleDao(TrainClassscheduleDao dao) {
		this.dao = dao;
	}

	@Resource
	private TrainClassService trainClassService;

	@Resource
	private BaseDicitemService dicitemService;

	@Resource
	private TrainTeacherService teacherService;

	@Resource
	private TrainCourseinfoService trainCourseinfoService;

	@Resource
	private TrainCoursecategoryService coursecategoryService;

	@Resource
	private TrainIndicatorStandService standService;

	private static Logger logger = Logger.getLogger(TrainClassscheduleServiceImpl.class);

	@Override
	public QueryResult<TrainClassschedule> list(Integer start, Integer limit, String sort, String filter,
			Boolean isDelete) {
		QueryResult<TrainClassschedule> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
		return qResult;
	}

	/**
	 * 根据主键逻辑删除数据 New:删除后，更新班级的isuse值
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
			this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;

			// 设置班级的状态
			TrainClass trainClass = trainClassService.get(classId);
			Integer isuse = trainClass.getIsuse();
			if (isuse != null && isuse != 0) { // 当班级已经提交过一次之后，每次修改都设置为2
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
	public TrainClassschedule doUpdateEntity(TrainClassschedule entity, String teachType, SysUser currentUser) {
		// 先拿到已持久化的实体
		TrainClassschedule saveEntity = this.get(entity.getUuid());
		try {
			//

			// 查询课程表中是否存在 课程名、教师名一致的课程
			TrainCourseinfo trainCourseInfo = trainCourseinfoService.getByProerties(
					new String[] { "courseName", "mainTeacherId", "isDelete" },
					new Object[] { entity.getCourseName(), entity.getMainTeacherId(), 0 });

			// 如果存在，把课程id和教师id直接存放到当前班级课程中
			// 否则，创建新的课程，并查找教师id
			if (trainCourseInfo == null) {
				// 查询未分类的id
				String hql = "select uuid from TrainCoursecategory a where a.isDelete=? and a.nodeText=?";
				String categoryId = coursecategoryService.getForValue(hql, 0, "未分类");

				// 否则存入到课程库
				trainCourseInfo = new TrainCourseinfo();
				trainCourseInfo.setCategoryId(categoryId);
				trainCourseInfo.setCourseName(entity.getCourseName());
				trainCourseInfo.setMainTeacherId(entity.getMainTeacherId());
				trainCourseInfo.setMainTeacherName(entity.getMainTeacherName());
			}
			trainCourseInfo.setCredits(entity.getCredits());
			trainCourseInfo.setTeachType(teachType); // 当课程不存在，并创建的时候，才会设置教学形式

			trainCourseinfoService.merge(trainCourseInfo);

			entity.setCourseId(trainCourseInfo.getUuid());

			List<String> exclued = new ArrayList<>();
			exclued.add("courseMode");
			BeanUtils.copyPropertiesExceptNull(saveEntity, entity, exclued);
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
	public TrainClassschedule doAddEntity(TrainClassschedule entity, String teachType, SysUser currentUser) {
		TrainClassschedule saveEntity = null;
		try {
			// 查询此班级是否已经存在此课程,若存在则取出来进行数据更新操作 //只要存在即可，isdelete为1的会被转为2
			// (2017-11-17加入开始结束时间进行判断是否重复)
			saveEntity = this.getByProerties(
					new String[] { "courseName", "mainTeacherName", "classId", "beginTime", "endTime" },
					new Object[] { entity.getCourseName(), entity.getMainTeacherName(), entity.getClassId(),
							entity.getBeginTime(), entity.getEndTime() });
			// 若不为null，则使用此班级课程的id
			if (saveEntity == null)
				saveEntity = new TrainClassschedule();

			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			excludedProp.add("scheduleAddress");
			excludedProp.add("roomId");
			BeanUtils.copyProperties(saveEntity, entity, excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名

			// 设置班级的状态
			TrainClass trainClass = trainClassService.get(entity.getClassId());
			Integer isuse = trainClass.getIsuse();
			if (isuse != null && isuse != 0) { // 当班级已经提交过一次之后，每次修改都设置为2
				saveEntity.setIsDelete(2);

				trainClass.setIsuse(2);
				trainClass.setUpdateTime(new Date()); // 设置修改时间
				trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
				trainClassService.update(trainClass);
			}

			// 查询课程表中是否存在 课程名、教师名一致的课程
			TrainCourseinfo trainCourseInfo = trainCourseinfoService.getByProerties(
					new String[] { "courseName", "mainTeacherId", "isDelete" },
					new Object[] { saveEntity.getCourseName(), saveEntity.getMainTeacherId(), 0 });

			// 如果存在，把课程id和教师id直接存放到当前班级课程中
			// 否则，创建新的课程，并查找教师id
			if (trainCourseInfo == null) {
				// 查询未分类的id
				String hql = "select uuid from TrainCoursecategory a where a.isDelete=? and a.nodeText=?";
				String categoryId = coursecategoryService.getForValue(hql, 0, "未分类");

				// 否则存入到课程库
				trainCourseInfo = new TrainCourseinfo();
				trainCourseInfo.setCategoryId(categoryId);
				trainCourseInfo.setTeachType(teachType); // 当课程不存在，并创建的时候，才会设置教学形式
				trainCourseInfo.setCourseName(entity.getCourseName());
				trainCourseInfo.setMainTeacherId(entity.getMainTeacherId());
				trainCourseInfo.setMainTeacherName(entity.getMainTeacherName());
				trainCourseInfo.setCredits(entity.getCredits());

				trainCourseinfoService.merge(trainCourseInfo);
			}

			saveEntity.setCourseId(trainCourseInfo.getUuid());
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
	public int doUpdateRoomInfo(String roomIds, String roomNames, String ids, SysUser currentUser) {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 判断房间是否被其他课程使用

		try {
			String idArray[] = ids.split(",");
			String roomIdArray[] = roomIds.split(",");
			String id = null;
			String roomId = null;
			for (int i = 0; i < idArray.length; i++) {
				id = idArray[i];
				for (int j = 0; j < roomIdArray.length; j++) {
					roomId = roomIdArray[j];
					List lists = this.doQuerySql("EXECUTE TRAIN_P_ISUSESITE '" + id + "','" + roomId + "'");
					if ("0".equals(lists.get(0).toString())) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return 0;
					}
				}
				String hqlUpdate = "update TrainClassschedule t set t.roomId='" + roomIds + "',t.scheduleAddress='"
						+ roomNames + "'," + "	t.updateUser='" + currentUser.getXm() + "',t.updateTime='"
						+ sdf.format(new Date()) + "' " + "where t.isDelete!=1 and t.uuid in ('" + id + "')";
				this.executeHql(hqlUpdate);
			}
			result = 1;

		} catch (Exception e) {
			// TODO: handle exception
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(e.getMessage());
			result = -1;
		}

		return result;
	}

	@Override
	public int doCancelRoomInfo(String ids, SysUser currentUser) {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			String hqlUpdate = "update TrainClassschedule t set t.roomId=NULL,t.scheduleAddress=NULL,"
					+ "	t.updateUser='" + currentUser.getXm() + "',t.updateTime='" + sdf.format(new Date()) + "' "
					+ "where t.uuid in ('" + ids.replace(",", "','") + "')";
			this.executeHql(hqlUpdate);
			result = 1;

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			result = -1;
		}

		return result;
	}

	@Override
	public List<ImportNotInfo> doImportData(List<List<Object>> importData, String classId, SysUser currentUser) {
		// TODO Auto-generated method stub

		List<ImportNotInfo> listNotExit = new ArrayList<>();
		SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String createUser = currentUser.getXm();

		ImportNotInfo notExits = null;
		Integer notCount = 1;

		// 所有的教学形式字典项
		Map<String, String> mapTeachType = new HashMap<>();
		Map<String, String> mapClassGroup = new HashMap<>();
		String hql = " from BaseDicitem where dicCode in ('TEACHTYPE','CLASSGROUP')";
		List<BaseDicitem> listTeachType = dicitemService.doQuery(hql);
		for (BaseDicitem baseDicitem : listTeachType) {
			if (baseDicitem.getDicCode().equals("TEACHTYPE"))
				mapTeachType.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
			else
				mapClassGroup.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
		}

		// 查询未分类的id
		hql = "select uuid from TrainCoursecategory a where a.isDelete=? and a.nodeText=?";
		String categoryId = coursecategoryService.getForValue(hql, 0, "未分类");

		/**
		 * 格式 第一行为列头【日期 开始时间 结束时间 教学形式 课程名称 主讲老师 是否评价 课程学分】 第二行开始为数据 【2017年6月13日
		 * 10:00 12:00 专题讲座 学习贯彻习近平总书记对广东工作重要批示精神 刘忠友 是 4】
		 */
		// 设置班级的状态
		TrainClass trainClass = trainClassService.get(classId);
		Integer isuse = trainClass.getIsuse();
		Integer isDelete = 0;
		if (isuse != null && isuse != 0) { // 当班级已经提交过一次之后，每次修改都设置为2
			isDelete = 2;
			trainClass.setIsuse(2);
			trainClass.setUpdateTime(new Date()); // 设置修改时间
			trainClass.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
		}
		String doResult = "";
		String title = "";
		String errorLevel = "";
		boolean isError = false;
		String beginTime = null;
		String endTime = null;
		String teacheName = null;
		String courseName = null;
		// String courseMode = null;
		String courseCredits = null;
		String isOptional = null;
		String classGroup = null;
		TrainCourseinfo trainCourseInfo = null;
		for (int i = 0; i < importData.size(); i++) {

			try {

				List<Object> lo = importData.get(i);

				// 导入的表格会错误的读取空行的内容，所以，当判断第一列为空，就跳过此行。
				if (!StringUtils.isNotEmpty((String) lo.get(0))) {
					continue;
				}

				beginTime = String.valueOf(lo.get(0)) + " " + String.valueOf(lo.get(1));
				endTime = String.valueOf(lo.get(0)) + " " + String.valueOf(lo.get(2));

				courseName = String.valueOf(lo.get(4));
				teacheName = String.valueOf(lo.get(5));
				courseCredits = String.valueOf(lo.get(7));
				isOptional = String.valueOf(lo.get(8));
				classGroup = String.valueOf(lo.get(9));

				title = courseName;
				doResult = "导入成功"; // 默认是成功
				isError = false;

				// 查询此班级是否已经存在此课程,若存在则取出来进行数据更新操作 //只要存在即可，isdelete为1的会被转为2
				TrainClassschedule tcs = this.getByProerties(
						new String[] { "courseName", "mainTeacherName", "classId", "beginTime", "endTime" },
						new Object[] { courseName, teacheName, classId, dateTimeSdf.parse(beginTime),
								dateTimeSdf.parse(endTime) });
				if (tcs == null)
					tcs = new TrainClassschedule();
				// TrainClassschedule tcs = new TrainClassschedule();
				tcs.setClassId(classId);
				tcs.setBeginTime(dateTimeSdf.parse(beginTime));
				tcs.setEndTime(dateTimeSdf.parse(endTime));
				tcs.setCourseName(courseName);
				tcs.setMainTeacherName(teacheName);
				tcs.setIsEval(String.valueOf(lo.get(6)).equals("是") ? 1 : 0);
				tcs.setIsDelete(isDelete); // 设置isdelete为特定的值。
				tcs.setCredits(Integer.valueOf(courseCredits));
				tcs.setIsOptional(String.valueOf(isOptional).equals("是") ? 1 : 0); // 课程是否选修课
				tcs.setClassGroup(mapClassGroup.get(classGroup)); // 课程分班

				// 查询课程表中是否存在 课程名、教师名一致的课程
				trainCourseInfo = trainCourseinfoService.getByProerties(
						new String[] { "courseName", "mainTeacherName", "isDelete" },
						new Object[] { courseName, teacheName, 0 });
						// hql = "from TrainCourseinfo a where a.isDelete=? and
						// a.courseName=? and a.mainTeacherName=?";
						// trainCourseInfo =
						// trainCourseinfoService.getForValue(hql, 0,
						// courseName, teacheName);

				// 查询是否是团队授课模式 1：单一模式 2：群组模式
				/*
				 * if (courseMode.equals("是")) { tcs.setCourseMode((short) 2); }
				 * else { tcs.setCourseMode((short) 1); }
				 */

				// 如果存在，把课程id和教师id直接存放到当前班级课程中
				// 否则，创建新的课程，并查找教师id
				if (trainCourseInfo == null) {
					// 否则存入到课程库
					trainCourseInfo = new TrainCourseinfo();
					trainCourseInfo.setCategoryId(categoryId);
					trainCourseInfo.setTeachType(mapTeachType.get(lo.get(3)));
					trainCourseInfo.setCourseName(courseName);
					trainCourseInfo.setMainTeacherName(teacheName);
					trainCourseInfo.setCredits(tcs.getCredits());

					// 查询是否是团队授课模式 1：单一模式 2：群组模式
					// if (courseMode.equals("是")) {
					// 理论上会出现多个老师，所以要拆分，存入老师id
					String teaStr[] = teacheName.split(",");
					String teaIds = "";
					for (int j = 0; j < teaStr.length; j++) {
						hql = "from TrainTeacher a where a.isDelete=? and a.xm=?";
						List<TrainTeacher> trainTeachers = teacherService.getForValues(hql, 0, teaStr[j]);
						if (trainTeachers.size() > 1) {
							// 若存在多个同名老师，则记录报错信息
							errorLevel = "错误";
							doResult = "异常信息：系统中存在同名的老师 " + teaStr[j] + "；请在班级课程管理处进行手动添加！";
							teaIds = null;
							isError = true;
							break;
						} else if (trainTeachers.size() == 1) {
							teaIds += trainTeachers.get(0).getUuid() + ",";
						} else {
							// 如果老师没找到id，就存放空字符串
							teaIds += ",";
						}
					}
					if (teaIds != null && !teaIds.equals("")) {
						teaIds = teaIds.substring(0, teaIds.length() - 1);
						trainCourseInfo.setMainTeacherId(teaIds);
					}

					// } else {
					// // 理论是单个教师，所以直接查找通过教师名称查找即可
					// hql = "from TrainTeacher a where a.isDelete=? and
					// a.xm=?";
					// List<TrainTeacher> trainTeachers =
					// teacherService.getForValues(hql, -1, teacheName);
					// if (trainTeachers.size() > 1) {
					// // 若存在多个同名老师，则记录报错信息
					// errorLevel = "错误";
					// doResult = "异常信息：系统中存在同名的老师 " + teacheName +
					// "；请在班级课程管理处进行手动调整！";
					// isError = true;
					// } else if (trainTeachers.size() == 1) {
					// trainCourseInfo.setMainTeacherId(trainTeachers.get(0).getUuid());
					// }
					// }

					if (isError == false)
						trainCourseinfoService.merge(trainCourseInfo);
				}

				// 当没发生错误的时候，才会添加入库
				if (isError == false) {
					tcs.setCourseId(trainCourseInfo.getUuid());
					tcs.setMainTeacherId(trainCourseInfo.getMainTeacherId());

					this.merge(tcs);
				}

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
		if (importData.size() != listNotExit.size()) {
			trainClassService.update(trainClass);
		}

		return listNotExit;
	}

	@Override
	public CourseEvalApp getCourseEvalStand(String scheduleId) {
		List<TrainIndicatorStand> evalStand = standService.getCourseEvalStand();
		String sql = "SELECT classId,classCategory,className,courseDate,courseTime,convert(varchar(10),verySatisfaction) as verySatisfaction"
				+ ",convert(varchar(10),satisfaction) as satisfaction,ranking,teacherId,teacherName,courseId,courseName,classScheduleId,"
				+ " teachTypeName,advise FROM TRAIN_V_CLASSCOURSEEVAL where classScheduleId=''{0}''";
		sql = MessageFormat.format(sql, scheduleId);
		CourseEvalApp entity = new CourseEvalApp();
		List<TrainClassCourseEval> evalCourse = this.doQuerySqlObject(sql, TrainClassCourseEval.class);
		entity.setSuccess(true);
		entity.setMessage("获取评价标准成功");
		entity.setEvalCourse(evalCourse.get(0));
		entity.setEvalStand(evalStand);
		return entity;
	}

	@Override
	public QueryResult<TrainClassCourseEval> getClassCourseEval(Integer start, Integer limit, String orderSql,
			String classId) {
		// 课程名称 课程类型 上课教师 很满意度 满意度 很满意排名

		String sql = "SELECT classId,classCategory,className,courseDate,courseTime,convert(varchar(10),verySatisfaction) as verySatisfaction"
				+ ",convert(varchar(10),satisfaction) as satisfaction,ranking,teacherId,teacherName,courseId,courseName,classScheduleId,"
				+ " teachTypeName,advise FROM TRAIN_V_CLASSCOURSEEVAL where classId=''{0}''";
		sql = MessageFormat.format(sql, classId);
		if (StringUtils.isNotEmpty(orderSql)) {
			sql += orderSql;
		}
		// this.getForValuesToSql()
		QueryResult<TrainClassCourseEval> list = this.doQueryResultSqlObject(sql, start, limit,
				TrainClassCourseEval.class);

		return list;
	}

	@Override
	public List<Map<String, Object>> getClassCourseRanking(String orderSql, String classId) {
		String sql = "SELECT courseName,teachTypeName,teacherName,convert(varchar(10),verySatisfaction) as verySatisfaction"
				+ ",convert(varchar(10),satisfaction) as satisfaction,ranking FROM TRAIN_V_CLASSCOURSEEVAL where classId=''{0}'' ";
		sql = MessageFormat.format(sql, classId);
		if (StringUtils.isNotEmpty(orderSql)) {
			sql += orderSql;
		}
		List<Map<String, Object>> list = this.getForValuesToSql(sql);

		return list;
	}
}