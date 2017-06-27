package com.zd.school.oa.doc.service;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseService;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.model.DocSenddoc ;
import com.zd.school.plartform.system.model.SysUser;


/**
 * 
 * ClassName: DocSenddocService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文发文单实体Service接口类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface DocSenddocService extends BaseService<DocSenddoc> {

	/**
     * 
     * doSendCheckEmp:处理发文核稿人.
     * 
     * @author luoyibo
     * @param sendId
     *            发文ID
     * @param userIds
     *            要处理的人员ID,多个ID用英文逗号隔开
     * @param operName
     *            操作人
     * @return Integer 处理的记录数
     * @throws @since
     *             JDK 1.8
     */
    public Integer doSendCheckEmp(String sendId, String userIds, String operName,String sendState);

    /**
     * 
     * createRecNumb:生成发文编号.
     *
     * @author luoyibo
     * @param doctypeId
     *            公文类型
     * @return String 生成的流水号
     * @throws @since
     *             JDK 1.8
     */
    public String createSendNumb(String doctypeId);

    /**
     * 
     * sendDocToDept:将公文发送至指定的部门.
     *
     * @author luoyibo
     * @param sendIds
     *            要发送的公文的ID，多份公文时用英文逗号隔开
     * @param deptIds
     *            要发送至的部门的ID，多个部门时用英文逗号隔开
     * @return Integer
     * @throws @since
     *             JDK 1.8
     */
    public Integer sendDocToDept(String sendIds, String deptIds, String operName);
    
    public QueryResult<DocSenddoc> list(Integer start, Integer limit, String sort, String filter, String whereSql,String orderSql,
            SysUser currentUser);    
}