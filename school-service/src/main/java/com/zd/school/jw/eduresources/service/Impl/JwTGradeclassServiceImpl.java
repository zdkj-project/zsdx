package com.zd.school.jw.eduresources.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.dao.JwTGradeclassDao;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.model.JwGradeteacher;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.jw.eduresources.service.JwGradeteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwTGradeclassServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 学校班级信息实体Service接口实现类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwTGradeclassServiceImpl extends BaseServiceImpl<JwTGradeclass> implements JwTGradeclassService {

	@Resource
	public void setJwTGradeclassDao(JwTGradeclassDao dao) {
		this.dao = dao;
	}

	@Resource
	private JwTGradeService gradeService;

	@Resource
	private JwGradeteacherService gradeTeaService;

	@Resource
	private JwClassteacherService classTTeaService;

	/**
	 * 根据班级ID得到年级对象
	 * 
	 * @param claiId
	 * @return
	 * @author huangzc
	 */
	@Override
	public JwTGrade findJwTGradeByClaiId(String claiId) {
		JwTGradeclass jtgClass = this.dao.getByProerties("uuid", claiId);
		if (jtgClass == null)
			return null;
		JwTGrade grade = gradeService.get(jtgClass.getGraiId());

		if (ModelUtil.isNotNull(grade))
			return grade;
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<JwTGradeclass> getGradeClassList(Integer start, Integer limit, String sort, String filter,
			Boolean isDelete, SysUser currentUser) {
		String queryFilter = filter;
		// String jobId = currentUser.getJobId();
		// String jobName = currentUser.getJobName();
		Integer studyYear = currentUser.getStudyYear();
		String smester = currentUser.getSemester();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbClass = new StringBuffer();
		String qrClassId = "";
		ExtDataFilter selfFilter = new ExtDataFilter();
		String propName[] = { "studyYeah", "semester", "tteacId", "isDelete" };
		Object[] propValue = { studyYear, smester, currentUser.getUuid(), 0 };
		// 当前人是否年级组长,如果是年级组长则取年级下的所有班级
		List<JwGradeteacher> gt = gradeTeaService.queryByProerties(propName, propValue);
		for (JwGradeteacher jgt : gt) {
			sb.append(jgt.getGraiId() + ",");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			String st = sb.toString();
			List<JwTGradeclass> gcList = this.queryByProerties("graiId", st);
			for (JwTGradeclass gc : gcList) {
				sbClass.append(gc.getUuid() + ",");
			}
		}
		// 当前人是否班主任，如是则取所在的班级
		List<JwClassteacher> jct = classTTeaService.queryByProerties(propName, propValue);
		if (jct.size() > 0) {
			for (JwClassteacher jt : jct) {
				sbClass.append(jt.getClaiId() + ",");
			}
		}
		if (sbClass.length() > 0) {
			sbClass.deleteCharAt(sbClass.length() - 1);
		}
		qrClassId = sbClass.toString();
		selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
				"{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"uuid\"}",
				ExtDataFilter.class);
		if (StringUtils.isNotEmpty(filter)) {
			if (StringUtils.isNotEmpty(qrClassId)) {
				List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
						ExtDataFilter.class);
				listFilters.add(selfFilter);

				queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters,
						false);
			}
		} else {
			if (StringUtils.isNotEmpty(qrClassId))
				queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
						+ "\",\"field\":\"uuid\"}]";
		}
		// if (currentUser.getUserName().equals("schooladmin"))
		// queryFilter = "";
		QueryResult<JwTGradeclass> qr = this.getPaginationQuery(start, limit, sort, queryFilter, true);
		return qr;
	}

	@Override
	public QueryResult<JwTGradeclass> getGradeClassList(Integer start, Integer limit, String sort, String filter,
			Boolean isDelete, SysUser currentUser, String claiId, String claiLevel) {

		String queryFilter = "";
		StringBuffer sbClass = new StringBuffer();
		List<JwTGradeclass> gcList = null;
		switch (claiLevel) {
		case "1": // 查询学校
			gcList = this.queryByProerties("isDelete", 0);
			for (JwTGradeclass gc : gcList) {
				sbClass.append(gc.getUuid() + ",");
			}
			if (sbClass.length() > 0) {
				sbClass.deleteCharAt(sbClass.length() - 1);
				queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + sbClass.toString()
						+ "\",\"field\":\"uuid\"}]";
			}
			break;
		case "2": // 查询年级
			gcList = this.queryByProerties("graiId", claiId);
			for (JwTGradeclass gc : gcList) {
				sbClass.append(gc.getUuid() + ",");
			}
			if (sbClass.length() > 0) {
				sbClass.deleteCharAt(sbClass.length() - 1);
				queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + sbClass.toString()
						+ "\",\"field\":\"uuid\"}]";
			}
			break;
		case "3": // 查询班级
			queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + claiId
					+ "\",\"field\":\"uuid\"}]";
			break;
		}
		QueryResult<JwTGradeclass> qr = this.getGradeClassList(start, limit, sort, queryFilter, true, currentUser);

		return qr;
	}
}