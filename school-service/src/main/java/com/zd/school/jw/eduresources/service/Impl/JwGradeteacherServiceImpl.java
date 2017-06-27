package com.zd.school.jw.eduresources.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.dao.JwGradeteacherDao;
import com.zd.school.jw.eduresources.model.JwGradeteacher;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.service.JwGradeclassteacherService;
import com.zd.school.jw.eduresources.service.JwGradeteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.comm.model.CommBase;
import com.zd.school.plartform.comm.model.CommTree;
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
 * ClassName: JwGradeteacherServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 年级组长信息实体Service接口实现类. date: 2016-08-22
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwGradeteacherServiceImpl extends BaseServiceImpl<JwGradeteacher> implements JwGradeteacherService {

    @Resource
    public void setJwGradeteacherDao(JwGradeteacherDao dao) {
        this.dao = dao;
    }

    @Resource
    private BaseOrgService orgService;

    @Resource
    private BaseJobService jobService;

    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService roleService;

    @Resource
    private JwTGradeService gradeService;

    @Resource
    private JwGradeclassteacherService gcTeacherService;

    @Override
    public String getGradeLeader(String userId) {

        String gradeLeader = "";
        String sql = "EXECUTE JW_P_GETGRADETEACHER '" + userId + "'";

        List lists = this.dao.doQuerySql(sql);
        gradeLeader = lists.get(0).toString();

        return gradeLeader;
    }

    @Override
    public String getGradeLeaderList(String userId) {
        String gradeLeader = "";
        String sql = "EXECUTE JW_P_GETGRADETEACHER '" + userId + "'";

        try {
            List lists = this.dao.doQuerySql(sql);
            for (Object object : lists) {
                gradeLeader += object + ",";
            }
            gradeLeader = gradeLeader.substring(0, gradeLeader.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }

        return gradeLeader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<JwGradeteacher> getGradeTeacher(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, String graiId, SysUser currentUser) {
        String queryFilter = filter;
        String qrClassId = graiId;
        //当前用户有权限的年级列表
        QueryResult<JwTGrade> qr = gradeService.getGradeList(0, 0, "", "", true, currentUser);
        List<JwTGrade> jgClass = qr.getResultList();
        StringBuffer sb = new StringBuffer();
        if (jgClass.size() > 0) {
            for (JwTGrade jwTGrade : jgClass) {
                sb.append(jwTGrade.getUuid() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (StringUtils.isEmpty(graiId) || graiId.equals("2851655E-3390-4B80-B00C-52C7CA62CB39")) {
            //选择没有选择年级，使用有权限的所有年级
            qrClassId = sb.toString();
        }
        ExtDataFilter selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
                "{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"graiId\"}",
                ExtDataFilter.class);

        if (StringUtils.isNotEmpty(filter)) {
            List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                    ExtDataFilter.class);
            listFilters.add(selfFilter);

            queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters, false);
        } else {
            queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
                    + "\",\"field\":\"graiId\"}]";
        }
        QueryResult<JwGradeteacher> qrReturn = this.doPaginationQuery(start, limit, sort, queryFilter, true);
        return qrReturn;
    }

    @Override
    public JwGradeteacher doAddGradeTeacher(JwGradeteacher entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException {

/*        JwGradeteacher perEntity = new JwGradeteacher();
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
        //Integer i = entity.getStudyYeah() + 1;
        //String studyYeahname = entity.getStudyYeah().toString() + "-" + i.toString() + "学年";
        //gcTeacher.setStudyYeahname(studyYeahname);
        gcTeacher.setTeaType("0");
        gcTeacherService.persist(gcTeacher);

        //增加后要同步此人的岗位数据
        String teacherId = entity.getTteacId(); //教师ID
        String deptId = entity.getGraiId(); //年级ID,对应部门ID
        Integer studyYear = entity.getStudyYeah(); //学年
        String semester = entity.getSemester(); //学期
        if (studyYear.equals(currentUser.getStudyYear()) && semester.equals(currentUser.getSemester())) {
            //如果设置的是当前学年学期的
            SysUser user = userService.get(teacherId);
            Set<BaseJob> userJobs = user.getUserJobs();
            //设置默认岗位为年级组长
            BaseJob job = jobService.getByProerties("jobName", "年级组长");
            if (ModelUtil.isNotNull(job)) {
                userJobs.add(job);
                user.setUserJobs(userJobs);
            }
            //加入到对应的部门
            Set<BaseOrg> userDepts = user.getUserDepts();
            BaseOrg org = orgService.get(entity.getGraiId());
            if (ModelUtil.isNotNull(org)) {
                userDepts.add(org);
                user.setUserDepts(userDepts);
            }

            //设置增加默认的年级组长角色
            Set<SysRole> theUserRole = user.getSysRoles();
            SysRole role = roleService.getByProerties("roleCode", "GRADELEADER");
            if (ModelUtil.isNotNull(role)) {
                theUserRole.add(role);
                user.setSysRoles(theUserRole);
            }
            user.setDeptId(deptId);
            userService.merge(user);
        }*/
        // TODO Auto-generated method stub
        return entity;
    }

    @Override
    public Boolean doDelete(String delIds, SysUser currentUser) {
        Boolean result = false;
/*        String[] dels = delIds.split(",");
        List<JwGradeteacher> list = this.queryByProerties("uuid", dels);
        for (JwGradeteacher gt : list) {
            String teacherId = gt.getTteacId(); //教师ID
            Integer studyYear = gt.getStudyYeah(); //学年
            String semester = gt.getSemester(); //学期    
            if (studyYear.equals(currentUser.getStudyYear()) && semester.equals(currentUser.getSemester())) {
                SysUser user = userService.get(teacherId);

                //年级组长角色
                Set<SysRole> theUserRole = user.getSysRoles();
                SysRole role = roleService.getByProerties("roleCode", "GRADELEADER");
                if (ModelUtil.isNotNull(role)) {
                    theUserRole.remove(role);
                    user.setSysRoles(theUserRole);
                }
                //删除年级组长岗位
                Set<BaseJob> userJobs = user.getUserJobs();
                System.out.println("岗位数:" + userJobs.size());
                BaseJob job = jobService.getByProerties("jobName", "年级组长");
                if (ModelUtil.isNotNull(job)) {
                    userJobs.remove(job);
                    user.setUserJobs(userJobs);
                }
                //从对应年级部门删除
                Set<BaseOrg> userDepts = user.getUserDepts();
                System.out.println("部门数:" + userDepts.size());
                BaseOrg org = orgService.get(gt.getGraiId());
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
        }
        result = true;*/
        // TODO Auto-generated method stub
        return result;
    }

    @Override
    public List<CommTree> getGradeTree(String viewName, String whereSql, SysUser currentUser) {
        String sql = "";
        Map<String, JwTGrade> mapClass = new HashMap<String, JwTGrade>();
        //当前用户有权限的年级列表
        QueryResult<JwTGrade> qr = gradeService.getGradeList(0, 0, "", "", true, currentUser);
        List<JwTGrade> jgClass = qr.getResultList();
        for (JwTGrade jwTGradeclass : jgClass) {
            mapClass.put(jwTGradeclass.getUuid(), jwTGradeclass);
            //mapClass.put(jwTGradeclass.getGraiId(), jwTGradeclass);
        }
        Map<String, CommBase> mapBase = new HashMap<String, CommBase>();
        List<CommBase> romoeList = new ArrayList<CommBase>();
        sql = "select id,text,iconCls,leaf,level,parent from " + viewName + " where 1=1 " + whereSql;
        List<CommBase> lists = this.doQuerySqlObject(sql, CommBase.class);

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
                    fc.getLevel(), "", fc.getParent(),0, new ArrayList<CommTree>());

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
}