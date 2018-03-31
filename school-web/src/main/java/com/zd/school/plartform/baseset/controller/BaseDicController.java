
package com.zd.school.plartform.baseset.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseDic;
import com.zd.school.plartform.baseset.model.BaseDicTree;
import com.zd.school.plartform.baseset.service.BaseDicService;
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
 * ClassName: BaseDicController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 数据字典实体Controller. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseDic")
public class BaseDicController extends FrameWorkController<BaseDic> implements Constant {

    @Resource
    BaseDicService thisService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute BaseDic entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        // QueryResult<BaseDic> qr =
        // thisService.doPaginationQuery(super.start(request),
        // super.limit(request),
        // super.sort(request), super.filter(request), true);
        List<BaseDicTree> lists = thisService.getDicTreeList(request.getParameter("whereSql"));

        // strData = jsonBuilder.buildObjListToJson(Long.valueOf(lists.size()),
        // lists, true);// 处理数据
        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BaseDic
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(BaseDic entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String parentNode = entity.getParentNode();
        String parentName = entity.getParentName();
        String nodeText = entity.getNodeText();
        String dicCode = entity.getDicCode();
        Integer orderIndex = entity.getOrderIndex();
        // 此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0'";
        if (thisService.IsFieldExist("nodeText", nodeText, "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'字典名称不能重复！'"));
            return;
        }
        if (thisService.IsFieldExist("dicCode", dicCode, "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'字典编码不能重复！'"));
            return;
        }
        String hql1 = " o.isDelete='0' and parentNode='" + parentNode + "' ";
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), "-1", hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
            return;
        }
        // 获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //当前节点
        BaseDic saveEntity = new BaseDic();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人
        entity.setLeaf(true);

        //BaseDic parEntity = thisService.get(parentNode);
        if (!parentNode.equals(TreeVeriable.ROOT)) {
            BaseDic parEntity = thisService.get(parentNode);
            parEntity.setLeaf(false);
            thisService.doMerge(parEntity);

            entity.BuildNode(parEntity);
        } else
            entity.BuildNode(null);

        // 持久化到数据库
        entity = thisService.doMerge(entity);
        entity.setParentName(parentName);
        entity.setParentNode(parentNode);

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
     * BaseDic @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(BaseDic entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String parentNode = entity.getParentNode();
        String parentName = entity.getParentName();
        String nodeText = entity.getNodeText();
        String dicCode = entity.getDicCode();
        String uuid = entity.getUuid();
        Integer orderIndex = entity.getOrderIndex();
        // 此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0'";
        if (thisService.IsFieldExist("nodeText", nodeText, uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'字典名称不能重复！'"));
            return;
        }
        if (thisService.IsFieldExist("dicCode", dicCode, uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'字典编码不能重复！'"));
            return;
        }
        String hql1 = " o.isDelete='0' and parentNode='" + parentNode + "' ";
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), uuid, hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
            return;
        }
        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        BaseDic perEntity = thisService.get(entity.getUuid());
        Boolean isLeaf = perEntity.getLeaf();
        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        perEntity.setLeaf(isLeaf);

        entity = thisService.doMerge(perEntity);// 执行修改方法

        entity.setParentName(parentName);
        entity.setParentNode(parentNode);

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }
}
