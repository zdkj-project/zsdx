
package com.zd.school.oa.notice.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.zd.core.util.DateUtil;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.oa.notice.model.OaNotice;
import com.zd.school.oa.notice.model.OaNoticeauditor;
import com.zd.school.oa.notice.service.OaNoticeService;
import com.zd.school.oa.notice.service.OaNoticeauditorService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: OaNoticeauditorController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 公告审核人(OA_T_NOTICEAUDITOR)实体Controller.
 * date: 2016-12-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/OaNoticeauditor")
public class OaNoticeauditorController extends FrameWorkController<OaNoticeauditor> implements Constant {

	@Resource
	OaNoticeauditorService thisService; // service层接口
	@Resource
	OaNoticeService noticeService;

	/**
	 * list查询 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute OaNoticeauditor entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String strData = ""; // 返回给js的数据
		try {
			SysUser currentUser = getCurrentSysUser();
			String today = DateUtil.formatDate(new Date());
			String hql = "from OaNoticeauditor as o inner join fetch o.oaNotice as n where n.beginDate<='" + today
					+ "' and n.endDate>='" + today + "' and o.userId='" + currentUser.getUuid()
					+ "' and o.auditState=0";
			List<OaNoticeauditor> list = thisService.getQuery(hql);
			String ids = "";
			for (OaNoticeauditor oaNoticeauditor : list) {
				ids += "'" + oaNoticeauditor.getOaNotice().getUuid() + "',";
			}
			ids = StringUtils.trimLast(ids);

			String whereSql = super.whereSql(request);
			String orderSql = super.orderSql(request);
			Integer start = super.start(request);
			Integer limit = super.limit(request);
			String sort = super.sort(request);
			String filter = super.filter(request);
			whereSql = " and uuid in(" + ids + ")";
			QueryResult<OaNotice> qResult = thisService.userlist(start, limit, sort, filter, whereSql, orderSql,
					currentUser);
			strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
			writeJSON(response, strData);// 返回数据
		} catch (Exception e) {
			strData = jsonBuilder.buildObjListToJson(new Long(0), new ArrayList<OaNotice>(), true);// 处理数据
			writeJSON(response, strData);// 返回数据
		}

	}

	/**
	 * 
	 * @Title: 增加新实体信息至数据库 @Description: TODO @param @param OaNoticeauditor
	 *         实体类 @param @param request @param @param response @param @throws
	 *         IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doadd")
	public void doAdd(OaNoticeauditor entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 此处为放在入库前的一些检查的代码，如唯一校验等

		// 获取当前操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			entity = thisService.doAddEntity(entity, currentUser);// 执行增加方法
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
	 * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
	 * OaNoticeauditor @param @param request @param @param
	 * response @param @throws IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping("/doupdate")
	public void doUpdates(OaNoticeauditor entity, HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		try {
			String uuid = request.getParameter("uuid");
			OaNotice oaNotice = noticeService.get(uuid);
			entity = thisService.getByProerties("oaNotice", oaNotice);
			String auditOpinion = request.getParameter("auditOpinion");
			String auditState = request.getParameter("auditState");
			int state = auditState.equals("1") ? 1 : 2;
			entity.setAuditDate(new Date());
			entity.setAuditOpinion(auditOpinion);
			entity.setAuditState(state);

			entity.setUpdateTime(new Date());
			entity.setUpdateUser(currentUser.getUserName());
			entity = thisService.doMerge(entity);
			oaNotice.setIsCheck((state + 1) + "");
			noticeService.doMerge(oaNotice);
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		}

	}
}
