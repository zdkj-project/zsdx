package com.zd.school.teacher.teacherinfo.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.dao.TeaTeacherbaseDao;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: TeaTeacherbaseServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 教职工基本数据实体Service接口实现类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TeaTeacherbaseServiceImpl extends BaseServiceImpl<TeaTeacherbase> implements TeaTeacherbaseService {

    @Resource
    public void setTeaTeacherbaseDao(TeaTeacherbaseDao dao) {
        this.dao = dao;
    }

    @Resource
    private BaseOrgService orgService; // 部门服务层

    @Resource
    private BaseJobService jobService;

    @Override
    public QueryResult<TeaTeacherbase> getDeptTeacher(Integer start, Integer limit, String sort, String filter,
                                                      String whereSql, String orderSql, String querySql, Boolean isDelete, String deptId, SysUser currentUser) {
        String deptIds = "";
        StringBuffer hql = new StringBuffer();
        StringBuffer countHql = new StringBuffer();
        String filterHql = "";
        if (StringUtils.isNotEmpty(filter)) {
            filterHql = StringUtils.convertFilterToSql(filter);
        }
        if (StringUtils.isEmpty(deptId)) {
            // rightDept = orgService.getOrgList("", "", currentUser);
        } else {
            BaseOrg org = orgService.get(deptId);
            deptIds = org.getTreeIds(); // 指定部门及其子部门
            hql.append(
                    "from TeaTeacherbase as o inner join fetch o.userDepts as r where o.isDelete=0 and  r.treeIds like '" + deptIds + "%' ");
            hql.append(whereSql);
            hql.append(filterHql);
            hql.append(" order by o.jobCode ");

        }
        if (hql.length() > 0) {
            QueryResult<TeaTeacherbase> qr = this.doQueryResult(hql.toString(), start, limit);
            Set<TeaTeacherbase> tt = new HashSet<TeaTeacherbase>();
            tt.addAll(qr.getResultList());
            qr.getResultList().clear();
            qr.getResultList().addAll(tt);
            return qr;
            //return this.setTeacherJobAndDept(qr);
        } else {
            return null;
        }
    }


    public QueryResult<TeaTeacherbase> getDeptTeacher(Integer start, Integer limit, String whereSql) {

        StringBuffer hql = new StringBuffer();
        hql.append(
                "from TeaTeacherbase as o where o.isDelete=0 ");
        hql.append(whereSql);
        hql.append(" order by o.jobCode ");
        if (hql.length() > 0) {
            QueryResult<TeaTeacherbase> qr = this.doQueryResult(hql.toString(), start, limit);

            return qr;
        } else {
            return null;
        }
    }


    @Override
    public Boolean batchSetDept(String deptId, String userIds, SysUser currentUser) {
        Boolean reResult = false;
/*        String[] delId = userIds.split(",");
        List<TeaTeacherbase> list = this.queryByProerties("uuid", delId);
        for (TeaTeacherbase teacher : list) {
            Set<BaseOrg> userDept = teacher.getUserDepts();
            BaseOrg org = orgService.get(deptId);
            BaseOrg tempOrg = orgService.get("058b21fe-b37f-41c9-ad71-091f97201ff8");
            userDept.remove(tempOrg);
            userDept.add(org);

            teacher.setUserDepts(userDept);
            teacher.setUpdateTime(new Date());
            teacher.setUpdateUser(currentUser.getXm());

            this.merge(teacher);
            reResult = true;
        }*/
        return reResult;
    }

    @Override
    public Boolean delTeaFromDept(String deptId, String userIds, SysUser currentUser) {
        Boolean reResult = false;
/*        String[] delId = userIds.split(",");
        List<BaseOrg> all = orgService.getOrgAndChildList(deptId, "", currentUser, false);
        for (String id : delId) {
            String hql = "from TeaTeacherbase as u inner join fetch u.userDepts as d where u.uuid='" + id
                    + "' and d.isDelete=0";
            TeaTeacherbase teahcher = this.doQuery(hql).get(0);

            Set<BaseOrg> userDept = teahcher.getUserDepts();
            userDept.removeAll(all);
            teahcher.setUserDepts(userDept);
            teahcher.setUpdateTime(new Date());
            teahcher.setUpdateUser(currentUser.getXm());
            this.merge(teahcher);
            reResult = true;
        }*/
        return reResult;
    }

    @Override
    public Boolean setTeaToJob(String jobId, String userIds, SysUser currentUser) {
        Boolean reResult = false;
/*        String[] delId = userIds.split(",");
        String[] jobIds = jobId.split(",");
        Map<String, String> sortedCondition = new HashMap<String, String>();
        sortedCondition.put("jobCode", "ASC");

        List<BaseJob> listJob = jobService.queryByProerties("uuid", jobIds, sortedCondition);
        List<TeaTeacherbase> list = this.queryByProerties("uuid", delId);
        for (TeaTeacherbase teacher : list) {
            Set<BaseJob> userJob = teacher.getUserJobs();
            for (BaseJob job : listJob) {
                userJob.add(job);
            }
            teacher.setJobId(listJob.get(0).getUuid());
            teacher.setJobCode(listJob.get(0).getJobCode());
            teacher.setUserJobs(userJob);
            teacher.setUpdateTime(new Date());
            teacher.setUpdateUser(currentUser.getXm());

            this.merge(teacher);
            reResult = true;
        }*/
        return reResult;
    }

    @Override
    public Boolean delTeaFromJob(String jobId, String userIds, SysUser currentUser) {
        Boolean reResult = false;
/*        String[] delId = userIds.split(",");
        Object[] propValue = jobId.split(",");
        Map<String, String> sortedCondition = new HashMap<String, String>();
        sortedCondition.put("jobCode", "ASC");

        List<BaseJob> all = jobService.queryByProerties("uuid", propValue, sortedCondition);
        for (String id : delId) {
            String hql = "from TeaTeacherbase as u inner join fetch u.userJobs as d where u.uuid='" + id
                    + "' and d.isDelete=0";
            TeaTeacherbase teahcher = this.doQuery(hql).get(0);

            Set<BaseJob> userJob = teahcher.getUserJobs();
            //删除要解除绑定的岗位
            userJob.removeAll(all);

            //将剩下的岗位按jobCode排序后将第一个设置为主岗位
            List<BaseJob> listTemp = new ArrayList<BaseJob>();
            listTemp.addAll(userJob);
            SortListUtil<BaseJob> sortJob = new SortListUtil<BaseJob>();
            sortJob.Sort(listTemp, "jobCode", "");
            teahcher.setJobId(listTemp.get(0).getUuid());
            teahcher.setJobCode(listTemp.get(0).getJobCode());

            teahcher.setUserJobs(userJob);
            teahcher.setUpdateTime(new Date());
            teahcher.setUpdateUser(currentUser.getXm());
            this.merge(teahcher);
            reResult = true;
        }*/
        return reResult;
    }

    @Override
    public QueryResult<BaseJob> getTeahcerJobList(TeaTeacherbase teahcher, SysUser currentUser) {
/*        String hql = "from SysUser as u inner join fetch u.userJobs as r where u.uuid='" + teahcher.getUuid()
                + "' and r.isDelete=0 ";
        List<TeaTeacherbase> list = this.doQuery(hql);
        Set<BaseJob> userJobs = list.get(0).getUserJobs();
        // Set<BaseJob> userJobs = teahcher.getUserJobs();
        if (userJobs.size() > 0) {
            List<BaseJob> listJob = new ArrayList<BaseJob>();
            for (BaseJob baseJob : userJobs) {
                listJob.add(baseJob);
            }
            SortListUtil<BaseJob> sortJob = new SortListUtil<BaseJob>();
            sortJob.Sort(listJob, "jobCode", "");
            QueryResult<BaseJob> qr = new QueryResult<BaseJob>();
            qr.setResultList(listJob);
            qr.setTotalCount((long) listJob.size());

            return qr;
        } else*/
            return null;
    }

    @Override
    public String getTeacherJobs(TeaTeacherbase teacher) {
/*        String hql = "from SysUser as u inner join fetch u.userJobs as r where u.uuid='" + teacher.getUuid()
                + "' and r.isDelete=0 ";
        List<TeaTeacherbase> listTeachar = this.doQuery(hql);
        Set<BaseJob> userJobs = listTeachar.get(0).getUserJobs();
        // Set<BaseJob> userJobs = teahcher.getUserJobs();
        List<BaseJob> list = new ArrayList<>();
        list.addAll(userJobs);
        SortListUtil<BaseJob> sortJob = new SortListUtil<BaseJob>();
        sortJob.Sort(list, "jobCode", "");
        StringBuffer sbJobId = new StringBuffer();
        StringBuffer sbJobName = new StringBuffer();
        for (BaseJob baseJob : list) {
            sbJobId.append(baseJob.getUuid() + "|");
            sbJobName.append(baseJob.getJobName() + "|");
        }
        String jobIds = StringUtils.trimLast(sbJobId.toString());
        String jobNames = StringUtils.trimLast(sbJobName.toString());
        if (jobIds.length() > 0)
            return jobIds + "," + jobNames;
        else
            return ",";*/
        return  "";
    }

    @Override
    public String getTeacherDepts(TeaTeacherbase teacher) {
/*		Set<BaseOrg> userDepts = teacher.getUserDepts();
        List<BaseOrg> list = new ArrayList<>();
		list.addAll(userDepts);
		SortListUtil<BaseOrg> sortJob = new SortListUtil<BaseOrg>();
		sortJob.Sort(list, "jobCode", "");
		StringBuffer sbDeptId = new StringBuffer();
		StringBuffer sbDeptName = new StringBuffer();
		for (BaseOrg baseOrg : list) {
			sbDeptId.append(baseOrg.getUuid() + "|");
			sbDeptName.append(baseOrg.getNodeText() + "|");
		}
		String deptIds = StringUtils.trimLast(sbDeptId.toString());
		String deptNames = StringUtils.trimLast(sbDeptName.toString());
		if (deptNames.length() > 0)
			return deptIds + "," + deptNames;
		else
			return ",";*/
        return "";
    }

    @Override
    public QueryResult<TeaTeacherbase> getCourseTeacherlist(Integer start, Integer limit, String sort, String filter,
                                                            String whereSql, String orderSql, String querySql, Boolean isDelete) {
        QueryResult<TeaTeacherbase> qr = this.doPaginationQuery(start, limit, sort, filter, true);

        return this.setTeacherJobAndDept(qr);
    }

    public QueryResult<TeaTeacherbase> setTeacherJobAndDept(QueryResult<TeaTeacherbase> qr) {
        QueryResult<TeaTeacherbase> qrr = new QueryResult<TeaTeacherbase>();
        List<TeaTeacherbase> newList = new ArrayList<TeaTeacherbase>();
/*		for (TeaTeacherbase teaTeacherbase : qr.getResultList()) {
			String jobInfo = this.getTeacherJobs(teaTeacherbase);
			String[] strings = jobInfo.split(",");
			teaTeacherbase.setAllJobId(strings[0]);
			teaTeacherbase.setAllJobName(strings[1]);

			String deptInfo = this.getTeacherDepts(teaTeacherbase);
			strings = deptInfo.split(",");
			teaTeacherbase.setDeptName(strings[1]);

			newList.add(teaTeacherbase);
		}
		qrr.setResultList(newList);
		qrr.setTotalCount(qr.getTotalCount());	*/

        return qrr;
    }
}