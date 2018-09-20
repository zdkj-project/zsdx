package com.zd.school.weixin;

import com.zd.core.controller.core.BaseController;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.model.TrainTeacherOrder;
import com.zd.school.jw.train.model.TrainTeacherOrderDesc;
import com.zd.school.jw.train.service.TrainTeacherOrderDescService;
import com.zd.school.jw.train.service.TrainTeacherOrderService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 微信订餐页面
 * @author: yz
 * @date: 2018-09-18
 * @time: 14:49
 */

@Controller
@RequestMapping("/app/Reservation")
public class ReservationWeiXin extends BaseController<TrainTeacherOrderDesc> {

    private static Logger logger = Logger.getLogger(ReservationWeiXin.class);
    /**
     * Json工具类
     */
    protected static JsonBuilder jsonBuilder;

    static {
        jsonBuilder = JsonBuilder.getInstance();
    }

    @Resource
    TrainTeacherOrderDescService teacherOrderDescService;


    @Resource
    TrainTeacherOrderService teacherOrderService;

    /**
     * @param userid
     * @param request
     * @param response
     * @return org.springframework.web.servlet.ModelAndView
     * @description 进入订餐页面
     * @author yz
     * @date 2018/9/18 18:42
     * @method index
     */
    @RequestMapping(value = "/food", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView index(String userid, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userid)) {
            return new ModelAndView("static/core/app/miniweb/jsp/hint", "", null);
        }
        request.setAttribute("userid", userid);
        return new ModelAndView("static/core/app/miniweb/reservation/food", "", null);
    }

    /**
     * @param response
     * @return void
     * @description 订餐说明
     * @author yz
     * @date 2018/9/18 18:42
     * @method orderDesc
     */
    @RequestMapping(value = "/orderDesc", method = {RequestMethod.POST, RequestMethod.GET})
    public void orderDesc(HttpServletResponse response) throws IOException {
        String sql = " SELECT TOP 1 * FROM TRAIN_T_TEACHERORDER_DESC WHERE ISDELETE = 0 ";
        List<TrainTeacherOrderDesc> list = teacherOrderDescService.getQuerySql(sql);
        String strData;
        strData = jsonBuilder.toJson(list.get(0));
        writeJSON(response, strData);
    }

    /**
     * @param request
     * @param response
     * @return void
     * @description
     * @author yz
     * @date 2018/9/18 18:43
     * @method getOrderDesc 订餐信息
     */
    @RequestMapping("/getOrderList")
    public void getOrderDesc(String userid, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (StringUtils.isEmpty(userid)) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        // 创建 Calendar 对象
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(calendar1.getTime());
        calendar2.add(Calendar.DAY_OF_MONTH, 14);
        String hql = "from TrainTeacherOrder where userId=? and dinnerDate>=? and dinnerDate<? and isDelete = 0  order by dinnerDate asc";
        List<TrainTeacherOrder> list = teacherOrderService.getForValues(hql, userid, calendar1.getTime(), calendar2.getTime());
        int len = list.size();

        List<TrainTeacherOrder> returnlist = new ArrayList<>();
        returnlist.addAll(list);
        if (len < 14) {
            TrainTeacherOrder temp = null;

            boolean isExist = false;
            int day = 0;
            int day2 = 0;
            for (int i = 0; i < 14; i++) {
                isExist = false;
                day = calendar1.get(Calendar.DAY_OF_YEAR);
                for (int j = 0; j < len; j++) {
                    calendar2.setTime(list.get(j).getDinnerDate());
                    day2 = calendar2.get(Calendar.DAY_OF_YEAR);
                    if (day == day2) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist == false) {
                    temp = new TrainTeacherOrder();
                    temp.setDinnerDate(calendar1.getTime());
                    temp.setDinnerGroup((short) 0);
                    temp.setUserId(userid);
                    temp.setUuid(null);
                    returnlist.add(i, temp);
                }
                calendar1.set(Calendar.DAY_OF_MONTH, calendar1.get(Calendar.DAY_OF_MONTH) + 1);
            }
        }

        String strData = jsonBuilder.buildObjListToJson((long) returnlist.size(), returnlist, true);
        writeJSON(response, strData);
    }


    /**
     * @param userId   订餐人员名单
     * @param dateArr  订餐时间
     * @param response
     * @return void
     * @description 微信订餐
     * @author yz
     * @date 2018/9/20 10:14
     * @method addOrder
     */
    @RequestMapping("/addOrder")
    public void addOrder(String userId, String[] dateArr, HttpServletResponse response) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前操作用户
        String userCh = "微信端";
        //当前时间
        Date date;
        //订餐时间
        Date dinnerDate;
        try {
            for (int i = 0; i < dateArr.length; i++) {
                date = new Date();
                dinnerDate = sdf.parse(dateArr[i]);
                // 创建 Calendar 对象
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(dinnerDate);

                if (calendar1.compareTo(calendar2) == 1) {
                    writeJSON(response, 0, "当前时间不允许再修改订餐");
                    return;
                } else {
                    calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) - 1);
                    if (calendar2.compareTo(calendar1) != 1) {
                        calendar2.set(Calendar.HOUR_OF_DAY, 19);
                        calendar2.set(Calendar.MINUTE, 0);
                        calendar2.set(Calendar.SECOND, 0);
                        calendar2.set(Calendar.MILLISECOND, 0);
                        if (calendar2.compareTo(calendar1) != 1) {
                            writeJSON(response, 0, "超过19点钟，就不可再预定明天的就餐！");
                            return;
                        }
                    }
                }
                // 当前节点
                TrainTeacherOrder saveEntity = new TrainTeacherOrder();
                List<String> excludeList = new ArrayList<>();
                excludeList.add("uuid");
                saveEntity.setUserId(userId);
                // 增加时要设置创建人
                saveEntity.setCreateUser(userCh);
                saveEntity.setDinnerDate(dinnerDate);
                saveEntity.setCreateTime(date);
                saveEntity.setDinnerGroup((short) 1);
                // 持久化到数据库
                teacherOrderService.doMerge(saveEntity);
            }
            // 返回实体到前端界面
            writeJSON(response, 200, "订餐成功");

        } catch (Exception e) {
            logger.error("订餐失败" + e.getMessage());
            // 返回实体到前端界面
            writeJSON(response, 0, "订餐失败,请联系管理员");
        }
    }

    /**
     * @param order    订单号
     * @param response
     * @return void
     * @description 取消订单
     * @author yz
     * @date 2018/9/20 10:16
     * @method doCancel
     */
    @RequestMapping("/doCancel")
    public void doCancel(String[] order, HttpServletResponse response)
            throws IOException {

        try {
            for (int i = 0; i < order.length; i++) {
                // 先拿到已持久化的实体
                TrainTeacherOrder perEntity = teacherOrderService.get(order[i]);
                if (null == perEntity) {
                    writeJSON(response, 0, "未订餐不能取消！");
                }
                Date date = new Date();
                // 创建 Calendar 对象
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(perEntity.getDinnerDate());
                if (calendar1.compareTo(calendar2) == 1) {
                    writeJSON(response, 0, "当前时间不允许再取消订餐！");
                    return;
                } else {
                    calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) - 1);
                    if (calendar2.compareTo(calendar1) != 1) {
                        calendar2.set(Calendar.HOUR_OF_DAY, 19);
                        calendar2.set(Calendar.MINUTE, 0);
                        calendar2.set(Calendar.SECOND, 0);
                        calendar2.set(Calendar.MILLISECOND, 0);
                        if (calendar2.compareTo(calendar1) != 1) {
                            writeJSON(response, 0, "超过19点钟，就不可再取消明天的就餐信息！");
                            return;
                        }
                    }
                }
                teacherOrderService.doDelete(perEntity);
            }
            // 返回实体到前端界面
            writeJSON(response, 200, "取消订餐成功");
        } catch (Exception e) {
            logger.error("取消订餐失败" + e.getMessage());
            // 返回实体到前端界面
            writeJSON(response, 0, "取消订餐失败，请联系管理员！");
        }
    }

    /**
     * @param userid
     * @param remark
     * @param response
     * @return void
     * @description 添加备注
     * @author yz
     * @date 2018/9/20 10:16
     * @method doUpdate
     */
    @RequestMapping("/doUpdate")
    public void doUpdate(String userid, String remark, HttpServletResponse response)
            throws IOException {

        // 获取当前的操作用户
        String userCh = "微信端";

        try {
            // 先拿到已持久化的实体
            TrainTeacherOrder perEntity = teacherOrderService.get(userid);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            // 创建 Calendar 对象
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(perEntity.getDinnerDate());
            if (calendar1.compareTo(calendar2) == 1) {
                writeJSON(response, 0, "当前时间不允许再修改订餐！");
                return;
            } else {
                calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) - 1);
                if (calendar2.compareTo(calendar1) != 1) {
                    calendar2.set(Calendar.HOUR_OF_DAY, 19);
                    calendar2.set(Calendar.MINUTE, 0);
                    calendar2.set(Calendar.SECOND, 0);
                    calendar2.set(Calendar.MILLISECOND, 0);
                    if (calendar2.compareTo(calendar1) != 1) {
                        writeJSON(response, 0, "超过19点钟，就不可再修改明天的就餐！");
                        return;
                    }
                }
            }
            perEntity.setRemark(remark);
            // 设置修改时间
            perEntity.setUpdateTime(new Date());
            // 设置修改人的中文名
            perEntity.setUpdateUser(userCh);
            // 执行修改方法
            teacherOrderService.doMerge(perEntity);
            writeJSON(response, 200, "备注成功");
        } catch (Exception e) {
            // 返回实体到前端界面
            writeJSON(response, 0, "请求失败，请联系管理员！");
        }
    }

}
