package com.zd.school.oa.doc.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.constant.StringVeriable;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.oa.doc.dao.DocSenddocDao ;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.doc.model.DocSenddoc ;
import com.zd.school.oa.doc.service.DocSendcheckService;
import com.zd.school.oa.doc.service.DocSenddocService ;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: DocSenddocServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文发文单实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocSenddocServiceImpl extends BaseServiceImpl<DocSenddoc> implements DocSenddocService{

    @Resource
    public void setDocSenddocDao(DocSenddocDao dao) {
        this.dao = dao;
    }
    
    @Resource
    private SysUserService userService;
    
    @Resource
    private PushInfoService pushService;
    
    @Resource
    private DocSendcheckService sendCheckService;
    
    @Override
    public Integer doSendCheckEmp(String sendId, String userIds, String operName,String sendState) {

        StringBuffer sql = new StringBuffer("EXECUTE DOC_P_UPDATEDOCSENDCHECKS ");
        sql.append("'" + sendId + "', ");
        sql.append("'" + userIds + "', ");
        sql.append("'" + operName + "', ");
        sql.append("'" + sendState + "'");

        Integer executeCount = super.executeSql(sql.toString());
        
        String eventType = "公文核稿提醒";
        String ids[] = userIds.split(",");
        for (String s : ids) {
        	SysUser user = userService.get(s);
        	String [] propName = new String[]{"sendId","userId"};
        	Object[] propValue = new Object[]{sendId,s};
        	DocSendcheck exam = sendCheckService.getByProerties(propName, propValue);
        	
    		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
    		url.append("app/DocSendcheck/detailed?");
    		url.append("id=" + exam.getUuid());
    		url.append("&personUserId=" + s);        	
        	String regStatus="您好," + user.getXm() + "老师,有公文需要您尽快处理!";
        	pushService.pushInfo(user.getXm(), user.getUserNumb(), eventType, regStatus,url.toString());
		}

        return executeCount;
    }

    @Override
    public String createSendNumb(String doctypeId) {

        String recNumb = "";
        String sql = "EXECUTE DOC_P_GETDOCNUMBER '" + doctypeId + "'";
        List lists = this.dao.doQuerySql(sql);
        recNumb = lists.get(0).toString();

        return recNumb;
    }

    @Override
    public Integer sendDocToDept(String sendIds, String deptIds, String operName) {

        StringBuffer sql = new StringBuffer("EXECUTE DOC_P_SETSENDTODEPTS ");
        sql.append("'" + sendIds + "', ");
        sql.append("'" + deptIds + "', ");
        sql.append("'" + operName + "'");

        Integer executeCount = super.executeSql(sql.toString());

        return executeCount;
    }

	@Override
	public QueryResult<DocSenddoc> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);
		Boolean isSchoolAdminRole = false;
		StringBuffer hql = new StringBuffer("from DocSenddoc o where 1=1 ");
		List<SysUser> roleUsers = userService.getUserByRoleName("学校管理员");
		for (SysUser su : roleUsers) {
			if(su.getUuid().equals(currentUser.getUuid())){
				isSchoolAdminRole = true;
				break;
			}
		}
		if (!isSchoolAdminRole){
			//如果当前人不是schooladmin角色,则只能显示自己的拟稿
			hql.append(" and drafterId='" + currentUser.getUuid() + "' ");
		}
		hql.append(whereSql);
		hql.append(filterSql);
        if (orderSql.length()>0){
        	if (sortSql.length()>0)
        		hql.append(orderSql+ " , " + sortSql);
        	else 
        		hql.append(orderSql);
        } else {
        	if (sortSql.length()>0)
        		hql.append(" order by  " + sortSql);
        }
        
        QueryResult<DocSenddoc> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

}