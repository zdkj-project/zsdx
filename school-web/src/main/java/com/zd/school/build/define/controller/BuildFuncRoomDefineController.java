
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
import com.zd.school.build.define.model.BuildFuncRoomDefine;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildFuncRoomDefineService;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BuildFuncroomdefinController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: BUILD_T_FUNCROOMDEFIN实体Controller. date:
 * 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BuildFuncRoomDefine")
public class BuildFuncRoomDefineController extends FrameWorkController<BuildFuncRoomDefine> implements Constant {

	@Resource
	BuildFuncRoomDefineService thisService; // service层接口

	@Resource
	BuildRoominfoService infoService;// 房间

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute BuildFuncRoomDefine entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<BuildFuncRoomDefine> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BuildFuncroomdefin
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(BuildFuncRoomDefine entity, HttpServletRequest request, HttpServletResponse response)
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
                BuildFuncRoomDefine perEntity = new BuildFuncRoomDefine();
        		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
                //生成默认的orderindex
                Integer orderIndex = thisService.getDefaultOrderIndex(entity);
                entity.setRoomId(ids[i]);//设置房间id
                entity.setCreateUser(userCh); //创建人
                entity.setUpdateUser(userCh); //创建人的中文名
                entity.setOrderIndex(orderIndex);//排序
                thisService.merge(entity); // 执行添加方法
                
                BuildRoominfo info = infoService.get(ids[i]);
                info.setUpdateTime(new Date());
                info.setUpdateUser(userCh);
                info.setRoomType("5");//设置房间类型为教室--1.宿舍，2.办公室，3.教室，4、实验室，5、功能室，9、其它，0、未分配
                info.setAreaStatu(1);//设置为已分配
                //执行更新方法
                infoService.merge(info);
            }
        writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));
	}

	/**
	 * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
	 * request @param @param response @param @throws IOException 设定参数 @return
	 * void 返回类型 @throws
	 */
	@RequestMapping("/dodelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String delIds = request.getParameter("ids");
		int fs = 0;
		if (StringUtils.isEmpty(delIds)) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
			return;
		} else {
			String[] ids = delIds.split(",");
			BuildRoominfo roominfo = null;
			BuildFuncRoomDefine defin = null;
			for (int j = 0; j < ids.length; j++) {
				defin = thisService.get(ids[j]);
				roominfo = infoService.get(defin.getRoomId());
				roominfo.setUpdateTime(new Date());
				roominfo.setRoomType("0");// 设置房间类型为空
				roominfo.setAreaStatu(0);// 设置房间状态为未分配
				// 执行更新方法
				infoService.merge(roominfo);
				defin.setIsDelete(1);
				thisService.merge(defin);
				++fs;
			}
			if (fs > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'功能室已分配的未删除，未分配的已删除。'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'功能室都已分配了，不允许删除。'"));
			}

		}
	}
}
