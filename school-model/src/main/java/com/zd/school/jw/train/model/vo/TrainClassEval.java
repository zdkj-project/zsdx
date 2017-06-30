package com.zd.school.jw.train.model.vo;

import com.zd.core.annotation.FieldInfo;

/**
 * Created by luoyibo on 2017-06-20.
 * 班级评价的实体类
 */
public class TrainClassEval {

    @FieldInfo(name = "班级ID")
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @FieldInfo(name = "班级类型")
    private String classCategory;

    public String getClassCategory() {
        return classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    @FieldInfo(name = "班级名称")
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @FieldInfo(name = "开始日期")
    private String beginDate;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    @FieldInfo(name = "结束日期")
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    @FieldInfo(name = "培训天数")
    private Integer trainDays;

    public Integer getTrainDays() {
        return trainDays;
    }

    public void setTrainDays(Integer trainDays) {
        this.trainDays = trainDays;
    }

    @FieldInfo(name = "主办单位")
    private String holdUnit;

    public String getHoldUnit() {
        return holdUnit;
    }

    public void setHoldUnit(String holdUnit) {
        this.holdUnit = holdUnit;
    }

    @FieldInfo(name = "承办单位")
    private String undertaker;

    public String getUndertaker() {
        return undertaker;
    }

    public void setUndertaker(String undertaker) {
        this.undertaker = undertaker;
    }

    @FieldInfo(name = "很满意度")
    private String verySatisfaction;

    public String getVerySatisfaction() {
        return verySatisfaction;
    }

    public void setVerySatisfaction(String verySatisfaction) {
        this.verySatisfaction = verySatisfaction;
    }

    @FieldInfo(name = "满意度")
    private String satisfaction;

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    @FieldInfo(name = "培训人数")
    private Integer trainees;

    public Integer getTrainees() {
        return trainees;
    }

    public void setTrainees(Integer trainees) {
        this.trainees = trainees;
    }

    @FieldInfo(name = "班主任")
    private String bzr;

    public String getBzr() {
        return bzr;
    }

    public void setBzr(String bzr) {
        this.bzr = bzr;
    }
    @FieldInfo(name = "联系电话")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @FieldInfo(name = "意见与建议")
    private String advise;

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }
}
