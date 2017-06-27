package com.zd.school.plartform.comm.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.core.model.BaseEntity;
import com.zd.school.plartform.comm.dao.CommTreeDao;

/**
 * 
 * ClassName: BaseDicDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 数据字典实体Dao接口实现类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class CommTreeDaoImpl extends BaseDaoImpl<BaseEntity> implements CommTreeDao {
    public CommTreeDaoImpl() {
        super(BaseEntity.class);
        // TODO Auto-generated constructor stub
    }
}