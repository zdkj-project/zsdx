package com.zd.school.oa.flow.service;

import com.zd.core.model.BaseApplayEntity;
import com.zd.core.service.BaseService;

public interface ApplayService extends BaseService<BaseApplayEntity> {

	/**
	 * 
	 * getClassLeader:获取指定学生的所在班级的班主任
	 *
	 * @author luoyibo
	 * @param userId
	 * @return String
	 * @throws @since
	 *             JDK 1.8
	 */
	public String getClassLeader(String userId);

	public String getClassLeaderList(String userId);

	/**
	 * 
	 * getClassLeader:获取指定学生的所在年级的年级组长
	 *
	 * @author luoyibo
	 * @param userId
	 * @return String
	 * @throws @since
	 *             JDK 1.8
	 */
	public String getGradeLeader(String userId);

	public String getGradeLeaderList(String userId);

	/**
	 * 
	 * getClassLeader:获取指定学生的学生处主任
	 *
	 * @author luoyibo
	 * @param userId
	 * @return String
	 * @throws @since
	 *             JDK 1.8
	 */
	public String getEduLeader(String userId);

	/**
	 * 
	 * getClassLeader:获取指定人员的分管副校长
	 *
	 * @author luoyibo
	 * @param userId
	 * @return String
	 * @throws @since
	 *             JDK 1.8
	 */
	public String getViceSchoolLeader(String userId);
}
