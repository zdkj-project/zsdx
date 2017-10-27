
package com.zd.school.build.define.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: BuildRoominfoController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 教室信息实体Controller. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BuildRoominfo")
public class BuildRoominfoController extends FrameWorkController<BuildRoominfo> implements Constant {

	private static Logger logger = Logger.getLogger(BuildRoominfoController.class);

	@Resource
	private  BuildRoominfoService thisService; // service层接口
	@Resource
	private  CommTreeService treeService; // 生成树
	

	@Resource
	private TrainClassService classService; // service层接口
	
	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute BuildRoominfo entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String filter = request.getParameter("filter");
		String classId = request.getParameter("classId");
		
		if(classId!=null){
			//查询出这个班级中已经安排宿舍的宿舍ID
			String sql="select a.Room_ID from TRAIN_T_CLASSTRAINEE a"
					+ " join BUILD_T_ROOMINFO b on a.Room_ID=B.ROOM_ID"
					+ " where  a.isdelete=0 and b.isdelete=0 and "
					+ " a.class_ID='"+classId+"' "
					+ " group by  a.Room_ID,b.ext_field03 "
					+ " having count(a.Room_ID)=b.ext_field03";
			List<Map<String,Object>> roomList=thisService.getForValuesToSql(sql);
			
			
			StringBuffer ids=new StringBuffer();
			for(int i=0;i<roomList.size();i++){
				ids.append(roomList.get(i).get("Room_ID")+",");
			}
			if(ids.length()>0){
				if(filter.length()>0){
					filter=filter.substring(0, filter.length()-1);
					filter+=",{\"type\":\"string\",\"comparison\":\"not in\",\"value\":\""+ids.substring(0, ids.length()-1)+"\",\"field\":\"uuid\"}]";
				}else{
					filter="[{\"type\":\"string\",\"comparison\":\"not in\",\"value\":\""+ids.substring(0, ids.length()-1)+"\",\"field\":\"uuid\"}]";
				}
			}
		}
		
		
		String strData = ""; // 返回给js的数据
		
		QueryResult<BuildRoominfo> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), filter, true);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param BuildRoominfo
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(BuildRoominfo entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
			String areaId = entity.getAreaId();
			String roomName = entity.getRoomName();
			String rommType=entity.getRoomType();
			// 此处为放在入库前的一些检查的代码，如唯一校验等
			String hql = " o.isDelete='0' and o.areaId='" + areaId + "' ";
			if (thisService.IsFieldExist("roomName", roomName, "-1", hql)) {
				writeJSON(response, jsonBuilder.returnFailureJson("'同一区域已有此房间！'"));
				return;
			}
			Integer orderIndex = thisService.getDefaultOrderIndex(entity);
			// 获取当前操作用户
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();

			BuildRoominfo perEntity = new BuildRoominfo();
			BeanUtils.copyPropertiesExceptNull(entity,perEntity);
			entity.setOrderIndex(orderIndex);
			// 增加时要设置创建人
			entity.setCreateUser(userCh); // 创建人
			// 持久化到数据库
			entity.setRoomType(rommType);
			entity = thisService.merge(entity);
			// 返回实体到前端界面
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
	}

	/**
	 * 批量添加房间
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/batChdoAdd")
	public void batChdoAdd(BuildRoominfo entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		SysUser currentUser = getCurrentSysUser();
		try {
			Boolean bResult = thisService.batchAddRoom(entity, currentUser);
			if(bResult){
				writeJSON(response, jsonBuilder.returnSuccessJson("'批量添加房间成功'"));
			} else
				writeJSON(response, jsonBuilder.returnSuccessJson("'批量添加房间失败，详情见错误日志'"));
		} catch (Exception e){
            logger.error(e.getMessage());
            writeJSON(response, jsonBuilder.returnSuccessJson("'批量添加房间失败，详情见错误日志'"));
        }
			// 此处为放在入库前的一些检查的代码，如唯一校验等
/*			String hql = " o.isDelete='0' and o.areaId='" + entity.getAreaId() + "' ";
			if (thisService.IsFieldExist("roomName", entity.getRoomName(), "-1", hql)) {
				writeJSON(response, jsonBuilder.returnFailureJson("'同一区域已有此房间！'"));
				return;
			}
			String roomName = entity.getRoomName();
			String roomNames = roomName.substring(0, 2);
			String is = roomName.substring(2,roomName.length());
			Integer orderIndex = 0;
			BuildRoominfo perEntity = null;
			int roomCount = entity.getRoomCount();
			// 获取当前操作用户
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();
			for (int i = 0; i < roomCount; i++) {
				orderIndex = thisService.getDefaultOrderIndex(entity);
				perEntity = new BuildRoominfo();
				BeanUtils.copyPropertiesExceptNull(entity,perEntity);
				entity.setRoomName(roomNames + (Integer.valueOf(is) + i));
				entity.setOrderIndex(orderIndex);
				entity.setExtField01(entity.getRoomName());
				// 增加时要设置创建人
				entity.setCreateUser(userCh); // 创建人
				// 持久化到数据库
				thisService.merge(entity);
			}*/
			// 返回实体到前端界面
		
		
		//writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
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
			boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
			if (flag) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
		}
	}

	/**
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * BuildRoominfo @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(BuildRoominfo entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String areaId = entity.getAreaId();
		String roomName = entity.getRoomName();
		String roomId = entity.getUuid();
		// 此处为放在入库前的一些检查的代码，如唯一校验等
		String hql = " o.isDelete='0' and o.areaId='" + areaId + "' ";
		if (thisService.IsFieldExist("roomName", roomName, roomId, hql)) {
			writeJSON(response, jsonBuilder.returnFailureJson("'同一区域已有此房间！'"));
			return;
		}

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		BuildRoominfo perEntity = thisService.get(entity.getUuid());
		// 将entity中不为空的字段动态加入到perEntity中去。
		BeanUtils.copyPropertiesExceptNull(perEntity, entity);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		entity = thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));

	}
	
	@RequestMapping("/allRoomTree")
	public void treelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		String excludes = request.getParameter("excludes");
		List<CommTree> lists = treeService.getCommTree("BUILD_V_ALLROOMTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, excludes);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

}
