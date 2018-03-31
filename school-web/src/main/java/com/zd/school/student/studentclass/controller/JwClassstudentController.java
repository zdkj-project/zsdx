
package com.zd.school.student.studentclass.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.JwClassRoomAllot;
import com.zd.school.build.allot.service.JwClassRoomAllotService;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.plartform.comm.model.UpGradeRule;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.model.StuDivideparam;
import com.zd.school.student.studentclass.model.StuDividerecord;
import com.zd.school.student.studentclass.service.JwClassstudentService;
import com.zd.school.student.studentclass.service.StuDivideparamService;
import com.zd.school.student.studentclass.service.StuDividerecordService;
import com.zd.school.student.studentclass.service.StuDividescoreService;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;

/**
 * 
 * ClassName: JwClassstudentController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 学生分班信息(JW_T_CLASSSTUDENT)实体Controller. date:
 * 2016-08-25
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwClassstudent")
public class JwClassstudentController extends FrameWorkController<JwClassstudent> implements Constant {

    @Resource
    JwClassstudentService thisService; // service层接口

    @Resource
    CommTreeService commTreeService;

    @Resource
    PushInfoService pushService;

    @Resource
    JwClassRoomAllotService roomaService; // 班级分配教室

    @Resource
    JwClassteacherService cltService;// 班主任

    @Resource
    StuDividescoreService stuScoreService;// 学生分数

    @Resource
    JwTGradeclassService jwTGradeclassService;// 班级

    @Resource
    StuBaseinfoService stuBaseinfoService;// 学生信息

    @Resource
    JwTGradeclassService gcService;// 班级

    @Resource
    StuDivideparamService stuDivideparamService;// 分班条件

    @Resource
    StuDividerecordService stuDividerecordService;// 分班记录

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute JwClassstudent entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String claiId = request.getParameter("claiId");
        SysUser currentUser = getCurrentSysUser();
        QueryResult<JwClassstudent> qr = thisService.getclassStudent(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true, claiId, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 用于分配门禁权限时选择学生
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/mjList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void mjList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        String claiId = request.getParameter("claiId");
        SysUser currentUser = getCurrentSysUser();
        QueryResult<JwClassstudent> qr = thisService.getclassStudent(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true, claiId, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 查询不在学生分班表中的学生
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/stuNotAllotlist" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void stuNotAllotlist(@ModelAttribute StuBaseinfo entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        int start = super.start(request); // 起始记录数
        int limit = entity.getLimit();// 每页记录数
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<StuBaseinfo> lists = stuBaseinfoService.getQuery(hql.toString(), start, limit);// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param JwClassstudent
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwClassstudent entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String[] stuId = entity.getStudentId().split(",");
        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        StuBaseinfo stuBaseinfo = null;
        for (int i = 0; i < stuId.length; i++) {
            StuBaseinfo stu = stuBaseinfoService.get(stuId[i]);
            if (stu == null) {
                writeJSON(response, jsonBuilder.returnFailureJson("'该学生未存在学生信息表，无法继续。'"));
                return;
            }
            boolean flag = thisService.IsFieldExist("studentId", stu.getUuid(), "-1", "isDelete=0");
            if (flag) {
                writeJSON(response, jsonBuilder.returnFailureJson("'" + stu.getXm() + "已经存在。'"));
                return;
            }
            Integer orderIndex = thisService.getDefaultOrderIndex(entity);

            stuBaseinfo = stuBaseinfoService.get(stuId[i]);
            stuBaseinfo.setClassId(entity.getClaiId());
            JwClassstudent perEntity = new JwClassstudent();
            BeanUtils.copyPropertiesExceptNull(entity, perEntity);
            entity.setUserNumb(stuBaseinfo.getUserNumb()); // 学号
            entity.setXm(stuBaseinfo.getXm());// 姓名
            entity.setXbm(stuBaseinfo.getXbm());// 性别码
            entity.setSemester(currentUser.getSemester()); // 学期
            entity.setStudyYeah(currentUser.getStudyYear().toString()); // 学年
            entity.setStudentId(stuId[i]); // 设置学生id
            entity.setOrderIndex(orderIndex);// 排序
            // 增加时要设置创建人
            entity.setCreateUser(userCh); // 创建人
            // 持久化到数据库
            thisService.doMerge(entity);
            stuBaseinfoService.doMerge(stuBaseinfo);
        }
        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * doRestore还原删除的记录 @Title: doRestore @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * JwClassstudent @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(JwClassstudent entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
        JwClassstudent perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        entity = thisService.doMerge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

    /**
     * 开始分班
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/yesStuFb")
    public void yesStuFb(JwClassstudent entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        StuDividerecord cord = stuDividerecordService.getByProerties("isDelete", 0);
        if (cord == null) {
            writeJSON(response, jsonBuilder.returnFailureJson("'您已经分班成功了，请勿重复该操作。'"));
            return;
        }
        String divideId = cord.getUuid();
        int countrs = 0;
        for (int i = 1; i <= 3; i++) {
            int divideType = i;
            for (int j = 1; j <= 3; j++) {
                int divideLevel = j;
                String sql = "EXEC JW_T_CLASSSTUDENT_YI '" + divideId + "','" + userCh + "','" + divideLevel + "','"
                        + divideType + "', '" + countrs + "'";
                List retuValue = new ArrayList<>();
                retuValue = thisService.getQuerySql(sql);
                countrs = 0;
                countrs = countrs + Integer.valueOf(retuValue.get(0).toString());
                if (j == 3) {
                    countrs = 0;
                }
            }
        }
        cord = stuDividerecordService.get(divideId);
        cord.setIsDelete(1);
        stuDividerecordService.doMerge(cord);
        String[] str = { "divideId", "isDelete" };
        Object[] obj = { divideId, 0 };

        List<StuDivideparam> aram = stuDivideparamService.queryByProerties(str, obj);
        if (aram != null)
            for (int i = 0; i < aram.size(); i++) {
                aram.get(i).setIsDelete(1);
                stuDivideparamService.doMerge(aram.get(i));
            }
        thisService.doExecuteSql("EXEC JW_T_CLASSSCORE_VIEW");
        writeJSON(response, jsonBuilder.returnSuccessJson("'分班成功。'"));

    }

    /**
     * 查询升级规则列表
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/uprulelist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getUpGradeRuleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        List<UpGradeRule> lists = commTreeService.getUpGradeRuleList();
        strData = jsonBuilder.buildObjListToJson((long) lists.size(), lists, false);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 班级升级
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/gridUpgrade")
    public void gridUpgrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        SysUser currentUser = getCurrentSysUser();

        String sql = "EXECUTE JW_P_UPGRADESTUDENT 3,3,'" + currentUser.getXm() + "'";
        List lists = thisService.getQuerySql(sql);
        strData = lists.get(0).toString();

        writeJSON(response, jsonBuilder.returnSuccessJson("'" + strData + "'"));
    }

    /**
     * 保存分班条件
     * 
     * @param claiId
     * @param rs
     * @param bs
     * @param request
     * @param response
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("/classStuFb")
    public void classStuFb(String claiId, String rs, String bs, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 获取当前操作用户
        String userCh = "超级管理员";

        boolean flag = stuDivideparamService.IsFieldExist("isDelete", "0", "-1");
        if (flag) {
            writeJSON(response, jsonBuilder.returnFailureJson("'数据库中已有记录，请勿重复操作，您可以直接操作下一步。'"));
            return;
        }
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String[] str = rs.split(","); // 获取人数
        String[] claiIds = claiId.split("\\|");// 班级id
        String[] s = null; // 用于接收多个班级的id
        String[] bjbs = bs.split("\\|");// 班级标识
        String[] clbs = null;// 处理后的班级标识
        StuDivideparam divideparam = null;
        // 将记录插入到分班记录表
        StuDividerecord stucode = new StuDividerecord();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String date = format.format(new Date());
        String schoolYear = date.substring(0, 4); // 学年
        String semester = date.substring(4, 6); // 学期
        String xxq = ""; // 下学期所需要的学年
        int xq = 0;
        if (Integer.valueOf(semester) >= 3 && Integer.valueOf(semester) < 9) { // 如果年份小于或者等于3，那么可以判断是下学期
            xxq = Integer.valueOf(schoolYear) - 1 + "-" + schoolYear + ""; // 那么学年的结果为：2016-2017
            xq = 1;// 下学期
        }
        if (Integer.valueOf(semester) >= 9 || Integer.valueOf(semester) < 3) {
            xxq = schoolYear;
            xq = 2;// 上学期
        }
        stucode.setSchoolYear(xxq); // 学年
        stucode.setSemester(String.valueOf(xq)); // 学期
        stucode.setState("1");
        stucode.setJustStep(1);
        stucode.setCreateUser(userCh);
        stucode.setCreateTime(new Date());
        stucode.setDivideTitle("1");
        stucode = stuDividerecordService.doMerge(stucode);
        for (int i = 0; i < str.length; i++) {
            s = claiIds[i].split(",");// 多个班级
            clbs = bjbs[i].split(",");// 处理后的班级标识
            if (s.length >= 2) {
                for (int j = 0; j < s.length; j++) {
                    divideparam = new StuDivideparam();
                    divideparam.setClaiId(s[j]);// 班级id
                    divideparam.setDivideId(stucode.getUuid());// 班级列标识
                    divideparam.setDivideType(clbs[0]);// 班级类型
                    divideparam.setDivideLevel(Integer.valueOf(clbs[1]));// 班级等级
                    divideparam.setDivideCount(Integer.valueOf(str[i]));// 班级人数
                    divideparam.setOrderIndex(j + 1);// 班级的班序
                    divideparam.setCreateUser(userCh);// 创建人
                    divideparam.setCreateTime(new Date());// 创建时间
                    stuDivideparamService.doMerge(divideparam);
                }
            } else {
                divideparam = new StuDivideparam();
                divideparam.setClaiId(s[0]);// 班级id
                divideparam.setDivideId(stucode.getUuid());// 班级列标识
                divideparam.setDivideType(clbs[0]);// 班级类型
                divideparam.setDivideLevel(Integer.valueOf(clbs[1]));// 班级等级
                divideparam.setDivideCount(Integer.valueOf(str[i]));// 班级人数
                divideparam.setOrderIndex(1);// 班级的班序
                divideparam.setCreateUser(userCh);// 创建人
                divideparam.setCreateTime(new Date());// 创建时间
                stuDivideparamService.doMerge(divideparam);
            }
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'保存本页内容成功'"));
    }

    /**
     * 分班出错处理
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/catchDispose")
    public void catchDispose(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sql = "exec CLASSSTUCATCHDISPOSE";
        stuScoreService.doExecuteSql(sql);
        writeJSON(response, jsonBuilder.returnSuccessJson("'处理成功'"));
    }

    /**
     * 学生换班
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/changeShifts")
    public void changeShifts(JwClassstudent entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        JwTGradeclass jwTGradeclass = jwTGradeclassService.get(entity.getClaiId());
        if (!entity.getGradeCode().equals(jwTGradeclass.getGradeCode())) {
            writeJSON(response, jsonBuilder.returnFailureJson("'只能跟换到相同年级的班。'"));
            return;
        }
        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String[] idStrings = entity.getStudentId().split(",");
        String[] cStrings = entity.getUuid().split(",");
        StuBaseinfo stuBaseinfo = null;
        for (int i = 0; i < idStrings.length; i++) {
            JwClassstudent jwt = thisService.get(cStrings[i]); // 获取之前所在班级
            if (jwt.getClaiId().equals(entity.getClaiId())) { // 判断是否与选中班级相等
                writeJSON(response, jsonBuilder.returnFailureJson("'无法选择已在班级换班。'"));
                return;
            }
            Integer orderIndex = thisService.getDefaultOrderIndex(entity);// 顺序号
            stuBaseinfo = stuBaseinfoService.get(idStrings[i]);// 学生信息
            //entity.setUserNumb(stuBaseinfo.getUserNumb()); // 学号
            entity.setUserNumb(stuBaseinfo.getUserNumb());
            entity.setXm(stuBaseinfo.getXm());// 姓名
            entity.setXbm(stuBaseinfo.getXbm());// 性别码
            entity.setSemester(currentUser.getSemester()); // 学期
            entity.setStudyYeah(currentUser.getStudyYear().toString()); // 学年
            entity.setStudentId(idStrings[i]); // 设置学生id
            entity.setOrderIndex(orderIndex);// 排序
            entity.setUuid(null);
            // 增加时要设置创建人
            entity.setCreateUser(userCh); // 创建人
            // 持久化到数据库
            thisService.doMerge(entity);
            thisService.doLogicDelOrRestore(cStrings[i], StatuVeriable.ISDELETE);// 删除之前所在班级数据
        }
        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * 推送消息
     * 
     * @param id
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/classStuTs")
    public void classStuTs(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] str = { "claiId", "isDelete" };
        Object[] str2 = { id, 0 };
        List<JwClassstudent> newLists = thisService.queryByProerties(str, str2);
        PushInfo pushInfo = null;
        String stchName = "";
        JwTGradeclass jwTGradeclass = null;
        List<JwClassteacher> list = cltService.queryByProerties(str, str2);
        for (JwClassteacher jwTClassteacher : list) {
            String d;
            if (jwTClassteacher.getCategory() == 0)
                d = "正";
            else
                d = "副";
            stchName = jwTClassteacher.getXm() + "-" + d + "," + stchName;
        }
        JwClassRoomAllot info = roomaService.getByProerties(str, str2);
        if (info == null) {
            writeJSON(response, jsonBuilder.returnFailureJson("'这个班还没有分配教室，请前往教室分配分配教室给该班级，不允许推送。'"));
            return;
        }
        String roomName = info.getRoomName();
        for (JwClassstudent jwTClassstudent : newLists) {
            pushInfo = new PushInfo();
            jwTGradeclass = gcService.get(jwTClassstudent.getClaiId());
            pushInfo.setEmplName(jwTClassstudent.getXm());// 姓名
            pushInfo.setEmplNo(jwTClassstudent.getUserNumb());// 学号
            pushInfo.setRegTime(new Date());
            pushInfo.setEventType("分班信息");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            pushInfo.setRegStatus("学生：" + pushInfo.getEmplName() + "，你分配在" + jwTGradeclass.getClassName() + "，上课教室为："
                    + roomName + "，班主任是：" + stchName);
            pushService.doMerge(pushInfo);
        }
        StringBuffer stuName = new StringBuffer();
        for (JwClassstudent jwTClassstudent : newLists) {
            stuName.append(jwTClassstudent.getXm() + ",");
        }

        List<JwClassteacher> lists = cltService.queryByProerties(str, str2);
        for (JwClassteacher jwTClassteacher : lists) {
            pushInfo = new PushInfo();
            pushInfo.setEmplName(jwTClassteacher.getXm());// 姓名
            pushInfo.setEmplNo(jwTClassteacher.getGh());// 工号
            pushInfo.setRegTime(new Date());
            pushInfo.setEventType("学生信息");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            pushInfo.setRegStatus(
                    pushInfo.getEmplName() + "您好，您所带班级的学生有如下：" + stuName.substring(0, stuName.length() - 1));
            pushService.doMerge(pushInfo);
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'推送信息成功。'"));
    }
}
