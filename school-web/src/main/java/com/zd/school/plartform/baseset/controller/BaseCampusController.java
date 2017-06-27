
package com.zd.school.plartform.baseset.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.plartform.baseset.model.BaseCampus;
import com.zd.school.plartform.baseset.service.BaseCampusService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseCampusController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 校区信息实体Controller. date: 2016-08-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseCampus")
public class BaseCampusController extends FrameWorkController<BaseCampus> implements Constant {

    @Resource
    BaseCampusService thisService; // service层接口

    @Resource
    private BuildRoomareaService areaService;

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute BaseCampus entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<BaseCampus> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BaseCampus
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(BaseCampus entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String campusName = entity.getCampusName();
        String campusCode = entity.getCampusCode();
        String schoolId = entity.getSchoolId();
        String schoolName = entity.getSchoolName();
        Integer orderIndex = entity.getOrderIndex();

        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0' and o.schoolId='" + schoolId + "'";
        if (thisService.IsFieldExist("campusName", campusName, "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'已存在此校区名称！'"));
            return;
        }
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'已存在此顺序号！'"));
            return;
        }
        if (StringUtils.isNotEmpty(campusCode)) {
            if (thisService.IsFieldExist("campusCode", campusCode, "-1", hql)) {
                writeJSON(response, jsonBuilder.returnFailureJson("'已存在此校区编码！'"));
                return;
            }
        }

        SysUser currentUser = getCurrentSysUser();

        //持久化到数据库
        entity = thisService.doAdd(entity, currentUser);
        if (ModelUtil.isNotNull(entity)) {
            entity.setSchoolName(schoolName);
            //返回实体到前端界面
            writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("'增加校区失败'"));
        }
    }

    /**
     * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String delIds = request.getParameter("ids");
        SysUser currentUser = getCurrentSysUser();
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doDelete(delIds, currentUser);
            //flag = areaService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'校区关联有部门或建筑物区域,不能删除'"));
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
     * BaseCampus @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(BaseCampus entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String campusName = entity.getCampusName();
        String campusCode = entity.getCampusCode();
        String schoolId = entity.getSchoolId();
        String schoolName = entity.getSchoolName();
        String uuid = entity.getUuid();
        Integer orderIndex = entity.getOrderIndex();

        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0' and o.schoolId='" + schoolId + "'";
        if (thisService.IsFieldExist("campusName", campusName, uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'已存在此校区名称！'"));
            return;
        }
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'已存在此顺序号!'"));
            return;
        }
        if (StringUtils.isNotEmpty(campusCode)) {
            if (thisService.IsFieldExist("campusCode", campusCode, uuid, hql)) {
                writeJSON(response, jsonBuilder.returnFailureJson("'已存在此校区编码！'"));
                return;
            }
        }
        SysUser currentUser = getCurrentSysUser();

        //持久化到数据库
        entity = thisService.doUpdate(entity, currentUser);
        if (ModelUtil.isNotNull(entity)) {
            entity.setSchoolName(schoolName);
            //返回实体到前端界面
            writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
        } else {
            writeJSON(response, jsonBuilder.returnFailureJson("'修改校区失败'"));
        }

    }
}
