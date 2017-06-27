package com.zd.school.oa.terminal.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.terminal.dao.OaInfotermuseDao ;
import com.zd.school.oa.terminal.model.OaInfotermuse ;


/**
 * 
 * ClassName: OaInfotermuseDaoImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 终端使用历史(OA_T_INFOTERMUSE)实体Dao接口实现类.
 * date: 2017-01-14
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class OaInfotermuseDaoImpl extends BaseDaoImpl<OaInfotermuse> implements OaInfotermuseDao {
    public OaInfotermuseDaoImpl() {
        super(OaInfotermuse.class);
        // TODO Auto-generated constructor stub
    }
}