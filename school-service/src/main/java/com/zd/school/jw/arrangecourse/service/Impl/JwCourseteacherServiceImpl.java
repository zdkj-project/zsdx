package com.zd.school.jw.arrangecourse.service.Impl;

import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.arrangecourse.dao.JwCourseteacherDao;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher;
import com.zd.school.jw.arrangecourse.service.JwCourseteacherService;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 
 * ClassName: JwCourseteacherServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 教师任课信息(JW_T_COURSETEACHER)实体Service接口实现类.
 * date: 2016-08-26
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwCourseteacherServiceImpl extends BaseServiceImpl<JwCourseteacher> implements JwCourseteacherService {

    @Resource
    public void setJwCourseteacherDao(JwCourseteacherDao dao) {
        this.dao = dao;
    }

    @Resource
    private BaseOrgService orgService;

    @Resource
    private SysUserService userService;
    
    @Resource
    private JwTGradeclassService gradeClassService;
    
    @Resource 
    private TeaTeacherbaseService teacherService;    
    
    /**
     * 
     * doAddCourseTeacher:设置任课教师.
     * 
     * @author luoyibo
     * @param studyYeah
     *            任课学年
     * @param semester
     *            任课学期
     * @param jsonData
     *            需要设置的教师数据
     * @param removeIds
     *            要移除的教师数据
     * @param currentUser
     *            当前操作者
     * @return String
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws @since
     *             JDK 1.8
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean doAddCourseTeacher(Integer studyYeah, String semester, String jsonData, String removeIds,
            SysUser currentUser) throws IllegalAccessException, InvocationTargetException {
        Boolean strData = false;

/*        try {
            List<JwCourseteacher> addList = (List<JwCourseteacher>) JsonBuilder.getInstance().fromJsonArray(jsonData,
                    JwCourseteacher.class);
            for (JwCourseteacher addTeacher : addList) {
                JwCourseteacher saveEntity = new JwCourseteacher();
                BeanUtils.copyPropertiesExceptNull(addTeacher, saveEntity);
                addTeacher.setOrderIndex(0);//排序
                //增加时要设置创建人
                addTeacher.setCreateUser(currentUser.getXm()); //创建人
                //持久化到数据库
                this.merge(addTeacher);

                //根据设置的班级和课程来处理教师所在的部门
                //加入到班级对应的部门
                SysUser user = userService.get(addTeacher.getTteacId());
                Set<BaseOrg> classDept = user.getUserDepts();
                BaseOrg org = orgService.get(addTeacher.getClaiId());
                if (ModelUtil.isNotNull(org)) {
                    classDept.add(org);
                }

                //加入到科目对应的部门
                BaseOrg couseDept = this.getCourseDept(addTeacher.getClaiId(), addTeacher.getCourseId());
                if (ModelUtil.isNotNull(couseDept)) {
                    classDept.add(couseDept);
                    user.setUserDepts(classDept);
                }
                user.setUpdateTime(new Date());
                user.setUpdateUser(currentUser.getXm());
                userService.merge(user);
            }

            if (StringUtils.isEmpty(removeIds)) {
                //writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
                //return;
            } else {
                this.logicDelOrRestore(removeIds, StatuVeriable.ISDELETE);
            }

            strData = true;

        } catch (Exception e) {
            strData = false;
        }*/

        return strData;
    }

    public BaseOrg getCourseDept(String classId, String courseId) {
        BaseOrg couseDept = null;
        BaseOrg classDept = orgService.get(classId);
        String[] classDeptTreIds = classDept.getTreeIds().split(",");
        List<BaseOrg> courseDeptList = orgService.queryByProerties("extField01", courseId);
        for (BaseOrg baseOrg : courseDeptList) {
            String[] treeIds = baseOrg.getTreeIds().split(",");
            if (classDeptTreIds[1].equals(treeIds[1])) {
                couseDept = baseOrg;
                break;
            }
        }
        return couseDept;
    }

    @Override
    public Boolean doDelCourseTeacher(String delIds, SysUser currentUser) {
        Boolean reResult = false;
        String[] idStrings = delIds.split(",");

        return reResult;

    }

	@Override
	public QueryResult<JwCourseteacher> getClassCourseTeacherList(Integer start, Integer limit, String sort,
			String filter, Boolean isDelete, String claiId, Integer claiLevel) {
        String queryFilter = filter;
        String qrClassId = "";
        ExtDataFilter selfFilter = new ExtDataFilter();
        StringBuffer sbClass = new StringBuffer();
        //指定了班级或年级
        if (StringUtils.isNotEmpty(claiId)) {
            switch (claiLevel) {
            case 1:
                //为1级是查询所有的数据
                break;
            case 2:
                //为2级是查询年级及年级下的班级的数据
                List<JwTGradeclass> classLists = gradeClassService.queryByProerties("graiId", claiId);
                for (JwTGradeclass gc : classLists) {
                    sbClass.append(gc.getUuid() + ",");
                }
                sbClass.append(claiId);
                qrClassId = sbClass.toString();
                break;
            case 3:
                //是3级，查询班级的数据
                qrClassId = claiId;
                break;
            default:
                break;
            }
        }
        //如果要根据指定的ID过滤
        if (StringUtils.isNotEmpty(qrClassId)) {
            //组装指定的过滤条件
            selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
                    "{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"claiId\"}",
                    ExtDataFilter.class);
            //检查有没有外部传入的过滤条件
            if (StringUtils.isNotEmpty(filter)) {
                //有外部传入的条件，要合并上去
                List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                        ExtDataFilter.class);
                listFilters.add(selfFilter);

                queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters,
                        false);
            } else {
                queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
                        + "\",\"field\":\"claiId\"}]";
            }
        }
        QueryResult<JwCourseteacher> qr = this.doPaginationQuery(start, limit, sort, queryFilter, true);
//        QueryResult<JwCourseteacher> teacherList = new QueryResult<TeaTeacherbase>();
//        List<TeaTeacherbase> newList = new ArrayList<TeaTeacherbase>();
//        for (JwCourseteacher t : qr.getResultList()) {
//			TeaTeacherbase teacherbase = teacherService.get(t.getTteacId());
//			String jobInfo = teacherService.getTeacherJobs(teacherbase);
//			String[] strings = jobInfo.split(",");
//			teacherbase.setJobId(strings[0]);
//			teacherbase.setJobName(strings[1]);
//			
//			String deptInfo = teacherService.getTeacherDepts(teacherbase);
//			strings = deptInfo.split(",");
//			teacherbase.setDeptName(strings[1]);	
//			newList.add(teacherbase);			
//		}
//        teacherList.setResultList(newList);
//        teacherList.setTotalCount(qr.getTotalCount());
        return qr;
	}
	
	@Override
	public String updateZjsByClassId(String classid,String courseid, int zjs) {
		JwTGrade grade =gradeClassService.findJwTGradeByClaiId(classid);
		String hql ="update JwCourseteacher ct set ct.acszjs="+zjs+" where "
				+ " ct.claiId in( select gc.uuid from JwTGradeclass gc where gc.graiId"
				+ " in (select uuid from JwTGrade  where sectionCode='"+grade.getSectionCode()+"' )) and  ct.courseId='"+courseid+"' ";
		executeHql(hql);
		return null;
	}
}