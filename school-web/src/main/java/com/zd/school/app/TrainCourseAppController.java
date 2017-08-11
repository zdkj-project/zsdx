package com.zd.school.app;

import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.school.jw.model.app.CourseCheckApp;
import com.zd.school.jw.model.app.TrainCourseApp;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.model.vo.VoTrainClasstrainee;
import com.zd.school.jw.train.service.*;
import com.zd.school.oa.terminal.model.OaInfoterm;
import com.zd.school.oa.terminal.service.OaInfotermService;
import com.zd.school.plartform.comm.model.CommAttachment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/app/traincourse/")
public class TrainCourseAppController {

    @Resource
    private TrainClassscheduleService courseService;
    @Resource
    private TrainClasstraineeService traineeService;
    @Resource
    private TrainClasstraineeService classtraineeService;
    @Resource
    private OaInfotermService termService;
    @Resource
    private TrainCheckruleService ruleService;
    @Resource
    private TrainCourseattendService attendService;
    @Resource
    private TrainClassService classService;


    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    TrainCourseApp courselist(String termCode, String time, HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ParseException {
        //传过来的时间为yyyy-MM-dd HH：mm：ss
        TrainCourseApp tca = new TrainCourseApp();
        try {
            //判断时间查询上午下午
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String s = dateFormat.format(date);
            Date date2 = sdf.parse(s + " 12:00:00");
            Date date3 = sdf.parse(s + " 18:00:00");
            Date date4 = sdf.parse(s + " 23:59:59");
            //设备号查询房间
            OaInfoterm roomTerm = termService.getByProerties("termCode", termCode);
            List<VoTrainClasstrainee> volist = null;
            VoTrainClasstrainee vo = null;
            List<TrainClassschedule> course = null;
            List<Object[]> obj = null;

            if (ModelUtil.isNotNull(roomTerm)) {
                if (date.getTime() < date2.getTime()) {
//                    course = courseService.doQuery("from TrainClassschedule where scheduleAddress='" + roomTerm.getRoomName() + "' and beginTime Between '" + s + " 06:00:00' And '" + sdf.format(date2) + "' order by beginTime asc");
                    course = courseService.doQuery("from TrainClassschedule where scheduleAddress='" + roomTerm.getRoomName() + "' and beginTime Between '" +sdf.format(date) + "' And '" + sdf.format(date2) + "' order by beginTime asc");
                } else if (date.getTime() < date3.getTime()) {
                    course = courseService.doQuery("from TrainClassschedule where scheduleAddress='" + roomTerm.getRoomName() + "' and beginTime Between '" +sdf.format(date) + "' And '" + sdf.format(date3) + "' order by beginTime asc");
                } else {
                    course = courseService.doQuery("from TrainClassschedule where scheduleAddress='" + roomTerm.getRoomName() + "' and beginTime Between '" +sdf.format(date) + "' And '" + sdf.format(date4) + "' order by beginTime asc");
                }
                if (course.size() > 0) {
                    //同一教室，同一时间段只可能有一个培训班
                    String classId = course.get(0).getClassId();
                    TrainClass classInfo = classService.get(classId);
                    String sql = MessageFormat.format(" SELECT CLASS_ID AS classId,xm,CLASS_TRAINEE_ID AS traineeId," +
                            "CONVERT(VARCHAR(36),(ISNULL((SELECT top 1 a.FACT_NUMB FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order BY a.CREATE_TIME desc),''0''))) AS factoryfixId," +
                            "CONVERT(VARCHAR(36),(isnull((SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=TRAIN_T_CLASSTRAINEE.CLASS_TRAINEE_ID order by a.CREATE_TIME desc),''0''))) AS cardNo " +
                            " FROM dbo.TRAIN_T_CLASSTRAINEE WHERE ISDELETE=0 and  CLASS_ID=''{0}''",classId);
                    volist = classService.doQuerySqlObject(sql, VoTrainClasstrainee.class);
                    for (TrainClassschedule c : course) {
                        c.setList(volist);
                    }

                    TrainCheckrule checkrule = ruleService.get(classInfo.getCheckruleId());
                    tca.setCheckrule(checkrule);

                    sql = MessageFormat.format("SELECT ATTACH_NAME AS attachName,ATTACH_URL AS attachUrl FROM dbo.BASE_T_ATTACHMENT WHERE ENTITY_NAME=''TrainClass'' AND RECORD_ID=''{0}''", classId);
                    List<CommAttachment> attachmentList = classService.doQuerySqlObject(sql, CommAttachment.class);
                    tca.setAttachment(attachmentList);

                } else {
                    tca.setCode(false);
                    tca.setMessage("数据异常调用失败,没有对应时间的课程");
                    return tca;
                }
            }
            tca.setCode(true);
            tca.setMessage("调用成功");
            tca.setCourse(course);
            return tca;


        } catch (Exception e) {
            tca.setCode(false);
            tca.setMessage("数据异常调用失败" + e.getMessage());
            return tca;
        }

    }

    @RequestMapping(value = {"/rule"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    TrainCheckrule rule(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ParseException {
        TrainCheckrule rule = ruleService.getByProerties("startUsing", (short) 1);

        return rule;

    }

    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public @ResponseBody
    CourseCheckApp update(String coursecheck, HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ParseException {
        List<CourseCheck> check = null;
        if (null != coursecheck) {
            check = (List<CourseCheck>) JsonBuilder.getInstance().fromJsonArray(coursecheck,
                    CourseCheck.class);
        }
        CourseCheckApp cca = new CourseCheckApp();

        TrainCourseattend attend = null;
        TrainClassschedule course = null;
        try {
            if(coursecheck.length()>0) {
                for (CourseCheck t : check) {
  /*              String uid = "";
                List<Object[]> obj = termService.ObjectQuerySql("select USER_ID,CARDNO from dbo.PT_CARD where FACTORYFIXID='" + t.getWlkh() + "'");
                uid = obj.get(0)[0].toString();*/

                    course = courseService.get(t.getCourseId());

                    String[] param = {"classId", "classScheduleId", "traineeId"};
                    Object[] values = {t.getClassId(), t.getCourseId(), t.getUserId()};
//                Object[] values = {t.getClassId(), t.getCourseId(), uid};
                    attend = attendService.getByProerties(param, values);
                    if(attend==null)
                        attend = new TrainCourseattend();
                   // if (attend != null) {
                        attend.setClassId(t.getClassId());
                        attend.setClassScheduleId(t.getCourseId());
                        attend.setTraineeId(t.getUserId());
                        attend.setBeginTime(course.getBeginTime());
                        attend.setEndTime(course.getEndTime());
                        if (t.getLg().equals("0")) {
                            attend.setIncardTime(t.getTime());
                        } else {
                            attend.setOutcardTime(t.getTime());
                        }
                        attend.setAttendResult(t.getAttendResult());
                        attendService.merge(attend);
    /*                } else {
                        cca.setCode(false);
                        cca.setMessage("查询不到对应的考勤人员");
                        return cca;
                    }*/

                }
                cca.setCode(true);
                cca.setMessage("存储数据成功");
                return cca;
            } else{
                cca.setCode(false);
                cca.setMessage("没有上传考勤数据");
                return cca;
            }

        } catch (Exception e) {
            cca.setCode(false);
            cca.setMessage("存储异常" + e.getMessage());
            return cca;
        }


    }

}