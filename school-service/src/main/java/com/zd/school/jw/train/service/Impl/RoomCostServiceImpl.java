
package com.zd.school.jw.train.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.train.dao.RoomCostDao;
import com.zd.school.jw.train.dao.RoomTypeListDao;
import com.zd.school.jw.train.model.RoomCost;
import com.zd.school.jw.train.model.RoomTypeList;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.service.RoomCostService;
import com.zd.school.jw.train.service.RoomTypeListService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TrainClassServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 培训开班信息(TRAIN_T_CLASS)实体Service接口实现类. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class RoomCostServiceImpl extends BaseServiceImpl<RoomCost> implements RoomCostService {

    @Resource
    public void setRoomCostDao(RoomCostDao dao) {
        this.dao = dao;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomCostServiceImpl.class);



    @Resource
    private TrainClasstraineeService classtraineeService;


    @Override
    public void timingUpdate() {
        RoomCost roomCost = null;
        TrainClasstrainee trainClasstrainee = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            /*   String json = opuService.GetLiveGuests();*/
            String json = "{\"datas\":[{\"TeamNo\":\"F010144\",\"list\":[{\"GuestNo\":10827,\"RoomNo\":\"1421\",\"Name\":\"王大友2\",\"CheckinDate\":\"2018-09-25\",\"CheckoutDate\":\"2018-10-25\",\"GuestType\":0,\"CheckinTime\":\"2018-10-11 15:24:11\",\"Rent\":0,\"Meal\":0,\"Compensate\":0,\"OtherSpend\":0,\"SpendTotal\":0,\"Cash\":0,\"Deposit\":0,\"CreditCard\":0,\"OnAccount\":0,\"MemberCard\":0,\"OtherPay\":0,\"PayTotal\":0,\"Surplus\":0,\"StudentNo\":\"201709005\"}]}],\"rspCode\":0,\"rspMsg\":\"成功\"}";
            Map<String, Object> maps = JSON.parseObject(json);
            if (!"0".equals(maps.get("rspCode"))) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) maps.get("datas");
                for (Map<String, Object> m : list) {
                    String TeamNo = m.get("TeamNo").toString().substring(2, m.get("TeamNo").toString().length());
                    String sql = "select * from CLASS_RESERVATION_NUMBER where reservationid = '" + TeamNo + "'";
                    List<Map<String, Object>> classNumber = classtraineeService.getForValuesToSql(sql);
                    if (classNumber.isEmpty()) {
                        LOGGER.info("团号" + TeamNo + "无更新数据");
                        continue;
                    }
                    List<Map<String, Object>> listMap = (List<Map<String, Object>>) m.get("list");
                    for (Map<String, Object> lm : listMap) {
                        roomCost = this.getByProerties(new String[]{"isDelete", "studentno", "classId"}, new Object[]{0, lm.get("Studentno"), listMap.get(0).get("classid")});
                        if (null == roomCost) {
                            trainClasstrainee = classtraineeService.getForValue("from TrainClasstrainee where isDelete <> 1 and traineeNumber = ? and classId = ? ", lm.get("StudentNo"), classNumber.get(0).get("classid"));
                            if (null == trainClasstrainee) {
                                continue;
                            }
                            roomCost = new RoomCost();
                            roomCost.setTeamNo(m.get("TeamNo").toString());
                            roomCost.setGuestNo(Integer.parseInt(lm.get("GuestNo").toString()));
                            roomCost.setRoomNo(lm.get("RoomNo").toString());
                            roomCost.setName(lm.get("Name").toString());
                            roomCost.setStudentno(lm.get("StudentNo").toString());
                            roomCost.setUserId(trainClasstrainee.getUuid());
                            roomCost.setClassId(trainClasstrainee.getClassId());
                            roomCost.setVersion(0);
                            roomCost.setCreateTime(new Date());
                            roomCost.setCreateUser("后台定时创建");

                        }
                        roomCost.setCheckinDate(format.parse(lm.get("CheckinDate").toString()));
                        roomCost.setCheckoutDate(format.parse(lm.get("CheckoutDate").toString()));
                        roomCost.setGuestType(Integer.parseInt(lm.get("GuestType").toString()));
                        roomCost.setCheckinTime(formatter.parse(lm.get("CheckinTime").toString()));
                        roomCost.setRent(new BigDecimal(lm.get("Rent").toString()));
                        roomCost.setMeal(new BigDecimal(lm.get("Meal").toString()));
                        roomCost.setCompensate(new BigDecimal(lm.get("Compensate").toString()));
                        roomCost.setOtherspend(new BigDecimal(lm.get("OtherSpend").toString()));
                        roomCost.setSpendtotal(new BigDecimal(lm.get("SpendTotal").toString()));
                        roomCost.setCash(Integer.parseInt(lm.get("Cash").toString()));
                        roomCost.setDeposit(new BigDecimal(lm.get("Deposit").toString()));
                        roomCost.setCreditcard(Integer.parseInt(lm.get("CreditCard").toString()));
                        roomCost.setOnaccount(Integer.parseInt(lm.get("OnAccount").toString()));
                        roomCost.setMembercard(Integer.parseInt(lm.get("MemberCard").toString()));
                        roomCost.setOtherpay(new BigDecimal(lm.get("OtherPay").toString()));
                        roomCost.setPaytotal(new BigDecimal(lm.get("PayTotal").toString()));
                        roomCost.setSurplus(new BigDecimal(lm.get("Surplus").toString()));
                        roomCost.setUpdateTime(new Date());
                        roomCost.setUpdateUser("后台定时修改");

                        this.doMerge(roomCost);
                    }
                }
            } else {
                LOGGER.error(maps.get("rspMsg").toString());
            }
            LOGGER.info(json);
        } catch (Exception e) {
            LOGGER.error("定时更新学员住宿费" + e.getMessage());
        }
    }
}