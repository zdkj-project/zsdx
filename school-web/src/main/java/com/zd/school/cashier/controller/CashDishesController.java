
package com.zd.school.cashier.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.cashier.model.CashDishes ;
import com.zd.school.cashier.dao.CashDishesDao ;
import com.zd.school.cashier.service.CashDishesService ;
import com.zd.school.jw.train.model.TrainEvalIndicator;
import com.zd.school.jw.train.model.TrainIndicatorStand;

/**
 * 
 * ClassName: CashDishesController
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 菜品管理(CASH_T_DISHES)实体Controller.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/CashDishes")
public class CashDishesController extends FrameWorkController<CashDishes> implements Constant {

    @Resource
    CashDishesService thisService; // service层接口
    
    @Resource
	BaseDicitemService dicitemService;// 字典项service层接口

    /**
      * @Title: list
      * @Description: 查询数据列表
      * @param entity 实体类
      * @param request
      * @param response
      * @throws IOException    设定参数
      * @return void    返回类型
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute CashDishes entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<CashDishes> qResult = thisService.list(start, limit, sort, filter,true);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
      * @Title: doadd
      * @Description: 增加新实体信息至数据库
      * @param CashDishes 实体类
      * @param request
      * @param response
      * @return void    返回类型
      * @throws IOException    抛出异常
     */
    @RequestMapping("/doadd")
    public void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String cashDishesStand = request.getParameter("cashDishesStand");

        List<CashDishes> stand = (List<CashDishes>) jsonBuilder.fromJsonArray(cashDishesStand, CashDishes.class);
        int standCount = stand.size();
        //获取当前操作用户
        SysUser currentUser = getCurrentSysUser();
        if(standCount!=0) {
        		try {
        			thisService.doAddEntity(stand,currentUser);// 执行增加方法
                        writeJSON(response, jsonBuilder.returnSuccessJson("'数据添加成功'"));
                } catch (Exception e) {
                    writeJSON(response, jsonBuilder.returnFailureJson("'数据增加失败,详情见错误日志'"));
        	}
        	
        }else {
        	writeJSON(response, jsonBuilder.returnFailureJson("'请添加菜品'"));
        }
        
    }

    /**
      * 
      * @Title: doDelete
      * @Description: 逻辑删除指定的数据
      * @param request
      * @param response
      * @return void    返回类型
      * @throws IOException  抛出异常
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
     * @Title: doUpdate
     * @Description: 编辑指定记录
     * @param CashDishes
     * @param request
     * @param response
     * @return void    返回类型
     * @throws IOException  抛出异常
    */
    @RequestMapping("/doupdate")
    public void doUpdates( HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		//入库前检查代码
    	String cashDishesStand = request.getParameter("cashDishesStand");
        List<CashDishes> stand = (List<CashDishes>) jsonBuilder.fromJsonArray(cashDishesStand, CashDishes.class);
        int standCount = stand.size();
        CashDishes entity = stand.get(0);
		//获取当前的操作用户
		SysUser currentUser = getCurrentSysUser();
		if(standCount!=0) {
		try {
			entity = thisService.doUpdateEntity(entity, currentUser);// 执行修改方法
			if (ModelUtil.isNotNull(entity))
				writeJSON(response, jsonBuilder.returnSuccessJson("'数据更新成功'"));
			else
				writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		} catch (Exception e) {
			writeJSON(response, jsonBuilder.returnFailureJson("'数据修改失败,详情见错误日志'"));
		}
		}else {
        	writeJSON(response, jsonBuilder.returnFailureJson("'请选择菜品'"));
        }
    }
}
