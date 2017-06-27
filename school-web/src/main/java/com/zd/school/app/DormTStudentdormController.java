
package com.zd.school.app;

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
import com.zd.core.constant.NodeType;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.JSONTreeNode;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.model.JwClassDormAllot;
import com.zd.school.build.allot.service.DormStudentdormService;
import com.zd.school.build.allot.service.JwClassDormAllotService;
import com.zd.school.build.define.model.BuildDormDefine;
import com.zd.school.build.define.model.BuildRoomarea;
import com.zd.school.build.define.service.BuildDormDefineService;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.model.StuDividescore;
import com.zd.school.student.studentclass.service.JwClassstudentService;
import com.zd.school.student.studentclass.service.StuDividescoreService;
import com.zd.school.student.studentinfo.model.StuBaseinfo;
import com.zd.school.student.studentinfo.service.StuBaseinfoService;

/**
 * 
 * ClassName: DormTStudentdormController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 学生宿舍床位分配信息实体Controller. date: 2016-05-25
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/DormTStudentdorm")
public class DormTStudentdormController extends FrameWorkController<DormStudentDorm> implements Constant {

    @Resource
    DormStudentdormService thisService; // service层接口
    @Resource
    StuDividescoreService studentTDividescoreService; //学生成绩表

    @Resource
    StuBaseinfoService jwTStudentService; //学生接口

    @Resource
    JwTGradeclassService jwTGradeclassService; //班级接口

    @Resource
    BuildDormDefineService jwTDormDefinService; //宿舍定义

    @Resource
    BuildRoomareaService areaService; //建筑物

    @Resource
    JwClassDormAllotService jwTClassdormService; //班级分配宿舍

    @Resource
    JwClassstudentService jwTClassstudentService;//学生分班

    @Resource
    PushInfoService pushService; //推送

    /**
     * 分配宿舍，用于显示楼栋下宿舍的信息 getJwTDormDefinTree(这里用一句话描述这个方法的作用) TODO(这里描述这个方法适用条件
     * – 可选) TODO(这里描述这个方法的执行流程 – 可选) TODO(这里描述这个方法的使用方法 – 可选)
     * TODO(这里描述这个方法的注意事项 – 可选)
     *
     * @Title: getJwTDormDefinTree @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/getJwTStuDormAllotTree")
    public void getJwTDormDefinTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String node = request.getParameter("node");
        String excludes = request.getParameter("excludes");
        String parentId = request.getParameter("parentId");
        String whereSql = request.getParameter("whereSql");
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select * from V_DORMFP_BJ where 1=1");
        if (StringUtils.isEmpty(parentId)) {
            parentId = NodeType.ROOT;
        }
        List<JSONTreeNode> jsonList = new ArrayList<>();

        if (StringUtils.isNotEmpty(whereSql)) {
            sBuffer.append(whereSql);
        }
        List<?> list = thisService.doQuerySql(sBuffer.toString());
        //循环添加数据
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = (Object[]) list.get(i);
            JSONTreeNode jsonNode = new JSONTreeNode();
            jsonNode.setText((String) obj[0]);
            jsonNode.setId((String) obj[1]);
            jsonNode.setParent(obj[2].toString());
            jsonNode.setNodeLayer(obj[3].toString());
            jsonList.add(jsonNode);
        }
        // 构建成树形节点对象
        JSONTreeNode roots = buildJSONTreeNode(jsonList, node);
        //获得json格式树结构
        String strData = JsonBuilder.getInstance().buildList(roots.getChildren(), excludes);
        writeJSON(response, strData);
    }

    @RequestMapping("/getJwTStuDormAllotTrees")
    public void getJwTDormDefinTrees(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String node = request.getParameter("node");
        String excludes = request.getParameter("excludes");
        String parentId = request.getParameter("parentId");
        String whereSql = request.getParameter("whereSql");
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select * from V_DORMFP_BJ");
        if (StringUtils.isEmpty(parentId)) {
            parentId = NodeType.ROOT;
        }
        List<JSONTreeNode> jsonList = new ArrayList<>();

        if (StringUtils.isNotEmpty(whereSql)) {
            sBuffer.append(whereSql);
        }
        List<?> list = thisService.doQuerySql(sBuffer.toString());
        //循环添加数据
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = (Object[]) list.get(i);
            JSONTreeNode jsonNode = new JSONTreeNode();
            jsonNode.setText((String) obj[0]);
            jsonNode.setId((String) obj[1]);
            jsonNode.setParent(obj[2].toString());
            jsonNode.setNodeLayer(obj[3].toString());
            jsonList.add(jsonNode);
        }
        // 构建成树形节点对象
        JSONTreeNode roots = buildJSONTreeNode(jsonList, node);
        //获得json格式树结构
        String strData = JsonBuilder.getInstance().buildList(roots.getChildren(), excludes);
        writeJSON(response, strData);
    }

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
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
        List<DormStudentDorm> lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
        List<DormStudentDorm> newLists = new ArrayList<>();
        StuBaseinfo jwTStudent = null;
        for (DormStudentDorm dormTStudentdorm : lists) {
            jwTStudent = jwTStudentService.get(dormTStudentdorm.getStuId());
            dormTStudentdorm.setXm(jwTStudent.getXm());
            dormTStudentdorm.setUserNumb(jwTStudent.getUserNumb());
            dormTStudentdorm.setClaiName(jwTGradeclassService.get(dormTStudentdorm.getClaiId()).getClassName());
            newLists.add(dormTStudentdorm);
        }
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), newLists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 班级宿舍查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/dormList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void dormList(@ModelAttribute JwClassDormAllot entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        int start = 0; // 起始记录数
        int limit = 0;// 每页记录数
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<JwClassDormAllot> lists = jwTClassdormService.doQuery(hql.toString(), start, limit);// 执行查询方法
        List<JwClassDormAllot> newLists = new ArrayList<>();
        BuildDormDefine jwTDormDefin = null;
        for (JwClassDormAllot jwTClassdorm : lists) {
            jwTDormDefin = jwTDormDefinService.get(jwTClassdorm.getDormId());
            jwTClassdorm.setDormName(jwTDormDefin.getRoomName()); //宿舍名称
            //jwTClassdorm.setDormChestCount(jwTDormDefin.getDormChestCount());//柜子数
            jwTClassdorm.setDormBedCount(jwTDormDefin.getDormBedCount().toString()); //床位数
            jwTClassdorm.setDormType(jwTDormDefin.getDormType());//宿舍类型1男2女3不限
            newLists.add(jwTClassdorm);
        }
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), newLists, false);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * clsStList 查询分班表不在宿舍分配表中的学生 TODO(这里描述这个方法适用条件 – 可选) TODO(这里描述这个方法的执行流程 –
     * 可选) TODO(这里描述这个方法的使用方法 – 可选) TODO(这里描述这个方法的注意事项 – 可选)
     *
     * @Title: clsStList @Description: TODO @param @param entity @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping(value = { "/clsStList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void clsStList(@ModelAttribute JwClassstudent entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " as t where 1=1");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " as t where  1=1");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        int start = super.start(request); // 起始记录数
        int limit = entity.getLimit();// 每页记录数
        hql.append(whereSql + " order by jwTStudent.xbm");
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<JwClassstudent> lists = jwTClassstudentService.doQuery(hql.toString(), start, limit);// 执行查询方法
        List<JwClassstudent> newLists = new ArrayList<>();
        for (JwClassstudent jwTClassstudent : lists) {
            jwTClassstudent.setStudentId(jwTClassstudent.getStudentId());
            jwTClassstudent.setXbm(jwTClassstudent.getXbm());
            jwTClassstudent.setUserNumb(jwTClassstudent.getUserNumb());
            jwTClassstudent.setXm(jwTClassstudent.getXm());
            jwTClassstudent.setClassName(jwTGradeclassService.get(jwTClassstudent.getClaiId()).getClassName());
            newLists.add(jwTClassstudent);
        }
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), newLists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param DormTStudentdorm
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doAdd")
    public void doAdd(DormStudentDorm entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int rs = 0;//人数
        //获取该宿舍下存在的人数
        List<DormStudentDorm> liStudentdorms = thisService.queryByProerties("dormId", entity.getCdormId());
        for (int i = 0; i < liStudentdorms.size(); i++) {
            if (liStudentdorms.get(i).getIsDelete() == 0) {
                ++rs;//获取有效的人数
            }
        }
        BuildDormDefine jwTDormDefin = jwTDormDefinService.get(entity.getCdormId());//获取宿舍信息
        if (rs >= Integer.valueOf(jwTDormDefin.getDormBedCount())) {
            writeJSON(response, jsonBuilder
                    .returnFailureJson("'该宿舍最大人数为：" + jwTDormDefin.getDormBedCount() + "人。现已入住：" + rs + "。'"));
            return;
        }
        int kfp = Integer.valueOf(jwTDormDefin.getDormBedCount()) - rs;
        String[] studentId = entity.getStuId().split(",");
        if (studentId.length > kfp) {
            writeJSON(response, jsonBuilder.returnFailureJson(
                    "'该宿舍最大人数为：" + jwTDormDefin.getDormBedCount() + "人。现已入住：" + rs + "。可分配床位数为：" + kfp + "'"));
            return;
        }
        for (int i = 0; i < studentId.length; i++) {
            String userCh = "超级管理员";
            SysUser currentUser = getCurrentSysUser();
            if (currentUser != null)
                userCh = currentUser.getXm();
            // 如果界面有了排序号的输入，则不需要取默认的了
            Integer orderIndex = thisService.getDefaultOrderIndex(entity);
            entity.setOrderIndex(orderIndex);// 排序
            entity.setStuId(studentId[i]); //设置学生id
            entity.setCdormId(entity.getCdormId());//设置宿舍id
            entity.setBedNum(entity.getBedNum() + 1);//床号
            entity.setArkNum(entity.getBedNum());//柜号（默认跟床号对应）
            entity.setCreateUser(userCh); // 创建人
            entity.setCreateTime(new Date());//创建时间
            entity.setInTime(new Date());//入住时间
            // 持久化到数据库
            thisService.merge(entity);
        }
        // 返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson("'分配成功。'"));
    }

    /**
     * doDelete @Title: doDelete @Description: TODO @param @param
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
                flag = thisService.logicDelOrRestore(delIds[i], StatuVeriable.ISDELETE);
            }
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
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * DormTStudentdorm @param @param request @param @param
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
                //perEntity.setModifyUser(userCh); // 设置修改人的中文名
                thisService.merge(perEntity);// 执行修改方法
                ++count;
            }
        }
        if (count > 0) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'保存成功。'"));
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("'保存失败。'"));
        }
    }

    /**
     * 推送消息 classStuTs(这里用一句话描述这个方法的作用) TODO(这里描述这个方法适用条件 – 可选)
     * TODO(这里描述这个方法的执行流程 – 可选) TODO(这里描述这个方法的使用方法 – 可选) TODO(这里描述这个方法的注意事项 –
     * 可选)
     *
     * @Title: classStuTs @Description: TODO @param @param id @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dormTs")
    public void dormTs(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] str = { "claiId", "isDelete" };
        Object[] str2 = { id, 0 };
        List<DormStudentDorm> newLists = thisService.queryByProerties(str, str2);
        PushInfo pushInfo = new PushInfo();
        StuBaseinfo jwTStudent = null;
        BuildDormDefine jwTDormDefin = null;
        BuildRoomarea jwTRoomarea = null;
        String roomName = null;
        String areaName = null;//楼栋名
        String areaLc = null;//楼层

        for (DormStudentDorm dormTStudentdorm : newLists) {
            jwTStudent = jwTStudentService.get(dormTStudentdorm.getStuId()); //学生信息
            jwTDormDefin = jwTDormDefinService.get(dormTStudentdorm.getCdormId());//获取宿舍定义信息
            roomName = jwTDormDefin.getRoomName();//房间名
            jwTRoomarea = areaService.getByProerties("areaId", jwTDormDefin.getAreaId());//楼层对象
            areaLc = jwTDormDefin.getAreaName();//楼层名
            areaName = jwTClassdormService.get(dormTStudentdorm.getUuid()).getAreaName();
            pushInfo.setEmplName(jwTStudent.getXm());//姓名
            pushInfo.setEmplNo(jwTStudent.getUserNumb());//学号
            pushInfo.setRegTime(new Date());
            pushInfo.setEventType("宿舍信息");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            pushInfo.setRegStatus("学生：" + pushInfo.getEmplName() + "，你的宿舍分配在" + areaName + "，" + areaLc + "，" + roomName
                    + "房间，床号为：" + dormTStudentdorm.getBedNum());
            pushService.merge(pushInfo);
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'推送信息成功。'"));
    }

    /**
     * 构建树形对象.
     *
     * @param list
     *            树形列表对象
     * @param rootId
     *            树形的根节点
     * @return the JSON tree node
     */
    public JSONTreeNode buildJSONTreeNode(List<JSONTreeNode> list, String rootId) {
        JSONTreeNode root = new JSONTreeNode();
        for (JSONTreeNode node : list) {
            if (!(StringUtils.isNotEmpty(node.getParent()) && !node.getId().equals(rootId))) {
                root = node;
                list.remove(node);
                break;
            }
        }
        createTreeChildren(list, root);
        return root;
    }

    /**
     * 分数表查询 scoreTableList(这里用一句话描述这个方法的作用) TODO(这里描述这个方法适用条件 – 可选)
     * TODO(这里描述这个方法的执行流程 – 可选) TODO(这里描述这个方法的使用方法 – 可选) TODO(这里描述这个方法的注意事项 –
     * 可选)
     *
     * @Title: scoreTableList @Description: TODO @param @param
     * entity @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/scoreTableList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void scoreTableList(@ModelAttribute StuDividescore entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1");
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
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
        List<StuDividescore> lists = studentTDividescoreService.doQuery(hql.toString(), start, limit);// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 创建指定节点的子节点.
     *
     * @param childrens
     *            字节点清单
     * @param root
     *            作为根的指定节点
     */
    private void createTreeChildren(List<JSONTreeNode> childrens, JSONTreeNode root) {
        String parentId = root.getId();
        for (int i = 0; i < childrens.size(); i++) {
            JSONTreeNode node = childrens.get(i);
            if (StringUtils.isNotEmpty(node.getParent()) && node.getParent().equals(parentId)) {
                root.getChildren().add(node);
                createTreeChildren(childrens, node);
            }
            if (i == childrens.size() - 1) {
                if (root.getChildren().size() < 1) {
                    root.setLeaf(true);
                }
                return;
            }
        }
    }
}
