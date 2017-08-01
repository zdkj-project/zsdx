package com.zd.school.oa.doc.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.constant.StringVeriable;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.oa.doc.dao.DocReceiveDao ;
import com.zd.school.oa.doc.model.DocReceive ;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.service.DocReceiveService ;
import com.zd.school.oa.doc.service.DocRecexaminesService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 
 * ClassName: DocReceiveServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 公文收文单实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class DocReceiveServiceImpl extends BaseServiceImpl<DocReceive> implements DocReceiveService{

    @Resource
    public void setDocReceiveDao(DocReceiveDao dao) {
        this.dao = dao;
    }

    @Resource
    private SysUserService userService;
    
    @Resource
    private PushInfoService pushService;
    
    @Resource DocRecexaminesService docExamService;
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
    @Override
    public Integer doExamOrRecric(String docrecId, String userIds, String distribType, String operName) {
        //处理公文批阅人
    	StringBuffer sql = new StringBuffer("EXECUTE DOC_P_UPDATEDOCRECEXAMINES ");
        sql.append("'" + docrecId + "', ");
        sql.append("'" + userIds + "', ");
        sql.append("'" + distribType + "', ");
        sql.append("'" + operName + "'");

        Integer executeCount = super.executeSql(sql.toString());
        String eventType="公文批阅提醒";
        if (distribType.equals("1")){
        	eventType="公文传阅提醒";
        }
        //写入微信通知
        String ids[] = userIds.split(",");
        for (String s : ids) {
        	SysUser user = userService.get(s);
        	String [] propName = new String[]{"docrecId","userId","recexamType"};
        	Object[] propValue = new Object[]{docrecId,s,distribType};
        	DocRecexamines exam = docExamService.getByProerties(propName, propValue);
        	
    		StringBuffer url = new StringBuffer(StringVeriable.WEB_URL);
    		url.append("app/DocSendcheck/detailed?");
    		url.append("id=" + exam.getUuid());
    		url.append("&personUserId=" + s);        	
        	String regStatus="您好," + user.getXm() + "老师,有公文需要您尽快处理!";
        	pushService.pushInfo(user.getXm(), user.getUserNumb(), eventType, regStatus,url.toString());
		}
        
        return executeCount;
    }
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
    @Override
    public String createRecNumb(String doctypeId) {
        String recNumb = "";
        String sql = "EXECUTE DOC_P_GETDOCNUMBER '" + doctypeId + "'";
        List lists = this.dao.doQuerySql(sql);
        recNumb = lists.get(0).toString();

        return recNumb;
    }

	@Override
    /**
     * 
     * doAdd:增加新的收文.
     *
     * @author luoyibo
     * @param entity
     *            收文实体类
     * @param currentUser
     *            当前操作用户
     * @return DocReceive 增加后的实体
     * @throws @since
     *             JDK 1.8
     */
	public DocReceive doAdd(DocReceive entity, SysUser currentUser) throws IllegalAccessException, InvocationTargetException {
		
		DocReceive saveEntity = new DocReceive();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);
        
        String recNumb = this.createRecNumb(entity.getDoctypeId());    //生成 收文编号
        entity.setOrderIndex(1);        // 增加时要设置创建人
        entity.setCreateUser(currentUser.getXm()); // 创建人
        entity.setRecNumb(recNumb);
        entity.setReceiveSource("0");    //外部收文
        entity.setRegUserId(currentUser.getUuid());
        //0-待登记  1-已登记 2-待批阅 3-已批阅 4-待传阅 5-已传阅
        switch (entity.getDistribType()) {
		case "0":
			//领导批阅 设置状态为已登记
			entity.setDocrecState(1);
			break;
		case "1":
			//分发传阅
			entity.setDocrecState(4);
			break;	
		case "2":
			//完成批阅(收文后不再向下走)
			entity.setDocrecState(5);
			break;			
		default:
			entity.setDocrecState(1);
			break;
		}
        // 持久化到数据库
        this.persist(entity);

        //当处理类型不为完成 批阅时处理传阅人员或批阅人员
        String docrecId = entity.getUuid();
        if (!entity.getDistribType().equals("2"))
        	doExamOrRecric(docrecId, entity.getDistribId(), entity.getDistribType(), currentUser.getXm());	
        
		return entity;
	}
	@Override
	public DocReceive doUpdate(DocReceive entity, SysUser currentUser)
			throws IllegalAccessException, InvocationTargetException {
       
		// 先拿到已持久化的实体
        DocReceive perEntity = this.get(entity.getUuid());
        String newDistribId = entity.getDistribId();
        String oldDistribId = perEntity.getDistribId();
        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
        if (entity.getDistribType().equals("0")) {
        	perEntity.setDocrecState(1);
        } else {
        	perEntity.setDocrecState(4);
        }
        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
        entity = this.merge(perEntity);// 执行修改方法

      //当处理类型不为完成 批阅时处理传阅人员或批阅人员
        if (!newDistribId.equals(oldDistribId))
            this.doExamOrRecric(entity.getUuid(), entity.getDistribId(), entity.getDistribType(), currentUser.getXm());
        
        return entity;
	}
	@Override
	public QueryResult<DocReceive> list(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);
		
		Boolean isSchoolAdminRole = false;
		List<SysUser> roleUsers = userService.getUserByRoleName("学校管理员");
		for (SysUser su : roleUsers) {
			if(su.getUuid().equals(currentUser.getUuid())){
				isSchoolAdminRole = true;
				break;
			}
		}
		StringBuffer hql = new StringBuffer("from DocReceive o where 1=1 ");
		if (!isSchoolAdminRole){
			//如果当前人不是schooladmin角色,则只能显示自己的登记
			hql.append(" and regUserId='" + currentUser.getUuid() + "' ");
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
        
        QueryResult<DocReceive> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}
	@Override
	public QueryResult<DocReceive> fenfalist(Integer start, Integer limit, String sort, String filter, String whereSql,
			String orderSql, SysUser currentUser) {
		String sortSql = StringUtils.convertSortToSql(sort);
		String filterSql = StringUtils.convertFilterToSql(filter);

		StringBuffer hql = new StringBuffer("from DocReceive o where distribType='0' and docrecState='3' ");
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
        
        QueryResult<DocReceive> qResult = this.doQueryResult(hql.toString(), start, limit);
		return qResult;
	}

}