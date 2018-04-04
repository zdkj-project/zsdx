package com.zd.school.jw.eduresources.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.dao.JwClassteacherDao;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.jw.eduresources.service.JwGradeclassteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.comm.model.CommBase;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: JwClassteacherServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 班主任信息实体Service接口实现类. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwClassteacherServiceImpl extends BaseServiceImpl<JwClassteacher> implements JwClassteacherService {

    @Resource
    public void setJwClassteacherDao(JwClassteacherDao dao) {
        this.dao = dao;
    }

    @Resource
    private CommTreeService treeService; //通用树形数据获取service

    @Resource
    private BaseOrgService orgService;

    @Resource
    private BaseJobService jobService;

    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService roleService;

    @Resource
    private JwTGradeclassService classService;

    @Resource
    private JwTGradeService gradeService;

    @Resource
    private JwGradeclassteacherService gcTeacherService;

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
    @Override
    public String getClassLeader(String userId) {
        String classLeader = "";
        String sql = "EXECUTE JW_P_GETCLASSTEACHER '" + userId + "'";

        List lists = this.dao.doQuerySql(sql);
        classLeader = lists.get(0).toString();

        return classLeader;
    }

    @Override
    public String getClassLeaderList(String userId) {
        String classLeader = "";
        String sql = "EXECUTE JW_P_GETCLASSTEACHER '" + userId + "'";

        try {
            List lists = this.dao.doQuerySql(sql);
            for (Object object : lists) {
                classLeader += object + ",";
            }
            classLeader = classLeader.substring(0, classLeader.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            classLeader = "-1";
        }

        return classLeader;
    }

    /**
     * 
     * getGradeClasTree:获取班级的树形数据
     *
     * @author luoyibo
     * @param viewName
     *            获取数据的视图/表名
     * @param whereSql
     *            额外指定的查询语句
     * @param currentUser
     *            当前操作用户对象
     * @return List<CommTree>
     * @throws @since
     *             JDK 1.8
     */
    @Override
    public List<CommTree> getGradeClassTree(String viewName, String whereSql, SysUser currentUser) {
        String sql = "";
        Map<String, JwTGradeclass> mapClass = new HashMap<String, JwTGradeclass>();
        //当前用户有权限的班级列表
        QueryResult<JwTGradeclass> qr = classService.getGradeClassList(0, 0, "", "", true, currentUser);
        List<JwTGradeclass> jgClass = qr.getResultList();
        for (JwTGradeclass jwTGradeclass : jgClass) {
            mapClass.put(jwTGradeclass.getUuid(), jwTGradeclass);
            mapClass.put(jwTGradeclass.getGraiId(), jwTGradeclass);
        }
        Map<String, CommBase> mapBase = new HashMap<String, CommBase>();
        List<CommBase> romoeList = new ArrayList<CommBase>();
        sql = "select id,text,iconCls,leaf,level,parent from JW_V_GRADECLASSTREE where 1=1 " + whereSql;
        List<CommBase> lists = this.getQuerySqlObject(sql, CommBase.class);

        //for (JwTGradeclass jwTGradeclass : jgClass) {
        for (CommBase cb : lists) {
            if (!cb.getParent().equals("ROOT") && mapClass.get(cb.getId()) == null) {
                //&& mapClass.get(cb.getParent()) == null) {
                romoeList.add(cb);
            }
        }
        //}

        lists.removeAll(romoeList);

        List<CommTree> result = new ArrayList<CommTree>();

        // 构建Tree数据
        createTreeChild(new CommTree(TreeVeriable.ROOT, new ArrayList<CommTree>()), result, lists);
        return result;
    }

    private void createTreeChild(CommTree parentNode, List<CommTree> result, List<CommBase> list) {
        List<CommBase> childs = new ArrayList<CommBase>();
        for (CommBase dic : list) {
            if (dic.getParent().equals(parentNode.getId())) {
                childs.add(dic);
            }
        }

        for (CommBase fc : childs) {
            CommTree child = new CommTree(fc.getId(), fc.getText(), fc.getIconCls(), Boolean.parseBoolean(fc.getLeaf()),
                    fc.getLevel(), "", fc.getParent(),0, new ArrayList());

            if (fc.getParent().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<CommTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createTreeChild(child, result, list);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<JwClassteacher> getclassTeacher(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, String claiId, SysUser currentUser) {
        String queryFilter = filter;
        String qrClassId = claiId;
        //当前用户有权限的班级列表
        QueryResult<JwTGradeclass> qr = classService.getGradeClassList(0, 0, "", "", true, currentUser);
        List<JwTGradeclass> jgClass = qr.getResultList();
        StringBuffer sb = new StringBuffer();
        if (jgClass.size() > 0) {
            for (JwTGradeclass jwTGrade : jgClass) {
                sb.append(jwTGrade.getUuid() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (StringUtils.isEmpty(claiId) || claiId.equals("2851655E-3390-4B80-B00C-52C7CA62CB39")) {
            //选择没有选择年级，使用有权限的所有年级
            qrClassId = sb.toString();
        }
        ExtDataFilter selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
                "{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"claiId\"}",
                ExtDataFilter.class);

        if (StringUtils.isNotEmpty(filter)) {
            List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                    ExtDataFilter.class);
            listFilters.add(selfFilter);

            queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters, false);
        } else {
            queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
                    + "\",\"field\":\"claiId\"}]";
        }
        QueryResult<JwClassteacher> qrReturn = this.getPaginationQuery(start, limit, sort, queryFilter, true);
        return qrReturn;
    }

    @Override
    public JwClassteacher doAddClassTeacher(JwClassteacher entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException {

/*        JwClassteacher perEntity = new JwClassteacher();
        BeanUtils.copyPropertiesExceptNull(entity, perEntity);
        // 生成默认的orderindex
        Integer orderIndex = this.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);//排序
        //增加时要设置创建人
        entity.setCreateUser(currentUser.getXm()); //创建人
        //持久化到数据库
        entity = this.merge(entity);

        JwGradeclassteacher gcTeacher = new JwGradeclassteacher(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(gcTeacher, entity);
        //        Integer i = entity.getStudyYeah() + 1;
        //        String studyYeahname = entity.getStudyYeah().toString() + "-" + i.toString() + "学年";
        //        gcTeacher.setStudyYeahname(studyYeahname);
        gcTeacher.setTeaType("1");
        gcTeacher.setCategory(entity.getCategory() + 2);
        gcTeacher.setGraiId(entity.getClaiId());
        gcTeacherService.persist(gcTeacher);*/

        //增加后要同步此人的岗位数据
   /*     String teacherId = entity.getTteacId(); //教师ID
        String deptId = entity.getClaiId(); //班级ID,对应部门ID
        Integer studyYear = entity.getStudyYeah(); //学年
        String semester = entity.getSemester(); //学期*/
/*        if (studyYear.equals(currentUser.getStudyYear()) && semester.equals(currentUser.getSemester())) {
            //如果设置的是当前学年学期的
            SysUser user = userService.get(teacherId);
            //设置默认岗位为班主任
            Set<BaseJob> userJobs = user.getUserJobs();
            BaseJob job = jobService.getByProerties("jobName", "班主任");
            if (ModelUtil.isNotNull(job)) {
                //                user.setJobId(job.getUuid());
                //                user.setJobName(job.getJobName());
                userJobs.add(job);
                user.setUserJobs(userJobs);
                //userService.merge(user);
            }
            //设置增加默认的班主任角色
            Set<SysRole> theUserRole = user.getSysRoles();
            SysRole role = roleService.getByProerties("roleCode", "CLASSLEADER");
            if (ModelUtil.isNotNull(role)) {
                theUserRole.add(role);
                user.setSysRoles(theUserRole);
            }
            //加入到对应的部门
            Set<BaseOrg> userDepts = user.getUserDepts();
            BaseOrg org = orgService.get(entity.getClaiId());
            if (ModelUtil.isNotNull(org)) {
                userDepts.add(org);
                user.setUserDepts(userDepts);
            }
            user.setDeptId(deptId);*/
  /*          userService.merge(user);
        }*/
        // TODO Auto-generated method stub
        return entity;
    }

    @Override
    public Boolean doDelete(String delIds, SysUser currentUser) {
        Boolean result = false;
        String[] dels = delIds.split(",");
/*        List<JwClassteacher> list = this.queryByProerties("uuid", dels);
        for (JwClassteacher gt : list) {
            String teacherId = gt.getTteacId(); //教师ID
            Integer studyYear = gt.getStudyYeah(); //学年
            String semester = gt.getSemester(); //学期    
            if (studyYear.equals(currentUser.getStudyYear()) && semester.equals(currentUser.getSemester())) {
                SysUser user = userService.get(teacherId);
                Set<SysRole> theUserRole = user.getSysRoles();
                SysRole role = roleService.getByProerties("roleCode", "CLASSLEADER");
                if (ModelUtil.isNotNull(role)) {
                    theUserRole.remove(role);
                    user.setSysRoles(theUserRole);
                }
                //                user.setJobId("");
                //                user.setJobName("");
                //删除班主任岗位
                Set<BaseJob> userJobs = user.getUserJobs();
                //System.out.println("岗位数:" + userJobs.size());
                BaseJob job = jobService.getByProerties("jobName", "班主任");
                if (ModelUtil.isNotNull(job)) {
                    userJobs.remove(job);
                    user.setUserJobs(userJobs);
                }
                //从对应年级部门删除
                Set<BaseOrg> userDepts = user.getUserDepts();
                //System.out.println("部门数:" + userDepts.size());
                BaseOrg org = orgService.get(gt.getClaiId());
                if (ModelUtil.isNotNull(org)) {
                    userDepts.remove(org);
                    user.setUserDepts(userDepts);
                }
                user.setUpdateTime(new Date());
                user.setUpdateUser(currentUser.getXm());
                userService.merge(user);
            }
            gt.setIsDelete(1);
            gt.setUpdateTime(new Date());
            gt.setUpdateUser(currentUser.getXm());
            this.merge(gt);

            JwGradeclassteacher gcTeacher = gcTeacherService.get(gt.getUuid());
            gcTeacher.setIsDelete(1);
            gcTeacher.setUpdateTime(new Date());
            gcTeacher.setUpdateUser(currentUser.getXm());
            gcTeacherService.merge(gcTeacher);

        }*/
        result = true;
        return result;
    }

}