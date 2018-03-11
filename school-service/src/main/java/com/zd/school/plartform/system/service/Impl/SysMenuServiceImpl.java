package com.zd.school.plartform.system.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.constant.AuthorType;
import com.zd.core.constant.MenuType;
import com.zd.core.constant.PermType;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.ModelUtil;
import com.zd.school.plartform.system.dao.SysMenuDao;
import com.zd.school.plartform.system.model.SysMenu;
import com.zd.school.plartform.system.model.SysMenuChkTree;
import com.zd.school.plartform.system.model.SysMenuTree;
import com.zd.school.plartform.system.model.SysPermission;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysMenuService;
import com.zd.school.plartform.system.service.SysPerimissonService;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: BaseTMenuServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 系统菜单表实体Service接口实现类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    @Resource
    public void setBaseTMenuDao(SysMenuDao dao) {
        this.dao = dao;
    }

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysPerimissonService perimissonSevice;

    /**
     * 
     * listTree:获取系统菜单的树形列表
     *
     * @author luoyibo
     * @param whereSql
     *            :查询条件
     * @param orderSql
     *            :排序条件
     * @param parentSql:
     * @param querySql
     * @return List<SysMenuTree>
     * @throws @since
     *             JDK 1.8
     */

    @Override
    public List<SysMenuTree> getTreeList(String whereSql, String orderSql) {

        StringBuffer hql = new StringBuffer("from SysMenu where 1=1");
        hql.append(whereSql);
        hql.append(orderSql);

        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from SysMenu where 1=1");
        countHql.append(whereSql);

        List<SysMenu> typeList = super.doQuery(hql.toString());
        List<SysMenuTree> result = new ArrayList<SysMenuTree>();
        // 构建Tree数据
        recursion(new SysMenuTree(TreeVeriable.ROOT, new ArrayList<SysMenuTree>()), result, typeList);

        return result;
    }

    private void recursion(SysMenuTree parentNode, List<SysMenuTree> result, List<SysMenu> list) {
        List<SysMenu> childs = new ArrayList<SysMenu>();
        for (SysMenu SysMenu : list) {
            if (SysMenu.getParentNode().equals(parentNode.getId())) {
                childs.add(SysMenu);
            }
        }
        for (SysMenu SysMenu : childs) {
            SysMenuTree child = new SysMenuTree(SysMenu.getUuid(), SysMenu.getNodeText(), "", SysMenu.getLeaf(),
                    SysMenu.getNodeLevel(), SysMenu.getTreeIds(),SysMenu.getParentNode(),SysMenu.getOrderIndex(), new ArrayList<SysMenuTree>(), SysMenu.getMenuCode(),
                    SysMenu.getSmallIcon(), SysMenu.getBigIcon(), SysMenu.getMenuTarget(), SysMenu.getMenuType(),
                    SysMenu.getMenuLeaf(), SysMenu.getNodeCode(), SysMenu.getIssystem(), SysMenu.getIsHidden());
            if (SysMenu.getParentNode().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<SysMenuTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            recursion(child, result, list);
        }

    }

    /**
     * 获取指定对象的授权树
     * 
     * @param roodId
     *            授权树的根节点
     * @param author
     *            授权对象ID
     * @param authorType
     *            授权类型
     * @param isSee
     *            是否可见
     * @param expanded
     *            是否展开
     * @return
     */
    @Override
    public List<SysMenuTree> getPermTree(String roodId, String author, String authorType, Boolean isSee,
            Boolean expanded) {
        Boolean isAdmin = false;
        String hql = "from SysMenu ";
        //除了超级管理员用户，其它的只能看未隐藏的菜单
        if (!author.equals("8a8a8834533a065601533a065ae80000"))
            hql += " where isHidden='0'";
        hql += " order by parentNode,orderIndex asc ";
        List<SysMenu> lists = this.doQuery(hql.toString());
        //对于超级管理员的用户与角色，默认有所有菜单的权限
        if (authorType.equals(AuthorType.ROLE)) {
            SysRole thisRole = sysRoleService.get(author);
            if (thisRole.getRoleCode().equals("ROLE_ADMIN"))
                isAdmin = true;
        } else {
            SysUser thisUser = sysUserService.get(author);
            if (thisUser.getUserName().equals("administrator"))
                isAdmin = true;
        }
        // 得到当前对象的权限
        Map<String, SysPermission> maps = buildPermMap(author, authorType);
        if (maps == null) {
            return null;
        }
        //非超级管理员账户
        if (!isAdmin) {
            List<SysMenu> removeLists = new ArrayList<SysMenu>();
            for (SysMenu node : lists) {
                if (isSee) {
                    if (maps.get(node.getUuid()) == null && !node.getUuid().equalsIgnoreCase(TreeVeriable.ROOT)) {
                        removeLists.add(node);
                    }
                } else {
                }
            }
            if (isSee) {
                for (SysMenu node : removeLists) {
                    lists.remove(node);
                }
            }
        }
        List<SysMenuTree> result = new ArrayList<SysMenuTree>();
        // 构建Tree数据
        recursion(new SysMenuTree(TreeVeriable.ROOT, new ArrayList<SysMenuTree>()), result, lists);

        return result;

    }

    // 构建权限map
    private Map<String, SysPermission> buildPermMap(String author, String authorType) {
        Map<String, SysPermission> maps = new HashMap<String, SysPermission>();
        if (AuthorType.ROLE.equalsIgnoreCase(authorType)) {
            SysRole sysRole = sysRoleService.get(author);
            if (sysRole != null) {
                Set<SysPermission> perms = sysRole.getSysPermissions();
                for (SysPermission perm : perms) {
                    maps.put(perm.getPerCode(), perm);
                }
            }
        } else {
            SysUser user = sysUserService.get(author);
            if (user != null) {
                // 得到角色
                //Set<SysRole> roles = user.getSysRoles();
            	List<SysRole> roles = sysUserService.getSysRoleList(user);
                for (SysRole role : roles) {
                    // 得到指定角色的权限
                    //Set<SysPermission> perms = role.getSysPermissions();
                	List<SysPermission> perms = sysRoleService.getSysPermissionList(role);
       
                    for (SysPermission perm : perms) {
                        maps.put(perm.getPerCode(), perm);
                    }
                }
            }
        }
        return maps;
    }

    @Override
    public List<SysMenuTree> getRoleMenuTreeList(String roleId) {
        //当前角色有权限的菜单
        Set<SysPermission> rolePerimisson = sysRoleService.get(roleId).getSysPermissions();
        //String hql = "from SysMenu e where e.isHidden='0' and  e.uuid in (select o.perCode from SysPermission o where o in (:roleRight)) ";
        String hql = "from SysMenu e where e.uuid in (select o.perCode from SysPermission o where o in (:roleRight)) ";
        //非超级管理员要排除掉隐藏的菜单
        if (!roleId.equals("8a8a8834533a0f8a01533a0f8e220000"))
            hql += " and e.isHidden='0'";
        List<SysMenu> lists = new ArrayList<SysMenu>();
        List<SysMenuTree> result = new ArrayList<SysMenuTree>();
        if (rolePerimisson.size() > 0) {
            lists = this.doQuery(hql.toString(), 0, 999, "roleRight", rolePerimisson.toArray());// 执行查询方法
            recursion(new SysMenuTree(TreeVeriable.ROOT, new ArrayList<SysMenuTree>()), result, lists);
        }
        return result;
    }

    /**
     * 
     * cancelRoleRightMenu:取消指定角色的菜单权限).
     *
     * @author luoyibo
     * @param roleId
     * @param cancelMenuId
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    @Override
    public Boolean cancelRoleRightMenu(String roleId, String cancelMenuId) {
        Boolean doResult = false;

        String menuIds = "'" + cancelMenuId.replace(",", "','") + "'";
        String hql = " from SysPermission a where a.perCode in (" + menuIds + ") and a.perType='" + PermType.TYPE_MENU
                + "'";
        List<SysPermission> cancelPerimission = perimissonSevice.doQuery(hql);

        SysRole cancelRole = sysRoleService.get(roleId);
        Set<SysPermission> rolePermission = cancelRole.getSysPermissions();
        rolePermission.removeAll(cancelPerimission);

        cancelRole.setSysPermissions(rolePermission);
        sysRoleService.merge(cancelRole);

        doResult = true;

        return doResult;
    }

    /**
     * 
     * addRoleRightMenu给指定的角色增加权限菜单 .
     *
     * @author luoyibo
     * @param roleId
     * @param addMenuid
     * @return Boolean
     * @throws @since
     *             JDK 1.8
     */
    @Override
    public Boolean addRoleRightMenu(String roleId, String addMenuid) {
        Boolean doResult = false;

        String[] addMenuIds = addMenuid.split(",");

        //要增加权限菜单的角色及已有权限菜单信息
        SysRole addRoleEntity = sysRoleService.get(roleId);
        Set<SysPermission> rolePermission = addRoleEntity.getSysPermissions();

        //要添加的菜单的权限清单
        List<SysMenu> addMenuEntity = this.queryByProerties("uuid", addMenuIds);
        String[] propName = { "perType", "perCode" };
        String[] propValue = new String[] {};
        Set<SysPermission> addPerimisson = new HashSet<SysPermission>();
        for (SysMenu sysMenu : addMenuEntity) {
            String perCode = sysMenu.getUuid();
            propValue = new String[] { PermType.TYPE_MENU, perCode };
            SysPermission isPeriminsson = perimissonSevice.getByProerties(propName, propValue);
            //当前菜单在权限资源表中
            if (ModelUtil.isNotNull(isPeriminsson)) {
                addPerimisson.add(isPeriminsson);
            } else {
                //当前菜单不在权限资源中，需要先增加权限资源
                SysPermission newPermisson = new SysPermission();
                newPermisson.setPerCode(perCode);
                newPermisson.setPerType(PermType.TYPE_MENU);

                newPermisson = perimissonSevice.merge(newPermisson);
                addPerimisson.add(newPermisson);
            }
        }
        //rolePermission.removeAll(rolePermission);
        rolePermission.addAll(addPerimisson);

        addRoleEntity.setSysPermissions(rolePermission);
        sysRoleService.merge(addRoleEntity);

        doResult = true;

        return doResult;
    }

    /**
     * 
     * getPermissionMenu:获取指定对象有权限的菜单.
     *
     * @author luoyibo
     * @param roodId
     * @param author
     * @param authorType
     * @return List<SysMenu>
     * @throws @since
     *             JDK 1.8
     */
    private List<SysMenu> getPermissionMenu(String author, String authorType) {
        String hql = "from SysMenu where isHidden='0'";

        //查询出有效的菜单
        List<SysMenu> lists = super.doQuery(hql.toString());

        //对于超级管理员的用户与角色，默认有所有菜单的权限
        if (authorType.equals(AuthorType.ROLE)) {
            SysRole thisRole = sysRoleService.get(author);
            if (thisRole.getRoleCode().equals("ROLE_ADMIN"))
                return lists;
        } else {
            SysUser thisUser = sysUserService.get(author);
            if (thisUser.getUserName().equals("administrator"))
                return lists;
        }

        // 对于非超级管理员，得到当前对象的权限
        Map<String, SysPermission> maps = buildPermMap(author, authorType);
        if (maps == null) {
            return null;
        }

        //根据当前用户的权限，从系统菜单中清除无权限的菜单
        List<SysMenu> removeLists = new ArrayList<SysMenu>();
        for (SysMenu node : lists) {
            if (maps.get(node.getUuid()) == null && !node.getUuid().equalsIgnoreCase(TreeVeriable.ROOT)) {
                //如果当前菜单不在权限菜单中，则放入清除的菜单中
                removeLists.add(node);
            }
        }
        //从所有的菜单中删除无权限的菜单
        for (SysMenu node : removeLists) {
            lists.remove(node);
        }

        return lists;
    }

    /**
     * 
     * getUserPermissionToRole 获取指定用户能对指定角色授权的菜单.
     *
     * @author luoyibo
     * @param roleId
     *            要授权菜单的角色
     * @param userId
     *            当前授权人
     * @return List<SysMenuTree>
     * @throws @since
     *             JDK 1.8
     */

    @Override
    public List<SysMenuChkTree> getUserPermissionToRole(String roleId, String userId) {
        //当前角色已有的授权菜
        Map<String, SysPermission> maps = this.buildPermMap(roleId, AuthorType.ROLE);

        //当前用户有权限的菜单
        List<SysMenu> userPermissionMenu = this.getPermissionMenu(userId, AuthorType.USER);
        List<SysMenu> removeLists = new ArrayList<SysMenu>();
        for (SysMenu node : userPermissionMenu) {
            if (maps.get(node.getUuid()) != null && !node.getMenuType().equals(MenuType.TYPE_MENU)) {
                //如果当前菜单在角色的菜单权限中，则过滤掉
                removeLists.add(node);
            }
        }
        //从当前用户有权限的菜单中除掉已对角色授权的菜单
        for (SysMenu node : removeLists) {
            userPermissionMenu.remove(node);
        }

        List<SysMenuChkTree> result = new ArrayList<SysMenuChkTree>();
        //生成树形数据
        createChildChkTree(new SysMenuChkTree(TreeVeriable.ROOT, new ArrayList<SysMenuChkTree>()), result,
                userPermissionMenu);

        return result;
    }

    private void createChildChkTree(SysMenuChkTree parentNode, List<SysMenuChkTree> result, List<SysMenu> list) {
        List<SysMenu> childs = new ArrayList<SysMenu>();
        for (SysMenu SysMenu : list) {
            if (SysMenu.getParentNode().equals(parentNode.getId())) {
                childs.add(SysMenu);
            }
        }

        for (SysMenu sysMenu : childs) {
            SysMenuChkTree child = new SysMenuChkTree(sysMenu.getUuid(), sysMenu.getNodeText(), "", sysMenu.getLeaf(),
                    sysMenu.getNodeLevel(), sysMenu.getTreeIds(), new ArrayList<SysMenuChkTree>(), false,
                    sysMenu.getMenuCode(), sysMenu.getSmallIcon(), sysMenu.getBigIcon(), sysMenu.getMenuTarget(),
                    sysMenu.getMenuType(), sysMenu.getMenuLeaf(), sysMenu.getNodeCode(), sysMenu.getParentNode(),
                    sysMenu.getOrderIndex(), sysMenu.getIssystem());

            if (sysMenu.getParentNode().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<SysMenuChkTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createChildChkTree(child, result, list);
        }
    }

    @Override
    public SysMenu addMenu(SysMenu menu, SysUser currentUser) throws IllegalAccessException, InvocationTargetException {
        String parentNode = menu.getParentNode();
        String parentName = menu.getParentName();
        String menuType = menu.getMenuType();
        String menuLeaf = "LEAF";
        if (menuType.equals(MenuType.TYPE_MENU))
            menuLeaf = "GENERAL";
        //String parentName = menu.getp
        SysMenu saveEntity = new SysMenu();
        BeanUtils.copyPropertiesExceptNull(menu, saveEntity);
        menu.setCreateUser(currentUser.getXm()); // 创建人
        menu.setLeaf(true);
        menu.setIssystem(1);
        menu.setIsHidden("1");
        menu.setMenuLeaf(menuLeaf);
        if (!parentNode.equals(TreeVeriable.ROOT)) {
            SysMenu parEntity = this.get(parentNode);
            parEntity.setLeaf(false);
            this.merge(parEntity);
            menu.BuildNode(parEntity);
        } else
            menu.BuildNode(null);

        menu = this.merge(menu);
        menu.setParentName(parentName);
        menu.setParentNode(parentNode);

        //给超级管理员授权
        // TODO Auto-generated method stub
        return menu;
    }
}