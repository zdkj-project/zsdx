
package com.zd.school.build.allot.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

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
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.model.JwClassDormAllot;
import com.zd.school.build.allot.service.DormStudentdormService;
import com.zd.school.build.allot.service.JwClassDormAllotService;
import com.zd.school.build.define.model.BuildDormDefine;
import com.zd.school.build.define.service.BuildDormDefineService;
import com.zd.school.jw.eduresources.model.JwClassteacher;
import com.zd.school.jw.eduresources.service.JwClassteacherService;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwClassdormController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 班级分配宿舍(JW_T_CLASSDORM)实体Controller. date:
 * 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwClassDormAllot")
public class JwClassDormAllotController extends FrameWorkController<JwClassDormAllot> implements Constant {

	@Resource
	JwClassDormAllotService thisService; // service层接口

	@Resource
	CommTreeService treeService; // 生成班级树接口

	@Resource
	BuildDormDefineService dormService;// 宿舍定义

	@Resource
	JwClassteacherService classTeacherService;// 班主任

	@Resource
	PushInfoService pushService; // 推送

	@Resource
	DormStudentdormService dormStudentDormService; // 学生分配宿舍

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })

	public void list(@ModelAttribute JwClassDormAllot entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		QueryResult<JwClassDormAllot> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
				super.sort(request), super.filter(request), true);

		strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 生成树
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/classtreelist")
	public void getGradeTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_V_GRADECLASSTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param JwClassdorm
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(JwClassDormAllot entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String[] strId = entity.getDormId().split(",");// 获取宿舍id
		String[] isMixed = entity.getIsmixed().split(",");// 混班宿舍标识
		for (int i = 0; i < strId.length; i++) {
			boolean cz = thisService.IsFieldExist("dormId", strId[i], "-1",
					"isdelete=0 and claiId=" + "'" + entity.getClaiId() + "'");
			if (cz) {
				writeJSON(response, jsonBuilder.returnFailureJson("'请勿重复添加。'"));
				return;
			}
			BuildDormDefine jwTDormDefin = dormService.get(strId[i]);
			// 获取当前操作用户
			String userCh = "超级管理员";
			SysUser currentUser = getCurrentSysUser();
			if (currentUser != null)
				userCh = currentUser.getXm();
			// 生成默认的orderindex
			// 如果界面有了排序号的输入，则不需要取默认的了
			Integer orderIndex = thisService.getDefaultOrderIndex(entity);
			JwClassDormAllot perEntity = new JwClassDormAllot();
			BeanUtils.copyPropertiesExceptNull(entity, perEntity);
			entity.setOrderIndex(orderIndex);// 排序
			entity.setDormId(strId[i]);// 设置宿舍id
			if (isMixed[i].equals("1")) {
				entity.setIsmixed(isMixed[i]);
				jwTDormDefin.setRoomStatus("0");// 将宿舍状态设置为未分配
			} else {
				entity.setIsmixed(isMixed[i]);
				jwTDormDefin.setRoomStatus("1");// 将宿舍状态设置为已分配
			}
			jwTDormDefin.setIsMixed(isMixed[i]);

			// 增加时要设置创建人
			entity.setCreateUser(userCh); // 创建人
			// 持久化到数据库
			thisService.doMerge(entity);
			dormService.doMerge(jwTDormDefin);// 修改
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("'分配成功。'"));
	}

	/**
	 * doDelete @Title:局部取消分配 ---> 逻辑删除指定的数据 @Description: TODO @param @param
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
			BuildDormDefine defin = null;
			JwClassDormAllot jwTClassdorm = null;
			for (int j = 0; j < ids.length; j++) {
				jwTClassdorm = thisService.get(ids[j]);
				boolean flag = dormStudentDormService.IsFieldExist("cdormId", jwTClassdorm.getUuid(), "-1",
						"isdelete=0");
				if (flag) {
					++count;
				}
				if (count == 0) {
					defin = dormService.get(jwTClassdorm.getDormId());
					defin.setRoomStatus("0"); // 设置成未分配
					dormService.doMerge(defin); // 持久化
					jwTClassdorm.setIsDelete(1); // 设置删除状态
					thisService.doMerge(jwTClassdorm); // 持久化
					++fs;
				}
				count = 0;
			}
			if (fs > 0) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'已删除没有学生的宿舍，有学生的宿舍未能删除。'"));
			} else {
				writeJSON(response, jsonBuilder.returnSuccessJson("'宿舍都已分配给学生，不允许删除。'"));
			}

		}
	}

	/**
	 * 全部取消分配
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dodeletes")
	public void doDeletes(JwClassDormAllot entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String hql = "from " + entity.getClass().getSimpleName() + " where 1=1 and isdelete=0";
		List<JwClassDormAllot> list = thisService.getQuery(hql);// 将班级下的宿舍全部查询出来
		boolean flag = false;
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				BuildDormDefine dormDefin = null;
				List<DormStudentDorm> lists = dormStudentDormService.queryByProerties("cdormId", list.get(i).getUuid());
				if (lists != null)
					for (int j = 0; j < lists.size(); j++) {
						DormStudentDorm dormTStudentdorm = null;
						if (lists.get(j).getIsDelete() == 0) {
							dormTStudentdorm = lists.get(j);
							dormTStudentdorm.setIsDelete(1);
							dormStudentDormService.doMerge(dormTStudentdorm);
						}
					}
				entity = list.get(i);
				entity.setIsDelete(1);
				entity.setIsmixed("0");
				thisService.doMerge(entity);
				dormDefin = dormService.get(list.get(i).getDormId());
				dormDefin.setRoomStatus("0");
				dormDefin.setIsMixed("0");
				dormService.doMerge(dormDefin);
				flag = true;
			}
		if (flag) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'成功。'"));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("'未能执行任何操作。'"));
			return;
		}
	}

	/**
	 * 推送消息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/dormaTs")
	public void dormaTs(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] str = { "claiId", "isDelete" };
		Object[] str2 = { id, 0 };
		List<JwClassDormAllot> newLists = thisService.queryByProerties(str, str2);
		PushInfo pushInfo = new PushInfo();
		BuildDormDefine jwTDormDefin = null;
		StringBuffer roomName = new StringBuffer();
		for (JwClassDormAllot jwTClassdorm : newLists) {
			jwTDormDefin = dormService.get(jwTClassdorm.getDormId());
			roomName.append(jwTDormDefin.getRoomName() + ",");
		}
		List<JwClassteacher> list = classTeacherService.queryByProerties(str, str2);
		if (list.size() <= 0) {
			writeJSON(response, jsonBuilder.returnFailureJson("'该班级还没有分配班主任，无法推送。'"));
			return;
		}
		for (JwClassteacher jwTClassteacher : list) {
			pushInfo.setEmplName(jwTClassteacher.getXm());
			pushInfo.setEmplNo(jwTClassteacher.getGh());
			pushInfo.setRegTime(new Date());
			pushInfo.setEventType("宿舍信息");
			pushInfo.setPushStatus(0);
			pushInfo.setPushWay(1);
			pushInfo.setRegStatus(
					pushInfo.getEmplName() + "您好，你所在的班级已分配宿舍如下：" + roomName.substring(0, roomName.length() - 1));
			pushService.doMerge(pushInfo);
		}
		writeJSON(response, jsonBuilder.returnSuccessJson("'推送信息成功。'"));
	}
}
