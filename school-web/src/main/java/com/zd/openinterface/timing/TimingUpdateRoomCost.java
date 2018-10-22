package com.zd.openinterface.timing;

import com.alibaba.fastjson.JSON;
import com.zd.school.jw.train.model.RoomCost;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.service.RoomCostService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-10-22
 * @time: 9:25
 */
public class TimingUpdateRoomCost {

    @Resource
    private RoomCostService roomCostService;


    /**
     * @param
     * @return void
     * @description 定时更新学员住宿情况
     * @author yz
     * @date 2018/10/22 11:06
     * @method timingUpdate
     */
    public void timingUpdate() {
        roomCostService.timingUpdate();
    }
}
