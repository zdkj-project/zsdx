package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.excel.annotation.MapperCell;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * ClassName: TrainTrainee 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学员信息(TRAIN_T_TRAINEE)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_TRAINEE")
@AttributeOverride(name = "uuid", column = @Column(name = "TRAINEE_ID", length = 36, nullable = false))
public class TrainTrainee extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @MapperCell(cellName="姓名",order=1)
    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 64, nullable = false)
    private String xm;
    public void setXm(String xm) {
        this.xm = xm;
    }
    public String getXm() {
        return xm;
    }

    @FieldInfo(name = "性别")
    @Column(name = "XBM", length = 1, nullable = false)
    private String xbm;
    public void setXbm(String xbm) {
        this.xbm = xbm;
    }
    public String getXbm() {
        return xbm;
    }

    @FieldInfo(name = "民族")
    @Column(name = "MZM", length = 16, nullable = true)
    private String mzm;
    public void setMzm(String mzm) {
        this.mzm = mzm;
    }
    public String getMzm() {
        return mzm;
    }

    @MapperCell(cellName="所在单位",order=10,width = 40)
    @FieldInfo(name = "所在单位")
    @Column(name = "WORK_UNIT", length = 128, nullable = true)
    private String workUnit;
    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }
    public String getWorkUnit() {
        return workUnit;
    }

    @FieldInfo(name = "政治面貌 ZZMMM字典")
    @Column(name = "ZZMMM", length = 16, nullable = true)
    private String zzmmm;
    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }
    public String getZzmmm() {
        return zzmmm;
    }

    @MapperCell(cellName="职务",order=4)
    @FieldInfo(name = "职务")
    @Column(name = "POSITION", length = 128, nullable = true)
    private String position;
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition() {
        return position;
    }

    @FieldInfo(name = "行政级别 HEADSHIPLEVEL字典")
    @Column(name = "HEADSHIP_LEVEL", length = 16, nullable = true)
    private String headshipLevel;
    public void setHeadshipLevel(String headshipLevel) {
        this.headshipLevel = headshipLevel;
    }
    public String getHeadshipLevel() {
        return headshipLevel;
    }

    @MapperCell(cellName="身份证件号",order=7,width = 25)
    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 20, nullable = true)
    private String sfzjh;
    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }
    public String getSfzjh() {
        return sfzjh;
    }

    @MapperCell(cellName="手机号码",order=8,width = 20)
    @FieldInfo(name = "移动电话")
    @Column(name = "MOBILE_PHONE", length = 36, nullable = false)
    private String mobilePhone;
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public String getMobilePhone() {
        return mobilePhone;
    }
        
    @FieldInfo(name = "照片")
    @Column(name = "ZP", length = 128, nullable = true)
    private String zp;
    public void setZp(String zp) {
        this.zp = zp;
    }
    public String getZp() {
        return zp;
    }

    @FieldInfo(name = "学历,国标代码")
    @Column(name = "XLM", length = 16, nullable = true)
    private String xlm;
    public void setXlm(String xlm) {
        this.xlm = xlm;
    }
    public String getXlm() {
        return xlm;
    }

    @MapperCell(cellName="专业",order=14)
    @FieldInfo(name = "专业")
    @Column(name = "ZYM", length = 32, nullable = true)
    private String zym;
    public void setZym(String zym) {
        this.zym = zym;
    }
    public String getZym() {
        return zym;
    }

    @FieldInfo(name = "学位,XWM字典")
    @Column(name = "XWM", length = 16, nullable = true)
    private String xwm;
    public void setXwm(String xwm) {
        this.xwm = xwm;
    }
    public String getXwm() {
        return xwm;
    }

    @MapperCell(cellName="毕业学校",order=15,width = 40)
    @FieldInfo(name = "毕业学校")
    @Column(name = "GRADUATE_SCHOOL", length = 128, nullable = true)
    private String graduateSchool;
    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }
    public String getGraduateSchool() {
        return graduateSchool;
    }

    @MapperCell(cellName="电子邮件",order=16)
    @FieldInfo(name = "电子邮件")
    @Column(name = "DZXX", length = 32, nullable = true)
    private String dzxx;
    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }
    public String getDzxx() {
        return dzxx;
    }

    @MapperCell(cellName="通讯地址",order=17,width = 40)
    @FieldInfo(name = "通讯地址")
    @Column(name = "ADDRESS", length = 64, nullable = true)
    private String address;
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    @MapperCell(cellName="党校培训证书号",order=18)
    @FieldInfo(name = "党校培训证书号")
    @Column(name = "PARTY_SCHOOL_NUMB", length = 16, nullable = true)
    private String partySchoolNumb;
    public void setPartySchoolNumb(String partySchoolNumb) {
        this.partySchoolNumb = partySchoolNumb;
    }
    public String getPartySchoolNumb() {
        return partySchoolNumb;
    }

    @MapperCell(cellName="行院培训证书号",order=19)
    @FieldInfo(name = "行院培训证书号")
    @Column(name = "NATIONAL_SCHOOL_NUMB", length = 16, nullable = true)
    private String nationalSchoolNumb;
    public void setNationalSchoolNumb(String nationalSchoolNumb) {
        this.nationalSchoolNumb = nationalSchoolNumb;
    }
    public String getNationalSchoolNumb() {
        return nationalSchoolNumb;
    }

    @FieldInfo(name = "学员类型")
    @Column(name = "TRAINEE_CATEGORY")
    private  String traineeCategory;

    public String getTraineeCategory() {
        return traineeCategory;
    }

    public void setTraineeCategory(String traineeCategory) {
        this.traineeCategory = traineeCategory;
    }
    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
    @MapperCell(cellName = "性别", order = 2)
    @Transient
    @FieldInfo(name = "性别")
    private String xbmName;

    public String getXbmName() {
        return xbmName;
    }

    public void setXbmName(String xbmName) {
        this.xbmName = xbmName;
    }

    @MapperCell(cellName="民族",order=3)
    @Transient
    @FieldInfo(name = "民族")
    private String mzmName;

    public String getMzmName() {
        return mzmName;
    }

    public void setMzmName(String mzmName) {
        this.mzmName = mzmName;
    }
    @MapperCell(cellName="行政级别",order=5)
    @Transient
    @FieldInfo(name = "行政级别")
    private String headshipLevelName;

    public String getHeadshipLevelName() {
        return headshipLevelName;
    }

    public void setHeadshipLevelName(String headshipLevelName) {
        this.headshipLevelName = headshipLevelName;
    }

    @MapperCell(cellName="政治面貌 ",order=12)
    @FieldInfo(name = "政治面貌")
    @Transient
    private String zzmmmName;

    public String getZzmmmName() {
        return zzmmmName;
    }

    public void setZzmmmName(String zzmmmName) {
        this.zzmmmName = zzmmmName;
    }

    @MapperCell(cellName="学历",order=13)
    @FieldInfo(name = "学历")
    @Transient
    private String xlmName;

    public String getXlmName() {
        return xlmName;
    }

    public void setXlmName(String xlmName) {
        this.xlmName = xlmName;
    }


    @MapperCell(cellName="学位",order=14)
    @FieldInfo(name = "学位")
    @Transient
    private String xwmName;

    public String getXwmName() {
        return xwmName;
    }

    public void setXwmName(String xwmName) {
        this.xwmName = xwmName;
    }

    @MapperCell(cellName="学员类别",order=11)
    @FieldInfo(name = "学员类型")
    @Transient
    private String traineeCategoryName;

    public String getTraineeCategoryName() {
        return traineeCategoryName;
    }

    public void setTraineeCategoryName(String traineeCategoryName) {
        this.traineeCategoryName = traineeCategoryName;
    }
}