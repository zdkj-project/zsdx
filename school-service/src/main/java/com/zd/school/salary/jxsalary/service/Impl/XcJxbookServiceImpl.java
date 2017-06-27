package com.zd.school.salary.jxsalary.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.comm.model.CommBase;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.jxsalary.dao.Impl.XcJxbookitemDaoImpl;
import com.zd.school.salary.jxsalary.dao.XcJxbookDao;
import com.zd.school.salary.jxsalary.model.XcJxbook;
import com.zd.school.salary.jxsalary.service.XcJxbookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: XcJxbookServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOK)实体Service接口实现类.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcJxbookServiceImpl extends BaseServiceImpl<XcJxbook> implements XcJxbookService{

    @Resource
    public void setXcJxbookDao(XcJxbookDao dao) {
        this.dao = dao;
    }
    
    @Resource
    private XcJxbookitemDaoImpl bookItemService;
	@Override
	public QueryResult<XcJxbook> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql,SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from XcJxbook o where 1=1 ");
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
        
        QueryResult<XcJxbook> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

	@Override
	public List<CommTree> getBookTree(String viewName, String whereSql, SysUser currentUser) {
        String sql = "";
        List<CommBase> romoeList = new ArrayList<CommBase>();
        sql = "select id,text,iconCls,leaf,level,parent from " + viewName +" where 1=1 " + whereSql;
        List<CommBase> lists = this.doQuerySqlObject(sql, CommBase.class);
        List<CommTree> result = new ArrayList<CommTree>();
        // 构建Tree数据
        createTreeChild(new CommTree(TreeVeriable.ROOT, new ArrayList<CommTree>()), result, lists);
        return result;
	}
    private void createTreeChild(CommTree parentNode, List<CommTree> result, List<CommBase> list) {
        List<CommBase> childs = new ArrayList<CommBase>();
        for (CommBase dic : list) {
            if (dic.getParent().equals(parentNode.getId())) {
                childs.add(dic);
            }
        }

        for (CommBase fc : childs) {
            CommTree child = new CommTree(fc.getId(), fc.getText(), fc.getIconCls(), Boolean.parseBoolean(fc.getLeaf()),
                    fc.getLevel(), "", fc.getParent(),0, new ArrayList<CommTree>());

            if (fc.getParent().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<CommTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createTreeChild(child, result, list);
        }
    }

	@Override
	public Boolean doDelete(String ids, SysUser currentUser) {
		Boolean doResult = false;
		bookItemService.deleteByProperties("jxbookId", ids);
		this.deleteByPK(ids);
		
		doResult = true;
		
		return doResult;
	}	
}