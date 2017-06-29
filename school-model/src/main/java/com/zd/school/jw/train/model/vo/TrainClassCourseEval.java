package com.zd.school.jw.train.model.vo;

import com.zd.core.annotation.FieldInfo;

/**
 * ClassName: TrainClassCourseEval
 * Function: .
 * Reason: .
 * Description: 培训班级课程的评价信息
 * date: 2017-03-07
 * Created by luoyibo on 2017-06-09.
 */
public class TrainClassCourseEval {

 /*   -      classCategory:班级类别
--      className: 班级名称
--		courseDate:上课日期
--		courseTime:上课时间
--		verySatisfaction:很满意度
--		satisfaction:满意度
--		ranking:排名
--      teacherId:主讲教师id
--      teqcherName:主讲教师姓名
--      courseId:课程的Id
--      courseName:课程名称
        --classId,
        --classCategory,
        --className,
        --courseDate,
        --courseTime,
        --convert(varchar(10),
        --verySatisfaction) as verySatisfaction,
        --convert(varchar(10),
        --satisfaction) as satisfaction,
        --ranking,
        --teacherId,
        --teacherName,
        --courseId,
        --courseName,
        --scheduleId,
        --evalState
    */
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

    @FieldInfo(name = "上课日期")
    private String courseDate;

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    @FieldInfo(name = "上课时间")
    private String courseTime;

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
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

    @FieldInfo(name = "排名")
    private Integer ranking;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    @FieldInfo(name = "主讲教师Id")
    private String teacherId;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @FieldInfo(name = "主讲教师姓名")
    private String teacherName;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @FieldInfo(name = "课程ID")
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @FieldInfo(name = "课程名称")
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @FieldInfo(name = "课程安排ID")
    private String classScheduleId;

    public String getClassScheduleId() {
        return classScheduleId;
    }

    public void setClassScheduleId(String classScheduleId) {
        this.classScheduleId = classScheduleId;
    }

    @FieldInfo(name = "课程类型")
    private String teachTypeName;

    public String getTeachTypeName() {
        return teachTypeName;
    }

    public void setTeachTypeName(String teachTypeName) {
        this.teachTypeName = teachTypeName;
    }

    @FieldInfo(name = "评价状态")
    private Integer evalState;

    public Integer getEvalState() {
        return evalState;
    }

    public void setEvalState(Integer evalState) {
        this.evalState = evalState;
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
