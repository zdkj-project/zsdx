package com.zd.school.te;

import com.zd.core.service.BaseService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-12
 * @time: 16:25
 */
public interface TeQrCodeService extends BaseService<TeQrCode> {
    /**
     * @param userid 学生编号
     * @param teQrCode 二维码序列号
     * @return com.zd.school.te.TeQrCode
     * @description
     * @author yz
     * @date 2018/9/12 18:54
     * @method findQrCode
     */
    Boolean findQrCode(String userid, TeQrCode teQrCode);

    /**
     * @description 远程开门
     * @author yz
     * @date 2018/9/14 11:53
     * @method  openDoor
     * @param map
     * @return com.zd.school.te.TrainClassForApp
     */
    TeQrCodeForApp openDoor(Map<String,Object> map);
}
