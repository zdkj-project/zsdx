package com.zd.school.jw.model.app;

import com.zd.school.jw.train.model.TrainClassschedule;

import java.util.List;

public class TrainCourseApp {
    //判断是否调用成功，true为调用成功,false为失败。默认为false
    private boolean code = false;


    public boolean getCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }


    //返回信息
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    //判断返回的课程，是否同属于一个班级，相同为0，否则为1
    private int isSameClass;

    public int getIsSameClass() {
        return isSameClass;
    }

    public void setIsSameClass(int isSameClass) {
        this.isSameClass = isSameClass;
    }
    
    //返回房间名称信息
    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    /**
     * 考勤课程列表
     */
    private List<TrainClassschedule> course;

    public List<TrainClassschedule> getCourse() {
        return course;
    }

    public void setCourse(List<TrainClassschedule> course) {
        this.course = course;
    }

    /**
     * 课程考勤规则
     */
//    private TrainCheckrule checkrule;
//
//    public TrainCheckrule getCheckrule() {
//        return checkrule;
//    }
//
//    public void setCheckrule(TrainCheckrule checkrule) {
//        this.checkrule = checkrule;
//    }


    /**
     * APP需要的会议相关附件下载
     */
//    private List<CommAttachment> attachment;
//
//    public List<CommAttachment> getAttachment() {
//        return attachment;
//    }
//
//    public void setAttachment(List<CommAttachment> attachment) {
//        this.attachment = attachment;
//    }
}
