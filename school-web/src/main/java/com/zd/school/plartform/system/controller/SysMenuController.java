
package com.zd.school.plartform.system.controller;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysMenu;
import com.zd.school.plartform.system.model.SysMenuChkTree;
import com.zd.school.plartform.system.model.SysMenuTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: BaseTMenuController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 系统菜单表实体Controller. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BaseMenu")
public class SysMenuController extends FrameWorkController<SysMenu> implements Constant {

    @Resource
    private SysMenuService thisService; // service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/treelist")
    public void getTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        String orderSql = request.getParameter("orderSql");

        SysUser currentUser = getCurrentSysUser();
        List<SysMenuTree> lists = new ArrayList<SysMenuTree>();
        if (currentUser.getUserName().equals("administrator"))
            lists = thisService.getTreeList(whereSql, orderSql);
        else {
            whereSql += " and isnull(isHidden,'0')='0'";
            lists = thisService.getTreeList(whereSql, orderSql);
        }

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * getRoleMenuList:获取指定角色的授权菜单.
     * 
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/rolemenulist")
    public void getRoleMenuList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String roleId = request.getParameter("roleId");

        List<SysMenuTree> lists = thisService.getRoleMenuTreeList(roleId);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * getUserCanPerissonMenuToRole:获取指定用户对指定角色的可授权菜单.
     * 要从用户所有的权限菜单中排除掉已对指定角色授权的菜单.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/userpertorole")
    public void getUserCanPermissionMenuToRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = "";
        String roleId = request.getParameter("roleId"); //要授权的角色
        if(roleId==null){
        	 strData = JsonBuilder.getInstance().buildList( new ArrayList<>(), "");// 处理数据
        	 writeJSON(response,strData);// 返回数据
        	 return;
        }
        SysUser currentUser = getCurrentSysUser();

        List<SysMenuChkTree> lists = thisService.getUserPermissionToRole(roleId, currentUser.getUuid());

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BaseTMenu
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(SysMenu entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	
        Integer orderIndex = entity.getOrderIndex();
        String menuName = entity.getNodeText();
        String menuCode = entity.getMenuCode();
        String parentNode = entity.getParentNode();
        try{
	        //此处为放在入库前的一些检查的代码，如唯一校验等
	        String hql = " o.isDelete='0'";
	        if (thisService.IsFieldExist("menuCode", menuCode, "-1", hql)) {
	            writeJSON(response, jsonBuilder.returnFailureJson("'菜单编码不能重复！'"));
	            return;
	        }
	        if (thisService.IsFieldExist("nodeText", menuName, "-1", hql)) {
	            writeJSON(response, jsonBuilder.returnFailureJson("'菜单名称不能重复！'"));
	            return;
	        }
	        String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
	        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), "-1", hql1)) {
	            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
	            return;
	        }
	        //获取当前操作用户
	        String userCh = "超级管理员";
	        SysUser currentUser = getCurrentSysUser();
	        //        if (currentUser != null)
	        //            userCh = currentUser.getXm();
	        //
	        //        SysMenu saveEntity = new SysMenu();
	        //        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);
	        //        //增加时要设置创建人
	        //        entity.setCreateUser(userCh); //创建人
	        //        entity.setLeaf(true);
	        //持久化到数据库
	        entity = thisService.addMenu(entity, currentUser);
	
	        //返回实体到前端界面
	        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
        } catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"请求失败，请联系管理员\""));
		}
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * BaseTMenu @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(SysMenu entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        Integer orderIndex = entity.getOrderIndex();
        String menuName = entity.getNodeText();
        String menuCode = entity.getMenuCode();
        String parentName = entity.getParentName();
        String parentNode = entity.getParentNode();
        String uuid = entity.getUuid();
        //此处为放在入库前的一些检查的代码，如唯一校验等
        String hql = " o.isDelete='0'";
        if (thisService.IsFieldExist("menuCode", menuCode, uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'菜单编码不能重复！'"));
            return;
        }
        if (thisService.IsFieldExist("nodeText", menuName, uuid, hql)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'菜单名称不能重复！'"));
            return;
        }
        String hql1 = " o.isDelete='0' and o.parentNode='" + parentNode + "' ";
        if (thisService.IsFieldExist("orderIndex", orderIndex.toString(), uuid, hql1)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'同一级别已有此顺序号！'"));
            return;
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String a[]={"123","2"};
        
        //先拿到已持久化的实体
        SysMenu perEntity = thisService.get(entity.getUuid());
        boolean isLeaf = perEntity.getLeaf();
        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        perEntity.setLeaf(isLeaf);
        entity = thisService.doMerge(perEntity);//执行修改方法
        entity.setParentName(parentName);
        entity.setParentNode(parentNode);
        //更新父节点的是否叶节点的标记
        SysMenu parentMenu = thisService.get(parentNode);
        if(parentMenu!=null){
        	parentMenu.setUpdateTime(new Date()); //设置修改时间
        	parentMenu.setUpdateUser(userCh); //设置修改人的中文名
        	parentMenu.setLeaf(false);
        	thisService.doMerge(parentMenu);//执行修改方法
        }
     

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

    /**
     * 
     * setUse:解锁菜单.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/setlockflag")
    public void setLockFlag(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        String lockFlag = request.getParameter("lockFlag");
        if (StringUtils.isEmpty(delIds)) {
            if (lockFlag.equals("0"))
                writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解锁的菜单'"));
            else
                writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要锁定的菜单'"));
            return;
        } else {
            delIds = "'" + delIds.replace(",", "','") + "'";
            String hql = " update SysMenu set isHidden='" + lockFlag + "'  where uuid in (" + delIds + ") ";
            thisService.doExecuteHql(hql);

            writeJSON(response, jsonBuilder.returnSuccessJson("'处理成功'"));
        }
    }
}
