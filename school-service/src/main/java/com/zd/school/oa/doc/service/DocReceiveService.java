package com.zd.school.oa.doc.service;

import java.lang.reflect.InvocationTargetException;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.doc.model.DocReceive ;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.plartform.system.model.SysUser;


/**
 * 
 * ClassName: DocReceiveService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文收文单实体Service接口类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface DocReceiveService extends BaseService<DocReceive> {

	
	/**
     * 
     * doExamOrRecric:处理批阅或传阅 人.
     * 
     * @author luoyibo
     * @param docrecId
     *            收文ID
     * @param userIds
     *            要处理的人员ID,多个ID用英文逗号隔开
     * @param distribType
     *            人员类型 0-批阅人 1-传阅人
     * @param operName
     *            操作人
     * @return Integer 处理的记录数
     * @throws @since
     *             JDK 1.8
     */
    public Integer doExamOrRecric(String docrecId, String userIds, String distribType, String operName);

    /**
     * 
     * createRecNumb:生成收文编号.
     *
     * @author luoyibo
     * @param doctypeId
     *            公文类型
     * @return String 生成的流水号
     * @throws @since
     *             JDK 1.8
     */
    public String createRecNumb(String doctypeId);
    
    /**
     * 
     * @param entity  收文实体类
     * @param currentUser 当前操作用户
     * @return DocReceive 增加后的实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DocReceive doAdd(DocReceive entity,SysUser currentUser) throws IllegalAccessException, InvocationTargetException;
    
    /**
     * 
     * @param entity  收文实体类
     * @param currentUser 当前操作用户
     * @return DocReceive 修改后的实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DocReceive doUpdate(DocReceive entity,SysUser currentUser) throws IllegalAccessException, InvocationTargetException;
    
    /**
     * list:所有收文列表
     * @param start 起始页数
     * @param limit 每页记录数
     * @param sort  排序字段
     * @param filter 过滤字段
     * @param whereSql 自定义查询条件
     * @param orderSql 自定义排序条件
     * @param currentUser 当前用户
     * @return
     */
    public QueryResult<DocReceive> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser);    
    
    /**
     * fenfalist:待分发的收文列表
     * @param start 起始页数
     * @param limit 每页记录数
     * @param sort  排序字段
     * @param filter 过滤字段
     * @param whereSql 自定义查询条件
     * @param orderSql 自定义排序条件
     * @param currentUser 当前用户
     * @return
     */
    public QueryResult<DocReceive> fenfalist(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser);      
}