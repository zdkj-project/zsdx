package com.zd.school.build.define.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.service.JwClassDormAllotService;
import com.zd.school.build.define.model.BuildDormDefine;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildDormDefineService;
import com.zd.school.build.define.service.BuildRoomareaService;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BuildOfficeController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 办公室信息实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BuildDormDefine")
public class BuildDormDefineController extends FrameWorkController<BuildDormDefine> implements Constant {

	@Resource
	BuildDormDefineService thisService; // service层接口

	@Resource
	BuildRoomareaService areaService; // BuildRoomarea接口
	
	@Resource
	BuildRoominfoService infoService;// 房间
	
	@Resource
	JwClassDormAllotService classDormService;// 班级分配宿舍

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute BuildDormDefine entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<BuildDormDefine> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 用于一键分配宿舍时使用
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/onKeylist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void onKeylist(@ModelAttribute BuildDormDefine entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<BuildDormDefine> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);
		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}
	
	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BuildOffice
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(BuildDormDefine entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String userCh = "超级管理员";//中文名
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String[] ids = entity.getRoomId().split(",");
        if (ids != null)
            for (int i = 0; i < ids.length; i++) {
                boolean cz = thisService.IsFieldExist("roomId", ids[i], "-1", "isdelete=0");
                if (cz) {
                    writeJSON(response, jsonBuilder.returnFailureJson("'请勿重复添加。'"));
                    return;
                }
                BuildRoominfo info = infoService.get(ids[i]);
                info.setUpdateTime(new Date());
                info.setUpdateUser(userCh);
                info.setRoomType("1");//设置房间类型为教室--1.宿舍，2.办公室，3.教室，4、实验室，5、功能室，9、其它，0、未分配
                info.setAreaStatu(1);//设置为已分配
                //执行更新方法
                infoService.merge(info);
                BuildDormDefine perEntity = new BuildDormDefine();
        		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
                //生成默认的orderindex
                Integer orderIndex = thisService.getDefaultOrderIndex(entity);
                entity.setRoomId(ids[i]);//设置房间id
                entity.setCreateUser(userCh); //创建人
                entity.setUpdateUser(userCh); //创建人的中文名
                entity.setOrderIndex(orderIndex);//排序
                thisService.merge(entity); // 执行添加方法
            }
        entity.setUuid(null);
        writeJSON(response, jsonBuilder.returnSuccessJson("'定义成功。'"));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		int count = 0;
		int fs = 0;
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			String[] ids = delIds.split(",");
			BuildRoominfo roominfo = null;
			BuildDormDefine define = null;
			for (int j = 0; j < ids.length; j++) {
				define = thisService.get(ids[j]);
				boolean cz = classDormService.IsFieldExist("dormId", define.getUuid(), "-1", "isdelete=0");
				if (cz) {
					++count;
				}
				if (count == 0) {
					roominfo = infoService.get(define.getRoomId());
					roominfo.setUpdateTime(new Date());
					roominfo.setRoomType("0");// 设置房间类型为空
					roominfo.setAreaStatu(0);// 设置房间状态为未分配
					// 执行更新方法
					infoService.merge(roominfo);
					define.setIsDelete(1);
					thisService.merge(define);
					++fs;
				}
				count = 0;
			}
			if (fs > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'已分配的宿舍未删除，未分配的已删除。'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'宿舍都已分配，不允许删除。'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * BuildOffice @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(BuildDormDefine entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		 //先拿到已持久化的实体
		BuildDormDefine perEntity = thisService.get(entity.getUuid());
        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
        if (entity.getTteacId() != null && !entity.getTteacId().equals(""))
            perEntity.setDormAdmin(entity.getTteacId()); //设置教师id
        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.merge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));

	}
}
