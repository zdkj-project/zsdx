package com.zd.school.oa.meeting.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.meeting.dao.OaMeetingempDao;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: OaMeetingempServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 会议人员信息(OA_T_MEETINGEMP)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaMeetingempServiceImpl extends BaseServiceImpl<OaMeetingemp> implements OaMeetingempService {

    @Resource
    public void setOaMeetingempDao(OaMeetingempDao dao) {
        this.dao = dao;
    }

    private static Logger logger = Logger.getLogger(OaMeetingempServiceImpl.class);

    @Resource
    private SysUserService userService;

    @Override
    public QueryResult<OaMeetingemp> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<OaMeetingemp> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }

    /**
     * 根据主键逻辑删除数据
     *
     * @param ids         要删除数据的主键
     * @param currentUser 当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    @Override
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
        Boolean delResult = false;
        try {
            Object[] conditionValue = ids.split(",");
            String[] propertyName = {"isDelete", "updateUser", "updateTime"};
            Object[] propertyValue = {1, currentUser.getXm(), new Date()};
            this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
            delResult = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            delResult = false;
        }
        return delResult;
    }

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public OaMeetingemp doUpdateEntity(OaMeetingemp entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        OaMeetingemp saveEntity = this.get(entity.getUuid());
        try {
            BeanUtils.copyPropertiesExceptNull(saveEntity, entity);
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public OaMeetingemp doAddEntity(OaMeetingemp entity, SysUser currentUser) {
        OaMeetingemp saveEntity = new OaMeetingemp();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyProperties(saveEntity, entity, excludedProp);
            saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public QueryResult<SysUser> getNotMeetingUserList(Integer start, Integer limit, String sort, String filter, String whereSql, String meetingId) {
        String orderSql = "";
        if (StringUtils.isNotEmpty(sort)) {
            orderSql = " order by " + StringUtils.convertSortToSql(sort);
        }
        if (StringUtils.isNotEmpty(filter)) {
            whereSql += StringUtils.convertFilterToSql(filter);
        }
        //获取该会议已有的人员Id
        String sql = MessageFormat.format("SELECT dbo.OA_F_GETMEETINGEMPID(''{0}'') AS meetingUserId ", meetingId);
        List meetingEmpIdList = this.doQuerySql(sql);
        if(meetingEmpIdList.get(0)!=null){
        	String meetingempIds = meetingEmpIdList.get(0).toString();
        
        	//可选择人员要排除已有的人员
        	whereSql += " and o.uuid not in ('" + meetingempIds.replace(",", "','") + "') ";
        }
        
        String hql = " from SysUser o where o.isDelete=0 and o.state='0' " + whereSql + orderSql;
        QueryResult<SysUser> queryResult = userService.doQueryResult(hql, start, limit);
        if (queryResult.getResultList().size() > 0)
            return queryResult;
        else
            return null;
    }
}