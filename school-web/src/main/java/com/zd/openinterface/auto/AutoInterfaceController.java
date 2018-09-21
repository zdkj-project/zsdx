package com.zd.openinterface.auto;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zd.core.util.Base64Util;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.te.TeQrCodeForApp;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-20
 * @time: 17:56
 */
@RestController
@RequestMapping("/autoInterface")
public class AutoInterfaceController {
    private static Logger logger = Logger.getLogger(AutoInterfaceController.class);

    @Resource
    private TrainClasstraineeService classtraineeService;

    /**
     * @param json
     * @return com.zd.school.te.TeQrCodeForApp
     * @description 查询住宿信息
     * @author yz
     * @date 2018/9/21 9:47
     * @method findClassIdTrainClassTrainee
     */
    @RequestMapping(value = "/findcartNoRoomName", produces = "application/json; charset=utf-8")
    public String findcartNoRoomName(@RequestBody String json) {
        TeQrCodeForApp result = new TeQrCodeForApp();
        try {
            Map<String, Object> maps = JSON.parseObject(json);
            String sql = " SELECT  TRAIN_T_CLASSTRAINEE.* ,(SELECT a.CLASS_NAME FROM TRAIN_T_CLASS a where a.CLASS_ID=TRAIN_T_CLASSTRAINEE.CLASS_ID) as class_Name FROM TRAIN_T_CLASSTRAINEE WHERE  (CREATE_TIME BETWEEN '" + maps.get("stratTime") + "' AND '" + maps.get("endTime") + "'  OR UPDATE_TIME BETWEEN '" + maps.get("stratTime") + "'  AND '" + maps.get("endTime") + "' ) AND ISDELETE <> 1 " +
                    "AND CLASS_TRAINEE_ID = (SELECT TOP 1 A.USER_ID FROM CARD_T_USEINFO A WHERE A.UP_CARD_ID='" + maps.get("cardNo") + "' AND ISDELETE = 0 ORDER BY A.CREATE_TIME DESC )";
            List<Map<String, Object>> mapList = classtraineeService.getForValuesToSql(sql);
            if (mapList.isEmpty()) {
                result.setData(new ArrayList<>());
                result.setResult(false);
                result.setMessage("该流水号没有查询到绑定卡号");
                return JSON.toJSONString(result);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("roomName", mapList.get(0).get("ROOM_NAME"));
            map.put("name", mapList.get(0).get("XM"));
            map.put("sex","1".equals(mapList.get(0).get("XBM"))?"男":"女");
            map.put("checkinDate", mapList.get(0).get("CHECKIN_DATE"));
            map.put("checkoutDate", mapList.get(0).get("CHECKOUT_DATE"));
            map.put("classId", mapList.get(0).get("CLASS_ID"));
            map.put("className", mapList.get(0).get("class_Name"));
            map.put("stuNo", mapList.get(0).get("TRAINEE_NUMBER"));
            map.put("workUnit", mapList.get(0).get("WORK_UNIT"));
            map.put("position", mapList.get(0).get("POSITION"));
            String phone = "";
            if (Base64Util.isBase64(String.valueOf(mapList.get(0).get("MOBILE_PHONE")))) {
                phone = Base64Util.decodeData(String.valueOf(mapList.get(0).get("MOBILE_PHONE")));
            }
            map.put("phone", phone);
            List<Object> list = new ArrayList<>();
            list.add(map);
            result.setData(list);
            result.setMessage("查询成功!");
            return JSON.toJSONString(result);
        } catch (Exception e) {
            result.setData(new ArrayList<>());
            result.setResult(false);
            result.setMessage(e.getMessage());
            return JSON.toJSONString(result);
        }
    }

    /**
     * @param json
     * @return java.lang.String
     * @description 通过流水号查询考勤信息
     * @author yz
     * @date 2018/9/21 15:55
     * @method findcartNoRoomName
     */
    @RequestMapping(value = "/findcartNoAttendance", produces = "application/json; charset=utf-8")
    public String findcartNoAttendance(@RequestBody String json) {
        TeQrCodeForApp result = new TeQrCodeForApp();
        try {
            Map<String, Object> maps = JSON.parseObject(json);
            String sql = "   SELECT  TRAIN_T_CLASSSCHEDULE.*,(SELECT a.CLASS_NAME FROM TRAIN_T_CLASS a where a.CLASS_ID=TRAIN_T_CLASSSCHEDULE.CLASS_ID) as class_Name," +
                    "(SELECT a.ITEM_NAME FROM dbo.BASE_T_DICITEM a WHERE a.isDelete=0 and a.DIC_ID=(SELECT b.DIC_ID FROM BASE_T_DIC b WHERE b.DIC_CODE='TEACHTYPE') AND a.ITEM_CODE=(select top 1 c.TEACH_TYPE from TRAIN_T_COURSEINFO c where c.COURSE_ID=TRAIN_T_CLASSSCHEDULE.COURSE_ID)) as teachTypeName" +
                    " FROM TRAIN_T_CLASSSCHEDULE WHERE  (CREATE_TIME BETWEEN  '" + maps.get("stratTime") + "' AND  '" + maps.get("endTime") + "'  OR UPDATE_TIME BETWEEN  '" + maps.get("stratTime") + "'  AND  '" + maps.get("endTime") + "' ) AND ISDELETE <> 1" +
                    " AND CLASS_ID in (SELECT CLASS_ID FROM TRAIN_T_CLASSTRAINEE where class_trainee_id =" +
                    "  ( SELECT TOP 1 A.USER_ID FROM CARD_T_USEINFO A WHERE A.UP_CARD_ID='" + maps.get("cardNo") + "' AND ISDELETE = 0 ORDER BY A.CREATE_TIME DESC))";
            List<Map<String, Object>> mapList = classtraineeService.getForValuesToSql(sql);
            if (mapList.isEmpty()) {
                result.setResult(false);
                result.setData(new ArrayList<>());
                result.setMessage("该流水号没有查询到绑定卡号");
                return JSON.toJSONString(result);
            }
            List<Object> list = new ArrayList<>();
            Map<String, Object> map1;
            for (Map<String, Object> m : mapList) {
                map1 = new HashMap<>();
                Map<String, Object> map2 = m;
                map1.put("courseName", map2.get("COURSE_NAME"));
                map1.put("courseId", map2.get("COURSE_ID"));
                map1.put("classId", map2.get("CLASS_ID"));
                map1.put("className", map2.get("class_Name"));
                map1.put("roomId", map2.get("ROOM_ID"));
                map1.put("mainTeacherName", map2.get("MAIN_TEACHER_NAME"));
                map1.put("mainTeacherId", map2.get("MAIN_TEACHER_ID"));
                map1.put("beginTime", map2.get("BEGIN_TIME"));
                map1.put("endTime", map2.get("END_TIME"));
                map1.put("teachTypeName", map2.get("teachTypeName"));
                map1.put("credits", map2.get("CREDITS"));
                list.add(map1);
            }
            result.setData(list);
            result.setMessage("查询成功!");
            return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            result.setData(new ArrayList<>());
            result.setResult(false);
            result.setMessage(e.getMessage());
            return JSON.toJSONString(result);
        }
    }

    /**
     * @param json
     * @return com.zd.school.te.TeQrCodeForApp
     * @description 查询学分
     * @author yz
     * @date 2018/9/21 9:47
     * @method findClassIdTrainClassTrainee
     */
    @RequestMapping(value = "/findcartNoRredit", produces = "application/json; charset=utf-8")
    public String findcartNoRredit(@RequestBody String json) {
        TeQrCodeForApp result = new TeQrCodeForApp();
        try {
            Map<String, Object> maps = JSON.parseObject(json);
            String sql = " SELECT  TRAIN_T_CLASSTRAINEE.* ,(SELECT a.CLASS_NAME FROM TRAIN_T_CLASS a where a.CLASS_ID=TRAIN_T_CLASSTRAINEE.CLASS_ID) as class_Name FROM TRAIN_T_CLASSTRAINEE WHERE  (CREATE_TIME BETWEEN '" + maps.get("stratTime") + "' AND '" + maps.get("endTime") + "'  OR UPDATE_TIME BETWEEN '" + maps.get("stratTime") + "'  AND '" + maps.get("endTime") + "' ) AND ISDELETE <> 1 " +
                    "AND CLASS_TRAINEE_ID = (SELECT TOP 1 A.USER_ID FROM CARD_T_USEINFO A WHERE A.UP_CARD_ID='" + maps.get("cardNo") + "' AND ISDELETE = 0 ORDER BY A.CREATE_TIME DESC )";
            List<Map<String, Object>> mapList = classtraineeService.getForValuesToSql(sql);
            if (mapList.isEmpty()) {
                result.setMessage("该流水号没有查询到绑定卡号");
                result.setData(new ArrayList<>());
                result.setResult(false);
                return JSON.toJSONString(result);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", mapList.get(0).get("XM"));
            map.put("sex","1".equals(mapList.get(0).get("XBM"))?"男":"女");
            map.put("stuNo", mapList.get(0).get("TRAINEE_NUMBER"));
            map.put("workUnit", mapList.get(0).get("WORK_UNIT"));
            map.put("position", mapList.get(0).get("POSITION"));
            map.put("classId", mapList.get(0).get("CLASS_ID"));
            map.put("className", mapList.get(0).get("class_Name"));
            map.put("totalCredit", null == mapList.get(0).get("TOTAL_CREDIT")?0:Integer.parseInt(mapList.get(0).get("TOTAL_CREDIT").toString()));
            map.put("realRredit", null == mapList.get(0).get("REAL＿CREDIT")?0:Integer.parseInt(mapList.get(0).get("REAL＿CREDIT").toString()));
            map.put("updateTime", mapList.get(0).get("UPDATE_TIME"));
            String phone = "";
            if (Base64Util.isBase64(String.valueOf(mapList.get(0).get("MOBILE_PHONE")))) {
                phone = Base64Util.decodeData(String.valueOf(mapList.get(0).get("MOBILE_PHONE")));
            }
            map.put("phone", phone);
            List<Object> list = new ArrayList<>();
            list.add(map);
            result.setData(list);
            result.setMessage("查询成功!");
            return JSON.toJSONString(result);
        } catch (Exception e) {
            result.setData(new ArrayList<>());
            result.setResult(false);
            result.setMessage(e.getMessage());
            return JSON.toJSONString(result);
        }
    }
}
