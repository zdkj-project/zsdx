
package com.zd.school.build.define.controller;
import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.model.BuildRoomAreaTree;
import com.zd.school.build.define.model.BuildRoomarea;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.plartform.system.model.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: BuildRoomareaController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 教室区域实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BuildRoomarea")
public class BuildRoomareaController extends FrameWorkController<BuildRoomarea> implements Constant {

    @Resource
    BuildRoomareaService thisService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute BuildRoomarea entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String excludes = request.getParameter("excludes");

        List<BuildRoomAreaTree> lists = thisService.getBuildAreaList(request.getParameter("whereSql"));

        strData = JsonBuilder.getInstance().buildList(lists, excludes);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BuildRoomarea
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(BuildRoomarea entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String parentNode = entity.getParentNode();
        String parentName = entity.getParentName();
        String parentType = entity.getParentType();
        String nodeText = entity.getNodeText();
        Integer orderIndex = entity.getOrderIndex();

        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), "-1", hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
            return;
        }
        if (thisService.IsFieldExist("nodeText", nodeText, "-1", hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此区域！'"));
            return;
        }
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        BuildRoomarea perEntity = new BuildRoomarea();
        BeanUtils.copyPropertiesExceptNull(entity, perEntity);

        entity.setCreateUser(currentUser.getXm()); // 创建人
        entity.setLeaf(true);
        if (!parentNode.equals(TreeVeriable.ROOT)) {
            BuildRoomarea parEntity = thisService.get(parentNode);
            parEntity.setLeaf(false);
            thisService.doMerge(parEntity);
            entity.BuildNode(parEntity);
        } else
            entity.BuildNode(null);

        entity = thisService.doMerge(entity);
        entity.setParentName(parentName);
        entity.setAreaType(parentType);
        entity.setParentNode(parentNode);

        //返回实体到前端界面
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
     * BuildRoomarea @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(BuildRoomarea entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String uuid = entity.getUuid();
        String parentNode = entity.getParentNode();
        String parentName = entity.getParentName();
        String parentType = entity.getParentType();
        String nodeText = entity.getNodeText();
        Integer orderIndex = entity.getOrderIndex();

        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), uuid, hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
            return;
        }
        if (thisService.IsFieldExist("nodeText", nodeText, uuid, hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此区域！'"));
            return;
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        //entity.getSchoolId()要自己修改成对应的获取主键的方法
        BuildRoomarea perEntity = thisService.get(entity.getUuid());
        Boolean isLeaf = perEntity.getLeaf();
        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        perEntity.setLeaf(isLeaf);
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
}
