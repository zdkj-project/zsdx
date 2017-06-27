package com.zd.school.salary.salary.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.comm.model.CommBase;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.dao.XcSalarybookDao;
import com.zd.school.salary.salary.model.XcSalarybook;
import com.zd.school.salary.salary.service.XcSalarybookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: XcSalarybookServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资台账表(XC_T_SALARYBOOK)实体Service接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcSalarybookServiceImpl extends BaseServiceImpl<XcSalarybook> implements XcSalarybookService{

    @Resource
    public void setXcSalarybookDao(XcSalarybookDao dao) {
        this.dao = dao;
    }

	@Override
	public QueryResult<XcSalarybook> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql,SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from XcSalarybook o where 1=1 ");
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
        
        QueryResult<XcSalarybook> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

	/**
     * 获取用户有数据的台帐
     * @param user 当前用户
     * @return 树集合
     */
	@Override
	public List<CommTree> getUserBookTree(SysUser user) {
		String sql = "EXECUTE XC_P_GETUSERBOOKTREE '"+user.getUuid()+"'";
		
		//List<CommBase> lists = this.doQuerySqlObject(sql, CommBase.class);
		List<CommBase> lists = new ArrayList<CommBase>();
		List<Object[]> list=this.ObjectQuerySql(sql);
		for (Object[] obj : list) {
			CommBase temp=new CommBase();
			temp.setId(obj[0]+"");
			temp.setText(obj[1]+"");
			temp.setIconCls(obj[2]+"");
			temp.setLeaf(obj[3]+"");
			temp.setLevel(new Integer(obj[4]+""));
			temp.setParent(obj[5]+"");
			temp.setTreeIds(obj[6]+"");
			lists.add(temp);
		}

        

        List<CommTree> result = new ArrayList<CommTree>();

        // 构建Tree数据
        createTreeChild(new CommTree(TreeVeriable.ROOT, new ArrayList<CommTree>()), result, lists);

        return result;
	}
	
	 public void createTreeChild(CommTree parentNode, List<CommTree> result, List<CommBase> list) {
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
}