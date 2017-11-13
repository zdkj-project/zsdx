package com.zd.school.app;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.security.SyncJobQuartz;
import com.zd.core.util.ModelUtil;
import com.zd.school.cashier.model.CashDinneritem;
import com.zd.school.cashier.model.CashDishes;
import com.zd.school.cashier.model.CashExpensedetail;
import com.zd.school.cashier.model.CashExpenseserial;
import com.zd.school.cashier.service.CashDinneritemService;
import com.zd.school.cashier.service.CashDishesService;
import com.zd.school.cashier.service.CashExpenseserialService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author luoyibo
 * @version 0.1
 * @projectName zsdx_githubTest
 * Description:
 * date: 2017-09-25 15:48
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/cashReset")
public class CashResetController extends FrameWorkController<CashExpenseserial> implements Constant {

	private static Logger logger = Logger.getLogger(CashResetController.class);
	
    @Resource
    private CashDishesService thisService; // service层接口

    @Resource
    private CashExpenseserialService billService;

    @Resource
    private  CashDinneritemService cashDinneritemService;

    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService roleService;

    /**
     * 收银操作员登录
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/casherLogin")
    public void casherLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String strData = "";
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");

        String userPwdEncode = new Sha256Hash(userPwd).toHex();
        String[] propName = {"userName", "userPwd", "isDelete", "state"};
        Object[] propValue = {userName, userPwdEncode, 0, "0"};

        SysUser user = userService.getByProerties(propName, propValue);
        if (ModelUtil.isNotNull(user)) {
            Set<SysRole> userRole = user.getSysRoles();
            SysRole casherRole = roleService.getByProerties("roleCode", "CASHER");
            if (ModelUtil.isNotNull(casherRole)) {
                if (userRole.contains(casherRole)) {
                    strData = jsonBuilder.returnSuccessJson("\"" + user.getXm() + "\"");
                } else {
                    strData = jsonBuilder.returnFailureJson("\"当前账号不是收银员，请和管理员联系\"");
                }
            } else {
                strData = jsonBuilder.returnFailureJson("\"没有配置收银员，请和管理员联系\"");
            }

        } else {

            strData = jsonBuilder.returnFailureJson("\"密码错误或账号已锁定，请和管理员联系\"");
        }
        writeJSON(response, strData);
    }

    /**
     * 获取可选择的快餐
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/getCashMealList")
    public void getCashMealList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String strData = "";
        Map<String, String> mapOrderBy = new HashMap<String, String>();
        mapOrderBy.put("mealType", "asc");
        mapOrderBy.put("mealPrice", "asc");
        List<CashDinneritem> dishesList = cashDinneritemService.queryByProerties("isDelete", 0, mapOrderBy);

        List<Map<String, Object>> dishesdleList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dishesMap = null;
        for (int i = 0; i < dishesList.size(); i++) {
            dishesMap = new HashMap<>();
            dishesMap.put("mealType", dishesList.get(i).getMealType());
            dishesMap.put("mealName", dishesList.get(i).getMealName().toString());
            dishesMap.put("mealPrice", dishesList.get(i).getMealPrice());
            dishesdleList.add(dishesMap);
        }
        strData = jsonBuilder.returnSuccessJson(jsonBuilder.toJson(dishesdleList));

        writeJSON(response, strData);
    }

    /**
     * 获取可选择的菜品
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/getDishesList")
    public void getDishesList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String strData = "";
        Map<String, String> mapOrderBy = new HashMap<String, String>();
        mapOrderBy.put("dishesType", "asc");
        List<CashDishes> dishesList = thisService.queryByProerties("isDelete", 0, mapOrderBy);

        List<Map<String, Object>> dishesdleList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dishesMap = null;
        for (int i = 0; i < dishesList.size(); i++) {
            dishesMap = new HashMap<>();
            dishesMap.put("dishesType", dishesList.get(i).getDishesType());
            dishesMap.put("dishesName", dishesList.get(i).getDishesName().toString());
            dishesMap.put("dishesPrice", dishesList.get(i).getDishesPrice());
            dishesdleList.add(dishesMap);
        }
        strData = jsonBuilder.returnSuccessJson(jsonBuilder.toJson(dishesdleList));

        writeJSON(response, strData);
    }

    /**
     * 将消费单据入库
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/doAddConsumeBill")
    public void doAddConsumeBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	
        String consumeBill = request.getParameter("consumeBill");
        String billDetail = request.getParameter("billDetail");

    	logger.info("收银数据入库-->参数-->consumeBill："+consumeBill + "; billDetail："+billDetail);
    	
        String returnData = null;
        try {
            CashExpenseserial billEntity = (CashExpenseserial) jsonBuilder.fromJson(consumeBill, CashExpenseserial.class);
            List<CashExpensedetail> listBillDetail = (List<CashExpensedetail>) jsonBuilder.fromJsonArray(billDetail, CashExpensedetail.class);
            boolean b = billService.doSaveConsumeBill(billEntity, listBillDetail);
            if (b){
                returnData = jsonBuilder.returnSuccessJson("\"单据入库成功\"");
                logger.info("入库成功");
            }else{
                returnData = jsonBuilder.returnFailureJson("\"单据入库失败\"");
                logger.info("入库失败");
            }
            writeAppJSON(response, returnData);
        } catch (Exception e) {
        	logger.info("入库失败:"+e.getStackTrace());
            returnData = jsonBuilder.returnFailureJson(e.getMessage());
            writeAppJSON(response, returnData);
        }
    }
}
