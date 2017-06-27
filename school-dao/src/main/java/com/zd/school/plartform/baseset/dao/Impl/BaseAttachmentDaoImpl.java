package com.zd.school.plartform.baseset.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.plartform.baseset.dao.BaseAttachmentDao;
import com.zd.school.plartform.baseset.model.BaseAttachment;

/**
 * 
 * ClassName: BaseTAttachmentDaoImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公共附件信息表实体Dao接口实现类. date: 2016-07-06
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class BaseAttachmentDaoImpl extends BaseDaoImpl<BaseAttachment> implements BaseAttachmentDao {
    public BaseAttachmentDaoImpl() {
        super(BaseAttachment.class);
        // TODO Auto-generated constructor stub
    }
}