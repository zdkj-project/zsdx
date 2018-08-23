package com.zd.school.jw.train.model.vo;

import com.zd.core.annotation.FieldInfo;

import java.util.Date;

public class DClassVo {

 public String opno;

 public String opact;

 public Date optime;

 public String classid;

 public String classname;

 public  String classtype;

 public  String headmaster;

 public  Date satrtdate;

 public Date enddate;

    public String getOpno() {
        return opno;
    }

    public void setOpno(String opno) {
        this.opno = opno;
    }

    public String getOpact() {
        return opact;
    }

    public void setOpact(String opact) {
        this.opact = opact;
    }

    public Date getOptime() {
        return optime;
    }

    public void setOptime(Date optime) {
        this.optime = optime;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getHeadmaster() {
        return headmaster;
    }

    public void setHeadmaster(String headmaster) {
        this.headmaster = headmaster;
    }

    public Date getSatrtdate() {
        return satrtdate;
    }

    public void setSatrtdate(Date satrtdate) {
        this.satrtdate = satrtdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
}
