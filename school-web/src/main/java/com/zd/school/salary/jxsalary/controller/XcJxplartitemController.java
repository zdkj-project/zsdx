
package com.zd.school.salary.jxsalary.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.jxsalary.model.XcJxplartitem ;
import com.zd.school.salary.jxsalary.dao.XcJxplartitemDao ;
import com.zd.school.salary.jxsalary.service.XcJxplartitemService ;
import com.zd.school.salary.salary.model.XcSalaryitem;
import com.zd.school.salary.salary.model.XcSalaryplatitem;
import com.zd.school.salary.salary.service.XcSalaryitemService;

/**
 * 
 * ClassName: XcJxplartitemController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效套账工资项目表(XC_T_JXPLARTITEM)实体Controller.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/Xcjxplatitem")
public class XcJxplartitemController extends FrameWorkController<XcJxplartitem> implements Constant {

    @Resource
    XcJxplartitemService thisService; // service层接口
    
    @Resource
	XcSalaryitemService salaryitemService;


    /**
      * list查询
      * @Title: list
      * @Description: TODO
      * @param @param entity 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute XcJxplartitem entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<XcJxplartitem> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
      * @Title: 增加新实体信息至数据库
      * @Description: TODO
      * @param @param XcJxplartitem 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(XcJxplartitem entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
		//此处为放在入库前的一些检查的代码，如唯一校验等
		
		//获取当前操作用户
		String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

	XcJxplartitem perEntity = new XcJxplartitem();
		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
		// 生成默认的orderindex
		//如果界面有了排序号的输入，则不需要取默认的了
        //Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		//entity.setOrderIndex(orderIndex);//排序
		
		//增加时要设置创建人
        entity.setCreateUser(userCh); //创建人
        		
		//持久化到数据库
		entity = thisService.doMerge(entity);
		
		//返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
      * doDelete
      * @Title: 逻辑删除指定的数据
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
        	String[] ids = delIds.split(",");
			int flag = thisService.deleteXcJxplartitem(ids);
			if (flag==1) {
				writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
			} else if (flag==-2) {
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败,请至少保留一项工资项'"));
			}else{
				writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
			}
        }
    }

    /**
      * doRestore还原删除的记录
      * @Title: doRestore
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }

    /**
     * doUpdate编辑记录
     * @Title: doUpdate
     * @Description: TODO
     * @param @param XcJxplartitem
     * @param @param request
     * @param @param response
     * @param @throws IOException    设定参数
     * @return void    返回类型
     * @throws
    */
    @RequestMapping("/doupdate")
    public void doUpdates(XcJxplartitem entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		
		//获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
			
			
        //先拿到已持久化的实体
		//entity.getSchoolId()要自己修改成对应的获取主键的方法
        XcJxplartitem perEntity = thisService.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity,entity);
       
        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    @RequestMapping(value = "/getNotAddXcSalaryitemlist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void getNotAddXcSalaryitemlist(@ModelAttribute XcJxplartitem entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
		QueryResult<XcSalaryitem> qResult = salaryitemService.list(start, limit, sort, filter, whereSql, orderSql,
				currentUser);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping(value = "/getAddedXcSalaryitemlist", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<XcSalaryitem> getAddedXcSalaryitemlist(HttpServletRequest req) {
		String jxplartId = req.getParameter("jxplartId");
		String hql = "from XcSalaryitem where isDelete=0 ";
		hql += " and uuid in(select salaryitemId from XcJxplartitem where jxplartId='" + jxplartId + "')";
		hql += " order by orderIndex";
		List<XcSalaryitem> list = salaryitemService.getQuery(hql);// 执行查询方法
		List<XcJxplartitem> list2=thisService.getQuery("from XcJxplartitem where jxplartId='" + jxplartId + "'");
		for (XcSalaryitem xcSalaryitem : list) {
			for (XcJxplartitem xcSalaryplatitem : list2) {
				if (xcSalaryitem.getUuid().equals(xcSalaryplatitem.getSalaryitemId())) {
					xcSalaryitem.setOrderIndex(xcSalaryplatitem.getOrderIndex());
				}
			}
		}
		Collections.sort(list, new Comparator() {
			 public int compare(Object a, Object b) {
			  int one = ((XcSalaryitem)a).getOrderIndex();
			  int two = ((XcSalaryitem)b).getOrderIndex();
			  return one- two ;
			 }
		});
		return list;
	}
	
	@RequestMapping("/doBatchAdd")
	public void doBatchAdd(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		String idstr = request.getParameter("ids");
		String jxplartId = request.getParameter("jxplartId");
		
		
		boolean flag = thisService.doBatchAdd(idstr, jxplartId, userCh);
		if (flag) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'保存成功'"));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("'保存失败'"));
		}

	}
	
	@RequestMapping("/doBatchUpdates")
	public void doBatchUpdates(HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {

		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		String idstr = request.getParameter("ids");
		String[] ids = idstr.split(",");
		boolean flag = thisService.doBatchUpdate(ids, userCh);

		if (flag) {
			writeJSON(response, jsonBuilder.returnSuccessJson("'修改成功'"));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson("'修改失败'"));
		}

	}
}
