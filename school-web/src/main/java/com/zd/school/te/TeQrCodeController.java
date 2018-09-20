package com.zd.school.te;

import com.alibaba.fastjson.JSON;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.service.PtTermService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-12
 * @time: 16:29
 */
@RestController
@RequestMapping("/teQeCode")
public class TeQrCodeController {

    @Resource
    private TeQrCodeService teQrCodeService;

    @Resource
    PtTermService ptTermService;

    /**
     * @param data json数据
     * @return java.lang.String
     * @description 微信开门接口
     * @author yz
     * @date 2018/9/12 19:12
     * @method openDoor
     */
    @RequestMapping(value = "/openDoor", produces = "application/json; charset=utf-8")
    public String openDoor(@RequestBody String data) {
        Map<String, Object> map;
        Map<String, Object> json = new HashMap<>();
        TeQrCodeForApp teQrCodeForApp = null;
        //开门json参数
        String jsonBean;
        //返回值
        String result;
        try {
            map = JSON.parseObject(data);
            String qrCode = (String) map.get("qrCode");
            String userid = (String) map.get("userid");
            //查询二维码序列表是否存在
            TeQrCode teQrCode = teQrCodeService.getByProerties("qrCode", qrCode);
            if (null == teQrCode) {
                teQrCodeForApp = new TeQrCodeForApp("二维码序不存在", false, null);
            } else {
                //查询是否有权限开门
                Boolean con = teQrCodeService.findQrCode(userid, teQrCode);
                if (con) {
                    //查询设备表
                    PtTerm ptTerm = ptTermService.getByProerties(new String[]{"roomId", "uuid", "isDelete"}, new Object[]{teQrCode.getRoomID(), teQrCode.getTreamID(), 0});
                    if (null == ptTerm) {
                        teQrCodeForApp = new TeQrCodeForApp("不存在此设备", false, null);
                    } else {
                        //用户编号
                        json.put("user_id", userid);
                        //设备序列号
                        json.put("sn", ptTerm.getTermSN());
                        //控制方式（0：关门，1：开门）
                        json.put("mode", 1);
                        //动作成功后延迟时间（秒）
                        json.put("delay", 1);
                        //微信控制开门
                        json.put("type", 2);
                        teQrCodeForApp = teQrCodeService.openDoor(json);
                    }
                } else {
                    teQrCodeForApp = new TeQrCodeForApp("没有开门权限", false, null);
                }
            }
        } catch (Exception e) {
            teQrCodeForApp = new TeQrCodeForApp(e.getCause().toString(), false, null);
        } finally {
            result = JSON.toJSONString(teQrCodeForApp);
            return result;
        }
    }


}
