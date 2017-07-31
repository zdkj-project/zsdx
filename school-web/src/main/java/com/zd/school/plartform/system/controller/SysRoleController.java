
package com.zd.school.plartform.system.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysMenuService;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
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

    private static Logger logger = Logger.getLogger(SysRoleController.class);

    @Resource
    private SysRoleService thisService; // service层接口

    @Resource
    private SysUserService userSerive;

    @Resource
    private SysMenuService menuService;

    /**
     * list查询 @Title: list
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
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
     * 增加记录
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/doadd")
    public void doAdd(SysRole entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roleName = entity.getRoleName();
        String roleCode = entity.getRoleCode();
        try {
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
            //获取当前的操作用户
            SysUser currentUser = getCurrentSysUser();
            //插入数据
            entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
            if (ModelUtil.isNotNull(entity))
                writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
            else {
                writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
        }
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
     * 更新记录
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */

    @RequestMapping("/doupdate")
    public void doUpdates(SysRole entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String roleName = entity.getRoleName();
        String roleCode = entity.getRoleCode();
        String roleId = entity.getUuid();
        try {
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
            SysUser currentUser = getCurrentSysUser();
            //更新数据
            entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
            if (ModelUtil.isNotNull(entity))
                writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
            else
                writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
        }

    }

    /**
     * selectList:用户所属角色选择时的可选择角色.
     *
     * @param entity
     * @param request
     * @param response
     * @throws IOException void
     * @throws @since      JDK 1.8
     * @author luoyibo
     */
    @RequestMapping(value = {"/selectlist"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
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
     * cancelRoleRightMenu:取消指定用户的授权菜单.
     *
     * @param request
     * @param response
     * @throws IOException void
     * @throws @since      JDK 1.8
     * @author luoyibo
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
     * addRoleRightMenu:增加指定角色的授权菜单.
     *
     * @param request
     * @param response
     * @throws IOException void
     * @throws @since      JDK 1.8
     * @author luoyibo
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

    /**
     * 获取指定角色的用户列表
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getRoleUser")
    public void  getRoleUser(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        String strData = "";
        Integer start = super.start(request);
        Integer limit = super.limit(request);
        String roleId = request.getParameter("ids");
        QueryResult<SysUser> qResult = thisService.getRoleUser(roleId, start, limit);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 删除指定角色的用户
     * @param ids 要删除用户的角色
     * @param userId 删除的用户Id,多个Id用英文逗号隔开
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/doDeleteRoleUser")
    public void doDeleteRoleUser(String ids, String userId,HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(StringUtils.isEmpty(ids) || StringUtils.isEmpty(userId)){
            writeJSON(response,jsonBuilder.returnFailureJson("'没有传入相关的参数:角色标识或删除的用户标识'"));
            return;
        }
        try {
            Boolean flag = thisService.doDeleteRoleUser(ids,userId);
            if(flag)
                writeJSON(response,jsonBuilder.returnSuccessJson("'角色用户删除成功'"));
            else
                writeJSON(response,jsonBuilder.returnFailureJson("角色用户删除失败，详情请见错误日志"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            writeJSON(response,jsonBuilder.returnFailureJson("角色用户删除失败，详情请见错误日志"));
        }
    }

    /**
     * 给指定的角色添加用户
     * @param ids 要添加用户的角色Id
     * @param userId 添加的用户Id,多个Id用英文逗号隔开
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/doAddRoleUser")
    public void doAddRoleUser(String ids, String userId,HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(StringUtils.isEmpty(ids) || StringUtils.isEmpty(userId)){
            writeJSON(response,jsonBuilder.returnFailureJson("'没有传入相关的参数:角色标识或s要添加的用户标识'"));
            return;
        }
        try {
            Boolean flag = thisService.doAddRoleUser(ids,userId);
            if(flag)
                writeJSON(response,jsonBuilder.returnSuccessJson("'角色用户添加成功'"));
            else
                writeJSON(response,jsonBuilder.returnFailureJson("角色用户添加失败，详情请见错误日志"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            writeJSON(response,jsonBuilder.returnFailureJson("角色用户添加失败，详情请见错误日志"));
        }
    }
}
