package com.zd.school.oa.terminal.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.terminal.dao.OaInfotermDao ;
import com.zd.school.oa.terminal.model.OaInfoterm ;


/**
 * 
 * ClassName: OaInfotermDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 信息发布终端(OA_T_INFOTERM)实体Dao接口实现类.
 * date: 2017-01-14
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaInfotermDaoImpl extends BaseDaoImpl<OaInfoterm> implements OaInfotermDao {
    public OaInfotermDaoImpl() {
        super(OaInfoterm.class);
        // TODO Auto-generated constructor stub
    }
}