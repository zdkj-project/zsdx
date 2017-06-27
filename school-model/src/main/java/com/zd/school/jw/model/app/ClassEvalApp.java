package com.zd.school.jw.model.app;

import com.zd.core.annotation.FieldInfo;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import com.zd.school.jw.train.model.vo.TrainClassEval;

import java.util.List;

/**
 * Created by luoyibo on 2017-06-20.
 * 返回给APP的班级评价实体
 */
public class ClassEvalApp {
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

    @FieldInfo(name = "班级信息")
    private TrainClassEval evalClass;

    public TrainClassEval getEvalClass() {
        return evalClass;
    }

    public void setEvalClass(TrainClassEval evalClass) {
        this.evalClass = evalClass;
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
