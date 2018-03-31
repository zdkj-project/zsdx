
package com.zd.school.oa.notice.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticeOther;
import com.zd.school.oa.notice.service.OaNoticeService;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaNoticeController Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 公告信息表(OA_T_NOTICE)实体Controller. date: 2016-12-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/OaNotice")
public class OaNoticeController extends FrameWorkController<OaNotice> implements Constant {

	@Resource
	OaNoticeService thisService; // service层接口

	@Resource
	private CommTreeService treeSerice;

	@Resource
	private BaseAttachmentService  baseTAttachmentService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute OaNotice entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<OaNotice> qResult = thisService.list(start, limit, sort, filter, true);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param OaNotice
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(OaNotice entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等
		String deptIds = request.getParameter("deptIds");
		String roleIds = request.getParameter("roleIds");
		String userIds = request.getParameter("userIds");
		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doAddEntity(entity, currentUser, deptIds, roleIds, userIds);// 执行增加方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
		}
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
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
			SysUser currentUser = getCurrentSysUser();
			try {
				boolean flag = thisService.doLogicDeleteByIds(delIds, currentUser);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
				}
			} catch (Exception e) {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,详情见错误日志'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录
	 * 
	 * @Title: doUpdate
	 * @Description:
	 * @param OaNotice
	 * @param request
	 * @param response
	 * @throws IOException
	 * @return void 返回类型
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(OaNotice entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String deptIds = request.getParameter("deptIds");
		String roleIds = request.getParameter("roleIds");
		String userIds = request.getParameter("userIds");
		// 入库前检查代码

		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doUpdateEntity(entity, currentUser, deptIds, roleIds, userIds);// 执行修改方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		}
	}

	/**
	 * getTypeTree 获取信息分类树形数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getTypeTree")
	public void getTypeTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = "";

		List<CommTree> lists = treeSerice.getCommTree("OA_V_NOTICETYPETREE", whereSql);

		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 获取指定公告的通知部门、角色、人员的信息
	 * 
	 * @param id
	 *            指定的公告id
	 * @return
	 */
	@RequestMapping("/getNoticeOther")
	public void getNoticeOther(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String id = request.getParameter("noticeId");
		if (StringUtils.isNotEmpty(id)) {
			OaNoticeOther other = thisService.getNoticeOther(id);
			strData = jsonBuilder.toJson(other);// 处理数据
			writeJSON(response, jsonBuilder.returnSuccessJson(strData));// 返回数据
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("'无数据'"));// 返回数据
		}
	}

	@RequestMapping("/getUserOaNotice")
	public @ResponseBody List<OaNotice> getUserOaNotice(HttpServletRequest request, HttpServletResponse response) {
		SysUser currentUser = getCurrentSysUser();
		List<OaNotice> list = thisService.getUserOaNotice(currentUser);
		return list;
	}

	@RequestMapping("/getOaNoticeById")
	public @ResponseBody OaNotice getOaNoticeById(HttpServletRequest request, HttpServletResponse response) {
		String uuid = request.getParameter("uuid");
		return thisService.get(uuid);
	}
	
	
	
	/**
	 * 上传文件
	 * 
	 * @param sendId
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/doUpload" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void doUpload(@RequestParam("recordId") String recordId,
			@RequestParam(value = "attachIsMain", required = false, defaultValue = "0") Integer attachIsMain,
			@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

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
					String url = "/static/upload/OaNotice/" + sdf.format(System.currentTimeMillis()) + "/";
					
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
					bt.setEntityName("OaNotice");
					bt.setRecordId(recordId);
					bt.setAttachUrl(url + fileName);
					bt.setAttachName(myFileName);
					bt.setAttachType(type);
					bt.setAttachSize(file.getSize());
					bt.setAttachIsMain(attachIsMain);
					baseTAttachmentService.doMerge(bt);

					writeJSON(response, "{ \"success\" : true,\"obj\":\"" + url + fileName + "\"}");
				}
			}

		} catch (Exception e) {
			writeJSON(response, "{ \"success\" : false,\"obj\":null}");
			return;
		}
	}

	@RequestMapping("/getFileList") // Filename sendId
	public void getFileList(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String setRecordId = request.getParameter("recordId");
		if (setRecordId == null) {
			writeJSON(response, "[]");
			return;
		}		
		String hql = "from BaseAttachment b where b.recordId='" + setRecordId + "' and b.entityName='OaNotice' order by b.createTime asc";
	
		List<BaseAttachment> list = baseTAttachmentService.getQuery(hql);

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

			int flag = baseTAttachmentService.doExecuteHql(hql);

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