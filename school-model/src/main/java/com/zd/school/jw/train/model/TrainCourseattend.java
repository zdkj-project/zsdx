package com.zd.school.jw.train.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ClassName: TrainCourseattend 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 课程考勤刷卡结果(TRAIN_T_COURSEATTEND)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_COURSEATTEND")
@AttributeOverride(name = "uuid", column = @Column(name = "CARDSERIA_ID", length = 36, nullable = false))
public class TrainCourseattend extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "班级ID")
    @Column(name = "CLASS_ID", length = 36, nullable = false)
    private String classId;
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getClassId() {
        return classId;
    }
        
    @FieldInfo(name = "日程ID")
    @Column(name = "CLASS_SCHEDULE_ID", length = 36, nullable = false)
    private String classScheduleId;
    public void setClassScheduleId(String classScheduleId) {
        this.classScheduleId = classScheduleId;
    }
    public String getClassScheduleId() {
        return classScheduleId;
    }
        
    @FieldInfo(name = "学员ID")
    @Column(name = "TRAINEE_ID", length = 36, nullable = false)
    private String traineeId;
    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }
    public String getTraineeId() {
        return traineeId;
    }
        
    @FieldInfo(name = "开始时间")
    @Column(name = "BEGIN_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date beginTime;
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getBeginTime() {
        return beginTime;
    }
        
    @FieldInfo(name = "结束时间")
    @Column(name = "END_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date endTime;
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return endTime;
    }
        
    @FieldInfo(name = "签到刷卡时间")
    @Column(name = "INCARD_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date incardTime;
    public void setIncardTime(Date incardTime) {
        this.incardTime = incardTime;
    }
    public Date getIncardTime() {
        return incardTime;
    }
        
    @FieldInfo(name = "签退刷卡时间")
    @Column(name = "OUTCARD_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date outcardTime;
    public void setOutcardTime(Date outcardTime) {
        this.outcardTime = outcardTime;
    }
    public Date getOutcardTime() {
        return outcardTime;
    }

    //考勤结果 1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
    @FieldInfo(name = "考勤结果")
    @Column(name = "ATTENT_RESULT",length = 16,nullable = true)
    private String attendResult;

    public String getAttendResult() {
        return attendResult;
    }

    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}