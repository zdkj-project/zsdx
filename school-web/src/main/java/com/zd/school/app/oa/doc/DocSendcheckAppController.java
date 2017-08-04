
package com.zd.school.app.oa.doc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.oa.doc.model.AllUserDoc;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.model.DocSendcheck;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.oa.doc.service.DocReceiveService;
import com.zd.school.oa.doc.service.DocRecexaminesService;
import com.zd.school.oa.doc.service.DocSendcheckService;
import com.zd.school.oa.doc.service.DocSenddocService;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.model.BaseDic;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.model.BaseOrgTree;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.baseset.service.BaseDicService;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
/**
 * 
 * ClassName: DocSendcheckController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公文发文核稿人实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/DocSendcheck")
public class DocSendcheckAppController extends FrameWorkController<DocSendcheck> implements Constant {

	@Resource
	private DocSendcheckService thisService; // service层接口

	@Resource
	private DocSenddocService sendService;
	
	@Resource
	private SysUserService userService;
    @Resource
    private PushInfoService pushService;
    @Resource
    BaseAttachmentService BaseService;
    @Resource
    DocRecexaminesService docService;
    @Resource
    private BaseDicitemService DicitemService;
    @Resource
    private BaseDicService dictionaryService;
    @Resource
    DocRecexaminesService recexaminesService; // service层接口

    @Resource
    DocReceiveService docReceiveService;
    
    @Resource
    private BaseOrgService orgService; // service层接口
    
    @Resource
    TeaTeacherbaseService teacherService; // service层接口
    
	/**
	 * 
	 * list:待核稿公文列表 .
	 * 
	 * @author luoyibo
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute DocSendcheck entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String userId = "";
		SysUser currentUser = getCurrentSysUser();

		if (currentUser != null)
			userId = currentUser.getUuid();
		String strData = ""; // 返回给js的数据
		String sort = request.getParameter("sort");
		String sortSql = "";
		if (sort.length() > 0)
			sortSql = StringUtils.convertSortToSql(sort);
		// hql语句
		StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " o where 1=1 ");
		// 总记录数
		StringBuffer countHql = new StringBuffer(
				"select count(*) from " + entity.getClass().getSimpleName() + " where   1=1");

		// if (StringUtils.isNotEmpty(userId)) {
		// hql.append(" and userId='" + userId + "' ");
		// countHql.append(" and userId='" + userId + "' ");
		// }
		String whereSql = entity.getWhereSql();// 查询条件
		String parentSql = entity.getParentSql();// 条件
		String querySql = entity.getQuerySql();// 查询条件
		String orderSql = entity.getOrderSql();// 排序

		int start = super.start(request); // 起始记录数
		int limit = entity.getLimit();// 每页记录数
		hql.append(whereSql);
		hql.append(parentSql);
		hql.append(querySql);
		if (orderSql.length() > 0) {
			if (sortSql.length() > 0)
				hql.append(orderSql + " , " + sortSql);
		} else {
			if (sortSql.length() > 0)
				hql.append(" order by  " + sortSql);
		}

		countHql.append(whereSql);
		countHql.append(querySql);
		countHql.append(parentSql);
		List<DocSendcheck> lists = thisService.doQuery(hql.toString(), start, limit);// 执行查询方法
		Integer count = thisService.getCount(countHql.toString());// 查询总记录数
		List<DocSendcheck> newLists = new ArrayList<DocSendcheck>();
		for (DocSendcheck dd : lists) {
			String uuid = dd.getUuid();
			DocSenddoc send = sendService.get(dd.getSendId());
			BeanUtils.copyPropertiesExceptNull(dd, send);
			dd.setUuid(uuid);
			newLists.add(dd);
		}
		strData = jsonBuilder.buildObjListToJson(new Long(count), newLists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * doRestore:批量核稿.
	 *
	 * @author luoyibo
	 * @param request
	 * @param response
	 * @throws IOException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/batchcheck")
	public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		String opininon = request.getParameter("opininon");
		String userCh = "超级管理员";

		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要批量核稿的公文'"));
			return;
		} else {
			// String hql = "UPATE DocSendcheck SET modifyUser='"
			Integer isExecute = thisService.doBatchcheck(opininon, delIds, userCh);

			if (isExecute > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'批量核稿成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'批量核稿失败'"));
			}
		}
	}

	/**
	 * 
	 * doUpdates:设置单个核稿完毕.
	 *
	 * @author luoyibo
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 *             void
	 * @throws @since
	 *             JDK 1.8
	 */
	@RequestMapping("/ischeck")
	public @ResponseBody String doIsCheck(String distribType,String opininon,String uuid,String distribId,String userid, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		SysUser currentUser = userService.get(userid);
		DocSendcheck perEntity=thisService.get(uuid);
		perEntity.setOpininon(opininon);
		thisService.isDoCheck(distribId, distribType, perEntity, currentUser);

		return "success";
	}

	@RequestMapping(value = { "/useralldoclist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getUserAlldocList(@ModelAttribute DocSendcheck entity,String personUserId, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
		String strData = "";
		SysUser currentUser = userService.get(personUserId);
		String whereSql = request.getParameter("whereSql");

		List<AllUserDoc> lists = thisService.getUserAllDoc(currentUser, whereSql);// 执行查询方法

		strData = jsonBuilder.buildObjListToJson(new Long(lists.size()), lists, true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}
	
	@RequestMapping(value = { "/detailed" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ModelAndView detailed(ModelAndView mav,String id,String personUserId, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
	
		String wheresql=" and recordID='"+id+"' and operType!='盖章'";
		SysUser currentUser = userService.get(personUserId);
		List<AllUserDoc> lists = thisService.getUserAllDoc(currentUser, wheresql);// 执行查询方法
		if(lists.get(0).getDoState().equals("1")){
			mav.setViewName("forward:/static/core/coreApp/person/phoneperson/error.jsp");
		}else{
			mav.addObject("rec",lists);
			mav.addObject("userid",personUserId);
			if((lists.get(0).getOperType()).equals("传阅")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/transmit.jsp");
			}else if((lists.get(0).getOperType()).equals("批阅")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/recexamines.jsp");
			}else if((lists.get(0).getOperType()).equals("核稿")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/sendcheck.jsp");
			}
		}
		return mav;
		
	}
	
	@RequestMapping(value = { "/detailedPad" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ModelAndView detailedPad(ModelAndView mav,String id,String personUserId, HttpServletRequest request,
			HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {
	
		String wheresql=" and recordID='"+id+"' and operType!='盖章'";
		SysUser currentUser = userService.get(personUserId);
		List<AllUserDoc> lists = thisService.getUserAllDoc(currentUser, wheresql);// 执行查询方法
		if(lists.get(0).getDoState().equals("1")){
			mav.setViewName("forward:/static/core/coreApp/person/phoneperson/errorPad.jsp");
		}else{
			mav.addObject("rec",lists);
			mav.addObject("userid",personUserId);
			if((lists.get(0).getOperType()).equals("传阅")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/transmitPad.jsp");
			}else if((lists.get(0).getOperType()).equals("批阅")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/recexaminesPad.jsp");
			}else if((lists.get(0).getOperType()).equals("核稿")){
				mav.setViewName("forward:/static/core/coreApp/person/phoneperson/sendcheckPad.jsp");
			}
		}
		return mav;
		
	}
	
    @RequestMapping("/getFileList")	//Filename	sendId
    public @ResponseBody List<HashMap<String, Object>> getFileList(String docId,HttpServletRequest request, HttpServletResponse response) throws IOException {
    	

    	if(docId==null){
    		writeJSON(response,"[]");
    		return null;
    	}
   
    	String hql="from BaseAttachment b where b.recordId='"+docId+"' ";

    	hql += " order by b.createTime asc";
    	List<BaseAttachment> list = BaseService.doQuery(hql);
    	
    	List<HashMap<String, Object>> lists=new ArrayList<>();
    	HashMap<String, Object> maps=null;
    	for(BaseAttachment bt : list){
    		maps = new LinkedHashMap<>();
    		maps.put("id", "SWFUpload_" + bt.getUuid());
    		maps.put("name", bt.getAttachName());
    		maps.put("size", bt.getAttachSize());
    		maps.put("type", bt.getAttachType());
    		maps.put("status", 0);
    		maps.put("percent", 100);
    		maps.put("fileId", bt.getUuid());
    		maps.put("fileUrl", bt.getAttachUrl());
    		lists.add(maps);
    	}
		return lists;
    	
    }
    @RequestMapping("/isread")
    public @ResponseBody String isRead(String uuid,String userid, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = userService.get(userid);
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        DocRecexamines perEntity = docService.get(uuid);


        perEntity.setUpdateTime(new Date()); // 设置修改时间
        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
        perEntity.setState("1");
        docService.merge(perEntity);// 执行修改方法

        String docrecId = perEntity.getDocrecId();
        String hql = "select count(*) from DocRecexamines where docrecId='" + docrecId
                + "' and recexamType=1 and state=0 ";
        Integer count = thisService.getCount(hql);
        if (count == 0) {
            String hql2 = "update DocReceive set docrecState=5 where uuid='" + docrecId + "'";
            thisService.executeHql(hql2);
            return "success";
        }
		return "success";
    }
    
    @RequestMapping(value = "/getDicItemByDicCode")
    public @ResponseBody List<BaseDicitem> getDicItemByDicCode(String itemCode,String dicCode,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";


            BaseDic dictionary = dictionaryService.getByProerties("dicCode", dicCode);
            String hql = " from BaseDicitem where isDelete=0 and dicId='" + dictionary.getUuid()+"' and itemCode='"+itemCode+"' order by orderIndex asc, itemCode asc ";
            List<BaseDicitem> lists = DicitemService.doQuery(hql);
			return lists;
        
    }
    
    
    @RequestMapping("/doupdate")
    public @ResponseBody String doUpdates(String distribType,String opininon,String uuid,String distribId,String userid,HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        // 获取当前的操作用户
    	SysUser currentUser = userService.get(userid);
        DocRecexamines perEntity = recexaminesService.get(uuid);
        perEntity.setOpininon(opininon);
        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
//        DocRecexamines perEntity = thisService.get(entity.getUuid());
//
//        // 将entity中不为空的字段动态加入到perEntity中去。
//        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
//
//        perEntity.setUpdateTime(new Date()); // 设置修改时间
//        perEntity.setUpdateUser(userCh); // 设置修改人的中文名
//        perEntity.setState("1");
        recexaminesService.setIsPiYue(perEntity, distribType, distribId, currentUser);// 执行修改方法
//
//        String docrecId = entity.getDocrecId();
//        String hql = "select count(*) from DocRecexamines where docrecId='" + docrecId
//                + "' and recexamType=0 and state=0";
//        Integer count = thisService.getCount(hql);
//        if (count == 0) {
//            String hql2 = "update DocReceive set docrecState=3 where uuid='" + docrecId + "'";
//            thisService.executeHql(hql2);
//        } else {
//            String hql2 = "update DocReceive set docrecState=2 where uuid='" + docrecId + "'";
//            thisService.executeHql(hql2);
//        }
		return "success";

        
    }
    
    @RequestMapping("/treelist")
    public void getOrgTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        String orderSql = request.getParameter("orderSql");

        List<BaseOrgTree> lists = orgService.getOrgTreeList(whereSql, orderSql, null);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    @RequestMapping(value = { "/teacherList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
       

        QueryResult<TeaTeacherbase> qr = teacherService.getDeptTeacher(super.start(request), super.limit(request),
               super.whereSql(request));

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    
}
