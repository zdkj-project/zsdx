package com.zd.school.plartform.baseset.service;

import java.util.List;

import com.zd.core.service.BaseService;
import com.zd.school.plartform.baseset.model.BaseDic ;
import com.zd.school.plartform.baseset.model.BaseDicTree;


/**
 * 
 * ClassName: BaseDicService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 数据字典实体Service接口类.
 * date: 2016-07-19
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface BaseDicService extends BaseService<BaseDic> {

    public List<BaseDicTree>getDicTreeList(String whereSql);
}