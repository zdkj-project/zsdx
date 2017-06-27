
package com.zd.school.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.zd.core.util.StringUtils;
import com.zd.school.oa.doc.model.DocSenddoc;
import com.zd.school.oa.doc.service.DocDoctypeService;
import com.zd.school.oa.doc.service.DocSendcheckService;
import com.zd.school.oa.doc.service.DocSenddocService;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import com.zd.school.plartform.system.model.SysUser;



/**
 * 
 * ClassName: DocTSenddocController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 公文发文单实体Controller. date: 2016-07-05
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/DocTSenddoc") 
public class DocTSenddocController extends FrameWorkController<DocSenddoc> implements Constant {

    @Resource
    DocSenddocService thisService; // service层接口

    @Resource
    DocDoctypeService doctypeService; // service层接口

    @Resource
    DocSendcheckService docTSendCheckService; // service层接口

    @Resource
    BaseAttachmentService baseTAttachmentService;// service层接口

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute DocSenddoc entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<DocSenddoc> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * doAdd @Title: doAdd @Description: TODO @param @param DocTSenddoc
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(DocSenddoc entity, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取当前操作用户
    	String sendState = entity.getDocsendState();
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 生成默认的orderindex
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);// 排序
        // 生成发文流水号
        String sendNumb = thisService.createSendNumb(entity.getDoctypeId());
        entity.setSendshopname(sendNumb);

        entity.setDocsendState("0");
        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人

        // 持久化到数据库
        entity = thisService.merge(entity);

        // 2.DocTSendCheck类插入数据
        String sendId = entity.getUuid();
        String distribId = request.getParameter("distribId");
        thisService.doSendCheckEmp(sendId, distribId, userCh,sendState);

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
                // 移除文件信息
                String doIds = "'" + delIds.replace(",", "','") + "'";
                String hql = "DELETE FROM BaseTAttachment b  WHERE b.recordId IN (" + doIds + ")";
                baseTAttachmentService.executeHql(hql);

                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }

    /**
     * doRestore还原删除的记录 @Title: doRestore @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * DocTSenddoc @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(DocSenddoc entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码
    	String sendState = entity.getDocsendState();
        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        // entity.getSchoolId()要自己修改成对应的获取主键的方法
        DocSenddoc perEntity = thisService.get(entity.getUuid());
        String newDistribId = entity.getDistribId();
        String oldDistribId = perEntity.getDistribId();
        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        //perEntity.setModifyUser(userCh); // 设置修改人的中文名
        entity = thisService.merge(perEntity);// 执行修改方法

        if (!newDistribId.equals(oldDistribId))
            thisService.doSendCheckEmp(entity.getUuid(), newDistribId, userCh,sendState);

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
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
    @RequestMapping("/doUpload") // Filename sendId
	public void doUpload(
			@RequestParam("recordId") String recordId,
			@RequestParam(value = "attachIsMain", required = false, defaultValue = "0") Integer attachIsMain,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
        try {

            if (file != null) {
                // 取得当前上传文件的文件名称
                String myFileName = file.getOriginalFilename();
                if(attachIsMain==1)
                	myFileName="正文.docx";
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
                    
                    if(attachIsMain==1){	//如果插入的是正文数据，则进行数据判断
                    	String hql="from BaseTAttachment b where b.recordId='"+recordId+"' and b.attachIsMain=1";
                    	List<BaseAttachment> list=baseTAttachmentService.doQuery(hql,0,1);
                    	if(list.size()>0){
                    		//执行更新数据
                    		BaseAttachment baseTAttachment=list.get(0);
                    		baseTAttachment.setAttachUrl(url + fileName);
                    		baseTAttachment.setAttachType(type);
                    		baseTAttachment.setAttachSize(file.getSize());
                    		baseTAttachmentService.merge(baseTAttachment);
                    		
                    		writeJSON(response, "{ \"success\" : true,\"obj\":\""+url + fileName+"\"}");
                    		return;
                    	}
                    }
                    	
                   
                    // 插入数据
                    BaseAttachment bt = new BaseAttachment();
                    bt.setEntityName("DocTSenddoc");
                    bt.setRecordId(recordId);
                    bt.setAttachUrl(url + fileName);
                    bt.setAttachName(myFileName);
                    bt.setAttachType(type);
                    bt.setAttachSize(file.getSize());
                    bt.setAttachIsMain(attachIsMain);
                    baseTAttachmentService.merge(bt);
                    
                    writeJSON(response, "{ \"success\" : true,\"obj\":\""+url + fileName+"\"}");
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
        String attachIsMain = request.getParameter("attachIsMain");
        
        String hql = "";
        if(attachIsMain!=null){
        	hql = "from BaseTAttachment b where b.recordId='" + setRecordId + "' and b.attachIsMain="+attachIsMain+" order by b.createTime asc";
        }else{
        	hql = "from BaseTAttachment b where b.recordId='" + setRecordId + "' order by b.createTime asc";
        }
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

            String hql = "DELETE FROM BaseTAttachment b  WHERE b.attachId IN (" + doIds + ")";

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

    @RequestMapping("/issealed")
    public void isSealed(DocSenddoc entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        // 入库前检查代码

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 先拿到已持久化的实体
        DocSenddoc perEntity = thisService.get(entity.getUuid());

        // 将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); // 设置修改时间
        //perEntity.setModifyUser(userCh); // 设置修改人的中文名
        perEntity.setDocsendState("3");
        entity = thisService.merge(perEntity);// 执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));
    }

    @RequestMapping("/downLoadFile")
	public ResponseEntity<byte[]> downLoadFile( HttpServletRequest request) throws UnsupportedEncodingException
		{
		
		System.out.println("entry responseentity");
		
		String fileUrl=request.getParameter("url");
		String fileName=request.getParameter("fileName");
		
        if (fileName != null) {  
        	fileName = new String(fileName.getBytes("iso-8859-1"), "UTF-8");  
        }  
        
		if(fileUrl==null)
			return null;
		
		byte[] body = null;
		
		String path=request.getServletContext().getRealPath("/");
		File file = new File(path+"/"+fileUrl);
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			body = new byte[in.available()];
			in.read(body);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename="+fileName);

		HttpStatus statusCode = HttpStatus.OK;

		ResponseEntity<byte[]> response = new ResponseEntity<>(body, headers,
				statusCode);

		return response;
	}


    /**
     * 
     * doSend:发送公文.
     *
     * @author luoyibo
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *             void
     * @throws @since
     *             JDK 1.8
     */
    @RequestMapping("/dosend")
    public void doSend(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        String reMessage = "'发送成功'";

        // 获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        // 要发送至的部门ID
        String deptIds = request.getParameter("deptIds");
        // 要发送的公文ID
        String sendIds = request.getParameter("sendIds");

        Integer isSend = thisService.sendDocToDept(sendIds, deptIds, userCh);
        if (isSend == 0)
            reMessage = "'发送成功'";

        writeJSON(response, jsonBuilder.returnSuccessJson(reMessage));
    }

}
