package com.orcl.sync.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orcl.sync.model.hibernate.hibernate.HrDepartment;
import com.orcl.sync.model.hibernate.hibernate.HrDeptPosition;
import com.orcl.sync.model.hibernate.hibernate.HrPosition;
import com.orcl.sync.model.hibernate.hibernate.HrUser;
import com.orcl.sync.model.hibernate.hibernate.HrUserDepartmentPosition;
import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.CustomerContextHolder;
import com.zd.core.util.NodeUtli;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.plartform.baseset.model.BaseDeptjob;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.model.BaseOrg;
import com.zd.school.plartform.baseset.model.BaseUserdeptjob;
import com.zd.school.plartform.baseset.service.BaseDeptjobService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.baseset.service.BaseUserdeptjobService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

@Controller
@RequestMapping("/usersync")
public class UserSyncController extends FrameWorkController<DocSendcheck> implements Constant{

    private static final HttpServletRequest HttpServletRequest = null;
	private static final HttpServletResponse HttpServletResponse = null;
    private static final HttpServletRequest HttpServletRequest1 = null;
	private static final HttpServletResponse HttpServletResponse1 = null;
    private static final HttpServletRequest HttpServletRequestDeptJob = null;
	private static final HttpServletResponse HttpServletResponseDeptJob = null;
    private static final HttpServletRequest HttpServletRequestUserDeptJob = null;
	private static final HttpServletResponse HttpServletResponseUserDeptJob = null;

	@Resource  
    //orcl的session工厂
    private SessionFactory sssssss;
    @Resource
    private BaseOrgService orgService;
    @Resource
    SysRoleService roleService;


    @Resource
    private SysUserService userservice;


    @Resource
    private BaseJobService jobservice;
    @Resource
    private BaseDeptjobService deptjobService;
    @Resource
    private BaseUserdeptjobService userdeptjobService;

    
	@RequestMapping(value = "list")
	public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
		dept(HttpServletRequest,HttpServletResponse);
		job(HttpServletRequest1, HttpServletResponse1);
		deptjob(HttpServletRequestDeptJob,HttpServletResponseDeptJob);
		
		
		
		//启用orcl数据库
		CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
		
/*//		
*/		
		//3.打开session  
	    Session session = sssssss.openSession();  
	    //4.开启事务  
	    session.beginTransaction();
	
	    Query query = session.createQuery("from HrUser");  

	    List<HrUser> list = query.list();	    
	    
