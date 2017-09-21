
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
import com.zd.school.cashier.model.CashDinneritem;
import com.zd.school.cashier.model.CashDinnertime;
import com.zd.school.cashier.service.CashDinneritemService;
import com.zd.school.cashier.service.CashDinnertimeService;

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
@RequestMapping("/CashDishesTime")
public class CashDishesTimeController extends FrameWorkController<CashDinneritem> implements Constant {

    @Resource
    CashDinneritemService thisService; // service层接口
    
    @Resource
	BaseDicitemService dicitemService;// 字典项service层接口
    
    @Resource
    CashDinnertimeService cashDinnertimeService;

    
    @RequestMapping("/typelist")
    public void typeList( HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	
    	String mealtime = "1900-01-01 "+request.getParameter("mealtime");
    	
		List<CashDinnertime> mealTimeList = new ArrayList<CashDinnertime>();
    	String hql = "from  CashDinnertime WHERE isDelete=0 and beginTime<='"+mealtime+"' and endTime>='"+mealtime+"'";
    	mealTimeList =cashDinnertimeService.doQuery(hql);
    	short mealtype = mealTimeList.get(0).getMealType();
    	
    	String mapKey = null;
		String[] propValue = { "MEALTYPE"};
		Map<String, String> mapDicItem = new HashMap<>();
		List<String> keylist = new ArrayList<>();
		List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
		for (BaseDicitem baseDicitem : listDicItem) {
				mapKey = baseDicitem.getItemCode();
				keylist.add(mapKey);
				mapDicItem.put(mapKey, baseDicitem.getItemName());
			}
		String strData = "";
		List<CashDinneritem> mealList = new ArrayList<CashDinneritem>();
    		hql = "from  CashDinneritem WHERE isDelete=0 and mealType='"+mealtype+"'";
    		mealList =thisService.doQuery(hql);
    		for(int j=0;j<mealList.size();j++) {
    			if(strData!="") {
    				strData = strData+","+"{"+'"'+"mealName"+'"'+":"+'"'+mealList.get(j).getMealName()+'"'+","+'"'+"mealPrice"+'"'+":"+'"'+mealList.get(j).getMealPrice()+'"'+"}";
    			}else {
    				strData = "{"+'"'+"mealName"+'"'+":"+'"'+mealList.get(j).getMealName()+'"'+","+'"'+"mealPrice"+'"'+":"+'"'+mealList.get(j).getMealPrice()+'"'+"}";
    			}
    	}
    		strData =  "{"+'"'+mapDicItem.get(mealtype+"")+'"'+":["+strData+"]}";
    		writeJSON(response, strData);
    }
    
    
}
