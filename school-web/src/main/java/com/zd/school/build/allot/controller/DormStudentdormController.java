
package com.zd.school.build.allot.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.model.JwClassDormAllot;
import com.zd.school.build.allot.service.DormStudentdormService;
import com.zd.school.build.allot.service.JwClassDormAllotService;
import com.zd.school.build.define.model.BuildDormDefine;
import com.zd.school.build.define.service.BuildDormDefineService;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.service.JwClassstudentService;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;

/**
 * 
 * ClassName: DormStudentdormController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: (DORM_T_STUDENTDORM)实体Controller. date:
 * 2016-08-26
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/DormStudentDorm")
public class DormStudentdormController extends FrameWorkController<DormStudentDorm> implements Constant {

    @Resource
    DormStudentdormService thisService; // service层接口

    @Resource
    CommTreeService treeService;// 生成树

    @Resource
    JwClassDormAllotService classDormService;// 班级宿舍

    @Resource
    JwClassstudentService classStuService; // 学生分班

    @Resource
    PushInfoService pushService; // 推送

    @Resource
    BuildRoomareaService roomAreaService;// 区域

    @Resource
    JwTGradeclassService gradeClassService; // 班级

    @Resource
    StuBaseinfoService stuBaseinfoService;// 学生

    @Resource
    BuildDormDefineService dormDefineService;// 宿舍定义

    /**
     * 已入住宿舍学生列表 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<DormStudentDorm> qr = thisService.getPaginationQuery(super.start(request), Integer.MAX_VALUE,
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 计算未分配完的混合宿舍
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/hunDormList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void hunDormList(@ModelAttribute JwClassDormAllot entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List list = thisService.getQuerySql(
                "SELECT A.CDORM_ID,D.ROOM_NAME,F.CLASS_NAME,C.DORM_TYPE,C.DORM_BEDCOUNT,COUNT(*) counts,F.CLAI_ID FROM DORM_T_STUDENTDORM A "
                        + "JOIN JW_T_CLASSDORMALLOT B ON A.CDORM_ID=B.CDORM_ID "
                        + "JOIN BUILD_T_DORMDEFINE C ON B.DORM_ID=C.DORM_ID "
                        + "JOIN BUILD_T_ROOMINFO D ON c.ROOM_ID=d.ROOM_ID "
                        + "JOIN dbo.JW_T_GRADECLASS F ON b.CLAI_ID=f.CLAI_ID WHERE A.ISDELETE=0 "
                        + "GROUP BY A.CDORM_ID,D.ROOM_NAME,F.CLASS_NAME,C.DORM_TYPE,C.DORM_BEDCOUNT,F.CLAI_ID HAVING COUNT(*)<6");
        List<JwClassDormAllot> dormAllotList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] objArray = (Object[]) list.get(i);
            if (objArray != null) {
                entity = new JwClassDormAllot();
                entity.setUuid(objArray[0].toString());
                entity.setDormName(objArray[1].toString());
                entity.setClainame(objArray[2].toString());
                entity.setDormType(objArray[3].toString());
                entity.setDormBedCount(objArray[4].toString());
                entity.setStuCount(objArray[5].toString());
                entity.setClaiId(objArray[6].toString());
                dormAllotList.add(entity);
            }
        }
        String strData = jsonBuilder.buildObjListToJson(new Long(dormAllotList.size()), dormAllotList, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 查询出已分配并且人数为0的混班宿舍
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/hunDormListL" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void hunDormListL(@ModelAttribute JwClassDormAllot entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List list = thisService
                .getQuerySql("SELECT A.CDORM_ID,D.ROOM_NAME,F.CLASS_NAME,C.DORM_TYPE,C.DORM_BEDCOUNT,F.CLAI_ID FROM "
                        + " JW_T_CLASSDORMALLOT A JOIN dbo.JW_T_CLASSDORMALLOT B ON A.CDORM_ID=B.CDORM_ID "
                        + " JOIN dbo.BUILD_T_DORMDEFINE C ON B.DORM_ID=C.DORM_ID "
                        + " JOIN dbo.BUILD_T_ROOMINFO D ON c.ROOM_ID=d.ROOM_ID "
                        + " JOIN dbo.JW_T_GRADECLASS F ON b.CLAI_ID=f.CLAI_ID "
                        + " WHERE A.CDORM_ID NOT IN(SELECT CDORM_ID FROM DORM_T_STUDENTDORM  WHERE ISDELETE=0) AND A.ISDELETE=0");
        List<JwClassDormAllot> dormAllotList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] objArray = (Object[]) list.get(i);
            if (objArray != null) {
                entity = new JwClassDormAllot();
                entity.setUuid(objArray[0].toString());
                entity.setDormName(objArray[1].toString());
                entity.setClainame(objArray[2].toString());
                entity.setDormType(objArray[3].toString());
                entity.setDormBedCount(objArray[4].toString());
                entity.setClaiId(objArray[5].toString());
                dormAllotList.add(entity);
            }
        }
        String strData = jsonBuilder.buildObjListToJson(new Long(dormAllotList.size()), dormAllotList, false);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 删除宿舍
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/dormdodelete")
    public void doDelete(String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int count = 0;
        int fs = 0;
        if (StringUtils.isEmpty(uuid)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            String[] ids = uuid.split(",");
            BuildDormDefine defin = null;
            JwClassDormAllot jwTClassdorm = null;
            for (int j = 0; j < ids.length; j++) {
                jwTClassdorm = classDormService.get(ids[j]);
                boolean flag = thisService.IsFieldExist("cdormId", jwTClassdorm.getDormId(), "-1", "isdelete=0");
                if (flag) {
                    ++count;
                }
                if (count == 0) {
                    defin = dormDefineService.get(jwTClassdorm.getDormId());
                    defin.setRoomStatus("0"); // 设置成未分配
                    dormDefineService.doMerge(defin); // 持久化
                    jwTClassdorm.setIsDelete(1); // 设置删除状态
                    classDormService.doMerge(jwTClassdorm); // 持久化
                    ++fs;
                }
                count = 0;
            }
            if (fs > 0) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功。'"));
            } else {
                writeJSON(response, jsonBuilder.returnSuccessJson("'宿舍都已分配给学生，不允许删除。'"));
            }

        }
    }

    @RequestMapping(value = { "/countList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void countList(@ModelAttribute DormStudentDorm entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<DormStudentDorm> qr = thisService.getPaginationQuery(super.start(request), Integer.MAX_VALUE,
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 班级下面宿舍列表
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/classDormlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void classDormlist(@ModelAttribute JwClassDormAllot entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<JwClassDormAllot> qr = classDormService.getPaginationQuery(super.start(request), Integer.MAX_VALUE,
                super.sort(request), super.filter(request), true);
        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 未分配宿舍学生列表
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/stuNotAllotlist" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void stuNotAllotlist(@ModelAttribute JwClassstudent entity, HttpServletRequest request,
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
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<JwClassstudent> lists = classStuService.getQuery(hql.toString(), 0, 0);// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 生成树
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/classtreelist")
    public void classtreelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        List<CommTree> lists = treeService.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 生成树
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/dormTreelist")
    public void dormTreelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        List<CommTree> lists = treeService.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 查询出一键分配宿舍的信息
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/onKeyList")
    public void onKeyList(@ModelAttribute DormStudentDorm entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
        // 查询出该年级下所有的有效宿舍
        List<DormStudentDorm> lists = thisService.getQuerySql("EXEC JW_P_DORMCOUNT '" + entity.getWhereSql() + "'");
        String whereSql = " where claiId in(select uuid from JwTGradeclass  where graiId='" + entity.getWhereSql()
                + "') and isDelete=0 and studentId not in(select stuId from DormStudentDorm where isdelete=0) order by className,xbm";
        // 先获取到该年级下全部学生
        List<JwClassstudent> list = classStuService.getQuery("from JwClassstudent " + whereSql);
        // 获取到现有年级下的所有班级
        List<JwTGradeclass> gradeClassList = gradeClassService
                .getQuery("from JwTGradeclass where graiId='" + entity.getWhereSql() + "'");
        List<JwClassstudent> nanList = new ArrayList<>(); // 男生
        List<JwClassstudent> nvList = new ArrayList<>(); // 女生
        // 将男生分出来
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getXbm().equals("1")) {
                nanList.add(list.get(i));
            }
        }
        // 将女生分出来
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getXbm().equals("2")) {
                nvList.add(list.get(i));
            }
        }
        int nan = this.countDorm(gradeClassList, nanList);
        int nv = this.countDorm(gradeClassList, nvList);
        Object objList = lists.get(0);
        List<String> strList = new ArrayList<>();
        Object[] objArray = (Object[]) objList;
        if (objArray != null) {
            for (Object o : objArray) {
                if (o == null) {
                    return;
                }
                strList.add(o.toString());
            }
            Integer nanCount = Integer.valueOf(strList.get(0)); // 男生数量
            Integer nvCount = Integer.valueOf(strList.get(1));// 女生数量
            Integer stuCount = Integer.valueOf(strList.get(2));// 合计学生总数量
            Integer nanDormCount = nan;// 男生所需宿舍
            Integer nvDormCount = nv;// 女生所需宿舍
            Integer sxDorm = nan + nv; // 合计所需宿舍
            Integer nanDorm = Integer.valueOf(strList.get(6));// 男生有效宿舍
            Integer nvDorm = Integer.valueOf(strList.get(7));// 女生有效宿舍
            Integer hunDorm = Integer.valueOf(strList.get(8)); // 混合有效宿舍
            Integer yxDorm = Integer.valueOf(strList.get(9));// 合计有效宿舍
            DormStudentDorm dormStudentDorm = new DormStudentDorm();
            dormStudentDorm.setNanCount(nanCount);
            dormStudentDorm.setNvCount(nvCount);
            dormStudentDorm.setStuCount(stuCount);
            dormStudentDorm.setNanDormCount(nanDormCount);
            dormStudentDorm.setNvDormCount(nvDormCount);
            dormStudentDorm.setSxDorm(sxDorm);
            dormStudentDorm.setNanDorm(nanDorm);
            dormStudentDorm.setNvDorm(nvDorm);
            dormStudentDorm.setHunDorm(hunDorm);
            dormStudentDorm.setYxDorm(yxDorm);
            List<DormStudentDorm> newlists = new ArrayList<>();// 执行查询方法
            newlists.add(dormStudentDorm);
            String strData = jsonBuilder.buildObjListToJson(new Long(newlists.size()), newlists, false);// 处理数据
            writeJSON(response, strData);// 返回数据
        }
    }

    /**
     * 计算男女生宿舍数量
     * 
     * @param gradeClassList
     * @param list
     * @return
     */
    public int countDorm(List<JwTGradeclass> gradeClassList, List<JwClassstudent> list) {
        int zc = 0;
        List<Integer> ys = new ArrayList<>();
        List<JwClassstudent> tempList;
        for (int j = 0; j < gradeClassList.size(); j++) {
            tempList = new ArrayList<>();
            for (int k = 0; k < list.size(); k++) {
                if (list.get(k).getClaiId().equals(gradeClassList.get(j).getUuid())) {
                    tempList.add(list.get(k));
                }
            }
            if (tempList.size() % 6 == 0) {
                zc = tempList.size() / 6 + zc;
            } else {
                zc = tempList.size() / 6 + zc;
                ys.add(tempList.size() % 6);
            }
        }
        int ysj = 0;
        int ysi = 0;
        for (int i = 0; i < ys.size(); i++) {
            List<Integer> temp = ys;
            for (int j = 0; j < temp.size(); j++) {
                if ((ys.get(i) + temp.get(j)) == 6) {
                    if (i == j) {
                        continue;
                    }
                    zc = zc + 1;
                    ys.remove(j);
                    ys.remove(i);
                    i = -1;
                    break;
                }
                if ((ys.get(i) + temp.get(j)) < 6) {
                    ysj = temp.get(j);
                    ysi = ys.get(i);
                    ys.remove(j);
                    ys.remove(i);
                    i = -1;
                    ys.add(ysj + ysi);
                    break;
                }
            }

        }
        return zc + ys.size();
    }

    /**
     * 一键分配宿舍调用方法
     * 
     * @param gradId
     *            年级id
     * @param nanId
     *            男宿舍id
     * @param nvId
     *            女宿舍id
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/onKeyAllotDorm")
    public void onKeyAllotDorm(String gradId, String nanId, String nvId, HttpServletRequest request,
            HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String whereSql = " where claiId in(select uuid from JwTGradeclass  where graiId='" + gradId
                + "') and isDelete=0 and studentId not in(select stuId from DormStudentDorm where isdelete=0) order by className,xbm";
        // 先获取到该年级下全部学生
        List<JwClassstudent> list = classStuService.getQuery("from JwClassstudent " + whereSql);
        // 获取到现有年级下的所有班级
        List<JwTGradeclass> gradeClassList = gradeClassService
                .getQuery("from JwTGradeclass where graiId='" + gradId + "'");
        String nanDormId[] = nanId.split(","); // 男宿舍id
        String nvDormId[] = nvId.split(","); // 男宿舍id
        List<JwClassstudent> nanList = new ArrayList<>(); // 男生
        List<JwClassstudent> nvList = new ArrayList<>(); // 女生
        // 将男生分出来
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getXbm().equals("1")) {
                nanList.add(list.get(i));
            }
        }
        // 将女生分出来
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getXbm().equals("2")) {
                nvList.add(list.get(i));
            }
        }
        List<String> dormNanList = new ArrayList<>(); // 男宿舍集合
        for (int i = 0; i < nanDormId.length; i++) {
            dormNanList.add(nanDormId[i]);
        }
        List<String> dormNvList = new ArrayList<>();// 女宿舍集合
        for (int i = 0; i < nvDormId.length; i++) {
            dormNvList.add(nvDormId[i]);
        }
        // 分配男
        this.onKeyAllot(gradeClassList, nanList, dormNanList, userCh);
        // 分配女
        this.onKeyAllot(gradeClassList, nvList, dormNvList, userCh);
        writeJSON(response, jsonBuilder.returnSuccessJson("'一键分配分配成功。'"));
    }

    /**
     * 一键分配宿舍实现方法
     * 
     * @param gradeClassList
     *            班级集合
     * @param list
     *            //学生集合（男list或者女list）
     * @param dormList
     *            //宿舍集合(男List或者女list)
     * @param userCh
     *            //创建人
     */
    public void onKeyAllot(List<JwTGradeclass> gradeClassList, List<JwClassstudent> list, List<String> dormList,
            String userCh) {
        List<DormStudentDorm> hunDorm = new ArrayList<>();
        JwClassDormAllot jwClassDormAllot = null; // 班级宿舍
        String claiId = ""; // 班级id
        DormStudentDorm dormStudentDorm = null; // 学生宿舍
        BuildDormDefine jwTDormDefin; // 宿舍定义
        Integer orderIndex = 0; // 排序号
        List<JwClassstudent> tempList; // 用来存储临时数据
        // 循环班级
        for (int j = 0; j < gradeClassList.size(); j++) {
            int zc = 0;
            int ys = 0;
            claiId = gradeClassList.get(j).getUuid();
            tempList = new ArrayList<>();
            for (int k = 0; k < list.size(); k++) {
                if (list.get(k).getClaiId().equals(claiId)) {
                    tempList.add(list.get(k));
                }
            }
            if (tempList.size() % 6 == 0) {
                zc = tempList.size() / 6;
            } else {
                zc = tempList.size() / 6;
                ys = tempList.size() % 6;
            }
            // 看看需要几个宿舍
            for (int i = 0; i < zc; i++) {
                jwClassDormAllot = new JwClassDormAllot();
                jwTDormDefin = new BuildDormDefine();
                orderIndex = classDormService.getDefaultOrderIndex(jwClassDormAllot);// 获取排序号
                jwClassDormAllot.setOrderIndex(orderIndex);// 排序
                jwClassDormAllot.setDormId(dormList.get(0));// 设置宿舍id
                jwClassDormAllot.setClaiId(claiId); // 设置班级id
                jwClassDormAllot.setCreateUser(userCh); // 创建人
                jwTDormDefin = dormDefineService.get(dormList.get(0)); // 获取到宿舍
                jwTDormDefin.setRoomStatus("1");// 将宿舍状态设置为已分配
                jwClassDormAllot = classDormService.doMerge(jwClassDormAllot);// 将宿舍分配到班级
                dormDefineService.doMerge(jwTDormDefin); // 修改
                // 此处循环作为插入学生到宿舍
                for (int k = 0; k < 6; k++) {
                    dormStudentDorm = new DormStudentDorm();
                    orderIndex = thisService.getDefaultOrderIndex(dormStudentDorm);// 排序号
                    dormStudentDorm.setOrderIndex(orderIndex);// 排序
                    dormStudentDorm.setStuId(tempList.get(0).getStudentId()); // 设置学生id
                    dormStudentDorm.setCdormId(jwClassDormAllot.getUuid());// 设置宿舍id
                    dormStudentDorm.setBedNum((k + 1));// 床号
                    dormStudentDorm.setArkNum(dormStudentDorm.getBedNum());// 柜号（默认跟床号对应）
                    dormStudentDorm.setClaiId(jwClassDormAllot.getClaiId());// 班级id
                    dormStudentDorm.setCreateUser(userCh); // 创建人
                    dormStudentDorm.setInTime(new Date());// 设置入住时间
                    dormStudentDorm = thisService.doMerge(dormStudentDorm);// 持久化到数据库
                    tempList.remove(0); // 每次使用完一个学生就将其移除
                }
                dormList.remove(0); // 将使用完的宿舍移除
            }
            // 如果余数不小于0那么代表该宿舍未满6人，那么可以考虑寻找混合宿舍，如混合宿舍床位不匹配那么就重新创建一个宿舍
            int bs = 0;
            int bedCount = 0;
            String uuid = null;
            if (ys > 0) {
                if (hunDorm.size() > 0) {
                    for (int i = 0; i < hunDorm.size(); i++) {
                        // 如果存取剩余的床位数刚好相等，那么将剩余的学生加入到该宿舍
                        if ((6 - hunDorm.get(i).getBedNum()) == ys) {
                            bs = 1;
                            dormStudentDorm = hunDorm.get(i);// 获取到存在里面的值
                            hunDorm.remove(i);
                            break;
                        }
                        if ((6 - hunDorm.get(i).getBedNum()) > ys) {
                            bs = 1;
                            dormStudentDorm = hunDorm.get(i);// 获取到存在里面的值
                            hunDorm.remove(i);
                            break;
                        }
                    }
                    // 如果标识为0那么代表在混合宿舍中没找到符合当前剩余学生的宿舍
                    if (bs == 0 && tempList.size() > 0 && dormList.size() > 0) {
                        // 如过剩余床位数小于剩余人数，那么代表该宿舍无法存入这么多人，那么将重新分配新宿舍给这个班，并且计算出分配完所剩余的床位
                        jwClassDormAllot = new JwClassDormAllot();
                        jwTDormDefin = new BuildDormDefine();
                        orderIndex = classDormService.getDefaultOrderIndex(jwClassDormAllot);// 获取排序号
                        jwClassDormAllot.setOrderIndex(orderIndex);// 排序
                        jwClassDormAllot.setDormId(dormList.get(0));// 设置宿舍id
                        jwClassDormAllot.setClaiId(claiId); // 设置班级id
                        jwClassDormAllot.setCreateUser(userCh); // 创建人
                        jwClassDormAllot.setIsmixed("1");// 混合宿舍
                        jwClassDormAllot = classDormService.doMerge(jwClassDormAllot);// 将宿舍分配到班级
                        jwTDormDefin = dormDefineService.get(dormList.get(0)); // 获取到宿舍
                        jwTDormDefin.setRoomStatus("1");// 将宿舍状态设置为已分配
                        jwTDormDefin.setIsMixed("1");// 设置为混合宿舍
                        dormDefineService.doMerge(jwTDormDefin); // 修改
                        // 直接开始新一轮分配宿舍
                        for (int k = 0; k < ys; k++) {
                            dormStudentDorm = new DormStudentDorm();
                            orderIndex = thisService.getDefaultOrderIndex(dormStudentDorm);
                            dormStudentDorm.setOrderIndex(orderIndex);// 排序
                            dormStudentDorm.setStuId(tempList.get(0).getStudentId()); // 设置学生id
                            dormStudentDorm.setCdormId(jwClassDormAllot.getUuid());// 设置宿舍id
                            dormStudentDorm.setBedNum((k + 1));// 床号
                            dormStudentDorm.setArkNum(dormStudentDorm.getBedNum());// 柜号（默认跟床号对应）
                            dormStudentDorm.setClaiId(jwClassDormAllot.getClaiId());// 班级id
                            dormStudentDorm.setCreateUser(userCh); // 创建人
                            dormStudentDorm.setInTime(new Date());// 设置入住时间
                            dormStudentDorm = thisService.doMerge(dormStudentDorm);// 持久化到数据库
                            tempList.remove(0);
                        }
                        dormList.remove(0);
                    }
                    if (ys > 0 && tempList.size() > 0 && bs == 1) {
                        // 此处专门针对床位数与学生数，或者床位数大于当前学生数的宿舍做处理
                        for (int j2 = 0; j2 < ys; j2++) {
                            bedCount = dormStudentDorm.getBedNum();// 首先获取最大床位数
                            uuid = dormStudentDorm.getCdormId(); // 获取到宿舍id
                            dormStudentDorm = new DormStudentDorm();
                            orderIndex = thisService.getDefaultOrderIndex(dormStudentDorm);
                            dormStudentDorm.setOrderIndex(orderIndex);// 排序
                            dormStudentDorm.setStuId(tempList.get(0).getStudentId()); // 设置学生id
                            dormStudentDorm.setCdormId(uuid);// 设置宿舍id
                            dormStudentDorm.setBedNum((bedCount + 1));// 床号
                            dormStudentDorm.setArkNum(dormStudentDorm.getBedNum());// 柜号（默认跟床号对应）
                            dormStudentDorm.setClaiId(jwClassDormAllot.getClaiId());// 班级id
                            dormStudentDorm.setCreateUser(userCh); // 创建人
                            dormStudentDorm.setInTime(new Date());// 设置入住时间
                            dormStudentDorm = thisService.doMerge(dormStudentDorm);// 持久化到数据库
                            tempList.remove(0);
                        }
                    }
                    // 如果在循环完之后该混合宿舍还有剩余床位那么就继续加入到混合宿舍列表中
                    if (dormStudentDorm.getBedNum() < 6) {
                        hunDorm.add(dormStudentDorm);
                    }
                    DormStudentDorm dormStudentDorms = null;
                    // 当宿舍被使用完时，并且还剩余有学生未分配宿舍，那么可以去混合宿舍里寻找是否有匹配的宿舍
                    if (dormList.size() <= 0 && tempList.size() > 0 && bs == 0) {
                        for (int i = 0; i < hunDorm.size(); i++) {
                            dormStudentDorm = hunDorm.get(i);
                            bedCount = dormStudentDorm.getBedNum();
                            for (int k = 0; k < (6 - bedCount); k++) {
                                dormStudentDorms = new DormStudentDorm();
                                dormStudentDorms.setBedNum(bedCount);// 首先获取最大床位数
                                orderIndex = thisService.getDefaultOrderIndex(dormStudentDorms);
                                dormStudentDorms.setOrderIndex(orderIndex);// 排序
                                dormStudentDorms.setStuId(tempList.get(0).getStudentId()); // 设置学生id
                                dormStudentDorms.setCdormId(dormStudentDorm.getCdormId());// 设置宿舍id
                                dormStudentDorms.setBedNum((dormStudentDorms.getBedNum() + 1));// 床号
                                dormStudentDorms.setArkNum(dormStudentDorms.getBedNum());// 柜号（默认跟床号对应）
                                dormStudentDorms.setClaiId(jwClassDormAllot.getClaiId());// 班级id
                                dormStudentDorms.setCreateUser(userCh); // 创建人
                                dormStudentDorms.setInTime(new Date());// 设置入住时间
                                dormStudentDorms = thisService.doMerge(dormStudentDorms);// 持久化到数据库
                                tempList.remove(0);
                            }
                            // 如果有一个宿舍被分配完了，那么从第一个宿舍开始寻找合适宿舍给学生入住
                            if (dormStudentDorms.getBedNum() == 6) {
                                hunDorm.remove(i);
                                i = -1;
                            }
                        }
                    }
                } else {
                    jwClassDormAllot = new JwClassDormAllot();
                    jwTDormDefin = new BuildDormDefine();
                    orderIndex = classDormService.getDefaultOrderIndex(jwClassDormAllot);// 获取排序号
                    jwClassDormAllot.setOrderIndex(orderIndex);// 排序
                    jwClassDormAllot.setDormId(dormList.get(0));// 设置宿舍id
                    jwClassDormAllot.setClaiId(claiId); // 设置班级id
                    jwClassDormAllot.setCreateUser(userCh); // 创建人
                    jwClassDormAllot.setIsmixed("1");// 混合宿舍
                    jwClassDormAllot = classDormService.doMerge(jwClassDormAllot);// 将宿舍分配到班级
                    jwTDormDefin = dormDefineService.get(dormList.get(0)); // 获取到宿舍
                    jwTDormDefin.setRoomStatus("1");// 将宿舍状态设置为已分配
                    jwTDormDefin.setIsMixed("1");// 设置为混合宿舍
                    dormDefineService.doMerge(jwTDormDefin); // 修改
                    dormList.remove(0);
                    // 直接开始新一轮分配宿舍
                    for (int k = 0; k < ys; k++) {
                        dormStudentDorm = new DormStudentDorm();
                        orderIndex = thisService.getDefaultOrderIndex(dormStudentDorm);
                        dormStudentDorm.setOrderIndex(orderIndex);// 排序
                        dormStudentDorm.setStuId(tempList.get(0).getStudentId()); // 设置学生id
                        dormStudentDorm.setCdormId(jwClassDormAllot.getUuid());// 设置宿舍id
                        dormStudentDorm.setBedNum((k + 1));// 床号
                        dormStudentDorm.setArkNum(dormStudentDorm.getBedNum());// 柜号（默认跟床号对应）
                        dormStudentDorm.setClaiId(jwClassDormAllot.getClaiId());// 班级id
                        dormStudentDorm.setCreateUser(userCh); // 创建人
                        dormStudentDorm.setInTime(new Date());// 设置入住时间
                        dormStudentDorm = thisService.doMerge(dormStudentDorm);// 持久化到数据库
                        tempList.remove(0);
                    }
                    // 此时宿舍肯定无法全部使用完那么将此宿舍加入到混合宿舍列表，并且将其最大床位数记录下来
                    hunDorm.add(dormStudentDorm);
                }
            }
        }
    }

    /**
     * 
     * @Title: 学生分配宿舍 @Description: TODO @param @param DormStudentdorm
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        int rs = 0;// 人数
        // 获取班级下面宿舍
        JwClassDormAllot jwClassDormAllot = classDormService.get(entity.getCdormId());
        // 获取该宿舍下存在的人数
        List<DormStudentDorm> liStudentdorms = thisService.queryByProerties("cdormId", entity.getCdormId());
        for (int i = 0; i < liStudentdorms.size(); i++) {
            if (liStudentdorms.get(i).getIsDelete() == 0) {
                ++rs;// 获取有效的人数
            }
        }
        BuildDormDefine buildDormDefine = dormDefineService.get(jwClassDormAllot.getDormId());// 获取宿舍信息
        if (rs >= Integer.valueOf(buildDormDefine.getDormBedCount())) {
            writeJSON(response, jsonBuilder
                    .returnFailureJson("'该宿舍最大人数为：" + buildDormDefine.getDormBedCount() + "人。现已入住：" + rs + "。'"));
            return;
        }
        int kfp = Integer.valueOf(buildDormDefine.getDormBedCount()) - rs;
        String[] studentId = entity.getStuId().split(",");
        if (studentId.length > kfp) {
            writeJSON(response, jsonBuilder.returnFailureJson(
                    "'该宿舍最大人数为：" + buildDormDefine.getDormBedCount() + "人。现已入住：" + rs + "。可分配床位数为：" + kfp + "'"));
            return;
        }
        for (int i = 0; i < studentId.length; i++) {
            String userCh = "超级管理员";
            SysUser currentUser = getCurrentSysUser();
            if (currentUser != null)
                userCh = currentUser.getXm();
            // 如果界面有了排序号的输入，则不需要取默认的了
            DormStudentDorm perEntity = new DormStudentDorm();
            perEntity.setBedNum(entity.getBedNum());
            BeanUtils.copyPropertiesExceptNull(entity, perEntity);
            Integer orderIndex = thisService.getDefaultOrderIndex(entity);
            entity.setOrderIndex(orderIndex);// 排序
            entity.setStuId(studentId[i]); // 设置学生id
            entity.setCdormId(jwClassDormAllot.getUuid());// 设置宿舍id
            entity.setBedNum(entity.getBedNum() + 1);// 床号
            entity.setArkNum(entity.getBedNum());// 柜号（默认跟床号对应）
            entity.setCreateUser(userCh); // 创建人
            entity.setInTime(new Date());// 设置入住时间
            // 持久化到数据库
            thisService.doMerge(entity);
        }
        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson("'分配成功。'"));
    }

    /**
     * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        boolean flag = false;
        if (StringUtils.isEmpty(entity.getUuid())) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            String[] delIds = entity.getUuid().split(",");
            for (int i = 0; i < delIds.length; i++) {
                flag = thisService.doLogicDelOrRestore(delIds[i], StatuVeriable.ISDELETE);
            }
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * 自动分配宿舍
     * 
     * @param claiId
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/dormSelfAllot")
    public void dormSelfAllot(String claiId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] propName = { "claiId", "isDelete" };
        Object[] propValue = { claiId, 0 };
        List<JwClassDormAllot> dormStuList = classDormService.queryByProerties(propName, propValue);// 获取该班级下所有有效宿舍
        List<JwClassDormAllot> nandormList = new ArrayList<>(); // 男宿舍
        List<JwClassDormAllot> nvdormList = new ArrayList<>(); // 女宿舍
        List<JwClassDormAllot> nanhundormList = new ArrayList<>(); // 男混班宿舍
        List<JwClassDormAllot> nvhundormList = new ArrayList<>(); // 男混班宿舍
        // 将班级下所有宿舍分类
        for (JwClassDormAllot jwClassDormAllot : dormStuList) {
            if (jwClassDormAllot.getDormType().equals("1")) { // 男
                if (jwClassDormAllot.getIsmixed().equals("1")) {
                    nanhundormList.add(jwClassDormAllot);// 混班
                } else {
                    nandormList.add(jwClassDormAllot);
                }
            } else if (jwClassDormAllot.getDormType().equals("2")) {// 女
                if (jwClassDormAllot.getIsmixed().equals("1")) {
                    nvhundormList.add(jwClassDormAllot);// 混班
                } else {
                    nvdormList.add(jwClassDormAllot);
                }
            }
        }
        List<JwClassstudent> nanList = new ArrayList<>(); // 有效男学生
        List<JwClassstudent> nvList = new ArrayList<>(); // 有效女学生
        List<JwClassstudent> classStulist = classStuService.getQuery(
                "from JwClassstudent WHERE ISDELETE=0" + " AND studentId NOT IN(SELECT stuId FROM DormStudentDorm "
                        + "WHERE ISDELETE=0) AND claiId='" + claiId + "'");// 获取该班级下所有有效学生且未分配宿舍的
        for (JwClassstudent jwClassstudent : classStulist) {
            if (jwClassstudent.getXbm() != null)
                if (jwClassstudent.getXbm().equals("1")) {
                    nanList.add(jwClassstudent);
                } else if (jwClassstudent.getXbm().equals("2")) {
                    nvList.add(jwClassstudent);
                }
        }
        this.fp(nandormList, nanhundormList, nanList, claiId); // 自动分配男
        this.fp(nvdormList, nvhundormList, nvList, claiId); // 自动分配女

        writeJSON(response, jsonBuilder.returnSuccessJson("'自动分配成功。'"));
    }

    /**
     * 自动分配学生到宿舍
     * 
     * @param clsssList
     *            班级下面的学生（男or女）
     * @param dormList
     *            （宿舍的类型）
     * @param claiId
     *            （班级id）
     */
    public void fp(List<JwClassDormAllot> dormList, List<JwClassDormAllot> hunDormList, List<JwClassstudent> stuList,
            String claiId) {
        int bs = 0; // 标识用了几个宿舍
        for (int i = 0; i < dormList.size(); i++) {
            String roomCount = "";
            int bedCount = Integer.valueOf(dormList.get(i).getDormBedCount());
            for (int j = 1; j <= bedCount; j++) {
                roomCount = roomCount + j + ",";
            }
            roomCount = roomCount.substring(0, roomCount.length() - 1);
            // 直接获取宿舍下面的可入住人数
            List list = thisService.getQuerySql("SELECT * FROM dbo.Split('" + roomCount + "',',')  A"
                    + "	WHERE A NOT IN(SELECT BED_NUM FROM dbo.DORM_T_STUDENTDORM" + " WHERE ISDELETE=0 and CDORM_ID "
                    + "IN(SELECT CDORM_ID FROM dbo.JW_T_CLASSDORMALLOT WHERE CDORM_ID='" + dormList.get(i).getUuid()
                    + "'))");
            DormStudentDorm dormStudentDorm = null;
            for (int j = 0; j < list.size(); j++) {
                if (stuList.size() > 0) {
                    dormStudentDorm = new DormStudentDorm();
                    int dormBedCount = Integer.valueOf(list.get(j).toString()); // 获取到床位
                    dormStudentDorm.setBedNum(dormBedCount); // 设置床位
                    dormStudentDorm.setArkNum(dormBedCount); // 设置柜号
                    dormStudentDorm.setCdormId(dormList.get(i).getUuid());
                    dormStudentDorm.setClaiId(claiId);
                    dormStudentDorm.setInTime(new Date());
                    dormStudentDorm.setStuId(stuList.get(0).getStudentId());
                    stuList.remove(0);
                    thisService.doMerge(dormStudentDorm);
                }
            }
            ++bs;
            if (bs == dormList.size() && stuList.size() > 0) {
                for (int j = 0; j < hunDormList.size(); j++) {
                    String rmCount = "";
                    int rCount = Integer.valueOf(hunDormList.get(j).getDormBedCount());
                    for (int k = 1; k <= rCount; k++) {
                        rmCount = rmCount + k + ",";
                    }
                    rmCount = rmCount.substring(0, rmCount.length() - 1);
                    List hunList = thisService.getQuerySql("SELECT * FROM dbo.Split('" + rmCount + "',',')  A"
                            + "	WHERE A NOT IN(SELECT BED_NUM FROM dbo.DORM_T_STUDENTDORM"
                            + " WHERE ISDELETE=0 and CDORM_ID "
                            + "IN(SELECT CDORM_ID FROM dbo.JW_T_CLASSDORMALLOT WHERE CDORM_ID='"
                            + hunDormList.get(j).getUuid() + "'))");
                    for (int k = 0; k < hunList.size(); k++) {
                        if (stuList.size() > 0) {
                            dormStudentDorm = new DormStudentDorm();
                            int dormBedCount = Integer.valueOf(hunList.get(k).toString()); // 获取到床位
                            dormStudentDorm.setBedNum(dormBedCount); // 设置床位
                            dormStudentDorm.setArkNum(dormBedCount); // 设置柜号
                            dormStudentDorm.setCdormId(hunDormList.get(j).getUuid());
                            dormStudentDorm.setClaiId(claiId);
                            dormStudentDorm.setInTime(new Date());
                            dormStudentDorm.setStuId(stuList.get(0).getStudentId());
                            stuList.remove(0);
                            thisService.doMerge(dormStudentDorm);
                        }
                    }
                }
            }

        }
    }

    /**
     * 推送
     * 
     * @param id
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/dormTs")
    public void dormTs(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] str = { "claiId", "isDelete" };
        Object[] str2 = { id, 0 };
        List<DormStudentDorm> newLists = thisService.queryByProerties(str, str2);
        PushInfo pushInfo = null;
        StuBaseinfo stuBaseinfo = null;
        BuildDormDefine dormDefin = null;
        JwClassDormAllot jwClassDormAllot = null;
        String roomName = null;
        String areaName = null;// 楼栋名
        String areaLc = null;// 楼层
        for (DormStudentDorm dormTStudentdorm : newLists) {
            stuBaseinfo = stuBaseinfoService.get(dormTStudentdorm.getStuId()); // 学生信息
            roomName = dormTStudentdorm.getRoomName();// 房间名
            jwClassDormAllot = classDormService.getByProerties("uuid", dormTStudentdorm.getCdormId());
            dormDefin = dormDefineService.get(jwClassDormAllot.getDormId());
            areaLc = jwClassDormAllot.getAreaName();// 楼层名
            areaName = roomAreaService.getByProerties("uuid", dormDefin.getAreaId()).getParentName();
            pushInfo = new PushInfo();
            pushInfo.setEmplName(stuBaseinfo.getXm());// 姓名
            pushInfo.setEmplNo(stuBaseinfo.getUserNumb());// 学号
            pushInfo.setRegTime(new Date());
            pushInfo.setVersion(0);
            pushInfo.setEventType("宿舍信息");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            pushInfo.setRegStatus("学生：" + pushInfo.getEmplName() + "，你的宿舍分配在" + areaName + "，" + areaLc + "，" + roomName
                    + "房间，床号为：" + dormTStudentdorm.getBedNum());
            pushService.doMerge(pushInfo);
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'推送信息成功。'"));
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * DormStudentdorm @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String[] list = entity.getUuid().split(";");
        int count = 0;
        DormStudentDorm perEntity = null;
        for (int i = 0; i < list.length; i++) {
            String[] id = list[i].split(",");
            if (id != null && id.length > 0) {
                if (id.length == 4) {
                    perEntity = thisService.get(id[1]);
                    perEntity.setBedNum(Integer.valueOf(id[2]));
                    perEntity.setArkNum(Integer.valueOf(id[3]));
                } else {
                    perEntity = thisService.get(id[0]);
                    perEntity.setBedNum(Integer.valueOf(id[1]));
                    perEntity.setArkNum(Integer.valueOf(id[2]));
                }
                perEntity.setUpdateTime(new Date()); // 设置修改时间
                perEntity.setUpdateUser(userCh); // 设置修改人的中文名
                thisService.doMerge(perEntity);// 执行修改方法
                ++count;
            }
        }
        if (count > 0) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'保存成功。'"));
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("'保存失败。'"));
        }

    }
}
