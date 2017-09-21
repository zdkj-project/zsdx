package com.zd.school.app;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.cashier.model.CashDishes;
import com.zd.school.cashier.service.CashDishesService;

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
@RequestMapping("/CashDishesType")
public class CashDishesListController extends FrameWorkController<CashDishes> implements Constant {

    @Resource
    CashDishesService thisService; // service层接口
    
    @Resource
	BaseDicitemService dicitemService;// 字典项service层接口
    
    @RequestMapping("/disheslist")
    public void disheslist( HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	
		List<CashDishes> dishesList = new ArrayList<CashDishes>();
    	String hql = "from  CashDishes WHERE isDelete=0";
    	dishesList =thisService.doQuery(hql);
    	
    	String mapKey = null;
		String[] propValue = {"DISHESTYPE"};
		Map<String, String> mapDicItem = new HashMap<>();
		List<String> keylist = new ArrayList<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
				mapKey = baseDicitem.getItemCode();
				keylist.add(mapKey);
				mapDicItem.put(mapKey, baseDicitem.getItemName());
			}
		List<Map<String,String>> dishesdleList = new ArrayList<Map<String,String>>();
		Map<String,String> dishesMap ;
		for(int i=0;i<dishesList.size();i++){
			dishesMap = new HashMap<>();
			dishesMap.put("dishesType", mapDicItem.get( String.valueOf(dishesList.get(i).getDishesType())));
			dishesMap.put("dishesName", dishesList.get(i).getDishesName());
			dishesMap.put("dishesPrice", String.valueOf(dishesList.get(i).getDishesPrice()));
			dishesdleList.add(dishesMap);
		}
		writeJSON(response, jsonBuilder.toJson(dishesdleList));
    }
    
}
