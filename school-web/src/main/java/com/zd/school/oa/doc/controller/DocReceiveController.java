
package com.zd.school.oa.doc.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.DateUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.oa.doc.model.DocDoctype;
import com.zd.school.oa.doc.model.DocReceive;
import com.zd.school.oa.doc.model.DocRecexamines;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.oa.doc.service.DocDoctypeService;
import com.zd.school.oa.doc.service.DocReceiveService;
import com.zd.school.oa.doc.service.DocRecexaminesService;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: DocReceiveController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公文收文单实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/DocReceive")
public class DocReceiveController extends FrameWorkController<DocReceive> implements Constant {

	@Resource
	DocReceiveService thisService; // service层接口

	@Resource
	DocRecexaminesService recExamineService;

	@Resource
	DocDoctypeService docTypeService;

	@Resource
	BaseAttachmentService baseTAttachmentService;// service层接口

	@Resource
	PushInfoService pushInfoService;

	@Resource
	TeaTeacherbaseService jwTTeacherService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute DocReceive entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String strData = ""; // 返回给js的数据
		String sendDocId = request.getParameter("sendDocId");
		SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		if (StringUtils.isNotEmpty(sendDocId)) {
			whereSql += (" and o.sendId='" + sendDocId + "' ");
		}
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<DocReceive> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * fenfalist:待分发传阅的收文（所有领导都已批阅）.
	 *
	 * @author luoyibo
	 * @param @param
	 *            entity
	 * @param @param
	 *            request
	 * @param @param
	 *            response
	 * @param @throws
	 *            IOException
	 * @param @throws
	 *            IllegalAccessException
	 * @param @throws
	 *            InvocationTargetException
	 * @return void
	 * @throws @since
	 *             JDK 1.8
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/fenfalist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void fenfalist(@ModelAttribute DocReceive entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String strData = ""; // 返回给js的数据
		SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<DocReceive> qResult = thisService.fenfalist(start, limit, sort, filter, whereSql, orderSql, currentUser);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * doAdd @Title: doAdd @Description: TODO @param @param DocReceive
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping("/doadd")
	public void doAdd(DocReceive entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		SysUser currentUser = getCurrentSysUser();

		// 持久化到数据库
		entity = thisService.doAdd(entity, currentUser);

		// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	/**
	 * doDelete @Title: doDelete @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	@RequestMapping("/fenfa")
	public void fenfa(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 收文分发的对象
		String[] userIds = request.getParameter("distribId").split(",");
		String[] docrecIds = request.getParameter("docrecId").split(",");
		String idString = request.getParameter("docrecId");
		// 获取当前操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		StringBuffer hql = new StringBuffer("UPDATE DocReceive SET updateUser='");
		String doIds = "'" + idString.replace(",", "','") + "'";
		hql.append(userCh + "', updateTime=CONVERT(datetime,'");
		hql.append(DateUtil.formatDateTime(new Date()) + "'),docrecState='4'  WHERE uuid IN (" + doIds + ")");
		Integer isExecute = thisService.executeHql(hql.toString());
		// if (isExecute > 0) {
		// writeJSON(response, jsonBuilder.returnSuccessJson("'分发传阅成功'"));
		// } else {
		// writeJSON(response, jsonBuilder.returnFailureJson("'分发传阅 失败'"));
		// }
		for (String userId : userIds) {
			for (String docrecId : docrecIds) {
				DocRecexamines recExamine = new DocRecexamines();
				recExamine.setDocrecId(docrecId);
				recExamine.setUserId(userId);
				recExamine.setRecexamType("1");
				recExamine.setCreateUser(userCh);
				recExamine.setState(StatuVeriable.ISNOTDELETE);
				recExamineService.persist(recExamine);
			}

			TeaTeacherbase j = jwTTeacherService.get(userId);
			if (j != null) {
				PushInfo pushInfo = new PushInfo();
				pushInfo.setEmplName(j.getXm());
				pushInfo.setEmplNo(j.getUserNumb());
				pushInfo.setRegTime(new Date());
				pushInfo.setEventType("分发阅读");
				pushInfo.setPushStatus(0);
				pushInfo.setPushWay(1);
				pushInfo.setRegStatus("您有一封未阅读的文件，请及时阅读！");
				pushInfoService.merge(pushInfo);
			}
		}

		writeJSON(response, jsonBuilder.returnSuccessJson("'传阅分发成功'"));
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * DocReceive @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(DocReceive entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();

		DocReceive perEntity = thisService.doUpdate(entity, currentUser);

		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

	}

	/**
	 * 上传文件
	 * 
	 * @param recordId
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/doUpload") // Filename sendId
	public void doUpload(@RequestParam("recordId") String recordId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			if (file != null) {
				// 取得当前上传文件的文件名称
				String myFileName = file.getOriginalFilename();
				// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
				if (myFileName.trim() != "") {
					// 重命名上传后的文件名
					String type = myFileName.substring(myFileName.lastIndexOf("."));
					String fileName = String.valueOf(System.currentTimeMillis()) + type;

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String url = "/static/upload/doc/" + sdf.format(System.currentTimeMillis()) + "/";

					String rootPath = request.getSession().getServletContext().getRealPath("/");
					rootPath = rootPath.replace("\\", "/");

					// 定义上传路径
					String path = rootPath + url + fileName;
					File localFile = new File(path);

					if (!localFile.exists()) { // 判断文件夹是否存在
						localFile.mkdirs(); // 不存在则创建
					}

					file.transferTo(localFile);

					// 插入数据
					BaseAttachment bt = new BaseAttachment();
					bt.setEntityName("DocReceive");
					bt.setRecordId(recordId);
					bt.setAttachUrl(url + fileName);
					bt.setAttachName(myFileName);
					bt.setAttachType(type);
					bt.setAttachSize(file.getSize());
					baseTAttachmentService.merge(bt);
				}
			}

		} catch (Exception e) {
			writeJSON(response, "{ \"success\" : false}");
			return;
		}
		writeJSON(response, "{ \"success\" : true}");
	}

	@RequestMapping("/getFileList") // Filename sendId
	public void getFileList(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String setRecordId = request.getParameter("recordId");
		if (setRecordId == null) {
			writeJSON(response, "[]");
			return;
		}

		String hql = "from BaseAttachment b where b.recordId='" + setRecordId + "'  and b.entityName='DocReceive' order by b.createTime asc";
		List<BaseAttachment> list = baseTAttachmentService.doQuery(hql);

		List<HashMap<String, Object>> lists = new ArrayList<>();
		HashMap<String, Object> maps = null;
		for (BaseAttachment bt : list) {
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

		writeJSON(response, jsonBuilder.toJson(lists));
	}

	@RequestMapping("/doDeleteFile") // Filename sendId
	public void doDeleteFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String fileIds = request.getParameter("fileIds");

			String doIds = "'" + fileIds.replace(",", "','") + "'";

			String hql = "DELETE FROM BaseAttachment b  WHERE b.uuid IN (" + doIds + ")";

			int flag = baseTAttachmentService.executeHql(hql);

			if (flag > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("\"删除成功\""));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("\"删除失败\""));
			}
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("\"删除失败,请刷新重试！\""));
		}
	}
}
