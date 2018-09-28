package com.zd.openinterface.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.service.*;
import com.zd.school.te.TeQrCodeForApp;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 扫码考勤接口
 * @author: yz
 * @date: 2018-09-25
 * @time: 14:34
 */
@RestController
@RequestMapping("/checkClass")
public class CheckClassController {

    private static Logger logger = Logger.getLogger(CheckClassController.class);

    private ValueFilter valueFilter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (value == null) {
                return "";
            }
            return value;
        }
    };

    @Resource
    private TrainClasstraineeService classtraineeService;

    @Resource
    private TrainClassscheduleService trainClassscheduleService;

    @Resource
    private TrainCourseattendService trainCourseattendService;

    @Resource
    private TrainClassService trainClassService;

    @Resource
    private TrainCheckruleService trainCheckruleService;

    /**
     * @param userId  学员ID
     * @param classId 班级ID
     * @return java.lang.String
     * @description
     * @author yz
     * @date 2018/9/25 15:26
     * @method signIn
     */
    @RequestMapping(value = "/signIn/{classId}/{userId}", produces = "application/json; charset=utf-8")
    public String signIn(@PathVariable String userId, @PathVariable String classId) {
        TeQrCodeForApp result = new TeQrCodeForApp();
        String message = "签到成功";
        try {
            String hql = "from TrainClasstrainee where classId = ? and uuid = ? and isDelete <> 1";
            List<TrainClasstrainee> traineeList = classtraineeService.getForValues(hql, classId, userId);
            if (traineeList.isEmpty()) {
                return jsonStr("改班级没有此人!");
            }

            TrainClass trainClass = trainClassService.getByProerties(new String[]{"uuid", "isDelete"}, new Object[]{classId, 0});
            if (null == trainClass) {
                return jsonStr("该班级已经被注销不存在!");
            } else if (StringUtils.isEmpty(trainClass.getCheckruleId())) {
                return jsonStr("该班级无需考勤!");
            }

            //获取班级考勤规则
            TrainCheckrule trainCheckrule = trainCheckruleService.get(trainClass.getCheckruleId());
            if (trainCheckrule.getStartUsing() == 0) {
                return jsonStr("考勤规则没有启用!");
            }

            Date date = new Date();
            SimpleDateFormat se = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //获取当前时间判断是 上午还是下午
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String sqlChedule = "select * from TRAIN_T_CLASSSCHEDULE where class_Id = '" + classId + "' and isDelete <>1 and CONVERT(varchar(100), begin_Time, 23) = CONVERT(varchar(100), GETDATE(), 23)";
            List<Map<String, Object>> listChedule;
            if (hour >= 13) {
                sqlChedule += " and begin_Time < cast(substring(CONVERT(varchar(100), GETDATE(), 23)+' 23:00:00:000',1,19) as datetime) and END_TIME > cast(substring(CONVERT(varchar(100), GETDATE(), 23)+' 13:00:00:000',1,19) as datetime) order by begin_Time";
                listChedule = trainClassscheduleService.getForValuesToSql(sqlChedule);
            } else {
                sqlChedule += " and END_TIME < cast(substring(CONVERT(varchar(100), GETDATE(), 23)+' 13:00:00:000',1,19) as datetime) order by begin_Time";
                listChedule = trainClassscheduleService.getForValuesToSql(sqlChedule);
            }
            //考勤模式 1-按节次考勤 2-按半天考勤 3-按全天考勤
            if (1 == trainCheckrule.getCheckMode()) {
                TrainCourseattend trainCourseattend;
                for (int i = 0; i < listChedule.size(); i++) {
                    //查看该课程有没有签到
                    trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, listChedule.get(i).get("CLASS_SCHEDULE_ID"), userId, 0});
                    if (null == trainCourseattend) {
                        //当前时间大于改课程结束时间不予签到
                        if (date.after(se.parse(listChedule.get(i).get("END_TIME").toString()))) {
                            continue;
                        } else {
                            long diff = se.parse(listChedule.get(0).get("BEGIN_TIME").toString()).getTime() - System.currentTimeMillis();
                            int days = (int) diff / (1000 * 60);
                            if (days < 0) {
                                //迟到签到  //考勤结果 1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                                if (Math.abs(days) > trainCheckrule.getBeLate() && Math.abs(days) < trainCheckrule.getAbsenteeism()) {
                                    message = "签到成功--迟到";
                                    trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "2", trainCheckrule.getNeedCheckout(), date);
                                } else if (Math.abs(days) > trainCheckrule.getAbsenteeism()) {
                                    message = "签到成功--缺勤";
                                    trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "4", trainCheckrule.getNeedCheckout(), date);
                                }
                            } else if (days > trainCheckrule.getInBefore()) {
                                return jsonStr("当前时间不能签到!还差" + days + "分钟");
                            } else {
                                //正常签到
                                trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "1", trainCheckrule.getNeedCheckout(), date);
                            }
                        }
                    } else {
                        //当前时间大于改课程结束时间不予签到
                        if (date.after(se.parse(listChedule.get(i).get("END_TIME").toString()))) {
                            continue;
                        } else {
                            //不需要签退
                            if (1 == trainCheckrule.getNeedCheckout()) {
                                //获取最后一门课程的结束时间
                                long diff = se.parse(listChedule.get(i).get("END_TIME").toString()).getTime() - System.currentTimeMillis();
                                int days = (int) diff / (1000 * 60);
                                if (days > trainCheckrule.getOutBefore()) {
                                    return jsonStr("当前时间不能签退!还差" + days + "分钟");
                                } else {
                                    trainCourseattendService.updateTrainCourseattend(listChedule, classId, userId, date);
                                    message = "签退成功";
                                }
                            } else {
                                return jsonStr("你已经签到,该课程不需要签退!");
                            }
                        }
                    }
                }
            } else if (2 == trainCheckrule.getCheckMode()) {
                if (listChedule.isEmpty()) {
                    return jsonStr("该班级今天" + (hour > 12 ? "下午" : "上午") + "没有课程,无需考勤!");
                }
                TrainCourseattend trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, listChedule.get(0).get("CLASS_SCHEDULE_ID"), userId, 0});
                //等于空就是 今天还没签到
                if (null == trainCourseattend) {
                    long diff = se.parse(listChedule.get(0).get("BEGIN_TIME").toString()).getTime() - System.currentTimeMillis();
                    int days = (int) diff / (1000 * 60);
                    if (days < 0) {
                        //迟到签到  //考勤结果 1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                        if (Math.abs(days) > trainCheckrule.getBeLate() && Math.abs(days) < trainCheckrule.getAbsenteeism()) {
                            trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "2", trainCheckrule.getNeedCheckout(), date);
                            message = "签到成功--迟到";
                        } else if (Math.abs(days) > trainCheckrule.getAbsenteeism()) {
                            trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "4", trainCheckrule.getNeedCheckout(), date);
                            message = "签到成功--缺勤";
                        }
                    } else if (days > trainCheckrule.getInBefore()) {
                        return jsonStr("当前时间不能签到!还差" + days + "分钟");
                    } else {
                        //正常签到
                        trainCourseattendService.installTrainCourseattend(listChedule, classId, userId, "1", trainCheckrule.getNeedCheckout(), date);
                    }
                } else {
                    //不需要签退
                    if (0 == trainCheckrule.getNeedCheckout()) {
                        return jsonStr("你已经签到,该课程不需要签退!");
                    } else {
                        //获取最后一门课程的结束时间
                        long diff = se.parse(listChedule.get(listChedule.size() - 1).get("END_TIME").toString()).getTime() - System.currentTimeMillis();
                        int days = (int) diff / (1000 * 60);
                        if (days > trainCheckrule.getOutBefore()) {
                            return jsonStr("当前时间不能签退!还差" + days + "分钟");
                        } else {
                            trainCourseattendService.updateTrainCourseattend(listChedule, classId, userId, date);
                            message = "签退成功";
                        }
                    }
                }
            } else {
                //查询这一天的所有课程
                String hqlChedule = "from TrainClassschedule where classId = ? and isDelete <>1 and CONVERT(varchar(100), beginTime, 23) = CONVERT(varchar(100), GETDATE(), 23)  order by beginTime";
                List<TrainClassschedule> listChedules = trainClassscheduleService.getForValues(hqlChedule, classId);
                if (listChedules.isEmpty()) {
                    return jsonStr("该班级今天没有课程,无需考勤!");
                }

                TrainCourseattend trainCourseattend = trainCourseattendService.getByProerties(new String[]{"classId", "classScheduleId", "traineeId", "isDelete"}, new Object[]{classId, listChedules.get(0).getUuid(), userId, 0});
                //等于空就是 今天还没签到
                if (null == trainCourseattend) {
                    long diff = se.parse(listChedules.get(0).getBeginTime().toString()).getTime() - System.currentTimeMillis();
                    int days = (int) diff / (1000 * 60);
                    if (days > trainCheckrule.getInBefore()) {
                        return jsonStr("当前时间不能签到!还差" + days + "分钟");
                    } else if (days < 0) {
                        //迟到签到  //考勤结果 1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                        if (Math.abs(days) > trainCheckrule.getBeLate() && Math.abs(days) < trainCheckrule.getAbsenteeism()) {
                            trainCourseattendService.installTrainCourseattends(listChedules, classId, userId, "2", trainCheckrule.getNeedCheckout(), date);
                            message = "签到成功--迟到";
                        } else if (Math.abs(days) > trainCheckrule.getAbsenteeism()) {
                            trainCourseattendService.installTrainCourseattends(listChedules, classId, userId, "4", trainCheckrule.getNeedCheckout(), date);
                            message = "签到成功--缺勤";
                        }
                    } else {
                        //正常签到
                        trainCourseattendService.installTrainCourseattends(listChedules, classId, userId, "1", trainCheckrule.getNeedCheckout(), date);
                    }
                } else {
                    //不需要签退
                    if (0 == trainCheckrule.getNeedCheckout()) {
                        return jsonStr("你已经签到,该课程不需要签退!");
                    } else {
                        //获取最后一门课程的结束时间
                        long diff = se.parse(listChedules.get(listChedules.size() - 1).getEndTime().toString()).getTime() - System.currentTimeMillis();
                        int days = (int) diff / (1000 * 60);
                        if (days > trainCheckrule.getOutBefore()) {
                            return jsonStr("当前时间不能签退!还差" + days + "分钟");
                        } else {
                            trainCourseattendService.updateTrainCourseattends(listChedules, classId, userId, date);
                            message = "签退成功";
                        }
                    }
                }
            }

            result.setMessage(message);
            result.setData(new ArrayList<>());
            result.setResult(true);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setData(new ArrayList<>());
            result.setResult(false);
            logger.error(e.getMessage());
            e.printStackTrace();
            return JSON.toJSONString(result);
        }

    }


    private static String jsonStr(String str) {
        TeQrCodeForApp result = new TeQrCodeForApp();
        result.setMessage(str);
        result.setData(new ArrayList<>());
        result.setResult(false);
        return JSON.toJSONString(result);
    }
}
