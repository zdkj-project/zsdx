package com.zd.school.oa.flow.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.BaseApplayEntity;
import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.jw.eduresources.service.JwGradeteacherService;
import com.zd.school.oa.flow.dao.BaseApplayDao;
import com.zd.school.oa.flow.service.ApplayService;

@Service
@Transactional
public class ApplayServiceImpl extends BaseServiceImpl<BaseApplayEntity> implements ApplayService {

	@Resource
	public void setBaseApplayDao(BaseApplayDao baseApplayDao) {
		this.dao = baseApplayDao;
	}

	@Resource
	private JwClassteacherService jwTClassteacherService;

	@Resource
	private JwGradeteacherService jwTGradeteacherService;

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
	public String getClassLeader(String userId) {
		String classLeader = jwTClassteacherService.getClassLeader(userId);

		return classLeader;
	}

	public String getClassLeaderList(String userId) {
		String classLeader = jwTClassteacherService.getClassLeaderList(userId);

		return classLeader;
	}

	public String getGradeLeader(String userId) {

		// TODO Auto-generated method stub
		return jwTGradeteacherService.getGradeLeader(userId);
	}

	public String getGradeLeaderList(String userId) {

		// TODO Auto-generated method stub
		return jwTGradeteacherService.getGradeLeaderList(userId);
	}

	public String getEduLeader(String userId) {
		String eduLeader = "AB6F1370-FE57-4703-B4EA-A7B31445193E";
		// TODO Auto-generated method stub
		return eduLeader;
	}

	public String getViceSchoolLeader(String userId) {
		String viceSchoolLeader = "5D543842-D874-4D71-AB89-8C48A6534F48";
		// TODO Auto-generated method stub
		return viceSchoolLeader;
	}
}
