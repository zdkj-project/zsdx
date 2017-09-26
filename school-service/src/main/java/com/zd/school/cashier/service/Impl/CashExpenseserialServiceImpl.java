package com.zd.school.cashier.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.DateUtil;
import com.zd.school.cashier.dao.CashExpenseserialDao;
import com.zd.school.cashier.model.CashExpensedetail;
import com.zd.school.cashier.model.CashExpenseserial;
import com.zd.school.cashier.service.CashExpensedetailService;
import com.zd.school.cashier.service.CashExpenseserialService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zd.core.util.BeanUtils.copyProperties;

/**
 * ClassName: CashExpenseserialServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 消费流水(CASH_T_EXPENSESERIAL)实体Service接口实现类.
 * date: 2017-09-25
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class CashExpenseserialServiceImpl extends BaseServiceImpl<CashExpenseserial> implements CashExpenseserialService {

    @Resource
    public void setCashExpenseserialDao(CashExpenseserialDao dao) {
        this.dao = dao;
    }

    private static Logger logger = Logger.getLogger(CashExpenseserialServiceImpl.class);

    @Resource
    private CashExpensedetailService billDetailService;
    @Override
    public QueryResult<CashExpenseserial> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<CashExpenseserial> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
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
    public CashExpenseserial doUpdateEntity(CashExpenseserial entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        CashExpenseserial saveEntity = this.get(entity.getUuid());
        try {
            copyProperties(saveEntity, entity);
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
    public CashExpenseserial doAddEntity(CashExpenseserial entity, SysUser currentUser) {
        CashExpenseserial saveEntity = new CashExpenseserial();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            copyProperties(saveEntity, entity, excludedProp);
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
    public boolean doSaveConsumeBill(CashExpenseserial bill, List<CashExpensedetail> billDetails) throws InvocationTargetException, IllegalAccessException {
        CashExpensedetail saveBillItem = null;
        String oper = bill.getOpertioner();
        List<String> excludedProp = new ArrayList<>();
        excludedProp.add("uuid");

        CashExpenseserial saveEntity = new CashExpenseserial();
        copyProperties(saveEntity, bill, excludedProp);
        saveEntity.setConsumeDate(DateUtil.getDate(bill.getTestDate(),"yyyy-MM-dd HH:mm:ss"));
        saveEntity.setCreateUser(oper);
        saveEntity.setUpdateUser(oper);
        bill = this.merge(saveEntity);// 执行修改方法

        String expenseserialId = bill.getUuid();
        for (CashExpensedetail  bi : billDetails) {
            saveBillItem = new CashExpensedetail();
            copyProperties(saveBillItem, bi, excludedProp);
            saveBillItem.setExpenseserialId(expenseserialId);
            saveBillItem.setCreateUser(oper);
            saveBillItem.setUpdateUser(oper);

            billDetailService.merge(saveBillItem);
        }

        return true;
    }
}