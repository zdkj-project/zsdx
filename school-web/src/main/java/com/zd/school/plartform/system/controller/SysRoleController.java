
package com.zd.school.plartform.system.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysMenuService;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: BaseTRoleController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 角色管理实体Controller. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/sysrole")
public class SysRoleController extends FrameWorkController<SysRole> implements Constant {

    @Resource
    private SysRoleService thisService; // service层接口

    @Resource
    private SysUserService userSerive;

    @Resource
    private SysMenuService menuService;

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute SysRole entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String filter = super.filter(request);
        List<ExtDataFilter> listFilters = new ArrayList<ExtDataFilter>();
        if (StringUtils.isNotEmpty(filter)) {
            listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter, ExtDataFilter.class);
        }
        ExtDataFilter hideDataFilter = new ExtDataFilter();
        hideDataFilter.setComparison("=");
        hideDataFilter.setField("isHidden");
        hideDataFilter.setType("string");
        hideDataFilter.setValue("0");

        SysUser currentUser = getCurrentSysUser();
        if (!currentUser.getUserName().equals("administrator"))
            listFilters.add(hideDataFilter);
        String newFilter = jsonBuilder.buildObjListToJson(Long.valueOf(listFilters.size()), listFilters, false);
        QueryResult<SysRole> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), newFilter, true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BaseTRole
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(SysRole entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String roleName = entity.getRoleName();
        String roleCode = entity.getRoleCode();
        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0'";
        if (thisService.IsFieldExist("roleName", roleName, "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'角色名称不能重复！'"));
            return;
        }
        if (thisService.IsFieldExist("roleCode", roleCode, "-1", hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'角色编码不能重复！'"));
            return;
        }
        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        SysRole saveEntity = new SysRole();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        //增加时要设置创建人
        entity.setCreateUser(userCh); //创建人
        entity.setIsHidden("0");
        entity.setIssystem(1);

        //持久化到数据库
        entity = thisService.merge(entity);

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
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
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
     * BaseTRole @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(SysRole entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String roleName = entity.getRoleName();
        String roleCode = entity.getRoleCode();
        String roleId = entity.getUuid();
        //入库前检查代码
        String hql = " o.isDelete='0'";
        if (thisService.IsFieldExist("roleName", roleName, roleId, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'角色名称不能重复！'"));
            return;
        }
        if (thisService.IsFieldExist("roleCode", roleCode, roleId, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'角色编码不能重复！'"));
            return;
        }
        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        SysRole perEntity = thisService.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.merge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

    /**
     * 
     * selectList:用户所属角色选择时的可选择角色.
     *
     * @author luoyibo
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping(value = { "/selectlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void selectList(@ModelAttribute SysRole entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String userId = request.getParameter("userId");
        int start = super.start(request); // 起始记录数
        int limit = super.limit(request);// 每页记录数

        Set<SysRole> userRole = userSerive.get(userId).getSysRoles();
        // hql语句
        StringBuffer hql = new StringBuffer("from SysRole e where isDelete=0 ");
        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from SysRole e where isDelete=0 ");
        List<SysRole> lists = new ArrayList<SysRole>();
        Integer count = 0;
        if (userRole.size() > 0) {
            hql.append("and e not in(:roles)");
            countHql.append("and e not in(:roles)");
            lists = thisService.doQuery(hql.toString(), start, limit, "roles", userRole.toArray());// 执行查询方法
            count = thisService.getCount(countHql.toString(), "roles", userRole.toArray());// 查询总记录数
        } else {
            lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
            count = thisService.getCount(countHql.toString());// 查询总记录数
        }

        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据        
    }

    /**
     * 
     * cancelRoleRightMenu:取消指定用户的授权菜单.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/deleteright")
    public void cancelRoleRightMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roleId = request.getParameter("roleId");
        String cancelId = request.getParameter("ids");

        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(cancelId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入取消权限的数据'"));
            return;
        } else {
            boolean flag = menuService.cancelRoleRightMenu(roleId, cancelId);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'取消权限成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'取消权限失败'"));
            }
        }
    }

    /**
     * 
     * addRoleRightMenu:增加指定角色的授权菜单.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/addright")
    public void addRoleRightMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roleId = request.getParameter("roleId");
        String addId = request.getParameter("ids");

        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(addId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入增加权限的数据'"));
            return;
        } else {
            boolean flag = menuService.addRoleRightMenu(roleId, addId);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'增加权限成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'增加权限失败'"));
            }
        }
    }
}
