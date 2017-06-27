package com.zd.school.jw.model.app;

import com.zd.core.annotation.FieldInfo;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.model.vo.TrainClassCourseEval;

import java.util.List;

/**
 * Created by luoyibo on 2017-06-20.
 * 返回给APP使用的课程评价的实体类
 */
public class CourseEvalApp {
    @FieldInfo(name = "返回状态")
    private Boolean success = false;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @FieldInfo(name = "返回消息")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @FieldInfo(name = "课程信息")
    private TrainClassCourseEval evalCourse;

    public TrainClassCourseEval getEvalCourse() {
        return evalCourse;
    }

    public void setEvalCourse(TrainClassCourseEval evalCourse) {
        this.evalCourse = evalCourse;
    }

    @FieldInfo(name = "评价指标及标准")
    private List<TrainIndicatorStand> evalStand;

    public List<TrainIndicatorStand> getEvalStand() {
        return evalStand;
    }

    public void setEvalStand(List<TrainIndicatorStand> evalStand) {
        this.evalStand = evalStand;
    }
}
