package com.zd.school.plartform.baseset.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.constant.StatuVeriable;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.model.BuildRoomarea;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.plartform.baseset.dao.BaseCampusDao;
import com.zd.school.plartform.baseset.model.BaseCampus;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.service.BaseCampusService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BaseCampusServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 校区信息实体Service接口实现类. date: 2016-08-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BaseCampusServiceImpl extends BaseServiceImpl<BaseCampus> implements BaseCampusService {

    @Resource
    public void setBaseCampusDao(BaseCampusDao dao) {
        this.dao = dao;
    }

    @Resource
    private BuildRoomareaService areaService;

    @Resource
    private BaseOrgService orgService;

    @Override
    public BaseCampus doAdd(BaseCampus entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException {

        //增加区域表的数据
        BaseCampus saveEntity = new BaseCampus();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);
        //entity.setOrderIndex(0);//排序
        entity.setCreateUser(currentUser.getXm());
        entity = this.doMerge(entity);

        //增加到建筑物的区域
        BuildRoomarea roomarea = new BuildRoomarea(entity.getUuid());
        roomarea.setNodeText(entity.getCampusName());
        roomarea.setCreateTime(new Date());
        roomarea.setCreateUser(currentUser.getXm());
        roomarea.setAreaType("02");
        roomarea.setParentNode(currentUser.getSchoolId());
        roomarea.setLeaf(true);
        roomarea.setNodeLevel(2);
        roomarea.setTreeIds(currentUser.getSchoolId() + "," + entity.getUuid());
        areaService.doMerge(roomarea);

        //增加到部门的第二级
        BaseOrg orgSave = new BaseOrg(entity.getUuid());
        orgSave.setNodeText(entity.getCampusName()); //部门名称
        orgSave.setOrderIndex(entity.getOrderIndex());
        orgSave.setParentNode(entity.getSchoolId()); //上级节点
        orgSave.setCreateUser(currentUser.getXm()); // 创建人
        orgSave.setDeptType("02"); //默认类型为校区
        orgSave.setLeaf(true);
        orgSave.setIssystem(1);

        BaseOrg parEntity = orgService.get(entity.getSchoolId());
        parEntity.setLeaf(false);
        orgService.doMerge(parEntity);
        orgSave.BuildNode(parEntity);
        orgService.doMerge(orgSave);

        return entity;
    }

    @Override
    public BaseCampus doUpdate(BaseCampus entity, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException {

        //先拿到已持久化的实体
        BaseCampus perEntity = this.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(currentUser.getXm()); //设置修改人的中文名
        entity = this.doMerge(perEntity);//执行修改方法

        //更新建筑物区域中对应的名称
        BuildRoomarea roomarea = areaService.get(entity.getUuid());
        roomarea.setNodeText(entity.getCampusName());
        roomarea.setUpdateTime(new Date());
        roomarea.setUpdateUser(currentUser.getXm());
        areaService.doMerge(roomarea);

        //更新部门名称
        BaseOrg orgSave = orgService.get(entity.getUuid());
        orgSave.setNodeText(entity.getCampusName());
        orgSave.setOrderIndex(entity.getOrderIndex());
        orgSave.setUpdateTime(new Date());
        orgSave.setUpdateUser(currentUser.getXm());
        orgService.doMerge(orgSave);
        // TODO Auto-generated method stub
        return entity;
    }

    @Override
    public boolean doDelete(String delIds, SysUser currentUser)
            throws IllegalAccessException, InvocationTargetException {
        boolean rs = true;
        String[] ids = delIds.split(",");
        Integer childOrg = 0;
        Integer childArea = 0;
        StringBuffer canSb = new StringBuffer();
        for (String uuid : ids) {
            //检查当前校区是否配置了下属的部门
            childOrg = orgService.getChildCount(uuid);

            //检查当前校区是否配置了下属的建筑物区域
            childArea = areaService.getChildCount(uuid);

            //如果都没有配置下级，则可以删除
            if (childArea.equals(0) && childOrg.equals(0)) {
                canSb.append(uuid + ",");
            }
        }
        if (canSb.length() > 0) {
            String s = StringUtils.trimLast(canSb.toString());
            rs = orgService.doLogicDelOrRestore(s, StatuVeriable.ISDELETE);
            rs = areaService.doLogicDelOrRestore(s, StatuVeriable.ISDELETE);
            rs = this.doLogicDelOrRestore(s, StatuVeriable.ISDELETE);
        } else {
            rs = false;
        }
        // TODO Auto-generated method stub
        return rs;
    }

}