	    //6.提交事务  
	    session.getTransaction().commit();  
	    //7.关闭session  
	    session.flush();  
	    //8.关闭session工厂  
	  //  sssssss.close();
	    
	    
	    SysUser u=null;
	    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
	    	userservice.executeSql("delete SYS_T_USERJOB where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
	 	    userservice.executeSql("delete SYS_T_USERDEPT where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
	 	    userservice.executeSql("delete SYS_T_ROLEUSER where user_id!='8a8a8834533a065601533a065ae80000' and user_id!='f111ebab-933b-4e48-b328-c731ae792ca0'");
		    for(HrUser d : list){
	/*	    	Integer count=userservice.getCountSql("select count(*) from SYS_T_USER where user_id='"+d.getId()+"'");
		    	if(count==0){*/
		    		u=userservice.get(d.getId());
		    		if (u==null) {
						u=new SysUser(d.getId());
					}
		    		u.setXm(d.getUserName());
		    		u.setUserName(d.getAccounts());
		    		u.setUserPwd(d.getPasswords());
		    		u.setIsDelete(d.getIsEnable()==0?1:0);
		    		u.setCategory("1");
		    		u.setState("0");
		    		u.setIssystem(1);
		    		u.setIsHidden("0");
		    		if(d.getUserSex()!=null){
		    			if(d.getUserSex().equals("男")){
		    				u.setXbm("1");
		    			}else{
		    				u.setXbm("2");
		    			}
		    		}
		    		u.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
		    		u.setUserNumb(d.getJobNumber());
		    		u.setCreateTime(d.getCreateDate());
		    		u.setCreateUser(d.getCreateName());
		    		
		    		String[] param={"roleName","isDelete"};
	                Object[] values={"教师",0};
	                Set<SysRole> theUserRole = u.getSysRoles();
	                SysRole defaultRole = roleService.getByProerties(param, values);
	                theUserRole.add(defaultRole);
	                u.setSysRoles(theUserRole);
	
	                
	                
	        	    Query queryorg=session.createQuery("from HrUserDepartmentPosition where sysUserId='"+d.getId()+"'");
	        	    List<HrUserDepartmentPosition> listorg=queryorg.list();
//	                Set<BaseOrg> teaOrgs = u.getUserDepts();
//	                Set<BaseJob> teaJobs = u.getUserJobs();
//	                BaseOrg defaultOrg=null;
//	                BaseJob defaultJob=null;
//	                for (HrUserDepartmentPosition baseOrg : listorg) {
//	                	if(null!=baseOrg.getDepartmentId()&&!baseOrg.getDepartmentId().equals("")){
//	                		defaultOrg=orgService.get(baseOrg.getDepartmentId());
//	                		//teaOrgs.add(defaultOrg);
//	                		teaOrgs.add(defaultOrg);
//	                	}
//	                	if(null!=baseOrg.getDeptPositionId()&&!baseOrg.getDeptPositionId().equals("")){
//		                	defaultJob=jobservice.get(baseOrg.getDeptPositionId());
//		                	//teaJobs.add(defaultJob);
//		                	teaJobs.add(defaultJob);
//	                	}
//
//
//					}
//	                u.setUserDepts(teaOrgs);
//	                u.setUserJobs(teaJobs);
	/*                //默认部门为临时部门
	                Set<BaseOrg> teaOrgs = u.getUserDepts();
	                BaseOrg defaultOrg = orgService.getByProerties("nodeText", "临时部门");
	                BaseOrg defaultOrg1 = orgService.getByProerties("nodeText", "教务科");
	                
	                teaOrgs.add(defaultOrg);
	                teaOrgs.add(defaultOrg1);
	                
	                u.setUserDepts(teaOrgs);*/
		    		userservice.merge(u);
		    	}
		        
		    //}  
	
		    userdeptjob(HttpServletRequestUserDeptJob, HttpServletResponseUserDeptJob);
		    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
	    
	    	//writeJSON(response, jsonBuilder.returnFailureJson("'请先同步部门和岗位数据'"));
	    
	}
	
	   
		@RequestMapping(value = "dept")
		public void dept(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//启用orcl数据库
			CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
			
			//3.打开session  
		    Session session = sssssss.openSession();  
		    //4.开启事务  
		    session.beginTransaction();
		
		    Query query = session.createQuery("from HrDepartment");  
		    //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
		    List<HrDepartment> list = query.list();
		    
		    //6.提交事务  
		    session.getTransaction().commit();  
		    //7.关闭session  
		    //session.flush(); 
		    
		    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
		    String sql="update BASE_T_ORG  set isdelete=1 where parent_node!='ROOT'";
		    orgService.executeSql(sql);
		    BaseOrg b=null;
		    for(HrDepartment h:list){
/*		    	Integer count=orgService.getCountSql("select count(*) from BASE_T_ORG where dept_id='"+h.getId()+"'");
		    	if(count==0){*/
		    		b=orgService.get(h.getId());
		    		if (b==null) {
						b=new BaseOrg(h.getId());
					}
		    		//b.setUuid(h.getId());
		    		b.setCreateTime(h.getCreateDate());
		    		b.setCreateUser(h.getCreateName());
		    		b.setIsDelete(h.getIsEnable()==0?1:0);
		    		b.setUpdateTime(h.getUpdateDate());
		    		b.setUpdateUser(h.getUpdateName());
		    		b.setDeptType("03");
		    		//b.setParentNode(h.getParentid());
		    		b.setNodeCode(h.getCode());
		    		b.setNodeText(h.getNames());
		    		b.setIssystem(0);
		    		b.setOrderIndex(h.getOrderby());
/*		    		Query query1=session.createSQLQuery("select * fromn HR_USER_DEPARTMENT_POSITION where DEPARTMENT_ID='"+h.getId()+"'");
		    		List<Object[]> listDP=query1.list();
		    		for(Object[] o:listDP){
		    			
		    		}*/
		    		Query query1=session.createSQLQuery("select count(*) from hr_department where PARENTID='"+h.getId()+"'");
		    		Integer count1=Integer.parseInt(query1.uniqueResult().toString());
		    		if(count1==0){
		    			b.setLeaf(true);
		    		}else{
		    			b.setLeaf(false);
		    		}
		    		int index=1;
		    		HrDepartment temp=h;
		    		String id=h.getId();
		    		String treeids="";
		    		boolean root=false;
		    		b.setParentNode("2851655E-3390-4B80-B00C-52C7CA62CB39");
		    		if (!temp.getParentid().equals("0")) {
		    			while (true) {
		    				b.setParentNode(h.getParentid());
			    			Query query2=session.createSQLQuery("select * from hr_department where ID='"+temp.getParentid()+"'").addEntity(HrDepartment.class);
				    		temp=(HrDepartment) query2.list().get(0);
				    		if (temp.getParentid().equals("0")) {
				    			treeids=temp.getId()+","+treeids;
								break;
							}
				    		treeids=temp.getId()+","+treeids;
				    		index++;
						}
					}else{
						root=true;
					}
		    		if (!root) {
		    			treeids=treeids+id;
		    			b.setTreeIds(treeids);
			    		b.setNodeLevel(index);
					}else{
						treeids=id;
						index=0;
						b.setTreeIds(treeids);
			    		b.setNodeLevel(index);
					}
		    		orgService.merge(b);
		    	}
		    List<BaseOrg> orgs = new ArrayList<>();
			NodeUtli<BaseOrg> nodes = new NodeUtli<>();
			nodes.setValue("2851655E-3390-4B80-B00C-52C7CA62CB39");
			NodeUtli<BaseOrg> node = null;
		    for(HrDepartment h:list){
	    		b=orgService.get(h.getId());
	    		if (b==null) {
					b=new BaseOrg(h.getId());
				}
	    		//根据id放入值
				orgs.add(b);
	    		BaseOrg parentOrg=orgService.get(b.getParentNode());
	    		String ex4=parentOrg.getExtField04();
	    		if (StringUtils.isEmpty(ex4)) {
	    			ex4="001";
				}
	    		b.setExtField05(ex4);
	    		orgService.merge(b);
		    }
		    
		    String i = "101";
			List<BaseOrg> orgs1 = new ArrayList<>();
			//分层
			for (BaseOrg o : orgs) {
				//获取根节点下值
				if(o.getParentNode().equals(nodes.getValue())){
					node = new NodeUtli<>(o,nodes,o.getUuid());
					o.setExtField04(i);
					i = (Integer.parseInt(i)+1)+"";
					o.setExtField05("001");
					nodes.getNodeList().add(node);
					orgService.merge(o);
				}else{
					orgs1.add(o);
				}
			}
			orgs.clear();
			for (NodeUtli<BaseOrg> nodeutil :nodes.getNodeList()) {
				i = "001";
				for (BaseOrg o : orgs1) {
					if(o.getParentNode().equals(nodeutil.getValue())){
						if(i.length()==3)
							i = nodeutil.getCurrentNode().getExtField04()+i;
						o.setExtField04(i);
						i = (Integer.parseInt(i)+1)+"";
						o.setExtField05(nodeutil.getCurrentNode().getExtField04());
						node = new NodeUtli<>(o,nodeutil,o.getUuid());
						nodeutil.getNodeList().add(node);
						orgService.merge(o);
					}else{
						orgs.add(o);
					}
				}
			}
			for (NodeUtli<BaseOrg> nodeutil :nodes.getNodeList()) {
				for (NodeUtli<BaseOrg> no : nodeutil.getNodeList()) {
					i =  no.getCurrentNode().getExtField04();
					for (BaseOrg o : orgs1) {
						if(o.getParentNode().equals(no.getValue())){
							if(i.length()==6)
								i = no.getCurrentNode().getExtField04()+"001";
							o.setExtField04(i);
							i = (Integer.parseInt(i)+1)+"";
							o.setExtField05(no.getCurrentNode().getExtField04());
							orgService.merge(o);
						}
					}
				}
			}
		    
		   // }
			
			job(HttpServletRequest1, HttpServletResponse1);
			deptjob(HttpServletRequestDeptJob,HttpServletResponseDeptJob);
			userdeptjob(HttpServletRequestUserDeptJob, HttpServletResponseUserDeptJob);
		     
		    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
			
		}
		
		@RequestMapping(value = "job")
		public void job(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//启用orcl数据库
			CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
			
			//3.打开session  
		    Session session = sssssss.openSession();  
		    //4.开启事务  
		    session.beginTransaction();
		
		    Query query = session.createQuery("from HrPosition");  
		    //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
		    List<HrPosition> list = query.list();
		    
		    //6.提交事务  
		    session.getTransaction().commit();  
		    //7.关闭session  
		    //session.flush(); 
		    
		    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
		    String sql="update BASE_T_JOB set isdelete=1";
		    jobservice.executeSql(sql);
		    BaseJob b=null;
		    for(HrPosition h:list){
	    		b=jobservice.get(h.getId());
	    		if (b==null) {
					b=new BaseJob(h.getId());
				}
	    		b.setCreateTime(h.getCreateDate());
	    		b.setCreateUser(h.getCreateName());
	    		b.setUpdateTime(h.getUpdateDate());
	    		b.setCreateUser(h.getUpdateName());
	    		b.setOrderIndex(h.getOrderby());
	    		b.setIsDelete(0);
	    		b.setJobName(h.getNames());
	    		b.setRemark(h.getDuty());
	    		
	    		jobservice.merge(b);
		    }
		    
		    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
		}
		
		//新岗位对应部门
		@RequestMapping(value = "deptjob")
		public void deptjob(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//启用orcl数据库
			CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
			
			//3.打开session  
		    Session session = sssssss.openSession();  
		    //4.开启事务  
		    session.beginTransaction();
		
		    Query query = session.createQuery("from HrDeptPosition");  
		    //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
		    List<HrDeptPosition> list = query.list();
		    
		    //6.提交事务  
		    session.getTransaction().commit();  
		    //7.关闭session  
		    //session.flush(); 
		    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
		    String sql="delete from BASE_T_DEPTJOB";
		    deptjobService.executeSql(sql);
		    BaseDeptjob dj=null;
		    for(HrDeptPosition hdp:list){
		    	dj=deptjobService.get(hdp.getId());
	    		if (dj==null) {
	    			dj=new BaseDeptjob(hdp.getId());
				}
	    		dj.setDeptId(hdp.getDepartmentId());
	    		dj.setJobId(hdp.getPositionId());
	    		dj.setParentdeptId(hdp.getPredepartmentId());
	    		dj.setParentjobId(hdp.getPrepositionId());
	    		dj.setCreateUser(hdp.getCreateName());
	    		dj.setCreateTime(hdp.getCreateDate());
	    		dj.setUpdateUser(hdp.getUpdateName());
	    		dj.setUpdateTime(hdp.getUpdateDate());
	    		dj.setIsDelete(0);
	    		dj.setJobType(2);
	    		deptjobService.merge(dj);
		    }
		    
		    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
		    
	
		}
		
		//新人员对应部门对应岗位
		@RequestMapping(value = "userdeptjob")
		public void userdeptjob(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//启用orcl数据库
			CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
			
			//3.打开session  
		    Session session = sssssss.openSession();  
		    //4.开启事务  
		    session.beginTransaction();
		
		    Query query = session.createQuery("from HrUserDepartmentPosition");  
		    //session.createSQLQuery("update dept set dname='SALES1' where deptno=30").executeUpdate();
		    List<HrUserDepartmentPosition> list = query.list();
		    
		    //6.提交事务  
		    session.getTransaction().commit();  
		    //7.关闭session  
		    //session.flush(); 
		    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
		    String sql="delete from BASE_T_USERDEPTJOB";
		    userdeptjobService.executeSql(sql);
		    BaseUserdeptjob udj=null;
		    for(HrUserDepartmentPosition hudp:list){
		    	udj=userdeptjobService.get(hudp.getId());
		    	if(udj==null){
		    		udj=new BaseUserdeptjob(hudp.getId());
		    	}
		    	udj.setUserId(hudp.getSysUserId());
		    	udj.setDeptId(hudp.getDepartmentId());
		    	if(null!=hudp.getDeptPositionId()&&!hudp.getDeptPositionId().equals("")){
		    		udj.setJobId(hudp.getDeptPositionId());
		    	}else{
		    		udj.setJobId("0");
		    	}
		    	udj.setMasterDept(hudp.getMasterflag());
		    	udj.setCreateUser(hudp.getCreateBy());
		    	udj.setCreateTime(hudp.getCreateDate());
				CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_ORACLE);
			    Query query1 = session.createQuery("from HrDeptPosition where positionId='"+hudp.getDeptPositionId()+"' and departmentId='"+hudp.getDepartmentId()+"'");  
			    List<HrDeptPosition> list1 = query1.list();
			    if(list1.size()>0){
			    	udj.setDeptjobId(list1.get(0).getId());
			    }else{
			    	udj.setDeptjobId("0");
			    }
			    CustomerContextHolder.setCustomerType(CustomerContextHolder.SESSION_FACTORY_MYSQL);
			    userdeptjobService.merge(udj);
			
		    }
		    writeJSON(response, jsonBuilder.returnSuccessJson("'同步成功'"));
		}

}
